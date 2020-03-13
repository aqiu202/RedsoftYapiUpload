package com.redsoft.idea.plugin.yapi.schema.base;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ItemJsonSchema {

    private String $schema;

    private SchemaType type;
    @SerializedName("default")
    private String _default;
    private String description;

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

    public void setDefault(String _default) {
        this._default = _default;
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

    public void set$schema(String $schema) {
        this.$schema = $schema;
    }

    public String toPrettyJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
