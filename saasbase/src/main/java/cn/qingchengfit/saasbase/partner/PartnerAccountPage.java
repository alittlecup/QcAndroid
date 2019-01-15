package cn.qingchengfit.saasbase.partner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PagePartnerBinding;
import cn.qingchengfit.saasbase.items.GymPartnerItem;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;

@Leaf(module = "staff", path = "/partner/page") public class PartnerAccountPage
    extends SaasCommonFragment {
  @Need Boolean status;
  @Need @GymPartnerItem.GymPartnerType Integer type;
  PagePartnerBinding mBinding;
  @Inject GymWrapper gymWrapper;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    SaasbaseParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = PagePartnerBinding.inflate(inflater, container, false);
    initToolBar();
    initView();
    return mBinding.getRoot();
  }

  private void initToolBar() {
    String title = "";
    switch (type) {
      case GymPartnerItem.GymPartnerType.PARTNER_ALI:
        title = "认证支付宝";
        break;
      case GymPartnerItem.GymPartnerType.PARTNER_KOUBEI:
        title = "认证口碑";
        break;
      case GymPartnerItem.GymPartnerType.PARTNER_TAOBAO:
        title = "认证淘宝";
        break;
      case GymPartnerItem.GymPartnerType.PARTNER_MEITUAN:
        title = "认证美团点评";
        break;
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initView() {
    if (status) {
      mBinding.imgParnterMark.setVisibility(View.VISIBLE);
      mBinding.btnScan.setVisibility(View.GONE);
      mBinding.imgGymPartner.setVisibility(View.GONE);
      mBinding.imgGymPhoto.setVisibility(View.VISIBLE);
      mBinding.imgParnterMark.setImageResource(getDrawableResourse(type, status));
      Glide.with(getContext())
          .load(PhotoUtils.getSmall(gymWrapper.getCoachService().getPhoto()))
          .asBitmap()
          .placeholder(R.drawable.ic_default_header)
          .into(new CircleImgWrapper(mBinding.imgGymPhoto, getContext()));

      mBinding.tvContent.setTextSize(17f);
      mBinding.tvContent.setText("认证成功");
      mBinding.tvPath.setTextColor(getResources().getColor(R.color.text_grey));
      mBinding.tvPath.setPadding(0, MeasureUtils.dpToPx(10f, getResources()), 0, 0);
      String content = "";
      switch (type) {
        case GymPartnerItem.GymPartnerType.PARTNER_ALI:
          content = "场馆将可以参与青橙联合阿里体育、淘宝、支付宝等持续推出的阿里健身新零售的流量赋能、金融赋能、企业用户开拓";
          break;
        case GymPartnerItem.GymPartnerType.PARTNER_KOUBEI:
          content = "场馆已成为口碑网认证商将获得更多的线上曝光机会";
          break;
        case GymPartnerItem.GymPartnerType.PARTNER_TAOBAO:
          content = "场馆将可以参与青橙联合阿里体育、淘宝、支付宝等持续推出的阿里健身新零售的流量赋能、金融赋能、企业用户开拓";
          break;
        case GymPartnerItem.GymPartnerType.PARTNER_MEITUAN:
          content = "场馆已成为美团点评认证商家将获得更多的线上曝光机会";
          break;
      }
      mBinding.tvPath.setText(content);
    } else {
      mBinding.imgParnterMark.setVisibility(View.GONE);
      mBinding.btnScan.setVisibility(View.VISIBLE);
      mBinding.imgParnterMark.setVisibility(View.VISIBLE);
      mBinding.imgGymPhoto.setVisibility(View.GONE);
      mBinding.imgGymPartner.setImageResource(getDrawableResourse(type, status));

      mBinding.tvContent.setTextSize(14f);
      mBinding.tvContent.setText("请在电脑浏览器中打开以下链接，管理认证");
      mBinding.tvPath.setText("http://cloud.qingchengfit.cn");
      mBinding.tvPath.setPadding(0, MeasureUtils.dpToPx(2f, getResources()), 0, 0);
      mBinding.tvPath.setTextColor(getResources().getColor(R.color.primary));

      mBinding.btnScan.setOnClickListener(v -> {
        if (type == GymPartnerItem.GymPartnerType.PARTNER_KOUBEI) {
          QRActivity.start(getContext(), QRActivity.GYM_PARTNER_KOUBEI);
        } else if (type == GymPartnerItem.GymPartnerType.PARTNER_ALI
            || type == GymPartnerItem.GymPartnerType.PARTNER_TAOBAO) {
          QRActivity.start(getContext(), QRActivity.GYM_PARTNER_ALI);
        }
      });
    }
  }

  private int getDrawableResourse(int type, boolean status) {
    switch (type) {
      case GymPartnerItem.GymPartnerType.PARTNER_ALI:
        return status ? R.drawable.ic_gym_al_circle : R.drawable.ic_gym_ali_account;
      case GymPartnerItem.GymPartnerType.PARTNER_KOUBEI:
        return status ? R.drawable.ic_gym_kb_circle : R.drawable.ic_gym_kb_account;
      case GymPartnerItem.GymPartnerType.PARTNER_TAOBAO:
        return status ? R.drawable.ic_gym_tb_circle : R.drawable.ic_gym_tb_account;
      default:
        return status ? R.drawable.ic_gym_mt_circle : R.drawable.ic_gym_mt_account;
    }
  }
}
