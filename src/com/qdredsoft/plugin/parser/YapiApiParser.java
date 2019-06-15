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
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiReference;
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
import com.qdredsoft.plugin.ext.KV;
import com.qdredsoft.plugin.model.ValueWraper;
import com.qdredsoft.plugin.model.YapiApiDTO;
import com.qdredsoft.plugin.model.YapiFormDTO;
import com.qdredsoft.plugin.model.YapiHeaderDTO;
import com.qdredsoft.plugin.model.YapiPathVariableDTO;
import com.qdredsoft.plugin.model.YapiQueryDTO;
import com.qdredsoft.plugin.util.DesUtil;
import com.qdredsoft.plugin.util.PsiAnnotationSearchUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author aqiu
 * @date 2019-06-15 11:46
 * @description 接口信息解析
 **/
public class YapiApiParser {

  private static NotificationGroup notificationGroup;

  static {
    notificationGroup = new NotificationGroup("Java2Json.NotificationGroup",
        NotificationDisplayType.BALLOON, true);
  }

  public List<YapiApiDTO> build(AnActionEvent e) {
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
                .createNotification("JSON转化失败." + ex.getMessage(), NotificationType.ERROR);
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
              .createNotification("JSON转化失败." + ex.getMessage(), NotificationType.ERROR);
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
   * 根据方法初步生成 YapiApiDTO （设置path,method,desc,menu等字段）
   */
  public static YapiApiDTO handleMethod(PsiClass selectedClass, PsiMethod psiMethodTarget,
      Project project) throws Exception {
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
      PsiNameValuePair[] psiNameValuePairs = psiAnnotation.getParameterList().getAttributes();
      if (psiNameValuePairs.length > 0) {
        if (psiNameValuePairs[0].getLiteralValue() != null) {
          path.append(buildPath(psiNameValuePairs[0].getLiteralValue()));
        } else {
          PsiAnnotationMemberValue psiAnnotationMemberValue = psiAnnotation
              .findAttributeValue("value");
          if (psiAnnotationMemberValue != null) {
            String[] results = psiAnnotationMemberValue.getReference().resolve().getText()
                .split("=");
            path.append(results[results.length - 1].split(";")[0].replace("\"", "").trim());
          }
        }
      }
    }
    //获取方法上的RequestMapping注解
    PsiAnnotation psiAnnotationMethod = PsiAnnotationSearchUtil
        .findAnnotation(psiMethodTarget, SpringMVCConstant.RequestMapping);
    if (psiAnnotationMethod != null) {
      PsiNameValuePair[] psiNameValuePairs = psiAnnotationMethod.getParameterList().getAttributes();
      if (psiNameValuePairs != null && psiNameValuePairs.length > 0) {
        for (PsiNameValuePair psiNameValuePair : psiNameValuePairs) {
          //获得方法上的路径
          if (Objects.isNull(psiNameValuePair.getName()) || "value"
              .equals(psiNameValuePair.getName())) {
            PsiReference psiReference = psiNameValuePair.getValue().getReference();
            if (psiReference == null) {
              path.append(buildPath(psiNameValuePair.getLiteralValue()));
            } else {
              buildReference(psiReference, path, yapiApiDTO);
              yapiApiDTO.setDesc(
                  "<pre><code>  " + psiReference.resolve().getText() + " </code></pre> <hr>");
            }
            yapiApiDTO.setPath(buildPath(path));
            // 判断是否为Get 请求
          } else if ("method".equals(psiNameValuePair.getName()) && psiNameValuePair.getValue()
              .toString().toUpperCase().contains("GET")) {
            yapiApiDTO.setMethod("GET");
            // 判断是否为Post 请求
          } else if ("method".equals(psiNameValuePair.getName()) && psiNameValuePair.getValue()
              .toString().toUpperCase().contains("POST")) {
            yapiApiDTO.setMethod("POST");
            // 判断是否为Put 请求
          } else if ("method".equals(psiNameValuePair.getName()) && psiNameValuePair.getValue()
              .toString().toUpperCase().contains("PUT")) {
            yapiApiDTO.setMethod("PUT");
            // 判断是否为DELETE 请求
          } else if ("method".equals(psiNameValuePair.getName()) && psiNameValuePair.getValue()
              .toString().toUpperCase().contains("DELETE")) {
            yapiApiDTO.setMethod("DELETE");
            // 判断是否为PATCH 请求
          } else if ("method".equals(psiNameValuePair.getName()) && psiNameValuePair.getValue()
              .toString().toUpperCase().contains("PATCH")) {
            yapiApiDTO.setMethod("PATCH");
          }
        }
      } else {
        yapiApiDTO.setPath(buildPath(path));
      }
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
                  .findAnnotation(psiMethodTarget, SpringMVCConstant.PatchMapping);
              if (psiAnnotationMethodSemple != null) {
                yapiApiDTO.setMethod("PATCH");
              }
            }
          }
        }
      }
      if (psiAnnotationMethodSemple != null) {
        PsiNameValuePair[] psiNameValuePairs = psiAnnotationMethodSemple.getParameterList()
            .getAttributes();
        if (psiNameValuePairs != null && psiNameValuePairs.length > 0) {
          for (PsiNameValuePair psiNameValuePair : psiNameValuePairs) {
            //获得方法上的路径
            if (Objects.isNull(psiNameValuePair.getName()) || psiNameValuePair.getName()
                .equals("value")) {
              PsiReference psiReference = psiNameValuePair.getValue().getReference();
              if (psiReference == null) {
                path.append(buildPath(psiNameValuePair.getLiteralValue()));
              } else {
                buildReference(psiReference, path, yapiApiDTO);
                if (!Strings.isNullOrEmpty(psiReference.resolve().getText())) {
                  String refernceDesc = psiReference.resolve().getText().replace("<", "&lt;")
                      .replace(">", "&gt;");
                  yapiApiDTO.setDesc("<pre><code>  " + refernceDesc + " </code></pre> <hr>");
                }
              }
              yapiApiDTO.setPath(buildPath(path));
            }
          }
        } else {
          yapiApiDTO.setPath(buildPath(path));
        }
      }
    }
    String classDesc = psiMethodTarget.getText().replace(
        Objects.nonNull(psiMethodTarget.getBody()) ? psiMethodTarget.getBody().getText() : "", "");
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

  private static void buildReference(PsiReference psiReference, StringBuilder path,
      YapiApiDTO yapiApiDTO) {
    String[] results = psiReference.resolve().getText().split("=");
    path.append(results[results.length - 1].split(";")[0].replace("\"", "").trim());
    String title = DesUtil.getUrlReFerenceRDesc(psiReference.resolve().getText())
        .replaceAll("\t", "").trim();
    yapiApiDTO.setTitle(title);
    yapiApiDTO.setMenu(DesUtil.getMenu(psiReference.resolve().getText()));
  }

  /**
   * @description: 获得请求参数
   * @param: [project, yapiApiDTO, psiMethodTarget]
   * @return: void
   * @date: 2019/2/19
   */
  public static void getRequest(Project project, YapiApiDTO yapiApiDTO, PsiMethod psiMethodTarget) {
    PsiParameter[] psiParameters = psiMethodTarget.getParameterList().getParameters();
    if (psiParameters.length > 0) {
      boolean hasRequestBody = hasRequestBody(psiParameters);
      String method = yapiApiDTO.getMethod();
      List list = new ArrayList<YapiQueryDTO>();
      List<YapiHeaderDTO> yapiHeaderDTOList = new ArrayList<>();
      List<YapiPathVariableDTO> yapiPathVariableDTOList = new ArrayList<>();
      for (PsiParameter psiParameter : psiParameters) {
        String desc = psiParameter.getType().getPresentableText();
        // request,response,session 参数跳过
        if (JavaConstant.HttpServletRequest.equals(psiParameter.getType().getCanonicalText())
            || JavaConstant.HttpServletResponse.equals(psiParameter.getType().getCanonicalText())
            || JavaConstant.HttpSession.equals(psiParameter.getType().getCanonicalText())) {
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
          String description = DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + "("
              + desc + ")";
          valueWraper.setDesc(description);
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
            YapiQueryDTO yapiQueryDTO = getRequestQuery(psiMethodTarget, psiParameter);
            list.add(yapiQueryDTO);
          } else if ("PUT".equals(method) || "POST".equals(method) || "PATCH".equals(method)) {
            //参数中含有@RequestBody注解，但是当前参数无@RequestBody注解，当作Query参数处理
            if (hasRequestBody) {
              psiAnnotation = PsiAnnotationSearchUtil
                  .findAnnotation(psiParameter, SpringMVCConstant.RequestBody);
              if (psiAnnotation == null) {
                YapiQueryDTO yapiQueryDTO = getRequestQuery(psiMethodTarget, psiParameter);
                list.add(yapiQueryDTO);
              } else {
                yapiApiDTO.setRequestBody(getResponse(project, psiParameter.getType()));
              }
            } else {//到这儿只能是form参数
              // 支持实体对象接收
              yapiApiDTO.setReq_body_type("form");
              if (yapiApiDTO.getReq_body_form() != null) {
                yapiApiDTO.getReq_body_form()
                    .addAll(getRequestForm(project, psiParameter, psiMethodTarget));
              } else {
                yapiApiDTO.setReq_body_form(getRequestForm(project, psiParameter, psiMethodTarget));
              }
            }
          }
        }
      }
      yapiApiDTO.setParams(list);
      yapiApiDTO.setHeader(yapiHeaderDTOList);
      yapiApiDTO.setReq_params(yapiPathVariableDTOList);
    }
  }

  private static boolean hasRequestBody(PsiParameter[] psiParameters) {
    for (PsiParameter psiParameter : psiParameters) {
      if (PsiAnnotationSearchUtil
          .findAnnotation(psiParameter, SpringMVCConstant.RequestBody) != null) {
        return true;
      }
    }
    return false;
  }

  private static YapiQueryDTO getRequestQuery(PsiMethod psiMethodTarget,
      PsiParameter psiParameter) {
    String desc = psiParameter.getType().getPresentableText();
    PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
        .findAnnotation(psiParameter, SpringMVCConstant.RequestParam);
    YapiQueryDTO yapiQueryDTO = new YapiQueryDTO();
    if (psiAnnotation != null) {
      PsiNameValuePair[] psiNameValuePairs = psiAnnotation.getParameterList()
          .getAttributes();
      if (psiNameValuePairs.length > 0) {
        ValueWraper valueWraper = handleParamAnnotation(psiAnnotation, psiParameter);
        yapiQueryDTO.full(valueWraper);
      }
    } else {//没有注解
      yapiQueryDTO.setRequired(notNullOrBlank(psiParameter) ? "1" : "0");
      yapiQueryDTO.setName(psiParameter.getName());
      yapiQueryDTO.setExample(NormalTypes.normalTypes.get(desc)
          .toString());
    }
    yapiQueryDTO.setDesc(DesUtil.getParamDesc(psiMethodTarget, psiParameter.getName()) + "("
        + desc + ")");
    return yapiQueryDTO;
  }


  /**
   * @description: 获得表单提交数据对象
   * @param: [requestClass]
   * @return: java.util.List<YapiFormDTO>
   * @date: 2019/5/17
   */
  public static List<YapiFormDTO> getRequestForm(Project project, PsiParameter psiParameter,
      PsiMethod psiMethodTarget) {
    List<YapiFormDTO> requestForm = new ArrayList<>();
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
      form.setRequired(required);
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
    } else {//没有@RequestBody注解
      PsiClass psiClass = JavaPsiFacade.getInstance(project)
          .findClass(typeClassName, GlobalSearchScope.allScope(project));
      for (PsiField field : psiClass.getAllFields()) {
        if (field.getModifierList().hasModifierProperty("final")) {
          continue;
        }
        YapiFormDTO form = new YapiFormDTO();
        form.setRequired(required);
        form.setName(field.getName());
        remark = DesUtil.getLinkRemark(DesUtil.getFiledDesc(field), project, field);
        form.setDesc(remark);
        if (Objects.nonNull(field.getType().getPresentableText())) {
          Object obj = NormalTypes.normalTypes.get(field.getType().getPresentableText());
          if (Objects.nonNull(obj)) {
            form.setExample(
                NormalTypes.normalTypes.get(field.getType().getPresentableText()).toString());
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
  private static ValueWraper handleParamAnnotation(PsiAnnotation psiAnnotation,
      PsiParameter psiParameter) {
    PsiNameValuePair[] psiNameValuePairs = psiAnnotation.getParameterList().getAttributes();
    ValueWraper valueWraper = new ValueWraper();
    if (psiNameValuePairs.length > 0) {
      for (PsiNameValuePair psiNameValuePair : psiNameValuePairs) {
        if ("name".equals(psiNameValuePair.getName()) || "value"
            .equals(psiNameValuePair.getName())) {
          valueWraper.setName(psiNameValuePair.getValue().getText().replace("\"", ""));
        } else if ("required".equals(psiNameValuePair.getName())) {
          valueWraper.setRequired(
              psiNameValuePair.getValue().getText().replace("\"", "")
                  .replace("false", "0")
                  .replace("true", "1"));
        } else if ("defaultValue".equals(psiNameValuePair.getName())) {
          valueWraper.setExample(psiNameValuePair.getValue().getText().replace("\"", ""));
        }
        if (Objects.isNull(valueWraper.getName())) {
          valueWraper.setName(psiNameValuePair.getLiteralValue());
        }
        if (Objects.isNull(valueWraper.getExample())) {
          valueWraper.setExample(
              NormalTypes.normalTypes.get(psiParameter.getType().getPresentableText()).toString());
        }
      }
    } else {//注解没有参数，生成默认描述
      valueWraper.setRequired("1");
      valueWraper.setName(psiParameter.getName());
      valueWraper.setExample(
          NormalTypes.normalTypes.get(psiParameter.getType().getPresentableText()).toString());
    }
    return valueWraper;
  }

  /**
   * @description: 获得响应参数
   * @param: [project, psiType]
   * @return: java.lang.String
   * @date: 2019/2/19
   */
  public static String getResponse(Project project, PsiType psiType) {
    return getPojoJson(project, psiType);
  }

  public static boolean notNullOrBlank(PsiParameter psiParameter) {
    PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil
        .findAnnotation(psiParameter, JavaConstant.NotNull);
    if (psiAnnotation == null) {
      psiAnnotation = PsiAnnotationSearchUtil
          .findAnnotation(psiParameter, JavaConstant.NotBlank);
    }
    return psiAnnotation != null;
  }


  public static String getPojoJson(Project project, PsiType psiType) {
    if (psiType instanceof PsiPrimitiveType) {
      //如果是基本类型
      KV kvClass = KV.create();
      kvClass.set(psiType.getCanonicalText(),
          NormalTypes.normalTypes.get(psiType.getPresentableText()));
    } else if (NormalTypes.isNormalType(psiType.getPresentableText())) {
      //如果是包装类型
      KV kvClass = KV.create();
      kvClass.set(psiType.getCanonicalText(),
          NormalTypes.normalTypes.get(psiType.getPresentableText()));
    } else if (psiType.getPresentableText().startsWith("List")) {
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
          if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild.getSuperClass()
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
          if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild.getSuperClass()
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
      String[] types = psiType.getCanonicalText().split("<");
      if (types.length > 1) {
        PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
            .findClass(types[0], GlobalSearchScope.allScope(project));
        KV result = new KV();
        List<String> requiredList = new ArrayList<>();
        KV kvObject = getFields(psiClassChild, project, types, 1, requiredList);
        result.set("type", "object");
        result.set("title", psiType.getPresentableText());
        result.set("required", requiredList);
        if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild.getSuperClass()
            .getName().toString().equals("Object")) {
        }
        result.set("description",
            (psiType.getPresentableText() + " :" + psiClassChild.getName()).trim());
        result.set("properties", kvObject);
        String json = result.toPrettyJson();
        return json;
      } else {
        PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
            .findClass(psiType.getCanonicalText(), GlobalSearchScope.allScope(project));
        KV result = new KV();
        List<String> requiredList = new ArrayList<>();
        KV kvObject = getFields(psiClassChild, project, null, null, requiredList);
        if (Objects.nonNull(psiClassChild.getSuperClass()) && !psiClassChild.getSuperClass()
            .getName().toString().equals("Object")) {
        }
        result.set("type", "object");
        result.set("required", requiredList);
        result.set("title", psiType.getPresentableText());
        result.set("description",
            (psiType.getPresentableText() + " :" + psiClassChild.getName()).trim());
        result.set("properties", kvObject);
        String json = result.toPrettyJson();
        return json;
      }
    }
    return null;
  }

  /**
   * @description: 获得属性列表
   * @param: [psiClass, project, childType, index]
   * @return: com.qdredsoft.plugin.ext.KV
   * @date: 2019/5/15
   */
  public static KV getFields(PsiClass psiClass, Project project, String[] childType, Integer index,
      List<String> requiredList) {
    KV kv = KV.create();
    if (psiClass != null) {
      if (Objects.nonNull(psiClass.getSuperClass()) && Objects
          .nonNull(NormalTypes.collectTypes.get(psiClass.getSuperClass().getName()))) {
        for (PsiField field : psiClass.getFields()) {
          if (Objects
              .nonNull(PsiAnnotationSearchUtil.findAnnotation(field, JavaConstant.NotNull)) ||
              Objects
                  .nonNull(PsiAnnotationSearchUtil.findAnnotation(field, JavaConstant.NotBlank))) {
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
                .nonNull(PsiAnnotationSearchUtil.findAnnotation(field, JavaConstant.NotNull)) ||
                Objects
                    .nonNull(
                        PsiAnnotationSearchUtil.findAnnotation(field, JavaConstant.NotBlank))) {
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
  public static void getField(PsiField field, Project project, KV kv, String[] childType,
      Integer index, String pName) {
    if (field.getModifierList().hasModifierProperty("final")) {
      return;
    }
    PsiType type = field.getType();
    String name = field.getName();
    String remark = "";
    if (field.getDocComment() != null) {
      remark = DesUtil.getFiledDesc(field);
      //获得link 备注
      remark = DesUtil.getLinkRemark(remark, project, field);
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
                .findClass(childType[index].split(">")[0], GlobalSearchScope.allScope(project));
            getCollect(kv, psiClassChild.getName(), remark, psiClassChild, project, name, pName,
                childType, index + 1);
          } else {
            //class type
            KV kv1 = new KV();
            kv1.set(KV.by("type", "object"));
            PsiClass psiClassChild = JavaPsiFacade.getInstance(project)
                .findClass(child, GlobalSearchScope.allScope(project));
            kv1.set(KV.by("description",
                (Strings.isNullOrEmpty(remark) ? ("" + psiClassChild.getName().trim())
                    : remark + " ," + psiClassChild.getName().trim())));
            if (!pName.equals(psiClassChild.getName())) {
              List<String> requiredList = new ArrayList<>();
              kv1.set(KV.by("properties",
                  getFields(psiClassChild, project, childType, index + 1, requiredList)));
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
            kvlist.set("properties", getFields(psiClass, project, null, null, requiredList));
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
        getCollect(kv, classTypeName, remark, iterableClass, project, name, pName, childType,
            index);
      } else if (fieldTypeName.startsWith("HashMap") || fieldTypeName.startsWith("Map")
          || fieldTypeName.startsWith("LinkedHashMap")) {
        //HashMap or Map
        CompletableFuture.runAsync(() -> {
          try {
            TimeUnit.MILLISECONDS.sleep(700);
            Notification warning = notificationGroup
                .createNotification("Map Type Can not Change,So pass", NotificationType.WARNING);
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
  public static void getCollect(KV kv, String classTypeName, String remark, PsiClass psiClass,
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
        kvlist.set("properties", getFields(psiClass, project, childType, index, requiredList));
        kvlist.set("required", requiredList);
      } else {
        kvlist.set(KV.by("type", pName));
      }
    }
    KV kv1 = new KV();
    kv1.set(KV.by("type", "array"));
    kv1.set(KV.by("description", (Strings.isNullOrEmpty(remark) ? ("" + psiClass.getName().trim())
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
  public static void addFilePaths(Set<String> filePaths, PsiClass psiClass) {
    try {
      filePaths.add(
          ((PsiJavaFileImpl) psiClass.getContext()).getViewProvider().getVirtualFile().getPath());
    } catch (Exception e) {
      try {
        filePaths.add(
            ((ClsFileImpl) psiClass.getContext()).getViewProvider().getVirtualFile().getPath());
      } catch (Exception e1) {
      }
    }
  }

  private static String buildPath(StringBuilder path) {
    return buildPath(path.toString());
  }

  private static String buildPath(String path) {
    final String split = "/";
    String pathStr = path.trim();
    return pathStr.startsWith(split) ? pathStr : (split + pathStr);
  }
}
