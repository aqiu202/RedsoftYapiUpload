package com.qdredsoft.plugin.model;

public class LongRange implements Range {

    public LongRange() {

    }

    public LongRange(Long min, Long max) {
        this.min = min;
        this.max = max;
    }

    public LongRange(Long min, Long max, boolean enableBasicScope) {
        this.setMin(min, enableBasicScope);
        this.setMax(max, enableBasicScope);
    }

    private Long min;

    private Long max;

    @Override
    public Long getMin() {
        return min;
    }

    public LongRange setMin(Long min, boolean enableBasicScope) {
        if(min == null && enableBasicScope) {
            this.min = Long.MIN_VALUE;
        } else {
            this.min = min;
        }
        return this;
    }

    public LongRange setMin(Long min) {
        this.min = min;
        return this;
    }

    @Override
    public Long getMax() {
        return max;
    }

    public LongRange setMax(Long max, boolean enableBasicScope) {
        if(max == null && enableBasicScope) {
            this.max = Long.MAX_VALUE;
        } else {
            this.max = max;
        }
        return this;
    }

    public LongRange setMax(Long max) {
        this.max = max;
        return this;
    }
}
