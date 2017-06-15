package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.WebActivity;
import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;

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
 * Created by Paper on 16/4/22 2016.
 */
public class SimpleImageBanner extends BaseIndicatorBanner<Banner, SimpleImageBanner> {
    private ColorDrawable colorDrawable;

    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override public void onTitleSlect(TextView tv, int position) {
        final Banner item = mDatas.get(position);
        tv.setText(item.title);
    }

    @Override public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        ImageView iv = (ImageView) inflate.findViewById(R.id.iv);

        final Banner item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        //        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.MATRIX);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                WebActivity.startWebForResult(item.getUrl(), getContext(), 444);
            }
        });
        String imgUrl = item.image;

        if (!TextUtils.isEmpty(imgUrl)) {
            try {

                Glide.with(mContext).load(imgUrl).asBitmap().into(new ScaleWidthWrapper(iv, mDisplayMetrics.widthPixels));
            } catch (Exception e) {

            }
        } else {
            iv.setImageDrawable(colorDrawable);
        }

        return inflate;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        super.onTouchEvent(event);
        return true;
    }
}
