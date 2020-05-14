package com.redsoft.idea.plugin.yapiv2.template;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class YApiTemplateContextType extends TemplateContextType {

    protected YApiTemplateContextType() {
        super("JAVA", "Java");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        return file.getName().endsWith(".java");
    }
}
