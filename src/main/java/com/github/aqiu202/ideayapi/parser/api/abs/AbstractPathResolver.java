package com.github.aqiu202.ideayapi.parser.api.abs;

import com.github.aqiu202.ideayapi.parser.api.BasePathResolver;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * <b>根据SpringMVC注解解析路由</b>
 *
 * @author aqiu 2020/7/23 3:44 下午
 **/
public abstract class AbstractPathResolver implements BasePathResolver {

    protected static final Set<String> empty = Collections.singleton("");

    @Override
    public Set<String> getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation) {
        String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
        if (value == null || "{}".equals(value)) {
            value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "path");
        }
        return (value == null || "{}".equals(value)) ? empty : this.processPath(value);
    }

    protected Set<String> processPath(@NotNull String pathString) {
        pathString = pathString.replace(" ", "");
        //如果是多个
        if (pathString.startsWith("{")) {
            //去除两边的大括号
            pathString = pathString.substring(1, pathString.length() - 1);
            //逗号结尾表示最后一个元素为空，舍弃
            if (pathString.endsWith(",")) {
                pathString = pathString.substring(0, pathString.length() - 1);
            }
            //去掉双引号
            pathString = pathString.replace("\"", "");
            //使用有序的LinkedHashSet放置所有的path字符串
            Set<String> result = new LinkedHashSet<>(Arrays.asList(pathString.split(",")));
            //最后一位是逗号表示最后还有一个元素为空字符串，split函数会自动过滤掉，手动添加
            if (pathString.endsWith(",")) {
                result.add("");
            }
            return result;
        } else {
            //返回单个的集合
            return Collections.singleton(pathString.replace("\"", ""));
        }
    }

}
