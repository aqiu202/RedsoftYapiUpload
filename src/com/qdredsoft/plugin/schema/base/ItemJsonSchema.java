package com.qdredsoft.plugin.schema.base;

public class ItemJsonSchema {

    protected SchemaType type;
    protected String _default;
    protected String description;

    public ItemJsonSchema(SchemaType type) {
        this.type = type;
    }

    public SchemaType getType() {
        return type;
    }

    public ItemJsonSchema setType(SchemaType type) {
        this.type = type;
        return this;
    }

    public String getDefault() {
        return _default;
    }

    public ItemJsonSchema setDefault(String _default) {
        this._default = _default;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemJsonSchema setDescription(String description) {
        this.description = description;
        return this;
    }
}
