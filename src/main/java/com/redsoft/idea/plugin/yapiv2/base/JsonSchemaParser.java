package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.schema.ArraySchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.SchemaType;

public interface JsonSchemaParser {

    ItemJsonSchema getPojoSchema(String typePkName);

    ItemJsonSchema getOtherTypeSchema(PsiType psiType);

    ArraySchema getArraySchema(String typePkName);

    ItemJsonSchema getOtherFieldSchema(PsiField psiField);

    ItemJsonSchema getBaseFieldSchema(SchemaType schemaType, PsiField psiField);

    ItemJsonSchema getFieldSchema(PsiField psiField);

    String getSchemaResponse(PsiType psiType);

    String handleFieldName(String fieldName);

    String getRawResponse(PsiType psiType);
}
