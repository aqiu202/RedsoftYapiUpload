package com.github.aqiu202.ideayapi.parser.base;

import com.github.aqiu202.ideayapi.http.req.impl.RequestContentTypeResolverImpl;
import com.github.aqiu202.ideayapi.http.res.impl.ResponseContentTypeResolverImpl;

/**
 * <b>HTTP Content-Type Header解析器</b>
 * <p>包括 request实现{@link RequestContentTypeResolverImpl} 和
 * response实现{@link ResponseContentTypeResolverImpl}</p>
 *
 * @author aqiu 2020/7/23 3:53 下午
 **/
public interface ContentTypeResolver extends BaseResolver {

    String JSON = "application/json";
    String FORM = "application/x-www-form-urlencoded";
    String RAW = "text/plain";
    String JSON_VALUE = "json";
    String FORM_VALUE = "form";
    String RAW_VALUE = "raw";

}
