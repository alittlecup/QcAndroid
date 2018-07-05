//package cn.qingchengfit.saasbase.mvvm_student.view.attendance.nosign;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import cn.qingchengfit.model.others.ToolbarModel;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.databinding.PageAttendanceNosignBinding;
//import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
//import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.nosign.AttendanceNosignViewModel;
//import cn.qingchengfit.utils.DateUtils;
//import cn.qingchengfit.widgets.CommonFlexAdapter;
//import com.anbillon.flabellum.annotations.Leaf;
//import java.util.ArrayList;
//
///**
// * Created by huangbaole on 2017/11/17.
// */
//@Leaf(module = "student",path = "/attendance/nosign")
//public class AttendanceNosignPage extends
//    SaasBindingFragment<PageAttendanceNosignBinding,AttendanceNosignViewModel> {
//    private AttendanceNosignView filterView;
//
//    @Override
//    protected void subscribeUI() {
//
//        mViewModel.getFilterIndex().observe(this,index->filterView.showPage(index));
//
//        mViewModel.getLiveItems().observe(this,notSignClassItems -> {
//            mViewModel.items.set(notSignClassItems);
//            mViewModel.textDetail.set(
//                    getString(cn.qingchengfit.saasbase.R.string.text_not_sign_tip,
//                            DateUtils.interval(mViewModel.getDataHolder().getDays().first,mViewModel.getDataHolder().getDays().second)+1
//                    ,mViewModel.topCount.get(),notSignClassItems==null?0:notSignClassItems.size()));
//        });
//
//    }
//
//    @Override
//    public PageAttendanceNosignBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mBinding=PageAttendanceNosignBinding.inflate(inflater,container,false);
//        mBinding.setViewModel(mViewModel);
//        mBinding.setToolbarModel(new ToolbarModel("未签课统计"));
//        initToolbar(mBinding.includeToolbar.toolbar);
//        initFragment();
//        mBinding.recyclerview.setAdapter(new CommonFlexAdapter(new ArrayList()));
//        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        mViewModel.loadSource(mViewModel.getDataHolder());
//        return mBinding;
//    }
//
//    private void initFragment() {
//        filterView = new AttendanceNosignView();
//        stuff(R.id.frag_filter, filterView);
//    }
//}
