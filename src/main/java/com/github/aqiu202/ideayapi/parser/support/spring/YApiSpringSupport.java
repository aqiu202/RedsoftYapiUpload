package com.github.aqiu202.ideayapi.parser.support.spring;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.mode.json5.Json;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.github.aqiu202.ideayapi.parser.support.YApiSupport;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiAnnotation;

import com.intellij.psi.PsiType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class YApiSpringSupport implements YApiSupport {

    public static final YApiSpringSupport INSTANCE = new YApiSpringSupport();

    @Override
    public void handleParam(ValueWrapper wrapper) {
        PsiDescriptor descriptor = wrapper.getSource();
        if (TypeUtils.isDate(descriptor.getType())
                && descriptor.hasAnnotation(SpringMVCConstants.DateTimeFormat)) {
            PsiAnnotation annotation = descriptor.findFirstAnnotation(SpringMVCConstants.DateTimeFormat);
            String pattern = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "pattern");
            if (StringUtils.isNotBlank(pattern)) {
                try {
                    wrapper.setExample(LocalDateTime.now().format(DateTimeFormatter
                            .ofPattern(pattern)));
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void handleProperty(ValueWrapper wrapper) {
        PsiDescriptor descriptor = wrapper.getSource();
        PsiType type = descriptor.getType();
        if (type != null && TypeUtils.isDate(type)
            && descriptor.hasAnnotation(SpringMVCConstants.DateTimeFormat)) {
            PsiAnnotation annotation = descriptor.findFirstAnnotation(SpringMVCConstants.DateTimeFormat);
            String pattern = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "pattern");
            if (StringUtils.isNotBlank(pattern)) {
                try {
                    String format = LocalDateTime.now().format(DateTimeFormatter
                        .ofPattern(pattern));
                    Jsonable jsonable = wrapper.getJson();
                    if (jsonable == null) {
                        wrapper.setExample(format);
                    } else {
                        if (jsonable instanceof Json) {
                            Json json = (Json) jsonable;
                            json.setValue(format);
                        }
                        if (jsonable instanceof ItemJsonSchema) {
                            ItemJsonSchema jsonSchema = (ItemJsonSchema) jsonable;
                            jsonSchema.setDefault(format);
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        }
    }
}
