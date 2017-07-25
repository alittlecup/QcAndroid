package cn.qingchengfit.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.qingchengfit.widgets.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;

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

  public static void originCenterCrop(ImageView v, String url, int placeholder, int error) {
    Glide.with(v.getContext()).load(url).centerCrop().placeholder(placeholder).error(error).into(v);
  }

  public static void originCircle(ImageView v, String url, int placeholder, int error) {
    Glide.with(v.getContext())
        .load(url)
        .asBitmap()
        .placeholder(placeholder)
        .error(error)
        .into(new CircleImgWrapper(v, v.getContext()));
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

  public static void small(ImageView v, String url, int loading) {
    origin(v, getSmall(url), loading, loading);
  }

  public static void middle(ImageView v, String url) {
    origin(v, getMiddle(url), R.drawable.img_loadingimage, R.drawable.img_loadingimage);
  }

  public static void origin(ImageView v, String url) {
    origin(v, url, R.drawable.img_loadingimage, R.drawable.img_loadingimage);
  }

  public static void originCenterCrop(ImageView v, String url) {
    originCenterCrop(v, url, R.drawable.img_loadingimage, R.drawable.img_loadingimage);
  }

  public static void smallCornner4dp(ImageView v, String url){
    Glide.with(v.getContext())
        .load(getSmall(url))
        .asBitmap()
        .placeholder(R.drawable.img_loadingimage)
        .error(R.drawable.img_loadingimage)
        .into(new Corner4dpImgWrapper(v, v.getContext()));
  }

  public static void conner4dp(ImageView v, String url) {
    Glide.with(v.getContext()).load(url).asBitmap().placeholder(R.color.backgroud_grey)
        //.transform(new Corner4dpImgTrans(v.getContext(), 10))
        .into(new Corner4dpImgWrapper(v, v.getContext()));
  }

  public static void loadWidth(final Context context, final String url, final ImageView v,
      final int w) {
    Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {
      @Override public void onResourceReady(GlideDrawable resource,
          GlideAnimation<? super GlideDrawable> glideAnimation) {
      }
    }).getSize(new SizeReadyCallback() {
      @Override public void onSizeReady(int width, int height) {
        int newH = height / width * w;
        Glide.with(context).load(url).override(w, newH).into(v);
      }
    });
  }

  //public static void smallFile(ImageView v, String url){
  //    Glide.with(v.getContext())
  //        .load(url)
  //        .asBitmap()
  //        .into(new CircleImgWrapper(v, v.getContext()));
  //}
}
