package cn.qingchengfit.staffkit.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import cn.qingchengfit.model.responese.UpdateVersion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.AppUtils;
import com.google.gson.Gson;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class DownloadIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPGRADE = "cn.qingchengfit.staffkit.service.action.FOO";

    private static final String EXTRA_URL = "cn.qingchengfit.staffkit.service.extra.PARAM1";

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(ACTION_UPGRADE);
        intent.putExtra(EXTRA_URL, param1);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    @Override protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_UPGRADE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_URL);
                handleActionFoo(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1) {
        FIR.checkForUpdateInFIR(getString(R.string.fir_token_release), new VersionCheckCallback() {
            @Override public void onSuccess(String s) {
                final UpdateVersion updateVersion = new Gson().fromJson(s, UpdateVersion.class);
                if (updateVersion.version > AppUtils.getAppVerCode(App.context)) {
                    Request request = new Request.Builder().url(updateVersion.direct_install_url).build();
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                    Call call = okHttpClient.newCall(request);
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = call.execute().body().byteStream();
                        File file = new File(getExternalCacheDir(), "trainer" + updateVersion.updated_at + ".apk");
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径

                    } catch (IOException e) {

                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
    }
}
