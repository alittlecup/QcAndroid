package cn.qingchengfit.shop.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * Created by huangbaole on 2018/1/29.
 */

public class LoadingImageDialog extends Dialog {

  private final View imageLoading;
  private final Animation rotate;
  private TextView textView;

  public LoadingImageDialog(Activity context) {
    super(context, cn.qingchengfit.widgets.R.style.Translucent_NoTitle_TransParent);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.setContentView(R.layout.shop_dialog_loading);
    setCancelable(false);
    setCanceledOnTouchOutside(false);
    imageLoading = findViewById(cn.qingchengfit.widgets.R.id.img_loading);
    rotate = AnimationUtils.loadAnimation(context, cn.qingchengfit.widgets.R.anim.loading_rotate);
    textView = findViewById(cn.qingchengfit.widgets.R.id.content_text);
    textView.setText("正在上传中...");
  }

  public void setTextView(String content) {
    textView.setText(content);
  }

  @Override public void show() {
    Window window = this.getWindow();
    if (window != null) {
      int i = MeasureUtils.dpToPx(10f, getContext().getResources());
      window.setBackgroundDrawableResource(
          cn.qingchengfit.widgets.R.drawable.bg_input_loading_cornor);
      imageLoading.startAnimation(rotate);
      super.show();
    }
  }

  @Override public void dismiss() {
    rotate.cancel();
    super.dismiss();
  }
}
