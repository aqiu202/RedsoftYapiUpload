package com.redsoft.idea.plugin.yapiv2.api.impl;

import static com.redsoft.idea.plugin.yapiv2.constant.DocCommentConstants.TAG_PREFIX;

import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.PathPrefixResolver;
import com.redsoft.idea.plugin.yapiv2.util.PathUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

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
