package com.qdredsoft.plugin.parser;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.compiled.ClsFileImpl;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.qdredsoft.plugin.constant.JavaConstant;
import com.qdredsoft.plugin.constant.NormalTypes;
import com.qdredsoft.plugin.constant.SpringMVCConstant;
import com.qdredsoft.plugin.constant.ValidConstant;
import com.qdredsoft.plugin.ext.KV;
import com.qdredsoft.plugin.model.DecimalRange;
import com.qdredsoft.plugin.model.IntegerRange;
import com.qdredsoft.plugin.model.LongRange;
import com.qdredsoft.plugin.model.ValueWraper;
import com.qdredsoft.plugin.model.YapiApiDTO;
import com.qdredsoft.plugin.model.YapiFormDTO;
import com.qdredsoft.plugin.model.YapiHeaderDTO;
import com.qdredsoft.plugin.model.YapiPathVariableDTO;
import com.qdredsoft.plugin.model.YapiQueryDTO;
import com.qdredsoft.plugin.schema.ArraySchema;
import com.qdredsoft.plugin.schema.BooleanSchema;
import com.qdredsoft.plugin.schema.IntegerSchema;
import com.qdredsoft.plugin.schema.NumberSchema;
import com.qdredsoft.plugin.schema.ObjectSchema;
import com.qdredsoft.plugin.schema.SchemaHelper;
import com.qdredsoft.plugin.schema.StringSchema;
import com.qdredsoft.plugin.schema.base.ItemJsonSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;
import com.qdredsoft.plugin.schema.root.RootArrayJsonSchema;
import com.qdredsoft.plugin.schema.root.RootJsonSchema;
import com.qdredsoft.plugin.util.DesUtil;
import com.qdredsoft.plugin.util.PsiAnnotationSearchUtil;
import com.qdredsoft.plugin.util.ValidUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * @author aqiu
 * @date 2019-06-15 11:46
 * @description 接口信息解析
 **/
public class YapiApiParser {

    private NotificationGroup notificationGroup;

    {
        notificationGroup = new NotificationGroup("Java2Json.NotificationGroup",
                NotificationDisplayType.BALLOON, true);
    }

