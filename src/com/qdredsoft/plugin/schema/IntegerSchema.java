package com.qdredsoft.plugin.schema;

import com.qdredsoft.plugin.model.LongRange;
import com.qdredsoft.plugin.schema.base.EnumableSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;

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

    public IntegerSchema setExclusiveMinimum(boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    public boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public IntegerSchema setExclusiveMaximum(boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    public Long getMinimum() {
        return minimum;
    }

    public IntegerSchema setMinimum(Long minimum) {
        this.minimum = minimum;
        return this;
    }

    public Long getMaximum() {
        return maximum;
    }

    public IntegerSchema setMaximum(Long maximum) {
        this.maximum = maximum;
        return this;
    }

    public IntegerSchema setRange(LongRange longRange) {
        this.minimum = longRange.getMin();
        this.maximum = longRange.getMax();
        return this;
    }
}
