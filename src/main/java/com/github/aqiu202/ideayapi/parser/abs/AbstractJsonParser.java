package com.github.aqiu202.ideayapi.parser.abs;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.PropertyNamingStrategy;
import com.github.aqiu202.ideayapi.constant.SpringWebFluxConstants;
import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiDescriptorFilter;
import com.github.aqiu202.ideayapi.http.filter.PsiDescriptorListFilter;
import com.github.aqiu202.ideayapi.http.res.DocTagValueHandler;
import com.github.aqiu202.ideayapi.http.res.ResponseFieldNameHandler;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.github.aqiu202.ideayapi.parser.ObjectJsonParser;
import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.parser.type.*;
import com.github.aqiu202.ideayapi.util.DesUtils;
import com.github.aqiu202.ideayapi.util.PropertyNamingUtils;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractJsonParser implements ObjectJsonParser, ResponseFieldNameHandler,
        DocTagValueHandler {

    private static final PsiGenericTypeResolver GENERIC_TYPE_RESOLVER = new SimplePsiGenericTypeResolver();

    protected final YApiProjectProperty property;
    protected final Project project;
    protected final boolean notConvertFieldName;

    protected Source source;

    protected AbstractJsonParser(@NotNull YApiProjectProperty property, Project project, boolean notConvertFieldName) {
        this.property = property;
        this.project = project;
        this.notConvertFieldName = notConvertFieldName;
    }

    protected AbstractJsonParser(YApiProjectProperty property, Project project) {
        this(property, project, false);
    }

    public Source getSource() {
        return source;
    }

    public AbstractJsonParser setSource(Source source) {
        this.source = source;
        return this;
    }

    @Override
    public Jsonable parse(PsiClass rootClass, PsiType type, LevelCounter counter) {
        //是否是数组
        if (type instanceof PsiArrayType) {
            return this.parseCollection(rootClass, ((PsiArrayType) type).getComponentType(), counter);
        }
        Jsonable result;
        //如果是基本类型
        if (TypeUtils.isBasicType(type)) {
            result = this.parseBasic(type);
        } else if (TypeUtils.isMap(type)) {
            //对Map及其子类进行处理
            result = this.parseMap(type);
        } else if (TypeUtils.isCollection(type)) {
            //截取子类型
            //如果是集合类型（List Set）
            result = this.parseCollection(rootClass, GENERIC_TYPE_RESOLVER.resolveFirst(type), counter);
        } else {
            PsiType targetType;
            if (StringUtils.equals(SpringWebFluxConstants.Mono, TypeUtils.getTypePkName(type))) {
                targetType = GENERIC_TYPE_RESOLVER.resolveFirst(type);
            } else {
                targetType = type;
            }
            //其他情况 pojo
            result = this.parsePojo(rootClass, targetType, counter);
        }
        return result;
    }

    @Override
    public String handleFieldName(String fieldName) {
        //Row格式的返回值不进行字段名称转换
        if (this.notConvertFieldName) {
            return fieldName;
        }
        return PropertyNamingUtils.convert(fieldName, PropertyNamingStrategy.of(String.
                valueOf(this.property.getStrategy())));
    }

    @Override
    public String getJson(PsiClass rootClass, PsiType psiType) {
        Jsonable jsonable = this.parse(rootClass, psiType, new LevelCounter());
        if (jsonable instanceof ItemJsonSchema) {
            ((ItemJsonSchema) jsonable).set$schema(YApiConstants.$schema);
        }
        return jsonable.toJson();
    }

    @Override
    public PsiDescriptorFilter getPsiDescriptorFilter() {
        return (f, c) ->
                !DeprecatedAssert.instance.isDeprecated(f.getOrigin())
                        && !YApiSupportHolder.supports.isIgnored(f.getOrigin(), f.getParent());
    }

    @Override
    public PsiDescriptorListFilter getPsiDescriptorsFilter() {
        return c -> {
            List<PsiModifierListOwner> owners = new ArrayList<>(Arrays.asList(c.getAllFields()));
            owners.addAll(Arrays.asList(c.getAllMethods()));
            return owners.stream()
                    .map(SimplePsiDescriptor::of)
                    .filter(PsiDescriptor::isValid)
                    .filter(f -> this.getPsiDescriptorFilter().apply(f, c))
                    .distinct().collect(Collectors.toList());
        };
    }

    @Override
    public Jsonable parsePojo(PsiClass rootClass, PsiType psiType, LevelCounter counter) {
        if (counter.getLevel() >= YApiConstants.maxLevel) {
            return this.parseMap(psiType, DesUtils.getTypeDesc(String.format("超出最大解析层数:%d，不再展示详细字段信息", YApiConstants.maxLevel)));
        }
        counter.incrementLevel();
        PsiClass psiClass = PsiUtils.convertToClass(psiType);
        if (psiClass == null) {
            psiClass = PsiUtils.findPsiClass(TypeUtils.getTypePkName(psiType));
        }
        List<ValueWrapper> wrapperList = new ArrayList<>();
        if (Objects.nonNull(psiClass)) {
            for (PsiDescriptor descriptor : this.getPsiDescriptorsFilter().apply(psiClass)) {
                if (Objects.requireNonNull(descriptor.getOrigin().getModifierList())
                        .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                if (this.isIgnoredField(descriptor)) {
                    continue;
                }
                ValueWrapper valueWrapper = this.parseProperty(rootClass, new SimpleDescriptorWrapper(psiType, psiClass, descriptor), counter);
                YApiSupportHolder.supports.handleField(valueWrapper);
                DesUtils.handleTypeDesc(valueWrapper);
                wrapperList.add(valueWrapper);
            }
        }
        return this.buildPojo(wrapperList);
    }

    protected boolean isIgnoredField(PsiDescriptor descriptor) {
        Source source = this.getSource();
        String fieldName = descriptor.getName();
        if (Source.REQUEST == source) {
            return this.property.getIgnoredReqFieldList().contains(fieldName);
        }
        if (Source.RESPONSE == source) {
            return this.property.getIgnoredResFieldList().contains(fieldName);
        }
        return false;
    }

    /**
     * 是否需要生成描述信息（raw响应信息可以复用Json5解析器，去除description描述信息即可）
     */
    protected boolean needDescription() {
        return true;
    }

    @Override
    public ValueWrapper parseProperty(PsiClass targetClass, PsiDescriptorWrapper fieldWrapper, LevelCounter counter) {
        ValueWrapper result = new ValueWrapper();
        PsiDescriptor descriptor = fieldWrapper.getDescriptor();
        PsiModifierListOwner owner = descriptor.getOrigin();
        result.setSource(owner);
        result.setJson(this.parseFieldValue(targetClass, fieldWrapper, counter));
        if (this.needDescription()) {
            String desc = DesUtils.getLinkRemark(owner);
            desc = this.handleDocTagValue(desc);
            if (StringUtils.isNotBlank(desc)) {
                result.setDesc(desc);
            }
        }
        if (this.property.isEnableTypeDesc()) {
            result.setTypeDesc(TypeUtils.getTypeName(descriptor.getType()));
        }
        String fieldName = this.handleFieldName(descriptor.getName());
        result.setName(fieldName);
        return result;
    }

    /**
     * <b>解析具体字段</b>
     *
     * @author aqiu 2020/7/23 3:14 下午
     **/
    protected Jsonable parseFieldValue(PsiClass rootClass, PsiDescriptorWrapper fieldWrapper, LevelCounter counter) {
        return this.parse(rootClass, fieldWrapper.resolveFieldType(), counter);
    }

    /**
     * <b>根据解析到的所有字段信息构建最终的解析结果</b>
     *
     * @author aqiu 2020/7/23 3:15 下午
     **/
    public abstract Jsonable buildPojo(Collection<ValueWrapper> wrappers);
}
