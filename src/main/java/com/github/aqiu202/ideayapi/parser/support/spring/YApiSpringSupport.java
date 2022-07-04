package com.github.aqiu202.ideayapi.parser.support.spring;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.support.YApiSupport;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiParameter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class YApiSpringSupport implements YApiSupport {

    public static final YApiSpringSupport INSTANCE = new YApiSpringSupport();

    @Override
    public void handleParam(ValueWrapper wrapper) {
        PsiParameter param = (PsiParameter) wrapper.getSource();
        if (TypeUtils.isDate(param.getType().getCanonicalText())
                && PsiAnnotationUtils.isAnnotatedWith(param, SpringMVCConstants.DateTimeFormat)) {
            String pattern = PsiAnnotationUtils.getPsiAnnotationAttributeValue(param, SpringMVCConstants.DateTimeFormat, "pattern");
            if (StringUtils.isNotBlank(pattern)) {
                try {
                    wrapper.setExample(LocalDateTime.now().format(DateTimeFormatter
                            .ofPattern(pattern)));
                } catch (Exception ignore) {
                }
            }
        }
        YApiSupport.super.handleParam(wrapper);
    }
}
