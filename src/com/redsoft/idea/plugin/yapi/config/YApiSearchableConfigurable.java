package com.redsoft.idea.plugin.yapi.config;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.ui.YApiConfigurationForm;
import com.redsoft.idea.plugin.yapi.xml.YApiProperty;
import com.redsoft.idea.plugin.yapi.xml.YApiPropertyConvertHolder;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YApiSearchableConfigurable implements SearchableConfigurable {

    private YApiConfigurationForm yApiConfigurationForm;

    private YApiProperty yApiProperty;

    private final YApiPersistentState persistent;

    YApiSearchableConfigurable(Project project) {
        this.persistent = YApiPersistentState.getInstance(project);
    }

    @NotNull
    @Override
    public String getId() {
        return "Redsoft_YApi_Upload";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Redsoft YApi Upload";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (yApiConfigurationForm == null) {
            yApiConfigurationForm = new YApiConfigurationForm();
        }
        return yApiConfigurationForm.getPanel();
    }

    @Override
    public boolean isModified() {
        String idStr = yApiConfigurationForm.getProjectIdField().getText();
        int projectId = 1;
        if (Strings.isNotBlank(idStr)) {
            projectId = Integer.parseInt(idStr);
        }
        this.yApiProperty = new YApiProperty(yApiConfigurationForm.getUrlField().getText(),
                projectId,
                yApiConfigurationForm.getTokenField().getText(),
                yApiConfigurationForm.getEnableBasicScopeCheckBox().isSelected());
        return !this.yApiProperty
                .equals(YApiPropertyConvertHolder.getConvert().deserialize(persistent.getState()));
    }

    @Override
    public void apply() {
        persistent.loadState(YApiPropertyConvertHolder.getConvert().serialize(this.yApiProperty));
    }

    @Override
    public void reset() {
        this.loadValue();
    }

    private void loadValue() {
        YApiProperty property = YApiPropertyConvertHolder.getConvert()
                .deserialize(persistent.getState());
        String url = property.getUrl();
        if (Strings.isNotBlank(url)) {
            yApiConfigurationForm.getUrlField().setText(url);
        }
        yApiConfigurationForm.getProjectIdField().setText(String.valueOf(property.getProjectId()));
        String token = property.getToken();
        if (Strings.isNotBlank(token)) {
            yApiConfigurationForm.getTokenField().setText(token);
        }
    }


}
