package com.qdredsoft.plugin.schema.base;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class ItemJsonSchema {

    protected String $schema;

    protected SchemaType type;
    @SerializedName("default")
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

    public String get$schema() {
        return $schema;
    }

    public ItemJsonSchema set$schema(String $schema) {
        this.$schema = $schema;
        return this;
    }

    public String toPrettyJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
