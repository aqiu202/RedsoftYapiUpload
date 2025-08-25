package com.github.aqiu202.ideayapi.mode.json5;

import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.github.aqiu202.ideayapi.util.StringUtils;

public class Json<T> implements Jsonable {

    public final static String INTENT = "  ";

    protected T value;

    public Json() {
        this(null);
    }

    public Json(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String toString(String description) {
        return this.toString(description, 0);
    }

    public String toString(int level) {
        return this.toString(null, level);
    }

    public String toString(CommentMode commentMode) {
        return this.toString(null, 0, commentMode);
    }

    public String toString(int level, CommentMode commentMode) {
        return this.toString(null, level, commentMode);
    }

    public String toString(String description, CommentMode commentMode) {
        return this.toString(description, 0, commentMode);
    }

    public String toString(String description, int level) {
        return this.toString(description, level, CommentMode.SINGLE);
    }

    public String toString(String description, int level, CommentMode commentMode) {
        String vs = value instanceof Json<?> ? ((Json<?>) this.value).toString(level, commentMode) : this.wrapValue(value);
        vs += (", " + this.buildCommentString(description, commentMode));
        return vs;
    }

    protected String buildCommentString(String description, CommentMode commentMode) {
        if (StringUtils.isBlank(description) || commentMode == null) {
            return "";
        }
        return String.format(commentMode.getTemplate(), description);
    }

    protected String wrapValue(Object value) {
        return "\"" + value + "\"";
    }

    protected String intent(int level) {
        if (level <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            builder.append(INTENT);
        }
        return builder.toString();
    }

    @Override
    public String toJson() {
        return this.toString();
    }
}
