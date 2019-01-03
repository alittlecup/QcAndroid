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
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.QcScanActivityBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;
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
    if (type.equals("ALIPAY_QRCODE")) {
      selectAliPayCode(true);
    } else {
      selectAliPayCode(false);
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.getRoot().setBackgroundColor(Color.TRANSPARENT);
    mViewModel = ViewModelProviders.of(this, factory).get(QcScanActivityModel.class);
    mViewModel.scanResult.observe(this, scanResultBean -> {
      hideLoading();
      if (scanResultBean.successful) {
        WebActivity.startWebForResult(scanResultBean.url, this, PAY_SUCCESS);
      } else {
        payError();
      }
    });
    mBinding.llAli.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        selectAliPayCode(true);
      }
    });
    mBinding.llWx.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        selectAliPayCode(false);
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
            .addParam("params", scanRepayInfo.getParams())).callAsync(qcResult -> {
      if (qcResult.isSuccess()) {
        Map<String, Object> dataMap = qcResult.getDataMap();
        Object data = dataMap.get("cashierBean");
        if (data != null) {
          CashierBean cashierBean = null;
          if (data instanceof String) {
            cashierBean = new Gson().fromJson((String) data, CashierBean.class);
          } else if (data instanceof CashierBean) {
            cashierBean = (CashierBean) data;
          }
          if (cashierBean == null) {
            ToastUtils.show("网络连接异常");
            return;
          }
          if ("10FC".equals(cashierBean.getResult_code())) {
            ToastUtils.show("收款金额超过您的额度上限，请您重新输入");
            return;
          }
          mViewModel.scanPay(barCode, cashierBean.getOut_trade_no(), cashierBean.getPay_trade_no());
        } else {
          Object out_trade_no = dataMap.get("out_trade_no");
          Object pay_trade_no = dataMap.get("pay_trade_no");
          if (out_trade_no != null && pay_trade_no != null) {
            mViewModel.scanPay(barCode, (String) out_trade_no, (String) pay_trade_no);
          } else {
            ToastUtils.show("网络连接异常");
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

  public void selectAliPayCode(boolean selected) {
    mBinding.tvAli.setTextColor(
        getResources().getColor(selected ? R.color.colorPrimary : R.color.text_grey));
    mBinding.tvWx.setTextColor(
        getResources().getColor(selected ? R.color.text_grey : R.color.colorPrimary));
    mBinding.imgAli.setImageDrawable(getResources().getDrawable(
        selected ? R.drawable.ic_turnover_ali : R.drawable.ic_turnover_ali_disable));
    mBinding.imgWx.setImageDrawable(getResources().getDrawable(
        selected ? R.drawable.ic_newcard_line : R.drawable.ic_turnover_wx));
    if (selected) {
      type = "ALIPAY_QRCODE";
      mBinding.tvPoint.setText("支付宝付款二维码");
    } else {
      type = "WEIXIN_QRCODE";
      mBinding.tvPoint.setText("微信付款二维码");
    }
  }
}
