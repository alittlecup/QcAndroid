package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

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
    AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog_Style);
        initView();
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    void initView() {
        setContentView(R.layout.loading_view);
        ImageView img = (ImageView) findViewById(R.id.loading_img);
        animationDrawable = (AnimationDrawable) img.getDrawable();
//        Glide.with(App.AppContex).load(R.drawable.ic_loading_gif).into(img);

    }

    @Override
    public void show() {
        animationDrawable.start();
        super.show();
    }

    @Override
    public void hide() {
        animationDrawable.stop();
        super.hide();
    }
}
