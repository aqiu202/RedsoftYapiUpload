package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.redsoft.idea.plugin.yapiv2.constant.TypeConstants;
import com.redsoft.idea.plugin.yapiv2.json5.Json;
import com.redsoft.idea.plugin.yapiv2.json5.JsonArray;
import com.redsoft.idea.plugin.yapiv2.json5.JsonItem;
import com.redsoft.idea.plugin.yapiv2.json5.JsonObject;
import com.redsoft.idea.plugin.yapiv2.parser.Json5Parser;
import com.redsoft.idea.plugin.yapiv2.util.DesUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Objects;

public class Json5ParserImpl extends AbstractObjectParser implements Json5Parser {

    public Json5ParserImpl(YApiProjectProperty property, Project project) {
        super(property, project);
    }

    @Override
    public Json<?> getJson(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        Json<?> result;
        //如果是基本类型
        if (TypeConstants.isBaseType(typePkName)) {
            result = new Json<>(TypeConstants.normalTypesPackages.get(typePkName));
        } else {
            result = this.getOtherJson(psiType);
        }
        return result;
    }

    @Override
    public String getJsonResponse(PsiType psiType) {
        return this.getJson(psiType).toString();
    }

    @Override
    public JsonArray<?> getJsonArray(String typePkName) {
        String[] types = typePkName.split("<");
        JsonArray<?> array;
        //如果有泛型
        if (types.length > 1) {
            String childrenType = types[1].split(">")[0];
            childrenType = childrenType.replace("? extends ", "")
                    .replace("? super ", "");
            boolean isWrapArray = childrenType.endsWith("[]");
            //是否是数组类型
            if (isWrapArray) {
                childrenType = childrenType.replace("[]", "");
            }
            //如果泛型是基本类型
            if (TypeConstants.isBaseType(childrenType)) {
                array = new JsonArray<>(
                        new Json<>(TypeConstants.normalTypesPackages.get(childrenType)));
            } else {
                array = new JsonArray<>(this.getJsonObject(childrenType));
            }
        } else {
            //没有泛型 默认
            array = new JsonArray<>();
        }
        return array;
    }

    @Override
    public JsonObject getJsonObject(String typePkName) {
        JsonObject jsonObject = new JsonObject();
        String[] types = typePkName.split("<");
        typePkName = types[0];
        PsiClass psiClass = JavaPsiFacade.getInstance(this.project)
                .findClass(typePkName,
                        GlobalSearchScope.allScope(this.project));
        if (Objects.nonNull(psiClass)) {
            boolean hasChildren;
            PsiClassType classType = null;
            if (hasChildren = types.length == 2) {
                String childrenType = types[1].split(">")[0];
                childrenType = childrenType.replace("? extends ", "")
                        .replace("? super ", "");
                classType = PsiType.getTypeByName(childrenType, this.project,
                        GlobalSearchScope.allScope(this.project));
            } else if (hasChildren = types.length == 3) {
                String childrenType = types[1].split(">")[0] + "<" + types[2].split(">")[0] + ">";
                childrenType = childrenType.replace("? extends ", "")
                        .replace("? super ", "");
                classType = PsiType.getTypeByName(childrenType, this.project,
                        GlobalSearchScope.allScope(this.project));
            }
            for (PsiField field : psiClass.getAllFields()) {
                if (
                        Objects.requireNonNull(field.getModifierList())
                                .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                //防止对象内部嵌套自身导致死循环
                if (field.getType().getCanonicalText().contains(
                        Objects.requireNonNull(psiClass.getQualifiedName()))) {
                    continue;
                }
                String fieldName = this.handleFieldName(field.getName());
                if (hasChildren) {
                    String gType = field.getType().getCanonicalText();
                    String[] gTypes = gType.split("<");
                    if (gTypes.length > 1 && TypeConstants.genericList
                            .contains(gTypes[1].split(">")[0]) && TypeConstants.arrayTypeMappings
                            .containsKey(gTypes[0])) {
                        jsonObject.addItem(new JsonItem<>(fieldName,
                                new JsonArray<>(this.getJson(classType)),
                                DesUtils.getLinkRemark(field, this.project)));
                    } else if (TypeConstants.genericList
                            .contains(gType)) {
                        jsonObject.addItem(new JsonItem<>(fieldName, this.getJson(classType),
                                DesUtils.getLinkRemark(field, this.project)));
                    } else {
                        jsonObject.addItem(new JsonItem<>(fieldName, this.getJsonByField(field),
                                DesUtils.getLinkRemark(field, this.project)));
                    }
                } else {
                    jsonObject.addItem(new JsonItem<>(fieldName, this.getJsonByField(field),
                            DesUtils.getLinkRemark(field, this.project)));
                }
            }
            return jsonObject;
        }
        return new JsonObject();
    }

    @Override
    public Json<?> getJsonByField(PsiField psiField) {
        PsiType type = psiField.getType();
        String typePkName = type.getCanonicalText();
        Json<?> json;
        if (TypeConstants.isBaseType(typePkName)) {
            json = new Json<>(TypeConstants.normalTypesPackages.get(typePkName));
        } else {
            json = this.getOtherJson(type);
        }
        return json;
    }

    @Override
    public Json<?> getOtherJson(PsiType psiType) {
        Json<?> result;
        String typePkName = psiType.getCanonicalText();
        boolean wrapArray = false;
        if (typePkName.endsWith("[]")) {
            typePkName = typePkName.replace("[]", "");
            wrapArray = true;
        }
        String type = typePkName.split("<")[0];
        //对Map和Map类型的封装类进行过滤
        if (PsiUtils.isMap(psiType)) {
            result = wrapArray ? new JsonArray<>(
                    new JsonObject(new JsonItem<>("key", new Json<>("value"), "map")))
                    : new JsonObject(new JsonItem<>("key", new Json<>("value"), "map"));
        } else if (TypeConstants.arrayTypeMappings.containsKey(type)) {
            //如果是集合类型（List Set）
            JsonArray<?> array = this.getJsonArray(typePkName);
            result = wrapArray ? new JsonArray<>(array) : array;
        } else if (typePkName.endsWith("[]")) {
            //数组形式的返回值（且不是集合类型前缀）
            typePkName = typePkName.replace("[]", "");
            result = new JsonArray<>(this.getJsonObject(typePkName));
        } else {
            //其他情况 object
            result = this.getJsonObject(typePkName);
        }
        return result;
    }
}
