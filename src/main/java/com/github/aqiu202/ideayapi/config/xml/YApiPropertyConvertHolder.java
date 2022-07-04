package com.github.aqiu202.ideayapi.config.xml;

public class YApiPropertyConvertHolder {

    private static class InnerClass {

        private static final YApiPropertyXmlConvert<YApiProjectProperty> CONVERT = new DefaultYApiPropertyXmlConvert();
        private static final YApiPropertyXmlConvert<YApiApplicationProperty> APP_CONVERT = new DefaultYApiApplicationPropertyXmlConvert();

    }

    private YApiPropertyConvertHolder() {
    }

    public static YApiPropertyXmlConvert<YApiProjectProperty> getConvert() {
        return InnerClass.CONVERT;
    }

    public static YApiPropertyXmlConvert<YApiApplicationProperty> getApplicationConvert() {
        return InnerClass.APP_CONVERT;
    }
}
