package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ShareUtils;
import com.qingchengfit.fitcoach.activity.MyHomeActivity;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.adapter.FragmentAdapter;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CustomSwipeRefreshLayout;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 15/12/16 2015.
 */
public class CoachHomeFragment extends Fragment implements CustomSwipeRefreshLayout.CanChildScrollUpCallback {

    @Bind(R.id.header)
    ImageView myhomeHeader;
    @Bind(R.id.gender)
    ImageView myhomeGender;
    @Bind(R.id.name)
    TextView myhomeName;
    @Bind(R.id.myhome_brief)
    TextView myhomeBrief;
    @Bind(R.id.myhome_location)
    TextView myhomeLocation;
    @Bind(R.id.myhome_sawtooth)
    ImageView myhomeSawtooth;
    @Bind(R.id.myhome_appBar)
    AppBarLayout myhomeAppBar;
    @Bind(R.id.tab)
    TabLayout myhomeTab;
    @Bind(R.id.student)
    ViewPager myhomeViewpager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sfl)
    CustomSwipeRefreshLayout sfl;
    @Bind(R.id.scroll_root)
    CoordinatorLayout scrollRoot;
    private Gson gson = new Gson();
    private int mAppBarOffset = 0;
    private Observable<QcMyhomeResponse> qcMyhomeResponseObservable;
    private QcMyhomeResponse qcMyhomeResponse;
    private FragmentAdapter adatper;


    Observer<QcMyhomeResponse> qcMyhomeResponseObserver = new Observer<QcMyhomeResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(QcMyhomeResponse qcMyhomeResponse)

        {
            CoachHomeFragment.this.qcMyhomeResponse = qcMyhomeResponse;
            handleResponse(qcMyhomeResponse);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_home, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("我的主页");
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> {
            ((MyHomeActivity) getActivity()).openDrawer();
        });
        toolbar.inflateMenu(R.menu.menu_myhome);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_myhome_settings) {
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), 333);
            } else if (item.getItemId() == R.id.action_myhome_share) {
                StringBuffer sb = new StringBuffer();
                sb.append(Configs.Server).append("mobile/coaches/").append(App.coachid).append("/share/index/");
                if (qcMyhomeResponse != null) {
                    ShareUtils.oneKeyShared(App.AppContex, sb.toString(), qcMyhomeResponse.getData().getCoach().getAvatar(),
                            qcMyhomeResponse.getData().getCoach().getShort_description()
                            , qcMyhomeResponse.getData().getCoach().getUsername() + "的教练主页");//分享
                    //
                }
            }
            return true;
        });
        initUser();
        myhomeAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LogUtil.e("verticalOffset:"+verticalOffset+"   "+scrollRoot.canScrollVertically(-1));
                mAppBarOffset = verticalOffset;
            }
        });
        initSRL();
        return view;

    }

    private void initUser() {
        String cache = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "_cache_myhome", "");
        if (!TextUtils.isEmpty(cache)) {
            this.qcMyhomeResponse = gson.fromJson(cache, QcMyhomeResponse.class);
            if (qcMyhomeResponse != null)
                handleResponse(qcMyhomeResponse);
        }
        qcMyhomeResponseObservable = QcCloudClient.getApi().getApi.qcGetDetail(Integer.toString(App.coachid))
                .observeOn(AndroidSchedulers.mainThread());
        qcMyhomeResponseObservable.subscribe(qcMyhomeResponseObserver);
    }

    public void initSRL() {
        sfl.setColorSchemeResources(R.color.primary);
        sfl.setProgressViewOffset(true, MeasureUtils.dpToPx(50f, getResources()), MeasureUtils.dpToPx(70f, getResources()));
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUser();
            }
        });
        sfl.setCanChildScrollUpCallback(this);
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (isFresh) {
        QcCloudClient.getApi().getApi.qcGetDetail(Integer.toString(App.coachid))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcMyhomeResponseObserver);
//            isFresh = false;
//        }
    }

    public void handleResponse(QcMyhomeResponse qcMyhomeResponse) {
        myhomeLocation.setText(qcMyhomeResponse.getData().getCoach().getCity());
        if (TextUtils.isEmpty(qcMyhomeResponse.getData().getCoach().getShort_description()))
            myhomeBrief.setText(getString(R.string.myhome_default_desc));
        else
            myhomeBrief.setText(qcMyhomeResponse.getData().getCoach().getShort_description());
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseInfoFragment.newInstance(gson.toJson(qcMyhomeResponse.getData().getCoach()), ""));
        fragments.add(new RecordComfirmFragment());
        fragments.add(new WorkExperienceFragment());
        fragments.add(new StudentEvaluateFragment());
        adatper = new FragmentAdapter(getChildFragmentManager(), fragments);
        StudentJudgeFragment fragment = StudentJudgeFragment.newInstance(qcMyhomeResponse.getData().getCoach().getTagArray()
                , qcMyhomeResponse.getData().getCoach().getEvaluate());
        getChildFragmentManager().beginTransaction().replace(R.id.myhome_student_judge, fragment).
                commit();

        myhomeViewpager.setAdapter(adatper);

        myhomeViewpager.setOffscreenPageLimit(4);
        myhomeViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(myhomeViewpager);
        myhomeName.setText(qcMyhomeResponse.getData().getCoach().getUsername());
        myhomeLocation.setText(qcMyhomeResponse.getData().getCoach().getDistrictStr());
        initHead(qcMyhomeResponse.getData().getCoach().getAvatar(), qcMyhomeResponse.getData().getCoach().getGender());//TODO
        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "_cache_myhome", gson.toJson(qcMyhomeResponse));
        sfl.setRefreshing(false);
    }

    public void initHead(String userAvatar, int userGender) {
        int gender = R.drawable.img_default_female;
        Glide.with(App.AppContex)
                .load(R.drawable.ic_gender_signal_female)
                .into(myhomeGender);
        if (userGender == 0) {
            gender = R.drawable.img_default_male;
            Glide.with(App.AppContex)
                    .load(R.drawable.ic_gender_signal_male)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myhomeGender);
        }
        if (TextUtils.isEmpty(userAvatar)) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CircleImgWrapper(myhomeHeader, App.AppContex));
        } else {
            Glide.with(App.AppContex)
                    .load(userAvatar)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CircleImgWrapper(myhomeHeader, App.AppContex));
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return mAppBarOffset != 0;

    }



}
