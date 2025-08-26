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
        if (property == null || !PluginConstants.CURRENT_VERSION.equals(property.getVersion())) {
            /**
             * <h4>2.2.2+4版本，完善对Json5的解析和格式化逻辑，修复已知问题</h4>
             *      <ol>
             *         <li>完善对Json5的解析和格式化逻辑，修复之前的格式化缩进问题</li>
             *         <li>修复Lombok插件的兼容性问题</li>
             *         <li>完善对范型参数的解析问题，特别对Map的解析进行了优化</li>
             *         <li>优化了内置代码模版的逻辑，生成注释时不再在参数注释处停留</li>
             *         <li><li>修复了已知问题#34,#35,#37</li></li>
             *      </ol>
             */
            String changeLogTitle = "<h4>2.2.2+4版本，完善对Json5的解析和格式化逻辑，修复已知问题</h4>";
            String changeLogContent = "<ol>\n" +
                    "        <li>完善对Json5的解析和格式化逻辑，修复之前的格式化缩进问题</li>\n" +
                    "        <li>修复Lombok插件的兼容性问题</li>\n" +
                    "        <li>完善对范型参数的解析问题，特别对Map的解析进行了优化</li>\n" +
                    "        <li>优化了内置代码模版的逻辑，生成注释时不再在参数注释处停留</li>\n" +
                    "        <li><li>修复了已知问题#34,#35,#37</li>\n" +
                    "     </ol>";
            NotificationUtils.createNotification("更新内容",
                            changeLogTitle + "\n" + changeLogContent
                                    + "<p>更多信息请查看<a href=\"https://github.com/aqiu202/RedsoftYApiUpload/wiki/使用指南\">使用文档</a>||"
                                    + "<a href=\"https://github.com/aqiu202/RedsoftYapiUpload/issues\">问题反馈</a></p>", NotificationType.INFORMATION)
                    .setListener(new UrlOpeningListener(false))
                    .setImportant(true)
                    .notify(this.project);
            property = new YApiApplicationProperty();
            property.setVersion(PluginConstants.CURRENT_VERSION);
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
