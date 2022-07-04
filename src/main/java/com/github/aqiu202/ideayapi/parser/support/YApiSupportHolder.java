package com.github.aqiu202.ideayapi.parser.support;

import com.github.aqiu202.ideayapi.parser.support.jackson.YApiJacksonSupport;
import com.github.aqiu202.ideayapi.parser.support.spring.YApiSpringSupport;
import com.github.aqiu202.ideayapi.parser.support.swagger.YApiSwaggerSupport;

/**
 * 当前支持的所有扩展
 */
public interface YApiSupportHolder {

    YApiSupport supports = new YApiSupports(
            YApiSpringSupport.INSTANCE,
            YApiJacksonSupport.INSTANCE,
            YApiSwaggerSupport.INSTANCE);
}
