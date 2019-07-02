package com.qdredsoft.plugin.schema;

import com.qdredsoft.plugin.schema.base.ItemJsonSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectSchema extends ItemJsonSchema {

    public ObjectSchema(){
        super(SchemaType.object);
    }

    private Map<String, ItemJsonSchema> properties = new HashMap<>();

    private List<String> required;

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public Map<String, ItemJsonSchema> getProperties() {
        return properties;
    }

    public void setProperties(
            Map<String, ItemJsonSchema> properties) {
        this.properties = properties;
    }

    public ObjectSchema addProperty(String key, ItemJsonSchema jsonSchemaItem) {
        this.properties.put(key, jsonSchemaItem);
        return this;
    }

    public ObjectSchema addRequired(String key) {
        if(this.required == null) {
            this.required = new ArrayList<>();
        }
        this.required.add(key);
        return this;
    }

}
