package com.redsoft.idea.plugin.yapiv2;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.redsoft.idea.plugin.yapiv2.constant.TypeConstants;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;

public class Test {

    private final Project project = ProjectManager.getInstance().getDefaultProject();

    private void code(String typePkName) {
        //是否是数组
        boolean isArray = typePkName.endsWith("[]");
        int s = typePkName.indexOf("<");
        String type;
        String subType = null;
        if (s != -1) {
            type = typePkName.substring(0, s);
            //截取子类型
            subType = typePkName.substring(s + 1, typePkName.lastIndexOf(">"));
        } else {
            type = typePkName;
        }
        Object result = null;
        //判断type如果是基本类型
        if (TypeConstants.isBasicType(type)) {
//			result = func1(type);
        } else if (PsiUtils.isMap(this.project, type)) { //如果是Map不解析
//			result = func2(type);
        } else if (PsiUtils.isCollection(this.project, type)) { //如果是集合类型，解析第一个泛型参数作为自己的子元素类型
//			result = func3(type);
        } else {//自定义POJO类型
            //没有泛型
            if (Strings.isNullOrEmpty(subType)) {
//				result = func4(type);
            } else {//有泛型
//				result = func5(type, subType);
            }
        }
    }
}
