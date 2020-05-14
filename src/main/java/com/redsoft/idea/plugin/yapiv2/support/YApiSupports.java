package com.redsoft.idea.plugin.yapiv2.support;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class YApiSupports implements YApiSupport {

    private final List<YApiSupport> supportList;

    public YApiSupports(List<YApiSupport> supports) {
        this.supportList = supports;
    }

    public YApiSupports(YApiSupport... supports) {
        this.supportList = new ArrayList<>(Arrays.asList(supports));
    }

    public void addSupport(YApiSupport support) {
        this.supportList.add(support);
        this.supportList.sort(Comparator.comparingInt(YApiSupport::getOrder));
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, YApiParam apiDTO) {
        this.supportList.forEach((i) -> i.handleMethod(psiMethod, apiDTO));
    }

    @Override
    public void handleParam(PsiParameter psiParameter, ValueWrapper wrapper) {
        this.supportList.forEach((i) -> i.handleParam(psiParameter, wrapper));
    }

    @Override
    public void handleField(PsiField psiField, ValueWrapper wrapper) {
        this.supportList.forEach((i) -> i.handleField(psiField, wrapper));
    }

}
