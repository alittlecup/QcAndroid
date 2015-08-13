package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.LogUtil;

import java.io.File;
import java.io.IOException;

import main.java.com.UpYun;

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
 * Created by Paper on 15/8/13 2015.
 */
public class UpYunClient {

    public static UpYun init() {
        UpYun upyun = new UpYun("zoneke-img", "qcandroid", "07279e9e81c661b259f93a457d6491af");
        upyun.setDebug(true);
        upyun.setTimeout(60);
        upyun.setApiDomain(UpYun.ED_AUTO);
        return upyun;
    }

    public static boolean upLoadImg(String userid, File file) {
        UpYun upYun = init();
        boolean ret = false;
        try {
            upYun.setContentMD5(UpYun.md5(file));
            ret = upYun.writeFile("/header/" + userid + "/", file, true);
        } catch (IOException e) {
            LogUtil.e("Upyun", "upload headerimg err:" + e.getMessage());
        }
        return ret;
    }


}