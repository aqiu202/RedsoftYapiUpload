package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

import java.util.List;

public interface DescriptorResolver {

    List<String> getIgnoredProperties();

    void setPsiDescriptorParser(PsiDescriptorParser parser);

    PsiDescriptorParser getPsiDescriptorParser();

    default void addIgnoredProperty(String property) {
        List<String> ignoredProperties = this.getIgnoredProperties();
        if (ignoredProperties != null) {
            ignoredProperties.add(property);
        }
    }

    default void removeIgnoredProperty(String property) {
        List<String> ignoredProperties = this.getIgnoredProperties();
        if (CollectionUtils.isNotEmpty(ignoredProperties)) {
            ignoredProperties.remove(property);
        }
    }

    List<PsiDescriptor> resolveDescriptors(PsiType c, Source source);

    boolean filterPropertyName(String propertyName);

    boolean filter(PsiDescriptor descriptor, PsiClass c);

}
