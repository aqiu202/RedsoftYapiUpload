package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.res.DocTagValueHandler;
import com.redsoft.idea.plugin.yapiv2.res.ResponseFieldNameHandler;

public interface ObjectParser extends ObjectRawParser, ResponseFieldNameHandler,
        DocTagValueHandler {

    String getJsonResponse(PsiType psiType);

}
