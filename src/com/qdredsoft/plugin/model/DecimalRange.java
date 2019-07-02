package com.qdredsoft.plugin.model;

import java.math.BigDecimal;

public class DecimalRange implements Range {

    public DecimalRange() {

    }
    public DecimalRange(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    private BigDecimal min;

    private BigDecimal max;

    @Override
    public BigDecimal getMin() {
        return min;
    }

    public DecimalRange setMin(BigDecimal min) {
        this.min = min;
        return this;
    }

    @Override
    public BigDecimal getMax() {
        return max;
    }

    public DecimalRange setMax(BigDecimal max) {
        this.max = max;
        return this;
    }
}
