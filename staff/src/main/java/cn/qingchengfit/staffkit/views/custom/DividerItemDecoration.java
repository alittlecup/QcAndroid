package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;

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
 * Created by Paper on 15/9/8 2015.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public final static int LEFT = 1;
    public final static int ALL = 0;
    public final static int RIGHT = 2;

    /*
    * RecyclerView的布局方向，默认先赋值
    * 为纵向布局
    * RecyclerView 布局可横向，也可纵向
    * 横向和纵向对应的分割想画法不一样
    * */
    private int mOrientation = LinearLayoutManager.VERTICAL;

    /**
     * item之间分割线的size，默认为1
     */
    private int mItemSize = 1;
    private int mMargin = 0;

    /**
     * 绘制item分割线的画笔，和设置其属性
     * 来绘制个性分割线
     */
    private Paint mPaint;
    private Paint mBgPaint;
    private Context context;

    private int type;

    /**
     * 构造方法传入布局方向，不可不传
     */
    public DividerItemDecoration(Context context, int orientation) {
        this.mOrientation = orientation;
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        this.context = context;
        //        mItemSize = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_PX, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.divider_grey));
         /*设置填充*/
        mPaint.setStyle(Paint.Style.FILL);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(CompatUtils.getColor(context, R.color.white));
         /*设置填充*/
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    public DividerItemDecoration(Context context, int orientation, int margin) {
        this.mOrientation = orientation;
        this.mMargin = margin;
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        this.context = context;
        //        mItemSize = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_PX, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.divider_grey));
         /*设置填充*/
        mPaint.setStyle(Paint.Style.FILL);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(CompatUtils.getColor(context, R.color.white));
         /*设置填充*/
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制纵向 item 分割线
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mItemSize;
            canvas.drawRect(left, top, right, bottom, mBgPaint);
            if (type == ALL) {
                canvas.drawRect(left + mMargin, top, right - mMargin, bottom, mPaint);
            } else if (type == LEFT) {
                canvas.drawRect(left + mMargin, top, right, bottom, mPaint);
            } else if (type == RIGHT) {
                canvas.drawRect(left, top, right, bottom - mMargin, mPaint);
            }
        }
    }

    /**
     * 绘制横向 item 分割线
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 设置item分割线的size
     */
    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, MeasureUtils.dpToPx((float) mItemSize / 2, context.getResources()));
        } else {
            outRect.set(0, 0, MeasureUtils.dpToPx((float) mItemSize / 2, context.getResources()), 0);
        }
    }
}
