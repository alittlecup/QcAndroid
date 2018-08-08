package cn.qingchengfit.checkout.view.scan;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.QcScanActivityBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class QcScanActivity extends SaasCommonActivity
    implements QRCodeReaderView.OnQRCodeReadListener {
  QcScanActivityBinding mBinding;
  ScanRepayInfo scanRepayInfo;
  @Inject ViewModelProvider.Factory factory;
  private static final int PAY_SUCCESS = 111;
  QcScanActivityModel mViewModel;
  String type = "";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.qc_scan_activity);
    String title = getIntent().getStringExtra("title");
    scanRepayInfo = getIntent().getParcelableExtra("repay");
    type = getIntent().getStringExtra("type");
    if (scanRepayInfo == null || TextUtils.isEmpty(type)) return;
    mBinding.setToolbarModel(new ToolbarModel(title));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.getRoot().setBackgroundColor(Color.TRANSPARENT);
    mViewModel = ViewModelProviders.of(this, factory).get(QcScanActivityModel.class);
    mViewModel.scanResult.observe(this, scanResultBean -> {
      hideLoading();
      if (scanResultBean.successful) {
        ToastUtils.show("success");
        //这里跳转的逻辑是，如果是从收银台进来的就去收银台首页，如果是从原有的其他位置进来的就执行之前的成功之后的逻辑
        WebActivity.startWebForResult(scanResultBean.url, this, PAY_SUCCESS);
      } else {
        payError();
      }
    });
  }

  private boolean hasBarCodeFlag = false;

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == PAY_SUCCESS) {
        paySuccessBack();
      }
    }
  }

  private void paySuccessBack() {
    setResult(Activity.RESULT_OK);
    finish();
  }

  @Override public void onQRCodeRead(String text, PointF[] points) {
    if (mBinding.qrCodeView != null) mBinding.qrCodeView.getCameraManager().stopPreview();
    if (hasBarCodeFlag) return;
    hasBarCodeFlag = true;
    rePay(text);
  }

  private void rePay(String barCode) {
    showLoading();
    QcRouteUtil.setRouteOptions(
        new RouteOptions(scanRepayInfo.getModuleName()).setActionName(scanRepayInfo.getActionName())
            .addParam("type", type)
            .addParams(new HashMap<>(scanRepayInfo.getParams()))).callAsync(qcResult -> {
      if (qcResult.isSuccess()) {
        Map<String, Object> dataMap = qcResult.getDataMap();
        Object cashierBean = dataMap.get("cashierBean");
        if (cashierBean instanceof CashierBean) {
          mViewModel.scanPay(barCode, ((CashierBean) cashierBean).getOut_trade_no(),
              ((CashierBean) cashierBean).getPay_trade_no());
        } else {
          Object out_trade_no = dataMap.get("out_trade_no");
          Object pay_trade_no = dataMap.get("pay_trade_no");
          if (out_trade_no != null && pay_trade_no != null) {
            mViewModel.scanPay(barCode, (String) out_trade_no, (String) pay_trade_no);
          }
        }
      } else {
        payError();
      }
    });
  }

  private void payError() {
    hideLoading();
    DialogUtils.showAlert(this, "收款失败", "收款失败，请点击下方按钮重新扫码", (materialDialog, dialogAction) -> {
      materialDialog.dismiss();
      hasBarCodeFlag = false;
      if (mBinding.qrCodeView != null) mBinding.qrCodeView.getCameraManager().startPreview();
    });
  }

  @Override protected void onResume() {
    super.onResume();
    if (mBinding.qrCodeView != null) {
      new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
        if (aBoolean) {
          mBinding.qrCodeView.setOnQRCodeReadListener(QcScanActivity.this);
          mBinding.qrCodeView.getCameraManager().startPreview();
        } else {
          DialogUtils.showAlert(QcScanActivity.this, "请打开摄像头权限");
        }
      });
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (mBinding.qrCodeView != null) mBinding.qrCodeView.getCameraManager().stopPreview();
  }

  @Override public void cameraNotFound() {

  }

  @Override public void QRCodeNotFoundOnCamImage() {

  }
}
