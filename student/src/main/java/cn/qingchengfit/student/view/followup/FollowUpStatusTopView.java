package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.ViewFollowupStatusTopBinding;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStatusViewModel;

/**
 * Created by huangbaole on 2017/11/6.
 */

public class FollowUpStatusTopView extends StudentBaseFragment<ViewFollowupStatusTopBinding, FollowUpStatusViewModel> {

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(getParentFragment()).get(FollowUpStatusViewModel.class);
        mViewModel.getTopDatas().observe(this, datas -> {
            mViewModel.datas.set(datas);
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ViewFollowupStatusTopBinding.inflate(inflater, container, false);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }
}
