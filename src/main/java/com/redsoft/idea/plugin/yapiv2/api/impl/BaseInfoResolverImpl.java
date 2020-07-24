package com.redsoft.idea.plugin.yapiv2.api.impl;

import static com.redsoft.idea.plugin.yapiv2.constant.DocCommentConstants.TAG_DESCRIPTION;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.BaseInfoResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.support.YApiSupportHolder;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class BaseInfoResolverImpl implements BaseInfoResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
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
                title = PsiDocUtils.getTagValueByName(docComment, TAG_DESCRIPTION);
            }
            target.setTitle(title);
        }
        YApiSupportHolder.supports.handleMethod(m, target);
    }
}
