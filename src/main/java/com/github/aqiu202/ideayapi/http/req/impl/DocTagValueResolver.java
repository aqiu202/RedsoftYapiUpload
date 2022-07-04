package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.http.req.YApiParamResolver;
import com.github.aqiu202.ideayapi.http.res.DocTagValueHandler;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.model.YApiStatus;
import com.github.aqiu202.ideayapi.util.CollectionUtils;

/**
 * 处理带有格式的注释信息
 */
public class DocTagValueResolver implements YApiParamResolver, DocTagValueHandler {
    @Override
    public void accept(YApiParam param) {
        param.setTitle(this.handleDocTagValue(param.getTitle()));
        param.setMenu(this.handleDocTagValue(param.getMenu()));
        param.setMenuDesc(this.handleDocTagValue(param.getMenuDesc()));
        param.setStatus(YApiStatus.getStatus(this.handleDocTagValue(param.getStatus())));
        if (CollectionUtils.isNotEmpty(param.getParams())) {
            param.getParams()
                    .forEach(query -> query.setDesc(this.handleDocTagValue(query.getDesc())));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_body_form())) {
            param.getReq_body_form()
                    .forEach(form -> form.setDesc(this.handleDocTagValue(form.getDesc())));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_params())) {
            param.getReq_params().forEach(pathVariable -> pathVariable
                    .setDesc(this.handleDocTagValue(pathVariable.getDesc())));
        }
    }
}
