package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.json5.Json;
import com.redsoft.idea.plugin.yapiv2.json5.JsonArray;
import com.redsoft.idea.plugin.yapiv2.json5.JsonObject;

public interface Json5JsonParser extends ObjectJsonParser {

    JsonArray<?> getJsonArray(String typePkName);

    JsonObject getJsonObject(String typePkName);

    Json<?> getJson(PsiType psiType);

    Json<?> getOtherJson(String typePkName);

    Json<?> getJsonByField(PsiField psiField);
}
