package com.github.aqiu202.ideayapi.config.xml;

import java.util.Objects;

public class YApiApplicationProperty {

    private String version = "1.0.0";

    public YApiApplicationProperty() {

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
