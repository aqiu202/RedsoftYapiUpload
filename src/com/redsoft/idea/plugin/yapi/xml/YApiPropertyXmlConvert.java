package com.redsoft.idea.plugin.yapi.xml;

import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

public interface YApiPropertyXmlConvert {

    Element serialize(YApiProjectProperty property);

    YApiProjectProperty deserialize(@NonNls Element element);
}
