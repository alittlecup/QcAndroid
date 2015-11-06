package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;

import java.io.File;

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
 * Created by Paper on 15/8/26 2015.
 */
public class PicChooseDialog extends Dialog {

    private View view;

    public PicChooseDialog(Context context) {
        super(context, R.style.ChoosePicDialogStyle);
        view = getLayoutInflater().inflate(R.layout.dialog_pic_choose, null);
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.findViewById(R.id.choosepic_camera).setOnClickListener(view1 -> ChoosePicUtils.choosePicFromCamera(context, Uri.fromFile(new File(Configs.CameraPic))));
        view.findViewById(R.id.choosepic_galley).setOnClickListener(view1 -> ChoosePicUtils.choosePicFromGalley(context));
    }

    public PicChooseDialog(Context context, int theme, @LayoutRes int layout) {
        super(context, theme);
        this.setContentView(layout);
    }

    @Override
    public void show() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        super.show();
    }

    public void onDismiss(OnDismissListener listener) {
        this.setOnDismissListener(listener);
    }

    public void setListener(View.OnClickListener camera, View.OnClickListener gallery) {
        view.findViewById(R.id.choosepic_camera).setOnClickListener(camera);
        view.findViewById(R.id.choosepic_galley).setOnClickListener(gallery);
    }
}
