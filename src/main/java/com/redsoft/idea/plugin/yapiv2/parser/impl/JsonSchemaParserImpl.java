package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.TypeConstants;
import com.redsoft.idea.plugin.yapiv2.constant.YApiConstants;
import com.redsoft.idea.plugin.yapiv2.parser.JsonSchemaParser;
import com.redsoft.idea.plugin.yapiv2.range.DecimalRange;
import com.redsoft.idea.plugin.yapiv2.range.IntegerRange;
import com.redsoft.idea.plugin.yapiv2.range.LongRange;
import com.redsoft.idea.plugin.yapiv2.schema.ArraySchema;
import com.redsoft.idea.plugin.yapiv2.schema.BooleanSchema;
import com.redsoft.idea.plugin.yapiv2.schema.IntegerSchema;
import com.redsoft.idea.plugin.yapiv2.schema.NumberSchema;
import com.redsoft.idea.plugin.yapiv2.schema.ObjectSchema;
import com.redsoft.idea.plugin.yapiv2.schema.SchemaHelper;
import com.redsoft.idea.plugin.yapiv2.schema.StringSchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.SchemaType;
import com.redsoft.idea.plugin.yapiv2.util.DesUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import com.redsoft.idea.plugin.yapiv2.util.ValidUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class JsonSchemaParserImpl extends AbstractObjectParser implements JsonSchemaParser {

    public JsonSchemaParserImpl(YApiProjectProperty property, Project project) {
        super(property, project);
    }

    @Override
    public ItemJsonSchema getPojoSchema(String typePkName) {
        ObjectSchema objectSchema = new ObjectSchema();
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
                        objectSchema.addProperty(fieldName,
                                new ArraySchema().setItems(this.getSchema(classType, false))
                                        .setDescription(
                                                DesUtils.getLinkRemark(field, this.project)));
                    } else if (TypeConstants.genericList
                            .contains(gType)) {
                        objectSchema.addProperty(fieldName, this.getSchema(classType, false)
                                .setDescription(DesUtils.getLinkRemark(field, this.project)));
                    } else {
                        objectSchema.addProperty(fieldName, this.getFieldSchema(field)
                                .setDescription(DesUtils.getLinkRemark(field, this.project)));
                    }
                } else {
                    objectSchema.addProperty(fieldName, this.getFieldSchema(field));
                }
                if (ValidUtils.notNullOrBlank(field)) {
                    objectSchema.addRequired(fieldName);
                }
            }
            return objectSchema;
        }
        return new ObjectSchema();
    }

    public ItemJsonSchema getSchema(PsiType psiType, boolean needSchema) {
        String typePkName = psiType.getCanonicalText();
        ItemJsonSchema result;
        //如果是基本类型
        if (TypeConstants.isBaseType(typePkName)) {
            result = SchemaHelper.parse(TypeConstants.normalTypeMappings.get(typePkName));
            result.setDefault(TypeConstants.normalTypesPackages.get(typePkName).toString());
            result.setMock(TypeConstants.formatMockType(psiType.getPresentableText()));
        } else {
            result = this.getOtherTypeSchema(psiType);
        }
        if (needSchema) {
            result.set$schema(YApiConstants.$schema);
        }
        return result;
    }

    @Override
    public ItemJsonSchema getOtherTypeSchema(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        boolean wrapArray = false;
        if (typePkName.endsWith("[]")) {
            typePkName = typePkName.replace("[]", "");
            wrapArray = true;
        }
        String type = typePkName.split("<")[0];
        ItemJsonSchema result;
        //对Map和Map类型的封装类进行过滤
        if (PsiUtils.isMap(psiType)) {
            ObjectSchema mapResult = new ObjectSchema();
            result = wrapArray ? new ArraySchema().setItems(mapResult) : mapResult;
        } else if (TypeConstants.arrayTypeMappings.containsKey(type)) {
            //如果是集合类型（List Set）
            ArraySchema tmp = this.getArraySchema(typePkName);
            result = wrapArray ? new ArraySchema().setItems(tmp) : tmp;
        } else if (typePkName.endsWith("[]")) {
            //数组形式的返回值（且不是集合类型前缀）
            typePkName = typePkName.replace("[]", "");
            result = new ArraySchema().setItems(this.getPojoSchema(typePkName));
        } else {
            //其他情况 object
            result = this.getPojoSchema(typePkName);
        }
        return result;
    }

    @Override
    public ArraySchema getArraySchema(String typePkName) {
        String[] types = typePkName.split("<");
        ArraySchema arraySchema = new ArraySchema();
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
            ItemJsonSchema item;
            if (TypeConstants.isBaseType(childrenType)) {
                item = SchemaHelper
                        .parse(TypeConstants.normalTypeMappings.get(childrenType));
            } else {
                item = this.getPojoSchema(childrenType);
            }
            arraySchema.setItems(isWrapArray ? new ArraySchema().setItems(item) : item);
        } else {
            //没有泛型 默认
            arraySchema.setItems(new ObjectSchema());
        }
        return arraySchema;
    }

    @Override
    public ItemJsonSchema getOtherFieldSchema(PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typeName = psiType.getPresentableText();
        boolean wrapArray = typeName.endsWith("[]");
        ItemJsonSchema result = this.getOtherTypeSchema(psiType);
        if (result instanceof ArraySchema) {
            ArraySchema a = (ArraySchema) result;
            if (typeName.contains("Set") && !wrapArray) {
                a.setUniqueItems(true);
            }
            if (ValidUtils.notEmpty(psiField)) {
                a.setMinItems(1);
            }
            IntegerRange integerRange = ValidUtils
                    .rangeSize(psiField, this.property.isEnableBasicScope());
            a.setMinItems(integerRange.getMin(), this.property.isEnableBasicScope());
            a.setMaxItems(integerRange.getMax(), this.property.isEnableBasicScope());
        }
        result.setDescription(DesUtils.getLinkRemark(psiField, this.project));
        return result;
    }

    @Override
    public ItemJsonSchema getBaseFieldSchema(SchemaType schemaType, PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typePkName = psiType.getCanonicalText();
        ItemJsonSchema result;
        switch (schemaType) {
            case number:
                NumberSchema numberSchema = new NumberSchema();
                DecimalRange decimalRange = ValidUtils.rangeDecimal(psiField);
                if (Objects.nonNull(decimalRange)) {
                    numberSchema.setRange(decimalRange);
                }
                if (ValidUtils.isPositive(psiField)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                    numberSchema.setExclusiveMinimum(true);
                }
                if (ValidUtils.isPositiveOrZero(psiField)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                }
                if (ValidUtils.isNegative(psiField)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                    numberSchema.setExclusiveMaximum(true);
                }
                if (ValidUtils.isNegativeOrZero(psiField)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                }
                result = numberSchema;
                break;
            case integer:
                IntegerSchema integerSchema = new IntegerSchema();
                if (TypeConstants.hasBaseRange(typePkName)) {
                    if (this.property.isEnableBasicScope()) {
                        integerSchema.setRange(TypeConstants.baseRangeMappings.get(typePkName));
                    }
                }
                LongRange longRange = ValidUtils
                        .range(psiField, this.property.isEnableBasicScope());
                if (Objects.nonNull(longRange)) {
                    integerSchema.setRange(longRange);
                }
                if (ValidUtils.isPositive(psiField)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMinimum(true);
                }
                if (ValidUtils.isPositiveOrZero(psiField)) {
                    integerSchema.setMinimum(0L);
                }
                if (ValidUtils.isNegative(psiField)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMaximum(true);
                }
                if (ValidUtils.isNegativeOrZero(psiField)) {
                    integerSchema.setMinimum(0L);
                }
                result = integerSchema;
                break;
            case string:
                StringSchema stringSchema = new StringSchema();
                IntegerRange integerRange = ValidUtils
                        .rangeLength(psiField, this.property.isEnableBasicScope());
                stringSchema.setMinLength(integerRange.getMin());
                stringSchema.setMaxLength(integerRange.getMax());
                String pattern = ValidUtils.getPattern(psiField);
                if (!Strings.isEmpty(pattern)) {
                    stringSchema.setPattern(pattern);
                }
                result = stringSchema;
                break;
            case bool:
                result = new BooleanSchema();
                break;
            default:
                return new StringSchema();
        }
        result.setDescription(DesUtils.getLinkRemark(psiField, this.project));
        result.setDefault(TypeConstants.normalTypesPackages.get(typePkName).toString());
        return result;
    }

    @Override
    public ItemJsonSchema getFieldSchema(PsiField psiField) {
        PsiType type = psiField.getType();
        String typePkName = type.getCanonicalText();
        ItemJsonSchema itemJsonSchema;
        if (TypeConstants.isBaseType(typePkName)) {
            SchemaType schemaType = TypeConstants.normalTypeMappings.get(typePkName);
            itemJsonSchema = getBaseFieldSchema(schemaType, psiField);
            itemJsonSchema.setMock(TypeConstants.formatMockType(type.getPresentableText()));
        } else {
            itemJsonSchema = getOtherFieldSchema(psiField);
        }
        return itemJsonSchema;
    }

    @Override
    public String getJsonResponse(PsiType psiType) {
        return this.getSchema(psiType, true).toPrettyJson();
    }

}
