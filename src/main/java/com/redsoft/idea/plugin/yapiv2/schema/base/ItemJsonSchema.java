package com.redsoft.idea.plugin.yapiv2.schema.base;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.redsoft.idea.plugin.yapiv2.model.Mock;

@SuppressWarnings("unused")
public class ItemJsonSchema {

    private String $schema;

    private SchemaType type;
    @SerializedName("default")
    private String _default;
    private String description;
    private Mock mock;

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

    public Mock getMock() {
        return mock;
    }

    public void setMock(Mock mock) {
        this.mock = mock;
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
