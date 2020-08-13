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
            String changeLogTitle = "<h4>版本2.0.4（Bug fix版本），架构优化，泛型解析方式优化，部分bug修改</h4>";
            String changeLogContent = "<ol>\n"
                    + "        <li>架构优化，泛型解析方式优化</li>\n"
                    + "        <li>raw类型的数据解析失败的bug修改</li>\n"
                    + "        <li>@PathVariable注解的参数会在body中重复出现的bug修复</li>\n"
                    + "        <li>@RequestParam和@PathVariable注解参数名称解析错误bug修复</li>\n"
                    + "        <li>@PathVariable注解解析bug修改</li>\n"
                    + "        <li>字段命名策略的设置对@RequestBody的入参无效，BUG修改</li>\n"
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
