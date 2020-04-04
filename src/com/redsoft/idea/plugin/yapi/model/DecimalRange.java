package com.redsoft.idea.plugin.yapi.model;

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

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    @Override
    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }
}
