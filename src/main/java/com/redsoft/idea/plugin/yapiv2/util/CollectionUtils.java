package com.redsoft.idea.plugin.yapiv2.util;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
}
