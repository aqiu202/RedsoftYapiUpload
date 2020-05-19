package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.res.ResponseFieldNameHandler;

public interface ObjectParser extends ObjectRawParser, ResponseFieldNameHandler {

    String getJsonResponse(PsiType psiType);

}
