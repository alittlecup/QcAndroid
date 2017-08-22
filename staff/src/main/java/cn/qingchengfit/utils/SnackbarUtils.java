package cn.qingchengfit.utils;

import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;

/**
 *
 */
public class SnackbarUtils {

    public static void showSnackBar(View view, String content) {
        Snackbar snackbar = Snackbar.make(view, content, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(view.getContext().getResources().getColor(R.color.qc_snackbar_bg));
        snackbarLayout.setAlpha(0.88f);
        TextView textView = ((TextView) snackbarLayout.findViewById(R.id.snackbar_text));
        textView.setTextColor(view.getContext().getResources().getColor(R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setGravity(Gravity.CENTER);
        Drawable drawable = view.getContext().getResources().getDrawable(R.drawable.ic_snackbar);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setCompoundDrawablePadding(18);
        snackbar.show();
    }
}
