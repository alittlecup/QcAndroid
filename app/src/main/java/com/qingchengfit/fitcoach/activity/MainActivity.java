package com.qingchengfit.fitcoach.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneInfoUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.RecievePush;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout;
import com.qingchengfit.fitcoach.component.DrawerModuleItem;
import com.qingchengfit.fitcoach.component.SegmentLayout;
import com.qingchengfit.fitcoach.fragment.OriginWebFragment;
import com.qingchengfit.fitcoach.fragment.WebFragment;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.DrawerGuide;
import com.qingchengfit.fitcoach.http.bean.DrawerModule;
import com.qingchengfit.fitcoach.http.bean.User;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;

//import javax.inject.Inject;

public class MainActivity extends BaseAcitivity {

    public static final String ACTION = "main_action";
    public static final int LOGOUT = 0;
    public static final int NOTIFICATION = 1;
    private static final String TAG = MainActivity.class.getName();
    private static int ids[] = {
            R.id.segmentbtn_0,
            R.id.segmentbtn_1,
            R.id.segmentbtn_2,
            R.id.segmentbtn_3,
            R.id.segmentbtn_4,
            R.id.segmentbtn_5,
            R.id.segmentbtn_6,
            R.id.segmentbtn_7,
            R.id.segmentbtn_8,
            R.id.segmentbtn_9,
    };
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
    CustomSetmentLayout drawerRadiogroup;
    @Bind(R.id.header_icon)
    ImageView headerIcon;
    @Bind(R.id.drawer_modules)
    LinearLayout drawerModules;
    @Bind(R.id.drawer_name)
    TextView drawerName;
    HashMap<String, Fragment> fragments = new HashMap<>();
    //    @Bind(R.id.main_navi)
//    NavigationView mainNavi;
    private User user;
    //    private WebFragment xWalkFragment;
//    private MyHomeFragment myHomeFragment;
    private Fragment topFragment;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();
    private String pathOri;
    private MaterialDialog dialog;
    private Gson gson;
    private Observable mMainObservabel;
    private MaterialDialog updateDialog;
    private MaterialDialog downloadDialog;
    private String url;
    private File newAkp;
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
        mMainObservabel = RxBus.getBus().register(RxBus.OPEN_DRAWER);
        mMainObservabel.subscribe((Action1) o -> mainDrawerlayout.openDrawer(Gravity.LEFT));
        initUser();
        initDialog();
        initDrawer();


    }

    private void initVersion() {
        updateDialog = new MaterialDialog.Builder(this)
                .title("前方发现新版本!!")
                .content("是否马上更新?")
                .positiveText("更新")
                .negativeText("下次再说")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        updateDialog.dismiss();

                        if (url != null) {
                            //TODO download app
                            downloadDialog.show();
                            new AsyncDownloader().execute(url);
                        }

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        updateDialog.dismiss();
                    }
                })
                .build();
        downloadDialog = new MaterialDialog.Builder(this)
                .title("正在飞速为您下载")
                .progress(false, 100)
                .build();

        LogUtil.e("version:" + AppUtils.getAppVer(this));
        QcCloudClient.getApi().getApi.qcGetVersion()
                .subscribe(qcVersionResponse -> {
                    if (qcVersionResponse.getData().getVersion().getAndroid().getRelease() > AppUtils.getAppVerCode(getApplication())) {
                        url = qcVersionResponse.getData().getDownload().getAndroid();
                        newAkp = new File(Configs.ExternalCache + getString(R.string.app_name) + "_" + qcVersionResponse.getData().getVersion().getAndroid().getVersion() + ".apk");
                        if (!newAkp.exists()) {
                            try {
                                newAkp.createNewFile();
                            } catch (IOException e) {
//e.printStackTrace();
                            }
                        }
                        runOnUiThread(() -> updateDialog.show());

                    }

                });
    }

    private void initUser() {
        String u = PreferenceUtils.getPrefString(this, "user_info", "");
        if (!TextUtils.isEmpty(u)) {
            user = gson.fromJson(u, User.class);
            App.setgUser(user);
        } else {
            //TODO ERROR
        }
        drawerName.setText(user.username);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra(ACTION, -1) == LOGOUT) {
            logout();
        } else if (intent.getIntExtra(ACTION, -1) == NOTIFICATION) {
            if (gson == null) {
                gson = new Gson();
            }
            String contetn = intent.getStringExtra("content");
            if (!TextUtils.isEmpty(contetn)) {
                RecievePush recievePush = gson.fromJson(contetn, RecievePush.class);

                goXwalkfragment(recievePush.url, null);
            }
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
        Intent logout = new Intent(this, LoginActivity.class);
        logout.putExtra("isRegiste", 0);

        startActivity(logout);

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

    }

    public void goXwalkfragment(String url, String from) {


        WebFragment fragment = WebFragment.newInstance(url);

        if (urls.contains(url)) {
            topFragment = fragment;
            if (path.size() > 0) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                for (String str : path) {
                    Fragment f = mFragmentManager.findFragmentByTag(str);
                    if (f != null)
                        ft.remove(f);
                }
                ft.commit();

            }
            path.clear();
        } else {
            path.add(url);
        }

        FragmentTransaction fr = mFragmentManager.beginTransaction();
        fr.replace(R.id.main_fraglayout, fragment, url);
        fr.addToBackStack(null);
        fr.commit();
