package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.api.BasePathResolver;
import com.redsoft.idea.plugin.yapiv2.api.PathPrefixResolver;
import com.redsoft.idea.plugin.yapiv2.api.PathResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class PathResolverImpl implements PathResolver {

    private final PathPrefixResolver pathPrefixResolver = new PathPrefixResolverImpl();
    private final BasePathResolver clzPathResolver = new ClassPathResolverImpl();
    private final BasePathResolver methodPathResolver = new MethodPathResolverImpl();

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        clzPathResolver.resolve(c, target);
        methodPathResolver.resolve(m, target);
        String prefix = pathPrefixResolver.resolve(classDoc, methodDoc);
        if (Objects.nonNull(prefix)) {
            Set<String> paths = target.getPaths();
            LinkedHashSet<String> pathSet = paths.stream()
                    .map(p -> prefix + p).collect(Collectors.toCollection(LinkedHashSet::new));
            target.setPaths(pathSet);
        }
    }

}
