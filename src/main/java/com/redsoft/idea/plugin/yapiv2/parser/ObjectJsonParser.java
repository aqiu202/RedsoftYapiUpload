package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;

public interface ObjectJsonParser extends BaseObjectParser {

    String getJsonResponse(PsiType psiType);

}
