package com.github.aqiu202.ideayapi.config.xml;

import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

public interface YApiPropertyXmlConvert<T> {

    Element serialize(T property);

    T deserialize(@NonNls Element element);
}
