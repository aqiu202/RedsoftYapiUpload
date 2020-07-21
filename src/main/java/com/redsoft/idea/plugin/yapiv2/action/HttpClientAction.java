package com.redsoft.idea.plugin.yapiv2.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.redsoft.idea.plugin.yapiv2.api.impl.HttpClientFileResolverImpl;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.PsiMethodParser;
import com.redsoft.idea.plugin.yapiv2.parser.YApiParser;
import com.redsoft.idea.plugin.yapiv2.parser.impl.PsiMethodParserImpl;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientAction extends AnAction {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
//        String basePath = project.getBasePath();
//        String httpPath = basePath + "/src/test/resources/http";
//        File path = new File(httpPath);
//        path.mkdirs();
//        File file = new File(httpPath + "/test.http");
//        try {
//            file.createNewFile();
//        } catch (IOException ioException) {
//            logger.error("创建http client测试文件失败：", ioException);
//        }
        PsiMethodParser methodParser = new PsiMethodParserImpl(
                new HttpClientFileResolverImpl(project));
        //获得api 需上传的接口列表 参数对象
        Set<YApiParam> yApiParams = new YApiParser(project, methodParser).parse(e);
        if (yApiParams != null) {
            for (YApiParam yApiParam : yApiParams) {
                //TODO 生成http client文件
            }
        }
    }

}
