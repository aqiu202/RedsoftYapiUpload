package com.qdredsoft.plugin.model;

public class IntegerRange implements Range {

    public IntegerRange(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    private Integer min;

    private Integer max;

    @Override
    public Integer getMin() {
        return min;
    }

    public IntegerRange setMin(Integer min) {
        this.min = min;
        return this;
    }

    @Override
    public Integer getMax() {
        return max;
    }

    public IntegerRange setMax(Integer max) {
        this.max = max;
        return this;
    }
}
