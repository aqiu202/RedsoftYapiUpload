package com.qdredsoft.plugin.model;

import java.util.Objects;

public class LongRange implements Range {

    public LongRange() {

    }

    public LongRange(Long min, Long max) {
        this.min = min;
        this.max = max;
    }
    public LongRange(Long min, Long max, boolean enableBasicScope) {
        if(Objects.nonNull(min)) {
            this.setMin(min, enableBasicScope);
        }
        if(Objects.nonNull(max)) {
            this.setMax(max, enableBasicScope);
        }
    }

    private Long min;

    private Long max;

    @Override
    public Long getMin() {
        return min;
    }

    public LongRange setMin(Long min, boolean enableBasicScope) {
        this.min = enableBasicScope ? min : (Long.MIN_VALUE == min ? null : min);
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
        this.max = enableBasicScope ? max : (Long.MAX_VALUE == max ? null : max);
        return this;
    }
    public LongRange setMax(Long max) {
        this.max = max;
        return this;
    }
}
