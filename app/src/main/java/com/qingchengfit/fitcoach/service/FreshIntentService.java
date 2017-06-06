package com.qingchengfit.fitcoach.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FreshIntentService extends IntentService {
    private static final String ACTION_REFRESH_GYMS = "com.qingchengfit.fitcoach.service.action.refresh.gym";

    public FreshIntentService() {
        super("FreshIntentService");
    }

    /**
     * 刷新场馆
     */
    public static void startFreshGyms(Context context) {
        Intent intent = new Intent(context, FreshIntentService.class);
        intent.setAction(ACTION_REFRESH_GYMS);
        context.startService(intent);
    }

    @Override protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REFRESH_GYMS.equals(action)) {
                handleRefreshGyms();
            }
        }
    }

    /**
     * 刷新场馆
     */
    private void handleRefreshGyms() {
    }
}
