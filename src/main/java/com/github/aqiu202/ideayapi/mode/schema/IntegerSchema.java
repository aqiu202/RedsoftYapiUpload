package com.github.aqiu202.ideayapi.mode.schema;

import com.github.aqiu202.ideayapi.mode.schema.base.EnumableSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;
import com.github.aqiu202.ideayapi.model.range.LongRange;

@SuppressWarnings("unused")
public class IntegerSchema extends EnumableSchema {

    public IntegerSchema() {
        super(SchemaType.integer);
    }

    private Boolean exclusiveMinimum;

    private Boolean exclusiveMaximum;

    private Long minimum;

    private Long maximum;

    public boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Long getMinimum() {
        return minimum;
    }

    public void setMinimum(Long minimum) {
        this.minimum = minimum;
    }

    public Long getMaximum() {
        return maximum;
    }

    public IntegerSchema setMaximum(Long maximum) {
        this.maximum = maximum;
        return this;
    }

    public void setRange(LongRange longRange) {
        this.minimum = longRange.getMin();
        this.maximum = longRange.getMax();
    }
}
