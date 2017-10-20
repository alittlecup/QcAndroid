package cn.qingchengfit.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.ButterKnife;
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
 * Created by Paper on 15/10/16 2015.
 */
public class LoadingDialog extends Dialog {

    private ImageView imgLoading;
    private Animation rotate;

    public LoadingDialog(final Context context) {
        super(context, R.style.Translucent_NoTitle_TransParent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_input_loading);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
        this.setOnCancelListener(new OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                if (context instanceof Activity) {
                    ((Activity) context).onBackPressed();
                }
            }
        });
        imgLoading = ButterKnife.findById(this, R.id.img_input_loading);
        rotate = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
    }

    @Override public void show() {
        Window window = this.getWindow();
        if(window != null){
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = MeasureUtils.dpToPx(90f, getContext().getResources());
            lp.height = MeasureUtils.dpToPx(90f, getContext().getResources());
            window.setBackgroundDrawableResource(R.drawable.bg_input_loading_cornor);
            window.setAttributes(lp);
            imgLoading.startAnimation(rotate);
            super.show();
        }
    }

    @Override public void dismiss() {
        rotate.cancel();
        super.dismiss();
    }
}

