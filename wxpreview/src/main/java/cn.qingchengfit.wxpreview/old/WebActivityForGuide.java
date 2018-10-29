package cn.qingchengfit.wxpreview.old;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.views.fragments.WebFragment;
import cn.qingchengfit.widgets.CustomSwipeRefreshLayout;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.newa.MiniProgramUtil;
import cn.qingchengfit.wxpreview.old.newa.WebActivityForGuideViewModel;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Trunk;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    CompletedConnectFragment.class, ConnectWechatFragment.class, HomePageQrCodeFragment.class
}) public class WebActivityForGuide extends BaseActivity implements FragCallBack {
  @Inject IPermissionModel permissionModel;
  @Inject GymWrapper gymWrapper;
  @Inject ViewModelProvider.Factory factory;
  CompletedConnectFragment completedConnectFragment;
  HomePageQrCodeFragment homePageQrCodeFragment;
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
    initFragment();
    mCopyUrl = getIntent().getStringExtra("copyurl");
    mUrl = getIntent().getStringExtra("url");
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.web_for_guide_frag, WebFragment.newInstance(mUrl))
        .commit();
    initViewModel();
    mViewModel.loadShopDetail();
    if(AppUtils.getCurApp(this)==0){
      findViewById(R.id.btn_config_homepage).setVisibility(View.GONE);
    }
  }

  private void initFragment() {
    completedConnectFragment = new CompletedConnectFragment();
    homePageQrCodeFragment = new HomePageQrCodeFragment();
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
    mViewModel.miniProgramMutableLiveData.observe(this,miniprogram->{
      if(miniprogram!=null){
        MiniProgramUtil.saveMiniProgream(WebActivityForGuide.this,miniprogram);
      }
    });
  }

  public void doneConnectWechat() {
    completedConnectFragment.mCopyUrl = mCopyUrl;
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.other_frag, completedConnectFragment)
        .addToBackStack("")
        .commit();
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
          ConnectWechatFragment fragment = new ConnectWechatFragment();
          Bundle bundle = new Bundle();
          bundle.putString("wxQr", mWxQr);
          bundle.putString("wxName", mWxName);
          fragment.setArguments(bundle);
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.other_frag, fragment)
              .addToBackStack("wechat")
              .commit();
        }

        @Override public void onBtnHomeQrClicked(GymPoplularize dialog) {
          dialog.dismiss();
          homePageQrCodeFragment.mUrl = mUrl;
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.other_frag, homePageQrCodeFragment)
              .addToBackStack("")
              .commit();
        }

        @Override public void onBtnMorePopularizeClicked(GymPoplularize dialog) {
          dialog.dismiss();
          WebActivity.startWeb(
              "http://cloud.qingchengfit.cn/mobile/urls/eeb0e361a378428fa1a862c949495e0d/",
              WebActivityForGuide.this);
        }
      });
      gymPoplularize.show(getSupportFragmentManager(), "");
    } else if (i == R.id.btn_wx_pm) {
      if (MiniProgramUtil.getMiniProgream(this) != null) {
        RouteUtil.routeTo(this, "wxmini", "/show/mini", null);
      } else {
        RouteUtil.routeTo(this, "wxmini", "/mini/page", null);
      }
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
