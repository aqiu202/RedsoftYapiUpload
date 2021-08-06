package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.json5.Json;
import com.redsoft.idea.plugin.yapiv2.json5.JsonArray;
import com.redsoft.idea.plugin.yapiv2.json5.JsonItem;
import com.redsoft.idea.plugin.yapiv2.json5.JsonObject;
import com.redsoft.idea.plugin.yapiv2.model.FieldValueWrapper;
import com.redsoft.idea.plugin.yapiv2.parser.Json5JsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.Jsonable;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectRawParser;
import com.redsoft.idea.plugin.yapiv2.parser.abs.AbstractJsonParser;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Collection;

/**
 * <b>json5解析器默认实现</b>
 *
 * @author aqiu 2020/7/24 9:56 上午
 **/
public class Json5ParserImpl extends AbstractJsonParser implements Json5JsonParser,
        ObjectRawParser {

    private final boolean needDesc;

    public Json5ParserImpl(YApiProjectProperty property, Project project) {
        this(property, project, true);
    }

    public Json5ParserImpl(YApiProjectProperty property, Project project, boolean needDesc) {
        super(property, project);
        this.needDesc = needDesc;
    }

    public Json5ParserImpl(Project project) {
        this(project, true);
    }

    public Json5ParserImpl(Project project, boolean needDesc) {
        super(project);
        this.needDesc = needDesc;
    }

    @Override
    public Json<?> parseJson5(String typePkName) {
        return (Json<?>) super.parse(typePkName);
    }

    @Override
    public Jsonable parseBasic(String typePkName) {
        return new Json<>(TypeUtils.getDefaultValueByPackageName(typePkName));
    }

    @Override
    public JsonObject parseMap(String typePkName) {
        return new JsonObject(new JsonItem<>("key", new Json<>("value"), "map"));
    }

    @Override
    public JsonArray<?> parseCollection(String typePkName) {
        if (Strings.isBlank(typePkName)) {
            return new JsonArray<>();
        }
        return new JsonArray<>(this.parseJson5(typePkName));
    }

    @Override
    public String getRawResponse(PsiType psiType) {
        return this.parse(psiType.getCanonicalText()).toJson();
    }

    @Override
    protected boolean needDescription() {
        return this.needDesc;
    }

    @Override
    public Jsonable buildPojo(Collection<FieldValueWrapper> wrappers) {
        JsonObject jsonObject = new JsonObject();
        for (FieldValueWrapper wrapper : wrappers) {
            final Jsonable value = wrapper.getValue();
            if (value instanceof Json) {
                jsonObject.addItem(new JsonItem<>(wrapper.getFieldName(), (Json<?>) value,
                        wrapper.getDescription()));
            }
        }
        return jsonObject;
    }
}
