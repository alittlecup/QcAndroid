package cn.qingchengfit.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import cn.qingchengfit.widgets.R;
import com.afollestad.materialdialogs.GravityEnum;
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
  public static MaterialDialog instanceDelDialog(Context context, String content,
      MaterialDialog.SingleButtonCallback callback) {
    return new MaterialDialog.Builder(context).content(content)
        .autoDismiss(true)
        .contentGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .positiveColorRes(R.color.colorPrimary)
        .negativeColorRes(R.color.text_black)
        .negativeText(R.string.pickerview_cancel)
        .positiveText(R.string.pickerview_submit)
        .contentTextSize(15)
        .contentColorRes(R.color.text_dark)
        .onPositive(callback)
        .build();
  }

  public static MaterialDialog instanceDelDialog(Context context, String title, String content,
      MaterialDialog.SingleButtonCallback callback) {
    return new MaterialDialog.Builder(context).title(title)
        .content(content)
        .autoDismiss(true)
        .contentGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .positiveColorRes(R.color.colorPrimary)
        .negativeColorRes(R.color.text_dark)
        .negativeText(R.string.pickerview_cancel)
        .positiveText(R.string.pickerview_submit)
        .onPositive(callback)
        .build();
  }

  public static MaterialDialog instanceForbid(Context context,
      MaterialDialog.SingleButtonCallback callback) {
    return new MaterialDialog.Builder(context).content(R.string.alert_permission_forbid)
        .autoDismiss(false)
        .contentGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .positiveColorRes(R.color.colorPrimary)
        .positiveText(R.string.common_comfirm)
        .onPositive(callback)
        .contentColorRes(R.color.text_dark)
        .contentTextSize(15)
        .build();
  }

  public static void showIconDialog(Context context, @DrawableRes int drawable, String title,
      String content, String negativeText, String positiveText,
      MaterialDialog.SingleButtonCallback callback) {
    initIconDialog(context, drawable, title, content, negativeText, positiveText, callback).show();
  }

  public static MaterialDialog initIconDialog(Context context, @DrawableRes int drawable,
      String title, String content, String negativeText, String positiveText,
      MaterialDialog.SingleButtonCallback callback) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context).content(content)
        .autoDismiss(false)
        .contentGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .iconRes(drawable)
        .dividerColorRes(R.color.divider_medium)
        .positiveColorRes(R.color.colorPrimary)
        .positiveText(positiveText)
        .onPositive(callback)
        .negativeText(negativeText)
        .negativeColorRes(R.color.text_dark)
        .onNegative(callback);
    if (!TextUtils.isEmpty(title)) {
      builder.title(title);
    } else {
      builder.contentColorRes(R.color.text_dark);
      builder.contentTextSize(15);
    }
    return builder.build();
  }

  public static void showIconDialog(Context context, @DrawableRes int drawable, String title,
      String content, String neutralText, MaterialDialog.SingleButtonCallback callback) {
    initIconDialog(context, drawable, title, content, neutralText, callback).show();
  }

  public static MaterialDialog initIconDialog(Context context, @DrawableRes int drawable,
      String title, String content, String neutralText,
      MaterialDialog.SingleButtonCallback callback) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context).content(content)
        .autoDismiss(false)
        .contentGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .iconRes(drawable)
        .dividerColorRes(R.color.divider_medium)
        .neutralColorRes(R.color.colorPrimary)
        .onNeutral(callback)
        .neutralText(neutralText);
    if (!TextUtils.isEmpty(title)) {
      builder.title(title);
    } else {
      builder.contentColorRes(R.color.text_dark);
      builder.contentTextSize(15);
    }
    return builder.build();
  }

  public static void showConfirm(Context context, String title, String content,
      MaterialDialog.SingleButtonCallback callback) {
    initConfirmDialog(context, title, content, callback).show();
  }

  public static MaterialDialog initConfirmDialog(Context context, String title, String content,
      MaterialDialog.SingleButtonCallback callback) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context).content(content)
        .autoDismiss(false)
        .contentGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .dividerColorRes(R.color.divider_medium)
        .positiveColorRes(R.color.colorPrimary)
        .positiveText("确定")
        .onPositive(callback)
        .negativeText("取消")
        .negativeColorRes(R.color.text_dark)
        .onNegative(callback);
    if (!TextUtils.isEmpty(title)) {
      builder.title(title);
    } else {
      builder.contentColorRes(R.color.text_dark);
      builder.contentTextSize(15);
    }
    return builder.build();
  }

  public static void shwoConfirm(Context context, String content,
      MaterialDialog.SingleButtonCallback callback) {
    showConfirm(context, null, content, callback);
  }

  public static void showAlert(Context context, String title, String content,
      MaterialDialog.SingleButtonCallback callback) {
    initAlertDialog(context, title, content, callback).show();
  }

  public static MaterialDialog initAlertDialog(Context context, String title, String content,
      MaterialDialog.SingleButtonCallback callback) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context).content(content)
        .autoDismiss(false)
        .contentGravity(GravityEnum.CENTER)
        .titleGravity(GravityEnum.CENTER)
        .buttonsGravity(GravityEnum.CENTER)
        .dividerColorRes(R.color.divider_medium)
        .neutralColorRes(R.color.colorPrimary)
        .onNeutral(callback==null?(dialog,action)->dialog.dismiss():callback)
        .neutralText(cn.qingchengfit.widgets.R.string.common_i_konw);
    if (!TextUtils.isEmpty(title)) {
      builder.title(title);
    } else {
      builder.contentColorRes(R.color.text_dark);
      builder.contentTextSize(15);
    }
    return builder.build();
  }

  public static void showAlert(Context context, String content,
      MaterialDialog.SingleButtonCallback callback) {
    showAlert(context, null, content, callback);
  }

  public static void showAlert(Context context, String content) {
    showAlert(context, null, content, null);
  }

  public static void showAlert(Context context, @StringRes int content,
      MaterialDialog.SingleButtonCallback callback) {
    showAlert(context, null, context.getString(content), callback);
  }

  public static void showAlert(Context context, @StringRes int content) {
    showAlert(context, null, context.getString(content), null);
  }
}
