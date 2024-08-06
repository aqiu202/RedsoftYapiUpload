package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.ValidConstants;
import com.github.aqiu202.ideayapi.model.range.DecimalRange;
import com.github.aqiu202.ideayapi.model.range.IntegerRange;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 数据校验工具类
 */
public final class ValidUtils {

    public static boolean hasAnnotation(PsiModifierListOwner descriptor,
                                        String... annotationNames) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils.findAnnotation(descriptor,
                annotationNames);
        return Objects.nonNull(psiAnnotation);
    }

    public static boolean hasAnnotation(PsiDescriptor descriptor,
                                        String... annotationNames) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils.findAnnotation(descriptor.getElements(),
                annotationNames);
        return Objects.nonNull(psiAnnotation);
    }

    public static boolean notNull(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.NotNull);
    }

    public static boolean notBlank(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.NotBlank);
    }

    public static boolean notEmpty(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.NotEmpty);
    }

    public static boolean notNullOrBlank(PsiDescriptor descriptor) {
        return notNull(descriptor) || notBlank(descriptor) ||
                notEmpty(descriptor);
    }

    public static boolean notNull(PsiModifierListOwner owner) {
        return hasAnnotation(owner, ValidConstants.NotNull);
    }

    public static boolean notBlank(PsiModifierListOwner owner) {
        return hasAnnotation(owner, ValidConstants.NotBlank);
    }

    public static boolean notEmpty(PsiModifierListOwner owner) {
        return hasAnnotation(owner, ValidConstants.NotEmpty);
    }

    public static boolean notNullOrBlank(PsiModifierListOwner owner) {
        return notNull(owner) || notBlank(owner) ||
                notEmpty(owner);
    }

    public static String getRequired(PsiDescriptor descriptor) {
        return notNullOrBlank(descriptor) ? "1" : "0";
    }

    public static String getRequired(PsiModifierListOwner owner) {
        return notNullOrBlank(owner) ? "1" : "0";
    }

    public static boolean isPositive(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.Positive);
    }

    public static boolean isPositiveOrZero(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.PositiveOrZero);
    }

    public static boolean isNegative(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.Negative);
    }

    public static boolean isNegativeOrZero(PsiDescriptor descriptor) {
        return hasAnnotation(descriptor, ValidConstants.NegativeOrZero);
    }

    public static IntegerRange rangeSize(PsiDescriptor descriptor,
                                         boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.Size);
        Integer min = null;
        Integer max = null;
        if (Objects.nonNull(psiAnnotation)) {
            String minVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "min");
            if (StringUtils.isNotBlank(minVal)) {
                min = Integer.valueOf(minVal);
            }
            String maxVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "max");
            if (StringUtils.isNotBlank(maxVal)) {
                max = Integer.valueOf(maxVal);
            }
        }
        return new IntegerRange(min, max, enableBasicScope);
    }

    public static IntegerRange rangeLength(PsiDescriptor descriptor,
                                           boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.Length);
        Integer min = null;
        Integer max = null;
        if (Objects.nonNull(psiAnnotation)) {
            String minVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "min");
            if (StringUtils.isNotBlank(minVal)) {
                min = Integer.valueOf(minVal);
            }
            String maxVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "max");
            if (StringUtils.isNotBlank(maxVal)) {
                max = Integer.valueOf(maxVal);
            }
        }
        return new IntegerRange(min, max, enableBasicScope);
    }

    public static Long getMin(PsiDescriptor descriptor) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.Min);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return Long.valueOf(attributeValue);
            }
        }
        return null;
    }

    public static Long getMax(PsiDescriptor descriptor) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.Max);
        if (Objects.nonNull(psiAnnotation)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(value)) {
                return Long.valueOf(value);
            }
        }
        return null;
    }

    public static LongRange range(PsiDescriptor descriptor,
                                  boolean enableBasicScope) {
        LongRange result = null;
        Long min;
        Long max;
        if (Objects.nonNull(min = getMin(descriptor))) {
            result = new LongRange();
            result.setMin(min);
        }
        if (Objects.nonNull(max = getMax(descriptor))) {
            if (Objects.isNull(result)) {
                result = new LongRange();
            }
            result.setMax(max);
        }
        if (Objects.isNull(result)) {
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(descriptor.getElements(), ValidConstants.Range);
            if (Objects.nonNull(psiAnnotation)) {
                String minVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "min");
                if (StringUtils.isNotBlank(minVal)) {
                    min = Long.valueOf(minVal);
                }
                String maxVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "max");
                if (StringUtils.isNotBlank(maxVal)) {
                    max = Long.valueOf(maxVal);
                }
                return new LongRange(min, max, enableBasicScope);
            }
        }
        return result;
    }

    public static DecimalRange rangeDecimal(PsiDescriptor descriptor) {
        DecimalRange result = null;
        BigDecimal min;
        BigDecimal max;
        if (Objects.nonNull(min = getDecimalMin(descriptor))) {
            result = new DecimalRange();
            result.setMin(min);
        }
        if (Objects.nonNull(max = getDecimalMax(descriptor))) {
            if (Objects.isNull(result)) {
                result = new DecimalRange();
            }
            result.setMax(max);
        }
        if (Objects.isNull(result)) {
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(descriptor.getElements(), ValidConstants.Digits);
            if (Objects.nonNull(psiAnnotation)) {
                String integerVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "integer");
                String fractionVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "fraction");
                if (StringUtils.isBlank(integerVal) || StringUtils.isBlank(fractionVal)) {
                    return null;
                }
                String maxValue = maxValue(Integer.parseInt(integerVal), Integer.parseInt(fractionVal));
                return new DecimalRange(
                        new BigDecimal("-" + maxValue),
                        new BigDecimal(maxValue));
            }
        }
        return null;
    }

    public static BigDecimal getDecimalMin(PsiDescriptor descriptor) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.DecimalMin);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return new BigDecimal(attributeValue);
            }
        }
        return null;
    }

    public static BigDecimal getDecimalMax(PsiDescriptor descriptor) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.DecimalMax);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return new BigDecimal(attributeValue);
            }
        }
        return null;
    }

    public static String getPattern(PsiDescriptor descriptor) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(descriptor.getElements(), ValidConstants.Pattern);
        if (Objects.nonNull(psiAnnotation)) {
            String pattern = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "regexp");
            if (StringUtils.isNotBlank(pattern)) {
                return pattern.replace("\\\\", "\\").replace("\"", "");
            }
        }
        return null;
    }

    private static String maxString(int len) {
        StringBuilder buffer = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            buffer.append("9");
        }
        return buffer.toString();
    }


    private static String maxValue(int integer, int fraction) {
        return maxString(integer).concat(".").concat(maxString(fraction));
    }

}

