package com.redsoft.idea.plugin.yapiv2.model;

import java.io.Serializable;

/**
 * query 参数
 *
 * @author aqiu 2019/2/11 5:05 PM
 */
@SuppressWarnings("unused")
public class YApiQuery extends ValueWrapper implements Serializable {

    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public YApiQuery() {
    }
}
