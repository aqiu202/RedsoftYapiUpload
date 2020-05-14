package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.javadoc.PsiDocComment;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.base.MenuSetter;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import org.jetbrains.annotations.NotNull;

public class MenuSetterImpl implements MenuSetter {

    private final static String TAG_MENU = "menu";
    private final static String DESCRIPTION_VALUE = "description";

    @Override
    public void set(@NotNull PsiDocComment docComment, @NotNull YApiParam target) {
        String tagDescription = PsiDocUtils.getTagDescription(docComment);
        //以前的取值方法（@menuDesc替换为@description）
        String value = PsiDocUtils.getTagValueByName(docComment, TAG_MENU);
        String descValue = PsiDocUtils.getTagValueByName(docComment, DESCRIPTION_VALUE);
        //如果有@menu注释
        if (Strings.isBlank(value)) {
            //描述默认读取描述信息
            value = tagDescription;
        }
        target.setMenu(value);
        target.setMenuDesc(descValue);
    }

}
