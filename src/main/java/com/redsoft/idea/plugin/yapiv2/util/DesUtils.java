package com.redsoft.idea.plugin.yapiv2.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.search.GlobalSearchScope;
import com.jgoodies.common.base.Strings;
import java.util.Objects;

/**
 * 描述工具
 *
 * @date 2019/4/30 4:13 PM
 */
public final class DesUtils {

    /**
     * 去除字符串首尾出现的某个字符.
     *
     * @param source 源字符串.
     * @param element 需要去除的字符.
     */
    private static String trimFirstAndLastChar(String source, char element) {
        boolean beginIndexFlag;
        boolean endIndexFlag;
        do {
            if (Strings.isEmpty(source.trim()) || source.equals(String.valueOf(element))) {
                source = "";
                break;
            }
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex =
                    source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element)
                            : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

    /**
     * @description 通过paramName 获得描述
     * @date 2019/5/22
     */
    public static String getParamDesc(PsiMethod psiMethodTarget, String paramName) {
        if (psiMethodTarget.getDocComment() != null) {
            PsiDocTag[] psiDocTags = psiMethodTarget.getDocComment().getTags();
            for (PsiDocTag psiDocTag : psiDocTags) {
                if ((psiDocTag.getText().contains("@param") || psiDocTag.getText()
                        .contains("@Param"))
                        && (!psiDocTag.getText().contains("[")) && psiDocTag.getText()
                        .contains(paramName)) {
                    return trimFirstAndLastChar(
                            psiDocTag.getText().replace("@param", "").replace("@Param", "")
                                    .replace(paramName, "")
                                    .replace(":", "").replace("*", "").replace("\n", " "), ' ')
                            .replace("\t", "");
                }
            }
        }
        return "";
    }

    /**
     * @description 获得属性注释
     * @date 2019/4/27
     */
    public static String getFiledDesc(PsiField psiField) {
        PsiDocComment psiDocComment = psiField.getDocComment();
        if (Objects.nonNull(psiDocComment)) {
            String fileText = psiDocComment.getText();
            if (!Strings.isEmpty(fileText)) {
                return trimFirstAndLastChar(
                        fileText.replace("*", "").replace("/", "")
                                .replace("\n", "")
                                .replace("\t", ""), ',').split("\\{@link")[0].trim();
            }
        }
        return "";
    }


    /**
     * @description 获得link 备注
     * @date 2019/5/18
     */
    public static String getLinkRemark(PsiField field, Project project) {
        StringBuilder remark = new StringBuilder(DesUtils.getFiledDesc(field));
        // 尝试获得@link 的常量定义
        if (Objects.isNull(field.getDocComment())) {
            return remark.toString();
        }
        String[] linkString = field.getDocComment().getText().split("@link");
        if (linkString.length > 1) {
            //说明有link
            String linkAddress = linkString[1].split("}")[0].trim();
            PsiClass psiClassLink = JavaPsiFacade.getInstance(project)
                    .findClass(linkAddress, GlobalSearchScope.allScope(project));
            if (Objects.isNull(psiClassLink)) {
                //可能没有获得全路径，尝试获得全路径
                String[] importPaths = Objects.requireNonNull(field.getParent().getContext())
                        .getText().split("import");
                if (importPaths.length > 1) {
                    for (String importPath : importPaths) {
                        if (importPath.contains(linkAddress.split("\\.")[0])) {
                            linkAddress =
                                    importPath.split(linkAddress.split("\\.")[0])[0] + linkAddress;
                            psiClassLink = JavaPsiFacade.getInstance(project)
                                    .findClass(linkAddress.trim(),
                                            GlobalSearchScope.allScope(project));
                            break;
                        }
                    }
                }
                //如果小于等于一为不存在import，不做处理
            }
            if (Objects.nonNull(psiClassLink)) {
                //说明获得了link 的class
                PsiField[] linkFields = psiClassLink.getFields();
                if (linkFields.length > 0) {
                    remark.append(",").append(psiClassLink.getName()).append("[");
                    for (int i = 0; i < linkFields.length; i++) {
                        PsiField psiField = linkFields[i];
                        if (i > 0) {
                            remark.append(",");
                        }
                        // 先获得名称
                        remark.append(psiField.getName());
                        // 后获得value,通过= 来截取获得，第二个值，再截取;
                        String[] splitValue = psiField.getText().split("=");
                        if (splitValue.length > 1) {
                            String value = splitValue[1].split(";")[0];
                            remark.append(":").append(value);
                        }
                        String filedValue = DesUtils.getFiledDesc(psiField);
                        if (!Strings.isEmpty(filedValue)) {
                            remark.append("(").append(filedValue).append(")");
                        }
                    }
                    remark.append("]");
                }
            }
        }
        return remark.toString();
    }

}
