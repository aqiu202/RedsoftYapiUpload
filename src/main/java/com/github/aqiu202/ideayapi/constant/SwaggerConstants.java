package com.github.aqiu202.ideayapi.constant;

/**
 * <b>swagger相关注解路径</b>
 *
 * @author aqiu 2020/3/27 10:10 下午
 **/
public interface SwaggerConstants {

    String API = "io.swagger.annotations.Api";
    String API_OPERATION = "io.swagger.annotations.ApiOperation";
    String API_OPERATION_V3 = "io.swagger.v3.oas.annotations.Operation";

    String API_MODEL = "io.swagger.annotations.ApiModel";
    String API_MODEL_PROPERTY = "io.swagger.annotations.ApiModelProperty";

    String[] API_PARAM = new String[] {"io.swagger.annotations.ApiParam","io.swagger.v3.oas.annotations.Parameter"};
}