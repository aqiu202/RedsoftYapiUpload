package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.parser.api.PathPrefixResolver;
import com.github.aqiu202.ideayapi.util.PathUtils;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_PREFIX;

public class PathPrefixResolverImpl implements PathPrefixResolver {

    private final static String PATH_ROOT = "/";

    @Override
    public String resolve(@Nullable PsiDocComment c, @Nullable PsiDocComment m) {
        final List<String> prefixes = new ArrayList<>();
        if (Objects.nonNull(c)) {
            String prefix = PsiDocUtils.getTagValueByName(c, TAG_PREFIX);
            if (Strings.isNotBlank(prefix) && !PATH_ROOT.equals(prefix)) {
                prefixes.add(PathUtils.pathFormat(prefix));
            }
        }
        if (Objects.nonNull(m)) {
            String prefix = PsiDocUtils.getTagValueByName(m, TAG_PREFIX);
            if (Strings.isNotBlank(prefix) && !PATH_ROOT.equals(prefix)) {
                prefixes.add(PathUtils.pathFormat(prefix));
            }
        }
        return prefixes.isEmpty() ? null : String.join("", prefixes);
    }

}
