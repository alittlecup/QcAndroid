package cn.qingchengfit.saascommon.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.qingchengfit.saascommon.R;

/**
 * Created by Bob Du on 2018/10/29 14:29
 */
public class CommonDialog extends Dialog {
    private ImageView closeBtn;
    private ImageView contentIv;
    private DialogClickListener listener;

    public CommonDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_common);
        closeBtn = findViewById(R.id.btn_close);
        contentIv = findViewById(R.id.iv_content);
        setCanceledOnTouchOutside(true);
    }

    @SuppressLint("ResourceType")
    public void setImageView(@LayoutRes int imageRes) {
        contentIv.setImageResource(imageRes);
    }

    public void showDialog() {
        initListener();
        Window window = getWindow();
        window.setWindowAnimations(R.style.QcStyleDialog);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        show();
    }

    private void initListener() {
        closeBtn.setOnClickListener(v -> {
            if(listener != null) {
                listener.onCloseClickListener(this);
            }
        });
        contentIv.setOnClickListener(v -> {
            if(listener != null) {
                listener.onItemClickListener(this);
            }
        });
    }

    public void setClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public interface DialogClickListener {
        void onCloseClickListener(Dialog dialog);

        void onItemClickListener(Dialog dialog);
    }


}
