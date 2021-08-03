package com.redsoft.idea.plugin.yapiv2.constant;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;

public interface NotificationConstants {

    NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(YApiConstants.name,
            NotificationDisplayType.BALLOON, true);

    NotificationGroup NOTIFICATION_GROUP_WINDOW = new NotificationGroup(YApiConstants.name,
            NotificationDisplayType.TOOL_WINDOW, true);
}
