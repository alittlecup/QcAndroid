package cn.qingchengfit.checkout.view.scan;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.QcScanActivityBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
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
      // TODO: 2018/7/25 deal result
    });
  }

  private boolean hasBarCodeFlag = false;

  @Override public void onQRCodeRead(String text, PointF[] points) {
    if (mBinding.qrCodeView != null) mBinding.qrCodeView.getCameraManager().stopPreview();
    if (hasBarCodeFlag) return;
    hasBarCodeFlag = true;
    showLoading();
    QcRouteUtil.setRouteOptions(
        new RouteOptions(scanRepayInfo.getModuleName()).setActionName(scanRepayInfo.getActionName())
            .addParam("type", type)
            .addParams(new HashMap<>(scanRepayInfo.getParams()))).callAsync(qcResult -> {
      hasBarCodeFlag = false;
      if (qcResult.isSuccess()) {
        Map<String, Object> dataMap = qcResult.getDataMap();
        String pay_trade_no = (String) dataMap.get("orderNumber");
        String out_trade_no = (String) dataMap.get("pollingNumber");
        mViewModel.scanPay(text, out_trade_no, pay_trade_no);
      } else {
        ToastUtils.show(qcResult.getErrorMessage());
      }
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
