package com.qingchengfit.fitcoach.fragment.unlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/5/19.
 */
public class UnLoginScheduleAdFragment extends BaseFragment {

  Button btnUseNow;
  Button btnLogin;
  @Inject LoginStatus loginStatus;
  ImageView img;
  TextView tv1;
  LinearLayout llLogin, llGym;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_unlog_schedules, container, false);
    btnUseNow = (Button) view.findViewById(R.id.btn_use_now);
    btnLogin = (Button) view.findViewById(R.id.btn_login);
    img = (ImageView) view.findViewById(R.id.img);
    tv1 = (TextView) view.findViewById(R.id.tv1);
    view.findViewById(R.id.btn_use_now).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickUseNow();
      }
    });
    view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onLogin();
      }
    });
    llGym = view.findViewById(R.id.ll_add_gym);
    llLogin = view.findViewById(R.id.ll_login);

    Glide.with(getContext()).load(R.drawable.img_guide2).into(img);
    tv1.setText(R.string.str_guide_title_1);
    loginChange(loginStatus.isLogined());
    RxBusAdd(EventLoginChange.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(eventLoginChange -> {
          loginChange(loginStatus.isLogined());
        }, new HttpThrowable());
    view.findViewById(R.id.btn_add_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("gym", "/gym/search", null);
      }
    });
    view.findViewById(R.id.tv_create_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("gym", "/gym/create", null);
      }
    });
    return view;
  }

  @Override public String getFragmentName() {
    return UnLoginScheduleAdFragment.class.getName();
  }

  private void loginChange(boolean isLogin) {
    llLogin.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    llGym.setVisibility(isLogin ? View.VISIBLE : View.GONE);
  }

  /**
   * 注册
   */
  public void onClickUseNow() {
    if (!loginStatus.isLogined()) {
      Intent toLogin = new Intent(getContext().getPackageName(),
          Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
      toLogin.putExtra("isRegiste", true);
      startActivity(toLogin);
    } else {
      startActivity(new Intent(getActivity(), GuideActivity.class));
    }
  }

  /**
   * 立即登录
   */
  public void onLogin() {
    Intent toLogin = new Intent(getContext().getPackageName(),
        Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
    toLogin.putExtra("isRegiste", false);
    startActivity(toLogin);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
