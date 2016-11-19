package com.qingchengfit.fitcoach.component;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

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
 * Created by Paper on 15/10/1 2015.
 */
public class ScaleWidthWrapper extends BitmapImageViewTarget {
    //    WeakReference<Context> context;
    WeakReference<Bitmap> mSourse;
    ImageView imageView;
    private int mWidth = 0;
    public ScaleWidthWrapper(ImageView view) {
        super(view);
        this.imageView = view;
//        this.context = new WeakReference<Context>(context);
    }

    public ScaleWidthWrapper(ImageView view,int w) {
        super(view);
        this.imageView = view;
        this.mWidth = w;
    }

    @Override
    protected void setResource(Bitmap resource) {

        int SrcWidth = 0;
        if (mWidth != 0)
            SrcWidth = mWidth;
        else     SrcWidth = imageView.getWidth();
        float scale = (float) SrcWidth / (float) resource.getWidth();
        Matrix matrix = new Matrix();
//        if (scale > 1)
//            scale = 1;
        matrix.postScale(scale, scale);
        if (resource.getWidth() <=0 || resource.getHeight() <= 0 || scale<=0)
            return;
        mSourse = new WeakReference<Bitmap>(Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true));
        imageView.setImageBitmap(mSourse.get());
    }
}
