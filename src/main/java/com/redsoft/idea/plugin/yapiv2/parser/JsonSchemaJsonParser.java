package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiField;
import com.redsoft.idea.plugin.yapiv2.schema.ArraySchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.SchemaType;

public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema getPojoSchema(String typePkName);

    ItemJsonSchema getOtherTypeSchema(String typePkName);

    ArraySchema getArraySchema(String typePkName);

    ItemJsonSchema getOtherFieldSchema(PsiField psiField);

    ItemJsonSchema getBaseFieldSchema(SchemaType schemaType, PsiField psiField);

    ItemJsonSchema getFieldSchema(PsiField psiField);

}
