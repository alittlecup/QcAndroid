package cn.qingchengfit.support.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import cn.qingchengfit.widgets.R;

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
 * Created by Paper on 2017/6/10.
 */

public class CompatTextView extends AppCompatTextView {
    public CompatTextView(Context context) {
        super(context);
        init(null);
    }

    public CompatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CompatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public CompatTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            Context context = getContext();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompatTextView);

            // Obtain DrawableManager used to pull Drawables safely, and check if we're in RTL
            boolean rtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;

            // Grab the compat drawable resources from the XML
            int startDrawableRes = a.getResourceId(R.styleable.CompatTextView_drawableStart, 0);
            int topDrawableRes = a.getResourceId(R.styleable.CompatTextView_drawableTop, 0);
            int endDrawableRes = a.getResourceId(R.styleable.CompatTextView_drawableEnd, 0);
            int bottomDrawableRes = a.getResourceId(R.styleable.CompatTextView_drawableBottom, 0);

            // Load the used drawables, falling back to whatever may be set in an "android:" namespace attribute
            Drawable[] currentDrawables = getCompoundDrawables();
            Drawable left = startDrawableRes != 0 ? ContextCompat.getDrawable(context, startDrawableRes) : currentDrawables[0];
            Drawable right = endDrawableRes != 0 ? ContextCompat.getDrawable(context, endDrawableRes) : currentDrawables[1];
            Drawable top = topDrawableRes != 0 ? ContextCompat.getDrawable(context, topDrawableRes) : currentDrawables[2];
            Drawable bottom = bottomDrawableRes != 0 ? ContextCompat.getDrawable(context, bottomDrawableRes) : currentDrawables[3];

            // Account for RTL and apply the compound Drawables
            Drawable start = rtl ? right : left;
            Drawable end = rtl ? left : right;
            setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);

            a.recycle();
        }
    }
}
