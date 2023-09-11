package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.constant.LombokConstants;
import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class LombokDescriptorResolver extends SimpleDescriptorResolver {

    @Override
    public Collection<PsiDescriptor> resolveDescriptors(PsiClass c, Source source) {
        if (source == Source.REQUEST && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Data)
                && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Setter)) {
            return super.resolveDescriptors(c, source);
        } else if (source == Source.RESPONSE && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Data)
                && PsiAnnotationUtils.isNotAnnotatedWith(c, LombokConstants.Getter)) {
            return super.resolveDescriptors(c, source);
        }
        List<PsiDescriptor> descriptors = this.getMethodsDescriptors(c, source);
        Map<String, PsiDescriptor> descriptorMap = descriptors.stream().collect(Collectors.toMap(PsiDescriptor::getName, a -> a));
        List<PsiField> fields = null;
        if (source == Source.REQUEST) {
            fields = this.getSettableFields(c);
        } else if (source == Source.RESPONSE) {
            fields = this.getGettableFields(c);
        }
        if (CollectionUtils.isNotEmpty(fields)) {
            for (PsiField field : fields) {
                String fieldName = field.getName();
                PsiDescriptor descriptor = descriptorMap.get(fieldName);
                if (descriptor != null) {
                    descriptor.addElement(field);
                } else {
                    descriptors.add(new SimplePsiDescriptor(field));
                }
            }
        }
        return this.filterResults(descriptors, c);
    }

    protected List<PsiDescriptor> getMethodsDescriptors(PsiClass c, Source source) {
        List<PsiMethod> methods = null;
        if (source == Source.REQUEST) {
            methods = this.getSetters(c);
        } else if (source == Source.RESPONSE) {
            methods = this.getGetters(c);
        }
        if (methods == null) {
            methods = new ArrayList<>();
        }
        return methods.stream().map(SimplePsiDescriptor::of).collect(Collectors.toList());
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
