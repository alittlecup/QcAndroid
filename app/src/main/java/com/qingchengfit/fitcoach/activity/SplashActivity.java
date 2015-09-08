package com.qingchengfit.fitcoach.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImagesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private  int[] mSplashImg = new int[]{
            R.drawable.img_help1,
            R.drawable.img_help2,
            R.drawable.img_help3,
            R.drawable.img_help4,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();

        // 2.36（不包括）之前的版本需要调用以下2行代码
//        Intent service = new Intent(context, XGPushService.class);
//        context.startService(service);


        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(mSplashImg[i]);
            imageViews.add(imageView);
        }
        ImageView imageViewlast = new ImageView(this);
        imageViews.add(imageViewlast);
        imageViewlast.setId(R.id.splash_last);

        splashViewpager.setAdapter(new ImagesAdapter(imageViews));
        splashIndicator.setViewPager(splashViewpager);
        splashViewpager.setPageTransformer(true, (page, position) -> {
            //页面滑动动画
            LogUtil.d("page:" + page.getId() + "    positon:" + position);
            if (page.getId() == R.id.splash_last && position < 0.5) {
                setResult(1);
                this.finish();
            }
        });
    }

    @OnClick({
            R.id.splash_login_btn,
            R.id.splash_registe_btn
    })
    public void OnBtnClick(View v) {
//        Intent toLogin = new Intent(this, LoginActivity.class);
        if (v.getId() == R.id.splash_registe_btn) {
//            toLogin.putExtra("isRegiste", true);
            setResult(1);
        } else setResult(0);
//        startActivity(toLogin);

        this.finish();
    }


}
