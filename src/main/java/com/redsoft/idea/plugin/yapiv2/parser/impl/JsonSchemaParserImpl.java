package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.model.FieldValueWrapper;
import com.redsoft.idea.plugin.yapiv2.parser.JsonSchemaJsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.abs.AbstractJsonParser;
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
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.util.ValidUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * <b>json-schema解析器默认实现</b>
 * @author aqiu 2020/7/24 9:56 上午
 **/
public class JsonSchemaParserImpl extends AbstractJsonParser implements JsonSchemaJsonParser {

    public JsonSchemaParserImpl(YApiProjectProperty property, Project project) {
        super(property, project);
        this.enableBasicScope = property.isEnableBasicScope();
    }

    public JsonSchemaParserImpl(boolean enableBasicScope, Project project) {
        super(null, project);
        this.enableBasicScope = enableBasicScope;
    }

    private final boolean enableBasicScope;


    @Override
    public ItemJsonSchema parseJsonSchema(String typePkName) {
        return (ItemJsonSchema) super.parse(typePkName);
    }

    @Override
    public ItemJsonSchema parseBasic(String typePkName) {
        ItemJsonSchema result = SchemaHelper
                .parseBasic(TypeUtils.getBasicSchema(typePkName));
        result.setDefault(TypeUtils.getDefaultValueByPackageName(typePkName).toString());
        result.setMock(TypeUtils
                .formatMockType(typePkName.substring(typePkName.lastIndexOf(".") + 1)));
        return result;
    }

    @Override
    public ObjectSchema parseMap(String typePkName) {
        return new ObjectSchema();
    }

    @Override
    public ArraySchema parseCollection(String typePkName) {
        ArraySchema result = new ArraySchema();
        if (Strings.isBlank(typePkName)) {
            return result.setItems(new ObjectSchema());
        }
        return result.setItems(this.parseJsonSchema(typePkName));
    }

    @Override
    public ItemJsonSchema buildPojo(Collection<FieldValueWrapper> wrappers) {
        ObjectSchema objectSchema = new ObjectSchema();
        for (FieldValueWrapper wrapper : wrappers) {
            String fieldName = wrapper.getFieldName();
            objectSchema.addProperty(fieldName,
                    ((ItemJsonSchema) wrapper.getValue()).setDescription(wrapper.getDescription()));
            if (ValidUtils.notNullOrBlank(wrapper.getField())) {
                objectSchema.addRequired(fieldName);
            }
        }
        return objectSchema;
    }

    @Override
    public ItemJsonSchema parseFieldValue(PsiField psiField) {
        PsiType type = psiField.getType();
        String typePkName = type.getCanonicalText();
        ItemJsonSchema itemJsonSchema;
        if (TypeUtils.isBasicType(typePkName)) {
            itemJsonSchema = this.parseBasicField(psiField);
            itemJsonSchema.setMock(TypeUtils.formatMockType(type.getPresentableText()));
        } else {
            itemJsonSchema = this.parseCompoundField(psiField);
        }
        return itemJsonSchema;
    }

    private ItemJsonSchema parseBasicField(PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typePkName = psiType.getCanonicalText();
        ItemJsonSchema result;
        SchemaType schemaType = TypeUtils.getBasicSchema(typePkName);
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
                if (TypeUtils.hasBaseRange(typePkName)) {
                    if (this.enableBasicScope) {
                        integerSchema.setRange(TypeUtils.getBaseRange(typePkName));
                    }
                }
                LongRange longRange = ValidUtils.range(psiField, this.enableBasicScope);
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
                        .rangeLength(psiField, this.enableBasicScope);
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
        result.setDefault(TypeUtils.getDefaultValueByPackageName(typePkName).toString());
        return result;
    }

    private ItemJsonSchema parseCompoundField(PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typeName = psiType.getPresentableText();
        boolean wrapArray = typeName.endsWith("[]");
        ItemJsonSchema result = this.parseJsonSchema(psiType.getCanonicalText());
        if (result instanceof ArraySchema) {
            ArraySchema a = (ArraySchema) result;
            if (typeName.contains("Set") && !wrapArray) {
                a.setUniqueItems(true);
            }
            if (ValidUtils.notEmpty(psiField)) {
                a.setMinItems(1);
            }
            IntegerRange integerRange = ValidUtils
                    .rangeSize(psiField, this.enableBasicScope);
            a.setMinItems(integerRange.getMin(), this.enableBasicScope);
            a.setMaxItems(integerRange.getMax(), this.enableBasicScope);
        }
        return result;
    }

}
