//package cn.qingchengfit.saasbase.mvvm_student.view.home;
//
//import android.arch.lifecycle.ViewModelProviders;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import cn.qingchengfit.saasbase.R;
//
//import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
//import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotStaffListPageParams;
//import cn.qingchengfit.saasbase.student.views.home.StudentHomeViewModel;
//import cn.qingchengfit.saasbase.student.other.MyIndicator;
//import cn.qingchengfit.saasbase.student.other.MySnapHelper;
//import cn.qingchengfit.saasbase.student.views.StudentOperationItem;
//import cn.qingchengfit.utils.MeasureUtils;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.widgets.CommonFlexAdapter;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * StudentListFragment 顶部的会员功能界面
// */
//public class StudentOperationView extends BaseFragment
//        implements FlexibleAdapter.OnItemClickListener, MySnapHelper.OnSelectListener {
//
//
//    RecyclerView recycleview;
//
//
//    MyIndicator indicator;
//
//    StudentHomeViewModel viewModel;
//    private List<AbstractFlexibleItem> datas = new ArrayList<>();
//    private CommonFlexAdapter mCommonFlexAdapter;
//    private boolean proGym = true;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_student_operation, container, false);
//      recycleview = (RecyclerView) v.findViewById(R.id.recycleview);
//      indicator = (MyIndicator) v.findViewById(R.id.indicator);
//
//      viewModel = ViewModelProviders.of(getParentFragment()).get(StudentHomeViewModel.class);
////        RxRegiste(
////            gymBaseInfoAction.getGymByModel(gymWrapper.id(),gymWrapper.model())
////            .filter(new Func1<List<CoachService>, Boolean>() {
////            @Override public Boolean call(List<CoachService> list) {
////                return list != null && list.size() > 0;
////            }
////        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
////            @Override public void call(List<CoachService> list) {
////                CoachService now = list.get(0);
////                proGym = GymUtils.getSystemEndDay(now) >= 0;
////                datas.clear();
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_sales, R.string.qc_student_allotsale, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_attend, R.string.qc_student_attendance, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_coach, R.string.qc_student_allot_coach, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_send_sms, R.string.qc_student_send_sms, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_follow, R.string.qc_student_follow_up, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_export, R.string.qc_student_export, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vd_student_transfer, R.string.qc_student_follow_transfer, proGym, true));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_birthday, R.string.qc_student_birthday_notice, proGym, false));
//        datas.add(new StudentOperationItem(R.drawable.vector_student_management_tag, R.string.qc_student_vip, proGym, false));
//        if (mCommonFlexAdapter != null) mCommonFlexAdapter.notifyDataSetChanged();
////            }
////        }));
//
//        mCommonFlexAdapter = new CommonFlexAdapter(datas, this);
//        SmoothScrollGridLayoutManager gridLayoutManager =
//                new SmoothScrollGridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 1;
//            }
//        });
//        // REFACTOR: 2017/10/26 底部标
//        MySnapHelper pagerSnapHelper = new MySnapHelper();
//        pagerSnapHelper.setCount(8);
//        pagerSnapHelper.setListener(this);
//        pagerSnapHelper.attachToRecyclerView(recycleview);
//
//        recycleview.setLayoutManager(gridLayoutManager);
//        recycleview.setAdapter(mCommonFlexAdapter);
//        onDatasRefresh();
//        return v;
//    }
//
//    private void onDatasRefresh() {
//        setRecyclerPadding(datas.size());
//        indicator.createIndicators(datas.size() / 8 + 1);
//    }
//
//    private void setRecyclerPadding(int lastPosition) {
//        if (lastPosition % 2 == 0) {
//            lastPosition -= 1;
//        }
//        recycleview.setPadding(0, 0,
//                MeasureUtils.getScreenWidth(getResources()) * (4 - lastPosition % 8) / 4, 0);
//    }
//
//
//    @Override
//    public String getFragmentName() {
//        return this.getClass().getName();
//    }
//
//
//    @Override
//    public boolean onItemClick(int position) {
//
//        if (!viewModel.checkPro()) {
//            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
//            return true;
//        }
//        if (mCommonFlexAdapter.getItem(position) instanceof StudentOperationItem) {
//            int strId = ((StudentOperationItem) mCommonFlexAdapter.getItem(position)).getStrRes();
//            if (strId == R.string.qc_student_allotsale) {
//                Uri uri = Uri.parse("qcstaff://student/allot/student");
//                routeTo(uri, new AllotStaffListPageParams().type(0).build());
//            } else if (strId == R.string.qc_student_allot_coach) {
//                Uri uri = Uri.parse("qcstaff://student/allot/student");
//                routeTo(uri, new AllotStaffListPageParams().type(1).build());
//            } else if (strId == R.string.qc_student_follow_up) {
//                Uri uri = Uri.parse("qcstaff://student/followup/student");
//                routeTo(uri, null);
//            } else if (strId == R.string.qc_student_follow_transfer) {
//                Uri uri = Uri.parse("qcstaff://student/transfer/student");
//                routeTo(uri, null);
//            } else if (strId == R.string.qc_student_attendance) {
//                Uri uri = Uri.parse("qcstaff://student/attendance/page");
//                routeTo(uri, null);
//
//            } else if (strId == R.string.qc_student_send_sms) {
//
//            } else if (strId == R.string.qc_student_export) {
//                Uri uri = Uri.parse("qcstaff://student/follow/home");
//                routeTo(uri, null);
//
//            } else if (strId == R.string.qc_student_birthday_notice) {
//
//            } else if (strId == R.string.qc_student_vip) {
//
//            }
//
//        }
//        return true;
//    }
//
//    @Override
//    public void onPageSelect(int position) {
//        indicator.onPageSelected(position);
//    }
//}