//        }else
//            mFragmentManager.popBackStack(url, 0);


//        WebFragment fragment = (WebFragment) mFragmentManager.findFragmentByTag(url);
//        if (fragment == null) {
//        WebFragment fragment = WebFragment.newInstance(url);
//            FragmentTransaction fr = mFragmentManager.beginTransaction();
//            fr.replace(R.id.main_fraglayout, fragment, url);
//            fr.addToBackStack(url);
//        fr.commit();

//        } else {
//            mFragmentManager.beginTransaction().hide(topFragment).show(fragment).commit();
//            if (fragment instanceof XWalkFragment)
//                ((XWalkFragment) fragment).startLoadUrl(url);
//            else if (fragment instanceof OriginWebFragment) {
//                ((OriginWebFragment) fragment).startLoadUrl(url);
//            }
//        }
//        topFragment = fragment;
    }

    /**
     * 初始化侧滑,从后台拉去
     */
    private void initDrawer() {
        LogUtil.d(PhoneInfoUtils.getHandSetInfo());
        int gender = R.drawable.img_default_female;
        if (user.gender == 0)
            gender = R.drawable.img_default_male;
        if (TextUtils.isEmpty(user.avatar)) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .asBitmap()
                    .into(new CircleImgWrapper(headerIcon, App.AppContex));
        } else {
            Glide.with(App.AppContex)
                    .load(user.avatar)
                    .asBitmap()
                    .into(new CircleImgWrapper(headerIcon, App.AppContex));
        }


        String id = PreferenceUtils.getPrefString(this, "coach", "");
        if (TextUtils.isEmpty(id)) {
            //TODO error
        }
        Coach coach = gson.fromJson(id, Coach.class);
        App.coachid = Integer.parseInt(coach.id);
        QcCloudClient.getApi().getApi
                .getDrawerInfo(coach.id)
                .flatMap(qcResponDrawer -> {
                    if (qcResponDrawer.status == 200) {
                        for (int i = 0; i < qcResponDrawer.data.guide.size(); i++) {
                            setupBtn(qcResponDrawer.data.guide.get(i), i);
                        }
                        runOnUiThread(() -> {
                            setupModules(qcResponDrawer.data.modules);
                        });
                    } else if (qcResponDrawer.error_code.equals("400001")) {
                        logout();
                    }
                    return Observable.just("");
                })
                .delay(1, TimeUnit.SECONDS)
                .subscribe(s -> runOnUiThread(() -> {
                    initVersion();
                }));

    }

    public void guideTo(String url) {
        if (TextUtils.isEmpty(url)) {
            if (drawerRadiogroup.getChildCount() > 0) {
                runOnUiThread(() -> {
                    drawerRadiogroup.getChildAt(0).performClick();
                });

            }
        } else {
            goXwalkfragment(url, "");
        }
    }


    @UiThread
    private void setupModules(List<DrawerModule> modules) {
        for (DrawerModule module : modules) {
            DrawerModuleItem item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
            item.setTitle(module.title);
            item.setCount(module.text);
            item.setOnClickListener(view -> {
                goXwalkfragment(module.url, null);
                mainDrawerlayout.closeDrawers();
            });
            urls.add(module.url);
            drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        }
    }

    public void setupBtn(DrawerGuide btnInfo, int count) {
        SegmentLayout button = new SegmentLayout(this);

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
                            Response response = httpClient.newCall(request).execute();
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
//                        StateListDrawable drawable1 = DynamicSelector.getSelector(drawables.get(0), drawables.get(1));
                        button.setText(btnInfo.guideText);
                        button.setDrawables(drawables.toArray(new Drawable[2]));
//                        button.setButtonDrawable(drawable1);
//                        button.setPadding(MeasureUtils.dpToPx(15f, getResources()), 0, 0, 0);
                        button.setId(ids[count]);
                        button.setListener(view -> {
                            mainDrawerlayout.closeDrawer(Gravity.LEFT);
                            goXwalkfragment(btnInfo.intentUrl, null);
                        });
                        urls.add(btnInfo.intentUrl);
//                        fragments.put( btnInfo.intentUrl,  WebFragment.newInstance(btnInfo.intentUrl));
                        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));

                        if (count == 0) {
//                            drawerRadiogroup.getChildAt(0).performClick();
                            drawerRadiogroup.check(0);
                            mFragmentManager.beginTransaction().replace(R.id.main_fraglayout, XWalkFragment.newInstance(btnInfo.intentUrl), btnInfo.intentUrl)
                                    .commit();
                        }
                    });

                })
        ;
    }
