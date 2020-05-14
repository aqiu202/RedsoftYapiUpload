package com.redsoft.idea.plugin.yapiv2.util;

import org.jetbrains.annotations.NotNull;

public final class PathUtils {

    private PathUtils() {
    }

    public static String pathFormat(@NotNull StringBuilder path) {
        return pathFormat(path.toString());
    }

    public static String pathFormat(@NotNull String path) {
        String pathStr = path.trim();
        String split = "/";
        pathStr = pathStr.startsWith(split) ? pathStr : (split + pathStr);
        if (pathStr.endsWith("/") && pathStr.length() > 1) {
            pathStr = pathStr.substring(0, pathStr.length() - 1);
        }
        return pathStr;
    }
}
