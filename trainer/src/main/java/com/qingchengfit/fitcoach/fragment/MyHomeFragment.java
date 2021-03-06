package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MyHomeActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.HalfScrollView;
import com.qingchengfit.fitcoach.component.MyhomeViewPager;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHomeFragment extends Fragment {

	Toolbar toolbar;
	ImageView myhomeHeader;
	TabLayout myhomeTab;
	TextView myhomeName;
	TextView myhomeBrief;
	FrameLayout myhomeStudentJudge;
	HalfScrollView myhomeScroller;
	View myhomeBg;
	ImageView myhomeGender;
	TextView myhomeLocation;
	ImageView myhomeSawtooth;
	RelativeLayout myhomeTabLayout;
	MyhomeViewPager myhomeViewpager;
	LinearLayout halfscrollFirst;
	SwipeRefreshLayout sfl;
    private int mHomeBgHeight = 1;
    private boolean isFresh = false;
    private Gson gson;
    //    @BindView(R.id.myhome_coolaosingtoorbar)
    //    CollapsingToolbarLayout myhomeCoolaosingtoorbar;
    private FragmentCallBack fragmentCallBack;
    private Observable<QcMyhomeResponse> qcMyhomeResponseObservable;
    private FragmentAdatper adatper;
    private QcMyhomeResponse qcMyhomeResponse;

    Observer<QcMyhomeResponse> qcMyhomeResponseObserver = new Observer<QcMyhomeResponse>() {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {

        }

        @Override public void onNext(QcMyhomeResponse qcMyhomeResponse)

        {
            MyHomeFragment.this.qcMyhomeResponse = qcMyhomeResponse;
            handleResponse(qcMyhomeResponse);
        }
    };


    public MyHomeFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_home_test, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      myhomeHeader = (ImageView) view.findViewById(R.id.myhome_header);
      myhomeTab = (TabLayout) view.findViewById(R.id.myhome_tab);
      myhomeName = (TextView) view.findViewById(R.id.myhome_name);
      myhomeBrief = (TextView) view.findViewById(R.id.myhome_brief);
      myhomeStudentJudge = (FrameLayout) view.findViewById(R.id.myhome_student_judge);
      myhomeScroller = (HalfScrollView) view.findViewById(R.id.myhome_scroller);
      myhomeBg = (View) view.findViewById(R.id.myhome_bg);
      myhomeGender = (ImageView) view.findViewById(R.id.myhome_gender);
      myhomeLocation = (TextView) view.findViewById(R.id.myhome_location);
      myhomeSawtooth = (ImageView) view.findViewById(R.id.myhome_sawtooth);
      myhomeTabLayout = (RelativeLayout) view.findViewById(R.id.myhome_tab_layout);
      myhomeViewpager = (MyhomeViewPager) view.findViewById(R.id.myhome_viewpager);
      halfscrollFirst = (LinearLayout) view.findViewById(R.id.halfscroll_first);
      sfl = (SwipeRefreshLayout) view.findViewById(R.id.sfl);
      view.findViewById(R.id.myhome_student_judge).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onJudge();
        }
      });

      gson = new Gson();
        toolbar.setTitle("我的主页");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {
            ((MyHomeActivity) getActivity()).openDrawer();
        });
        toolbar.inflateMenu(R.menu.menu_myhome);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_myhome_share) {
                StringBuffer sb = new StringBuffer();
                sb.append(Configs.Server).append("mobile/coaches/").append(App.coachid).append("/share/index/");
                if (qcMyhomeResponse != null) {
                    ShareDialogFragment.newInstance(qcMyhomeResponse.getData().getCoach().getUsername() + "的教练主页",
                        qcMyhomeResponse.getData().getCoach().getShort_description(), qcMyhomeResponse.getData().getCoach().getAvatar(),
                        sb.toString()).show(getFragmentManager(), "");
                }
            }
            return true;
        });
        initUser();

        ViewTreeObserver observer = myhomeViewpager.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = myhomeViewpager.getLayoutParams();
                int hei = MeasureUtils.getTrueheight(getActivity()) - toolbar.getHeight() - myhomeTab.getHeight();
                lp.height = hei;
                myhomeViewpager.setLayoutParams(lp);
                mHomeBgHeight = myhomeBg.getHeight();
                myhomeViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        //toolbar 变透明,保留,以后版本需要
        //        myhomeScroller.setListener(new HalfScrollView.HalfViewListener() {
        //            @Override
        //            public void onScroll(int i) {
        //                if (i > mHomeBgHeight)
        //                    i = mHomeBgHeight;
        //                else if (i < 0)
        //                    i = 0;
        //                Drawable drawable = new ColorDrawable(getResources().getColor(R.color.primary));
        //                drawable.setAlpha(255 * i / mHomeBgHeight);
        //                if (Build.VERSION.SDK_INT < 16) {
        //                    toolbar.setBackgroundDrawable(drawable);
        //                } else toolbar.setBackground(drawable);
        //            }
        //        });

        //        Glide.with(App.AppContex).load(R.drawable.img_selfinfo_bg).into(myhomeBg);
        initSRL();
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        if (isFresh) {
            TrainerRepository.getStaticTrainerAllApi().qcGetDetail(Integer.toString(App.coachid))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcMyhomeResponseObserver);
            isFresh = false;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) isFresh = true;
        //            initUser();

    }

    public void initSRL() {
        sfl.setColorSchemeResources(R.color.primary);
        sfl.setProgressViewOffset(true, MeasureUtils.dpToPx(50f, getResources()), MeasureUtils.dpToPx(70f, getResources()));
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                initUser();
            }
        });
    }

    public void handleResponse(QcMyhomeResponse qcMyhomeResponse) {
        myhomeLocation.setText(qcMyhomeResponse.getData().getCoach().getCity());
        if (TextUtils.isEmpty(qcMyhomeResponse.getData().getCoach().getShort_description())) {
            myhomeBrief.setText(getString(R.string.myhome_default_desc));
        } else {
            myhomeBrief.setText(qcMyhomeResponse.getData().getCoach().getShort_description());
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseInfoFragment.newInstance(gson.toJson(qcMyhomeResponse.getData().getCoach()), ""));
        fragments.add(new RecordComfirmFragment());
        fragments.add(new WorkExperienceFragment());
        fragments.add(new StudentEvaluateFragment());
        adatper = new FragmentAdatper(getChildFragmentManager(), fragments);
        StudentJudgeFragment fragment = StudentJudgeFragment.newInstance(qcMyhomeResponse.getData().getCoach().getTagArray(),
            qcMyhomeResponse.getData().getCoach().getEvaluate());
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
        //        String key = CacheUtils.hashKeyForDisk(App.coachid+"_cache_myhome");
        sfl.setRefreshing(false);
    }

 public void onJudge() {
        //        myhomeScroller.fullScroll(View.FOCUS_DOWN);
        myhomeScroller.smoothScrollTo(0, myhomeScroller.getHeight());
        myhomeTab.setScrollPosition(3, 0, true);

        myhomeViewpager.setCurrentItem(3);
    }

    public void initHead(String userAvatar, int userGender) {
        int gender = R.drawable.img_default_female;

        Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_female).into(myhomeGender);
        if (userGender == 0) {
            gender = R.drawable.img_default_male;
            Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_male).diskCacheStrategy(DiskCacheStrategy.ALL).into(myhomeGender);
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

    private void initUser() {
        String cache = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "_cache_myhome", "");
        if (!TextUtils.isEmpty(cache)) {
            this.qcMyhomeResponse = gson.fromJson(cache, QcMyhomeResponse.class);
            if (qcMyhomeResponse != null) handleResponse(qcMyhomeResponse);
        }
        qcMyhomeResponseObservable =
            TrainerRepository.getStaticTrainerAllApi().qcGetDetail(Integer.toString(App.coachid)).observeOn(AndroidSchedulers.mainThread());
        qcMyhomeResponseObservable.subscribe(qcMyhomeResponseObserver);
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    public interface TouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    class FragmentAdatper extends FragmentStatePagerAdapter {

        List<Fragment> fragments;
        FragmentManager fm;

        public FragmentAdatper(FragmentManager fm, ArrayList<Fragment> fs) {
            super(fm);
            this.fragments = fs;
            this.fm = fm;
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

        @Override public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override public int getCount() {
            return fragments.size();
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "基本信息";
                case 1:
                    return "资质认证";
                case 2:
                    return "工作经历";
                case 3:
                    return "学员评价";
                default:
                    return "";
            }
        }
    }
}
