package cn.qingchengfit.shop.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import cn.qingchengfit.utils.MeasureUtils;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.lang.ref.WeakReference;

/**
 * Created by huangbaole on 2018/3/20.
 */

public class Corner4dpImgWrapper extends BitmapImageViewTarget {

  WeakReference<Context> context;
  ImageView imageView;

  public Corner4dpImgWrapper(ImageView view, Context context) {
    super(view);
    this.imageView = view;
    this.context = new WeakReference<Context>(context);
  }

  @Override protected void setResource(Bitmap resource) {
    RoundedBitmapDrawable circularBitmapDrawable =
        RoundedBitmapDrawableFactory.create(context.get().getResources(), resource);
    circularBitmapDrawable.setCornerRadius(MeasureUtils.dpToPx(4f, context.get().getResources()));
    imageView.setPadding(2, 2, 2, 2);
    imageView.setImageDrawable(circularBitmapDrawable);
  }
}