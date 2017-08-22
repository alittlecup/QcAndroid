package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import com.bumptech.glide.Glide;

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
 * Created by Paper on 16/1/26 2016.
 */
@SuppressWarnings("unused") public class DrawerImgItem extends LinearLayout {

    public DrawerImgItem(Context context) {
        super(context);
    }

    public DrawerImgItem(Context context, String img, String text, OnClickListener listener) {
        super(context);
        inflate(context, R.layout.layout_drawerimg, this);
        ImageView imgView = (ImageView) findViewById(R.id.segment_img);
        TextView tv = (TextView) findViewById(R.id.segment_text);
        Glide.with(context).load(img).into(imgView);
        tv.setText(text);
        setOnClickListener(listener);
    }

    public DrawerImgItem(Context context, @DrawableRes int img, String text, OnClickListener listener) {
        super(context);
        inflate(context, R.layout.layout_drawerimg, this);
        ImageView imgView = (ImageView) findViewById(R.id.segment_img);
        TextView tv = (TextView) findViewById(R.id.segment_text);
        Glide.with(context).load(img).into(imgView);
        tv.setText(text);
        setOnClickListener(listener);
    }

    public DrawerImgItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerImgItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
