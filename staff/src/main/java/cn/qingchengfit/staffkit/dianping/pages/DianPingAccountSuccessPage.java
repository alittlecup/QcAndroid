package cn.qingchengfit.staffkit.dianping.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.PageDianpingAccountSuccessBinding;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "dianping", path = "/dianping/success") public class DianPingAccountSuccessPage
    extends SaasCommonFragment {
  PageDianpingAccountSuccessBinding mBinding;
  @Need String gymName;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    StaffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = PageDianpingAccountSuccessBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("美团点评"));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
    if (!TextUtils.isEmpty(gymName)) {
      mBinding.tvContent.setText(
          String.format(getResources().getString(R.string.dianping_account_success), gymName));
    }
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });

    return mBinding.getRoot();
  }
}
