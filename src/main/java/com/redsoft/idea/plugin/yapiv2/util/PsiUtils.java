package com.redsoft.idea.plugin.yapiv2.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.redsoft.idea.plugin.yapiv2.constant.TypeConstants;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class PsiUtils {

    private PsiUtils() {
    }

    /**
     * @author Redsoft
     * @date 2020/5/5 10:10 下午
     * @description 获取事件触发的当前类
     * @param e: 事件
     * @return {@link PsiClass}
     **/
    public static PsiClass currentClass(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiElement referenceAt = Objects.requireNonNull(psiFile)
                .findElementAt(Objects.requireNonNull(e.getData(CommonDataKeys.EDITOR))
                        .getCaretModel().getOffset());
        return PsiTreeUtil.getContextOfType(referenceAt, PsiClass.class, true);
    }

    /**
     * 获取当前选中字符
     * @author aqiu
     * @date 2020/5/5 10:21 下午
     * @param e 事件
     * @return {@link String}
     **/
    public static String getSelectedText(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        return getSelectedText(Objects.requireNonNull(editor));
    }

    /**
     * 获取当前选中字符
     * @author aqiu
     * @date 2020/5/5 10:21 下午
     * @param editor 编辑器
     * @return {@link String}
     **/
    public static String getSelectedText(@NotNull Editor editor) {
        return editor.getSelectionModel().getSelectedText();
    }

    /**
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 是否是Map类型或者是Map的封装类型
     **/
    public static boolean isMap(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        if (TypeConstants.mapTypeMappings.containsKey(typePkName)) {
            return true;
        }
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (TypeConstants.mapTypeMappings.containsKey(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param project: 项目
     * @param typePkName: 类完整包名
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 是否是Map类型或者是Map的封装类型
     **/
    public static boolean isMap(Project project, String typePkName) {
        if (TypeConstants.mapTypeMappings.containsKey(typePkName)) {
            return true;
        }
        PsiClassType psiType = findPsiClassType(project, typePkName);
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (TypeConstants.mapTypeMappings.containsKey(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 是否是集合类型或者是集合的封装类型
     **/
    public static boolean isCollection(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        if (TypeConstants.collectionTypeMappings.containsKey(typePkName)) {
            return true;
        }
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (TypeConstants.collectionTypeMappings.containsKey(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param project: 项目
     * @param typePkName: 类完整包名
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 是否是集合类型或者是集合的封装类型
     **/
    public static boolean isCollection(Project project, String typePkName) {
        if (TypeConstants.collectionTypeMappings.containsKey(typePkName)) {
            return true;
        }
        PsiClassType psiType = findPsiClassType(project, typePkName);
        PsiType[] parentTypes = psiType.getSuperTypes();
        if (parentTypes.length > 0) {
            for (PsiType parentType : parentTypes) {
                String parentTypeName = parentType.getCanonicalText().split("<")[0];
                if (TypeConstants.collectionTypeMappings.containsKey(parentTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 通过类完整路径获取相应的psiClass对象
     * @author aqiu
     * @param project 项目
     * @param typePkName 类完整路径
     */
    public static PsiClass findPsiClass(Project project, String typePkName) {
        return JavaPsiFacade.getInstance(project)
                .findClass(typePkName,
                        GlobalSearchScope.allScope(project));
    }

    /**
     * 通过类完整路径获取相应的psiClassType对象
     * @author aqiu
     * @param project 项目
     * @param typePkName 类完整路径
     */
    public static PsiClassType findPsiClassType(Project project, String typePkName) {
        return PsiType.getTypeByName(typePkName, project,
                GlobalSearchScope.allScope(project));
    }

}
