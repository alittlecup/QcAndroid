package com.qingchengfit.fitcoach.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DynamicSelector;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.paper.paperbaselibrary.utils.PhoneInfoUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.component.DrawerModuleItem;
import com.qingchengfit.fitcoach.component.SegmentButton;
import com.qingchengfit.fitcoach.fragment.MyHomeFragment;
import com.qingchengfit.fitcoach.fragment.OriginWebFragment;
import com.qingchengfit.fitcoach.fragment.WebFragment;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.DrawerGuide;
import com.qingchengfit.fitcoach.http.bean.DrawerModule;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

//import javax.inject.Inject;

public class MainActivity extends BaseAcitivity {

    public static final String ACTION = "main_action";
    public static final int LOGOUT = 0;
    private static final String TAG = MainActivity.class.getName();
    ////    @Bind(R.id.float_btn)
//    FloatingActionButton mFloatBtn;
    FragmentManager mFragmentManager;
    @Bind(R.id.main_drawerlayout)
    DrawerLayout mainDrawerlayout;
    @Bind(R.id.main_fraglayout)
    FrameLayout mainFraglayout;
    @Bind(R.id.drawer_headerview)
    RelativeLayout drawerHeaderview;
    @Bind(R.id.drawer_radiogroup)
    RadioGroup drawerRadiogroup;
    @Bind(R.id.header_icon)
    SimpleDraweeView headerIcon;
    @Bind(R.id.drawer_modules)
    LinearLayout drawerModules;
//    @Bind(R.id.main_navi)
//    NavigationView mainNavi;

    private WebFragment xWalkFragment;
    private MyHomeFragment myHomeFragment;
    private Fragment topFragment;
    private ArrayList<String> urls = new ArrayList<>();
    private MaterialDialog dialog;
    private Gson gson;

//    @Override
//    protected void onXWalkReady() {
//
//    }

    //    @Inject RxBus rxBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gson = new Gson();
        mFragmentManager = getSupportFragmentManager();

        RxBus.getBus().toObserverable().subscribe(o -> {
            if (o instanceof OpenDrawer) {
                mainDrawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        initDialog();
        initDrawer();
        myHomeFragment = new MyHomeFragment();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra(ACTION, -1) == LOGOUT) {
            logout();
        }
    }

    public void logout() {
        PreferenceUtils.setPrefString(App.AppContex, "session_id", null);
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof XWalkFragment) {
                    ((XWalkFragment) fragment).removeCookie();
                } else if (fragment instanceof OriginWebFragment) {
                    ((OriginWebFragment) fragment).removeCookie();
                }
            }
        }
        startActivity(new Intent(this, LoginActivity.class));

        this.finish();
    }

    public void initDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title("是否确认退出?")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .positiveText("退出")
                .negativeText("不要")
                .build();

//        dialog.setTitle("是否确定退出?");
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setMessage("");
//        dialog.setPositiveButton("是",v ->{dialog.dismiss(); this.finish();});
//        dialog.setNegativeButton("否",v -> dialog.dismiss());
    }


    private void goXwalkfragment(String url) {
        WebFragment fragment = (WebFragment) mFragmentManager.findFragmentByTag(url);
        if (fragment == null) {
            fragment = WebFragment.newInstance(url);
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fraglayout, fragment, url)
                    .show(fragment)
                    .commit();

        } else {
            if (topFragment != fragment)
                mFragmentManager.beginTransaction().hide(topFragment).show(fragment).commit();
        }
        topFragment = fragment;
