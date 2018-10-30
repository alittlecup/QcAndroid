package cn.qingchengfit.wxpreview.old.newa;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.WxShareUtil;
import cn.qingchengfit.wxpreview.databinding.WxMiniShowPageBinding;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;

@Leaf(module = "wxmini", path = "/show/mini") public class WxMiniProgramPage
    extends SaasCommonFragment {
  WxMiniShowPageBinding mBinding;
  @Inject GymWrapper gymWrapper;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = WxMiniShowPageBinding.inflate(inflater, container, false);
    initToolbar();
    initView();
    initListener();
    return mBinding.getRoot();
  }

  private void initListener() {
    mBinding.btSaveImg.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        saveImage();
      }
    });
    mBinding.btShare.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        GymShortShareDialog dialog = new GymShortShareDialog();
        dialog.setListener(new GymShortShareDialog.onWxShareClickListener() {
          @Override public void onWxShareClick(DialogFragment dialog, boolean isFriend) {
            dialog.dismiss();
            mBinding.rlContainer.setDrawingCacheEnabled(true);
            mBinding.rlContainer.buildDrawingCache();
            final Bitmap drawingCache = mBinding.rlContainer.getDrawingCache();
            WxShareUtil.shareBitmap(getContext(), drawingCache, isFriend);
            mBinding.rlContainer.destroyDrawingCache();
          }
        });
        dialog.show(getChildFragmentManager(), "");
      }
    });
  }

  private void saveImage() {
    mBinding.rlContainer.setDrawingCacheEnabled(true);
    mBinding.rlContainer.buildDrawingCache();
    new Handler().post(new Runnable() {
      @Override public void run() {
        final Bitmap drawingCache = mBinding.rlContainer.getDrawingCache();
        FileUtils.savePicture(drawingCache, gymWrapper.getCoachService().getName() + "_qrcode.jpg");
        mBinding.rlContainer.destroyDrawingCache();
      }
    });
  }

  private void initView() {
    MiniProgram miniProgream = MiniProgramUtil.getMiniProgream(getContext(), gymWrapper.getGymId());
    if (miniProgream != null) {
      mBinding.tvGymName.setText(gymWrapper.getBrand().getName());
      mBinding.tvSerivceName.setText(miniProgream.nick_name);
      PhotoUtils.smallCircle(mBinding.imgGymPhoto, miniProgream.logo_url);
      PhotoUtils.smallCircle(mBinding.imgMiniQr, miniProgream.qrcode_url);
    }
  }

  private void initToolbar() {
    mBinding.setToolbar(new ToolbarModel("小程序"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
