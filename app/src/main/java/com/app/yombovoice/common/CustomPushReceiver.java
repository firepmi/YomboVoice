package com.app.yombovoice.common;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.util.Log;

import com.app.yombovoice.Activities.InboxActivity;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;


public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        if (intent == null) return;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            String id = json.getString("post");
            checkID(context,id);
//            if (!isBackground) {
//                Intent resultIntent = new Intent(context, MainActivity.class);
//                showNotificationMessage(context, "Log Home Living", "New Photo Added!", resultIntent);
//            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }
    private void checkID(final Context context, String post){
        Intent resultIntent;
        if(post == null){
            resultIntent = new Intent(context, InboxActivity.class);
            showNotificationMessage(context, Globals.TITLE, "New Voice Message Received!", resultIntent);
            return;
        }
        Globals.selectedItemID = post;
        resultIntent = new Intent(context, InboxActivity.class);

        resultIntent.putExtra("post",post);

        showNotificationMessage(context, Globals.TITLE, "New Voice Message Received!", resultIntent);
    }

    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}