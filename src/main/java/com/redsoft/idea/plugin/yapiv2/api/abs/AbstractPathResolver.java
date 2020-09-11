package com.redsoft.idea.plugin.yapiv2.api.abs;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.redsoft.idea.plugin.yapiv2.api.BasePathResolver;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * <b>根据SpringMVC注解解析路由</b>
 * @author aqiu
 * @date 2020/7/23 3:44 下午
 **/
public abstract class AbstractPathResolver implements BasePathResolver {

    protected final Set<String> empty = Collections.singleton("");

    @Override
    public Set<String> getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation) {
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("value");
        if (element == null) {
            return this.empty;
        }
        String value = element.getText().replace(" ", "");
        if ("{}".equals(value)) {
            value = Objects.requireNonNull(psiAnnotation.findAttributeValue("path")).getText().replace(" ", "");
        }
        return "{}".equals(value) ? this.empty : this.processPath(value);
    }

    protected Set<String> processPath(@NotNull String pathString) {
        //如果是多个
        if (pathString.startsWith("{")) {
            //去除两边的大括号
            pathString = pathString.substring(1, pathString.length() - 1);
            //逗号结尾表示最后一个元素为空，舍弃
            if(pathString.endsWith(",")) {
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
