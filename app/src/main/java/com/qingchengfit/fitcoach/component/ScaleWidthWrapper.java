package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.paper.paperbaselibrary.utils.MeasureUtils;

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
 * Created by Paper on 15/10/1 2015.
 */
public class ScaleWidthWrapper extends BitmapImageViewTarget {
    WeakReference<Context> context;
    ImageView imageView;

    public ScaleWidthWrapper(ImageView view, Context context) {
        super(view);
        this.imageView = view;
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void setResource(Bitmap resource) {
        int SrcWidth = MeasureUtils.getScreenWidth(context.get().getResources());
        float scale = (float) SrcWidth / (float) resource.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
        imageView.setImageBitmap(resource);
    }
}
