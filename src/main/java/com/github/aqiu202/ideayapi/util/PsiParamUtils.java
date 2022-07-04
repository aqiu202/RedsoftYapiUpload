package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.HttpMethodConstants;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.intellij.psi.PsiParameter;

import java.util.Collection;

/**
 * Psi参数解析工具类
 */
public final class PsiParamUtils {

    private PsiParamUtils() {
    }

    /**
     * 参数是否含有@RequestBody注解
     *
     * @return {@link boolean}
     * @author aqiu 2019-07-03 10:02
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
