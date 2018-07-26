package com.qingchengfit.fitcoach.fragment.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.GuideWindow;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.schedule.SpecialWebActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import javax.inject.Inject;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/9.
 */
public class MineFragmentFragment extends BaseFragment {

  ImageView imgHeader;
  TextView tvName;
  TextView toolbarTitle;
  Toolbar toolbar;
  LinearLayout layoutMyOrders;
  Button btnLogin;
  LinearLayout layoutLogin;
  @Inject LoginStatus loginStatus;
  @Inject BaseRouter baseRouter;

  private QcCoachRespone.DataEntity.CoachEntity user;
  private Subscription sp1;
  private GuideWindow gd1;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_mine, container, false);
    imgHeader = (ImageView) view.findViewById(R.id.img_header);
    tvName = (TextView) view.findViewById(R.id.tv_name);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    layoutMyOrders = (LinearLayout) view.findViewById(R.id.layout_my_orders);
    btnLogin = (Button) view.findViewById(R.id.btn_login);
    layoutLogin = (LinearLayout) view.findViewById(R.id.layout_login);
    view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        doLogin();
      }
    });
    view.findViewById(R.id.layout_header).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFunction(v);
      }
    });
    view.findViewById(R.id.layout_my_page).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFunction(v);
      }
    });
    view.findViewById(R.id.layout_my_resume).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFunction(v);
      }
    });
    view.findViewById(R.id.layout_my_courseplan).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFunction(v);
      }
    });
    view.findViewById(R.id.layout_my_orders).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFunction(v);
      }
    });

    toolbarTitle.setText(R.string.mine);

    RxBusAdd(EventLoginChange.class).subscribe(eventLoginChange -> checkLogin());
    //checkLogin();
    return view;
  }

  public void checkLogin() {
    if (loginStatus.isLogined()) {
      layoutLogin.setVisibility(View.GONE);
      toolbar.getMenu().clear();
      toolbar.inflateMenu(R.menu.menu_setting);
      toolbar.setOnMenuItemClickListener(item -> {
        Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
        toBaseInfo.putExtra("to", 0);
        startActivity(toBaseInfo);
        return false;
      });
      if (App.gUser != null) {
        tvName.setText(App.gUser.username);
        Glide.with(getContext())
            .load(App.gUser.avatar)
            .asBitmap()
            .error(App.gUser.gender == 0 ? R.drawable.default_manage_male
                : R.drawable.default_manager_female)
            .into(new CircleImgWrapper(imgHeader, getContext()));
      }

      queryData();
    } else {
      if (gd1 != null) gd1.dismiss();
      toolbar.getMenu().clear();
      layoutLogin.setVisibility(View.VISIBLE);
    }
  }

  public void doLogin() {
    Intent toLogin = new Intent(getContext().getPackageName(),
        Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
    toLogin.putExtra("isRegiste", false);
    startActivity(toLogin);
  }

  @Override public void onResume() {
    super.onResume();
    checkLogin();
  }

  @Override protected void onVisible() {
    super.onVisible();
    if (getContext() != null && layoutMyOrders != null && getActivity() instanceof Main2Activity) {
      if (((Main2Activity) getActivity()).ordersCount > 0 && !PreferenceUtils.getPrefBoolean(
          getContext(), App.coachid + "_has_show_orders", false)) {
        showGuide();
      }
    }
  }

  @Override protected void onInVisible() {
    super.onInVisible();
    hideGuide();
  }

  public void showGuide() {
    if (loginStatus.isLogined()) {
      if (gd1 == null) gd1 = new GuideWindow(getContext(), "点击此处查看订单和门票", GuideWindow.DOWN);
      gd1.show(layoutMyOrders);
    }
  }

  public void hideGuide() {
    if (gd1 != null) gd1.dismiss();
  }

  public void queryData() {
    if (sp1 != null && !sp1.isUnsubscribed()) sp1.unsubscribe();
    sp1 = QcCloudClient.getApi().getApi.qcGetCoach(App.coachid)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<QcCoachRespone>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(QcCoachRespone qcCoachRespone) {
            if (ResponseConstant.checkSuccess(qcCoachRespone)) {
              user = qcCoachRespone.getData().getCoach();
              if (user != null && tvName != null && imgHeader != null) {
                tvName.setText(user.getUsername());
                Glide.with(getContext())
                    .load(user.getAvatar())
                    .asBitmap()
                    .error(App.gUser.gender == 0 ? R.drawable.default_manage_male
                        : R.drawable.default_manager_female)
                    .into(new CircleImgWrapper(imgHeader, getContext()));
              }
            } else {
              ToastUtils.show(qcCoachRespone.getMsg());
            }
          }
        });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClickFunction(View view) {
    if (!loginStatus.isLogined()) {
      doLogin();
      return;
    }

    switch (view.getId()) {
      case R.id.layout_header:
        //Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
        //toBaseInfo.putExtra("to", 1);
        //startActivity(toBaseInfo);
        routeTo("user", "/edit/", null);
        break;
      case R.id.layout_my_page:
        //我的主页
        Intent toStudnet = new Intent(getActivity(), SpecialWebActivity.class);
        String s = "";
        toStudnet.putExtra("url", Configs.HOST_STUDENT_PREVIEW + s);
        startActivity(toStudnet);
        break;
      case R.id.layout_my_resume:
        try {
          Intent intent = new Intent();
          intent.setPackage(getContext().getPackageName());
          intent.setData(Uri.parse("qccoach://resume/"));
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        } catch (Exception e) {

        }
        break;
      case R.id.layout_my_courseplan:
        Intent tCoursePlan = new Intent(getContext(), FragActivity.class);
        tCoursePlan.putExtra("type", 14);
        startActivity(tCoursePlan);
        break;
      case R.id.layout_my_orders:
        if (gd1 != null) {
          gd1.dismiss();
        }
        PreferenceUtils.setPrefBoolean(getContext(), App.coachid + "_has_show_orders", true);
        WebActivity.startWeb(Configs.Server + Configs.HOST_ORDERS, getContext());
        break;
    }
  }
}
