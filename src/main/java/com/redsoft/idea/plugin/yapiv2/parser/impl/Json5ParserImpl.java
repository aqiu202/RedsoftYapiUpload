package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.parser.Json5JsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.Jsonable;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectRawParser;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.json5.Json;
import com.redsoft.idea.plugin.yapiv2.json5.JsonArray;
import com.redsoft.idea.plugin.yapiv2.json5.JsonItem;
import com.redsoft.idea.plugin.yapiv2.json5.JsonObject;
import com.redsoft.idea.plugin.yapiv2.util.DesUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Objects;

public class Json5ParserImpl extends AbstractJsonParser implements Json5JsonParser,
        ObjectRawParser {

    private final boolean needDesc;

    public Json5ParserImpl(YApiProjectProperty property, Project project) {
        this(property, project, true);
    }

    public Json5ParserImpl(YApiProjectProperty property, Project project, boolean needDesc) {
        super(property, project);
        this.needDesc = needDesc;
    }

    public Json5ParserImpl(Project project) {
        this(project, true);
    }

    public Json5ParserImpl(Project project, boolean needDesc) {
        super(project);
        this.needDesc = needDesc;
    }

    @Override
    public Json<?> parseJson5(String typePkName) {
        return ((Json<?>) super.parse(typePkName));
    }

    @Override
    public Jsonable parseBasic(String typePkName) {
        return new Json<>(TypeUtils.getDefaultValueByPackageName(typePkName));
    }

    @Override
    public JsonObject parseMap(String typePkName) {
        return new JsonObject(new JsonItem<>("key", new Json<>("value"), "map"));
    }

    @Override
    public JsonArray<?> parseCollection(String typePkName) {
        if (Strings.isBlank(typePkName)) {
            return new JsonArray<>();
        }
        return new JsonArray<>(this.parseJson5(typePkName));
    }

    @Override
    public Json<?> parsePojo(String typePkName) {
        return this.parsePojo(typePkName, null);
    }

    @Override
    public Json<?> parsePojo(String typePkName, String subType) {
        JsonObject jsonObject = new JsonObject();
        PsiClass psiClass = PsiUtils.findPsiClass(this.project, typePkName);
        boolean hasSubType = Strings.isNotBlank(subType);
        if (Objects.nonNull(psiClass)) {
            for (PsiField field : psiClass.getAllFields()) {
                if (Objects.requireNonNull(field.getModifierList())
                        .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                //防止对象内部嵌套自身导致死循环
                if (field.getType().getCanonicalText().contains(
                        Objects.requireNonNull(psiClass.getQualifiedName()))) {
                    continue;
                }
                String fieldName = this.handleFieldName(field.getName());
                String desc = null;
                if(this.needDesc) {
                    desc = DesUtils.getLinkRemark(field, this.project);
                    desc = this.handleDocTagValue(desc);
                }
                String fieldTypeName = field.getType().getCanonicalText();
                //如果含有泛型，处理泛型
                if (hasSubType) {
                    if (TypeUtils.hasGenericType(fieldTypeName)) {
                        subType = TypeUtils.parseGenericType(fieldTypeName, subType);
                        jsonObject
                                .addItem(new JsonItem<>(fieldName, this.parseJson5(subType), desc));
                    } else {
                        jsonObject.addItem(
                                new JsonItem<>(fieldName, this.parseJson5(fieldTypeName), desc));
                    }
                } else {
                    jsonObject.addItem(
                            new JsonItem<>(fieldName, this.parseJson5(fieldTypeName), desc));
                }
            }
        }
        return jsonObject;
    }

    @Override
    public String getRawResponse(PsiType psiType) {
        return this.parse(psiType.getCanonicalText()).toJson();
    }
}
