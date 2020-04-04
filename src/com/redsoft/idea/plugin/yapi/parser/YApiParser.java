package com.redsoft.idea.plugin.yapi.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.constant.HttpMethodConstants;
import com.redsoft.idea.plugin.yapi.constant.NotificationConstants;
import com.redsoft.idea.plugin.yapi.constant.PropertyNamingStrategy;
import com.redsoft.idea.plugin.yapi.constant.ServletConstants;
import com.redsoft.idea.plugin.yapi.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapi.constant.SwaggerConstants;
import com.redsoft.idea.plugin.yapi.constant.TypeConstants;
import com.redsoft.idea.plugin.yapi.constant.YApiConstants;
import com.redsoft.idea.plugin.yapi.model.DecimalRange;
import com.redsoft.idea.plugin.yapi.model.IntegerRange;
import com.redsoft.idea.plugin.yapi.model.LongRange;
import com.redsoft.idea.plugin.yapi.model.ValueWrapper;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;
import com.redsoft.idea.plugin.yapi.model.YApiFormDTO;
import com.redsoft.idea.plugin.yapi.model.YApiHeaderDTO;
import com.redsoft.idea.plugin.yapi.model.YApiPathVariableDTO;
import com.redsoft.idea.plugin.yapi.model.YApiQueryDTO;
import com.redsoft.idea.plugin.yapi.model.YApiStatusEnum;
import com.redsoft.idea.plugin.yapi.schema.ArraySchema;
import com.redsoft.idea.plugin.yapi.schema.BooleanSchema;
import com.redsoft.idea.plugin.yapi.schema.IntegerSchema;
import com.redsoft.idea.plugin.yapi.schema.NumberSchema;
import com.redsoft.idea.plugin.yapi.schema.ObjectSchema;
import com.redsoft.idea.plugin.yapi.schema.SchemaHelper;
import com.redsoft.idea.plugin.yapi.schema.StringSchema;
import com.redsoft.idea.plugin.yapi.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapi.schema.base.SchemaType;
import com.redsoft.idea.plugin.yapi.support.YApiSupports;
import com.redsoft.idea.plugin.yapi.support.swagger.YApiSwaggerSupport;
import com.redsoft.idea.plugin.yapi.util.DesUtil;
import com.redsoft.idea.plugin.yapi.util.PropertyNamingUtils;
import com.redsoft.idea.plugin.yapi.util.PsiAnnotationSearchUtil;
import com.redsoft.idea.plugin.yapi.util.ValidUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author aqiu
 * @date 2019-06-15 11:46
 * @description 接口信息解析
 **/
public class YApiParser {

    private Project project;
    private PropertyNamingStrategy strategy;
    private PropertyNamingStrategy c_strategy = null;
    private PropertyNamingStrategy m_strategy = null;
    private boolean enableBasicScope;

    private YApiSupports supports = new YApiSupports(YApiSwaggerSupport.INSTANCE);

