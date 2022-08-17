package com.github.aqiu202.ideayapi.model;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EnumFields extends ArrayList<EnumField> {

    private EnumDescriptionConverter enumDescriptionConverter = (enumFields -> {
        StringBuilder builder = new StringBuilder("枚举：\n");
        for (EnumField enumField : enumFields) {
            builder.append(enumField.getName()).append(" - ").append(enumField.getDescription()).append("\n");
        }
        return builder.toString();
    });

    public EnumFields(@NotNull Collection<? extends EnumField> c) {
        super(c);
    }

    public List<String> getFieldNames() {
        return this.stream().map(EnumField::getName).collect(Collectors.toList());
    }

    public List<String> getFieldDescriptions() {
        return this.stream().map(EnumField::getDescription).collect(Collectors.toList());
    }

    public String getFieldsName() {
        return this.stream().map(EnumField::getName).collect(Collectors.joining(","));
    }

    public String getFieldsDescription() {
        return this.getFieldDescriptions().stream()
                .map(description -> StringUtils.isBlank(description) ? "-" : description)
                .collect(Collectors.joining(","));
    }

    public String getDescriptionString() {
        return this.enumDescriptionConverter.convert(this);
    }

    public EnumDescriptionConverter getEnumDescriptionConverter() {
        return enumDescriptionConverter;
    }

    public void setEnumDescriptionConverter(EnumDescriptionConverter enumDescriptionConverter) {
        this.enumDescriptionConverter = enumDescriptionConverter;
    }

    interface EnumDescriptionConverter {
        String convert(List<EnumField> enumFields);
    }

}
