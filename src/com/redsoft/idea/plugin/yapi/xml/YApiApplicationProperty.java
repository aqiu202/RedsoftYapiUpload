package com.redsoft.idea.plugin.yapi.xml;

import java.util.Objects;

public class YApiApplicationProperty {

    private String version = "1.3.7";

    public YApiApplicationProperty() {

    }

    @Override
    public int hashCode() {
        return Objects
                .hash(this.version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        YApiApplicationProperty that = (YApiApplicationProperty) obj;
        return Objects.equals(version, that.version);
    }
}
