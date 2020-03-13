package com.redsoft.idea.plugin.yapi.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.config.YApiPersistentState;
import com.redsoft.idea.plugin.yapi.constant.YApiConstants;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;
import com.redsoft.idea.plugin.yapi.model.YApiResponse;
import com.redsoft.idea.plugin.yapi.model.YApiSaveParam;
import com.redsoft.idea.plugin.yapi.parser.YApiParser;
import com.redsoft.idea.plugin.yapi.upload.YApiUpload;
import com.redsoft.idea.plugin.yapi.xml.YApiProperty;
import com.redsoft.idea.plugin.yapi.xml.YApiPropertyConvertHolder;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class YApiUploadAction extends AnAction {

    private static NotificationGroup notificationGroup;

    static {
        notificationGroup = new NotificationGroup("Java2Json.NotificationGroup",
                NotificationDisplayType.BALLOON, true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        YApiProperty property = YApiPropertyConvertHolder.getConvert().deserialize(
                ServiceManager
                        .getService(Objects.requireNonNull(project), YApiPersistentState.class)
                        .getState());
        // token
        String projectToken = property.getToken();
        // 项目ID
        int projectId = property.getProjectId();
        // yapi地址
        String yapiUrl = property.getUrl();
        boolean enableBasicScope = property.isEnableBasicScope();
        // 配置校验
        if (Strings.isEmpty(projectToken) || Strings.isEmpty(yapiUrl) || projectId <= 0) {
            Notification error = notificationGroup
                    .createNotification("请检查配置参数是否正常", NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        //获得api 需上传的接口列表 参数对象
        List<YApiDTO> yapiApiDTOS = new YApiParser().parse(e, enableBasicScope);
        if (yapiApiDTOS != null) {
            for (YApiDTO yapiApiDTO : yapiApiDTOS) {
                YApiSaveParam yapiSaveParam = new YApiSaveParam(projectToken, yapiApiDTO.getTitle(),
                        yapiApiDTO.getPath(), yapiApiDTO.getParams(), yapiApiDTO.getRequestBody(),
                        yapiApiDTO.getResponse(), projectId, yapiUrl, true,
                        yapiApiDTO.getMethod(), yapiApiDTO.getDesc(), yapiApiDTO.getHeader());
                yapiSaveParam.setReq_body_form(yapiApiDTO.getReq_body_form());
                yapiSaveParam.setReq_body_type(yapiApiDTO.getReq_body_type());
                yapiSaveParam.setReq_params(yapiApiDTO.getReq_params());
                yapiSaveParam.setRes_body(yapiApiDTO.getResponse());
                yapiSaveParam.setStatus(yapiApiDTO.getStatus());
                yapiSaveParam.setRes_body_type(yapiApiDTO.getRes_body_type());
                String menuDesc = yapiApiDTO.getMenuDesc();
                if (Objects.nonNull(menuDesc)) {
                    yapiSaveParam.setMenuDesc(yapiApiDTO.getMenuDesc());
                }
                if (!Strings.isEmpty(yapiApiDTO.getMenu())) {
                    yapiSaveParam.setMenu(yapiApiDTO.getMenu());
                } else {
                    yapiSaveParam.setMenu(YApiConstants.menu);
                }
                try {
                    // 上传
                    YApiResponse yapiResponse = new YApiUpload()
                            .uploadSave(yapiSaveParam, project.getBasePath());
                    if (yapiResponse.getErrcode() != 0) {
                        Notification error = notificationGroup
                                .createNotification("抱歉，api上传失败:" + yapiResponse.getErrmsg(),
                                        NotificationType.ERROR);
                        Notifications.Bus.notify(error, project);
                    } else {
                        String host = yapiUrl.endsWith("/") ? yapiUrl : (yapiUrl + "/");
                        String url = host + "project/" + projectId + "/interface/api/cat_"
                                + YApiUpload.catMap
                                .get(String.valueOf(projectId))
                                .get(yapiSaveParam.getMenu());
                        Notification error = notificationGroup
                                .createNotification("接口上传成功:  " + url,
                                        NotificationType.INFORMATION);
                        Notifications.Bus.notify(error, project);
                    }
                } catch (Exception e1) {
                    Notification error = notificationGroup
                            .createNotification("抱歉，api上传失败:" + e1, NotificationType.ERROR);
                    Notifications.Bus.notify(error, project);
                }
            }
            YApiUpload.catMap.clear();
        }
    }
}
