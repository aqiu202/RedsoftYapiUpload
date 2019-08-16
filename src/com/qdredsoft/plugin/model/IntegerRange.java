package com.qdredsoft.plugin.model;

import java.util.Objects;

public class IntegerRange implements Range {

    public IntegerRange(Integer min, Integer max, boolean enableBasicScope) {
        if(Objects.nonNull(min)) {
            this.setMin(min, enableBasicScope);
        }
        if(Objects.nonNull(max)) {
            this.setMax(max, enableBasicScope);
        }
    }
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

    public IntegerRange setMin(Integer min, boolean enableBasicScope) {
        this.min = enableBasicScope ? min : (Integer.MIN_VALUE == min ? null : min);
        return this;
    }
    public IntegerRange setMin(Integer min) {
        return this.setMin(min, true);
    }

    @Override
    public Integer getMax() {
        return max;
    }

    public IntegerRange setMax(Integer max, boolean enableBasicScope) {
        this.max = enableBasicScope ? max : (Integer.MAX_VALUE == max ? null : max);
        return this;
    }
    public IntegerRange setMax(Integer max) {
        return this.setMax(max, true);
    }
}
