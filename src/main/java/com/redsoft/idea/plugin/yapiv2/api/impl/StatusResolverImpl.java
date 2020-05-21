package com.redsoft.idea.plugin.yapiv2.api.impl;

import static com.redsoft.idea.plugin.yapiv2.constant.DocCommentConstants.TAG_STATUS;

import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.StatusResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.model.YApiStatus;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatusResolverImpl implements StatusResolver {

    @Override
    public void resolve(@Nullable PsiDocComment classDoc, @Nullable PsiDocComment methodDoc,
            @NotNull YApiParam target) {
        String status = YApiStatus.done.name();
        if (Objects.nonNull(classDoc)) {
            String value = PsiDocUtils.getTagValueByName(classDoc, TAG_STATUS);
            if (Strings.isNotBlank(value)) {
                status = value;
            }
        }
        if (Objects.nonNull(methodDoc)) {
            String value = PsiDocUtils.getTagValueByName(methodDoc, TAG_STATUS);
            if (Strings.isNotBlank(value)) {
                status = value;
            }
        }
        target.setStatus(status);
    }
}
