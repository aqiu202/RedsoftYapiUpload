package com.redsoft.idea.plugin.yapi.xml;

public class YApiPropertyConvertHolder {

    private static class InnerClass {

        private static YApiPropertyXmlConvert CONVERT = new DefaultYApiPropertyXmlConvert();
    }

    private YApiPropertyConvertHolder() {
    }

    public static YApiPropertyXmlConvert getConvert() {
        return InnerClass.CONVERT;
    }
}
