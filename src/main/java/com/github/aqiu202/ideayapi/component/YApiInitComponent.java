package com.github.aqiu202.ideayapi.component;

import com.github.aqiu202.ideayapi.config.YApiApplicationPersistentState;
import com.github.aqiu202.ideayapi.config.impl.ApplicationConfigReader;
import com.github.aqiu202.ideayapi.config.xml.YApiApplicationProperty;
import com.github.aqiu202.ideayapi.config.xml.YApiPropertyConvertHolder;
import com.github.aqiu202.ideayapi.constant.PluginConstants;
import com.github.aqiu202.ideayapi.util.NotificationUtils;
import com.intellij.notification.NotificationListener.UrlOpeningListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
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
            /**
             * <h4>2.2.2+3版本，添加对Jakarta Servlet的支持，修复已知问题</h4>
             *      <ol>
             *         <li>添加对Jakarta Servlet的支持</li>
             *         <li>修复WebFlux接口的解析异常问题</li>
             *         <li>修复部分参数泛型解析异常的问题</li>
             *      </ol>
             */
            String changeLogTitle = "<h4>2.2.2+3版本，添加对Jakarta Servlet的支持，修复已知问题</h4>";
            String changeLogContent = "<ol>\n" +
                    "        <li>添加对Jakarta Servlet的支持</li>\n" +
                    "        <li>修复WebFlux接口的解析异常问题</li>\n" +
                    "        <li>修复部分参数泛型解析异常的问题</li>\n" +
                    "     </ol>";
            NotificationUtils.createNotification("更新内容",
                            changeLogTitle + "\n" + changeLogContent
                                    + "<p>更多信息请查看<a href=\"https://github.com/aqiu202/RedsoftYApiUpload/wiki/使用指南\">使用文档</a>||"
                                    + "<a href=\"https://github.com/aqiu202/RedsoftYapiUpload/issues\">问题反馈</a></p>", NotificationType.INFORMATION)
                    .setListener(new UrlOpeningListener(false))
                    .setImportant(true)
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
