package com.qdredsoft.plugin.schema.root;

import com.google.gson.GsonBuilder;
import com.qdredsoft.plugin.schema.ObjectSchema;

public final class RootJsonSchema extends ObjectSchema {

    private String $schema = "http://json-schema.org/draft-04/schema#";

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
