package com.qingchengfit.fitcoach.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.LoadingPointerView;
import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/10/16 2015.
 */
public class LoadingDialog extends Dialog {

  ImageView imageLoading;
  Animation rotate;

  public LoadingDialog(final Context context) {
    this(context, "加载中...");
  }

  public LoadingDialog(final Context context, String text) {
    super(context, R.style.Translucent_NoTitle);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.setContentView(R.layout.dialog_loading);
    this.setCanceledOnTouchOutside(false);
    this.setCancelable(true);
    this.setOnCancelListener(new OnCancelListener() {
      @Override public void onCancel(DialogInterface dialog) {
        if (context instanceof android.app.Activity) {
          ((Activity) context).onBackPressed();
        }
      }
    });
    View viewById = findViewById(R.id.content_text);
    if (viewById instanceof TextView&&!TextUtils.isEmpty(text)) {
      ((TextView) viewById).setText(text);
    }
    imageLoading = findViewById( cn.qingchengfit.widgets.R.id.img_loading);
    rotate = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
  }

  @Override public void show() {
    Window window = this.getWindow();
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = MeasureUtils.dpToPx(150f, getContext().getResources());
    lp.height = MeasureUtils.dpToPx(130f, getContext().getResources());
    window.setAttributes(lp);
    imageLoading.startAnimation(rotate);
    super.show();
  }

  @Override public void dismiss() {
    rotate.cancel();
    super.dismiss();
  }
}

