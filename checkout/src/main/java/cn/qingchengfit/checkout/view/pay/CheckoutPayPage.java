package cn.qingchengfit.checkout.view.pay;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CashierBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutPayBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.checkout.bean.IOrderData;
import cn.qingchengfit.checkout.view.scan.QcScanActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.zxing.WriterException;
import com.tbruyelle.rxpermissions.RxPermissions;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

@Leaf(module = "checkout", path = "/checkout/pay") public class CheckoutPayPage
    extends CheckoutCounterFragment<CkPageCheckoutPayBinding, CheckoutPayViewModel> {
  @Need String type = "";
  @Need IOrderData orderData;
  private static final int PAY_SUCCESS = 111;
  private boolean SUCCESS = false;

  @Override protected void subscribeUI() {
    mViewModel.orderStatusBean.observe(this, orderStatusBean -> {
      if (subscribe != null && orderStatusBean != null) {
        switch (orderStatusBean.order.getStatus()) {
          case 0:
            stopPollintOrderStatus();
            DialogUtils.showAlert(getContext(), "收款失败", "收款失败，请点击下方按钮重试",
                (materialDialog, dialogAction) -> {
                  materialDialog.dismiss();
                  stopPollintOrderStatus();
                  rePay();
                });

            break;
          case 2:
            stopPollintOrderStatus();
            SUCCESS = true;
            String success_url = orderStatusBean.order.getSuccess_url();
            WebActivity.startWebForResult(success_url, this, PAY_SUCCESS);
            break;
        }
      }
    });

    mViewModel.IOrderData.observe(this, iOrderData -> {
      hideLoading();
      if (iOrderData != null) {
        initQrCode(iOrderData);
        mBinding.tvCheckoutMoney.setText("￥" + String.valueOf(iOrderData.getPrice()));
        startPollingOrderStatus();
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == PAY_SUCCESS) {
        paySuccessBack();
      }
    }
  }

  private void paySuccessBack() {
    String qcCallId = getActivity().getIntent().getStringExtra("qcCallId");
    QC.sendQCResult(qcCallId, QCResult.success());
    getActivity().finish();
  }

  @Override
  public CkPageCheckoutPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutPayBinding.inflate(inflater, container, false);
    orderData = getArguments().getParcelable("orderData");
    mViewModel.IOrderData.setValue(orderData);
    initToolbar();
    initUI();
    initListener();
    return mBinding;
  }

  private void rePay() {
    showLoading();
    ScanRepayInfo scanRePayInfo = mViewModel.IOrderData.getValue().getScanRePayInfo();
    QcRouteUtil.setRouteOptions(
        new RouteOptions(scanRePayInfo.getModuleName()).setActionName(scanRePayInfo.getActionName())
            .addParam("type", type)
            .addParam("params",scanRePayInfo.getParams())).callAsync(qcResult -> {
      if (qcResult.isSuccess()) {
        Map<String, Object> dataMap = qcResult.getDataMap();
        Object cashierBean = dataMap.get("cashierBean");
        if (cashierBean instanceof CashierBean) {
          IOrderData value = mViewModel.IOrderData.getValue();
          CashierBeanWrapper cashierBeanWrapper = new CashierBeanWrapper((CashierBean) cashierBean);
          cashierBeanWrapper.setPrice(value.getPrice());
          cashierBeanWrapper.setInfo(value.getScanRePayInfo());
          mViewModel.IOrderData.setValue(value);
        }
      } else {
        ToastUtils.show(qcResult.getErrorMessage());
      }
    });
  }

  private void initQrCode(IOrderData orderData) {
    QRGEncoder qrgEncoder = new QRGEncoder(orderData.getQrCodeUri(), null, QRGContents.Type.TEXT,
        MeasureUtils.dpToPx(180f, getResources()));
    try {

      Bitmap bitmap = qrgEncoder.encodeAsBitmap();
      mBinding.imgQr.setAdjustViewBounds(true);
      mBinding.imgQr.setPadding(0, 0, 0, 0);
      mBinding.imgQr.setImageBitmap(bitmap);
    } catch (WriterException e) {
      Timber.e(e, " qrgen");
    }
  }

  private void initListener() {

    mBinding.flScan.setOnClickListener(v -> {
      new RxPermissions(getActivity()).request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
        if (aBoolean) {
          if (mViewModel.IOrderData.getValue() != null) {
            Intent intent = new Intent(getContext(), QcScanActivity.class);
            intent.putExtra("title", "扫码收款");
            intent.putExtra("type", type);
            intent.putExtra("repay", mViewModel.IOrderData.getValue().getScanRePayInfo());
            startActivityForResult(intent, PAY_SUCCESS);
          }
        } else {
          DialogUtils.showAlert(getContext(), "请打开摄像头权限");
        }
      });
    });
  }

  Disposable subscribe;

  @Override public void onResume() {
    super.onResume();
    if (!SUCCESS) {
      startPollingOrderStatus();
    }
  }

  private void startPollingOrderStatus() {
    if (subscribe != null && !subscribe.isDisposed()) {
      subscribe.dispose();
    }
    subscribe = null;
    if (mViewModel.IOrderData.getValue() != null) {
      subscribe = Flowable.interval(1000, 200, TimeUnit.MILLISECONDS)
          .subscribe(aLong -> mViewModel.pollingOrderresult(
              mViewModel.IOrderData.getValue().getPollingNUmber()));
    }
  }

  private void stopPollintOrderStatus() {
    if (subscribe != null && !subscribe.isDisposed()) {
      subscribe.dispose();
      subscribe = null;
    }
  }

  @Override public void onPause() {
    super.onPause();
    stopPollintOrderStatus();
  }

  private void initUI() {

    if (type.equals("ALIPAY_QRCODE")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_ali);
      mBinding.tvDec.setText("支付宝扫描上方二维码完成支付");
      mBinding.tvQrTitle.setText("支付宝收款码");
    } else if (type.equals("WEIXIN_QRCODE")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_wechat);
      mBinding.tvDec.setText("微信扫描上方二维码完成支付");
      mBinding.tvQrTitle.setText("微信收款码");
    }
  }

  private void initToolbar() {
    if (type.equals("ALIPAY_QRCODE")) {
      mBinding.setToolbarModel(new ToolbarModel("支付宝收款"));
    } else if (type.equals("WEIXIN_QRCODE")) {
      mBinding.setToolbarModel(new ToolbarModel("微信收款"));
    }
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
