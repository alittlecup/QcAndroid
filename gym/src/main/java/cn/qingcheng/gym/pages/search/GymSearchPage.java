package cn.qingcheng.gym.pages.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymSearchPageBinding;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

@Leaf(module = "gym", path = "/gym/search") public class GymSearchPage
    extends GymBaseFragment<GyGymSearchPageBinding, GymSearchViewModel> {
  private CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymSearchPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return mBinding = GyGymSearchPageBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecyclerView();
    initListener();
    if (!CompatUtils.less21() && isfitSystemPadding()) {
      mBinding.root.setPadding(0, MeasureUtils.getStatusBarHeight(getContext()),
          0, 0);
    }
  }

  private void initListener() {
    RxTextView.afterTextChangeEvents(mBinding.editSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .map((event -> event.editable().toString()))
        .subscribe((msg -> {
          if (TextUtils.isEmpty(msg)) {
            mBinding.tvCreateGym.setVisibility(View.GONE);
            mBinding.tvCancel.setVisibility(View.GONE);
          } else {
            mBinding.tvCreateGym.setVisibility(View.VISIBLE);
            mBinding.tvCreateGym.setText("+ 创建\"" + msg + "\"场馆");
            mBinding.tvCancel.setVisibility(View.VISIBLE);
          }
        }));
    mBinding.tvCreateGym.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }
}
