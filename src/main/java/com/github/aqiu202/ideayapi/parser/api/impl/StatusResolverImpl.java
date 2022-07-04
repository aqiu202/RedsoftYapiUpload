package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.model.YApiStatus;
import com.github.aqiu202.ideayapi.parser.api.StatusResolver;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_STATUS;

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
