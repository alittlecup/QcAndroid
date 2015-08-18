package com.qingchengfit.fitcoach.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.paper.paperbaselibrary.utils.DynamicSelector;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.paper.paperbaselibrary.utils.PhoneInfoUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.component.DrawerModuleItem;
import com.qingchengfit.fitcoach.component.SegmentButton;
import com.qingchengfit.fitcoach.fragment.MyHomeFragment;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.DrawerGuide;
import com.qingchengfit.fitcoach.http.bean.DrawerModule;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

//import javax.inject.Inject;

public class MainActivity extends BaseAcitivity implements Callback<QcResponse> {

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

    private CompositeSubscription _subscriptions;
    private XWalkFragment xWalkFragment;

    //    @Inject RxBus rxBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        xWalkFragment = new XWalkFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fraglayout, xWalkFragment).commit();
        _subscriptions = new CompositeSubscription();
        RxBus.getBus().toObserverable().subscribe(o -> {
            if (o instanceof OpenDrawer) {
                mainDrawerlayout.openDrawer(Gravity.LEFT);
            }
        });

        initDrawer();


//        View view = View.inflate(this,R.layout.drawer_header,null);
//        mainNavi.addHeaderView(view);
//        view.setOnClickListener(view1 ->
//                mFragmentManager.beginTransaction()
//                        .replace(R.id.main_fraglayout,new MyHomeFragment())
//                        .commit()
//        );
    }

    private void goXwalkfragment(String url) {
        mFragmentManager.beginTransaction().show(xWalkFragment).commit();
        xWalkFragment.startLoadUrl(url);
    }


    private void initDrawer() {

        LogUtil.d(PhoneInfoUtils.getHandSetInfo());
        QcCloudClient.getApi().getApi
                .getDrawerInfo()
                .subscribe(qcResponDrawer -> {
                    for (int i = 0; i < qcResponDrawer.data.guide.size(); i++) {
                        setupBtn(qcResponDrawer.data.guide.get(i));

                    }
                    setupModules(qcResponDrawer.data.modules);
                });

    }

    private void setupModules(List<DrawerModule> modules) {
        for (DrawerModule module : modules) {
            DrawerModuleItem item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
            item.setTitle(module.title);
            item.setCount(module.text);
            item.setOnClickListener(view -> goXwalkfragment(module.url));
            drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        }
    }

    public void setupBtn(DrawerGuide btnInfo) {
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
                            LogUtil.d("toSomeWhere");
                            goXwalkfragment(btnInfo.intentUrl);
                        });
                        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
                    });

                })
        ;

    }

    @OnClick(R.id.drawer_headerview)
    public void onHeadClick() {
        mainDrawerlayout.closeDrawers();
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fraglayout, new MyHomeFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }

    /**
     * http返回
     *
     * @param qcResponse http回调
     * @param response   返回头信息
     */
    @Override
    public void success(QcResponse qcResponse, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
