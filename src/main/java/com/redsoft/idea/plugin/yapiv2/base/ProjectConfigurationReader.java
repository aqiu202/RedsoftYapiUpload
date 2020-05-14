package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.openapi.project.Project;

public interface ProjectConfigurationReader<T> {

    T read(Project project);
}
