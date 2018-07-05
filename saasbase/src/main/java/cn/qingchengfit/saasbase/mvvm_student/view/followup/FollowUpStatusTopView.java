//package cn.qingchengfit.saasbase.mvvm_student.view.followup;
//
//import android.arch.lifecycle.ViewModelProviders;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import cn.qingchengfit.saasbase.SaasBaseFragment;
//import cn.qingchengfit.saasbase.databinding.ViewFollowupStatusTopBinding;
//import cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup.FollowUpStatusViewModel;
//
///**
// * Created by huangbaole on 2017/11/6.
// */
//
//public class FollowUpStatusTopView extends SaasBaseFragment {
//
//    FollowUpStatusViewModel mViewModel;
//
//    ViewFollowupStatusTopBinding mBinding;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        mBinding = ViewFollowupStatusTopBinding.inflate(inflater, container, false);
//        mBinding.setViewModel(mViewModel);
//        return mBinding.getRoot();
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mViewModel = ViewModelProviders.of(getParentFragment()).get(FollowUpStatusViewModel.class);
//        mViewModel.getTopDatas().observe(this, datas -> mViewModel.datas.set(datas));
//    }
//}
