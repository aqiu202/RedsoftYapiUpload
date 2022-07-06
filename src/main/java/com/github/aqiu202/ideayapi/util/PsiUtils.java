package com.github.aqiu202.ideayapi.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Psi基础工具类
 */
public final class PsiUtils {

    private PsiUtils() {
    }

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

    public static void collectClasses(PsiDirectory psiDirectory, Collection<PsiClass> classes) {
        if (psiDirectory.getChildren().length > 0) {
            PsiElement[] children = psiDirectory.getChildren();
            for (PsiElement child : children) {
                if (child instanceof PsiJavaFile) {
                    classes.addAll(Arrays.asList(((PsiJavaFile) child).getClasses()));
                }
                if (child instanceof PsiDirectory) {
                    collectClasses((PsiDirectory) child, classes);
                }
            }
        }
    }

    public static Collection<PsiClass> joinAllClasses(Collection<PsiJavaFile> javaFiles) {
        Set<PsiClass> results = new HashSet<>();
        javaFiles.forEach(file -> results.addAll(Arrays.asList(file.getClasses())));
        return results;
    }

    /**
     * 获取当前选中字符
     *
     * @param e 事件
     * @return {@link String}
     * @author aqiu 2020/5/5 10:21 下午
     **/
    public static String getSelectedText(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        return editor == null ? null : getSelectedText(editor);
    }

    /**
     * 获取当前选中字符
     *
     * @param editor 编辑器
     * @return {@link String}
     * @author aqiu 2020/5/5 10:21 下午
     **/
    public static String getSelectedText(@NotNull Editor editor) {
        return editor.getSelectionModel().getSelectedText();
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

}
