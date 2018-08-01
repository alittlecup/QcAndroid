package cn.qingchengfit.checkout.view.pay;

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
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.bean.CashierBeanWrapper;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutPayBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.qrcode.model.IOrderData;
import cn.qingchengfit.checkout.view.scan.QcScanActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

@Leaf(module = "checkout", path = "/checkout/pay") public class CheckoutPayPage
    extends CheckoutCounterFragment<CkPageCheckoutPayBinding, CheckoutPayViewModel> {
  @Need String type = "";
  @Need IOrderData orderData;

  @Override protected void subscribeUI() {
    mViewModel.orderStatusBean.observe(this, orderStatusBean -> {
      if (subscribe != null&&orderStatusBean!=null) {
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
            WebActivity.startWeb(orderStatusBean.order.getSuccess_url(), getContext());
            break;
        }
      }
    });

    mViewModel.IOrderData.observe(this, iOrderData -> {
      hideLoading();
      if (iOrderData != null) {
        initQrCode(iOrderData);
        mBinding.tvCheckoutMoney.setText("￥" + String.valueOf(iOrderData.getPrices()));
        startPollingOrderStatus();
      }
    });
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
            .addParams(new HashMap<>(scanRePayInfo.getParams()))).callAsync(qcResult -> {
      if (qcResult.isSuccess()) {
        Map<String, Object> dataMap = qcResult.getDataMap();
        Object cashierBean = dataMap.get("cashierBean");
        if (cashierBean instanceof CashierBean) {
          IOrderData value = mViewModel.IOrderData.getValue();
          CashierBeanWrapper cashierBeanWrapper = new CashierBeanWrapper((CashierBean) cashierBean);
          cashierBeanWrapper.setPrices(value.getPrices());
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
      Timber.e(e, " qr gen");
    }
  }

  private void initListener() {

    mBinding.flScan.setOnClickListener(v -> {
      if (mViewModel.IOrderData.getValue() != null) {
        Intent intent = new Intent(getContext(), QcScanActivity.class);
        intent.putExtra("title", "扫码收款");
        intent.putExtra("type", type);
        intent.putExtra("repay", mViewModel.IOrderData.getValue().getScanRePayInfo());
        startActivity(intent);
      }
    });
  }

  Disposable subscribe;

  @Override public void onResume() {
    super.onResume();
    startPollingOrderStatus();
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
    mBinding.tvDec.setText(type + "扫描上方二维码完成支付");
    mBinding.tvQrTitle.setText(type + "收款码");
    if (type.equals("支付宝")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_ali);
    } else if (type.equals("微信")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_wechat);
    }
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(type + "收款"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
