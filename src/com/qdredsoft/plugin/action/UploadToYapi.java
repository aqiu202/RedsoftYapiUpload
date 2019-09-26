package com.qdredsoft.plugin.action;

import com.google.common.base.Strings;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.qdredsoft.plugin.constant.YapiConstants;
import com.qdredsoft.plugin.model.YapiApiDTO;
import com.qdredsoft.plugin.model.YapiResponse;
import com.qdredsoft.plugin.model.YapiSaveParam;
import com.qdredsoft.plugin.parser.YapiApiParser;
import com.qdredsoft.plugin.upload.UploadYapi;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class UploadToYapi extends AnAction {

    private static NotificationGroup notificationGroup;

    static {
        notificationGroup = new NotificationGroup("Java2Json.NotificationGroup",
                NotificationDisplayType.BALLOON, true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = (Editor) e.getDataContext().getData(CommonDataKeys.EDITOR);
        Project project = editor.getProject();
        // token
        String projectToken = null;
        // 项目ID
        String projectId = null;
        // yapi地址
        String yapiUrl = null;
        boolean enableBasicScope = false;
        try {
//            String projectConfig = new String(
//                    editor.getProject().getProjectFile().contentsToByteArray(),
//                    "utf-8");
            String projectConfig = "<component name=\"yapi\">\n"
                    + "    <option name=\"projectToken\">9e643413546194549c72398adbaa3d08a78679788527690037b03f3c39519e6b</option>\n"
                    + "    <option name=\"projectId\">32</option>\n"
                    + "    <option name=\"yapiUrl\">http://47.105.222.231:3000</option>\n"
                    + "  </component>";
            projectToken = projectConfig.split("projectToken\">")[1].split("</")[0];
            projectId = projectConfig.split("projectId\">")[1].split("</")[0];
            yapiUrl = projectConfig.split("yapiUrl\">")[1].split("</")[0];
            enableBasicScope = projectConfig.contains("basicScope\">") && "true"
                    .equals(projectConfig.split("basicScope\">")[1].split("</")[0].toLowerCase());
        } catch (Exception ex) {
            Notification error = notificationGroup
                    .createNotification("读取配置错误:" + ex.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        // 配置校验
        if (Strings.isNullOrEmpty(projectToken) || Strings.isNullOrEmpty(projectId) || Strings
                .isNullOrEmpty(yapiUrl)) {
            Notification error = notificationGroup
                    .createNotification("请检查配置文件参数是否正常",
                            NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        //获得api 需上传的接口列表 参数对象
        List<YapiApiDTO> yapiApiDTOS = new YapiApiParser().parse(e, enableBasicScope);
        if (yapiApiDTOS != null) {
            for (YapiApiDTO yapiApiDTO : yapiApiDTOS) {
                YapiSaveParam yapiSaveParam = new YapiSaveParam(projectToken, yapiApiDTO.getTitle(),
                        yapiApiDTO.getPath(), yapiApiDTO.getParams(), yapiApiDTO.getRequestBody(),
                        yapiApiDTO.getResponse(), Integer.valueOf(projectId), yapiUrl, true,
                        yapiApiDTO.getMethod(), yapiApiDTO.getDesc(), yapiApiDTO.getHeader());
                yapiSaveParam.setReq_body_form(yapiApiDTO.getReq_body_form());
                yapiSaveParam.setReq_body_type(yapiApiDTO.getReq_body_type());
                yapiSaveParam.setReq_params(yapiApiDTO.getReq_params());
                yapiSaveParam.setRes_body(yapiApiDTO.getResponse());
                yapiSaveParam.setRes_body_type(yapiApiDTO.getRes_body_type());
                String menuDesc = yapiApiDTO.getMenuDesc();
                if (Objects.nonNull(menuDesc)) {
                    yapiSaveParam.setMenuDesc(yapiApiDTO.getMenuDesc());
                }
                if (!Strings.isNullOrEmpty(yapiApiDTO.getMenu())) {
                    yapiSaveParam.setMenu(yapiApiDTO.getMenu());
                } else {
                    yapiSaveParam.setMenu(YapiConstants.menu);
                }
                try {
//            System.out.println(new Gson().toJson(yapiSaveParam));
                    // 上传
                    YapiResponse yapiResponse = new UploadYapi()
                            .uploadSave(yapiSaveParam, project.getBasePath());
                    if (yapiResponse.getErrcode() != 0) {
                        Notification error = notificationGroup
                                .createNotification("抱歉，api上传失败:" + yapiResponse.getErrmsg(),
                                        NotificationType.ERROR);
                        Notifications.Bus.notify(error, project);
                    } else {
                        String url =
                                yapiUrl + "/project/" + projectId + "/interface/api/cat_"
                                        + UploadYapi.catMap
                                        .get(projectId).get(yapiSaveParam.getMenu());
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
            UploadYapi.catMap.clear();
        }
    }
}
