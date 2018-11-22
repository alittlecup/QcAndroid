package cn.qingchengfit.staffkit.dianping.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.databinding.PageDianpingAccountSuccessBinding;
import cn.qingchengfit.staffkit.views.custom.MyBindingViewHolder;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "dianping", path = "/dianping/success") public class DianPingAccountSuccessPage
    extends SaasCommonFragment {
  PageDianpingAccountSuccessBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = PageDianpingAccountSuccessBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("美团点评"));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });

    return mBinding.getRoot();
  }
}
