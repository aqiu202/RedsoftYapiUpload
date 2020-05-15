package com.redsoft.idea.plugin.yapiv2.config;

import com.intellij.openapi.project.Project;

public interface ProjectConfigurationReader<T> {

    T read(Project project);
}
