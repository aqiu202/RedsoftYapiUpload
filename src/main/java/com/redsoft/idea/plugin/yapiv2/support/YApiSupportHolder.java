package com.redsoft.idea.plugin.yapiv2.support;

import com.redsoft.idea.plugin.yapiv2.support.swagger.YApiSwaggerSupport;

/**
 * 当前支持的所有扩展
 */
public interface YApiSupportHolder {

    YApiSupport supports = new YApiSupports(YApiSwaggerSupport.INSTANCE);
}
