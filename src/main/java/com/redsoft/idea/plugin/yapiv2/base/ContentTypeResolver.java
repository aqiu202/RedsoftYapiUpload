package com.redsoft.idea.plugin.yapiv2.base;

/**
 * <b>HTTP Content-Type Header解析器</b>
 * <p>包括 request实现{@link com.redsoft.idea.plugin.yapiv2.req.impl.RequestContentTypeResolverImpl} 和
 * response实现{@link com.redsoft.idea.plugin.yapiv2.res.impl.ResponseContentTypeResolverImpl}</p>
 * @author aqiu
 * @date 2020/7/23 3:53 下午
**/
public interface ContentTypeResolver extends BaseResolver {

    String JSON = "application/json";
    String FORM = "application/x-www-form-urlencoded";
    String RAW = "text/plain";
    String JSON_VALUE = "json";
    String FORM_VALUE = "form";
    String RAW_VALUE = "raw";

}
