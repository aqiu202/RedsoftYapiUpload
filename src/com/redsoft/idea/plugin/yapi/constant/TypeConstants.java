package com.redsoft.idea.plugin.yapi.constant;

import com.redsoft.idea.plugin.yapi.model.LongRange;
import com.redsoft.idea.plugin.yapi.schema.base.SchemaType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NonNls;

/**
 * 基本类
 *
 * @date 2019/1/30 9:58 AM
 */
public class TypeConstants {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    @NonNls
    public static final Map<String, Object> normalTypes = new HashMap<>();
    @NonNls
    public static final Map<String, SchemaType> normalTypeMappings = new HashMap<>();
    public static final Map<String, SchemaType> arrayTypeMappings = new HashMap<>();
    public static final Map<String, SchemaType> mapTypeMappings = new HashMap<>();

    public static final Map<String, Object> noramlTypesPackages = new HashMap<>();

//    public static final Map<String, Object> collectTypes = new HashMap<>();
//
//    public static final Map<String, Object> collectTypesPackages = new HashMap<>();

    public static final Map<String, LongRange> baseRangeMappings = new HashMap<>();
    /**
     * 泛型列表
     */
    public static final List<String> genericList = Arrays
            .asList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));


    static {
        normalTypes.put("int", 1);
        normalTypes.put("boolean", false);
        normalTypes.put("byte", 1);
        normalTypes.put("short", 1);
        normalTypes.put("long", 1L);
        normalTypes.put("float", 1.0F);
        normalTypes.put("double", 1.0D);
        normalTypes.put("char", 'a');
        normalTypes.put("Boolean", false);
        normalTypes.put("Byte", 1);
        normalTypes.put("Short", (short) 1);
        normalTypes.put("Integer", 1);
        normalTypes.put("Long", 1L);
        normalTypes.put("Float", 1.0F);
        normalTypes.put("Double", 1.0D);
        normalTypes.put("String", "String");
        normalTypes.put("Date", LocalDateTime.now().format(dateTimeFormat));
        normalTypes.put("Time", LocalTime.now().format(timeFormat));
        normalTypes.put("LocalDateTime", LocalDateTime.now().format(dateTimeFormat));
        normalTypes.put("LocalDate", LocalDate.now().format(dateFormat));
        normalTypes.put("LocalTime", LocalTime.now().format(timeFormat));
        normalTypes.put("BigDecimal", 0.111111);
        normalTypes.put("Timestamp", new Timestamp(System.currentTimeMillis()));
        normalTypes.put("MultipartFile", "file");
        normalTypes.put("MultipartFile[]", "file[]");
//        collectTypes.put("HashMap", "HashMap");
//        collectTypes.put("Map", "Map");
//        collectTypes.put("LinkedHashMap", "LinkedHashMap");

    }

    static {
        noramlTypesPackages.put("int", 1);
        noramlTypesPackages.put("boolean", true);
        noramlTypesPackages.put("byte", 1);
        noramlTypesPackages.put("short", 1);
        noramlTypesPackages.put("long", 1L);
        noramlTypesPackages.put("float", 1.0F);
        noramlTypesPackages.put("double", 1.0D);
        noramlTypesPackages.put("char", 'a');
        noramlTypesPackages.put("java.lang.Boolean", false);
        noramlTypesPackages.put("java.lang.Byte", 1);
        noramlTypesPackages.put("java.lang.Short", (short) 0);
        noramlTypesPackages.put("java.lang.Integer", 1);
        noramlTypesPackages.put("java.lang.Long", 1L);
        noramlTypesPackages.put("java.lang.Float", 1L);
        noramlTypesPackages.put("java.lang.Double", 1.0D);
        noramlTypesPackages.put("java.sql.Timestamp", new Timestamp(System.currentTimeMillis()));
        noramlTypesPackages
                .put("java.util.Date",
                        LocalDateTime.now().format(dateTimeFormat));
        noramlTypesPackages
                .put("java.sql.Date", LocalDate.now().format(dateFormat));
        noramlTypesPackages
                .put("java.sql.Time", LocalTime.now().format(timeFormat));
        noramlTypesPackages
                .put("java.time.LocalDateTime",
                        LocalDateTime.now().format(dateTimeFormat));
        noramlTypesPackages
                .put("java.time.LocalDate", LocalDate.now().format(dateFormat));
        noramlTypesPackages
                .put("java.time.LocalTime", LocalTime.now().format(timeFormat));
        noramlTypesPackages.put("java.lang.String", "String");
        noramlTypesPackages.put("java.math.BigDecimal", 1);

        normalTypeMappings.put("int", SchemaType.integer);
        normalTypeMappings.put("boolean", SchemaType.bool);
        normalTypeMappings.put("byte", SchemaType.integer);
        normalTypeMappings.put("short", SchemaType.integer);
        normalTypeMappings.put("long", SchemaType.integer);
        normalTypeMappings.put("float", SchemaType.number);
        normalTypeMappings.put("double", SchemaType.number);
        normalTypeMappings.put("char", SchemaType.string);
        normalTypeMappings.put("java.lang.Boolean", SchemaType.bool);
        normalTypeMappings.put("java.lang.Byte", SchemaType.integer);
        normalTypeMappings.put("java.lang.Short", SchemaType.integer);
        normalTypeMappings.put("java.lang.Integer", SchemaType.integer);
        normalTypeMappings.put("java.lang.Long", SchemaType.integer);
        normalTypeMappings.put("java.lang.Float", SchemaType.number);
        normalTypeMappings.put("java.lang.Double", SchemaType.number);
        normalTypeMappings.put("java.sql.Timestamp", SchemaType.string);
        normalTypeMappings.put("java.util.Date", SchemaType.string);
        normalTypeMappings.put("java.sql.Date", SchemaType.string);
        normalTypeMappings.put("java.sql.Time", SchemaType.string);
        normalTypeMappings.put("java.time.LocalDateTime", SchemaType.string);
        normalTypeMappings.put("java.time.LocalDate", SchemaType.string);
        normalTypeMappings.put("java.time.LocalTime", SchemaType.string);
        normalTypeMappings.put("java.lang.String", SchemaType.string);
        normalTypeMappings.put("java.math.BigDecimal", SchemaType.number);

        arrayTypeMappings.put("java.util.List", SchemaType.array);
        arrayTypeMappings.put("java.util.ArrayList", SchemaType.array);
        arrayTypeMappings.put("java.util.LinkedList", SchemaType.array);
        arrayTypeMappings.put("java.util.Set", SchemaType.array);
        arrayTypeMappings.put("java.util.HashSet", SchemaType.array);
        arrayTypeMappings.put("java.util.LinkedHashSet", SchemaType.array);

        mapTypeMappings.put("java.util.Map", SchemaType.object);
        mapTypeMappings.put("java.util.HashMap", SchemaType.object);
        mapTypeMappings.put("java.util.LinkedHashMap", SchemaType.object);
        mapTypeMappings.put("java.util.TreeMap", SchemaType.object);

//        collectTypesPackages.put("java.util.LinkedHashMap", "LinkedHashMap");
//        collectTypesPackages.put("java.util.HashMap", "HashMap");
//        collectTypesPackages.put("java.util.Map", "Map");
    }

    static {
        baseRangeMappings.put("byte", new LongRange(-128L, 127L));
        baseRangeMappings.put("short", new LongRange(-32768L, 32767L));
        baseRangeMappings
                .put("int", new LongRange((long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE));
        baseRangeMappings.put("long", new LongRange(Long.MIN_VALUE, Long.MAX_VALUE));
        baseRangeMappings.put("java.lang.Byte", new LongRange(-128L, 127L));
        baseRangeMappings.put("java.lang.Short", new LongRange(-32768L, 32767L));
        baseRangeMappings.put("java.lang.Integer",
                new LongRange((long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE));
        baseRangeMappings.put("java.lang.Long", new LongRange(Long.MIN_VALUE, Long.MAX_VALUE));

    }


    public static boolean isNormalType(String typeName) {
        return normalTypes.containsKey(typeName);
    }

    public static boolean isBaseType(String typePkName) {
        return normalTypeMappings.containsKey(typePkName);
    }

    public static boolean hasBaseRange(String typePkName) {
        return baseRangeMappings.containsKey(typePkName);
    }
}
