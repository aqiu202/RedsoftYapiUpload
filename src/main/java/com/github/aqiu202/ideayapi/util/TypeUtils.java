package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.constant.SpringWebFluxConstants;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;
import com.github.aqiu202.ideayapi.model.Mock;
import com.github.aqiu202.ideayapi.model.range.LongRange;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private static final Map<String, SchemaType> collectionTypeMappings = new HashMap<>();
    private static final Map<String, SchemaType> mapTypeMappings = new HashMap<>();

    private static final Map<String, Object> normalTypesPackages = new HashMap<>();
    private static final Map<String, Object> dateTypesPackages = new HashMap<>();

    private static final Map<String, LongRange> baseRangeMappings = new HashMap<>();
    private static final Map<String, String> fileTypes = new HashMap<>();
    /**
     * 泛型列表
     */
    private static final List<String> genericList = Arrays
            .asList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));


    static {

        dateTypesPackages.put("java.sql.Timestamp", LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages
                .put("java.util.Date",
                        LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages
                .put("java.sql.Date", LocalDate.now().format(dateFormat));
        dateTypesPackages
                .put("java.sql.Time", LocalTime.now().format(timeFormat));
        dateTypesPackages
                .put("java.time.LocalDateTime",
                        LocalDateTime.now().format(dateTimeFormat));
        dateTypesPackages
                .put("java.time.LocalDate", LocalDate.now().format(dateFormat));
        dateTypesPackages
                .put("java.time.LocalTime", LocalTime.now().format(timeFormat));

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
        basicTypeMappings.put("java.lang.String", SchemaType.string);
        basicTypeMappings.put("java.math.BigDecimal", SchemaType.number);

        collectionTypeMappings.put("java.lang.Iterable", SchemaType.array);
        collectionTypeMappings.put("java.util.List", SchemaType.array);
        collectionTypeMappings.put("java.util.Collection", SchemaType.array);
        collectionTypeMappings.put("java.util.ArrayList", SchemaType.array);
        collectionTypeMappings.put("java.util.LinkedList", SchemaType.array);
        collectionTypeMappings.put("java.util.Set", SchemaType.array);
        collectionTypeMappings.put("java.util.HashSet", SchemaType.array);
        collectionTypeMappings.put("java.util.LinkedHashSet", SchemaType.array);
        collectionTypeMappings.put(SpringWebFluxConstants.Flux, SchemaType.array);

        mapTypeMappings.put("java.util.Map", SchemaType.object);
        mapTypeMappings.put("java.util.HashMap", SchemaType.object);
        mapTypeMappings.put("java.util.LinkedHashMap", SchemaType.object);
        mapTypeMappings.put("java.util.TreeMap", SchemaType.object);

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

        fileTypes.put(SpringMVCConstants.MultipartFile, "file");
        fileTypes.put(SpringMVCConstants.MultipartFile + "[]", "file[]");
        fileTypes.put("List<" + SpringMVCConstants.MultipartFile + ">", "file[]");
        fileTypes.put("Set<" + SpringMVCConstants.MultipartFile + ">", "file[]");
        fileTypes.put("Collection<" + SpringMVCConstants.MultipartFile + ">", "file[]");

    }

    public static boolean isBasicType(String typePkName) {
        return basicTypeMappings.containsKey(typePkName);
    }

    public static SchemaType getBasicSchema(String typePkName) {
        return basicTypeMappings.get(typePkName);
    }

    public static boolean isCollectionType(String typePkName) {
        return collectionTypeMappings.containsKey(typePkName);
    }

    public static boolean isMapType(String typePkName) {
        return mapTypeMappings.containsKey(typePkName);
    }

    public static boolean isGenericType(String typePkName) {
        return genericList.contains(typePkName);
    }

    public static boolean isFile(String typePkName) {
        return fileTypes.containsKey(typePkName);
    }

    public static Object getDefaultValueByPackageName(String typePkName) {
        return getListableValue(typePkName, normalTypesPackages);
    }

    private static Object getListableValue(String typePkName, Map<String, Object> source) {
        boolean isArray = typePkName.endsWith("[]");
        if (isArray) {
            typePkName = typePkName.substring(0, typePkName.length() - 2);
        }
        Object result = source.get(typePkName);
        return isArray ? result + "[]" : result;
    }

    /**
     * 是否是Map类型或者是Map的封装类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isMap(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        if (isMapType(typePkName)) {
            return true;
        }
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (isMapType(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是Map类型或者是Map的封装类型
     *
     * @param project:    项目
     * @param typePkName: 类完整包名
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isMap(Project project, String typePkName) {
        if (isMapType(typePkName)) {
            return true;
        }
        PsiClassType psiType = PsiUtils.findPsiClassType(project, typePkName);
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (isMapType(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是集合类型或者是集合的封装类型
     *
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isCollection(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        if (isCollectionType(typePkName)) {
            return true;
        }
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (isCollectionType(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是集合类型或者是集合的封装类型
     *
     * @param project:    项目
     * @param typePkName: 类完整包名
     * @return {@link boolean}
     * @author aqiu 2019-07-03 09:43
     **/
    public static boolean isCollection(Project project, String typePkName) {
        if (isCollectionType(typePkName)) {
            return true;
        }
        PsiClassType psiType = PsiUtils.findPsiClassType(project, typePkName);
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (isCollectionType(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean hasGenericType(String typePkName) {
        String[] split = typePkName.replace(">", "").split("<");
        for (String s : split) {
            if (isGenericType(s)) {
                return true;
            }
        }
        return false;
    }

    public static String parseGenericType(@NotNull String typePkName, String genericType) {
        if (isGenericType(typePkName)) {
            return genericType;
        }
        return typePkName.replaceFirst("<[A-Z]>", "<" + genericType + ">");
    }

    public static boolean hasBaseRange(String typePkName) {
        return baseRangeMappings.containsKey(typePkName);
    }

    public static boolean isDate(String typePkName) {
        return dateTypesPackages.containsKey(typePkName);
    }

    public static LongRange getBaseRange(String typePkName) {
        return baseRangeMappings.get(typePkName);
    }

    public static Mock formatMockType(String type) {
        return formatMockType(type, null);
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
            case "int":
//            case "java.lang.Long":
//            case "java.lang.Integer":
//            case "java.lang.Short":
            case "Short":
            case "Integer":
            case "BigInteger":
            case "Long":
            case "short":
            case "long":
                return new Mock("@integer");
            case "boolean":
//            case "java.lang.Boolean":
            case "Boolean":
                return new Mock("@boolean");
            case "byte":
//            case "java.lang.Byte":
            case "Byte":
                return new Mock("@byte");
            case "float":
//            case "java.math.BigDecimal":
//            case "java.lang.Double":
//            case "java.lang.Float":
            case "BigDecimal":
            case "Double":
            case "Float":
            case "double":
                return new Mock("@float");
            case "char":
                return new Mock("@char");
//            case "java.time.LocalDateTime":
//            case "java.time.LocalTime":
//            case "java.time.LocalDate":
//            case "java.util.Date":
//            case "java.sql.Timestamp":
            case "Date":
            case "Timestamp":
            case "LocalDateTime":
            case "LocalTime":
            case "LocalDate":
                return new Mock("@timestamp");
            default:
                return new Mock("@string");
        }
    }
}