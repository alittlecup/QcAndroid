package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImagesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

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
 * Created by Paper on 15/8/5 2015.
 */
public class SplashActivity extends BaseAcitivity {

    @Bind(R.id.splash_viewpager)
    ViewPager splashViewpager;
    @Bind(R.id.splash_indicator)
    CircleIndicator splashIndicator;

    List<View> imageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //百度云推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "ZVc12KfmeoroYVV0iLcvSCCr");

        for (int i=0 ;i < 4 ;i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.qingchenglogo);
            imageViews.add(imageView);
        }
        splashViewpager.setAdapter(new ImagesAdapter(imageViews));
        splashIndicator.setViewPager(splashViewpager);
        splashViewpager.setPageTransformer(true, (page, position) -> {
            //页面滑动动画

        });
    }



}
