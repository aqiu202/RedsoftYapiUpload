package com.qdredsoft.plugin.schema;

import com.qdredsoft.plugin.schema.base.ItemJsonSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;

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

    public ArraySchema setUniqueItems(boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
        return this;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public ArraySchema setMinItems(Integer minItems) {
        this.minItems = minItems;
        return this;
    }

    public ArraySchema setMinItems(Integer minItems, boolean enableBasicScope) {
        if(minItems == null && enableBasicScope) {
            this.minItems = Integer.MIN_VALUE;
        } else {
            this.minItems = minItems;
        }
        return this;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public ArraySchema setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public ArraySchema setMaxItems(Integer maxItems, boolean enableBasicScope) {
        if(maxItems == null && enableBasicScope) {
            this.maxItems = Integer.MAX_VALUE;
        } else {
            this.maxItems = maxItems;
        }
        return this;
    }
}
