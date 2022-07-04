package com.github.aqiu202.ideayapi.constant;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;

public interface NotificationConstants {

    NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(YApiConstants.name,
            NotificationDisplayType.BALLOON, true);

    NotificationGroup NOTIFICATION_GROUP_WINDOW = new NotificationGroup(YApiConstants.name,
            NotificationDisplayType.TOOL_WINDOW, true);
}
