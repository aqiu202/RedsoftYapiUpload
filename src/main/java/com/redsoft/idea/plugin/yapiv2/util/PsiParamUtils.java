package com.redsoft.idea.plugin.yapiv2.util;

import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.constant.HttpMethodConstants;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import java.util.Collection;

public final class PsiParamUtils {

    private PsiParamUtils() {
    }

    /**
     * @author aqiu
     * @date 2019-07-03 10:02
     * @description 参数是否含有@RequestBody注解
     * @return {@link boolean}
     **/
    public static boolean hasRequestBody(PsiParameter... psiParameters) {
        for (PsiParameter psiParameter : psiParameters) {
            if (PsiAnnotationUtils.isAnnotatedWith(psiParameter, SpringMVCConstants.RequestBody)) {
                return true;
            }
        }
        return false;
    }

    public static boolean noBody(String method) {
        return HttpMethodConstants.GET.equals(method)
//                || HttpMethodConstants.DELETE.equals(method) //YApi默认Delete方法有body
                ;
    }

    public static boolean noBody(Collection<String> methods) {
        return methods.stream().anyMatch(PsiParamUtils::noBody)
//        return HttpMethodConstants.GET.equals(method)
//                || HttpMethodConstants.DELETE.equals(method) //YApi默认Delete方法有body
                ;
    }

}
