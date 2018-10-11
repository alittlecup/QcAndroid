//package cn.qingchengfit.saasbase.student.views.transfer;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//
//import cn.qingchengfit.model.base.Staff;
//import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
//import cn.qingchengfit.saascommon.filtertime.FilterTimesFragment;
//import cn.qingchengfit.saasbase.student.views.followup.FollowUpTopSalerView;
//import cn.qingchengfit.views.fragments.BaseFilterFragment;
//import cn.qingchengfit.views.fragments.EmptyFragment;
//import rx.functions.Action0;
//import rx.functions.Action1;
//import rx.functions.Action3;
//
///**
// * Created by huangbaole on 2017/11/13.
// */
//
//public class TransferFilterFragment extends BaseFilterFragment {
//    FollowUpTopSalerView salerView;
//    FilterTimesFragment dayFragment;
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
//        if (dismissAction == null) return;
//        dismissAction.call();
//    }
//
//    private void initFragment() {
//        dayFragment = FilterTimesFragment.getInstance(1, 30);
//        dayFragment.setSelectDayAction(daySelect);
//        salerView = new FollowUpTopSalerView();
//        salerView.setOnItemClick(staffAction1);
//    }
//
//    @Override
//    protected String[] getTags() {
//        return new String[]{"saler", "day"};
//    }
//
//    @Override
//    protected Fragment getFragmentByTag(String tag) {
//        if (tag.equalsIgnoreCase(getTags()[0])) {
//            return salerView;
//        } else if (tag.equalsIgnoreCase(getTags()[1])) {
//            return dayFragment;
//        }
//        return new EmptyFragment();
//    }
//
//    private Action0 dismissAction;
//
//    public void setDismissAction(Action0 dismissAction) {
//        this.dismissAction = dismissAction;
//    }
//
//    Action1<Staff> staffAction1;
//
//    public void setStaffCallback(Action1<Staff> staffCallback) {
//        this.staffAction1 = staffCallback;
//    }
//
//    private Action3<String, String, String> daySelect;
//
//    public void setSelectDayAction(Action3<String, String, String> daySelected) {
//        this.daySelect = daySelected;
//    }
//
//}
