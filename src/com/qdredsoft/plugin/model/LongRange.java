package com.qdredsoft.plugin.model;

public class LongRange implements Range {

    public LongRange() {

    }

    public LongRange(Long min, Long max) {
        this.min = min;
        this.max = max;
    }

    private Long min;

    private Long max;

    @Override
    public Long getMin() {
        return min;
    }

    public LongRange setMin(Long min) {
        this.min = min;
        return this;
    }

    @Override
    public Long getMax() {
        return max;
    }

    public LongRange setMax(Long max) {
        this.max = max;
        return this;
    }
}
