package com.github.aqiu202.ideayapi.parser.abs;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.PropertyNamingStrategy;
import com.github.aqiu202.ideayapi.constant.SpringWebFluxConstants;
import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiFieldFilter;
import com.github.aqiu202.ideayapi.http.filter.PsiFieldListFilter;
import com.github.aqiu202.ideayapi.http.res.DocTagValueHandler;
import com.github.aqiu202.ideayapi.http.res.ResponseFieldNameHandler;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.github.aqiu202.ideayapi.parser.ObjectJsonParser;
import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.parser.type.PsiFieldWrapper;
import com.github.aqiu202.ideayapi.parser.type.SimpleFieldWrapper;
import com.github.aqiu202.ideayapi.util.DesUtils;
import com.github.aqiu202.ideayapi.util.PropertyNamingUtils;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractJsonParser implements ObjectJsonParser, ResponseFieldNameHandler,
        DocTagValueHandler {

    protected final YApiProjectProperty property;
    protected final Project project;
    protected final boolean notConvertFieldName;

    protected AbstractJsonParser(@NotNull YApiProjectProperty property, Project project, boolean notConvertFieldName) {
        this.property = property;
        this.project = project;
        this.notConvertFieldName = notConvertFieldName;
    }

    protected AbstractJsonParser(YApiProjectProperty property, Project project) {
        this(property, project, false);
    }

    private String handleTypePkName(@NotNull String typePkName) {
        //兼容WebFlux
        if (typePkName.startsWith(SpringWebFluxConstants.Mono)) {
            typePkName = typePkName.substring(SpringWebFluxConstants.Mono.length() + 1);
            typePkName = typePkName.substring(0, typePkName.length() - 1);
        }
        return typePkName.replace("? extends ", "")
                .replace("? super ", "").replace(" ", "");
    }

    @Override
    public Jsonable parse(PsiClass rootClass, String typePkName, LevelCounter counter) {
        //是否是数组
        boolean isArray = typePkName.endsWith("[]");
        if (isArray) {
            typePkName = typePkName.substring(0, typePkName.length() - 2);
            //如果是多维数组，递归解析
            if (typePkName.endsWith("[]")) {
                return this.parseCollection(rootClass, typePkName, counter);
            }
        }
        int s = typePkName.indexOf("<");
        String type;
        String genericType = null;
        //如果有泛型
        if (s != -1) {
            type = typePkName.substring(0, s);
            //截取子类型
            genericType = typePkName.substring(s + 1, typePkName.lastIndexOf(">"));
        } else {
            type = typePkName;
        }
        Jsonable result;
        //如果是基本类型
        if (TypeUtils.isBasicType(type)) {
            result = this.parseBasic(type);
        } else if (TypeUtils.isMap(this.project, type)) {
            //对Map及其子类进行处理
            result = this.parseMap(type);
        } else if (TypeUtils.isCollection(this.project, type)) {
            //如果是集合类型（List Set）
            result = this.parseCollection(rootClass, genericType, counter);
        } else {
            //其他情况 pojo
            result = this.parsePojo(rootClass, type, counter);
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
        String typePkName = psiType.getCanonicalText();
        Jsonable jsonable = this.parse(rootClass, this.handleTypePkName(typePkName), new LevelCounter());
        if (jsonable instanceof ItemJsonSchema) {
            ((ItemJsonSchema) jsonable).set$schema(YApiConstants.$schema);
        }
        return jsonable.toJson();
    }

    @Override
    public PsiFieldFilter getPsiFieldFilter() {
        return (f, c) ->
                !DeprecatedAssert.instance.isDeprecated(f) && !YApiSupportHolder.supports.isIgnored(f, c);
    }

    @Override
    public PsiFieldListFilter getPsiFieldListFilter() {
        return c -> Arrays.stream(c.getAllFields()).filter(f -> this.getPsiFieldFilter().apply(f, c)).collect(Collectors.toList());
    }

    @Override
    public Jsonable parsePojo(PsiClass rootClass, String typePkName, LevelCounter counter) {
        if (counter.getLevel() >= YApiConstants.maxLevel) {
            return this.parseMap(typePkName, DesUtils.getTypeDesc(String.format("超出最大解析层数:%d，不再展示详细字段信息", YApiConstants.maxLevel)));
        }
        counter.incrementLevel();
        PsiClass psiClass = PsiUtils.findPsiClass(this.project, typePkName);
        List<ValueWrapper> wrapperList = new ArrayList<>();
        if (Objects.nonNull(psiClass)) {
            for (PsiField field : this.getPsiFieldListFilter().apply(psiClass)) {
                if (Objects.requireNonNull(field.getModifierList())
                        .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                String fieldName = field.getName();
                if (this.property.getIgnoredResFieldList().contains(fieldName)) {
                    continue;
                }
                ValueWrapper valueWrapper = this.parseField(rootClass, new SimpleFieldWrapper(psiClass, field), counter);
                YApiSupportHolder.supports.handleField(valueWrapper);
                DesUtils.handleTypeDesc(valueWrapper);
                wrapperList.add(valueWrapper);
            }
        }
        return this.buildPojo(wrapperList);
    }

    /**
     * 是否需要生成描述信息（raw响应信息可以复用Json5解析器，去除description描述信息即可）
     */
    protected boolean needDescription() {
        return true;
    }

    @Override
    public ValueWrapper parseField(PsiClass targetClass, PsiFieldWrapper fieldWrapper, LevelCounter counter) {
        ValueWrapper result = new ValueWrapper();
        PsiField field = fieldWrapper.getField();
        result.setSource(field);
        result.setJson(this.parseFieldValue(targetClass, fieldWrapper, counter));
        if (this.needDescription()) {
            String desc = DesUtils.getLinkRemark(field, this.project);
            desc = this.handleDocTagValue(desc);
            if (StringUtils.isNotBlank(desc)) {
                result.setDesc(desc);
            }
        }
        if (this.property.isEnableTypeDesc()) {
            result.setTypeDesc(field.getType().getPresentableText());
        }
        String fieldName = this.handleFieldName(field.getName());
        result.setName(fieldName);
        return result;
    }

    /**
     * <b>解析具体字段</b>
     *
     * @author aqiu 2020/7/23 3:14 下午
     **/
    protected Jsonable parseFieldValue(PsiClass rootClass, PsiFieldWrapper fieldWrapper, LevelCounter counter) {
        return this.parse(rootClass, fieldWrapper.resolveFieldType().getCanonicalText(), counter);
    }

    /**
     * <b>根据解析到的所有字段信息构建最终的解析结果</b>
     *
     * @author aqiu 2020/7/23 3:15 下午
     **/
    public abstract Jsonable buildPojo(Collection<ValueWrapper> wrappers);
}
