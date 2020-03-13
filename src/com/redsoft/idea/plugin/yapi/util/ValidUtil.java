package com.redsoft.idea.plugin.yapi.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.constant.ValidConstants;
import com.redsoft.idea.plugin.yapi.model.DecimalRange;
import com.redsoft.idea.plugin.yapi.model.IntegerRange;
import com.redsoft.idea.plugin.yapi.model.LongRange;
import java.math.BigDecimal;
import java.util.Objects;

public final class ValidUtil {

    private ValidUtil() {
    }

    public static boolean hasAnnotation(PsiModifierListOwner psiModifierListOwner,
            String annotationName) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil.findAnnotation(psiModifierListOwner,
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

    public static IntegerRange rangeSize(PsiModifierListOwner psiModifierListOwner, boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.Size);
        if (Objects.nonNull(psiAnnotation)) {
            return new IntegerRange(
                    Integer.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("min")).getText()),
                    Integer.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("max")).getText()),
                    enableBasicScope);
        }
        return new IntegerRange(null, null,enableBasicScope);
    }

    public static IntegerRange rangeLength(PsiModifierListOwner psiModifierListOwner, boolean enableBasicScope) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.Length);
        if (Objects.nonNull(psiAnnotation)) {
            return new IntegerRange(
                    Integer.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("min")).getText()),
                    Integer.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("max")).getText()),
                    enableBasicScope);
        }
        return new IntegerRange(null, null, enableBasicScope);
    }

    public static Long getMin(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.Min);
        if(Objects.nonNull(psiAnnotation)) {
            return Long.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText());
        }
        return null;
    }

    public static Long getMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.Max);
        if(Objects.nonNull(psiAnnotation)) {
            return Long.valueOf(Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText());
        }
        return null;
    }

    public static LongRange range(PsiModifierListOwner psiModifierListOwner, boolean enableBasicScope) {
        LongRange result = null;
        Long min;
        Long max;
        if(Objects.nonNull(min = getMin(psiModifierListOwner))) {
            result = new LongRange();
            result.setMin(min);
        }
        if(Objects.nonNull(max = getMax(psiModifierListOwner))) {
            if(Objects.isNull(result)) {
                result = new LongRange();
            }
            result.setMax(max);
        }
        if(Objects.isNull(result)){
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiModifierListOwner, ValidConstants.Range);
            if (Objects.nonNull(psiAnnotation)) {
                return new LongRange(
                        Long.valueOf(
                                Objects.requireNonNull(psiAnnotation.findAttributeValue("min")).getText()),
                        Long.valueOf(
                                Objects.requireNonNull(psiAnnotation.findAttributeValue("max")).getText()),
                        enableBasicScope);
            }
        }
        return result;
    }

    public static DecimalRange rangeDecimal(PsiModifierListOwner psiModifierListOwner) {
        DecimalRange result = null;
        BigDecimal min;
        BigDecimal max;
        if(Objects.nonNull(min = getDecimalMin(psiModifierListOwner))) {
            result = new DecimalRange();
            result.setMin(min);
        }
        if(Objects.nonNull(max = getDecimalMax(psiModifierListOwner))) {
            if(Objects.isNull(result)) {
                result = new DecimalRange();
            }
            result.setMax(max);
        }
        if(Objects.isNull(result)) {
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiModifierListOwner, ValidConstants.Digits);
            if (Objects.nonNull(psiAnnotation)) {
                int integer = Integer.parseInt(
                        Objects.requireNonNull(psiAnnotation.findAttributeValue("integer"))
                                .getText());
                int fraction = Integer.parseInt(
                        Objects.requireNonNull(psiAnnotation.findAttributeValue("fraction"))
                                .getText());
                if (integer == 0 || fraction == 0) {
                    return null;
                }
                String maxValue = maxValue(integer, fraction);
                return new DecimalRange(
                        new BigDecimal("-" + maxValue),
                        new BigDecimal(maxValue));
            }
        }
        return null;
    }

    public static BigDecimal getDecimalMin(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.DecimalMin);
        if (Objects.nonNull(psiAnnotation)) {
            return new BigDecimal(Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText());
        }
        return null;
    }

    public static BigDecimal getDecimalMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.DecimalMax);
        if (Objects.nonNull(psiAnnotation)) {
            return new BigDecimal(Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText());
        }
        return null;
    }

    public static String getPattern(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstants.Pattern);
        if (Objects.nonNull(psiAnnotation)) {
            String pattern = Objects.requireNonNull(psiAnnotation.findAttributeValue("regexp")).getText();
            if(Strings.isNotBlank(pattern)) {
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

