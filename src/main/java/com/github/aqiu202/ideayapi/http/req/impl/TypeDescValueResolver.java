package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.http.req.YApiParamResolver;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 处理请求参数类型信息添加
 */
public class TypeDescValueResolver implements YApiParamResolver {
    @Override
    public void accept(YApiParam param) {
        if (CollectionUtils.isNotEmpty(param.getParams())) {
            param.getParams().stream().filter(p -> StringUtils.isNotBlank(p.getTypeDesc()))
                    .forEach(query -> query.setDesc(this.handleDescWithType(query)));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_body_form())) {
            param.getReq_body_form().stream().filter(p -> StringUtils.isNotBlank(p.getTypeDesc()))
                    .forEach(form -> form.setDesc(this.handleDescWithType(form)));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_params())) {
            param.getReq_params().stream().filter(p -> StringUtils.isNotBlank(p.getTypeDesc()))
                    .forEach(pathVariable -> pathVariable.setDesc(this.handleDescWithType(pathVariable)));
        }
    }

    private String handleDescWithType(ValueWrapper valueWrapper) {
        String desc;
        return (StringUtils.isBlank(desc = valueWrapper.getDesc()) ? "" : desc) + "(" + valueWrapper.getTypeDesc() + ")";
    }
}
