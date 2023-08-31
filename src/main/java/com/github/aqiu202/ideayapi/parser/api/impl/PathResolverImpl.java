package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.BasePathResolver;
import com.github.aqiu202.ideayapi.parser.api.PathPrefixResolver;
import com.github.aqiu202.ideayapi.parser.api.PathResolver;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PathResolverImpl implements PathResolver {

    private final PathPrefixResolver pathPrefixResolver = new PathPrefixResolverImpl();
    private final BasePathResolver methodPathResolver = new MethodPathResolverImpl();
    private final BasePathResolver clzPathResolver = new ClassPathResolverImpl();

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
