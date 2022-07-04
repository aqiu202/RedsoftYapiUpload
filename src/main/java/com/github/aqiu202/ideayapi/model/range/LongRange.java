package com.github.aqiu202.ideayapi.model.range;

import org.jetbrains.annotations.NotNull;

public class LongRange implements Range {

    public LongRange() {

    }

    public LongRange(@NotNull Number min, @NotNull Number max) {
        this.min = min.longValue();
        this.max = max.longValue();
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

    public void setMin(Long min, boolean enableBasicScope) {
        if (min == null && enableBasicScope) {
            this.min = Long.MIN_VALUE;
        } else {
            this.min = min;
        }
    }

    public void setMin(Long min) {
        this.min = min;
    }

    @Override
    public Long getMax() {
        return max;
    }

    public void setMax(Long max, boolean enableBasicScope) {
        if (max == null && enableBasicScope) {
            this.max = Long.MAX_VALUE;
        } else {
            this.max = max;
        }
    }

    public void setMax(Long max) {
        this.max = max;
    }
}
