package com.qingchengfit.fitcoach.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.paper.paperbaselibrary.utils.DynamicSelector;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.component.SegmentButton;
import com.qingchengfit.fitcoach.fragment.MyHomeFragment;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponse;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
//    @Bind(R.id.main_navi)
//    NavigationView mainNavi;

    private CompositeSubscription _subscriptions;

    //    @Inject RxBus rxBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        XWalkFragment xWalkFragment = new XWalkFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fraglayout, xWalkFragment).commit();
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

    private void initDrawer() {

        List<Drawable> drawables = new ArrayList<>();
        Observable.just("")
                .flatMap(s -> {
                    File f = new File(Configs.ExternalPath + s);
                    if (!f.exists()) {
                        Response response = QcCloudClient.getApi().downLoadApi
                                .qcDownload("");

                        try {
                            FileOutputStream output = new FileOutputStream(f);
                            IOUtils.write((CharSequence) response.getBody(), output);
                            output.close();

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
                    StateListDrawable drawable = DynamicSelector.getSelector(drawables.get(0), drawables.get(1));

                })
        ;

//        QcCloudClient.getApi()
//                .downLoadApi
//                .qcDownload("/header/123123/IMG_20150812_182222716.jpg")
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(response -> {
//
//                });


        SegmentButton button = new SegmentButton(this);
        button.setText("测试");
        button.setButtonDrawable(DynamicSelector.getSelector(getResources().getDrawable(R.drawable.ic_drawer_meeting_normal), getResources().getDrawable(R.drawable.ic_drawer_meeting_checked)));
        button.setPadding(15, 0, 0, 0);

        drawerRadiogroup.addView(button);


    }

    public void setupBtn() {

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
