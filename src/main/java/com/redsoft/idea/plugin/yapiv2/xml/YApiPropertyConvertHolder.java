package com.redsoft.idea.plugin.yapiv2.xml;

public class YApiPropertyConvertHolder {

    private static class InnerClass {

        private static YApiPropertyXmlConvert<YApiProjectProperty> CONVERT = new DefaultYApiPropertyXmlConvert();
        private static YApiPropertyXmlConvert<YApiApplicationProperty> APP_CONVERT = new DefaultYApiApplicationPropertyXmlConvert();

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
