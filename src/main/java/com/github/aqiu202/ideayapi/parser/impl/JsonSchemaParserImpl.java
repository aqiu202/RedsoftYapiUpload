package com.github.aqiu202.ideayapi.parser.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.mode.schema.*;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;
import com.github.aqiu202.ideayapi.model.EnumFields;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.range.DecimalRange;
import com.github.aqiu202.ideayapi.model.range.IntegerRange;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.github.aqiu202.ideayapi.parser.JsonSchemaJsonParser;
import com.github.aqiu202.ideayapi.parser.abs.AbstractJsonParser;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptorWrapper;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.github.aqiu202.ideayapi.util.ValidUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
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
    public ItemJsonSchema parseJsonSchema(PsiClass rootClass, PsiType type, LevelCounter counter) {
        return (ItemJsonSchema) super.parse(rootClass, type, counter);
    }

    @Override
    public ItemJsonSchema parseBasic(PsiType psiType) {
        ItemJsonSchema result = SchemaHelper
                .parseBasic(TypeUtils.getBasicSchema(psiType));
        result.setDefault(TypeUtils.getDefaultValueByPackageName(psiType));
        result.setMock(TypeUtils.formatMockType(psiType));
        return result;
    }

    @Override
    public ObjectSchema parseMap(PsiType psiType, String description) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setDescription(description);
        return objectSchema;
    }

    @Override
    public ArraySchema parseCollection(PsiClass rootClass, PsiType psiType, LevelCounter counter) {
        ArraySchema result = new ArraySchema();
        return result.setItems(this.parseJsonSchema(rootClass, psiType, counter));
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
            if (ValidUtils.notNullOrBlank(wrapper.getSource().getFirstElement())) {
                objectSchema.addRequired(fieldName);
            }
        }
        return objectSchema;
    }

    @Override
    public ItemJsonSchema parsePropertyValue(PsiClass rootClass, PsiDescriptorWrapper fieldWrapper, LevelCounter counter) {
        PsiDescriptor descriptor = fieldWrapper.getDescriptor();
        PsiType type = fieldWrapper.resolveFieldType();
        ItemJsonSchema itemJsonSchema;
        if (TypeUtils.isEnum(type)) {
            itemJsonSchema = new StringSchema();
            StringSchema stringSchema = (StringSchema) itemJsonSchema;
            PsiClass psiClass = PsiUtils.convertToClass(type);
            if (psiClass != null) {
                EnumFields enumFields = PsiUtils.resolveEnumFields(psiClass);
                stringSchema.setEnum(enumFields.getFieldNames());
                stringSchema.setEnumDesc(enumFields.getFieldsDescription());
            }
        } else if (TypeUtils.isBasicType(type)) {
            itemJsonSchema = this.parseBasicField(descriptor);
        } else {
            itemJsonSchema = this.parseCompoundField(rootClass, fieldWrapper, counter);
        }
        return itemJsonSchema;
    }

    private ItemJsonSchema parseBasicField(PsiDescriptor descriptor) {
        PsiType psiType = descriptor.getType();
        ItemJsonSchema result;
        SchemaType schemaType = TypeUtils.getBasicSchema(psiType);
        PsiModifierListOwner owner = descriptor.getFirstElement();
        switch (schemaType) {
            case number:
                NumberSchema numberSchema = new NumberSchema();
                DecimalRange decimalRange = ValidUtils.rangeDecimal(owner);
                if (Objects.nonNull(decimalRange)) {
                    numberSchema.setRange(decimalRange);
                }
                if (ValidUtils.isPositive(owner)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                    numberSchema.setExclusiveMinimum(true);
                }
                if (ValidUtils.isPositiveOrZero(owner)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                }
                if (ValidUtils.isNegative(owner)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                    numberSchema.setExclusiveMaximum(true);
                }
                if (ValidUtils.isNegativeOrZero(owner)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                }
                result = numberSchema;
                break;
            case integer:
                IntegerSchema integerSchema = new IntegerSchema();
                if (TypeUtils.hasBaseRange(psiType)) {
                    if (this.enableBasicScope) {
                        integerSchema.setRange(TypeUtils.getBaseRange(psiType));
                    }
                }
                LongRange longRange = ValidUtils.range(owner, this.enableBasicScope);
                if (Objects.nonNull(longRange)) {
                    integerSchema.setRange(longRange);
                }
                if (ValidUtils.isPositive(owner)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMinimum(true);
                }
                if (ValidUtils.isPositiveOrZero(owner)) {
                    integerSchema.setMinimum(0L);
                }
                if (ValidUtils.isNegative(owner)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMaximum(true);
                }
                if (ValidUtils.isNegativeOrZero(owner)) {
                    integerSchema.setMinimum(0L);
                }
                result = integerSchema;
                break;
            case string:
                StringSchema stringSchema = new StringSchema();
                IntegerRange integerRange = ValidUtils
                        .rangeLength(owner, this.enableBasicScope);
                stringSchema.setMinLength(integerRange.getMin());
                stringSchema.setMaxLength(integerRange.getMax());
                String pattern = ValidUtils.getPattern(owner);
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
        result.setDefault(TypeUtils.getDefaultValueByPackageName(psiType));
        result.setMock(TypeUtils.formatMockType(psiType));
        return result;
    }

    private ItemJsonSchema parseCompoundField(PsiClass rootClass, PsiDescriptorWrapper fieldWrapper, LevelCounter counter) {
        PsiDescriptor descriptor = fieldWrapper.getDescriptor();
        PsiType psiType = fieldWrapper.resolveFieldType();
        PsiModifierListOwner origin = descriptor.getFirstElement();
        boolean wrapArray = descriptor instanceof PsiArrayType;
        ItemJsonSchema result = this.parseJsonSchema(rootClass, psiType, counter);
        if (result instanceof ArraySchema) {
            ArraySchema a = (ArraySchema) result;
            if (TypeUtils.isSet(psiType) && !wrapArray) {
                a.setUniqueItems(true);
            }
            if (ValidUtils.notEmpty(origin)) {
                a.setMinItems(1);
            }
            IntegerRange integerRange = ValidUtils
                    .rangeSize(origin, this.enableBasicScope);
            a.setMinItems(integerRange.getMin(), this.enableBasicScope);
            a.setMaxItems(integerRange.getMax(), this.enableBasicScope);
        }
        return result;
    }

}