    public List<YApiDTO> parse(AnActionEvent e, PropertyNamingStrategy strategy,
            boolean enableBasicScope) {
        this.strategy = strategy;
        this.enableBasicScope = enableBasicScope;
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String selectedText = e.getRequiredData(CommonDataKeys.EDITOR).getSelectionModel()
                .getSelectedText();
        this.project = e.getProject();
        if (Strings.isEmpty(selectedText)) {
            NotificationConstants.NOTIFICATION_GROUP
                    .createNotification(YApiConstants.name, "提示", "请选中类或者方法",
                            NotificationType.ERROR).notify(this.project);
            return null;
        }
        PsiElement referenceAt = Objects.requireNonNull(psiFile)
                .findElementAt(Objects.requireNonNull(editor).getCaretModel().getOffset());
        PsiClass selectedClass = PsiTreeUtil.getContextOfType(referenceAt, PsiClass.class);
        //获取该类是否已经过时
        if (PsiAnnotationSearchUtil.hasDeprecated(Objects.requireNonNull(selectedClass)) || (
                selectedClass.getDocComment() != null && DesUtil
                        .deprecated(selectedClass.getDocComment().getText()))) {
            NotificationConstants.NOTIFICATION_GROUP
                    .createNotification(YApiConstants.name, "该类已过时",
                            "该类(或注释中)含有@Deprecated注解，如需上传，请删除该注解", NotificationType.WARNING)
                    .notify(this.project);
            return null;
        }
        String classMenu = null;
        String menuDesc = null;
        //如果类文件上有注解，读取接口分类信息
        if (Objects.nonNull(selectedClass.getDocComment())) {
            String text = selectedClass.getText();
            classMenu = DesUtil.getMenu(text);
            menuDesc = DesUtil.getMenuDesc(text);
        }
        ArrayList<YApiDTO> yApiDTOS = new ArrayList<>();
        //如果用户选中的是类
        if (selectedText.equals(selectedClass.getName())) {
            PsiMethod[] psiMethods = selectedClass.getMethods();
            for (PsiMethod psiMethodTarget : psiMethods) {
                //lombok插件的构造方法忽略
                if (psiMethodTarget.getName().equals(selectedClass.getName())) {
                    continue;
                }
                //去除私有方法
                if (!psiMethodTarget.getModifierList().hasModifierProperty("private")) {
                    //带有 @Deprecated 注解的方法跳过
                    if (PsiAnnotationSearchUtil.hasDeprecated(psiMethodTarget) || (
                            psiMethodTarget.getDocComment() != null && DesUtil
                                    .deprecated(psiMethodTarget.getDocComment().getText()))) {
                        continue;
                    }
                    YApiDTO yApiDTO = null;
                    try {
                        yApiDTO = handleMethod(selectedClass, psiMethodTarget);
                    } catch (Exception ex) {
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "接口信息解析失败",
                                        "失败原因：" + ex.getMessage(),
                                        NotificationType.ERROR).notify(this.project);
                    } finally {
                        this.m_strategy = null;
                    }
                    if (Objects.isNull(yApiDTO)) {
                        continue;
                    }
                    //如果方法注释中没有有接口分类信息，使用类中声明的接口分类
                    if (Objects.isNull(yApiDTO.getMenu())) {
                        yApiDTO.setMenu(classMenu);
                    }
                    //分类描述信息设置
                    if (Objects.nonNull(menuDesc)) {
                        yApiDTO.setMenuDesc(menuDesc);
                    }
                    yApiDTOS.add(yApiDTO);
                }
            }
        } else {//如果用户选中的是方法
            PsiMethod[] psiMethods = selectedClass.getAllMethods();
            //寻找目标Method
            PsiMethod psiMethodTarget = null;
            for (PsiMethod psiMethod : psiMethods) {
                if (psiMethod.getName().equals(selectedText)) {
                    psiMethodTarget = psiMethod;
                    break;
                }
            }
            if (Objects.nonNull(psiMethodTarget)) {
                YApiDTO yApiDTO = null;
                try {
                    if (PsiAnnotationSearchUtil.hasDeprecated(psiMethodTarget) || (
                            psiMethodTarget.getDocComment() != null && DesUtil
                                    .deprecated(psiMethodTarget.getDocComment().getText()))) {
                        NotificationConstants.NOTIFICATION_GROUP
                                .createNotification(YApiConstants.name, "接口或类已过时",
                                        "该方法或者类(或注释中)含有@Deprecated注解，如需上传，请删除该注解:" + selectedText,
                                        NotificationType.WARNING).notify(this.project);
                        return null;
                    } else {
                        yApiDTO = handleMethod(selectedClass, psiMethodTarget);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NotificationConstants.NOTIFICATION_GROUP
                            .createNotification(YApiConstants.name, "接口信息解析失败",
                                    "失败原因：" + ex.getMessage(),
                                    NotificationType.ERROR).notify(this.project);
                }
                if (Objects.isNull(Objects.requireNonNull(yApiDTO).getMenu())) {
                    yApiDTO.setMenu(classMenu);
                }
                yApiDTOS.add(yApiDTO);
            } else {
                NotificationConstants.NOTIFICATION_GROUP
                        .createNotification(YApiConstants.name, "找不到方法", "方法名称:" + selectedText,
                                NotificationType.ERROR)
                        .notify(this.project);
                return null;
            }
        }
        return yApiDTOS;
    }

    /**
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 00:06
     * @description 是否返回Json格式数据
     **/
    private boolean isResponseJson(PsiClass psiClass, PsiMethod psiMethod) {
        return ValidUtil.hasAnnotation(psiClass, SpringMVCConstants.RestController) ||
                ValidUtil.hasAnnotation(psiClass, SpringMVCConstants.ResponseBody) ||
                ValidUtil.hasAnnotation(psiMethod, SpringMVCConstants.ResponseBody);
    }

