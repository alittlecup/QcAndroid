package cn.qingchengfit.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RatingBar;
import cn.qingchengfit.utils.CompatUtils;

public class RatingBarVectorFix extends android.support.v7.widget.AppCompatRatingBar {

    private Bitmap mSampleTile;

    public RatingBarVectorFix(Context context) {
        this(context, null);
    }

    public RatingBarVectorFix(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.ratingBarStyle);
    }

    public RatingBarVectorFix(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayerDrawable drawable = (LayerDrawable) tileify(getProgressDrawable(), false);
        drawable.findDrawableByLayerId(android.R.id.background).setColorFilter(Color.parseColor("#d1d1d1"), PorterDuff.Mode.SRC_ATOP);
        drawable.findDrawableByLayerId(android.R.id.progress).setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_ATOP);
        setProgressDrawable(drawable);
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        boolean instanceOfDrawableWrapper = false;
        if(Build.VERSION.SDK_INT < 23){
            instanceOfDrawableWrapper = drawable instanceof android.support.v7.graphics.drawable.DrawableWrapper;
        }else {
            instanceOfDrawableWrapper = drawable instanceof DrawableWrapper;
        }

        if (instanceOfDrawableWrapper) {
            //if (Build.VERSION.SDK_INT < 23){
            //    Drawable inner = ((android.support.v7.graphics.drawable.DrawableWrapper) drawable).getWrappedDrawable();
            //    if (inner != null) {
            //        inner = tileify(inner, clip);
            //        ((android.support.v7.graphics.drawable.DrawableWrapper) drawable).setWrappedDrawable(inner);
            //    }
            //}else {
            //    Drawable inner = ((DrawableWrapper) drawable).getWrappedDrawable();
            //    if (inner != null) {
            //        inner = tileify(inner, clip);
            //        ((android.support.v7.graphics.drawable.DrawableWrapper) drawable).setWrappedDrawable(inner);
            //    }
            //}

        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }
            LayerDrawable newBg = new LayerDrawable(outDrawables);

            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }

            return newBg;

        } else if (drawable instanceof BitmapDrawable) {
            final BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            final Bitmap tileBitmap = bitmapDrawable.getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }

            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);
            shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
            return tileify(getBitmapDrawableFromVectorDrawable(drawable), clip);
        }

        return drawable;
    }

    private BitmapDrawable getBitmapDrawableFromVectorDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSampleTile != null) {
            final int width = mSampleTile.getWidth() * getNumStars();
            setMeasuredDimension(ViewCompat.resolveSizeAndState(width, widthMeasureSpec, 0),
                    getMeasuredHeight());
        }
    }

    private Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{5, 5, 5, 5, 5, 5, 5, 5};
        return new RoundRectShape(roundedCorners, null, null);
    }
}