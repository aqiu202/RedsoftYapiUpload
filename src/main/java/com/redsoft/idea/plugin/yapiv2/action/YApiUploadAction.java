package com.redsoft.idea.plugin.yapiv2.action;

import com.intellij.notification.NotificationListener.UrlOpeningListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.config.impl.ProjectConfigReader;
import com.redsoft.idea.plugin.yapiv2.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapiv2.constant.YApiConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.model.YApiResponse;
import com.redsoft.idea.plugin.yapiv2.model.YApiSaveParam;
import com.redsoft.idea.plugin.yapiv2.parser.YApiParser;
import com.redsoft.idea.plugin.yapiv2.upload.YApiUpload;
import com.redsoft.idea.plugin.yapiv2.util.Builders;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class YApiUploadAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        YApiProjectProperty property = ProjectConfigReader.read(project);
        // token
        String token = property.getToken();
        // 项目ID
        int projectId = property.getProjectId();
        // yapi地址
        String yapiUrl = property.getUrl();
        // 配置校验
        if (Strings.isEmpty(token) || Strings.isEmpty(yapiUrl) || projectId <= 0) {
            NotificationConstants.NOTIFICATION_GROUP
                    .createNotification(YApiConstants.name, "配置信息异常", "请检查配置参数是否正常",
                            NotificationType.ERROR).notify(project);
            return;
        }
        //获得api 需上传的接口列表 参数对象
        Set<YApiParam> yApiParams = new YApiParser(property, project).parse(e);
        if (yApiParams != null) {
            for (YApiParam yApiParam : yApiParams) {
                YApiSaveParam yapiSaveParam = Builders.of(yApiParam::convert)
                        .with(YApiSaveParam::setToken, token)
                        .build();
                if (Strings.isEmpty(yApiParam.getMenu())) {
                    yapiSaveParam.setMenu(YApiConstants.menu);
                }
                try {
                    // 上传
                    YApiResponse yapiResponse = new YApiUpload()
                            .uploadSave(property, yapiSaveParam, project.getBasePath());
                    if (yapiResponse.getErrcode() != 0) {
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "上传失败",
                                        "api上传失败原因:" + yapiResponse.getErrmsg(),
                                        NotificationType.ERROR).notify(project);
                    } else {
                        String url = yapiUrl + "/project/" + projectId + "/interface/api/cat_"
                                + YApiUpload.catMap
                                .get(Integer.toString(projectId))
                                .get(yapiSaveParam.getMenu());
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "上传成功",
                                        "<p>接口文档地址:  <a href=\"" + url + "\">" + url + "</a></p>",
                                        NotificationType.INFORMATION,
                                        new UrlOpeningListener(false))
                                .notify(project);
                    }
                } catch (Exception e1) {
                    NotificationConstants.NOTIFICATION_GROUP
                            .createNotification(YApiConstants.name, "上传失败", "api上传失败原因:" + e1,
                                    NotificationType.ERROR)
                            .notify(project);
                }
            }
            YApiUpload.catMap.clear();
        }
    }

}
