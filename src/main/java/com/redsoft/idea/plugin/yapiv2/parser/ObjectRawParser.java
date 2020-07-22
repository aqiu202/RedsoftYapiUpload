package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;

public interface ObjectRawParser extends BaseObjectParser {

    String getRawResponse(PsiType psiType);
}
