package com.redsoft.idea.plugin.yapi.constant;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;

public interface NotificationConstants {

    NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Redsoft YApi Upload",
            NotificationDisplayType.BALLOON, true);
}
