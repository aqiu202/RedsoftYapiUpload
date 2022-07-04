package com.github.aqiu202.ideayapi.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * form类型参数
 *
 * @author aqiu 2019/2/11 5:05 PM
 */
@SuppressWarnings("unused")
public class YApiForm extends ValueWrapper implements Serializable {

    private String _id;

    /**
     * 类型 text or file
     */
    private String type = "text";

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YApiForm() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YApiForm that = (YApiForm) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
