package com.onceapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

import android.util.Log;
/**
 * Created by ctyloading on 16/4/15.
 */
public class NotificationManager {
    final static String PREFERENCES_KEY = "ReactNativeSystemNotification";
    public Context context = null;
    public SharedPreferences sharedPreferences = null;

    /**
     * Constructor.
     */
    public NotificationManager(Context context) {
        this.context = context;
        this.sharedPreferences = (SharedPreferences) context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Create a notification.
     */
    public Notification create(
            Integer notificationID,
            NotificationAttributes notificationAttributes
    ) {
        Notification notification = new Notification(context, notificationID, notificationAttributes);

        notification.create();

        return notification;
    }

    /**
     * Create or update (if exists) a notification.
     */
    public Notification createOrUpdate(
            Integer notificationID,
            NotificationAttributes notificationAttributes
    ) {
        if (getIDs().contains(notificationID)) {
            Notification notification = find(notificationID);

            notification.update(notificationAttributes);
            return notification;

        } else {
            return create(notificationID, notificationAttributes);
        }
    }

    /**
     * Get all notification ids.
     */
    public ArrayList<Integer> getIDs() {
        Set<String> keys = sharedPreferences.getAll().keySet();
        ArrayList<Integer> ids = new ArrayList<Integer>();

        for (String key : keys) {
            try {
                ids.add(Integer.parseInt(key));
                // TODO: Delete out-dated notifications BTW
            } catch (Exception e) {
                Log.e("ReactSystemNotification", "NotificationManager: getIDs Error: " + Log.getStackTraceString(e));
            }
        }

        return ids;
    }

    /**
     * Get a notification by its id.
     */
    public Notification find(Integer notificationID) {
        Notification notification = new Notification(context, notificationID, null);

        if (notification.getAttributes() == null) notification.loadAttributesFromPreferences();

        return notification;
    }

    /**
     * Delete a notification by its id.
     */
    public Notification delete(Integer notificationID) {
        return find(notificationID).delete();
    }

    /**
     * Clear a notification by its id.
     */
    public Notification clear(Integer notificationID) {
        return find(notificationID).clear();
    }

    /**
     * Clear all notifications.
     */
    public void clearAll() {
        android.app.NotificationManager systemNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        systemNotificationManager.cancelAll();
    }
}
