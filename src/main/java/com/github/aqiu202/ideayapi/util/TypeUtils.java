package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;
import com.github.aqiu202.ideayapi.model.Mock;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.parser.type.SimplePsiGenericTypeResolver;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * java类型基础工具类
 *
 * @author aqiu 2019/1/30 9:58 AM
 */
public class TypeUtils {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    @NonNls
    private static final Map<String, SchemaType> basicTypeMappings = new HashMap<>();

    private static final Map<String, Object> normalTypesPackages = new HashMap<>();
    private static final Map<String, Object> dateTypesPackages = new HashMap<>();

    private static final Map<String, LongRange> baseRangeMappings = new HashMap<>();
    private static final String mapTypeName = "java.util.Map";
    private static final String collectionTypeName = "java.util.Collection";
    private static final String setTypeName = "java.util.Set";
    private static final String enumTypeName = "java.lang.Enum";

    private static PsiClassType collectionType;
    private static PsiClassType mapType;
    private static PsiClassType setType;
    private static PsiClassType enumType;

    static {

        dateTypesPackages.put("java.sql.Timestamp", LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages.put("java.util.Date",
                LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages.put("java.sql.Date", LocalDate.now().format(dateFormat));
        dateTypesPackages.put("java.sql.Time", LocalTime.now().format(timeFormat));
        dateTypesPackages.put("java.time.LocalDateTime", LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages.put("java.time.LocalDate", LocalDate.now().format(dateFormat));
        dateTypesPackages.put("java.time.LocalTime", LocalTime.now().format(timeFormat));
        dateTypesPackages.put("org.joda.time.LocalDateTime", LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages.put("org.joda.time.LocalDate", LocalDate.now().format(dateFormat));
        dateTypesPackages.put("org.joda.time.LocalTime", LocalTime.now().format(timeFormat));
        dateTypesPackages.put("org.joda.time.DateTime", LocalDateTime.now().format(dateTimeFormat));

        normalTypesPackages.put("int", 1);
        normalTypesPackages.put("boolean", true);
        normalTypesPackages.put("byte", 1);
        normalTypesPackages.put("short", 1);
        normalTypesPackages.put("long", 1L);
        normalTypesPackages.put("float", 1.0F);
        normalTypesPackages.put("double", 1.0D);
        normalTypesPackages.put("char", 'a');
//        normalTypesPackages.put("MultipartFile", "file");
//        normalTypesPackages.put("MultipartFile[]", "files");
        normalTypesPackages.put("java.lang.Boolean", false);
        normalTypesPackages.put("java.lang.Byte", 1);
        normalTypesPackages.put("java.lang.Short", (short) 0);
        normalTypesPackages.put("java.lang.Integer", 1);
        normalTypesPackages.put("java.lang.Long", 1L);
        normalTypesPackages.put("java.lang.Float", 1L);
        normalTypesPackages.put("java.lang.Double", 1.0D);
        normalTypesPackages.put(SpringMVCConstants.MultipartFile, "file");
        normalTypesPackages.putAll(dateTypesPackages);
        normalTypesPackages.put("java.lang.String", "string");
        normalTypesPackages.put("java.math.BigDecimal", 0.111111);

        basicTypeMappings.put("int", SchemaType.integer);
        basicTypeMappings.put("boolean", SchemaType.bool);
        basicTypeMappings.put("byte", SchemaType.integer);
        basicTypeMappings.put("short", SchemaType.integer);
        basicTypeMappings.put("long", SchemaType.integer);
        basicTypeMappings.put("float", SchemaType.number);
        basicTypeMappings.put("double", SchemaType.number);
        basicTypeMappings.put("char", SchemaType.string);
        basicTypeMappings.put("java.lang.Boolean", SchemaType.bool);
        basicTypeMappings.put("java.lang.Byte", SchemaType.integer);
        basicTypeMappings.put("java.lang.Short", SchemaType.integer);
        basicTypeMappings.put("java.lang.Integer", SchemaType.integer);
        basicTypeMappings.put("java.lang.Long", SchemaType.integer);
        basicTypeMappings.put("java.math.BigInteger", SchemaType.integer);
        basicTypeMappings.put("java.lang.Float", SchemaType.number);
        basicTypeMappings.put("java.lang.Double", SchemaType.number);
        basicTypeMappings.put("java.sql.Timestamp", SchemaType.string);
        basicTypeMappings.put("java.util.Date", SchemaType.string);
        basicTypeMappings.put("java.sql.Date", SchemaType.string);
        basicTypeMappings.put("java.sql.Time", SchemaType.string);
        basicTypeMappings.put("java.time.LocalDateTime", SchemaType.string);
        basicTypeMappings.put("java.time.LocalDate", SchemaType.string);
        basicTypeMappings.put("java.time.LocalTime", SchemaType.string);
        basicTypeMappings.put("org.joda.time.LocalDateTime", SchemaType.string);
        basicTypeMappings.put("org.joda.time.LocalDate", SchemaType.string);
        basicTypeMappings.put("org.joda.time.LocalTime", SchemaType.string);
        basicTypeMappings.put("org.joda.time.DateTime", SchemaType.string);
        basicTypeMappings.put("java.lang.String", SchemaType.string);
        basicTypeMappings.put("java.math.BigDecimal", SchemaType.number);

//        baseRangeMappings.put("boolean", new LongRange(0, 1));
//        baseRangeMappings.put("java.lang.Boolean", new LongRange(0, 1));
        baseRangeMappings.put("byte", new LongRange(Byte.MIN_VALUE, Byte.MAX_VALUE));
        baseRangeMappings.put("short", new LongRange(Short.MIN_VALUE, Short.MAX_VALUE));
        baseRangeMappings.put("int", new LongRange(Integer.MIN_VALUE, Integer.MAX_VALUE));
        baseRangeMappings.put("long", new LongRange(Long.MIN_VALUE, Long.MAX_VALUE));
        baseRangeMappings.put("java.lang.Byte", new LongRange(Byte.MIN_VALUE, Byte.MAX_VALUE));
        baseRangeMappings.put("java.lang.Short", new LongRange(Short.MIN_VALUE, Short.MAX_VALUE));
        baseRangeMappings.put("java.lang.Integer",
                new LongRange((long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE));
        baseRangeMappings.put("java.lang.Long", new LongRange(Long.MIN_VALUE, Long.MAX_VALUE));

    }

    public static boolean isBasicType(PsiType type) {
        if (type == null) {
            return false;
        }
        return basicTypeMappings.containsKey(getTypePkName(type));
    }

    public static SchemaType getBasicSchema(PsiType type) {
        return basicTypeMappings.get(getTypePkName(type));
    }

    public static boolean isFile(PsiType psiType) {
        PsiType contentType;
        if (isCollection(psiType)) {
            contentType = resolveFirstGenericType(psiType);
        } else if (isArray(psiType)) {
            contentType = ((PsiArrayType) psiType).getComponentType();
        } else {
            contentType = psiType;
        }
        return SpringMVCConstants.MultipartFile.equals(getTypePkName(contentType));
    }

    public static PsiType resolveGenericType(PsiType psiType, int index) {
        return SimplePsiGenericTypeResolver.INSTANCE.resolveType(psiType, index);
    }

    public static PsiType resolveFirstGenericType(PsiType psiType) {
        return resolveGenericType(psiType, 0);
    }

    public static String getDefaultValueByPackageName(PsiType psiType) {
        return getListableValue(psiType, normalTypesPackages);
    }

    private static String getListableValue(PsiType psiType, Map<String, Object> source) {
        boolean isArray = isArray(psiType);
        if (isArray) {
            psiType = ((PsiArrayType) psiType).getComponentType();
        }
        Object result = source.get(getTypePkName(psiType));
        if (result == null) {
            return "";
        }
        return isArray ? result + "[]" : String.valueOf(result);
    }

    private static PsiClassType getMapType() {
        if (mapType == null) {
            mapType = PsiUtils.findPsiClassType(YApiSupportHolder.project, mapTypeName);
        }
        return mapType;
    }

    private static PsiClassType getCollectionType() {
        if (collectionType == null) {
            collectionType = PsiUtils.findPsiClassType(YApiSupportHolder.project, collectionTypeName);
        }
        return collectionType;
    }

    private static PsiClassType getSetType() {
        if (setType == null) {
            setType = PsiUtils.findPsiClassType(YApiSupportHolder.project, setTypeName);
        }
        return setType;
    }

    private static PsiClassType getEnumType() {
        if (enumType == null) {
            enumType = PsiUtils.findPsiClassType(YApiSupportHolder.project, enumTypeName);
        }
        return enumType;
    }

    public static boolean isArray(PsiType psiType) {
        return psiType instanceof PsiArrayType;
    }

    /**
     * 是否是Map类型或者是Map的封装类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isMap(PsiType psiType) {
        if (isNull(psiType)) {
            return false;
        }
        return getMapType().isAssignableFrom(psiType);
    }

    /**
     * 是否是集合类型或者是集合的封装类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isCollection(PsiType psiType) {
        if (isNull(psiType)) {
            return false;
        }
        return getCollectionType().isAssignableFrom(psiType);
    }

    private static boolean isNull(PsiType psiType) {
        return psiType == null;
    }

    /**
     * 是否是Set类型或者是Set的封装类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isSet(PsiType psiType) {
        if (isNull(psiType)) {
            return false;
        }
        return getSetType().isAssignableFrom(psiType);
    }

    /**
     * 是否是枚举类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isEnum(PsiType psiType) {
        if (isNull(psiType)) {
            return false;
        }
        return getEnumType().isAssignableFrom(psiType);
    }

    public static boolean hasBaseRange(PsiType psiType) {
        return baseRangeMappings.containsKey(getTypePkName(psiType));
    }

    public static boolean isDate(PsiType psiType) {
        return dateTypesPackages.containsKey(getTypePkName(psiType));
    }

    public static LongRange getBaseRange(PsiType psiType) {
        return baseRangeMappings.get(getTypePkName(psiType));
    }

    public static Mock formatMockType(PsiType psiType) {
        return formatMockType(getTypePkName(psiType), null);
    }

    public static String getTypePkName(PsiType psiType) {
        if (isNull(psiType)) {
            return null;
        }
        return psiType.getCanonicalText();
    }

    public static String getTypeName(PsiType psiType) {
        return psiType.getPresentableText();
    }

    public static String getTypeDesc(String desc) {
        return org.apache.commons.lang3.StringUtils.isBlank(desc) ? "" : ("(" + desc + ")");
    }

    public static void handleTypeDesc(ValueWrapper valueWrapper) {
        String desc = valueWrapper.getDesc();
        desc = (org.apache.commons.lang3.StringUtils.isBlank(desc) ? "" : desc) + getTypeDesc(valueWrapper.getTypeDesc());
        valueWrapper.setDesc(desc);
    }

    /**
     * mock type
     *
     * @param type type
     */
    public static Mock formatMockType(String type, String exampleMock) {
        //支持传入自定义mock
        if (StringUtils.isNotBlank(exampleMock)) {
            return new Mock(exampleMock);
        }
        switch (type) {
            case "short":
            case "int":
            case "long":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.math.BigInteger":
                return new Mock("@integer");
            case "boolean":
            case "java.lang.Boolean":
                return new Mock("@boolean");
            case "byte":
            case "java.lang.Byte":
                return new Mock("@byte");
            case "float":
            case "double":
            case "java.math.BigDecimal":
            case "java.lang.Double":
            case "java.lang.Float":
                return new Mock("@float");
            case "char":
            case "java.lang.Character":
                return new Mock("@char");
            case "java.time.LocalDateTime":
            case "java.time.LocalTime":
            case "java.time.LocalDate":
            case "org.joda.time.LocalTime":
            case "org.joda.time.LocalDate":
            case "org.joda.time.LocalDateTime":
            case "org.joda.time.DateTime":
            case "java.util.Date":
            case "java.sql.Date":
            case "java.sql.Time":
            case "java.sql.Timestamp":
                return new Mock("@timestamp");
            default:
                return new Mock("@string");
        }
    }

}
