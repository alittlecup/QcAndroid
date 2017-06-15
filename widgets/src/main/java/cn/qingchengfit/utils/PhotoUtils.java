package cn.qingchengfit.utils;

import android.text.TextUtils;
import android.widget.ImageView;
import cn.qingchengfit.widgets.R;
import com.bumptech.glide.Glide;

/**
 * Created by peggy on 16/5/26.
 */

public class PhotoUtils {
    public static String getSmall(String url) {
        if (TextUtils.isEmpty(url)) return "";
        if (!url.contains("!")) {
            return url + "!120x120";
        } else {
            try {
                return url.split("!")[0] + "!120x120";
            } catch (Exception e) {
                return url;
            }
        }
    }

    public static String getMiddle(String url) {
        if (TextUtils.isEmpty(url)) return "";
        if (!url.contains("!")) {
            return url + "!180x180";
        } else {
            try {
                return url.split("!")[0] + "!120x120";
            } catch (Exception e) {
                return url;
            }
        }
    }

    public static String getGauss(String photo) {
        if (photo.contains("zoneke-img")) {
            if (photo.contains("!")) {
                return photo.split("!")[0].concat("!gaussblur");
            } else {
                return photo.concat("!gaussblur");
            }
        } else {
            return photo;
        }
    }

    public static void origin(ImageView v, String url, int placeholder, int error) {
        Glide.with(v.getContext()).load(url).placeholder(placeholder).error(error).into(v);
    }

    public static void originCircle(ImageView v, String url, int placeholder, int error) {
        Glide.with(v.getContext()).load(url).asBitmap().placeholder(placeholder).error(error).into(new CircleImgWrapper(v, v.getContext()));
    }

    public static void smallCircle(ImageView v, String url) {
        smallCircle(v, url, R.drawable.img_loadingimage, R.drawable.img_loadingimage);
    }

    public static void smallCircle(ImageView v, String url, int placeholder, int error) {
        Glide.with(v.getContext())
            .load(getSmall(url))
            .asBitmap()
            .placeholder(placeholder)
            .error(error)
            .into(new CircleImgWrapper(v, v.getContext()));
    }

    public static void small(ImageView v, String url) {
        origin(v, getSmall(url), R.drawable.img_loadingimage, R.drawable.img_loadingimage);
    }

    public static void middle(ImageView v, String url) {
        origin(v, getMiddle(url), R.drawable.img_loadingimage, R.drawable.img_loadingimage);
    }

    public static void origin(ImageView v, String url) {
        origin(v, url, R.drawable.img_loadingimage, R.drawable.img_loadingimage);
    }
}
