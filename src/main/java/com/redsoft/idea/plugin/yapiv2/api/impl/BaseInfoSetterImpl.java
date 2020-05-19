package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.BaseInfoSetter;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.support.YApiSupportHolder;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class BaseInfoSetterImpl implements BaseInfoSetter {

    private final static String DESCRIPTION_VALUE = "description";

    @Override
    public void set(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        String classDesc = m.getText().replace(
                Objects.nonNull(m.getBody()) ? m.getBody().getText()
                        : "", "");
        if (!Strings.isEmpty(classDesc)) {
            classDesc = classDesc.replace("<", "&lt;").replace(">", "&gt;");
        }
        target.setDesc("<pre><code>" + classDesc + "</code></pre>");
        PsiDocComment docComment = m.getDocComment();
        if (Objects.nonNull(docComment)) {
            String title = PsiDocUtils.getTagDescription(docComment);
            if (Strings.isBlank(title)) {
                title = PsiDocUtils.getTagValueByName(docComment, DESCRIPTION_VALUE);
            }
            target.setTitle(title);
        }
        YApiSupportHolder.supports.handleMethod(m, target);
    }
}
