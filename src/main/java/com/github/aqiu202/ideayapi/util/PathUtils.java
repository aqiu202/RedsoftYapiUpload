package com.github.aqiu202.ideayapi.util;

import org.jetbrains.annotations.NotNull;

/**
 * 接口路由处理工具
 */
public final class PathUtils {

    private PathUtils() {
    }

    public static String pathFormat(@NotNull String path) {
        return pathFormat(path, true);
    }

    public static String pathFormat(@NotNull String path, boolean defaultRoot) {
        String pathStr = path.trim();
        if (!defaultRoot && StringUtils.isEmpty(pathStr)) {
            return "";
        }
        String split = "/";
        pathStr = pathStr.startsWith(split) ? pathStr : (split + pathStr);
        if (pathStr.endsWith("/") && pathStr.length() > 1) {
            pathStr = pathStr.substring(0, pathStr.length() - 1);
        }
        return pathStr;
    }
}
