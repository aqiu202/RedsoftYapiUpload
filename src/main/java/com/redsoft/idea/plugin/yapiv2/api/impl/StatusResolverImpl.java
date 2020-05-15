package com.redsoft.idea.plugin.yapiv2.api.impl;

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

    private final static String TAG_STATUS = "status";

    @Override
    public void resolve(@Nullable PsiDocComment classDoc, @Nullable PsiDocComment methodDoc,
            @NotNull YApiParam target) {
        String status = YApiStatus.done.name();
        if (Objects.nonNull(classDoc)) {
            String value = PsiDocUtils.getTagValueByName(classDoc, TAG_STATUS);
            if (Strings.isNotBlank(value)) {
                status = YApiStatus.getStatus(value);
            }
        }
        if (Objects.nonNull(methodDoc)) {
            String value = PsiDocUtils.getTagValueByName(methodDoc, TAG_STATUS);
            if (Strings.isNotBlank(value)) {
                status = YApiStatus.getStatus(value);
            }
        }
        target.setStatus(status);
    }
}