// public void setupBtn(DrawerGuide btnInfo, int count) {
//        SegmentButton button = new SegmentButton(this);
//
//        List<Drawable> drawables = new ArrayList<>();
//        Observable.just(btnInfo.drawableOff, btnInfo.drawableOn)
//                .flatMap(s -> {
//                    File f = new File(Configs.ExternalPath, s.substring(s.length() - 20, s.length()));
//                    if (!f.exists()) {
////                        Response response = QcCloudClient.getApi().downLoadApi
////                                .qcDownload(s);
//                        OkHttpClient httpClient = new OkHttpClient();
//                        Request request = new Request.Builder().url(s).build();
//
//                        try {
//                            Response response = httpClient.newCall(request).execute();
//                            FileUtils.getFileFromBytes(response.body().bytes(), f.getAbsolutePath());
////                            FileOutputStream output = new FileOutputStream(f);
////                            IOUtils.write(response.body().bytes(), output);
////                            FileUtils.copyInputStreamToFile(response.body().byteStream(),f);
//
//                        } catch (FileNotFoundException e) {
//                            RevenUtils.sendException("initDrawer", TAG, e);
//                        } catch (IOException e) {
//                            RevenUtils.sendException("initDrawer", TAG, e);
//                        }
//                    }
//                    return Observable.just(f.getAbsolutePath());
//                })
//                .flatMap(s2 -> {
//                            drawables.add(Drawable.createFromPath(s2));
//                            return Observable.just("");
//                        }
//                )
//                .last()
//                .subscribe(s1 -> {
//                    runOnUiThread(() -> {
//                        StateListDrawable drawable1 = DynamicSelector.getSelector(drawables.get(0), drawables.get(1));
//                        button.setText(btnInfo.guideText);
//                        button.setButtonDrawable(drawable1);
//                        button.setPadding(MeasureUtils.dpToPx(15f, getResources()), 0, 0, 0);
//                        button.setOnClickListener(view -> {
//                            mainDrawerlayout.closeDrawer(Gravity.LEFT);
//                            goXwalkfragment(btnInfo.intentUrl);
//                        });
//                        urls.add(btnInfo.intentUrl);
////                        fragments.put( btnInfo.intentUrl,  WebFragment.newInstance(btnInfo.intentUrl));
//                        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
//                        if (count == 0) {
//                            drawerRadiogroup.getChildAt(0).performClick();
//                        }
//                    });
//
//                })
//        ;
//    }

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
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
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
        RxBus.getBus().unregister(RxBus.OPEN_DRAWER, mMainObservabel);
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }

    @Override
    public void onBackPressed() {
        if (topFragment == null || topFragment.getTag() == null || urls.size() == 0) {
            dialog.show();
            return;
        }

        if (path.size() > 0) {
            mFragmentManager.popBackStack();
            mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(path.get(path.size() - 1))).commit();
            path.remove(path.size() - 1);
        } else {
            if (topFragment.getTag().equalsIgnoreCase(urls.get(0))) {
                dialog.show();
            } else {
                if (drawerRadiogroup.getChildCount() > 0)
                    drawerRadiogroup.getChildAt(0).performClick();
            }
        }
