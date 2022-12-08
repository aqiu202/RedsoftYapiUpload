package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.MenuResolver;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_DESCRIPTION;
import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_MENU;

public class MenuResolverImpl implements MenuResolver {

    @Override
    public void set(@NotNull PsiClass c, @NotNull YApiParam target) {
        PsiDocComment docComment = c.getDocComment();
        if (docComment != null) {
            String value = PsiDocUtils.getTagDescription(docComment);
            //以前的取值方法（@menuDesc替换为@description）
            String descValue = PsiDocUtils.getTagValueByName(docComment, TAG_DESCRIPTION);
            //如果没有描述注释
            if (StringUtils.isBlank(value)) {
                //菜单默认读取@menu注释
                value = PsiDocUtils.getTagValueByName(docComment, TAG_MENU);
            }
            //如果没有@description注释
            if (StringUtils.isBlank(descValue)) {
                //描述默认读取描述信息
                descValue = value;
            }
            target.setMenu(value);
            target.setMenuDesc(descValue);
            YApiSupportHolder.supports.handleMenu(c, target);
        }
    }

}
