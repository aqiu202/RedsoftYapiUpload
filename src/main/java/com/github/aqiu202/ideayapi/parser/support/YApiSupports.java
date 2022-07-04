package com.github.aqiu202.ideayapi.parser.support;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiMethod;

import java.util.*;

/**
 * YApi的扩展的门面类
 */
public class YApiSupports implements YApiSupport {

    private final List<YApiSupport> supportList = new ArrayList<>();

    public YApiSupports(Collection<YApiSupport> supports) {
        this.supportList.addAll(supports);
        this.sort();
    }

    public YApiSupports(YApiSupport... supports) {
        this(Arrays.asList(supports));
    }

    public void addSupport(YApiSupport support) {
        this.supportList.add(support);
        this.sort();
    }

    private void sort() {
        this.supportList.sort(Comparator.comparingInt(YApiSupport::getOrder));
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, YApiParam apiDTO) {
        this.supportList.forEach((i) -> i.handleMethod(psiMethod, apiDTO));
    }

    @Override
    public void handleParam(ValueWrapper wrapper) {
        this.supportList.forEach((i) -> i.handleParam(wrapper));
    }

    @Override
    public void handleField(ValueWrapper wrapper) {
        this.supportList.forEach((i) -> i.handleField(wrapper));
    }

}
