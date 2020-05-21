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
//            String changeLogTitle = "<h4>版本1.3.7，添加Swagger参数解析支持，内置注释模板等</h4>";
//            String changeLogContent = "<ol>"
//                    + "        <li>添加Swagger参数解析支持</li>"
//                    + "        <li>内置注释模板，无需再单独配置注释模板</li>"
//                    + "        <li>优化1-兼容之前的配置方式</li>"
//                    + "        <li>优化2-响应参数自动设置mock</li>"
//                    + "      </ol>";
            String changeLogTitle = "<h4>版本2.0.1，架构重构，支持json5解析，bug修复</h4>";
            String changeLogContent = "<ol>"
                    + "        <li>返回数据支持json5格式解析</li>"
                    + "        <li>javadoc解析方式优化，添加值的HTML格式解析</li>"
                    + "        <li>内置注释模板优化，减少侵入</li>"
                    + "        <li>摒弃@strategy和@path注释</li>"
                    + "      </ol>"
                    + "<h4>版本2.0.1，修改bug</h4>"
                    + "      <ol>"
                    + "        <li>参数含@RequestBody注解（body是json格式）的接口解析异常BUG修复</li>"
                    + "        <li>所有的注释支持html标签包裹</li>"
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
