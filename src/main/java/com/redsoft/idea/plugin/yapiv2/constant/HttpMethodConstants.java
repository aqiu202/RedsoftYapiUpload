package com.redsoft.idea.plugin.yapiv2.constant;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * http 请求方式参量
 *
 * @author aqiu
 * @date 2020/3/27 5:22 PM
 */
public interface HttpMethodConstants {

    String GET = "GET";

    String POST = "POST";

    String PUT = "PUT";

    String DELETE = "DELETE";

    String PATCH = "PATCH";

    Set<String> ALL = new LinkedHashSet<>(Arrays.asList(GET, POST, PUT, DELETE, PATCH));
}
