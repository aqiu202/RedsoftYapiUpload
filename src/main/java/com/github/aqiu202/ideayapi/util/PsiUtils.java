package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.model.EnumField;
import com.github.aqiu202.ideayapi.model.EnumFields;
import com.github.aqiu202.ideayapi.model.EnumResult;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Psi基础工具类
 */
public final class PsiUtils {

    private PsiUtils() {
    }

    private static volatile PsiClassType ENUM_CLASS_TYPE = null;

    public static PsiMethod getSelectMethod(AnActionEvent e) {
        return findThenGet(e, PsiMethod.class);
    }

    public static PsiClass getSelectClass(AnActionEvent e) {
        return findThenGet(e, PsiClass.class);
    }

    public static PsiDirectory getSelectPackage(AnActionEvent e) {
        return findThenGet(e, PsiDirectory.class);
    }

    public static <T> T findThenGet(AnActionEvent e, Class<T> clz) {
        PsiElement pe = e.getData(CommonDataKeys.PSI_ELEMENT);
        return (pe != null && clz.isAssignableFrom(pe.getClass())) ? (T) pe : null;
    }

    public static Collection<PsiClass> collectPsiClasses(PsiJavaFile javaFile) {
        return Arrays.asList(javaFile.getClasses());
    }

    public static Collection<PsiClass> collectPsiClasses(PsiDirectory dir) {
        return collectPsiClasses(dir, new HashSet<>());
    }

    public static Collection<PsiClass> collectPsiClasses(PsiDirectory dir, Collection<PsiClass> preResult) {
        for (PsiElement child : dir.getChildren()) {
            if (child instanceof PsiJavaFile) {
                preResult.addAll(collectPsiClasses(((PsiJavaFile) child)));
            }
            if (child instanceof PsiDirectory) {
                collectPsiClasses(((PsiDirectory) child), preResult);
            }
        }
        return preResult;
    }

    public static Set<PsiClass> getClassesFormVirtualFile(VirtualFile virtualFile, Project project) {
        Set<PsiClass> results = new HashSet<>();
        VfsUtilCore.visitChildrenRecursively(virtualFile, new VirtualFileVisitor() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if (file.getFileType() instanceof JavaFileType) {
                    PsiJavaFile psiJavaFile = convertPsiJavaFile(file, project);
                    if (psiJavaFile != null) {
                        results.addAll(Arrays.asList(psiJavaFile.getClasses()));
                    }
                    return false;
                }
                return file.isDirectory();
            }
        });
        return results;
    }

    private static String readVirtualFileContent(VirtualFile file) throws IOException {
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
    private static PsiJavaFile convertPsiJavaFile(VirtualFile file, Project project) {
        FileType fileType = file.getFileType();
        try {
            if (fileType instanceof JavaFileType) {
                return (PsiJavaFile) PsiFileFactory.getInstance(project).createFileFromText(file.getName(),
                        fileType, readVirtualFileContent(file));
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * 通过类完整路径获取相应的psiClass对象
     *
     * @param project    项目
     * @param typePkName 类完整路径
     * @author aqiu
     */
    public static PsiClass findPsiClass(Project project, String typePkName) {
        return JavaPsiFacade.getInstance(project)
                .findClass(typePkName,
                        GlobalSearchScope.allScope(project));
    }

    /**
     * 通过类完整路径获取相应的psiClassType对象
     *
     * @param project    项目
     * @param typePkName 类完整路径
     * @author aqiu
     */
    public static PsiClassType findPsiClassType(Project project, String typePkName) {
        return PsiType.getTypeByName(typePkName, project,
                GlobalSearchScope.allScope(project));
    }

    public static EnumResult isEnum(Project project, String typePkName) {
        if (ENUM_CLASS_TYPE == null) {
            synchronized (PsiUtils.class) {
                if (ENUM_CLASS_TYPE == null) {
                    ENUM_CLASS_TYPE = findPsiClassType(project, "java.lang.Enum");
                }
            }
        }
        PsiClassType psiClassType = findPsiClassType(project, typePkName);
        return new EnumResult(ENUM_CLASS_TYPE.isAssignableFrom(psiClassType), psiClassType);
    }

    public static EnumFields resolveEnum(PsiClass psiClass) {
        return new EnumFields(Arrays.stream(psiClass.getFields()).map(
                filed -> new EnumField(filed.getName(), DesUtils.getFiledDesc(filed))
        ).collect(Collectors.toList()));
    }

}
