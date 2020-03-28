package com.redsoft.idea.plugin.yapi.support;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapi.model.ValueWrapper;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class YApiSupports implements YApiSupport{

    private final List<YApiSupport> supportList = new ArrayList<>();

    public void addSupport(YApiSupport support) {
        this.supportList.add(support);
        supportList.sort(Comparator.comparingInt(YApiSupport::getOrder));
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, YApiDTO apiDTO) {
        this.supportList.forEach((i) ->i.handleMethod(psiMethod, apiDTO));
    }

    @Override
    public void handleParam(PsiParameter psiParameter, ValueWrapper wrapper) {
        this.supportList.forEach((i) ->i.handleParam(psiParameter, wrapper));
    }

    @Override
    public void handleField(PsiField psiField, ValueWrapper wrapper) {
        this.supportList.forEach((i) ->i.handleField(psiField, wrapper));
    }
}
