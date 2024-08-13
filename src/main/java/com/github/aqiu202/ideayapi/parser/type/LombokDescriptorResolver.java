package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.constant.LombokConstants;
import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LombokDescriptorResolver extends SimpleDescriptorResolver {

    @Override
    public List<PsiDescriptor> resolveDescriptors(PsiType type, Source source) {
        PsiClass c = PsiUtils.convertToClass(type);
        if (c == null) {
            return Collections.emptyList();
        }
        if (source == Source.REQUEST && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Data)
                && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Setter)) {
            return super.resolveDescriptors(type, source);
        } else if (source == Source.RESPONSE && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Data)
                && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Getter)) {
            return super.resolveDescriptors(type, source);
        }
        List<PsiDescriptor> descriptors = this.getMethodsDescriptors(c, type, source);
        Map<String, PsiDescriptor> descriptorMap = descriptors.stream().collect(Collectors.toMap(PsiDescriptor::getName, a -> a));
        List<PsiField> fields = null;
        if (source == Source.REQUEST) {
            fields = this.getSettableFields(c);
        } else if (source == Source.RESPONSE) {
            fields = this.getGettableFields(c);
        }
        if (CollectionUtils.isNotEmpty(fields)) {
            PsiDescriptorParser psiDescriptorParser = this.getPsiDescriptorParser();
            for (PsiField field : fields) {
                String fieldName = field.getName();
                PsiDescriptor descriptor = descriptorMap.get(fieldName);
                if (descriptor != null) {
                    descriptor.addElement(0, field);
                } else {
                    descriptors.add(psiDescriptorParser.parse(field, type));
                }
            }
        }
        return this.filterResults(descriptors, c);
    }

    public List<PsiField> getGettableFields(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllFields())
                .filter(this::isGettableField).collect(Collectors.toList());
    }

    public List<PsiField> getSettableFields(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllFields())
                .filter(this::isSettableField).collect(Collectors.toList());
    }

    public boolean isSettableField(PsiField field) {
        PsiModifierList modifierList = field.getModifierList();
        if (modifierList == null) {
            return false;
        }
        return !modifierList.hasModifierProperty(PsiModifier.STATIC)
                && !modifierList.hasModifierProperty(PsiModifier.FINAL);
    }

    public boolean isGettableField(PsiField field) {
        PsiModifierList modifierList = field.getModifierList();
        if (modifierList == null) {
            return false;
        }
        return !modifierList.hasModifierProperty(PsiModifier.STATIC);
    }
}
