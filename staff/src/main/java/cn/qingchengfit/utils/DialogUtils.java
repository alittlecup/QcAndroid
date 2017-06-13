package cn.qingchengfit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import cn.qingchengfit.staffkit.R;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/18 2016.
 */
public class DialogUtils {
    public static MaterialDialog instanceDelDialog(Context context, String content, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context).content(content)
            .autoDismiss(true)
            .positiveColorRes(R.color.orange)
            .negativeColorRes(R.color.text_black)
            .negativeText(R.string.pickerview_cancel)
            .positiveText(R.string.pickerview_submit)
            .onPositive(callback)
            .build();
    }

    public static MaterialDialog instanceDelDialog(Context context, String title, String content,
        MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context).title(title)
            .content(content)
            .autoDismiss(true)
            .positiveColorRes(R.color.orange)
            .negativeColorRes(R.color.text_black)
            .negativeText(R.string.pickerview_cancel)
            .positiveText(R.string.pickerview_submit)
            .onPositive(callback)
            .build();
    }

    public static MaterialDialog instanceForbid(Context context, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context).content(R.string.alert_permission_forbid)
            .autoDismiss(false)
            .positiveColorRes(R.color.orange)
            .positiveText(R.string.common_comfirm)
            .onPositive(callback)
            .build();
    }

    public static void showAlert(Context context, String title, String content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).content(content)
            .autoDismiss(false)
            .positiveColorRes(R.color.orange)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            })
            .positiveText(R.string.common_i_konw);
        if (title != null) builder.title(title);
        builder.build().show();
    }

    public static void showAlert(Context context, String content) {
        showAlert(context, null, content);
    }

    public static void showAlert(Context context, @StringRes int content) {
        showAlert(context, null, context.getString(content));
    }
}
