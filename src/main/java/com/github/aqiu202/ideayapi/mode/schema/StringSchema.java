package com.github.aqiu202.ideayapi.mode.schema;

import com.github.aqiu202.ideayapi.mode.schema.base.EnumableSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;

@SuppressWarnings("unused")
public final class StringSchema extends EnumableSchema {

    public StringSchema() {
        super(SchemaType.string);
    }

    private String pattern;

    private String format;

    private Integer minLength;

    private Integer maxLength;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