//        super.onBackPressed();
//       if (mFragmentManager.getFragments().size() >1)
//           mFragmentManager.popBackStack();
//
//       else
//           dialog.show();
//
//    }


        //    @Override
//    public void onBackPressed() {
//        mainDrawerlayout.closeDrawers();
//        if (topFragment == null) {
//            this.finish();
//            return;
//        }


//        if (topFragment instanceof XWalkFragment) {
//            if (((XWalkFragment) topFragment).canGoBack()) {
//                ((XWalkFragment) topFragment).goBack();
//                return;
//            }
//
//        } else if (topFragment instanceof OriginWebFragment) {
//            if (((OriginWebFragment) topFragment).canGoBack()) {
//                ((OriginWebFragment) topFragment).goBack();
//                return;
//            }
//        }

//        if (topFragment.getTag() != null && topFragment.getTag().endsWith(urls.get(0))) {
//            dialog.show();
//        } else {
//            if (drawerRadiogroup.getChildCount() > 0)
//                drawerRadiogroup.getChildAt(0).performClick();
//        }
    }


    /**
     * 新版本下载
     */

    private class AsyncDownloader extends AsyncTask<String, Long, Boolean> {
//        private final String URL = "file_url";

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient httpClient = new OkHttpClient();
            Call call = httpClient.newCall(new Request.Builder().url(params[0]).get().build());
            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    InputStream inputStream = null;
                    OutputStream output = new FileOutputStream(newAkp);
                    try {
                        inputStream = response.body().byteStream();
                        byte[] buff = new byte[1024 * 4];
                        long downloaded = 0;
                        long target = response.body().contentLength();

                        publishProgress(0L, target);
                        while (true) {
                            int readed = inputStream.read(buff);
                            if (readed == -1) {
                                break;
                            }
                            output.write(buff, 0, readed);
                            output.flush();
                            //write buff
                            downloaded += readed;
                            publishProgress(downloaded, target);
                            if (isCancelled()) {
                                return false;
                            }
                        }
//                        FileUtils.getFileFromBytes(response.body().bytes(), newAkp.getAbsolutePath());
                        return downloaded == target;
                    } catch (IOException ignore) {
                        return false;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (output != null) {
                            output.close();
                        }
                    }
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            downloadDialog.setProgress((int) (values[0] * 100 / values[1]));

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                downloadDialog.dismiss();
                AppUtils.install(MainActivity.this, newAkp.getAbsolutePath());
                //TODO 安装应用
            } else {
                downloadDialog.dismiss();
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
