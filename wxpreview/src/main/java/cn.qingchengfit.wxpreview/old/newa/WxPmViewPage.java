package cn.qingchengfit.wxpreview.old.newa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.utils.BitmapUtils;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.wxpreview.databinding.WxWxpmViewPageBinding;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;
@Leaf(module = "wxmini",path = "/mini/page")
public class WxPmViewPage extends SaasCommonFragment {
  WxWxpmViewPageBinding mBinding;
  @Inject GymWrapper gymWrapper;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = WxWxpmViewPageBinding.inflate(inflater, container, false);
    initToolbar();
    mBinding.btnStartMini.setOnClickListener(v -> goQrScan());
    mBinding.imgPic.setOnLongClickListener(v -> {
      final Bitmap drawingCache = ((BitmapDrawable)mBinding.imgPic.getDrawable()).getBitmap();
      BitmapUtils.saveImageToGallery(getContext(),drawingCache);
      ToastUtils.showDefaultStyle("已保存到相册");
      return false;
    });

    return mBinding.getRoot();
  }
  private void goQrScan(){
    Intent toScan = new Intent(getContext(), QRActivity.class);
    toScan.putExtra(QRActivity.LINK_URL, Configs.Server
        + "app2web/?id="
        + gymWrapper.getCoachService().getId()
        + "&model="
        + gymWrapper.getCoachService().getModel()
        + "&module=/mini-program");
    startActivity(toScan);
  }

  private void initToolbar() {
    mBinding.setToolbar(new ToolbarModel("小程序"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
