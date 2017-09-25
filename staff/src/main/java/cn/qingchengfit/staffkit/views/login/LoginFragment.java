package cn.qingchengfit.staffkit.views.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.SendMsgEvent;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.LoginBody;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment
    implements CheckProtocolPresenter.MVPView, LoginView {

  @BindView(R.id.forgetpw_btn) ToggleButton mForgetpwBtn;
  @BindView(R.id.login_btn) Button mLoginBtn;
  Observable<SendMsgEvent> RxObMsg;
  @BindView(R.id.root_view) LinearLayout rootView;
  @BindView(R.id.login_phone) PhoneEditText loginPhone;
  @BindView(R.id.pw_view) PasswordView pwView;
  @BindView(R.id.btn_agree_protocol) CheckBox btnAgreeProtocol;
  @BindView(R.id.layout_protocol) LinearLayout layoutProtocol;
  @Inject CheckProtocolPresenter presenter;
  @Inject LoginPresenter loginPresenter;
  @Inject QcRestRepository restRepository;

  public LoginFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    delegatePresenter(loginPresenter, this);
    if (BuildConfig.DEBUG) {
      final EditText et = new EditText(getContext());
      String ip = PreferenceUtils.getPrefString(getContext(), "debug_ip", Configs.Server);
      if (!TextUtils.isEmpty(ip)) {
        Configs.Server = ip;
        et.setHint("当前IP:" + ip);
      }
      final Button btnChange = new Button(getContext());
      btnChange.setText("切换Ip");
      rootView.addView(et, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          MeasureUtils.dpToPx(50f, getResources())));
      rootView.addView(btnChange, 1,
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              MeasureUtils.dpToPx(50f, getResources())));
      mLoginBtn.setEnabled(false);
      btnChange.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (getActivity().getApplication() instanceof App) {
            if (!TextUtils.isEmpty(et.getText())) {
              PreferenceUtils.setPrefString(getContext(), "debug_ip",
                  "http://" + et.getText().toString().trim() + "/");
              Configs.Server = "http://" + et.getText().toString().trim() + "/";
              ((App) (getActivity().getApplication())).initInjcet();
              ToastUtils.show("修改成功");
            }
          }
        }
      });
    }

    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (getActivity() != null) AppUtils.hideKeyboard(getActivity());
        return false;
      }
    });
    pwView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (loginPhone.checkPhoneNum()) {
          pwView.blockRightClick(true);
          RxBus.getBus().post(new SendMsgEvent());
          loginPresenter.queryCode(new GetCodeBody.Builder().phone(loginPhone.getPhoneNum())
              .area_code(loginPhone.getDistrictInt())
              .build());
        }
      }
    });

    loginPhone.setOnEditFocusListener(new PhoneEditText.OnEditFocusListener() {
      @Override public void onFocusChange(boolean isFocus) {
        if (!isFocus) {
          if (loginPhone != null && (loginPhone.getPhoneNum().length() == 11
              || loginPhone.getPhoneNum().length() == 10)) {
            //TODO 请求是否勾选过协议
            presenter.getIsAgree(loginPhone.getPhoneNum());
          }
        }
      }
    });

    final InternalHandler handler = new InternalHandler(getActivity());
    RxObMsg = RxBus.getBus().register(SendMsgEvent.class);
    RxObMsg.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SendMsgEvent>() {
      @Override public void call(SendMsgEvent sendMsgEvent) {
        handler.sendEmptyMessage(0);
      }
    });
    return view;
  }

  @OnClick(R.id.btn_agree_protocol) public void onAgree() {
    mLoginBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

  @OnClick(R.id.text_protocol_detail) public void onProtocol() {
    WebActivity.startWeb(restRepository.getHost() + GymFunctionFactory.USER_PROTOCOL_URL,
        getContext());
  }

  @OnClick(R.id.forgetpw_btn) public void toggle() {
    pwView.toggle();
  }

  @OnClick(R.id.login_btn) public void onClick() {
    if (loginPhone.checkPhoneNum() && pwView.checkValid()) {
      loginPresenter.doLogin(new LoginBody.Builder().phone(loginPhone.getPhoneNum())
          .code(pwView.isPwMode() ? null : pwView.getCode())
          .password(pwView.isPwMode() ? pwView.getCode() : null)
          .area_code(loginPhone.getDistrictInt())
          .has_read_agreement(btnAgreeProtocol.isChecked())
          .build());
    } else {

    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onCheck(boolean isAgree) {
    if (!btnAgreeProtocol.isChecked()) {
      btnAgreeProtocol.setChecked(isAgree);
      mLoginBtn.setEnabled(btnAgreeProtocol.isChecked());
    }
  }

  @Override public void showAlert(String s) {
  }

  @Override public void onShowLogining() {
    RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
  }

  @Override public void onError(String msg) {
    ToastUtils.show(msg);
  }

  @Override public void cancelLogin() {
    RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
  }

  @Override public void onSuccess(int status) {
    getActivity().setResult(Activity.RESULT_OK);
    getActivity().finish();
  }

  /**
   * time count for send msg interval
   */
  private class InternalHandler extends Handler {
    WeakReference<Context> context;
    //use for send msg interval 60s
    int count = 60;

    InternalHandler(Context c) {
      context = new WeakReference<Context>(c);
    }

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (pwView != null && !pwView.isPwMode()) {
        pwView.setRightText(String.format(getString(R.string.login_resend_msg), count));
        if (count == 60) pwView.blockRightClick(true);
        if (count > 0) {
          count--;
          this.sendEmptyMessageDelayed(0, 1000);
        } else {
          count = 60;
          pwView.blockRightClick(false);
          pwView.setRightText(getResources().getString(R.string.login_getcode));
        }
      }
    }
  }
}
