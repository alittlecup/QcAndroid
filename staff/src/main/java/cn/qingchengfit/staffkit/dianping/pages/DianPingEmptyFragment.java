package cn.qingchengfit.staffkit.dianping.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.qrcode.views.QRScanActivity;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.anbillon.flabellum.annotations.Need;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class DianPingEmptyFragment extends SaasCommonFragment {
  @Inject StaffRespository staffRespository;
  @Inject GymWrapper gymWrapper;
  @Need String qrCode = "";
  private static final String DIANPING_EMPTY_TAG = "dianping_empty_tag";

  public static DianPingEmptyFragment addDianPingEmptyFragment(FragmentManager fragmentManager,
      String qrCode) {
    Fragment fragmentByTag = fragmentManager.findFragmentByTag(DIANPING_EMPTY_TAG);
    if (fragmentByTag != null && !(fragmentByTag instanceof DianPingEmptyFragment)) {
      throw new RuntimeException("Unexpected fragment instance was returned by DIANPING_EMPTY_TAG");
    }
    if (fragmentByTag == null) {
      fragmentByTag = createDianPingEmpteyFragment(qrCode);
      fragmentManager.beginTransaction()
          .add(fragmentByTag, DIANPING_EMPTY_TAG)
          .commitAllowingStateLoss();
    } else {
      ((DianPingEmptyFragment) fragmentByTag).setQrCode(qrCode);
    }
    return (DianPingEmptyFragment) fragmentByTag;
  }

  private static Fragment createDianPingEmpteyFragment(String qrCode) {
    DianPingEmptyFragment fragment = new DianPingEmptyFragment();
    Bundle bundle = new Bundle();
    bundle.putString("qrCode", qrCode);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StaffParamsInjector.inject(this);
    dealQrCode(qrCode);
  }

  private void dealQrCode(String qrCode) {
    if (TextUtils.isEmpty(qrCode)) {
      startToScan();
    } else if(gymWrapper.getCoachService().meituan_status!=0){
      routeTo("dianping", "/dianping/success",
          new DianPingAccountSuccessPageParams().gymName(
              gymWrapper.getCoachService().getName()).build());
    }else{
      loadDianPingGymInfo(qrCode);

    }
  }

  private void setQrCode(String qrCode) {
    this.qrCode = qrCode;
    dealQrCode(qrCode);
  }

  private void routeToEditGymInfoPage(String code, String mtShopName) {
    routeTo("/dianping/account",
        new DianPingAccountPageParams().dianPingGymName(mtShopName).barCode(code).build());
  }

  private void startToScan() {
    Intent intent = new Intent(getContext(), QRScanActivity.class);
    intent.putExtra("title", "扫描二维码");
    intent.putExtra("point_text",
        "在电脑上登录点评管家\n" + "https://e.dianping.com\n" + "打开第三方授权二维码，扫码即可授权");
    startActivityForResult(intent, 1001);
  }

  public void loadDianPingGymInfo(String qrcode) {
    Map<String, Object> params = new HashMap<>();
    params.put("qrcode", qrcode);
    RxRegiste(staffRespository.getStaffAllApi()
        .qcGetDianPingGymInfo(gymWrapper.getCoachService().getGym_id(), params)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            routeToEditGymInfoPage(qrcode, response.data.mt_shop.shopName);
          } else {
            showLoadDianPingGymErrorDialog(response.getMsg());
          }
        }, throwable -> {
          ToastUtils.show(throwable.getMessage());
        }));
  }

  private void showLoadDianPingGymErrorDialog(String content) {
    DialogUtils.initConfirmDialog(getContext(), "", content + "\n 仍然失败请联系客服：400-900-7986", "联系客服",
        "关闭", (materialDialog, dialogAction) -> {
          materialDialog.dismiss();
          if (dialogAction == DialogAction.POSITIVE) {
            AppUtils.doCallPhoneTo(getContext(), "400-900-7986");
          }
        }).show();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1001 & resultCode == Activity.RESULT_OK) {
      if (null != data) {
        String content = data.getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
          if (content.startsWith("DIANPING-QINGCHENG-SHOPMAPPING")) {
            if (gymWrapper.getCoachService().meituan_status == 0) {
              loadDianPingGymInfo(content);
            } else {
              routeTo("dianping", "/dianping/success",
                  new DianPingAccountSuccessPageParams().gymName(
                      gymWrapper.getCoachService().getName()).build());
            }
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
