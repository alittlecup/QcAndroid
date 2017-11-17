package cn.qingchengfit.student.base;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import javax.inject.Inject;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.student.viewmodel.attendance.AttendanceStudentViewModel;

/**
 * Created by huangbaole on 2017/11/15.
 */

public abstract class StudentBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends SaasBaseFragment {

    public DB mBinding;
    public View mRootView;
    public VM mViewModel;

    @Inject
    public ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = initView(inflater, container, savedInstanceState);
        return mRootView;
    }

    protected abstract void initViewModel();
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
