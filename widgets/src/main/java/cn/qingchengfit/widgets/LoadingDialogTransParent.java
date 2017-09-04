package cn.qingchengfit.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
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
 * Created by Paper on 16/2/15 2016.
 */
public class LoadingDialogTransParent extends Dialog {

    ImageView imageLoading;
    Animation rotate;

    public LoadingDialogTransParent(final Activity context) {
        super(context, R.style.Translucent_NoTitle_TransParent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = MeasureUtils.dpToPx(60f, getContext().getResources());
        window.setAttributes(params);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
        this.setOnCancelListener(new OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                if (context instanceof Activity) {
                    (context).onBackPressed();
                }
            }
        });

        imageLoading = ButterKnife.findById(this, R.id.img_loading);
        rotate = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);

    }

    @Override public void show() {
        imageLoading.startAnimation(rotate);
        super.show();
    }

    @Override public void dismiss() {
        rotate.cancel();
        super.dismiss();
    }
}
