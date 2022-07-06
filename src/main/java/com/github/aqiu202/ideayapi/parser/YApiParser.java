package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.constant.NotificationConstants;
import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.parser.impl.PsiClassParserImpl;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
                NotificationConstants.NOTIFICATION_GROUP
                        .createNotification(YApiConstants.name, "该类/方法已过时",
                                "该类/方法(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                        .notify(project);
                return null;
            }
            YApiParam param = this.methodParser.parse(currentClass, selectMethod);
            yApiParams.add(param);
        } else if ((selectedClass = PsiUtils.getSelectClass(e)) != null) {// 如果选取的是类
            //获取该类是否已经过时
            if (DeprecatedAssert.instance.isDeprecated(selectedClass)) {
                NotificationConstants.NOTIFICATION_GROUP
                        .createNotification(YApiConstants.name, "该类已过时",
                                "该类(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                        .notify(project);
                return null;
            }
            List<YApiParam> params = this.classParser.parse(selectedClass);
            yApiParams.addAll(params);
        } else if ((selectDir = PsiUtils.getSelectPackage(e)) != null) {// 如果选取的是文件夹
            Set<PsiClass> classes = new HashSet<>();
            PsiUtils.collectClasses(selectDir, classes);
            classes.stream()
                    .filter(c -> !DeprecatedAssert.instance.isDeprecated(c))
                    .forEach(c -> yApiParams.addAll(this.classParser.parse(c)));
        } else { //尝试获取选取的文件集合
            VirtualFile[] virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
            if (CollectionUtils.isNotEmpty(virtualFiles)) {
                Set<PsiJavaFile> files = Arrays.stream(virtualFiles).map(this::convertPsiJavaFile)
                        .filter(Objects::nonNull).collect(Collectors.toSet());
                PsiUtils.joinAllClasses(files).stream()
                        .filter(c -> !DeprecatedAssert.instance.isDeprecated(c))
                        .forEach(c -> yApiParams.addAll(this.classParser.parse(c)));
            }
        }
        return yApiParams;
    }

    private String readVirtualFileContent(VirtualFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final int bufferSize = 256;
        byte[] buff = new byte[bufferSize];
        int rc;
        while ((rc = inputStream.read(buff, 0, bufferSize)) > 0) {
            baos.write(buff, 0, rc);
        }
        return baos.toString(StandardCharsets.UTF_8.name());
    }

    @Nullable
    private PsiJavaFile convertPsiJavaFile(VirtualFile file) {
        FileType fileType = file.getFileType();
        try {
            if (fileType instanceof JavaFileType) {
                return (PsiJavaFile) PsiFileFactory.getInstance(this.project).createFileFromText(file.getName(),
                        fileType, this.readVirtualFileContent(file));
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

}
