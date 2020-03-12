package com.redsoft.idea.plugin.yapi.schema;

import com.redsoft.idea.plugin.yapi.schema.base.EnumableSchema;
import com.redsoft.idea.plugin.yapi.schema.base.SchemaType;

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

    public StringSchema setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public StringSchema setFormat(String format) {
        this.format = format;
        return this;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public StringSchema setMinLength(Integer minLength) {
        this.minLength = minLength;
        return this;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public StringSchema setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }
}
