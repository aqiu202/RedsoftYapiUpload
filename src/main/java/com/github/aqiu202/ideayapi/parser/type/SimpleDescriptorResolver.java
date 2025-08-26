package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDescriptorResolver extends AbstractDescriptorResolver {

    @Override
    public List<PsiDescriptor> resolveDescriptors(PsiType type, Source source) {
        PsiClass c = PsiUtils.convertToClass(type);
        if (c != null) {
            List<PsiDescriptor> methodsDescriptors = this.getMethodsDescriptors(c, type, source);
            for (PsiDescriptor methodsDescriptor : methodsDescriptors) {
                String name = methodsDescriptor.getName();
                PsiField field = c.findFieldByName(name, true);
                if (field != null) {
                    methodsDescriptor.addElement(field);
                }
            }
            return this.filterResults(methodsDescriptors, c);
        }
        return Collections.emptyList();
    }

    protected List<PsiDescriptor> getMethodsDescriptors(PsiClass c, PsiType type, Source source) {
        List<PsiMethod> methods = null;
        if (source == Source.REQUEST) {
            methods = this.getSetters(c);
        } else if (source == Source.RESPONSE) {
            methods = this.getGetters(c);
        }
        if (methods == null) {
            methods = new ArrayList<>();
        }
        PsiDescriptorParser psiDescriptorParser = this.getPsiDescriptorParser();
        return methods.stream().map(method -> psiDescriptorParser.parse(method, type)).distinct().collect(Collectors.toList());
    }

    protected List<PsiMethod> getGetters(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllMethods())
            .filter(this::filterObjectMethods)
            .filter(this::isGetter)
            .collect(Collectors.toList());
    }

    protected List<PsiMethod> getSetters(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllMethods())
            .filter(this::filterObjectMethods)
            .filter(this::isSetter)
            .collect(Collectors.toList());
    }

    protected boolean filterObjectMethods(PsiMethod method) {
        return !IGNORED_PROPERTIES.contains(method.getName());
    }

    protected boolean isGetter(PsiMethod method) {
        PsiType type = method.getReturnType();
        String methodName = method.getName();
        String prefix = StringUtils.equals("boolean", TypeUtils.getTypePkName(type)) ? "is" : "get";
        return !method.getModifierList().hasModifierProperty(PsiModifier.STATIC) &&
            methodName.startsWith(prefix) &&
            !methodName.equals(prefix) &&
            method.getParameterList().isEmpty();
    }

    protected boolean isSetter(PsiMethod method) {
        String prefix = "set";
        String methodName = method.getName();
        return !method.getModifierList().hasModifierProperty(PsiModifier.STATIC) &&
            methodName.startsWith(prefix) &&
            !methodName.equals(prefix) &&
            method.getParameterList().getParametersCount() == 1;
    }

}
