package cn.qingchengfit.student;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.student.base.BaseViewModel;

/**
 * Created by huangbaole on 2017/11/15.
 */

public abstract class StudentBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends SaasBaseFragment {

    public DB mBinding;
    public VM mViewModel;

    @Inject
    public ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, factory).get(getVMClass());
        initViewModel();
    }

    @Override
    public boolean isBlockTouch() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = initDataBinding(inflater, container, savedInstanceState);
        return mBinding.getRoot();
    }

    protected abstract void initViewModel();

    public abstract DB initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    private Class<VM> getVMClass() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        if(actualTypeArguments.length==2){
            return (Class<VM>) actualTypeArguments[1];
        }
        return (Class<VM>) BaseViewModel.class;
    }

}





