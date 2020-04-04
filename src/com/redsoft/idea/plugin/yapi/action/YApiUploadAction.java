package com.redsoft.idea.plugin.yapi.action;

import com.intellij.notification.NotificationListener.UrlOpeningListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.config.ProjectConfigReader;
import com.redsoft.idea.plugin.yapi.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapi.constant.PropertyNamingStrategy;
import com.redsoft.idea.plugin.yapi.constant.YApiConstants;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;
import com.redsoft.idea.plugin.yapi.model.YApiResponse;
import com.redsoft.idea.plugin.yapi.model.YApiSaveParam;
import com.redsoft.idea.plugin.yapi.parser.YApiParser;
import com.redsoft.idea.plugin.yapi.upload.YApiUpload;
import com.redsoft.idea.plugin.yapi.xml.YApiProjectProperty;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class YApiUploadAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        YApiProjectProperty property = ProjectConfigReader.read(project);
        // token
        String projectToken = property.getToken();
        // 项目ID
        int projectId = property.getProjectId();
        // yapi地址
        String yapiUrl = property.getUrl();
        boolean enableBasicScope = property.isEnableBasicScope();
        // 配置校验
        if (Strings.isEmpty(projectToken) || Strings.isEmpty(yapiUrl) || projectId <= 0) {
            NotificationConstants.NOTIFICATION_GROUP
                    .createNotification(YApiConstants.name, "配置信息异常", "请检查配置参数是否正常",
                            NotificationType.ERROR).notify(project);
            return;
        }
        //获得api 需上传的接口列表 参数对象
        List<YApiDTO> yApiDTOS = new YApiParser()
                .parse(e, PropertyNamingStrategy.of(String.valueOf(property.getStrategy())),
                        enableBasicScope);
        if (yApiDTOS != null) {
            for (YApiDTO yApiDTO : yApiDTOS) {
                YApiSaveParam yapiSaveParam = new YApiSaveParam(projectToken, yApiDTO.getTitle(),
                        yApiDTO.getPath(), yApiDTO.getParams(), yApiDTO.getRequestBody(),
                        yApiDTO.getResponse(), projectId, yapiUrl, true,
                        yApiDTO.getMethod(), yApiDTO.getDesc(), yApiDTO.getHeader());
                yapiSaveParam.setReq_body_form(yApiDTO.getReq_body_form());
                yapiSaveParam.setReq_body_type(yApiDTO.getReq_body_type());
                yapiSaveParam.setReq_params(yApiDTO.getReq_params());
                yapiSaveParam.setRes_body(yApiDTO.getResponse());
                yapiSaveParam.setStatus(yApiDTO.getStatus());
                yapiSaveParam.setRes_body_type(yApiDTO.getRes_body_type());
                String menuDesc = yApiDTO.getMenuDesc();
                if (Objects.nonNull(menuDesc)) {
                    yapiSaveParam.setMenuDesc(yApiDTO.getMenuDesc());
                }
                if (!Strings.isEmpty(yApiDTO.getMenu())) {
                    yapiSaveParam.setMenu(yApiDTO.getMenu());
                } else {
                    yapiSaveParam.setMenu(YApiConstants.menu);
                }
                try {
                    // 上传
                    YApiResponse yapiResponse = new YApiUpload()
                            .uploadSave(yapiSaveParam, project.getBasePath());
                    if (yapiResponse.getErrcode() != 0) {
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "上传失败",
                                        "api上传失败原因:" + yapiResponse.getErrmsg(),
                                        NotificationType.ERROR).notify(project);
                    } else {
                        String host = yapiUrl.endsWith("/") ? yapiUrl : (yapiUrl + "/");
                        String url = host + "project/" + projectId + "/interface/api/cat_"
                                + YApiUpload.catMap
                                .get(String.valueOf(projectId))
                                .get(yapiSaveParam.getMenu());
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "上传成功",
                                        "<p>接口文档地址:  <a href=\"" + url + "\">" + url + "</a></p>",
                                        NotificationType.INFORMATION,
                                        new UrlOpeningListener(true))
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
