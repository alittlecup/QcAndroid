package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import cn.qingchengfit.widgets.R;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.lang.ref.WeakReference;

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
 * Created by Paper on 15/9/22 2015.
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
    imageView.setBackgroundResource(R.drawable.circle_outside);
    imageView.setPadding(2, 2, 2, 2);
    imageView.setImageDrawable(circularBitmapDrawable);
  }
}

