package cn.qingchengfit.saascommon.widget.bubble;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.qingchengfit.saascommon.R;

/**
 * Created by Bob Du on 2018/9/10 16:53
 */
public class BubbleRelativeLayout extends RelativeLayout {

    public enum BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }
    public static int PADDING = 30;
    public static int LEG_HALF_BASE = 30;
    public static float CORNER_RADIUS = 25.0f;
    public static float BACKGROUND_COLOR = Color.rgb(0, 206, 216);
    public static float BACKGROUND_COLOR_BLACK = Color.rgb(35, 36, 40);
    public static float ALPHA = 0.95f;
    public static float MIN_LEG_DISTANCE = PADDING + LEG_HALF_BASE;

    private Paint mFillPaint = null;
    private final Path mPath = new Path();
    private final Path mBubbleLegPrototype = new Path();

    private float mBubbleLegOffset = 0.75f;
    private BubbleLegOrientation mBubbleOrientation = BubbleLegOrientation.TOP;

    public BubbleRelativeLayout(Context context, int identity) {
        this(context, null, identity);
    }

    public BubbleRelativeLayout(Context context, AttributeSet attrs, int identity) {
        super(context, attrs, identity);
        init(context, attrs, identity);
    }

    private void init(final Context context, final AttributeSet attrs, int identity) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.bubble);
            try {
                PADDING = a.getDimensionPixelSize(R.styleable.bubble_padding, PADDING);
                BACKGROUND_COLOR = a.getFloat(R.styleable.bubble_backgroundColor, BACKGROUND_COLOR);
                LEG_HALF_BASE = a.getDimensionPixelSize(R.styleable.bubble_halfBaseOfLeg, LEG_HALF_BASE);
                MIN_LEG_DISTANCE = PADDING + LEG_HALF_BASE;
                CORNER_RADIUS = a.getFloat(R.styleable.bubble_cornerRadius, CORNER_RADIUS);
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        mFillPaint = new Paint(Paint.DITHER_FLAG);
        mFillPaint.setColor(Color.WHITE);
        if(identity == 0) {
            mFillPaint.setShader(new LinearGradient(100f, 0f, 100f, 200f, (int)BACKGROUND_COLOR, (int)BACKGROUND_COLOR, Shader.TileMode.CLAMP));
        } else {
            mFillPaint.setShader(new LinearGradient(100f, 0f, 100f, 200f, (int)BACKGROUND_COLOR_BLACK, (int)BACKGROUND_COLOR_BLACK, Shader.TileMode.CLAMP));
        }
        mFillPaint.setAlpha((int)(255 * ALPHA));

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, mFillPaint);
        }
        renderBubbleLegPrototype();

        setPadding(PADDING, PADDING, PADDING, PADDING);

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void renderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(15, 0);
        mBubbleLegPrototype.lineTo(PADDING * 1.2f, -PADDING / 1.2f);
        mBubbleLegPrototype.lineTo(PADDING * 1.2f, PADDING / 1.2f);
        mBubbleLegPrototype.close();
    }

    public void setBubbleParams(final BubbleLegOrientation bubbleOrientation, final float bubbleOffset) {
        mBubbleLegOffset = bubbleOffset;
        mBubbleOrientation = bubbleOrientation;
    }

    private Matrix renderBubbleLegMatrix(final float width, final float height) {

        final float offset = Math.max(mBubbleLegOffset, MIN_LEG_DISTANCE);

        float dstX = 0;
        float dstY = Math.min(offset, height - MIN_LEG_DISTANCE);
        final Matrix matrix = new Matrix();

        switch (mBubbleOrientation) {

            case TOP:
                dstX = Math.min(offset, width - MIN_LEG_DISTANCE);
                dstY = 0;
                matrix.postRotate(90);
                break;

            case RIGHT:
                dstX = width;
                dstY = Math.min(offset, height - MIN_LEG_DISTANCE);
                matrix.postRotate(180);
                break;

            case BOTTOM:
                dstX = Math.min(offset, width - MIN_LEG_DISTANCE);
                dstY = height;
                matrix.postRotate(270);
                break;

        }

        matrix.postTranslate(dstX, dstY);
        return matrix;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final float width = canvas.getWidth();
        final float height = canvas.getHeight();

        mPath.rewind();
        mPath.addRoundRect(new RectF(PADDING, PADDING, width - PADDING - 10, height - PADDING),
                new float[]{CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS}, Path.Direction.CW);
        mPath.addPath(mBubbleLegPrototype, renderBubbleLegMatrix(width, height));
        canvas.drawPath(mPath, mFillPaint);
    }

}
