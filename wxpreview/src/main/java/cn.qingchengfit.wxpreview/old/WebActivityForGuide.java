package cn.qingchengfit.wxpreview.old;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.WebFragment;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.newa.MiniProgramUtil;
import cn.qingchengfit.wxpreview.old.newa.WebActivityForGuideViewModel;
import cn.qingchengfit.wxpreview.old.newa.WxPreviewEmptyActivity;
import com.anbillon.flabellum.annotations.Trunk;
import com.tbruyelle.rxpermissions.RxPermissions;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/12 2015.
 */
@Trunk(fragments = {
    CompletedConnectFragment.class,
}) public class WebActivityForGuide extends SaasCommonActivity implements FragCallBack {
  @Inject IPermissionModel permissionModel;
  @Inject GymWrapper gymWrapper;
  @Inject ViewModelProvider.Factory factory;
  private String mUrl;
  private String mCopyUrl;
  private boolean mWXSuccess;
  private String mWxName;
  private String mWxQr;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wx_activity_web_for_guide);
    findViewById(R.id.btn_help).setOnClickListener(this::onTabClick);
    findViewById(R.id.btn_config_homepage).setOnClickListener(this::onTabClick);
    findViewById(R.id.btn_poplularize).setOnClickListener(this::onTabClick);
    findViewById(R.id.btn_wx_pm).setOnClickListener(this::onTabClick);
    mCopyUrl = getIntent().getStringExtra("copyurl");
    mUrl = getIntent().getStringExtra("url");
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.web_for_guide_frag, WebFragment.newInstance(mUrl))
        .commit();
    initViewModel();
    mViewModel.loadShopDetail();
    if (AppUtils.getCurApp(this) == 0) {
      findViewById(R.id.btn_config_homepage).setVisibility(View.GONE);
      mViewModel.loadTrainerShopDetail();
    }else{
      mViewModel.loadShopDetail();

    }
  }


  private WebActivityForGuideViewModel mViewModel;

  private void initViewModel() {
    mViewModel = ViewModelProviders.of(this, factory).get(WebActivityForGuideViewModel.class);
    mViewModel.shopMutableLiveData.observe(this, shop -> {
      if (shop == null) return;
      mWXSuccess = shop.weixin_success;
      mWxName = shop.weixin;
      mWxQr = shop.weixin_image;
    });
    mViewModel.miniProgramMutableLiveData.observe(this, miniprogram -> {
      if (miniprogram != null) {
        MiniProgramUtil.saveMiniProgream(WebActivityForGuide.this, gymWrapper.getGymId(),
            miniprogram);
      }
    });
  }



  public void onTabClick(View view) {
    int i = view.getId();
    if (i == R.id.btn_help) {
      DialogUtils.showAlert(this, getString(R.string.wx_alert_title_what_is_home_page),
          getString(R.string.wx_alert_content_what_is_home_page), null);
    } else if (i == R.id.btn_config_homepage) {
      if (!permissionModel.check(PermissionServerUtils.SHOP_HOME_SETTING)) {
        DialogUtils.showAlert(this, R.string.sorry_for_no_permission);
        return;
      }

      new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
        @Override public void call(Boolean aBoolean) {
          if (aBoolean) {
            Intent toScan = new Intent(WebActivityForGuide.this, QRActivity.class);
            toScan.putExtra(QRActivity.LINK_URL, Configs.Server
                + "app2web/?id="
                + gymWrapper.id()
                + "&model="
                + gymWrapper.model()
                + "&module="
                + "");// TODO: 2017/1/21 模块名
            startActivity(toScan);
          } else {
            ToastUtils.show("请打开摄像头权限");
          }
        }
      });

      /**
       * 推广
       */
    } else if (i == R.id.btn_poplularize) {
      GymPoplularize gymPoplularize =
          GymPoplularize.newInstance(gymWrapper.name(), "", gymWrapper.photo(), mCopyUrl,
              mWXSuccess);
      gymPoplularize.setOnListItemClickListener(new GymPoplularize.GymPoplularizeListener() {
        @Override public void onBtnToWechatPublicClicked(GymPoplularize dialog) {
          dialog.dismiss();
          Bundle bundle = new Bundle();
          bundle.putString("wxQr", mWxQr);
          bundle.putString("wxName", mWxName);
          bundle.putString("mCopyUrl",mCopyUrl);
          Intent intent = new Intent(WebActivityForGuide.this, WxPreviewEmptyActivity.class);
          intent.putExtras(bundle);
          intent.putExtra("to", 1);
          startActivity(intent);
        }

        @Override public void onBtnHomeQrClicked(GymPoplularize dialog) {
          dialog.dismiss();
          Bundle bundle = new Bundle();
          bundle.putString("mUrl", mUrl);
          Intent intent = new Intent(WebActivityForGuide.this, WxPreviewEmptyActivity.class);
          intent.putExtras(bundle);
          intent.putExtra("to", 2);
          startActivity(intent);
        }

        @Override public void onBtnMorePopularizeClicked(GymPoplularize dialog) {
          dialog.dismiss();
          WebActivity.startWeb(
              "http://cloud.qingchengfit.cn/mobile/urls/eeb0e361a378428fa1a862c949495e0d/",
              WebActivityForGuide.this);
        }

        @Override public void onBtnMiniProgramClicked(GymPoplularize dialog) {
          toMiniProgramPage();
        }
      });
      gymPoplularize.show(getSupportFragmentManager(), "");
    } else if (i == R.id.btn_wx_pm) {
      toMiniProgramPage();
    }
  }

  private void toMiniProgramPage() {
    if (MiniProgramUtil.getMiniProgream(this, gymWrapper.getGymId()) != null) {
      RouteUtil.routeTo(this, "wxmini", "/show/mini", null);
    } else {
      RouteUtil.routeTo(this, "wxmini", "/mini/page", null);
    }
  }

  @Override public int getFragId() {
    return 0;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {

  }

  @Override public void cleanToolbar() {

  }

  @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

  }

  @Override public void onChangeFragment(BaseFragment fragment) {

  }

  @Override public void setBar(ToolbarBean bar) {

  }
}
