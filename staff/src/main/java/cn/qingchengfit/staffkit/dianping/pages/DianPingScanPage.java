package cn.qingchengfit.staffkit.dianping.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.qrcode.views.QRScanActivity;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.databinding.PageDianpingScanBinding;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@Leaf(module = "dianping", path = "/dianping/home") public class DianPingScanPage
    extends SaasCommonFragment {
  PageDianpingScanBinding mBinding;
  @Inject StaffRespository staffRespository;
  @Inject GymWrapper gymWrapper;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = PageDianpingScanBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("美团点评"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.btnScan.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DianPingEmptyFragment.addDianPingEmptyFragment(getFragmentManager(), "");
      }
    });
    return mBinding.getRoot();
  }
}
