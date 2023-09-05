package com.github.aqiu202.ideayapi.parser.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.mode.schema.*;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;
import com.github.aqiu202.ideayapi.model.EnumFields;
import com.github.aqiu202.ideayapi.model.EnumResult;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.range.DecimalRange;
import com.github.aqiu202.ideayapi.model.range.IntegerRange;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.github.aqiu202.ideayapi.parser.JsonSchemaJsonParser;
import com.github.aqiu202.ideayapi.parser.abs.AbstractJsonParser;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.type.PsiFieldWrapper;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.github.aqiu202.ideayapi.util.ValidUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <b>json-schema解析器默认实现</b>
 *
 * @author aqiu 2020/7/24 9:56 上午
 **/
public class JsonSchemaParserImpl extends AbstractJsonParser implements JsonSchemaJsonParser {

    public JsonSchemaParserImpl(YApiProjectProperty property, Project project) {
        super(property, project);
        this.enableBasicScope = property.isEnableBasicScope();
    }

    private final boolean enableBasicScope;


    @Override
    public ItemJsonSchema parseJsonSchema(PsiClass rootClass, String typePkName, LevelCounter counter) {
        return (ItemJsonSchema) super.parse(rootClass, typePkName, counter);
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
    public ObjectSchema parseMap(String typePkName, String description) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setDescription(description);
        return objectSchema;
    }

    @Override
    public ArraySchema parseCollection(PsiClass rootClass, String typePkName, LevelCounter counter) {
        ArraySchema result = new ArraySchema();
        if (StringUtils.isBlank(typePkName)) {
            return result.setItems(new ObjectSchema());
        }
        return result.setItems(this.parseJsonSchema(rootClass, typePkName, counter));
    }

    @Override
    public ItemJsonSchema buildPojo(Collection<ValueWrapper> wrappers) {
        ObjectSchema objectSchema = new ObjectSchema();
        for (ValueWrapper wrapper : wrappers) {
            String fieldName = wrapper.getName();
            ItemJsonSchema value = (ItemJsonSchema) wrapper.getJson();
            // 字段备注
            String desc = wrapper.getDesc();
            if (StringUtils.isBlank(desc)) {
                desc = "";
            }
            if (value != null) {
                // 类型备注
                String description = value.getDescription();
                if (StringUtils.isNotBlank(description)) {
                    desc += description;
                }
                objectSchema.addProperty(fieldName, value.setDescription(desc));
            }
            if (ValidUtils.notNullOrBlank(wrapper.getSource())) {
                objectSchema.addRequired(fieldName);
            }
        }
        return objectSchema;
    }

    @Override
    public ItemJsonSchema parseFieldValue(PsiClass rootClass, PsiFieldWrapper fieldWrapper, LevelCounter counter) {
        PsiField psiField = fieldWrapper.getField();
        PsiType type = fieldWrapper.resolveFieldType();
        String typePkName = type.getCanonicalText();
        EnumResult enumResult = PsiUtils.isEnum(this.project, typePkName);
        ItemJsonSchema itemJsonSchema;
        if (enumResult.isValid()) {
            itemJsonSchema = new StringSchema();
            StringSchema stringSchema = (StringSchema) itemJsonSchema;
            PsiClass psiClass = PsiUtils.findPsiClass(this.project, typePkName);
            EnumFields enumFields = PsiUtils.resolveEnum(psiClass);
            stringSchema.setEnum(enumFields.getFieldNames());
            stringSchema.setEnumDesc(enumFields.getFieldsDescription());
        } else if (TypeUtils.isBasicType(typePkName)) {
            itemJsonSchema = this.parseBasicField(psiField);
        } else {
            itemJsonSchema = this.parseCompoundField(rootClass, fieldWrapper, counter);
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
                if (!StringUtils.isEmpty(pattern)) {
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
        result.setMock(TypeUtils.formatMockType(psiType.getPresentableText()));
        return result;
    }

    private ItemJsonSchema parseCompoundField(PsiClass rootClass, PsiFieldWrapper fieldWrapper, LevelCounter counter) {
        PsiField psiField = fieldWrapper.getField();
        PsiType psiType = fieldWrapper.resolveFieldType();
        String typeName = psiType.getPresentableText();
        boolean wrapArray = typeName.endsWith("[]");
        ItemJsonSchema result = this.parseJsonSchema(rootClass, psiType.getCanonicalText(), counter);
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
