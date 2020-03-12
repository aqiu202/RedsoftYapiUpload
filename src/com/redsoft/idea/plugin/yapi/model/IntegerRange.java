package com.redsoft.idea.plugin.yapi.model;

public class IntegerRange implements Range {

    public IntegerRange(Integer min, Integer max, boolean enableBasicScope) {
        this.setMin(min, enableBasicScope);
        this.setMax(max, enableBasicScope);
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
        if(min == null && enableBasicScope) {
            this.min = Integer.MIN_VALUE;
        } else {
            this.min = min;
        }
        return this;
    }

    @Override
    public Integer getMax() {
        return max;
    }

    public IntegerRange setMax(Integer max, boolean enableBasicScope) {
        if(max == null && enableBasicScope) {
            this.max = Integer.MAX_VALUE;
        } else {
            this.max = max;
        }
        return this;
    }

}
