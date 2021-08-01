package com.redsoft.idea.plugin.yapiv2.component;

import com.intellij.notification.NotificationListener.UrlOpeningListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.redsoft.idea.plugin.yapiv2.config.YApiApplicationPersistentState;
import com.redsoft.idea.plugin.yapiv2.config.impl.ApplicationConfigReader;
import com.redsoft.idea.plugin.yapiv2.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapiv2.constant.PluginConstants;
import com.redsoft.idea.plugin.yapiv2.constant.YApiConstants;
import com.redsoft.idea.plugin.yapiv2.xml.YApiApplicationProperty;
import com.redsoft.idea.plugin.yapiv2.xml.YApiPropertyConvertHolder;
import org.jetbrains.annotations.NotNull;

public class YApiInitComponent implements ProjectComponent {

    private final Project project;

    public YApiInitComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        YApiApplicationProperty property = ApplicationConfigReader.read();
        if (property == null || !PluginConstants.currentVersion.equals(property.getVersion())) {
            String changeLogTitle = "<h4>2.0.6版本，兼容IDEA 2021.2版本</h4>";
            String changeLogContent = "<ol>\n"
                    + "        <li>兼容IDEA 2021.2版本</li>\n"
                    + "        <li>代码和注释模板优化</li>\n"
                    + "     </ol>";
            NotificationConstants.NOTIFICATION_GROUP.createNotification(YApiConstants.name, "更新内容",
                    changeLogTitle + "\n" + changeLogContent
                            + "<p>更多信息请查看<a href=\"https://github.com/aqiu202/RedsoftYApiUpload/wiki/使用指南\">使用文档</a>||"
                            + "<a href=\"https://github.com/aqiu202/RedsoftYapiUpload/issues\">问题反馈</a></p>",
                    NotificationType.INFORMATION, new UrlOpeningListener(false))
                    .notify(this.project);
            property = new YApiApplicationProperty();
            property.setVersion(PluginConstants.currentVersion);
            YApiApplicationPersistentState.getInstance().loadState(
                    YApiPropertyConvertHolder.getApplicationConvert().serialize(property));
        }
    }

    @Override
    public void projectClosed() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "YApiInitComponent";
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }
}
