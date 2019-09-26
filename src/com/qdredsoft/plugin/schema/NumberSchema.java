package com.qdredsoft.plugin.schema;

import com.qdredsoft.plugin.model.DecimalRange;
import com.qdredsoft.plugin.schema.base.EnumableSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;
import java.math.BigDecimal;

public final class NumberSchema extends EnumableSchema {

    public NumberSchema(){
        super(SchemaType.number);
    }

    private BigDecimal minimum;

    private BigDecimal maximum;

    private Boolean exclusiveMinimum;

    private Boolean exclusiveMaximum;

    public boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public NumberSchema setExclusiveMinimum(boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    public boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public NumberSchema setExclusiveMaximum(boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public NumberSchema setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
        return this;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public NumberSchema setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
        return this;
    }

    public NumberSchema setRange(DecimalRange decimalRange) {
        this.minimum = decimalRange.getMin();
        this.maximum = decimalRange.getMax();
        return this;
    }
}
