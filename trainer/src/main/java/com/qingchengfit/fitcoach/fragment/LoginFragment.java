package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.fragment.protocol.CheckProtocolPresenter;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.MutiSysSession;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.reciever.PushReciever;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements CheckProtocolPresenter.MVPView {
  LoginPresenter loginPresenter;

  @BindView(R.id.loginview) LoginView loginview;

  Gson gson = new Gson();
  @Inject LoginStatus loginStatus;
  @Inject CheckProtocolPresenter protocolPresenter;
  @Inject QcRestRepository restRepository;
  MaterialDialog materialDialog;
  @BindView(R.id.phone_view) PhoneEditText phoneView;
  @BindView(R.id.pw_view) PasswordView pwView;
  @BindView(R.id.text_protocol_detail) TextView textProtocolDetail;
  @BindView(R.id.forgetpw_btn) ToggleButton forgetpwBtn;
  @BindView(R.id.goregiste_btn) Button goregisteBtn;
  @BindView(R.id.btn_agree_protocol) CheckBox btnAgreeProtocol;
  @BindView(R.id.layout_protocol) LinearLayout layoutProtocol;
  @BindView(R.id.login_btn) Button loginBtn;
  private Observable mObservable;
  private View debugView;

  public LoginFragment() {

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    materialDialog =
        new MaterialDialog.Builder(getActivity()).content("登录中请稍后").progress(true, 0).build();

    loginPresenter = new LoginPresenter() {
      @Override public void onPhoneDone() {

      }

      @Override public void doLogin(String arCode, String account, String code) {
        if (BuildConfig.DEBUG) {
          EditText et = (EditText) debugView.findViewById(R.id.et_ip);
          if (!TextUtils.isEmpty(et.getText())) {
            Configs.Server = "http://" + et.getText().toString().trim() + "/";
            PreferenceUtils.setPrefString(getContext(), "debug_ip", Configs.Server);
          }
        }
        List<MutiSysSession> systems = new ArrayList<>();
        LoginBean bean = new LoginBean();
        bean.setPhone(account);
        bean.setArea_code(arCode);
        if (!loginview.isPassword()) {
          bean.setCode(code);
        } else {
          bean.setPassword(code);
        }
        String userid = PreferenceUtils.getPrefString(getActivity(), PushReciever.BD_USERLID, null);
        String channelid =
            PreferenceUtils.getPrefString(getActivity(), PushReciever.BD_CHANNELID, null);
        if (userid != null) bean.setPush_id(userid);
        if (channelid != null) bean.setPush_channel_id(channelid);
        bean.setHas_read_agreement(btnAgreeProtocol.isChecked());
        bean.setDevice_type("android");
        materialDialog.show();
        QcCloudClient.getApi().postApi.qcLogin(bean)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.newThread())
            .flatMap(qcResponLogin -> {
              if (qcResponLogin.status == ResponseResult.SUCCESS) {

                if (qcResponLogin.data.coach != null && qcResponLogin.data.coach.id != null) {
                  PreferenceUtils.setPrefString(getActivity(), "session_id",
                      qcResponLogin.data.session_id);
                  PreferenceUtils.setPrefString(getActivity(), "user_info",
                      gson.toJson(qcResponLogin.data.user));
                  App.gUser = qcResponLogin.data.user;
                  PreferenceUtils.setPrefString(getActivity(), "coach",
                      gson.toJson(qcResponLogin.data.coach));
                  App.coachid = Integer.parseInt(qcResponLogin.data.coach.id);
                  PreferenceUtils.setPrefBoolean(getActivity(), "first", false);
                  PreferenceUtils.setPrefString(getActivity(),
                      qcResponLogin.data.coach.id + "hostarray", "");
                  loginStatus.setUserId(qcResponLogin.data.user.getId());
                  loginStatus.setSession(qcResponLogin.data.session_id);
                  loginStatus.setLoginUser(new Staff(App.gUser, App.coachid + ""));
                  return Observable.just(true);
                } else {
                  SnackbarOnUiThread("该号码未注册教练");
                  return Observable.just(false);
                }
              } else {
                SnackbarOnUiThread(qcResponLogin.msg);
                return Observable.just(false);
              }
            })
            .subscribe(aBoolean -> {
              if (aBoolean) {
                Intent toMain = new Intent(getActivity(), FragActivity.class);
                toMain.putExtra("type", 10);
                startActivity(toMain);
                materialDialog.dismiss();
                getActivity().finish();
              } else {
                materialDialog.dismiss();
              }
            }, throwable -> {
            }, () -> {
            });
      }

      @Override public void getCode(GetCodeBean account) {
        if (BuildConfig.DEBUG) {
          EditText et = (EditText) debugView.findViewById(R.id.et_ip);
          if (!TextUtils.isEmpty(et.getText())) {
            Configs.Server = "http://" + et.getText().toString().trim() + "/";
            PreferenceUtils.setPrefString(getContext(), "debug_ip", Configs.Server);
          }
        }
        QcCloudClient.getApi().postApi.qcGetCode(account)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.newThread())
            .subscribe(qcResponse -> {
              if (qcResponse.status == ResponseResult.SUCCESS) {
                LogUtil.d("send msg success!");
              } else {
                LogUtil.d(qcResponse.msg);
                Toast.makeText(getActivity(), qcResponse.msg, Toast.LENGTH_SHORT).show();
                SnackbarOnUiThread(qcResponse.msg);
              }
            }, throwable -> {
            }, () -> {
            });
      }

      @Override public void goRegister() {
        //                getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));
      }
    };
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(protocolPresenter, this);
    if (BuildConfig.DEBUG) {
      debugView = inflater.inflate(R.layout.layout_debug, null);
      EditText et = (EditText) debugView.findViewById(R.id.et_ip);
      debugView.findViewById(R.id.test)
          .setOnClickListener(v -> et.setText("cloudtest.qingchengfit.cn"));
      debugView.findViewById(R.id.dev).setOnClickListener(v -> et.setText("dev.qingchengfit.cn"));
      debugView.findViewById(R.id.clear).setOnClickListener(v -> et.setText(""));
      if (view instanceof LinearLayout) {
        ((LinearLayout) view).addView(debugView, 0);
      }
    }
    loginBtn.setEnabled(false);
    loginview.setLoginPresenter(loginPresenter);
    loginview.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (getActivity() != null) AppUtils.hideKeyboard(getActivity());
        return false;
      }
    });

    phoneView.setOnEditFocusListener(new PhoneEditText.OnEditFocusListener() {
      @Override public void onFocusChange(boolean isFocus) {
        if (!isFocus) {
          if (phoneView != null && ((phoneView.getPhoneNum().length() == 11
              || phoneView.getPhoneNum().length() == 10))) {
            protocolPresenter.getIsAgree(phoneView.getPhoneNum());
          }
        }
      }
    });

    return view;
  }

  @OnClick(R.id.btn_agree_protocol) public void onAgree() {
    loginBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

  @OnClick(R.id.text_protocol_detail) public void onProtocol() {
    WebActivity.startWeb(restRepository.getHost() + Configs.USER_PROTOCOL_URL, getContext());
  }

  @Override public void onDestroyView() {
    if (loginview != null) loginview.unRegiste();
    super.onDestroyView();
  }

  public void SnackbarOnUiThread(String msg) {
    getActivity().runOnUiThread(() -> {
      Snackbar.make(loginview, msg, Snackbar.LENGTH_LONG).show();
    });
  }

  @Override public void onCheck(boolean isAgree) {
    if (!btnAgreeProtocol.isChecked()) {
      btnAgreeProtocol.setChecked(isAgree);
      loginBtn.setEnabled(btnAgreeProtocol.isChecked());
    }
  }
}
