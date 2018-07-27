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
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutPayBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.qrcode.model.IOrderData;
import cn.qingchengfit.checkout.view.scan.QcScanActivity;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.activity.WebActivity;
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
      if (subscribe != null && !subscribe.isDisposed()) {
        subscribe.dispose();
        subscribe = null;
      }
      switch (orderStatusBean.getStatus()) {
        case 0:
          WebActivity.startWeb(orderStatusBean.getFail_url(), getContext());
          break;
        case 2:
          WebActivity.startWeb(orderStatusBean.getSuccess_url(), getContext());
          break;
      }
    });
  }

  @Override
  public CkPageCheckoutPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutPayBinding.inflate(inflater, container, false);
    orderData = getArguments().getParcelable("orderData");
    initToolbar();
    initUI();
    initListener();
    initQrCode();
    return mBinding;
  }

  private void initQrCode() {

    QRGEncoder qrgEncoder = new QRGEncoder(orderData.getQrCodeUri(), null, QRGContents.Type.TEXT, MeasureUtils.dpToPx(180f, getResources()));
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
      Intent intent = new Intent(getContext(), QcScanActivity.class);
      intent.putExtra("title", "扫码收款");
      intent.putExtra("tyoe", type);
      intent.putExtra("repay", orderData.getScanRePayInfo());
      startActivity(intent);
    });

  }

  Disposable subscribe;

  @Override public void onResume() {
    super.onResume();
    subscribe = Flowable.interval(1000, 200, TimeUnit.MILLISECONDS)
        .subscribe(aLong -> mViewModel.pollingOrderresult(orderData.getPollingNUmber()));
  }

  @Override public void onPause() {
    super.onPause();
    if (subscribe != null && !subscribe.isDisposed()) {
      subscribe.dispose();
      subscribe = null;
    }
  }

  private void initUI() {
    mBinding.tvDec.setText(type + "扫描上方二维码完成支付");
    mBinding.tvQrTitle.setText(type + "收款码");
    mBinding.tvCheckoutMoney.setText("￥" + String.valueOf(orderData.getPrices()));
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
