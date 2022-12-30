package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;

public class NotificationUtils {

    public static Notification createNotification(String subTitle, String content, NotificationType notificationType) {
        return new Notification(YApiConstants.name, YApiConstants.name, content, notificationType).setSubtitle(subTitle);
    }

}
