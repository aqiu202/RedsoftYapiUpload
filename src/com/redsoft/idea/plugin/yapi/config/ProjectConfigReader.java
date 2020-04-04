package com.redsoft.idea.plugin.yapi.config;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.redsoft.idea.plugin.yapi.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapi.constant.YApiConstants;
import com.redsoft.idea.plugin.yapi.xml.YApiProjectProperty;
import com.redsoft.idea.plugin.yapi.xml.YApiPropertyConvertHolder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jdom.Element;

public class ProjectConfigReader {

    public static YApiProjectProperty read(Project project) {
        YApiProjectPersistentState projectState = ServiceManager
                .getService(Objects.requireNonNull(project), YApiProjectPersistentState.class);
        Element element = projectState.getState();
        if (element == null) {
            YApiProjectProperty property = new YApiProjectProperty();
            try {
                String projectConfig = new String(
                        project.getProjectFile().contentsToByteArray(),
                        StandardCharsets.UTF_8);
                if (projectConfig.contains("yapi")) {
                    NotificationConstants.NOTIFICATION_GROUP
                            .createNotification(YApiConstants.name, "配置信息更新",
                                    "读取到旧版本的配置信息，配置信息已经更新", NotificationType.INFORMATION)
                            .notify(project);
                    property.setToken(projectConfig.split("projectToken\">")[1].split("</")[0]);
                    property.setProjectId(
                            Integer.parseInt(
                                    projectConfig.split("projectId\">")[1].split("</")[0]));
                    property.setUrl(projectConfig.split("yapiUrl\">")[1].split("</")[0]);
                    property.setEnableBasicScope(projectConfig.contains("basicScope\">") && "true"
                            .equals(projectConfig.split("basicScope\">")[1].split("</")[0]
                                    .toLowerCase()));
                }
            } catch (Exception ignored) {
            }
            projectState.loadState(YApiPropertyConvertHolder.getConvert().serialize(property));
            return property;
        } else {
            return YApiPropertyConvertHolder.getConvert().deserialize(element);
        }
    }
}
