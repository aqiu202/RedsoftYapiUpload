package com.redsoft.idea.plugin.yapiv2.template;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

public class YApiTemplateProvider implements DefaultLiveTemplatesProvider {

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        //模板
        return new String[]{"live-templates/yapi-template"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }

}
