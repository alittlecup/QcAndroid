package cn.qingchengfit.staffkit.dianping.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.qrcode.views.QRScanActivity;
import cn.qingchengfit.staffkit.databinding.PageDianpingAccountBinding;
import cn.qingchengfit.staffkit.databinding.PageDianpingScanBinding;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import lib_zxing.activity.CaptureActivity;
import lib_zxing.activity.CodeUtils;

@Leaf(module = "dianping", path = "/dianping/scan") public class DianPingScanPage
    extends SaasCommonFragment {
  PageDianpingScanBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = PageDianpingScanBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("美团点评"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.btnScan.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startToScan();
      }
    });
    return mBinding.getRoot();
  }

  private void startToScan() {
    Intent intent = new Intent(getContext(), QRScanActivity.class);
    intent.putExtra("title", "扫描二维码");
    intent.putExtra("point_text",
        "在电脑上登录点评管家\n" + "https://e.dianping.com\n" + "打开第三方授权二维码，扫码即可授权");
    startActivityForResult(intent, 1001);
  }

  private void routeToEditGymInfoPage(String code) {
    routeTo("/dianping/account", new DianPingAccountPageParams().barCode(code).build());
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1001 & resultCode == Activity.RESULT_OK) {
      if (null != data) {
        String content = data.getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
          if (content.startsWith("DIANPING-QINGCHENG-SHOPMAPPING")) {
            routeToEditGymInfoPage(content);
          } else {
            routeTo("/dianping/error", null);
          }
        } else {
          routeTo("/dianping/error", null);
        }
      }
    }
  }
}
