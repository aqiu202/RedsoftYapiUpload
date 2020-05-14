package com.redsoft.idea.plugin.yapiv2.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import java.util.Objects;

public final class ProjectHolder {

    private ProjectHolder() {
    }

    private static Project project;

    public static void setCurrentProject(Project project) {
        ProjectHolder.project = project;
    }

    public static Project getCurrentProject() {
        return Objects.isNull(ProjectHolder.project) ? ProjectManager.getInstance()
                .getDefaultProject() :
                ProjectHolder.project;
    }
}
