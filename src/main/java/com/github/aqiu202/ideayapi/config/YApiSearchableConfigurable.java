package com.github.aqiu202.ideayapi.config;

import com.github.aqiu202.ideayapi.config.impl.ProjectConfigReader;
import com.github.aqiu202.ideayapi.config.ui.YApiConfigurationForm;
import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.config.xml.YApiPropertyConvertHolder;
import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class YApiSearchableConfigurable implements SearchableConfigurable {

    private YApiConfigurationForm yApiConfigurationForm;

    private YApiProjectProperty yApiProjectProperty;

    private final YApiProjectPersistentState persistent;

    private final Project project;

    YApiSearchableConfigurable(Project project) {
        this.project = project;
        this.persistent = YApiProjectPersistentState.getInstance(project);
    }

    @NotNull
    @Override
    public String getId() {
        return "Redsoft_YApi_Upload";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return YApiConstants.name;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (yApiConfigurationForm == null) {
            yApiConfigurationForm = new YApiConfigurationForm();
            yApiConfigurationForm.init();
        }
        return yApiConfigurationForm.getPanel();
    }

    @Override
    public boolean isModified() {
        String idStr = yApiConfigurationForm.getProjectIdField().getText();
        int projectId = 1;
        if (StringUtils.isNotBlank(idStr)) {
            projectId = Integer.parseInt(idStr);
        }
        this.yApiProjectProperty = new YApiProjectProperty(
                yApiConfigurationForm.getUrlField().getText(),
                projectId,
                yApiConfigurationForm.getTokenField().getText(),
                yApiConfigurationForm.getNamingStrategyComboBox().getSelectedIndex(),
                yApiConfigurationForm.getDataModeComboBox().getSelectedIndex(),
                yApiConfigurationForm.getEnableBasicScopeCheckBox().isSelected(),
                yApiConfigurationForm.getEnableTypeDescCheckBox().isSelected());
        this.yApiProjectProperty.setUseMethodDefineAsRemark(
                yApiConfigurationForm.getUseMethodDefineAsRemarkCheckBox().isSelected());
        this.yApiProjectProperty.setPassPageUrl(
                yApiConfigurationForm.getPassPageUrlCheckBox().isSelected());
        this.yApiProjectProperty.setUseLombok(
                yApiConfigurationForm.getUseLombokCheckBox().isSelected());
        this.yApiProjectProperty.setIgnoredReqFields(yApiConfigurationForm.getIgnoredReqFields());
        this.yApiProjectProperty.setIgnoredResFields(yApiConfigurationForm.getIgnoredResFields());
        return !this.yApiProjectProperty
                .equals(ProjectConfigReader.read(this.project));
    }

    @Override
    public void apply() {
        persistent.loadState(
                YApiPropertyConvertHolder.getConvert().serialize(this.yApiProjectProperty));
    }

    @Override
    public void reset() {
        this.loadValue();
    }

    private void loadValue() {
        YApiProjectProperty property = ProjectConfigReader.read(this.project);
        String url = property.getUrl();
        if (StringUtils.isNotBlank(url)) {
            yApiConfigurationForm.getUrlField().setText(url);
        }
        yApiConfigurationForm.getProjectIdField().setText(String.valueOf(property.getProjectId()));
        int strategy = property.getStrategy();
        yApiConfigurationForm.getNamingStrategyComboBox().setSelectedIndex(strategy);
        int dataMode = property.getDataMode();
        yApiConfigurationForm.getDataModeComboBox().setSelectedIndex(dataMode);
        String token = property.getToken();
        if (StringUtils.isNotBlank(token)) {
            yApiConfigurationForm.getTokenField().setText(token);
        }
        yApiConfigurationForm.getEnableBasicScopeCheckBox().setSelected(property.isEnableBasicScope());
        yApiConfigurationForm.getEnableTypeDescCheckBox().setSelected(property.isEnableTypeDesc());
        yApiConfigurationForm.getUseMethodDefineAsRemarkCheckBox().setSelected(property.isUseMethodDefineAsRemark());
        yApiConfigurationForm.getPassPageUrlCheckBox().setSelected(property.isPassPageUrl());
        yApiConfigurationForm.getUseLombokCheckBox().setSelected(property.isUseLombok());
        yApiConfigurationForm.setIgnoredReqFields(property.getIgnoredReqFieldList());
        yApiConfigurationForm.setIgnoredResFields(property.getIgnoredResFieldList());
    }

}
