package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.abs.Source;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDescriptorResolver extends AbstractDescriptorResolver {

    @Override
    public Collection<PsiDescriptor> resolveDescriptors(PsiClass c, Source source) {
        List<PsiMethod> methods = null;
        if (source == Source.REQUEST) {
            methods = this.getSetters(c);
        } else if (source == Source.RESPONSE) {
            methods = this.getGetters(c);
        }
        if (CollectionUtils.isNotEmpty(methods)) {
            return methods.stream().map(SimplePsiDescriptor::of)
                    .filter(d -> this.filterPropertyName(d.getName()))
                    .peek(d -> {
                        String name = d.getName();
                        PsiField field = c.findFieldByName(name, true);
                        if (field != null) {
                            d.addElement(field);
                        }
                    })
                    .filter(d -> this.filter(d, c))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected List<PsiMethod> getGetters(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllMethods())
                .filter(this::isGetter).collect(Collectors.toList());
    }

    protected List<PsiMethod> getSetters(PsiClass psiClass) {
        return Arrays.stream(psiClass.getAllMethods())
                .filter(this::isSetter).collect(Collectors.toList());
    }

    protected boolean isGetter(PsiMethod method) {
        String name = method.getName();
        if (IGNORED_PROPERTIES.contains(name)) {
            return false;
        }
        PsiType type = method.getReturnType();
        String prefix = StringUtils.equals("boolean", TypeUtils.getTypePkName(type)) ? "is" : "get";
        return !method.getModifierList().hasModifierProperty(PsiModifier.STATIC)
                && name.startsWith(prefix)
                && method.getParameterList().isEmpty();
    }

    protected boolean isSetter(PsiMethod method) {
        String name = method.getName();
        if (IGNORED_PROPERTIES.contains(name)) {
            return false;
        }
        return !method.getModifierList().hasModifierProperty(PsiModifier.STATIC)
                && name.startsWith("set") && method.getParameterList().getParametersCount() == 1;
    }

}