//        fragment.startLoadUrl(url);
    }

    /**
     * 初始化侧滑,从后台拉去
     */
    private void initDrawer() {
        LogUtil.d(PhoneInfoUtils.getHandSetInfo());
        String id = PreferenceUtils.getPrefString(this, "coach", "");
        if (TextUtils.isEmpty(id)) {
            //TODO error
        }
        Coach coach = gson.fromJson(id, Coach.class);
        QcCloudClient.getApi().getApi
                .getDrawerInfo(coach.id)
                .subscribe(qcResponDrawer -> {
                    for (int i = 0; i < qcResponDrawer.data.guide.size(); i++) {
                        setupBtn(qcResponDrawer.data.guide.get(i), i);
                    }
                    runOnUiThread(() -> {
                        setupModules(qcResponDrawer.data.modules);
                    });
                });

    }

    @UiThread
    private void setupModules(List<DrawerModule> modules) {
        for (DrawerModule module : modules) {
            DrawerModuleItem item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
            item.setTitle(module.title);
            item.setCount(module.text);
            item.setOnClickListener(view -> {
                goXwalkfragment(module.url);
                mainDrawerlayout.closeDrawers();
            });
            drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        }
    }

    public void setupBtn(DrawerGuide btnInfo, int count) {
        SegmentButton button = new SegmentButton(this);
        List<Drawable> drawables = new ArrayList<>();
        Observable.just(btnInfo.drawableOff, btnInfo.drawableOn)
                .flatMap(s -> {
                    File f = new File(Configs.ExternalPath, s.substring(s.length() - 20, s.length()));
                    if (!f.exists()) {
//                        Response response = QcCloudClient.getApi().downLoadApi
//                                .qcDownload(s);
                        OkHttpClient httpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(s).build();

                        try {
                            com.squareup.okhttp.Response response = httpClient.newCall(request).execute();
                            FileUtils.getFileFromBytes(response.body().bytes(), f.getAbsolutePath());
//                            FileOutputStream output = new FileOutputStream(f);
//                            IOUtils.write(response.body().bytes(), output);
//                            FileUtils.copyInputStreamToFile(response.body().byteStream(),f);

                        } catch (FileNotFoundException e) {
                            RevenUtils.sendException("initDrawer", TAG, e);
                        } catch (IOException e) {
                            RevenUtils.sendException("initDrawer", TAG, e);
                        }
                    }
                    return Observable.just(f.getAbsolutePath());
                })
                .flatMap(s2 -> {
                            drawables.add(Drawable.createFromPath(s2));
                            return Observable.just("");
                        }
                )
                .last()
                .subscribe(s1 -> {
                    runOnUiThread(() -> {
                        StateListDrawable drawable1 = DynamicSelector.getSelector(drawables.get(0), drawables.get(1));
                        button.setText(btnInfo.guideText);
                        button.setButtonDrawable(drawable1);
                        button.setPadding(MeasureUtils.dpToPx(15f, getResources()), 0, 0, 0);
                        button.setOnClickListener(view -> {
                            mainDrawerlayout.closeDrawer(Gravity.LEFT);
                            goXwalkfragment(btnInfo.intentUrl);
                        });
                        urls.add(btnInfo.intentUrl);
                        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
                        if (count == 0) {
                            drawerRadiogroup.getChildAt(0).performClick();
                        }
                    });

                })
        ;
    }

    @OnClick(R.id.drawer_headerview)
    public void onHeadClick() {

//        if (mFragmentManager.getFragments().contains(myHomeFragment)) {
//            mFragmentManager.beginTransaction().hide(topFragment).show(myHomeFragment).commit();
//        } else {
//            mFragmentManager.beginTransaction()
//                    .add(R.id.main_fraglayout, myHomeFragment)
//                    .show(myHomeFragment)
//                    .commit();
//        }
//        topFragment = myHomeFragment;
        startActivity(new Intent(this, MyHomeActivity.class));
//        mainDrawerlayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainDrawerlayout.closeDrawers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }


    @Override
    public void onBackPressed() {
        mainDrawerlayout.closeDrawers();

        if (topFragment instanceof XWalkFragment) {
            if (((XWalkFragment) topFragment).canGoBack()) {
                ((XWalkFragment) topFragment).goBack();
                return;
            }

        } else if (topFragment instanceof OriginWebFragment) {
            if (((OriginWebFragment) topFragment).canGoBack()) {
                ((OriginWebFragment) topFragment).goBack();
                return;
            }
        }

        if (topFragment.getTag() != null && topFragment.getTag().endsWith(urls.get(0))) {
            dialog.show();
        } else {
            if (drawerRadiogroup.getChildCount() > 0)
                drawerRadiogroup.getChildAt(0).performClick();
        }
    }
}
