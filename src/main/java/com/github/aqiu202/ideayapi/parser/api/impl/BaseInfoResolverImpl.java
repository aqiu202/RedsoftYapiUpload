package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.BaseInfoResolver;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_DESCRIPTION;

public class BaseInfoResolverImpl implements BaseInfoResolver {

    private final YApiProjectProperty property;
    public BaseInfoResolverImpl(YApiProjectProperty property) {
        this.property = property;
    }
    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        if (this.property.isUseMethodDefineAsRemark()) {
            String classDesc = m.getText().replace(
                    Objects.nonNull(m.getBody()) ? m.getBody().getText()
                            : "", "");
            if (!StringUtils.isEmpty(classDesc)) {
                classDesc = classDesc.replace("<", "&lt;").replace(">", "&gt;");
            }
            target.setDesc("<pre><code>" + classDesc + "</code></pre>");
        }
        PsiDocComment docComment = m.getDocComment();
        if (Objects.nonNull(docComment)) {
            String title = PsiDocUtils.getTagDescription(docComment);
            if (StringUtils.isBlank(title)) {
                title = PsiDocUtils.getTagValueByName(docComment, TAG_DESCRIPTION);
            }
            target.setTitle(title);
        }
        YApiSupportHolder.supports.handleMethod(m, target);
    }
}
