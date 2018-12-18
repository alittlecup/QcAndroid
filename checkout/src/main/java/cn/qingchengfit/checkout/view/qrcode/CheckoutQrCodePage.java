package cn.qingchengfit.checkout.view.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.databinding.CkCheckoutQrcodeBinding;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGContents;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGEncoder;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.zxing.WriterException;
import javax.inject.Inject;
import timber.log.Timber;

@Leaf(module = "checkout", path = "/checkout/qrcode") public class CheckoutQrCodePage
    extends CheckoutCounterFragment<CkCheckoutQrcodeBinding, CheckoutQrCodeVM> {
  @Inject GymWrapper gymWrapper;

  @Override protected void subscribeUI() {

  }

  @Override
  public CkCheckoutQrcodeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return CkCheckoutQrcodeBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initListener();
    loadImage();
  }

  private void loadImage() {
    String shop_id = gymWrapper.getCoachService().shop_id;
    String host = gymWrapper.getCoachService().host();
    QRGEncoder qrgEncoder = new QRGEncoder(host + "/shop/" + shop_id + "/qrcode/receipt/", null, QRGContents.Type.TEXT,
        MeasureUtils.dpToPx(180f, getResources()));
    try {
      Bitmap bitmap = qrgEncoder.encodeAsBitmap();
      mBinding.imgQrcode.setAdjustViewBounds(true);
      mBinding.imgQrcode.setPadding(0, 0, 0, 0);
      mBinding.imgQrcode.setImageBitmap(bitmap);
    } catch (WriterException e) {
      Timber.e(e, " qrgen");
    }
  }

  private void initListener() {
    mBinding.flPayOrders.setOnClickListener(v -> routeTo("/order/list", null));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("场馆收款二维码"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
