package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import cn.qingchengfit.utils.Utils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.ISectionable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/4/7.
 *
 * 可以传入左右padding
 */

public class QcLeftRightDivider extends RecyclerView.ItemDecoration {

    private final int resId = R.drawable.divider_qc_base_line;
    private Drawable mDivider;
    private int mSectionOffset;
    private boolean mDrawOver = false;
    private int viewType = 0;
    private static final int[] ATTRS = new int[]{
        android.R.attr.listDivider
    };
    private boolean withOffset;
    private  int left,right;

    /**
     * Default Android divider will be used.
     *
     * @since 5.0.0-b4
     */
    public QcLeftRightDivider(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used.
     * <p>By default, divider will be drawn underneath the item.</p>
     *
     * @since 5.0.0-b4
     */

   public QcLeftRightDivider(@NonNull Context context,int sectionOffset,
        int viewType,int offsetleft,int offsetright) {
        if (resId > 0) mDivider = ContextCompat.getDrawable(context, resId);
        mSectionOffset = (int) (context.getResources().getDisplayMetrics().density * sectionOffset);
        left = (int) (context.getResources().getDisplayMetrics().density * offsetleft);
        right = (int) (context.getResources().getDisplayMetrics().density * offsetright);
       this.viewType = viewType;

   }

    /**
     * Changes the mode to draw the divider.
     * <p>- When {@code false}, any content will be drawn before the item views are drawn, and will
     * thus appear <i>underneath</i> the views.
     * <br/>- When {@code true}, any content will be drawn after the item views are drawn, and will
     * thus  appear <i>over</i> the views.</p>
     * Default value is false (drawn underneath).
     *
     * @param drawOver true to draw after the item has been added, false to draw underneath the item
     * @return this Divider, so the call can be chained
     * @since 5.0.0-b8
     */
    public QcLeftRightDivider withDrawOver(boolean drawOver) {
        this.mDrawOver = drawOver;
        return this;
    }

    /**
     * @deprecated use {@link #withDrawOver(boolean)} instead.
     */
    public QcLeftRightDivider setDrawOver(boolean drawOver) {
        return withDrawOver(drawOver);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null && !mDrawOver) {
            draw(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null && mDrawOver) {
            draw(c, parent);
        }
    }

    private void draw(Canvas c, RecyclerView parent) {
        //int left = parent.getPaddingLeft();
        //int right = parent.getWidth() - parent.getPaddingRight();
        int left = 0;
        int right = parent.getWidth();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            int truePos = parent.getChildAdapterPosition(child);

            if (this.viewType ==0 ||(parent.getAdapter() != null
                && parent.getAdapter() instanceof FlexibleAdapter && ((FlexibleAdapter)parent.getAdapter()).getItem(truePos) != null
                && this.viewType == parent.getAdapter().getItemViewType(truePos)
            )) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                int bottom = top + mDivider.getIntrinsicHeight() + 1;
                if (parent.getAdapter().getItemCount() > truePos+1 && parent.getAdapter().getItemViewType(truePos+1) == this.viewType) {
                    mDivider.setBounds(left + this.left, top, right - this.right, bottom);
                }else mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * @param gap width of the gap between sections, in pixel. Must be positive.
     * @since 5.0.0-b6
     * @deprecated Use Constructor instead.
     */
    @Deprecated
    public void setSectionGapWidth(@IntRange(from = 0) int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException("Invalid section gap width [<0]: " + gap);
        }
        mSectionOffset = gap;
    }

    /**
     * Applies the physical offset between items, of the same size of the divider previously set.
     *
     * @param withOffset true to leave space between items, false divider will be drawn overlapping
     *                   the items
     * @return this DividerItemDecoration instance so the call can be chained
     * @since 5.0.0-b8
     */
    public QcLeftRightDivider withOffset(boolean withOffset) {
        this.withOffset = withOffset;
        return this;
    }

    /**
     * @since 5.0.0-b4
     */
    @SuppressWarnings({"ConstantConditions", "unchecked", "SuspiciousNameCombination"})
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int offset = (mDivider != null && withOffset ? mDivider.getIntrinsicHeight() : 0);
        if (offset == 0)
            offset = 1;
        if (mSectionOffset > 0 && recyclerView.getAdapter() instanceof FlexibleAdapter) {
            FlexibleAdapter flexibleAdapter = (FlexibleAdapter) recyclerView.getAdapter();
            int position = recyclerView.getChildAdapterPosition(view);
            //Only ISectionable items can finish with a gap and only if next item is a IHeader item
            if (flexibleAdapter.getItem(position) instanceof ISectionable &&
                (flexibleAdapter.isHeader(flexibleAdapter.getItem(position + 1)) ||
                    position >= recyclerView.getAdapter().getItemCount() - 1)) {

                offset += mSectionOffset;
            }
        }
        if (Utils.getOrientation(recyclerView.getLayoutManager()) == RecyclerView.VERTICAL) {
            outRect.set(0, 0, 0, offset);
        } else {
            outRect.set(0, 0, offset, 0);
        }
    }

}
