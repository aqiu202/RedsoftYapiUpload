package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.base.DeprecatedAssert;
import com.redsoft.idea.plugin.yapiv2.base.impl.DeprecatedAssertImpl;
import com.redsoft.idea.plugin.yapiv2.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapiv2.constant.YApiConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.impl.PsiClassParserImpl;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <b>接口信息解析入口</b>
 * @author aqiu
 * @date 2019-06-15 11:46
 **/
public class YApiParser {

    private final DeprecatedAssert deprecatedAssert = new DeprecatedAssertImpl();

    private final Project project;
    private final PsiMethodParser methodParser;
    private final PsiClassParser classParser;

    public YApiParser(Project project, PsiMethodParser methodParser) {
        this.project = project;
        this.methodParser = methodParser;
        this.classParser = new PsiClassParserImpl(methodParser);
    }

    public YApiParser(Project project, PsiMethodParser methodParser, PsiClassParser classParser) {
        this.project = project;
        this.methodParser = methodParser;
        this.classParser = classParser;
    }

    public Set<YApiParam> parse(AnActionEvent e) {
        String selectedText = PsiUtils.getSelectedText(e);
        PsiClass selectedClass = PsiUtils.currentClass(e);
        //获取该类是否已经过时
        if (Objects.isNull(selectedClass) || deprecatedAssert.isDeprecated(selectedClass)) {
            NotificationConstants.NOTIFICATION_GROUP
                    .createNotification(YApiConstants.name, "该类已过时",
                            "该类(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                    .notify(project);
            return null;
        }
        Set<YApiParam> yApiParams = new HashSet<>();
        if (Strings.isBlank(selectedText) || selectedText.equals(selectedClass.getName())) {
            List<YApiParam> params = this.classParser.parse(selectedClass);
            yApiParams.addAll(params);
        } else {//如果用户选中的是方法
            PsiMethod[] psiMethods = selectedClass.getMethods();
            PsiMethod psiMethodTarget = null;
            for (PsiMethod psiMethod : psiMethods) {
                if (psiMethod.getName().equals(selectedText)) {
                    psiMethodTarget = psiMethod;
                    break;
                }
            }
            if (Objects.nonNull(psiMethodTarget)) {
                //带有 @Deprecated 注解的方法跳过
                if (deprecatedAssert.isDeprecated(psiMethodTarget)) {
                    NotificationConstants.NOTIFICATION_GROUP
                            .createNotification(YApiConstants.name, "接口已过时",
                                    "该方法或者类(或注释中)含有@Deprecated注解，如需上传，请删除该注解:" + selectedText,
                                    NotificationType.WARNING).notify(project);
                }
                try {
                    YApiParam param = this.methodParser.parse(selectedClass, psiMethodTarget);
                    yApiParams.add(param);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NotificationConstants.NOTIFICATION_GROUP
                            .createNotification(YApiConstants.name, "接口信息解析失败",
                                    "失败原因：" + ex.getMessage(),
                                    NotificationType.ERROR).notify(project);
                }
            }
        }
        return yApiParams;
    }

}
