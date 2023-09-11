package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.intellij.psi.PsiClass;

import java.util.Collection;

public interface DescriptorResolver {

    Collection<String> getIgnoredProperties();

    default void addIgnoredProperty(String property){
        Collection<String> ignoredProperties = this.getIgnoredProperties();
        if (ignoredProperties != null) {
            ignoredProperties.add(property);
        }
    }

    default void removeIgnoredProperty(String property) {
        Collection<String> ignoredProperties = this.getIgnoredProperties();
        if (CollectionUtils.isNotEmpty(ignoredProperties)) {
            ignoredProperties.remove(property);
        }
    }

    Collection<PsiDescriptor> resolveDescriptors(PsiClass c, Source source);

    boolean filterPropertyName(String propertyName);

    boolean filter(PsiDescriptor descriptor, PsiClass c);

}
