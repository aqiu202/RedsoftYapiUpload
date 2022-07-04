package com.github.aqiu202.ideayapi.mode.schema;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;

@SuppressWarnings("unused")
public class ArraySchema extends ItemJsonSchema {

    public ArraySchema() {
        super(SchemaType.array);
    }

    private ItemJsonSchema items;

    private boolean uniqueItems;

    private Integer minItems;

    private Integer maxItems;

    public ItemJsonSchema getItems() {
        return items;
    }

    public ArraySchema setItems(ItemJsonSchema items) {
        this.items = items;
        return this;
    }

    public boolean isUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public void setMinItems(Integer minItems, boolean enableBasicScope) {
        if (minItems == null && enableBasicScope) {
            this.minItems = Integer.MIN_VALUE;
        } else {
            this.minItems = minItems;
        }
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public ArraySchema setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public void setMaxItems(Integer maxItems, boolean enableBasicScope) {
        if (maxItems == null && enableBasicScope) {
            this.maxItems = Integer.MAX_VALUE;
        } else {
            this.maxItems = maxItems;
        }
    }
}
