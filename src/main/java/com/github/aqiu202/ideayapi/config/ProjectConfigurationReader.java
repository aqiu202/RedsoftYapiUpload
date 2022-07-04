package com.github.aqiu202.ideayapi.config;

import com.intellij.openapi.project.Project;

public interface ProjectConfigurationReader<T> {

    T read(Project project);
}
