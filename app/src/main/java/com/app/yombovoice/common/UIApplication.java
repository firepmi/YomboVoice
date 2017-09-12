package com.app.yombovoice.common;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.ParseObject;

public class UIApplication extends Application {
    private static UIApplication mInstance;
    public UIApplication() {
        super();
    }

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
        initImageLoader(getApplicationContext());

        ParseUtils.registerParse(this);
        ParseObject.registerSubclass(VoiceMessage.class);
    }
    public static synchronized UIApplication getInstance() {
        return mInstance;
    }
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}