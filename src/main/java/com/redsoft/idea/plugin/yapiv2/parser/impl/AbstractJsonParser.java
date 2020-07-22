package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.PropertyNamingStrategy;
import com.redsoft.idea.plugin.yapiv2.constant.SpringWebFluxConstants;
import com.redsoft.idea.plugin.yapiv2.constant.YApiConstants;
import com.redsoft.idea.plugin.yapiv2.parser.Jsonable;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectJsonParser;
import com.redsoft.idea.plugin.yapiv2.res.DocTagValueHandler;
import com.redsoft.idea.plugin.yapiv2.res.ResponseFieldNameHandler;
import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapiv2.util.PropertyNamingUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractJsonParser implements ObjectJsonParser, ResponseFieldNameHandler,
        DocTagValueHandler {

    protected final YApiProjectProperty property;
    protected final Project project;

    protected AbstractJsonParser(@Nullable YApiProjectProperty property, Project project) {
        this.property = property;
        this.project = project;
    }

    protected AbstractJsonParser(Project project) {
        this(null, project);
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
    public Jsonable parse(String typePkName) {
        //是否是数组
        boolean isArray = typePkName.endsWith("[]");
        if (isArray) {
            typePkName = typePkName.substring(0, typePkName.length() - 2);
            //如果是多维数组，递归解析
            if (typePkName.endsWith("[]")) {
                return this.parseCollection(typePkName);
            }
        }
        int s = typePkName.indexOf("<");
        String type;
        String subType = null;
        //如果有泛型
        if (s != -1) {
            type = typePkName.substring(0, s);
            //截取子类型
            subType = typePkName.substring(s + 1, typePkName.lastIndexOf(">"));
        } else {
            type = typePkName;
        }
        Jsonable result;
        //如果是基本类型
        if (TypeUtils.isBasicType(type)) {
            result = this.parseBasic(type);
        } else if (PsiUtils.isMap(this.project, type)) {
            //对Map和Map类型的封装类进行过滤
            result = this.parseMap(type);
        } else if (TypeUtils.isCollectionType(type)) {
            //如果是集合类型（List Set）
            result = this.parseCollection(subType);
        } else {
            //其他情况 pojo
            if (Strings.isBlank(subType)) {
                //如果没有泛型
                result = this.parsePojo(type);
            } else {
                //如果有泛型
                result = this.parsePojo(type, subType);
            }
        }
        return result;
    }

    @Override
    public String handleFieldName(String fieldName) {
        //配置为空的时候，不处理字段名称，比如（请求的参数字段不能做处理）
        if (this.property == null) {
            return fieldName;
        }
        return PropertyNamingUtils.convert(fieldName, PropertyNamingStrategy.of(String.
                valueOf(this.property.getStrategy())));
    }

    @Override
    public String getJsonResponse(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        Jsonable jsonable = this.parse(this.handleTypePkName(typePkName));
        if (jsonable instanceof ItemJsonSchema) {
            ((ItemJsonSchema) jsonable).set$schema(YApiConstants.$schema);
        }
        return jsonable.toJson();
    }

}
