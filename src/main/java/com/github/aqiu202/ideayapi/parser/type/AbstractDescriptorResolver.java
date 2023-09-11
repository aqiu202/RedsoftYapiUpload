package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDescriptorResolver implements DescriptorResolver {

    protected static final List<String> IGNORED_PROPERTIES = Arrays.asList("toString", "equals", "hasCode", "getClass");

    private final List<String> ignoredProperties = new ArrayList<>();

    @Override
    public Collection<String> getIgnoredProperties() {
        return this.ignoredProperties;
    }

    @Override
    public boolean filterPropertyName(String propertyName) {
        return !this.getIgnoredProperties().contains(propertyName);
    }

    @Override
    public boolean filter(PsiDescriptor descriptor, PsiClass c) {
        return descriptor.isValid() && !DeprecatedAssert.instance.isDeprecated(c)
                && !descriptor.isDeprecated();
    }

    protected List<PsiDescriptor> filterResults(Collection<PsiDescriptor> results, PsiClass c) {
        return results.stream()
                .filter(d -> this.filterPropertyName(d.getName()))
                .filter(d -> this.filter(d, c))
                .collect(Collectors.toList());
    }
}
