package com.qdredsoft.plugin.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.qdredsoft.plugin.constant.ValidConstant;
import com.qdredsoft.plugin.model.DecimalRange;
import com.qdredsoft.plugin.model.IntegerRange;
import com.qdredsoft.plugin.model.LongRange;
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
        return hasAnnotation(psiModifierListOwner, ValidConstant.NotNull);
    }

    public static boolean notBlank(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.NotBlank);
    }

    public static boolean notEmpty(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.NotEmpty);
    }

    public static boolean isPositive(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.Positive);
    }

    public static boolean isPositiveOrZero(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.PositiveOrZero);
    }

    public static boolean isNegative(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.Negative);
    }

    public static boolean isNegativeOrZero(PsiModifierListOwner psiModifierListOwner) {
        return hasAnnotation(psiModifierListOwner, ValidConstant.NegativeOrZero);
    }

    public static IntegerRange rangeSize(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.Size);
        if (Objects.nonNull(psiAnnotation)) {
            return new IntegerRange(
                    Integer.valueOf(psiAnnotation.findAttributeValue("min").getText()),
                    Integer.valueOf(psiAnnotation.findAttributeValue("max").getText()));
        }
        return new IntegerRange(0, Integer.MAX_VALUE);
    }

    public static IntegerRange rangeLength(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.Length);
        if (Objects.nonNull(psiAnnotation)) {
            return new IntegerRange(
                    Integer.valueOf(psiAnnotation.findAttributeValue("min").getText()),
                    Integer.valueOf(psiAnnotation.findAttributeValue("max").getText()));
        }
        return new IntegerRange(0, Integer.MAX_VALUE);
    }

    public static Long getMin(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.Min);
        if(Objects.nonNull(psiAnnotation)) {
            return Long.valueOf(psiAnnotation.findAttributeValue("value").getText());
        }
        return null;
    }

    public static Long getMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.Max);
        if(Objects.nonNull(psiAnnotation)) {
            return Long.valueOf(psiAnnotation.findAttributeValue("value").getText());
        }
        return null;
    }

    public static LongRange range(PsiModifierListOwner psiModifierListOwner) {
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
                    .findAnnotation(psiModifierListOwner, ValidConstant.Range);
            if (Objects.nonNull(psiAnnotation)) {
                return new LongRange(
                        Long.valueOf(psiAnnotation.findAttributeValue("min").getText()),
                        Long.valueOf(psiAnnotation.findAttributeValue("max").getText()));
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
                    .findAnnotation(psiModifierListOwner, ValidConstant.Digits);
            if (Objects.nonNull(psiAnnotation)) {
                Integer integer = Integer
                        .valueOf(psiAnnotation.findAttributeValue("integer").getText());
                Integer fraction = Integer
                        .valueOf(psiAnnotation.findAttributeValue("fraction").getText());
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
                .findAnnotation(psiModifierListOwner, ValidConstant.DecimalMin);
        if (Objects.nonNull(psiAnnotation)) {
            return new BigDecimal(psiAnnotation.findAttributeValue("value").getText());
        }
        return null;
    }

    public static BigDecimal getDecimalMax(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.DecimalMax);
        if (Objects.nonNull(psiAnnotation)) {
            return new BigDecimal(psiAnnotation.findAttributeValue("value").getText());
        }
        return null;
    }

    public static String getPattern(PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.Pattern);
        if (Objects.nonNull(psiAnnotation)) {
            return psiAnnotation.findAttributeValue("value").getText();
        }
        return null;
    }

    private static String maxString(int len) {
        StringBuffer buffer = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            buffer.append("9");
        }
        return buffer.toString();
    }


    private static String maxValue(int integer, int fraction) {
        return maxString(integer).concat(".").concat(maxString(fraction));
    }

}

