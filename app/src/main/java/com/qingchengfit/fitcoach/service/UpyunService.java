package com.qingchengfit.fitcoach.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.http.UpYunClient;

import java.io.File;
import java.util.UUID;

import cn.qingchengfit.widgets.utils.LogUtil;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/7 2015.
 */
public class UpyunService extends IntentService {
    public static final String UPYUNPATH = "http://zoneke-img.b0.upaiyun.com/";
    private static final String FILE_PATH = "upload_filepath";

    public static void uploadPic(Context context, String filePaht) {
        LogUtil.e("uploadpic");
        Intent it = new Intent(context, UpyunService.class);
        it.putExtra(FILE_PATH, filePaht);
        context.startService(it);
    }

    public UpyunService() {
        super("upyunservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.e("handle intent");
        if (intent.getExtras() != null) {
            String filepath = intent.getExtras().getString(FILE_PATH);
            if (!TextUtils.isEmpty(filepath)) {
                File file = new File(filepath);
                String filename = UUID.randomUUID().toString();
                boolean issuccees = UpYunClient.upLoadImg("/header/", filename, file);
                if (issuccees) {
                    //图片上传成功
                    LogUtil.e("chengogn");
                    RxBus.getBus().post(new UpYunResult(UPYUNPATH + "/header/" + filename + ".png"));
                } else {
                    //图片上传失败
                    ToastUtils.show(R.drawable.ic_share_fail, "图片上传失败!");
                }

            }
        }
    }

    public class UpYunResult {

        private String url;

        public UpYunResult(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
