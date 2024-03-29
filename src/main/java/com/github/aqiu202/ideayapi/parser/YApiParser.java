package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.parser.impl.PsiClassParserImpl;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.NotificationUtils;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>接口信息解析入口</b>
 *
 * @author aqiu 2019-06-15 11:46
 **/
public class YApiParser {

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
        Set<YApiParam> yApiParams = new HashSet<>();
        PsiMethod selectMethod;
        PsiClass selectedClass;
        PsiDirectory selectDir;
        // 如果选取的是方法
        if ((selectMethod = PsiUtils.getSelectMethod(e)) != null) {
            PsiClass currentClass = (PsiClass) selectMethod.getParent();
            //获取该方法是否已经标记过时
            if (DeprecatedAssert.instance.isDeprecated(currentClass, selectMethod)) {
                NotificationUtils.createNotification("该类/方法已过时", "该类/方法(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                        .notify(this.project);
                return null;
            }
            List<YApiParam> params = this.methodParser.parse(currentClass, selectMethod);
            if (CollectionUtils.isNotEmpty(params)) {
                yApiParams.addAll(params);
            }
        } else if ((selectedClass = PsiUtils.getSelectClass(e)) != null) {// 如果选取的是类
            //获取该类是否已经过时
            if (DeprecatedAssert.instance.isDeprecated(selectedClass)) {
                NotificationUtils.createNotification("该类已过时", "该类(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                        .notify(this.project);
                return null;
            }
            List<YApiParam> params = this.classParser.parse(selectedClass);
            yApiParams.addAll(params);
        } else if ((selectDir = PsiUtils.getSelectPackage(e)) != null) {// 如果选取的是文件夹
            PsiUtils.collectPsiClasses(selectDir)
                    .forEach(c -> this.handleClass(c, yApiParams));
        } else { //尝试获取选取的文件/文件夹集合
            VirtualFile[] virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
            if (CollectionUtils.isNotEmpty(virtualFiles)) {
                for (VirtualFile virtualFile : virtualFiles) {
                    PsiUtils.getClassesFormVirtualFile(virtualFile, project)
                            .forEach(c -> this.handleClass(c, yApiParams));
                }
            }
        }
        return yApiParams;
    }

    private void handleClass(PsiClass c, Collection<YApiParam> params) {
        if (!DeprecatedAssert.instance.isDeprecated(c)) {
            params.addAll(this.classParser.parse(c));
        }
    }

}
