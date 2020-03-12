package com.redsoft.idea.plugin.yapi.xml;

import org.jdom.Element;

public interface YApiPropertyXmlConvert {

    Element serialize(YApiProperty property);

    YApiProperty deserialize(Element element);
}
