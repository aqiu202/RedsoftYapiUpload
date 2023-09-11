package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.model.EnumField;
import com.github.aqiu202.ideayapi.model.EnumFields;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
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
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Psi基础工具类
 */
public final class PsiUtils {

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
        VfsUtilCore.visitChildrenRecursively(virtualFile, new VirtualFileVisitor<>() {
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
        return baos.toString(StandardCharsets.UTF_8);
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

    public static PsiClass findPsiClass(String typePkName) {
        return findPsiClass(YApiSupportHolder.project, typePkName);
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

    public static PsiClassType findPsiClassType(String typePkName) {
        return findPsiClassType(YApiSupportHolder.project, typePkName);
    }

    public static PsiClassType getClassType(PsiClass psiClass) {
        return JavaPsiFacade.getElementFactory(YApiSupportHolder.project)
                .createType(psiClass);
    }

    public static PsiClass convertToClass(PsiType type) {
        if (type instanceof PsiClassType) {
            return ((PsiClassType) type).resolve();
        }
        return null;
    }


    public static EnumFields resolveEnumFields(PsiClass psiClass) {
        return new EnumFields(Arrays.stream(psiClass.getFields()).map(
                filed -> new EnumField(filed.getName(), PsiDocUtils.getComment(filed))
        ).collect(Collectors.toList()));
    }

    public static PsiType resolveGenericType(PsiClass targetClass, PsiType psiType) {
        PsiClassType[] superTypes = targetClass.getSuperTypes();
        for (PsiClassType superType : superTypes) {
            PsiType type = resolveGenericType(superType, psiType);
            if (!StringUtils.equals(type.getCanonicalText(), psiType.getCanonicalText())) {
                return type;
            }
        }
        return psiType;
    }

    public static PsiType resolveGenericType(PsiClassType classType, PsiType psiType) {
        PsiSubstitutor substitutor = classType.resolveGenerics().getSubstitutor();
        return substitutor.substitute(psiType);
    }

    public static PsiType resolveValidType(PsiModifierListOwner owner) {
        PsiType type = null;
        if (owner instanceof PsiVariable) {
            type = ((PsiVariable) owner).getType();
        } else if (owner instanceof PsiMethod) {
            type = ((PsiMethod) owner).getReturnType();
        }
        return type;
    }

}
