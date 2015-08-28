package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.RevenUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public static final String UPYUNPATH = "http://zoneke-img.b0.upaiyun.com/";

    public static UpYun init() {
        UpYun upyun = new UpYun("zoneke-img", "qcandroid", "07279e9e81c661b259f93a457d6491af");
        upyun.setDebug(true);
        upyun.setTimeout(60);
        upyun.setApiDomain(UpYun.ED_AUTO);
        return upyun;
    }

    /**
     * @param path   记得带分隔符 eg /header/
     * @param userid 云上存储的文件名
     * @param file
     * @return
     */
    public static boolean upLoadImg(String path, String userid, File file) {
        UpYun upYun = init();
        boolean ret = false;
        try {
            upYun.setContentMD5(UpYun.md5(file));
            ret = upYun.writeFile(path + userid + ".png", file, true);
        } catch (IOException e) {
            LogUtil.d("Upyun", "upload headerimg err:" + e.getMessage());
            RevenUtils.sendException("upLoadImg", "UpYunClient", e);
        }
        return ret;
    }

    public static void readDir(String path) {
        // 获取目录中文件列表
        List<UpYun.FolderItem> items = init().readDir(path);
        for (int i = 0; i < items.size(); i++) {
            LogUtil.d(items.get(i).name);
            LogUtil.d(items.get(i).type);
        }
    }

    public static void readFile(String path) {
        // 获取文件信息
        Map<String, String> info = init().getFileInfo(path);
        String type = info.get("type");
        String size = info.get("size");
        String date = info.get("date");
    }


}
