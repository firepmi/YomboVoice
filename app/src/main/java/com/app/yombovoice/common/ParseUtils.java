package com.app.yombovoice.common;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ParseUtils {

    private static String TAG = ParseUtils.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(Globals.APPLICATION_ID) || TextUtils.isEmpty(Globals.CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, Globals.APPLICATION_ID, Globals.CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    ParseInstallation p = ParseInstallation.getCurrentInstallation();
                    Boolean b =  p.getBoolean("notification");
                    if( b ) p.put("notification",true);
                    else p.put("notification",false);
                    p.saveInBackground();
                }
            }
        });

        ParsePush.subscribeInBackground(Globals.CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");
            }
        });
    }

    public static void subscribeWithName(String name) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("username", name);

        installation.saveInBackground();
    }
}