package com.qingchengfit.fitcoach.Utils;

import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.qingchengfit.fitcoach.App;
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
 * Created by Paper on 15/11/5 2015.
 */
public class ToastUtils {

    public static Toast gToast;

    public static void show(@DrawableRes int img, String content) {
        if (App.AppContex == null) return;
        if (gToast != null) gToast.cancel();
        gToast = Toast.makeText(App.AppContex, content, Toast.LENGTH_SHORT);
        gToast.setGravity(Gravity.CENTER, 0, -100);
        View view = LayoutInflater.from(App.AppContex).inflate(R.layout.toastview, null, false);
        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_img);
        gToast.setView(view);
        tv.setText(content);
        imageView.setImageResource(img);
        gToast.show();
    }

    public static void show(String content) {
        show(R.drawable.ic_share_success, content);
    }

    public static void showDefaultStyle(String content) {
        Toast.makeText(App.AppContex, content, Toast.LENGTH_SHORT).show();
    }
}
