package com.paper.paperbaselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

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
 * Created by Paper on 15/8/28 2015.
 */
public class ChoosePicUtils {
    public static final String TAG = ChoosePicUtils.class.getName();

    public static final int CHOOSE_CAMERA = 101;
    public static final int CHOOSE_GALLERY = 102;


    public static void choosePicFromCamera(Context context, Uri uri) {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//         根据文件地址创建文件
//        File file = new File(FILE_PATH);
//        if (!file.exists())
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                RevenUtils.sendException("goCamera", TAG, e);
//            }
//        Uri uri = Uri.fromFile(file);
//         设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        ((Activity) context).startActivityForResult(intent, CHOOSE_CAMERA);
    }

    public static void choosePicFromGalley(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ((Activity) context).startActivityForResult(intent, CHOOSE_GALLERY);
        } else {
            ((Activity) context).startActivityForResult(intent, CHOOSE_GALLERY);
        }
    }

    /**
     * 处理选择返回的图片
     *
     * @param requestcode 请求code
     */
    public static File choosePicFileCtl(Context context, int requestcode, Intent data, String fp) {
        String filepath = null;
        if (requestcode == CHOOSE_GALLERY || requestcode / 100 == 3) {
            filepath = FileUtils.getPath(context, data.getData());
        } else if (requestcode == CHOOSE_CAMERA || requestcode / 100 == 2) {
            filepath = fp;
        } else filepath = fp;
        return new File(filepath);
    }


}
