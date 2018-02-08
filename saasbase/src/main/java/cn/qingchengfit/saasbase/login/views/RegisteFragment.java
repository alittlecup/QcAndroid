package cn.qingchengfit.saasbase.login.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.constant.WebRouters;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.saasbase.login.bean.RegisteBody;
import cn.qingchengfit.saasbase.login.event.SendMsgEvent;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 *
 */
public class RegisteFragment extends BaseFragment implements LoginView {

  @BindView(R2.id.comple_gender_label) TextView compleGenderLabel;
  @BindView(R2.id.comple_gender_male) RadioButton compleGenderMale;
  @BindView(R2.id.comple_gender_female) RadioButton compleGenderFemale;
  @BindView(R2.id.comple_gender) RadioGroup compleGender;
  @BindView(R2.id.registe_gender) LinearLayout registeGender;
  @BindView(R2.id.registe_btn) Button registeBtn;
  @BindView(R2.id.registe_rootview) LinearLayout registeRootview;
  @BindView(R2.id.phone_num) PhoneEditText phoneNum;
  @BindView(R2.id.checkcode) PasswordView checkcode;
  @BindView(R2.id.password) PasswordView password;
  @BindView(R2.id.et_username) EditText etUsername;
  @BindView(R2.id.btn_agree_protocol) CheckBox btnAgreeProtocol;
  @BindView(R2.id.layout_protocol) LinearLayout layoutProtocol;
  private Observable<SendMsgEvent> RxObMsg;
  @Inject LoginPresenter loginPresenter;
  @Inject QcRestRepository qcRestRepository;
  @Inject IWXAPI api;

  public RegisteFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_regist, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(loginPresenter, this);

    registeBtn.setEnabled(false);
    registeGender.setOnClickListener(v -> {
      if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
        compleGender.check(R.id.comple_gender_male);
      } else {
        compleGender.check(R.id.comple_gender_female);
      }
    });

    final InternalHandler handler = new InternalHandler(getActivity());
    RxObMsg = RxBus.getBus().register(SendMsgEvent.class);
    RxObMsg.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SendMsgEvent>() {
      @Override public void call(SendMsgEvent sendMsgEvent) {
        handler.sendEmptyMessage(0);
      }
    });
    view.setOnTouchListener((v, event) -> {
      AppUtils.hideKeyboard(getActivity());
      return false;
    });
    checkcode.setOnClickListener(v -> getCode());
    return view;
  }

  /**
   * 获取验证码
   */
  public void getCode() {
    if (phoneNum.checkPhoneNum()) {
      RxBus.getBus().post(new SendMsgEvent());
      checkcode.blockRightClick(true);
      loginPresenter.queryCode(new GetCodeBody.Builder().phone(phoneNum.getPhoneNum())
          .area_code(phoneNum.getDistrictInt())
          .build());
    }
  }
  @OnClick(R2.id.btn_login_wx) public void loginWx(){
    if (!api.isWXAppInstalled()) {
      showAlert("您还未安装微信客户端");
      return;
    }
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = AppUtils.getCurAppName(getContext())+"_registe";
    api.sendReq(req);
  }

  @OnClick(R2.id.btn_agree_protocol) public void onAgree() {
    registeBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

  @OnClick(R2.id.text_protocol_detail) public void onProtocol() {
    WebActivity.startWeb(qcRestRepository.getHost() + WebRouters.USER_PROTOCOL_URL,
        getContext());
  }

  /**
   * 确认注册
   */
  @OnClick(R2.id.registe_btn) public void onRegiste() {

    if (phoneNum.checkPhoneNum() && checkcode.checkValid() && password.checkValid()) {
      loginPresenter.registe(
          new RegisteBody.Builder().username(etUsername.getText().toString().trim())
              .code(checkcode.getCode())
              .gender(compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female ? 1 : 0)
              .phone(phoneNum.getPhoneNum())
              .password(password.getCode())
              .area_code(phoneNum.getDistrictInt())
              .has_read_agreement(btnAgreeProtocol.isChecked())
              .build());
      SensorsUtils.track("QcRegister")
        .addProperty("qc_user_gender",compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female ? "1" : "0")
        .commit(getContext());

    }
  }

  @Override public void onDestroyView() {
    RxBus.getBus().unregister(SendMsgEvent.class.getName(), RxObMsg);
    super.onDestroyView();
  }

  @Override public void onShowLogining() {

  }

  @Override public void onError(String msg) {
    ToastUtils.show(msg);
  }

  @Override public void cancelLogin() {

  }

  @Override public void onSuccess(int status) {
    getActivity().setResult(Activity.RESULT_OK);
    getActivity().finish();
  }

  private class InternalHandler extends Handler {
    WeakReference<Context> context;
    int count = 60;

    InternalHandler(Context c) {
      context = new WeakReference<Context>(c);
    }

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (checkcode != null) {
        checkcode.setRightText(String.format(getString(R.string.login_resend_msg), count));
        if (count == 60) checkcode.blockRightClick(true);
        if (count > 0) {
          count--;
          this.sendEmptyMessageDelayed(0, 1000);
        } else {
          count = 60;
          checkcode.blockRightClick(false);
          checkcode.setRightText(getResources().getString(R.string.login_getcode));
        }
      }
    }
  }
}
