package com.github.aqiu202.ideayapi.mode.schema.base;

import com.github.aqiu202.ideayapi.model.Mock;
import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ItemJsonSchema implements Jsonable {

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

    @Override
    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
