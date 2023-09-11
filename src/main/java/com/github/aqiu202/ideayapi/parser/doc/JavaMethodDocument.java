package com.github.aqiu202.ideayapi.parser.doc;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.apache.commons.lang3.StringUtils;

public class JavaMethodDocument extends JavaDocument {
    private final String[] paramNames;

    public JavaMethodDocument(PsiMethod method) {
        super(method);
        PsiParameterList parameterList = method.getParameterList();
        int parametersCount = parameterList.getParametersCount();
        paramNames = new String[parametersCount];
        for (int i = 0; i < parametersCount; i++) {
            PsiParameter parameter = parameterList.getParameter(i);
            if (parameter == null) {
                continue;
            }
            paramNames[i] = parameter.getName();
        }
    }

    public String getParamValue(int paramIndex) {
        String name = this.paramNames[paramIndex];
        if (StringUtils.isNotBlank(name)) {
            return super.getParamValue(name);
        }
        return null;
    }

}
