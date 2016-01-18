package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImagesAdapter;
import com.qingchengfit.fitcoach.component.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

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
    @Bind(R.id.main_loading)
    RelativeLayout mainLoading;
    float touchX;
    private int[] mSplashImg = new int[]{
            R.drawable.help1,
            R.drawable.help2,
            R.drawable.help3,
            R.drawable.help4,
            R.drawable.help5,
    };
    private String[] mColors = new String[]{
            "#55b37f", "#5595b3", "#b38855", "#675Eb1", "#9e74b0"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "ZVc12KfmeoroYVV0iLcvSCCr");
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder();
//        builder.setStatusbarIcon(R.drawable.);
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(s -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    return Observable.just("");
                }).subscribe(s1 -> {
            runOnUiThread(() -> {
                if (PreferenceUtils.getPrefString(this, "session_id", null) != null) {
                    Intent toMain = new Intent(this, MainActivity.class);
                    startActivity(toMain);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
                    this.finish();
                } else {
                    goSplashViewpager();
                    ViewCompat.animate(mainLoading).alpha(0.1f).setDuration(1000).withLayer().setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {

                            mainLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    }).start();


                }
            });

        });


    }

    public void goSplashViewpager() {
        LogUtil.e("oem:"+getString(R.string.oem_tag));
        if (getString(R.string.oem_tag).equalsIgnoreCase("qingcheng")) {

            for (int i = 0; i < mSplashImg.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(mSplashImg[i]);
                imageView.setBackgroundColor(Color.parseColor(mColors[i]));
                imageViews.add(imageView);
            }
//            ImageView imageViewlast = new ImageView(this);
//            imageViews.add(imageViewlast);
//            imageViewlast.setId(R.id.splash_last);

            splashViewpager.setAdapter(new ImagesAdapter(imageViews));
            splashIndicator.setViewPager(splashViewpager);
//            splashViewpager.setPageTransformer(true, (page, position) -> {
//                LogUtil.d("page:" + page.getId() + "    positon:" + position);
//                if (page.getId() == R.id.splash_last && position < 0.5) {
//                    LogUtil.e("gologin");
//                    splashViewpager.setPageTransformer(true, null);
//                    goLogin(1);
//                }
//            });
        } else {
            goLogin(0);
        }

    }


    @UiThread
    public void goLogin(int registe) {
        Intent toLogin = new Intent(this, LoginActivity.class);
        toLogin.putExtra("isRegiste", registe);
        startActivity(toLogin);
        this.finish();
    }

    @OnClick({
            R.id.splash_login_btn,
            R.id.splash_registe_btn
    })
    public void OnBtnClick(View v) {
        if (v.getId() == R.id.splash_registe_btn) {
            goLogin(1);
        } else goLogin(0);

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (splashViewpager.getCurrentItem() != 3)
//            return super.dispatchTouchEvent(ev);
//        switch (MotionEventCompat.getActionMasked(ev)) {
//            case MotionEvent.ACTION_DOWN:
//                touchX = ev.getRawX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (ev.getRawY() - touchX < -50) {
//                    goLogin(1);
//                }
//                return true;
//        }
//        return super.dispatchTouchEvent(ev);
//
//    }
}