    public List<YapiApiDTO> parse(AnActionEvent e) {
        Editor editor = (Editor) e.getDataContext().getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = (PsiFile) e.getDataContext().getData(CommonDataKeys.PSI_FILE);
        String selectedText = e.getRequiredData(CommonDataKeys.EDITOR).getSelectionModel()
                .getSelectedText();
        Project project = editor.getProject();
        if (Strings.isNullOrEmpty(selectedText)) {
            Notification error = notificationGroup
                    .createNotification("请选中类或者方法", NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return null;
        }
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass selectedClass = (PsiClass) PsiTreeUtil
                .getContextOfType(referenceAt, PsiClass.class);
        String classMenu = null;
        String menuDesc = null;
        //如果类文件上有注解，读取接口分类信息
        if (Objects.nonNull(selectedClass.getDocComment())) {
            String text = selectedClass.getText();
            classMenu = DesUtil.getMenu(text);
            menuDesc = DesUtil.getMenuDesc(text);
        }
        ArrayList<YapiApiDTO> yapiApiDTOS = new ArrayList<>();
        //如果用户选中的是类
        if (selectedText.equals(selectedClass.getName())) {
            PsiMethod[] psiMethods = selectedClass.getMethods();
            for (PsiMethod psiMethodTarget : psiMethods) {
                //去除私有方法
                if (!psiMethodTarget.getModifierList().hasModifierProperty("private")) {
                    YapiApiDTO yapiApiDTO = null;
                    try {
                        yapiApiDTO = handleMethod(selectedClass, psiMethodTarget, project);
                    } catch (Exception ex) {
                        Notification error = notificationGroup
                                .createNotification("JSON转化失败." + ex.getMessage(),
                                        NotificationType.ERROR);
                        Notifications.Bus.notify(error, project);
                    }
                    if (Objects.isNull(yapiApiDTO)) {
                        continue;
                    }
                    //如果方法注释中没有有接口分类信息，使用类中声明的接口分类
                    if (Objects.isNull(yapiApiDTO.getMenu())) {
                        yapiApiDTO.setMenu(classMenu);
                    }
                    //分类描述信息设置
                    if (Objects.nonNull(menuDesc)) {
                        yapiApiDTO.setMenuDesc(menuDesc);
                    }
                    yapiApiDTOS.add(yapiApiDTO);
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
                YapiApiDTO yapiApiDTO = null;
                try {
                    yapiApiDTO = handleMethod(selectedClass, psiMethodTarget, project);
                    if (Objects.isNull(yapiApiDTO)) {
                        Notification error = notificationGroup
                                .createNotification("该方法注释含有@useless，如需上传，请删除该注解:" + selectedText,
                                        NotificationType.WARNING);
                        Notifications.Bus.notify(error, project);
                        return null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notification error = notificationGroup
                            .createNotification("JSON转化失败." + ex.getMessage(),
                                    NotificationType.ERROR);
                    Notifications.Bus.notify(error, project);
                }
                if (Objects.isNull(yapiApiDTO.getMenu())) {
                    yapiApiDTO.setMenu(classMenu);
                }
                yapiApiDTOS.add(yapiApiDTO);
            } else {
                Notification error = notificationGroup
                        .createNotification("找不到方法:" + selectedText, NotificationType.ERROR);
                Notifications.Bus.notify(error, project);
                return null;
            }
        }
        return yapiApiDTOS;
    }

    /**
     * 根据方法生成 YapiApiDTO （设置请求参数和path,method,desc,menu等字段）
     */
    public YapiApiDTO handleMethod(PsiClass selectedClass, PsiMethod psiMethodTarget,
            Project project) {
        //有@useless注释的方法不上传yapi
        if (DesUtil.notUsed(psiMethodTarget.getDocComment().getText())) {
            return null;
        }
        YapiApiDTO yapiApiDTO = new YapiApiDTO();
        // 获得路径
        StringBuilder path = new StringBuilder();
        // 获取类上面的RequestMapping 中的value
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(selectedClass, SpringMVCConstant.RequestMapping);
        if (psiAnnotation != null) {
            path.append(this.buildPath(this.getPathByAnno(psiAnnotation)));
        }
        ;
        //获取方法上的RequestMapping注解
        PsiAnnotation psiAnnotationMethod = PsiAnnotationSearchUtil
                .findAnnotation(psiMethodTarget, SpringMVCConstant.RequestMapping);
        if (psiAnnotationMethod != null) {
            path.append(this.buildPath(this.getPathByAnno(psiAnnotationMethod)));
            PsiAnnotationMemberValue method = psiAnnotationMethod.findAttributeValue("method");
            if (method != null) {
                yapiApiDTO.setMethod(method.getText().toUpperCase());
            }
            yapiApiDTO.setPath(this.buildPath(path));
        } else {
            PsiAnnotation psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                    .findAnnotation(psiMethodTarget, SpringMVCConstant.GetMapping);
            if (psiAnnotationMethodSemple != null) {
                yapiApiDTO.setMethod("GET");
            } else {
                psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                        .findAnnotation(psiMethodTarget, SpringMVCConstant.PostMapping);
                if (psiAnnotationMethodSemple != null) {
                    yapiApiDTO.setMethod("POST");
                } else {
                    psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                            .findAnnotation(psiMethodTarget, SpringMVCConstant.PutMapping);
                    if (psiAnnotationMethodSemple != null) {
                        yapiApiDTO.setMethod("PUT");
                    } else {
                        psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                                .findAnnotation(psiMethodTarget, SpringMVCConstant.DeleteMapping);
                        if (psiAnnotationMethodSemple != null) {
                            yapiApiDTO.setMethod("DELETE");
                        } else {
                            psiAnnotationMethodSemple = PsiAnnotationSearchUtil
                                    .findAnnotation(psiMethodTarget,
                                            SpringMVCConstant.PatchMapping);
                            if (psiAnnotationMethodSemple != null) {
                                yapiApiDTO.setMethod("PATCH");
                            }
                        }
                    }
                }
            }
            if (psiAnnotationMethodSemple != null) {
                path.append(this.buildPath(this.getPathByAnno(psiAnnotationMethodSemple)));
                yapiApiDTO.setPath(buildPath(path));
            }
        }
        String classDesc = psiMethodTarget.getText().replace(
                Objects.nonNull(psiMethodTarget.getBody()) ? psiMethodTarget.getBody().getText()
                        : "", "");
        if (!Strings.isNullOrEmpty(classDesc)) {
            classDesc = classDesc.replace("<", "&lt;").replace(">", "&gt;");
        }
        yapiApiDTO.setDesc(Objects.nonNull(yapiApiDTO.getDesc()) ? yapiApiDTO.getDesc()
                : " <pre><code>  " + classDesc + "</code> </pre>");
        // 生成响应参数
        yapiApiDTO.setResponse(getResponse(project, psiMethodTarget.getReturnType()));
        getRequest(project, yapiApiDTO, psiMethodTarget);
        if (Strings.isNullOrEmpty(yapiApiDTO.getTitle())) {
            String title = DesUtil.getDescription(psiMethodTarget).replaceAll("\t", "").trim();
            yapiApiDTO.setTitle(title);
        }
        return yapiApiDTO;
    }

    private String getPathByAnno(PsiAnnotation psiAnnotation) {
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("path");
        if (element == null) {
            return "";
        }
        String value = element.getText();
        if ("{}".equals(value)) {
            value = psiAnnotation.findAttributeValue("value").getText();
        }
        return value.replace("\"", "");
    }


    /**
     * @description: 获得请求参数
     * @param: [project, yapiApiDTO, psiMethodTarget]
     * @return: void
     * @date: 2019/2/19
     */
    public void getRequest(Project project, YapiApiDTO yapiApiDTO,
            PsiMethod psiMethodTarget) {
        PsiParameter[] psiParameters = psiMethodTarget.getParameterList().getParameters();
        if (psiParameters.length > 0) {
            boolean hasRequestBody = hasRequestBody(psiParameters);
            String method = yapiApiDTO.getMethod();
            List<YapiHeaderDTO> yapiHeaderDTOList = new ArrayList<>();
            List<YapiPathVariableDTO> yapiPathVariableDTOList = new ArrayList<>();
            for (PsiParameter psiParameter : psiParameters) {
                String desc = psiParameter.getType().getPresentableText();
                // request,response,session 参数跳过
                if (JavaConstant.HttpServletRequest
                        .equals(psiParameter.getType().getCanonicalText())
                        || JavaConstant.HttpServletResponse
                        .equals(psiParameter.getType().getCanonicalText())
                        || JavaConstant.HttpSession
                        .equals(psiParameter.getType().getCanonicalText())) {
                    continue;
                }
                YapiHeaderDTO yapiHeaderDTO = null;
                YapiPathVariableDTO yapiPathVariableDTO = null;
                PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                        .findAnnotation(psiParameter, SpringMVCConstant.RequestHeader);
                //参数上有@RequestHeader注解
                if (psiAnnotation != null) {
                    yapiHeaderDTO = new YapiHeaderDTO();
                } else {
                    psiAnnotation = PsiAnnotationSearchUtil
                            .findAnnotation(psiParameter, SpringMVCConstant.PathVariable);
                    //参数上有@PathVariable注解
                    if (psiAnnotation != null) {
                        yapiPathVariableDTO = new YapiPathVariableDTO();
                    }
                }
                if (psiAnnotation != null) {
                    ValueWraper valueWraper = handleParamAnnotation(psiAnnotation, psiParameter);
                    // 通过方法注释获得 描述 加上 类型
                    String description =
                            DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + "("
                                    + desc + ")";
                    valueWraper.setDesc(description.replace("\t", ""));
                    if (yapiHeaderDTO != null) {
                        yapiHeaderDTO.full(valueWraper);
                        yapiHeaderDTOList.add(yapiHeaderDTO);
                        continue;
                    }
                    if (yapiPathVariableDTO != null) {
                        yapiPathVariableDTO.full(valueWraper);
                        yapiPathVariableDTOList.add(yapiPathVariableDTO);
                        continue;
                    }
                } else if (psiAnnotation == null) { //没有@RequestHeader和@PathVariable注解
                    if ("GET".equals(method) || "DELETE".equals(method)) {
                        List<YapiQueryDTO> queryDTOList = getRequestQuery(project, psiMethodTarget,
                                psiParameter);
                        if (yapiApiDTO.getParams() == null) {
                            yapiApiDTO.setParams(queryDTOList);
                        } else {
                            yapiApiDTO.getParams().addAll(queryDTOList);
                        }
                    } else if ("PUT".equals(method) || "POST".equals(method) || "PATCH"
                            .equals(method)) {
                        //参数中含有@RequestBody注解，但是当前参数无@RequestBody注解，当作Query参数处理
                        if (hasRequestBody) {
                            psiAnnotation = PsiAnnotationSearchUtil
                                    .findAnnotation(psiParameter, SpringMVCConstant.RequestBody);
                            if (psiAnnotation == null) {
                                List<YapiQueryDTO> yapiQueryDTO = getRequestQuery(project,
                                        psiMethodTarget, psiParameter);
                                if (yapiApiDTO.getParams() == null) {
                                    yapiApiDTO.setParams(yapiQueryDTO);
                                } else {
                                    yapiApiDTO.getParams().addAll(yapiQueryDTO);
                                }
                            } else {
                                yapiApiDTO.setRequestBody(
                                        getPojoJson(project, psiParameter.getType()));
                            }
                        } else {//到这儿只能是form参数
                            // 支持实体对象接收
                            yapiApiDTO.setReq_body_type("form");
                            if (yapiApiDTO.getReq_body_form() != null) {
                                yapiApiDTO.getReq_body_form()
                                        .addAll(getRequestForm(project, psiParameter,
                                                psiMethodTarget));
                            } else {
                                yapiApiDTO.setReq_body_form(
                                        getRequestForm(project, psiParameter, psiMethodTarget));
                            }
                        }
                    }
                }
            }
            yapiApiDTO.setHeader(yapiHeaderDTOList);
            yapiApiDTO.setReq_params(yapiPathVariableDTOList);
        }
    }

    private boolean hasRequestBody(PsiParameter[] psiParameters) {
        for (PsiParameter psiParameter : psiParameters) {
            if (PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstant.RequestBody) != null) {
                return true;
            }
        }
        return false;
    }

    private List<YapiQueryDTO> getRequestQuery(Project project, PsiMethod psiMethodTarget,
            PsiParameter psiParameter) {
        List<YapiQueryDTO> results = new ArrayList<>();
        String typeClassName = psiParameter.getType().getCanonicalText();
        String typeName = psiParameter.getType().getPresentableText();
        //如果是基本类型
        if (NormalTypes.normalTypes.containsKey(typeName)) {
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstant.RequestParam);
            YapiQueryDTO yapiQueryDTO = new YapiQueryDTO();
            if (psiAnnotation != null) {
                ValueWraper valueWraper = handleParamAnnotation(psiAnnotation, psiParameter);
                yapiQueryDTO.full(valueWraper);
            } else {//没有注解
                yapiQueryDTO.setRequired(notNullOrBlank(psiParameter) ? "1" : "0");
                yapiQueryDTO.setName(psiParameter.getName());
                yapiQueryDTO.setExample(NormalTypes.normalTypes.get(typeName)
                        .toString());
            }
            yapiQueryDTO.setDesc(DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + "("
                    + typeName + ")");
            results.add(yapiQueryDTO);
        } else {
            PsiClass psiClass = JavaPsiFacade.getInstance(project)
                    .findClass(typeClassName, GlobalSearchScope.allScope(project));
            for (PsiField field : psiClass.getAllFields()) {
                if (field.getModifierList().hasModifierProperty("final") ||
                        field.getModifierList().hasModifierProperty("static")) {
                    continue;
                }
                YapiQueryDTO query = new YapiQueryDTO();
                query.setRequired(notNullOrBlank(field) ? "1" : "0");
                query.setName(field.getName());
                String remark = DesUtil.getLinkRemark(field, project);
                query.setDesc(remark);
                String typePkName = field.getType().getCanonicalText();
                if (NormalTypes.isBaseType(typePkName)) {
                    query.setExample(
                            NormalTypes.noramlTypesPackages.get(typePkName)
                                    .toString());
                }
                results.add(query);
            }
        }
        return results;
    }


    /**
     * @description: 获得表单提交数据对象
     * @param: [requestClass]
     * @return: java.util.List<YapiFormDTO>
     * @date: 2019/5/17
     */
    public Set<YapiFormDTO> getRequestForm(Project project, PsiParameter psiParameter,
            PsiMethod psiMethodTarget) {
        Set<YapiFormDTO> requestForm = new LinkedHashSet<>();
        String paramName = psiParameter.getName();
        String typeName = psiParameter.getType().getPresentableText();
        String required = notNullOrBlank(psiParameter) ? "1" : "0";
        boolean item = true;
        String typeClassName = psiParameter.getType().getCanonicalText();
        if (typeClassName.endsWith("[]")) {
            typeClassName = typeClassName.replace("[]", "");
        }
        //如果是基本类型或者文件
        String remark =
                DesUtil.getParamDesc(psiMethodTarget, paramName) + "(" + psiParameter
                        .getType().getPresentableText() + ")";
        if (NormalTypes.normalTypes.containsKey(typeName) || JavaConstant.MultipartFile
                .equals(typeClassName)) {
            PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                    .findAnnotation(psiParameter, SpringMVCConstant.RequestParam);
            YapiFormDTO form = new YapiFormDTO();
            //如果参数是文件类型
            if (JavaConstant.MultipartFile.equals(typeClassName)) {
                form.setType("file");
            }
            form.setDesc(remark);
            if (psiAnnotation == null) {//没有@RequestParam注解
                form.setName(paramName);
                form.setRequired(required);
                form.setExample(
                        NormalTypes.normalTypes.get(typeName).toString());
            } else {//处理@RequestParam注解
                ValueWraper valueWraper = handleParamAnnotation(psiAnnotation, psiParameter);
                form.full(valueWraper);
            }
            requestForm.add(form);
        } else {//非基本类型
            PsiClass psiClass = JavaPsiFacade.getInstance(project)
                    .findClass(typeClassName, GlobalSearchScope.allScope(project));
            for (PsiField field : psiClass.getAllFields()) {
                if (field.getModifierList().hasModifierProperty("final") ||
                        field.getModifierList().hasModifierProperty("static")) {
                    continue;
                }
                String fieldType = field.getType().getCanonicalText();
                YapiFormDTO form = new YapiFormDTO();
                form.setRequired(notNullOrBlank(field) ? "1" : "0");
                form.setType(JavaConstant.MultipartFile.equals(fieldType) ? "file" : "text");
                form.setName(field.getName());
                remark = DesUtil.getLinkRemark(field, project);
                form.setDesc(remark);
                if (Objects.nonNull(field.getType().getPresentableText())) {
                    Object obj = NormalTypes.normalTypes.get(field.getType().getPresentableText());
                    if (Objects.nonNull(obj)) {
                        form.setExample(
                                NormalTypes.normalTypes.get(field.getType().getPresentableText())
                                        .toString());
                    }
                }
                requestForm.add(form);
            }
        }
        return requestForm;
    }

    /**
     * @return {@link ValueWraper}
     * @author aqiu
     * @date 2019-06-15 08:48
     * @description 处理部分参数注解 @RequestParam @RequestHeader
     **/
    private ValueWraper handleParamAnnotation(PsiAnnotation psiAnnotation,
            PsiParameter psiParameter) {
        ValueWraper valueWraper = new ValueWraper();
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("name");
        if (Objects.nonNull(element)) {
            String name = element.getText();
            if (Strings.isNullOrEmpty(name)) {
                name = psiAnnotation.findAttributeValue("value").getText();
            }
//            name = psiAnnotation.findAttributeValue("value");
            valueWraper.setName(name.replace("\"", ""));
        }
        PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
        if (Objects.nonNull(required)) {
            valueWraper.setRequired(
                    required.getText().replace("\"", "")
                            .replace("false", "0")
                            .replace("true", "1"));
        }
        PsiAnnotationMemberValue defaultValue = psiAnnotation.findAttributeValue("defaultValue");
        if (Objects.nonNull(defaultValue)) {
            valueWraper.setExample(defaultValue.getText().replace("\"", ""));
        }
        if (Strings.isNullOrEmpty(valueWraper.getRequired())) {
            valueWraper.setRequired("1");
        }
        if (Strings.isNullOrEmpty(valueWraper.getName())) {
            valueWraper.setName(psiParameter.getName());
        }
        if (Strings.isNullOrEmpty(valueWraper.getExample())) {
            valueWraper.setExample(
                    NormalTypes.normalTypes.get(psiParameter.getType().getPresentableText())
                            .toString());
        }
        return valueWraper;
    }

    /**
     * @description: 获得响应参数
     * @param: [project, psiType]
     * @return: java.lang.String
     * @date: 2019/2/19
     */
    public String getResponse(Project project, PsiType psiType) {
        return getPojoJson(project, psiType);
    }

    public boolean notNullOrBlank(@NotNull PsiModifierListOwner psiModifierListOwner) {
        return notNull(psiModifierListOwner) || notBlank(psiModifierListOwner);
    }

    public boolean notNull(@NotNull PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.NotNull);
        return psiAnnotation != null;
    }

