package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staffkit.databinding.FragmentSigninTimeBinding;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import com.jakewharton.rxbinding.view.RxView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author huangbaole
 */
public class SignInTimeListFragment
    extends SaasBindingFragment<FragmentSigninTimeBinding, SignTimeListVM>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter mAdapter;

  @Override protected void subscribeUI() {
  }

  @Override
  public FragmentSigninTimeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentSigninTimeBinding.inflate(inflater, container, false);
    initRecyclerView();
    initListener();
    mViewModel.loadSinInTimes();
    mBinding.setToolbarModel(new ToolbarModel("入场时段"));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
  }

  private void initListener() {
    RxView.clicks(mBinding.tvAddSignInTime)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(v -> {
          routeTo(new SignInTimeSettingFragment());
        });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(mAdapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = mAdapter.getItem(i);
    if (item instanceof SignTimeListItem) {
      routeTo(SignInTimeSettingFragment.getInstance(true));
    }
    return false;
  }
}
