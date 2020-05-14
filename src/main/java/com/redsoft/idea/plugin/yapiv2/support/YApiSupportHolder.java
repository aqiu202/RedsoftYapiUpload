package com.redsoft.idea.plugin.yapiv2.support;

import com.redsoft.idea.plugin.yapiv2.support.swagger.YApiSwaggerSupport;

public interface YApiSupportHolder {

    YApiSupport supports = new YApiSupports(YApiSwaggerSupport.INSTANCE);
}
