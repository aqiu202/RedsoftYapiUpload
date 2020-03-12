package com.redsoft.idea.plugin.yapi.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.redsoft.idea.plugin.yapi.xml.YApiProperty;
import com.redsoft.idea.plugin.yapi.xml.YApiPropertyConvertHolder;
import com.redsoft.idea.plugin.yapi.xml.YApiPropertyXmlConvert;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@State(name = "RedsoftYApiUpload")
public class YApiPersistentState implements PersistentStateComponent<Element> {

    private final YApiPropertyXmlConvert convert = YApiPropertyConvertHolder.getConvert();
    private YApiProperty yApiProperty;

    YApiPersistentState() {

    }

    public static YApiPersistentState getInstance(Project project) {
        return ServiceManager.getService(project, YApiPersistentState.class);
    }

    @NotNull
    @Override
    public Element getState() {
        return this.convert.serialize(yApiProperty == null ? new YApiProperty() : yApiProperty);
    }

    @Override
    public void loadState(@NotNull Element element) {
        this.yApiProperty = this.convert.deserialize(element);
    }

}