    public boolean notBlank(@NotNull PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                .findAnnotation(psiModifierListOwner, ValidConstant.NotBlank);
        return psiAnnotation != null;
    }


    public String getPojoJson(Project project, PsiType psiType) {
        String typeName = psiType.getPresentableText();
        String typePkName = psiType.getCanonicalText();
//        System.out.println("1:"+typeName+"\r\n2:"+typePkName);
        RootJsonSchema schema = new RootJsonSchema();
        //如果是基本类型
        if (NormalTypes.isBaseType(typePkName)) {
            schema.setType(NormalTypes.normalTypeMappings.get(typePkName));
        } else if (typeName.startsWith("List")) {
            RootArrayJsonSchema rootArrayJsonSchema = new RootArrayJsonSchema();
            String[] types = typePkName.split("<");
            KV listKv = new KV();
            if (types.length > 1) {
                String childPackage = types[1].split(">")[0];
                //如果泛型类型为基本类型
                if (NormalTypes.isBaseType(childPackage)) {
                    rootArrayJsonSchema.setItems(
                            SchemaHelper.parse(NormalTypes.normalTypeMappings.get(typePkName)));
                } else if (NormalTypes.collectTypesPackages.containsKey(childPackage)) {
                    //如果泛型类型为MAP
                    rootArrayJsonSchema.setItems(new ObjectSchema());
                } else {
                    //泛型类型为包装类
                    PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                            .findClass(childPackage, GlobalSearchScope.allScope(project));
                    ObjectSchema objectSchema = new ObjectSchema();
                    for (PsiField psiField : psiClassChild.getFields()) {
                        if (psiField.getModifierList().hasModifierProperty("final") ||
                                psiField.getModifierList().hasModifierProperty("static")) {
                            continue;
                        }

                    }
                    List<String> requiredList = new ArrayList<>();
                    KV kvObject = getFields(psiClassChild, project, null, null, requiredList);
                    listKv.set("type", "object");
                    if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild
                            .getSuperClass()
                            .getName().toString().equals("Object")) {
                    }
                    listKv.set("properties", kvObject);
                    listKv.set("required", requiredList);
                }
            }
            KV result = new KV();
            result.set("type", "array");
            result.set("title", psiType.getPresentableText());
            result.set("description", psiType.getPresentableText());
            result.set("items", listKv);
            String json = result.toPrettyJson();
            return json;
        } else if (psiType.getPresentableText().startsWith("Set")) {
            String[] types = psiType.getCanonicalText().split("<");
            KV listKv = new KV();
            if (types.length > 1) {
                String childPackage = types[1].split(">")[0];
                if (NormalTypes.noramlTypesPackages.keySet().contains(childPackage)) {
                    listKv.set("type", NormalTypes.noramlTypesPackages.get(childPackage));
                } else if (NormalTypes.collectTypesPackages.containsKey(childPackage)) {
                    listKv.set("type", NormalTypes.collectTypesPackages.get(childPackage));
                } else {
                    PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                            .findClass(childPackage, GlobalSearchScope.allScope(project));
                    List<String> requiredList = new ArrayList<>();
                    KV kvObject = getFields(psiClassChild, project, null, null, requiredList);
                    listKv.set("type", "object");
                    if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild
                            .getSuperClass()
                            .getName().toString().equals("Object")) {
                    }
                    listKv.set("properties", kvObject);
                    listKv.set("required", requiredList);
                }
            }
            KV result = new KV();
            result.set("type", "array");
            result.set("title", psiType.getPresentableText());
            result.set("description", psiType.getPresentableText());
            result.set("items", listKv);
            String json = result.toPrettyJson();
            return json;
        } else if (psiType.getPresentableText().startsWith("Map")) {
            HashMap hashMapChild = new HashMap();
            String[] types = psiType.getCanonicalText().split("<");
            if (types.length > 1) {
                hashMapChild.put("paramMap", psiType.getPresentableText());
            }
            KV kvClass = KV.create();
            kvClass.set(types[0], hashMapChild);
            KV result = new KV();
            result.set("type", "object");
            result.set("title", psiType.getPresentableText());
            result.set("description", psiType.getPresentableText());
            result.set("properties", hashMapChild);
            String json = result.toPrettyJson();
            return json;
        } else if (NormalTypes.collectTypes.containsKey(psiType.getPresentableText())) {
            //如果是集合类型
            KV kvClass = KV.create();
            kvClass.set(psiType.getCanonicalText(),
                    NormalTypes.collectTypes.get(psiType.getPresentableText()));
        } else {
//            String[] types = psiType.getCanonicalText().split("<");
//            if (types.length > 1) {
//                PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
//                        .findClass(types[0], GlobalSearchScope.allScope(project));
//                KV result = new KV();
//                List<String> requiredList = new ArrayList<>();
//                KV kvObject = getFields(psiClassChild, project, types, 1, requiredList);
//                result.set("type", "object");
//                result.set("title", psiType.getPresentableText());
//                result.set("required", requiredList);
//                if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild.getSuperClass()
//                        .getName().toString().equals("Object")) {
//                }
//                result.set("description",
//                        (psiType.getPresentableText() + " :" + psiClassChild.getName()).trim());
//                result.set("properties", kvObject);
//                String json = result.toPrettyJson();
//                return json;
//            }
            String typeClassName = psiType.getCanonicalText();
            PsiClass psiClass = JavaPsiFacade.getInstance(project)
                    .findClass(typeClassName, GlobalSearchScope.allScope(project));
            KV result = KV.create();
            for (PsiField psiField : psiClass.getFields()) {
                if (psiField.getModifierList().hasModifierProperty("final") ||
                        psiField.getModifierList().hasModifierProperty("static")) {
                    continue;
                }
                String fieldType = psiField.getType().getPresentableText();
                String fullName = psiField.getType().getCanonicalText();
                String fieldName = psiField.getName();
                String remark = DesUtil.getLinkRemark(psiField, project);
                //基本类型
                if (NormalTypes.isBaseType(fullName)) {
                    ItemJsonSchema itemJsonSchema = SchemaHelper
                            .parse(NormalTypes.normalTypeMappings.get(fullName));
                    schema.addProperty(fieldName, itemJsonSchema.setDescription(remark));
                    if (itemJsonSchema instanceof IntegerSchema) {
                        IntegerSchema integerSchema = (IntegerSchema) itemJsonSchema;
                        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
                                .findAnnotation(psiField, ValidConstant.Min);
                        if (Objects.nonNull(psiAnnotation)) {
                            PsiAnnotationMemberValue value = psiAnnotation
                                    .findAttributeValue("value");
                            if (Objects.nonNull(value)) {
                                integerSchema.setMinimum(Long.valueOf(value.getText()));
                                System.out.println(value.getText());
                            }
                        }
                    }
                } else {
                    schema.addProperty(fieldName, new ObjectSchema().setDescription(remark));
                }
            }
//            result.set("type", "object");
//            result.set("required", "0");
//            result.set("title", psiType.getPresentableText());
//            result.set("description",
//                    psiType.getPresentableText().trim());
            String json = schema.toPrettyJson();
            return json;

        }
        return null;
    }

    public ItemJsonSchema getFieldSchema(PsiField psiField, Project project) {
        PsiType type = psiField.getType();
        String fullName = type.getCanonicalText();
        if (NormalTypes.isBaseType(fullName)) {
            SchemaType schemaType = NormalTypes.normalTypeMappings.get(fullName);
            return getBaseFieldSchema(schemaType, psiField, project);
        } else {
            return getOtherFieldSchema(psiField, project);
        }
    }


    public ItemJsonSchema getBaseFieldSchema(SchemaType schemaType, PsiField psiField,
            Project project) {
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
                if (NormalTypes.hasBaseRange(typePkName)) {
                    LongRange longRange = NormalTypes.baseRangeMappings.get(typePkName);
                    if (Objects.nonNull(longRange)) {
                        integerSchema.setRange(longRange);
                    }
                }
                LongRange longRange = ValidUtil.range(psiField);
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
                IntegerRange integerRange = ValidUtil.rangeLength(psiField);
                if (Objects.nonNull(integerRange)) {
                    stringSchema.setMinLength(integerRange.getMin());
                    stringSchema.setMaxLength(integerRange.getMax());
                }
                String pattern = ValidUtil.getPattern(psiField);
                if (!Strings.isNullOrEmpty(pattern)) {
                    stringSchema.setPattern(pattern);
                }
                result = stringSchema;
                break;
            case bool:
                result = new BooleanSchema();
                break;
            default:
                return null;
        }
        if (Objects.nonNull(result)) {
            result.setDescription(DesUtil.getLinkRemark(psiField, project));
            result.setDefault(NormalTypes.noramlTypesPackages.get(typePkName).toString());
        }
        return result;
    }

    public ItemJsonSchema getOtherFieldSchema(PsiField psiField, Project project) {
        String typePkName = psiField.getType().getCanonicalText();
        String[] types = typePkName.split("<");
        String type = types[0];
        ItemJsonSchema result;
        //如果是集合类型（List Set）
        if (NormalTypes.arrayTypeMappings.containsKey(type)) {
            ArraySchema arraySchema = new ArraySchema();
            if (type.contains("Set")) {
                arraySchema.setUniqueItems(true);
            }
            IntegerRange integerRange = ValidUtil.rangeSize(psiField);
            if (Objects.nonNull(integerRange)) {
                arraySchema.setMinItems(integerRange.getMin());
                arraySchema.setMaxItems(integerRange.getMax());
            }
            //如果有泛型
            if (types.length > 1) {
                String childrenType = types[1].split(">")[0];
                //如果泛型是基本类型
                if (NormalTypes.isBaseType(childrenType)) {
                    arraySchema.setItems(
                            SchemaHelper.parse(NormalTypes.normalTypeMappings.get(childrenType)));
                } else {
                    arraySchema.setItems(this.getObjectSchema(childrenType, project));
                }
            } else {
                //没有泛型 默认
                arraySchema.setItems(new ObjectSchema());
            }
            result = arraySchema;
        } else {
            //其他情况 object
            ItemJsonSchema objectSchema = this.getObjectSchema(typePkName, project);
            objectSchema.setDescription(DesUtil.getLinkRemark(psiField, project));
            result = objectSchema;
        }
        if (Objects.nonNull(result)) {
            result.setDescription(DesUtil.getLinkRemark(psiField, project));
        }
        return result;
    }

    public ItemJsonSchema getObjectSchema(String typePkName, Project project) {
        ObjectSchema objectSchema = new ObjectSchema();
        PsiClass psiClass = JavaPsiFacade.getInstance(project)
                .findClass(typePkName, GlobalSearchScope.allScope(project));
        for (PsiField field : psiClass.getAllFields()) {
            if (field.getModifierList().hasModifierProperty("final") ||
                    field.getModifierList().hasModifierProperty("static")) {
                continue;
            }
            String fieldName = field.getName();
            objectSchema.addProperty(fieldName, this.getFieldSchema(field, project));
            if(ValidUtil.notNull(field) || ValidUtil.notBlank(field)) {
                objectSchema.addRequired(fieldName);
            }
        }
        return objectSchema;
    }

    /**
     * @description: 获得属性列表
     * @param: [psiClass, project, childType, index]
     * @return: com.qdredsoft.plugin.ext.KV
     * @date: 2019/5/15
     */
    public KV getFields(PsiClass psiClass, Project project, String[] childType,
            Integer index,
            List<String> requiredList) {
        KV kv = KV.create();
        if (psiClass != null) {
            if (Objects.nonNull(psiClass.getSuperClass()) && Objects
                    .nonNull(NormalTypes.collectTypes.get(psiClass.getSuperClass().getName()))) {
                for (PsiField field : psiClass.getFields()) {
                    if (Objects
                            .nonNull(PsiAnnotationSearchUtil
                                    .findAnnotation(field, ValidConstant.NotNull)) ||
                            Objects
                                    .nonNull(PsiAnnotationSearchUtil
                                            .findAnnotation(field, ValidConstant.NotBlank))) {
                        requiredList.add(field.getName());
                    }
                    getField(field, project, kv, childType, index, psiClass.getName());
                }
            } else {
                //泛型类型
                if (NormalTypes.genericList.contains(psiClass.getName()) && childType != null
                        && childType.length > index) {
                    String child = childType[index].split(">")[0];
                    PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                            .findClass(child, GlobalSearchScope.allScope(project));
                    return getFields(psiClassChild, project, childType, index + 1, requiredList);
                } else {
                    for (PsiField field : psiClass.getAllFields()) {
                        if (Objects
                                .nonNull(PsiAnnotationSearchUtil
                                        .findAnnotation(field, ValidConstant.NotNull)) ||
                                Objects
                                        .nonNull(
                                                PsiAnnotationSearchUtil.findAnnotation(field,
                                                        ValidConstant.NotBlank))) {
                            requiredList.add(field.getName());
                        }
                        getField(field, project, kv, childType, index, psiClass.getName());
                    }
                }
            }
        }
        return kv;
    }

    /**
     * @description: 获得单个属性
     * @param: [field, project, kv, childType, index, pName]
     * @return: void
     * @date: 2019/5/15
     */
    public void getField(PsiField field, Project project, KV kv, String[] childType,
            Integer index, String pName) {
        if (field.getModifierList().hasModifierProperty("final") ||
                field.getModifierList().hasModifierProperty("static")) {
            return;
        }
        PsiType type = field.getType();
        String name = field.getName();
        String remark = "";
        if (field.getDocComment() != null) {
            //获得link 备注
            remark = DesUtil.getLinkRemark(field, project);
        }
        // 如果是基本类型
        if (type instanceof PsiPrimitiveType) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", type.getPresentableText());
            if (!Strings.isNullOrEmpty(remark)) {
                jsonObject.addProperty("description", remark);
            }
            kv.set(name, jsonObject);
        } else {
            //reference Type
            String fieldTypeName = type.getPresentableText();
            //normal Type
            if (NormalTypes.isNormalType(fieldTypeName)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", fieldTypeName);
                if (!Strings.isNullOrEmpty(remark)) {
                    jsonObject.addProperty("description", remark);
                }
                kv.set(name, jsonObject);
            } else if (!(type instanceof PsiArrayType) && ((PsiClassReferenceType) type).resolve()
                    .isEnum()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "enum");
                if (!Strings.isNullOrEmpty(remark)) {
                    jsonObject.addProperty("description", remark);
                }
                kv.set(name, jsonObject);
            } else if (NormalTypes.genericList.contains(fieldTypeName)) {
                if (childType != null) {
                    String child = childType[index].split(">")[0];
                    if (child.contains("java.util.List") || child.contains("java.util.Set") || child
                            .contains("java.util.HashSet")) {
                        index = index + 1;
                        PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                                .findClass(childType[index].split(">")[0],
                                        GlobalSearchScope.allScope(project));
                        getCollect(kv, psiClassChild.getName(), remark, psiClassChild, project,
                                name, pName,
                                childType, index + 1);
                    } else {
                        //class type
                        KV kv1 = new KV();
                        kv1.set(KV.by("type", "object"));
                        PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                                .findClass(child, GlobalSearchScope.allScope(project));
                        kv1.set(KV.by("description",
                                (Strings.isNullOrEmpty(remark) ? ("" + psiClassChild.getName()
                                        .trim())
                                        : remark + " ," + psiClassChild.getName().trim())));
                        if (!pName.equals(psiClassChild.getName())) {
                            List<String> requiredList = new ArrayList<>();
                            kv1.set(KV.by("properties",
                                    getFields(psiClassChild, project, childType, index + 1,
                                            requiredList)));
                            kv1.set("required", requiredList);
                        } else {
                            kv1.set(KV.by("type", pName));
                        }
                        kv.set(name, kv1);
                    }
                }
                //    getField()
            } else if (type instanceof PsiArrayType) {
                //array type
                PsiType deepType = type.getDeepComponentType();
                KV kvlist = new KV();
                String deepTypeName = deepType.getPresentableText();
                String cType = "";
                if (deepType instanceof PsiPrimitiveType) {
                    kvlist.set("type", type.getPresentableText());
                    if (!Strings.isNullOrEmpty(remark)) {
                        kvlist.set("description", remark);
                    }
                } else if (NormalTypes.isNormalType(deepTypeName)) {
                    kvlist.set("type", deepTypeName);
                    if (!Strings.isNullOrEmpty(remark)) {
                        kvlist.set("description", remark);
                    }
                } else {
                    kvlist.set(KV.by("type", "object"));
                    PsiClass psiClass = PsiUtil.resolveClassInType(deepType);
                    cType = psiClass.getName();
                    kvlist.set(KV.by("description",
                            (Strings.isNullOrEmpty(remark) ? ("" + psiClass.getName().trim())
                                    : remark + " ," + psiClass.getName().trim())));
                    if (!pName.equals(PsiUtil.resolveClassInType(deepType).getName())) {
                        List<String> requiredList = new ArrayList<>();
                        kvlist.set("properties",
                                getFields(psiClass, project, null, null, requiredList));
                        kvlist.set("required", requiredList);
                    } else {
                        kvlist.set(KV.by("type", pName));
                    }
                }
                KV kv1 = new KV();
                kv1.set(KV.by("type", "array"));
                kv1.set(KV.by("description", (remark + " :" + cType).trim()));
                kv1.set("items", kvlist);
                kv.set(name, kv1);
            } else if (fieldTypeName.startsWith("List") || fieldTypeName.startsWith("Set")
                    || fieldTypeName.startsWith("HashSet")) {
                //list type
                PsiType iterableType = PsiUtil.extractIterableTypeParameter(type, false);
                PsiClass iterableClass = PsiUtil.resolveClassInClassTypeOnly(iterableType);
                String classTypeName = iterableClass.getName();
                getCollect(kv, classTypeName, remark, iterableClass, project, name, pName,
                        childType,
                        index);
            } else if (fieldTypeName.startsWith("HashMap") || fieldTypeName.startsWith("Map")
                    || fieldTypeName.startsWith("LinkedHashMap")) {
                //HashMap or Map
                CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(700);
                        Notification warning = notificationGroup
                                .createNotification("Map Type Can not Change,So pass",
                                        NotificationType.WARNING);
                        Notifications.Bus.notify(warning, project);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                //class type
                KV kv1 = new KV();
                PsiClass psiClass = PsiUtil.resolveClassInType(type);
                kv1.set(KV.by("type", "object"));
                kv1.set(KV.by("description",
                        (Strings.isNullOrEmpty(remark) ? ("" + psiClass.getName().trim())
                                : (remark + " ," + psiClass.getName()).trim())));
                if (!pName.equals(((PsiClassReferenceType) type).getClassName())) {
                    List<String> requiredList = new ArrayList<>();
                    kv1.set(KV.by("properties",
                            getFields(PsiUtil.resolveClassInType(type), project, childType, index,
                                    requiredList)));
                    kv1.set("required", requiredList);
                } else {
                    kv1.set(KV.by("type", pName));
                }
                kv.set(name, kv1);
            }
        }
    }


    /**
     * @description: 获得集合
     * @param: [kv, classTypeName, remark, psiClass, project, name, pName]
     * @return: void
     * @date: 2019/5/15
     */
    public void getCollect(KV kv, String classTypeName, String remark, PsiClass psiClass,
            Project project, String name, String pName, String[] childType, Integer index) {
        KV kvlist = new KV();
        if (NormalTypes.isNormalType(classTypeName) || NormalTypes.collectTypes
                .containsKey(classTypeName)) {
            kvlist.set("type", classTypeName);
            if (!Strings.isNullOrEmpty(remark)) {
                kvlist.set("description", remark);
            }
        } else {
            kvlist.set(KV.by("type", "object"));
            kvlist.set(KV.by("description",
                    (Strings.isNullOrEmpty(remark) ? ("" + psiClass.getName().trim())
                            : remark + " ," + psiClass.getName().trim())));
            if (!pName.equals(psiClass.getName())) {
                List<String> requiredList = new ArrayList<>();
                kvlist.set("properties",
                        getFields(psiClass, project, childType, index, requiredList));
                kvlist.set("required", requiredList);
            } else {
                kvlist.set(KV.by("type", pName));
            }
        }
        KV kv1 = new KV();
        kv1.set(KV.by("type", "array"));
        kv1.set(KV
                .by("description", (Strings.isNullOrEmpty(remark) ? ("" + psiClass.getName().trim())
                        : remark + " ," + psiClass.getName().trim())));
        kv1.set("items", kvlist);
        kv.set(name, kv1);
    }


    /**
     * @description: 添加到文件路径列表
     * @param: [filePaths, psiClass]
     * @return: void
     * @date: 2019/5/6
     */
    public void addFilePaths(Set<String> filePaths, PsiClass psiClass) {
        try {
            filePaths.add(
                    ((PsiJavaFileImpl) psiClass.getContext()).getViewProvider().getVirtualFile()
                            .getPath());
        } catch (Exception e) {
            try {
                filePaths.add(
                        ((ClsFileImpl) psiClass.getContext()).getViewProvider().getVirtualFile()
                                .getPath());
            } catch (Exception e1) {
            }
        }
    }

    private String buildPath(StringBuilder path) {
        return buildPath(path.toString());
    }

    private String buildPath(String path) {
        final String split = "/";
        String pathStr = path.trim();
        return pathStr.startsWith(split) ? pathStr : (split + pathStr);
    }
}
