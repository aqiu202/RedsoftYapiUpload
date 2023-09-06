package com.github.aqiu202.ideayapi.mode.schema.base;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EnumableSchema extends ItemJsonSchema implements Enumable {

    public EnumableSchema(SchemaType type) {
        super(type);
    }

    @SerializedName("enum")
    private List<String> _enum;
    private String enumDesc;

    public EnumableSchema setEnum(List<String> _enum) {
        this._enum = _enum;
        return this;
    }

    public EnumableSchema setEnumDesc(String enumDesc) {
        this.enumDesc = enumDesc;
        return this;
    }

    @Override
    public List<String> getEnum() {
        return _enum;
    }

    @Override
    public String getEnumDesc() {
        return enumDesc;
    }

    public EnumableSchema addEnum(String _enum) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(_enum);
        return this;
    }
}
