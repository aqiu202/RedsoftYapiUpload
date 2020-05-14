package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.base.BaseInfoSetter;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.support.YApiSupportHolder;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class BaseInfoSetterImpl implements BaseInfoSetter {

    @Override
    public void set(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        String classDesc = m.getText().replace(
                Objects.nonNull(m.getBody()) ? m.getBody().getText()
                        : "", "");
        if (!Strings.isEmpty(classDesc)) {
            classDesc = classDesc.replace("<", "&lt;").replace(">", "&gt;");
        }
        target.setDesc("<pre><code>" + classDesc + "</code></pre>");
        YApiSupportHolder.supports.handleMethod(m, target);
    }
}
