package cn.qingchengfit.staffkit.views.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventFreshUnloginAd;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Paper on 16/2/16 2016.
 */
public class SettingFragment extends BaseFragment implements SettingView {
  public static final int RESULT_LOGIN = 1;
  ImageView headerIcon;
  TextView drawerName;
  TextView toolbarTitile;
  Toolbar toolbar;
  CommonInputView civBrands;

  @Inject SettingPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject BaseRouter baseRouter;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting, container, false);
    headerIcon = (ImageView) view.findViewById(R.id.header_icon);
    drawerName = (TextView) view.findViewById(R.id.drawer_name);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    civBrands = view.findViewById(R.id.civ_my_gyms);
    view.findViewById(R.id.drawer_headerview).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onHeader();
      }
    });

    view.findViewById(R.id.civ_resume).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMyResume();
      }
    });
    view.findViewById(R.id.civ_orders).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onOders();
      }
    });
    view.findViewById(R.id.civ_my_gyms).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!loginStatus.isLogined()) {
          baseRouter.toLogin(SettingFragment.this);
          return;
        }
        routeTo("gym", "/my/gyms", null);
      }
    });

    initToolbar(toolbar);
    delegatePresenter(presenter, this);

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.setNavigationIcon(null);
    toolbarTitile.setText("我");
    toolbar.inflateMenu(R.menu.menu_setting);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        routeTo("setting", "/setting/detail", null);
        return false;
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    onVisible();
  }

  @Override protected void onVisible() {
    super.onVisible();
    if (getContext() != null) {
      if (!loginStatus.isLogined()) {
        /*
         * 未登陆用户
         */
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(Configs.HEADER_COACH_MALE))
            .asBitmap()
            .into(new CircleImgWrapper(headerIcon, getContext()));
        drawerName.setText("未登录");
        onBrandsCount(0);
      } else {
        /*
         * 已登录用户
         */
        presenter.getSelfInfo();
      }
    }
  }

  public void onHeader() {
    if (!loginStatus.isLogined()) {
      goLogin();
      return;
    }
    routeTo("user", "/edit/", null);
  }

  public void onMyResume() {
    if (!loginStatus.isLogined()) {
      baseRouter.toLogin(this);
      return;
    }
    baseRouter.routerTo("recruit", "resume", getContext(), 1001);
  }

  public void onOders() {
    if (!loginStatus.isLogined()) {
      baseRouter.toLogin(this);
      return;
    }
    BaseRouter.routerToWeb(Configs.Server + Configs.HOST_ORDERS, getContext());
  }

  @Override public String getFragmentName() {
    return SettingFragment.class.getName();
  }

  @Override public void onSelfInfo(Staff bean) {
    hideLoading();
    loginStatus.setLoginUser(bean);
    Glide.with(getContext())
        .load(PhotoUtils.getSmall(bean.getAvatar()))
        .asBitmap()
        .placeholder(bean.getGender() == 0 ? R.drawable.default_manage_male
            : R.drawable.default_manager_female)
        .into(new CircleImgWrapper(headerIcon, getContext()));
    drawerName.setText(bean.getUsername());
    PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_NAME, bean.getUsername());
    if (!BuildConfig.DEBUG) {
      try {
        JSONObject properties = SensorsDataAPI.sharedInstance(getActivity().getApplicationContext())
            .getSuperProperties();
        if (properties == null) properties = new JSONObject();
        properties.put("qc_user_id", loginStatus.getUserId());
        SensorsDataAPI.sharedInstance(getActivity().getApplicationContext())
            .registerSuperProperties(properties);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @Override public void onBrandsCount(int count) {
    if (count <= 0) {
      civBrands.setContent("");
    } else {
      civBrands.setContent(count + "家");
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      showLoading();
      presenter.getSelfInfo();
      //发送消息告诉未登录页面已登录
      RxBus.getBus().post(new EventFreshUnloginAd());
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  void goLogin() {
    Intent toLogin = new Intent(getContext().getPackageName(),
        Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
    toLogin.putExtra("isRegiste", false);
    toLogin.putExtra("setting", true);
    startActivityForResult(toLogin, RESULT_LOGIN);
  }
}
