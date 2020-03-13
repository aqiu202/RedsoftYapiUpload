package com.redsoft.idea.plugin.yapi.schema.base;

import java.util.List;

@SuppressWarnings("unused")
public interface Enumable {

    List<String> getEnum();

    String getEnumDesc();
}