    /**
     * 根据方法生成 YApiDTO （设置请求参数和path,method,desc,menu等字段）
     */
    private YApiDTO handleMethod(PsiClass selectedClass, PsiMethod psiMethodTarget) {
        YApiDTO yApiDTO = new YApiDTO();
        // 获得路径
        StringBuilder path = new StringBuilder();
        String status = null;
        StringBuilder customPath = null;
        PsiDocComment classDoc;
        PsiDocComment methodDoc;
        if (Objects.nonNull(classDoc = selectedClass.getDocComment())) {
            String s = DesUtil.getStatus(classDoc.getText());
            if (Strings.isNotBlank(s)) {
                status = s;
            }
            //获取类注释上的@strategy注释（接口字段命名策略）
            String cs = DesUtil.getStrategy(classDoc.getText());
            if (Strings.isNotBlank(cs)) {
                this.c_strategy = PropertyNamingStrategy.of(cs);
            }
            //获取类注释上的@path注解（自定义路由用）
            String cpath = DesUtil.getPath(classDoc.getText());
            if (Strings.isNotBlank(cpath)) {
                customPath = new StringBuilder(this.buildPath(cpath));
            }
            if (customPath == null) {
                //获取类注释上的@prefix注解，（网关专用）
                String cprefix = DesUtil.getPrefix(classDoc.getText());
                if (Strings.isNotBlank(cprefix)) {
                    path.append(this.buildPath(cprefix));
                }
            }
        }
        if (Objects.nonNull(methodDoc = psiMethodTarget.getDocComment())) {
            String s = DesUtil.getStatus(methodDoc.getText());
            if (Strings.isNotBlank(s)) {
                status = s;
            }
            //获取方法上的@strategy注释（接口字段命名策略）
            String ms = DesUtil.getStrategy(methodDoc.getText());
            if (Strings.isNotBlank(ms)) {
                this.m_strategy = PropertyNamingStrategy.of(ms);
            }
            //获取方法注释上的@path注解（自定义路由用）
            String mpath = DesUtil.getPath(methodDoc.getText());
            if (Strings.isNotBlank(mpath)) {
                if (customPath == null) {
                    customPath = new StringBuilder(this.buildPath(mpath));
                } else {
                    if (mpath.startsWith("/")) {
                        customPath = new StringBuilder(this.buildPath(mpath));
                    } else {
                        customPath.append(this.buildPath(mpath));
                    }
                }
            }
            if (customPath == null) {
                //获取方法注释上的@prefix注解，（网关专用）
                String mprefix = DesUtil.getPrefix(methodDoc.getText());
                if (Strings.isNotBlank(mprefix)) {
                    if (mprefix.startsWith("/")) {
                        path = new StringBuilder();
                    }
                    path.append(this.buildPath(mprefix));
                }
            }
        }
        if (Strings.isNotBlank(status)) {
            yApiDTO.setStatus(YApiStatusEnum.getStatus(status));
        }
        if (customPath != null) {
            yApiDTO.setPath(this.buildPath(customPath));
        }
        //获取类上面的RequestMapping 中的value
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(selectedClass, SpringMVCConstants.RequestMapping);
        if (customPath == null && psiAnnotation != null) {
            path.append(this.buildPath(this.getPathByAnno(psiAnnotation)));
        }
        //获取方法上的RequestMapping注解
        PsiAnnotation psiAnnotationMethod = PsiAnnotationSearchUtil
                .findAnnotation(psiMethodTarget, SpringMVCConstants.RequestMapping);
        if (psiAnnotationMethod != null) {
            if (customPath == null) {
                path.append(this.buildPath(this.getPathByAnno(psiAnnotationMethod)));
                yApiDTO.setPath(this.buildPath(path));
            }
            PsiAnnotationMemberValue method = psiAnnotationMethod.findAttributeValue("method");
            if (method != null) {
                yApiDTO.setMethod(method.getText().toUpperCase());
            }
        } else {
            PsiAnnotation psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                    .findAnnotation(psiMethodTarget, SpringMVCConstants.GetMapping);
            if (psiAnnotationMethodSemple != null) {
                yApiDTO.setMethod(HttpMethodConstants.GET);
            } else {
                psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                        .findAnnotation(psiMethodTarget, SpringMVCConstants.PostMapping);
                if (psiAnnotationMethodSemple != null) {
                    yApiDTO.setMethod(HttpMethodConstants.POST);
                } else {
                    psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                            .findAnnotation(psiMethodTarget, SpringMVCConstants.PutMapping);
                    if (psiAnnotationMethodSemple != null) {
                        yApiDTO.setMethod(HttpMethodConstants.PUT);
                    } else {
                        psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                                .findAnnotation(psiMethodTarget, SpringMVCConstants.DeleteMapping);
                        if (psiAnnotationMethodSemple != null) {
                            yApiDTO.setMethod(HttpMethodConstants.DELETE);
                        } else {
                            psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                                    .findAnnotation(psiMethodTarget,
                                            SpringMVCConstants.PatchMapping);
                            if (psiAnnotationMethodSemple != null) {
                                yApiDTO.setMethod(HttpMethodConstants.PATCH);
                            }
                        }
                    }
                }
            }
            if (customPath == null && psiAnnotationMethodSemple != null) {
                path.append(this.buildPath(this.getPathByAnno(psiAnnotationMethodSemple)));
                yApiDTO.setPath(buildPath(path));
            }
        }

        String classDesc = psiMethodTarget.getText().replace(
                Objects.nonNull(psiMethodTarget.getBody()) ? psiMethodTarget.getBody().getText()
                        : "", "");
        if (!Strings.isEmpty(classDesc)) {
            classDesc = classDesc.replace("<", "&lt;").replace(">", "&gt;");
        }
        yApiDTO.setDesc(Objects.nonNull(yApiDTO.getDesc()) ? yApiDTO.getDesc()
                : " <pre><code>  " + classDesc + "</code> </pre>");
        // 生成响应参数
        if (!this.isResponseJson(selectedClass, psiMethodTarget)) {
            yApiDTO.setRes_body_type("raw");
        }
        PsiType returnType = psiMethodTarget.getReturnType();
        if ("raw".equals(yApiDTO.getRes_body_type())) {
            yApiDTO.setResponse(this.getRawResponseJson(returnType));
        } else {
            yApiDTO.setResponse(this.getSchemaResponse(returnType));
        }
        getRequest(yApiDTO, psiMethodTarget);
        //获取swagger注解
        String title = PsiAnnotationSearchUtil
                .getPsiParameterAnnotationValue(psiMethodTarget, SwaggerConstants.API_OPERATION);
        if (Strings.isBlank(title)) {
            title = DesUtil.getDescription(psiMethodTarget);
        }
        if (Strings.isNotBlank(title)) {
            yApiDTO.setTitle(title.replaceAll("\t", "").trim());
        }
        return yApiDTO;
    }

