package com.github.aqiu202.ideayapi.parser.support.jackson;

import com.github.aqiu202.ideayapi.constant.JacksonConstants;
import com.github.aqiu202.ideayapi.mode.json5.Json;
import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.github.aqiu202.ideayapi.parser.support.YApiSupport;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiVariable;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class YApiJacksonSupport implements YApiSupport {

    public static final YApiJacksonSupport INSTANCE = new YApiJacksonSupport();

    @Override
    public void handleField(ValueWrapper wrapper) {
        PsiVariable field = wrapper.getSource();
        if (PsiAnnotationUtils.isAnnotatedWith(field, JacksonConstants.JSON_PROPERTY)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(field, JacksonConstants.JSON_PROPERTY, "value");
            if (StringUtils.isNotBlank(value)) {
                wrapper.setName(value);
            }
        }
        if (TypeUtils.isDate(field.getType().getCanonicalText())
                && PsiAnnotationUtils.isAnnotatedWith(field, JacksonConstants.JSON_FORMAT)) {
            String pattern = PsiAnnotationUtils.getPsiAnnotationAttributeValue(field, JacksonConstants.JSON_FORMAT, "pattern");
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
        YApiSupport.super.handleField(wrapper);
    }

    @Override
    public boolean isIgnored(PsiField field, PsiClass psiClass) {
        if (PsiAnnotationUtils.isAnnotatedWith(psiClass, JacksonConstants.JSON_IGNORE_PROPERTIES)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiClass, JacksonConstants.JSON_IGNORE_PROPERTIES);
            String[] ignoredFields = value.replaceAll("[{} ]", "").split(",");
            return Arrays.asList(ignoredFields).contains(field.getName());
        }
        if (PsiAnnotationUtils.isAnnotatedWith(psiClass, JacksonConstants.JSON_INCLUDE_PROPERTIES)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiClass, JacksonConstants.JSON_INCLUDE_PROPERTIES);
            String[] ignoredFields = value.replaceAll("[{} ]", "").split(",");
            return !Arrays.asList(ignoredFields).contains(field.getName());
        }
        if (PsiAnnotationUtils.isAnnotatedWith(field, JacksonConstants.JSON_IGNORE)) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(field, JacksonConstants.JSON_IGNORE, "value");
            return "true".equals(value);
        }
        return YApiSupport.super.isIgnored(field, psiClass);
    }
}
