//package cn.qingchengfit.saasbase.student.views.followup;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//
//import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
//import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
//import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
//import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
//import cn.qingchengfit.views.fragments.BaseFilterFragment;
//import cn.qingchengfit.views.fragments.EmptyFragment;
//import rx.functions.Action0;
//import rx.functions.Action1;
//
///**
// * Created by huangbaole on 2017/11/7.
// */
//
//public class FollowUpFilterFragment extends BaseFilterFragment {
//    FilterListStringFragment studentStatusFragment;
//    FilterListStringFragment genderFragment;
//    FilterTimesFragment topDayFragment;
//    FilterTimesFragment dayFragment;
//    FollowUpTopSalerView topSalerView;
//    FollowUpTopSalerView salersView;
//
//    @Inject
//    FollowUpFilterModel model;
//
//    @Named("commonFilter")
//    @Inject
//    StudentFilter filter;
//    @Named("topFilter")
//    @Inject
//    StudentFilter topFilter;
//    private FilterTimesFragment filterTimesFragment;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        SaasbaseParamsInjector.inject(this);
//        initFragment();
//    }
//
//    @Override
//    public void dismiss() {
//        if (dimissAction == null) return;
//        dimissAction.call();
//    }
//
//    private void initFragment() {
//        studentStatusFragment = new FilterListStringFragment();
//        String[] status = new String[]{"全部", "新注册", "已接洽", "会员"};
//        studentStatusFragment.setStrings(status);
//        studentStatusFragment.setOnSelectListener(position -> {
//            model.studentStatus.set(position == 0 ? "会员状态" : status[position]);
//            filter.status = position == 0 ? null : String.valueOf(position - 1);
//            selectAction.call(0);
//        });
//
//        genderFragment = new FilterListStringFragment();
//        String[] genders = new String[]{"全部", "男", "女"};
//        genderFragment.setStrings(genders);
//        genderFragment.setOnSelectListener(position -> {
//            model.gender.set(position == 0 ? "性别" : genders[position]);
//            filter.gender = position == 0 ? null : String.valueOf(position - 1);
//            selectAction.call(0);
//        });
//        topDayFragment = FilterTimesFragment.getInstance(1, 30);
//        dayFragment = FilterTimesFragment.getInstance(0, -1);
//
//
//        topDayFragment.setSelectDayAction((start,end,title)->{
//            topFilter.registerTimeStart=start;
//            topFilter.registerTimeEnd=end;
//            model.topLatestDay.set(title);
//            selectAction.call(1);
//        });
//        dayFragment.setSelectDayAction((start,end,title)->{
//            filter.registerTimeStart=start;
//            filter.registerTimeEnd=end;
//            model.today.set(title);
//            selectAction.call(0);
//        });
//        topSalerView = new FollowUpTopSalerView();
//        topSalerView.setOnItemClick(staff -> {
//            model.topSalerName.set(staff == null ? "销售" : staff.username);
//            topFilter.sale = staff;
//            selectAction.call(1);
//        });
//        salersView = new FollowUpTopSalerView();
//        salersView.setOnItemClick(staff -> {
//            model.salerName.set(staff == null ? "销售" : staff.username);
//            filter.sale = staff;
//            selectAction.call(0);
//        });
//
//    }
//
//    @Override
//    protected String[] getTags() {
//        return new String[]{"salerList", "studentStatus", "today", "latestDay", "gender", "topSalers"};
//    }
//
//    @Override
//    protected Fragment getFragmentByTag(String tag) {
//        if (tag.equalsIgnoreCase(getTags()[0])) {
//            return salersView;
//        } else if (tag.equalsIgnoreCase(getTags()[1])) {
//            return studentStatusFragment;
//        } else if (tag.equalsIgnoreCase(getTags()[2])) {
//            return dayFragment;
//        } else if (tag.equalsIgnoreCase(getTags()[3])) {
//            return topDayFragment;
//        } else if (tag.equalsIgnoreCase(getTags()[4])) {
//            return genderFragment;
//        } else if (tag.equalsIgnoreCase(getTags()[5])) {
//            return topSalerView;
//        }
//        return new EmptyFragment();
//    }
//
//    private Action0 dimissAction;
//
//    public void setDismissAction(Action0 action) {
//        this.dimissAction = action;
//    }
//
//    private Action1<Integer> selectAction;
//
//    public void setSelectAction(Action1<Integer> action) {
//        this.selectAction = action;
//    }
//}