    private String getRawResponseJson(PsiType psiType) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this.getRawResponse(psiType));
    }

    private Object getRawResponse(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        String[] types = typePkName.split("<");
        String type = types[0];
        //如果是基本类型
        if (TypeConstants.isBaseType(typePkName)) {
            return TypeConstants.noramlTypesPackages.get(typePkName).toString();
        } else {
            //如果是Map类型
            if (this.isMap(psiType)) {
                return this.getMapRaw();
                //如果是集合类型（List Set）
            } else if (TypeConstants.arrayTypeMappings.containsKey(type)) {
                return this.getArrayRaw(typePkName);
            } else if (typePkName.endsWith("[]")) {
                //数组形式的返回值（且不是集合类型前缀）
                List<Object> tmp = new ArrayList<>();
                Object obj = this.getObjectRaw(typePkName.replace("[]", ""));
                tmp.add(obj);
                return tmp;
            } else {
                //其他情况 object
                return this.getObjectRaw(typePkName);
            }
        }
    }

    private Map<String, Object> getMapRaw() {
        Map<String, Object> m = new HashMap<>();
        m.put("key", "value");
        return m;
    }

    private List<Object> getArrayRaw(String typePkName) {
        List<Object> result = new ArrayList<>();
        String[] types = typePkName.split("<");
        //如果有泛型
        if (types.length > 1) {
            String childrenType = types[1].split(">")[0];
            childrenType = childrenType.replace("? extends ", "")
                    .replace("? super ", "");
            boolean isWrapArray = childrenType.endsWith("[]");
            //是否是数组类型
            if (isWrapArray) {
                childrenType = childrenType.replace("[]", "");
            }
            //如果是基本类型
            if (TypeConstants.isBaseType(childrenType)) {
                List<Object> tmp = new ArrayList<>();
                tmp.add(TypeConstants.noramlTypesPackages.get(childrenType));
                if (isWrapArray) {
                    result.add(tmp);
                } else {
                    result = tmp;
                }
            } else {
                //如果是其他类型
                List<Object> tmp = new ArrayList<>();
                tmp.add(this.getObjectRaw(childrenType));
                if (isWrapArray) {
                    result.add(tmp);
                } else {
                    result = tmp;
                }
            }
        } else {
            //如果没有泛型
            result.add(this.getMapRaw());
        }
        return result;
    }

    private Object getObjectRaw(String typePkName) {
        Map<String, Object> result = new HashMap<>();
        PsiClass psiClass = JavaPsiFacade.getInstance(this.project)
                .findClass(typePkName, GlobalSearchScope.allScope(this.project));
        if (Objects.nonNull(psiClass)) {
            for (PsiField field : psiClass.getAllFields()) {
                if (
//                        field.getModifierList().hasModifierProperty("final") ||
                        Objects.requireNonNull(field.getModifierList())
                                .hasModifierProperty("static")) {
                    continue;
                }
                String fieldName = field.getName();
                result.put(fieldName, this.getRawResponse(field.getType()));

            }
        }
        return result;
    }


    /**
     * @param psiAnnotation: RequestMapping注解
     * @return {@link String}
     * @author aqiu
     * @date 2019-07-03 10:07
     * @description 获取RequestMapping的路径
     **/
    private String getPathByAnno(PsiAnnotation psiAnnotation) {
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("path");
        if (element == null) {
            return "";
        }
        String value = element.getText();
        if ("{}".equals(value)) {
            value = Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText();
        }
        return "{}".equals(value) ? "" : value.replace("\"", "");
    }


    /**
     * @description 获得请求参数
     * @date 2019/2/19
     */
    private void getRequest(YApiDTO yApiDTO, PsiMethod psiMethodTarget) {
        PsiParameter[] psiParameters = psiMethodTarget.getParameterList().getParameters();
        if (psiParameters.length > 0) {
            boolean hasRequestBody = hasRequestBody(psiParameters);
            String method = yApiDTO.getMethod();
            Set<YApiHeaderDTO> yapiHeaderDTOList = new LinkedHashSet<>();
            Set<YApiPathVariableDTO> yapiPathVariableDTOList = new LinkedHashSet<>();
            for (PsiParameter psiParameter : psiParameters) {
                String desc = psiParameter.getType().getPresentableText();
                // request,response,session 参数跳过
                if (ServletConstants.HttpServletRequest
                        .equals(psiParameter.getType().getCanonicalText())
                        || ServletConstants.HttpServletResponse
                        .equals(psiParameter.getType().getCanonicalText())
                        || ServletConstants.HttpSession
                        .equals(psiParameter.getType().getCanonicalText())) {
                    continue;
                }
                YApiHeaderDTO yapiHeaderDTO = null;
                YApiPathVariableDTO yapiPathVariableDTO = null;
                PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                        .findAnnotation(psiParameter, SpringMVCConstants.RequestHeader);
                //参数上有@RequestHeader注解
                if (psiAnnotation != null) {
                    yapiHeaderDTO = new YApiHeaderDTO();
                } else {
                    psiAnnotation = PsiAnnotationSearchUtil
                            .findAnnotation(psiParameter, SpringMVCConstants.PathVariable);
                    //参数上有@PathVariable注解
                    if (psiAnnotation != null) {
                        yapiPathVariableDTO = new YApiPathVariableDTO();
                    }
                }
                if (psiAnnotation != null) {
                    ValueWrapper valueWrapper = handleParamAnnotation(psiAnnotation, psiParameter);
                    // 通过方法注释获得 描述 加上 类型
                    String description =
                            DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + " ("
                                    + desc + ")";
                    valueWrapper.setDesc(description.replace("\t", ""));
                    if (yapiHeaderDTO != null) {
                        yapiHeaderDTO.full(valueWrapper);
                        yapiHeaderDTOList.add(yapiHeaderDTO);
                        continue;
                    }
                    yapiPathVariableDTO.full(valueWrapper);
                    yapiPathVariableDTOList.add(yapiPathVariableDTO);
                } else { //没有@RequestHeader和@PathVariable注解
                    if (HttpMethodConstants.GET.equals(method) || HttpMethodConstants.DELETE
                            .equals(method)) {
                        Set<YApiQueryDTO> queryDTOList = this.getRequestQuery(psiMethodTarget,
                                psiParameter);
                        if (yApiDTO.getParams() == null) {
                            yApiDTO.setParams(queryDTOList);
                        } else {
                            yApiDTO.getParams().addAll(queryDTOList);
                        }
                    } else if (HttpMethodConstants.PUT.equals(method) || HttpMethodConstants.POST
                            .equals(method) || HttpMethodConstants.PATCH.equals(method)) {
                        //参数中含有@RequestBody注解
                        if (hasRequestBody) {
                            yApiDTO.setReq_body_type("json");
                            psiAnnotation = PsiAnnotationSearchUtil
                                    .findAnnotation(psiParameter, SpringMVCConstants.RequestBody);
                            //方法参数中有@RequestBody注解，但是当前参数无@RequestBody注解，当作Query参数处理
                            if (psiAnnotation == null) {
                                Set<YApiQueryDTO> yapiQueryDTO = getRequestQuery(
                                        psiMethodTarget, psiParameter);
                                if (yApiDTO.getParams() == null) {
                                    yApiDTO.setParams(yapiQueryDTO);
                                } else {
                                    yApiDTO.getParams().addAll(yapiQueryDTO);
                                }
                            } else {
                                yApiDTO.setRequestBody(
                                        getPojoJson(psiParameter.getType()));
                            }
                        } else {//到这儿只能是form参数
                            // 支持实体对象接收
                            yApiDTO.setReq_body_type("form");
                            Set<YApiFormDTO> formParams = getRequestForm(psiParameter,
                                    psiMethodTarget);
                            if (yApiDTO.getReq_body_form() != null) {
                                yApiDTO.getReq_body_form().addAll(formParams);
                            } else {
                                yApiDTO.setReq_body_form(formParams);
                            }
                        }
                    }
                }
            }
            yApiDTO.setHeader(yapiHeaderDTOList);
            yApiDTO.setReq_params(yapiPathVariableDTOList);
        }
    }

    /**
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 10:02
     * @description 参数是否含有@RequestBody注解
     **/
    private boolean hasRequestBody(PsiParameter[] psiParameters) {
        for (PsiParameter psiParameter : psiParameters) {
            if (PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstants.RequestBody) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param psiMethodTarget: 方法
     * @param psiParameter: 参数
     * @return {@link List<YApiQueryDTO>}
     * @author aqiu
     * @date 2019-07-03 10:01
     * @description 获取Query参数
     **/
    private Set<YApiQueryDTO> getRequestQuery(PsiMethod psiMethodTarget,
            PsiParameter psiParameter) {
        Set<YApiQueryDTO> results = new LinkedHashSet<>();
        String typeClassName = psiParameter.getType().getCanonicalText();
        String typeName = psiParameter.getType().getPresentableText();
        //如果是基本类型
        if (TypeConstants.isNormalType(typeName)) {
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstants.RequestParam);
            YApiQueryDTO yapiQueryDTO = new YApiQueryDTO();
            if (psiAnnotation != null) {
                ValueWrapper valueWrapper = handleParamAnnotation(psiAnnotation, psiParameter);
                yapiQueryDTO.full(valueWrapper);
            } else {//没有注解
                yapiQueryDTO.setRequired(ValidUtil.notNullOrBlank(psiParameter) ? "1" : "0");
                yapiQueryDTO.setName(psiParameter.getName());
                yapiQueryDTO.setExample(TypeConstants.normalTypes.get(typeName)
                        .toString());
            }
            yapiQueryDTO.setDesc(DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + "("
                    + typeName + ")");
            results.add(yapiQueryDTO);
        } else {
            PsiClass psiClass = JavaPsiFacade.getInstance(this.project)
                    .findClass(typeClassName, GlobalSearchScope.allScope(this.project));
            for (PsiField field : Objects.requireNonNull(psiClass).getAllFields()) {
                if (
//                        field.getModifierList().hasModifierProperty("final") ||
                        Objects.requireNonNull(field.getModifierList())
                                .hasModifierProperty("static")) {
                    continue;
                }
                YApiQueryDTO query = new YApiQueryDTO();
                query.setRequired(ValidUtil.notNullOrBlank(field) ? "1" : "0");
                query.setName(field.getName());
                String remark = DesUtil.getLinkRemark(field, this.project);
                query.setDesc(remark);
                String typePkName = field.getType().getCanonicalText();
                if (TypeConstants.isBaseType(typePkName)) {
                    query.setExample(
                            TypeConstants.noramlTypesPackages.get(typePkName)
                                    .toString());
                }
                results.add(query);
            }
        }
        return results;
    }


    /**
     * @description 获得表单提交数据对象
     * @return {@link List<YApiFormDTO>}
     * @date 2019/5/17
     */
    private Set<YApiFormDTO> getRequestForm(PsiParameter psiParameter, PsiMethod psiMethodTarget) {
        Set<YApiFormDTO> requestForm = new LinkedHashSet<>();
        String paramName = psiParameter.getName();
        String typeName = psiParameter.getType().getPresentableText();
        String required = ValidUtil.notNullOrBlank(psiParameter) ? "1" : "0";
        String typeClassName = psiParameter.getType().getCanonicalText();
        if (typeClassName.endsWith("[]")) {
            typeClassName = typeClassName.replace("[]", "");
        }
        //如果是基本类型或者文件
        String remark =
                DesUtil.getParamDesc(psiMethodTarget, paramName) + "(" + psiParameter
                        .getType().getPresentableText() + ")";
        if (TypeConstants.isNormalType(typeName) || SpringMVCConstants.MultipartFile
                .equals(typeClassName)) {
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstants.RequestParam);
            YApiFormDTO form = new YApiFormDTO();
            //如果参数是文件类型
            if (SpringMVCConstants.MultipartFile.equals(typeClassName)) {
                form.setType("file");
            }
            form.setDesc(remark);
            if (psiAnnotation == null) {//没有@RequestParam注解
                form.setName(paramName);
                form.setRequired(required);
                form.setExample(
                        TypeConstants.normalTypes.get(typeName).toString());
            } else {//处理@RequestParam注解
                ValueWrapper valueWrapper = handleParamAnnotation(psiAnnotation, psiParameter);
                form.full(valueWrapper);
            }
            this.supports.handleParam(psiParameter, form);
            requestForm.add(form);
        } else {//非基本类型
            PsiClass psiClass = JavaPsiFacade.getInstance(this.project)
                    .findClass(typeClassName, GlobalSearchScope.allScope(this.project));
            for (PsiField field : Objects.requireNonNull(psiClass).getAllFields()) {
                if (
//                        field.getModifierList().hasModifierProperty("final") ||
                        Objects.requireNonNull(field.getModifierList())
                                .hasModifierProperty("static")) {
                    continue;
                }
                String fieldType = field.getType().getCanonicalText();
                YApiFormDTO form = new YApiFormDTO();
                form.setRequired(ValidUtil.notNullOrBlank(field) ? "1" : "0");
                form.setType(SpringMVCConstants.MultipartFile.equals(fieldType) ? "file" : "text");
                form.setName(field.getName());
                remark = DesUtil.getLinkRemark(field, this.project);
                form.setDesc(remark);
                Object obj = TypeConstants.normalTypes
                        .get(field.getType().getPresentableText());
                if (Objects.nonNull(obj)) {
                    form.setExample(
                            TypeConstants.normalTypes.get(field.getType().getPresentableText())
                                    .toString());
                }
                this.supports.handleField(field, form);
                requestForm.add(form);
            }
        }
        return requestForm;
    }

    /**
     * @return {@link ValueWrapper}
     * @author aqiu
     * @date 2019-06-15 08:48
     * @description 处理部分参数注解 @RequestParam @RequestHeader
     **/
    private ValueWrapper handleParamAnnotation(PsiAnnotation psiAnnotation,
            PsiParameter psiParameter) {
        ValueWrapper valueWrapper = new ValueWrapper();
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("name");
        if (Objects.nonNull(element)) {
            String name = element.getText();
            if (Strings.isEmpty(name)) {
                name = Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText();
            }
            valueWrapper.setName(name.replace("\"", ""));
        }
        PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
        if (Objects.nonNull(required)) {
            valueWrapper.setRequired(
                    required.getText().replace("\"", "")
                            .replace("false", "0")
                            .replace("true", "1"));
        }
        PsiAnnotationMemberValue defaultValue = psiAnnotation.findAttributeValue("defaultValue");
        if (Objects.nonNull(defaultValue)
                && !"\"\\n\\t\\t\\n\\t\\t\\n\\uE000\\uE001\\uE002\\n\\t\\t\\t\\t\\n\""
                .equals(defaultValue.getText())) {
            valueWrapper.setExample(defaultValue.getText().replace("\"", ""));
            valueWrapper.setRequired("0");
        }
        if (Strings.isEmpty(valueWrapper.getRequired())) {
            valueWrapper.setRequired("1");
        }
        if (Strings.isEmpty(valueWrapper.getName())) {
            valueWrapper.setName(psiParameter.getName());
        }
        if (Strings.isEmpty(valueWrapper.getExample())) {
            valueWrapper.setExample(
                    TypeConstants.noramlTypesPackages.get(psiParameter.getType().getCanonicalText())
                            .toString());
        }
        return valueWrapper;
    }

    /**
     * @description 获得响应参数
     * @date 2019/2/19
     */
    private String getSchemaResponse(PsiType psiType) {
        return getPojoJson(psiType);
    }


    private String getPojoJson(PsiType psiType) {
        return this.getSchema(psiType, true).toPrettyJson();
    }

    private ItemJsonSchema getSchema(PsiType psiType, boolean needSchema) {
        String typePkName = psiType.getCanonicalText();
        ItemJsonSchema result;
        //如果是基本类型
        if (TypeConstants.isBaseType(typePkName)) {
            result = SchemaHelper.parse(TypeConstants.normalTypeMappings.get(typePkName));
            result.setDefault(TypeConstants.noramlTypesPackages.get(typePkName).toString());
            result.setMock(TypeConstants.formatMockType(psiType.getPresentableText()));
        } else {
            result = this.getOtherTypeSchema(psiType);
        }
        if (needSchema) {
            result.set$schema(YApiConstants.$schema);
        }
        return result;
    }

    /**
     * @return {@link ItemJsonSchema}
     * @author aqiu
     * @date 2019-07-03 09:53
     * @description 通过字段属性获取Schema信息
     **/
    private ItemJsonSchema getFieldSchema(PsiField psiField) {
        PsiType type = psiField.getType();
        String typePkName = type.getCanonicalText();
        ItemJsonSchema itemJsonSchema;
        if (TypeConstants.isBaseType(typePkName)) {
            SchemaType schemaType = TypeConstants.normalTypeMappings.get(typePkName);
            itemJsonSchema = getBaseFieldSchema(schemaType, psiField);
            itemJsonSchema.setMock(TypeConstants.formatMockType(type.getPresentableText()));
        } else {
            itemJsonSchema = getOtherFieldSchema(psiField);
        }
        return itemJsonSchema;
    }


    /**
     * @param schemaType: Schema 类型
     * @param psiField: 字段属性
     * @return {@link ItemJsonSchema}
     * @author aqiu
     * @date 2019-07-03 09:52
     * @description 获取基本类型Schema信息
     **/
    private ItemJsonSchema getBaseFieldSchema(SchemaType schemaType, PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typePkName = psiType.getCanonicalText();
        ItemJsonSchema result;
        switch (schemaType) {
            case number:
                NumberSchema numberSchema = new NumberSchema();
                DecimalRange decimalRange = ValidUtil.rangeDecimal(psiField);
                if (Objects.nonNull(decimalRange)) {
                    numberSchema.setRange(decimalRange);
                }
                if (ValidUtil.isPositive(psiField)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                    numberSchema.setExclusiveMinimum(true);
                }
                if (ValidUtil.isPositiveOrZero(psiField)) {
                    numberSchema.setMinimum(new BigDecimal("0"));
                }
                if (ValidUtil.isNegative(psiField)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                    numberSchema.setExclusiveMaximum(true);
                }
                if (ValidUtil.isNegativeOrZero(psiField)) {
                    numberSchema.setMaximum(new BigDecimal("0"));
                }
                result = numberSchema;
                break;
            case integer:
                IntegerSchema integerSchema = new IntegerSchema();
                if (TypeConstants.hasBaseRange(typePkName)) {
                    if (this.enableBasicScope) {
                        integerSchema.setRange(TypeConstants.baseRangeMappings.get(typePkName));
                    }
                }
                LongRange longRange = ValidUtil.range(psiField, this.enableBasicScope);
                if (Objects.nonNull(longRange)) {
                    integerSchema.setRange(longRange);
                }
                if (ValidUtil.isPositive(psiField)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMinimum(true);
                }
                if (ValidUtil.isPositiveOrZero(psiField)) {
                    integerSchema.setMinimum(0L);
                }
                if (ValidUtil.isNegative(psiField)) {
                    integerSchema.setMinimum(0L);
                    integerSchema.setExclusiveMaximum(true);
                }
                if (ValidUtil.isNegativeOrZero(psiField)) {
                    integerSchema.setMinimum(0L);
                }
                result = integerSchema;
                break;
            case string:
                StringSchema stringSchema = new StringSchema();
                IntegerRange integerRange = ValidUtil.rangeLength(psiField, this.enableBasicScope);
                stringSchema.setMinLength(integerRange.getMin());
                stringSchema.setMaxLength(integerRange.getMax());
                String pattern = ValidUtil.getPattern(psiField);
                if (!Strings.isEmpty(pattern)) {
                    stringSchema.setPattern(pattern);
                }
                result = stringSchema;
                break;
            case bool:
                result = new BooleanSchema();
                break;
            default:
                return new StringSchema();
        }
        result.setDescription(DesUtil.getLinkRemark(psiField, this.project));
        result.setDefault(TypeConstants.noramlTypesPackages.get(typePkName).toString());
        return result;
    }

    /**
     * @param psiField: 属性字段
     * @return {@link ItemJsonSchema}
     * @author aqiu
     * @date 2019-07-03 09:51
     * @description 根据属性字段获取Schema信息 （非基本类型可用）
     **/
    private ItemJsonSchema getOtherFieldSchema(PsiField psiField) {
        PsiType psiType = psiField.getType();
        String typeName = psiType.getPresentableText();
        boolean wrapArray = typeName.endsWith("[]");
        ItemJsonSchema result = this.getOtherTypeSchema(psiType);
        if (result instanceof ArraySchema) {
            ArraySchema a = (ArraySchema) result;
            if (typeName.contains("Set") && !wrapArray) {
                a.setUniqueItems(true);
            }
            if (ValidUtil.notEmpty(psiField)) {
                a.setMinItems(1);
            }
            IntegerRange integerRange = ValidUtil.rangeSize(psiField, this.enableBasicScope);
            a.setMinItems(integerRange.getMin(), this.enableBasicScope);
            a.setMaxItems(integerRange.getMax(), this.enableBasicScope);
        }
        result.setDescription(DesUtil.getLinkRemark(psiField, this.project));
        return result;
    }

    /**
     * @param psiType: 类型
     * @return {@link boolean}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 是否是Map类型或者是Map的封装类型
     **/
    private boolean isMap(PsiType psiType) {
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
     * @param typePkName: 类型的完整包名
     * @return {@link ArraySchema}
     * @author aqiu
     * @date 2019-07-03 09:44
     * @description 根据类型的完整名称获取集合类型的Schema信息（只有集合类型可用）
     **/
    private ArraySchema getArraySchema(String typePkName) {
        String[] types = typePkName.split("<");
        ArraySchema arraySchema = new ArraySchema();
        //如果有泛型
        if (types.length > 1) {
            String childrenType = types[1].split(">")[0];
            childrenType = childrenType.replace("? extends ", "")
                    .replace("? super ", "");
            boolean isWrapArray = childrenType.endsWith("[]");
            //是否是数组类型
            if (isWrapArray) {
                childrenType = childrenType.replace("[]", "");
            }
            //如果泛型是基本类型
            if (TypeConstants.isBaseType(childrenType)) {
                ItemJsonSchema item = SchemaHelper
                        .parse(TypeConstants.normalTypeMappings.get(childrenType));
                arraySchema.setItems(isWrapArray ? new ArraySchema().setItems(item) : item);
            } else {
                ItemJsonSchema item = this.getObjectSchema(childrenType);
                arraySchema.setItems(isWrapArray ? new ArraySchema().setItems(item) : item);
            }
        } else {
            //没有泛型 默认
            arraySchema.setItems(new ObjectSchema());
        }
        return arraySchema;
    }

    /**
     * @param psiType: 类型
     * @return {@link ItemJsonSchema}
     * @author aqiu
     * @date 2019-07-03 09:43
     * @description 通过类型获取Schema信息（非基本类型）
     **/
    private ItemJsonSchema getOtherTypeSchema(PsiType psiType) {
        String typePkName = psiType.getCanonicalText();
        boolean wrapArray = false;
        if (typePkName.endsWith("[]")) {
            typePkName = typePkName.replace("[]", "");
            wrapArray = true;
        }
        String type = typePkName.split("<")[0];
        ItemJsonSchema result;
        //对Map和Map类型的封装类进行过滤
        if (this.isMap(psiType)) {
            ObjectSchema mapResult = new ObjectSchema();
            result = wrapArray ? new ArraySchema().setItems(mapResult) : mapResult;
        } else if (TypeConstants.arrayTypeMappings.containsKey(type)) {
            //如果是集合类型（List Set）
            ArraySchema tmp = this.getArraySchema(typePkName);
            result = wrapArray ? new ArraySchema().setItems(tmp) : tmp;
        } else if (typePkName.endsWith("[]")) {
            //数组形式的返回值（且不是集合类型前缀）
            typePkName = typePkName.replace("[]", "");
            result = new ArraySchema().setItems(this.getObjectSchema(typePkName));
        } else {
            //其他情况 object
            result = this.getObjectSchema(typePkName);
        }
        return result;
    }

    /**
     * @param typePkName: 类完整包名
     * @return {@link ItemJsonSchema}
     * @author aqiu
     * @date 2019-07-03 09:49
     * @description 通过类完整包名获取object Schema信息（只有自定义pojo类可用）
     **/
    private ItemJsonSchema getObjectSchema(String typePkName) {
        ObjectSchema objectSchema = new ObjectSchema();
        String[] types = typePkName.split("<");
        typePkName = types[0];
        PsiClass psiClass = JavaPsiFacade.getInstance(this.project)
                .findClass(typePkName, GlobalSearchScope.allScope(this.project));
        if (Objects.nonNull(psiClass)) {
            boolean hasChildren;
            PsiClassType classType = null;
            if (hasChildren = types.length == 2) {
                String childrenType = types[1].split(">")[0];
                childrenType = childrenType.replace("? extends ", "")
                        .replace("? super ", "");
                classType = PsiType.getTypeByName(childrenType, this.project,
                        GlobalSearchScope.allScope(this.project));
            } else if (hasChildren = types.length == 3) {
                String childrenType = types[1].split(">")[0] + "<" + types[2].split(">")[0] + ">";
                childrenType = childrenType.replace("? extends ", "")
                        .replace("? super ", "");
                classType = PsiType.getTypeByName(childrenType, this.project,
                        GlobalSearchScope.allScope(this.project));
            }
            for (PsiField field : psiClass.getAllFields()) {
                if (
//                        field.getModifierList().hasModifierProperty("final") ||
                        Objects.requireNonNull(field.getModifierList())
                                .hasModifierProperty("static")) {
                    continue;
                }
                //防止对象内部嵌套自身导致死循环
                if (field.getType().getCanonicalText().contains(
                        Objects.requireNonNull(psiClass.getQualifiedName()))) {
                    continue;
                }
                String fieldName = this.handleFieldName(field.getName());
                if (hasChildren) {
                    String gType = field.getType().getCanonicalText();
                    String[] gTypes = gType.split("<");
                    if (gTypes.length > 1 && TypeConstants.genericList
                            .contains(gTypes[1].split(">")[0]) && TypeConstants.arrayTypeMappings
                            .containsKey(gTypes[0])) {
                        objectSchema.addProperty(fieldName,
                                new ArraySchema().setItems(this.getSchema(classType, false))
                                        .setDescription(
                                                DesUtil.getLinkRemark(field, this.project)));
                    } else if (TypeConstants.genericList
                            .contains(gType)) {
                        objectSchema.addProperty(fieldName, this.getSchema(classType, false)
                                .setDescription(DesUtil.getLinkRemark(field, this.project)));
                    } else {
                        objectSchema.addProperty(fieldName, this.getFieldSchema(field)
                                .setDescription(DesUtil.getLinkRemark(field, this.project)));
                    }
                } else {
                    objectSchema.addProperty(fieldName, this.getFieldSchema(field));
                }
                if (ValidUtil.notNullOrBlank(field)) {
                    objectSchema.addRequired(fieldName);
                }
            }
            return objectSchema;
        }
        return new ObjectSchema();
    }

    private String buildPath(StringBuilder path) {
        return buildPath(path.toString());
    }

    private String buildPath(String path) {
        final String split = "/";
        String pathStr = path.trim();
        pathStr = pathStr.startsWith(split) ? pathStr : (split + pathStr);
        if (pathStr.endsWith("/") && pathStr.length() > 1) {
            pathStr = pathStr.substring(0, pathStr.length() - 1);
        }
        return pathStr;
    }

    private String handleFieldName(String fieldName) {
        PropertyNamingStrategy s =
                this.m_strategy == null ? (this.c_strategy == null ? this.strategy
                        : this.c_strategy) : this.m_strategy;
        return PropertyNamingUtils.convert(fieldName, s);
    }

}
