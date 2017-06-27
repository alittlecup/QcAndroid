package com.qingchengfit.fitcoach.fragment.course.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.FlexableListFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.EmptyFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponseCoursePlan;
import com.qingchengfit.fitcoach.items.CoursePlanItem;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/2/22.
 */
public class CoursePlanHomeFragment extends BaseFragment {

    @BindView(R.id.tabview) TabLayout tabview;
    @BindView(R.id.vp) ViewPager vp;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private FlexableListFragment mGymPlanFragment;
    private FlexableListFragment mCustomPlanFragment;
    private EmptyFragment mEmptyFragment;
    private List<AbstractFlexibleItem> mCustomPlanData = new ArrayList<>();
    private List<AbstractFlexibleItem> mGymsPlanData = new ArrayList<>();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_plan_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbarTitle.setText("我的课件");
        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                addCoursePlan();
                return true;
            }
        });
        mGymPlanFragment = new FlexableListFragment();
        mEmptyFragment = new EmptyFragment();
        mCustomPlanFragment = new FlexableListFragment();
        vp.setAdapter(new PlanHomeAdapter(getChildFragmentManager()));
        tabview.setupWithViewPager(vp);
        mGymPlanFragment.customNoImage = R.drawable.no_statement;
        mGymPlanFragment.customNoStr = "场馆还没有添加任何课件";
        mCustomPlanFragment.customNoImage = R.drawable.no_statement;
        mCustomPlanFragment.customNoStr = "您还没有添加任何自定义课件";
        mGymPlanFragment.setItemClickListener(position -> {
            if (mGymsPlanData.size() > position) {
                WebActivity.startWeb(((CoursePlanItem) mGymsPlanData.get(position)).getUrl(), getContext());
            }
            return true;
        });
        mCustomPlanFragment.setItemClickListener(position -> {
            if (mCustomPlanData.size() > position) {
                WebActivity.startWeb(((CoursePlanItem) mCustomPlanData.get(position)).getUrl(), getContext());
            }
            return true;
        });

        return view;
    }

    @Override public void onResume() {
        super.onResume();
        showLoading();
        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoursePlanAll(App.coachid + "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseCoursePlan>() {
                @Override public void call(QcResponseCoursePlan qcResponse) {
                    hideLoading();
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.plans != null && qcResponse.data.plans.size() > 0) {
                            mCustomPlanData.clear();
                            mGymsPlanData.clear();
                            for (int i = 0; i < qcResponse.data.plans.size(); i++) {
                                if (qcResponse.data.plans.get(i).type == 1) {
                                    //自定义的计划
                                    mCustomPlanData.add(new CoursePlanItem(qcResponse.data.plans.get(i)));
                                } else if (qcResponse.data.plans.get(i).type == 2) {
                                    //场馆同步的
                                    mGymsPlanData.add(new CoursePlanItem(qcResponse.data.plans.get(i)));
                                }
                            }
                        }
                        mCustomPlanFragment.setData(mCustomPlanData);
                        mGymPlanFragment.setData(mGymsPlanData);
                        mCustomPlanFragment.freshView();
                        mGymPlanFragment.freshView();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    hideLoading();
                }
            }));
    }

    private void addCoursePlan() {
        Intent toWeb = new Intent(getContext(), WebActivity.class);
        toWeb.putExtra("url", Configs.Server + "mobile/coaches/add/plans/");
        startActivityForResult(toWeb, 10001);
    }

    @Override public String getFragmentName() {
        return CoursePlanHomeFragment.class.getName();
    }

    class PlanHomeAdapter extends FragmentStatePagerAdapter {

        public PlanHomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return mEmptyFragment;
                case 2:
                    return mCustomPlanFragment;
                default:
                    return mGymPlanFragment;
            }
        }

        @Override public int getCount() {
            return 3;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "学习培训";
                case 2:
                    return "自定义";
                default:
                    return "场馆";
            }
        }
    }
}
