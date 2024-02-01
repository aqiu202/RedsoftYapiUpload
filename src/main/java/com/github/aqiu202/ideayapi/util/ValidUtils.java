package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.ValidConstants;
import com.github.aqiu202.ideayapi.model.range.DecimalRange;
import com.github.aqiu202.ideayapi.model.range.IntegerRange;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 数据校验工具类
 */
public final class ValidUtils {

    public static boolean hasAnnotation(PsiModifierListOwner psiModifierListOwner,
                                        String annotationName) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils.findAnnotation(psiModifierListOwner,
                annotationName);
        return Objects.nonNull(psiAnnotation);
    }

    public static boolean notNull(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.NotNull);
    }

    public static boolean notBlank(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.NotBlank);
    }

    public static boolean notNullOrBlank(PsiModifierListOwner psiModifierListOwner) {
        return notNull(psiModifierListOwner) || notBlank(psiModifierListOwner) ||
                notEmpty(psiModifierListOwner);
    }

    public static String getRequired(PsiModifierListOwner psiModifierListOwner) {
        return (notNull(psiModifierListOwner) || notBlank(psiModifierListOwner) ||
                notEmpty(psiModifierListOwner)) ? "1" : "0";
    }

    public static boolean notEmpty(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.NotEmpty);
    }

    public static boolean isPositive(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.Positive);
    }

    public static boolean isPositiveOrZero(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.PositiveOrZero);
    }

    public static boolean isNegative(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.Negative);
    }

    public static boolean isNegativeOrZero(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstants.NegativeOrZero);
    }

    public static IntegerRange rangeSize(PsiModifierListOwner psiModifierListOwner,
                                         boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.Size);
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

    public static IntegerRange rangeLength(PsiModifierListOwner psiModifierListOwner,
                                           boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.Length);
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

    public static Long getMin(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.Min);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return Long.valueOf(attributeValue);
            }
        }
        return null;
    }

    public static Long getMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.Max);
        if (Objects.nonNull(psiAnnotation)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(value)) {
                return Long.valueOf(value);
            }
        }
        return null;
    }

    public static LongRange range(PsiModifierListOwner psiModifierListOwner,
                                  boolean enableBasicScope) {
        LongRange result = null;
        Long min;
        Long max;
        if (Objects.nonNull(min = getMin(psiModifierListOwner))) {
            result = new LongRange();
            result.setMin(min);
        }
        if (Objects.nonNull(max = getMax(psiModifierListOwner))) {
            if (Objects.isNull(result)) {
                result = new LongRange();
            }
            result.setMax(max);
        }
        if (Objects.isNull(result)) {
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(psiModifierListOwner, ValidConstants.Range);
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

    public static DecimalRange rangeDecimal(PsiModifierListOwner psiModifierListOwner) {
        DecimalRange result = null;
        BigDecimal min;
        BigDecimal max;
        if (Objects.nonNull(min = getDecimalMin(psiModifierListOwner))) {
            result = new DecimalRange();
            result.setMin(min);
        }
        if (Objects.nonNull(max = getDecimalMax(psiModifierListOwner))) {
            if (Objects.isNull(result)) {
                result = new DecimalRange();
            }
            result.setMax(max);
        }
        if (Objects.isNull(result)) {
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(psiModifierListOwner, ValidConstants.Digits);
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

    public static BigDecimal getDecimalMin(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.DecimalMin);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return new BigDecimal(attributeValue);
            }
        }
        return null;
    }

    public static BigDecimal getDecimalMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.DecimalMax);
        if (Objects.nonNull(psiAnnotation)) {
            String attributeValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
            if (StringUtils.isNotBlank(attributeValue)) {
                return new BigDecimal(attributeValue);
            }
        }
        return null;
    }

    public static String getPattern(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(psiModifierListOwner, ValidConstants.Pattern);
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

