package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.doc.JavaDocument;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 描述工具
 *
 * @author aqiu 2019/4/30 4:13 PM
 */
public final class DesUtils {

    private static final Map<String, JavaDocument> DOCUMENTS = new HashMap<>();

    public static void clearCache() {
        DOCUMENTS.clear();
    }

    /**
     * 通过paramName 获得描述
     *
     * @author aqiu 2019/5/22
     */
    public static String getParamDesc(PsiMethod psiMethodTarget, String paramName) {
        PsiDocComment docComment = psiMethodTarget.getDocComment();
        if (docComment != null) {
            return DOCUMENTS.compute(elementToString(psiMethodTarget), (k, v) -> {
                if (v == null) {
                    v = new JavaDocument(psiMethodTarget);
                }
                return v;
            }).getParamValue(paramName);
        }
        return "";
    }

    /**
     * 获得属性注释
     *
     * @author aqiu 2019/4/27
     */
    public static String getFiledDesc(PsiField psiField) {
        PsiDocComment psiDocComment = psiField.getDocComment();
        if (Objects.nonNull(psiDocComment)) {
            return DOCUMENTS.compute(elementToString(psiField), (k, v) -> {
                if (v == null) {
                    v = new JavaDocument(psiField);
                }
                return v;
            }).getText();
        }
        return "";
    }


    /**
     * 获得link 备注
     *
     * @author aqiu 2019/5/18
     */
    public static String getLinkRemark(PsiField field, Project project) {
        PsiDocComment docComment = field.getDocComment();
        // 尝试获得@link 的常量定义
        if (Objects.nonNull(docComment)) {
            String desc = DesUtils.getFiledDesc(field);
            if (StringUtils.isNotBlank(desc)) {
                return desc;
            }
            StringBuilder remark = new StringBuilder();
            String[] linkString = docComment.getText().split("@link");
            if (linkString.length > 1) {
                //说明有link
                String linkAddress = linkString[1].split("}")[0].trim();
                PsiClass psiClassLink = PsiUtils.findPsiClass(project, linkAddress);
                if (Objects.isNull(psiClassLink)) {
                    //可能没有获得全路径，尝试获得全路径
                    String[] importPaths = Objects.requireNonNull(field.getParent().getContext())
                            .getText().split("import");
                    if (importPaths.length > 1) {
                        for (String importPath : importPaths) {
                            if (importPath.contains(linkAddress.split("\\.")[0])) {
                                linkAddress =
                                        importPath.split(linkAddress.split("\\.")[0])[0] + linkAddress;
                                psiClassLink = PsiUtils.findPsiClass(project, linkAddress.trim());
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
                            if (!StringUtils.isEmpty(filedValue)) {
                                remark.append("(").append(filedValue).append(")");
                            }
                        }
                        remark.append("]");
                    }
                }
            }
            return remark.toString();
        }
        return "";
    }

    public static String getTypeDesc(String desc) {
        return StringUtils.isBlank(desc) ? "" : ("(" + desc + ")");
    }

    public static void handleTypeDesc(ValueWrapper valueWrapper) {
        String desc;
        desc = (StringUtils.isBlank(desc = valueWrapper.getDesc()) ? "" : desc) + getTypeDesc(valueWrapper.getTypeDesc());
        valueWrapper.setDesc(desc);
    }

    public static String elementToString(PsiNamedElement element) {
        PsiElement parent = element.getParent();
        String className;
        if (parent instanceof PsiClass) {
            className = ((PsiClass) parent).getQualifiedName();
        } else {
            className = parent.toString();
        }
        String elementString = className + "." + element.getName();
        if (element instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element;
            PsiParameterList parameterList = method.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            StringJoiner joiner = new StringJoiner(",", "(", ")");
            for (PsiParameter parameter : parameters) {
                String typeText = parameter.getType().getPresentableText();
                joiner.add(typeText);
            }
            elementString += joiner;
        }
        return elementString;
    }


}
