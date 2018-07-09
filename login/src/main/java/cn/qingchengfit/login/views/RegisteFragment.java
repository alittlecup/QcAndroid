package cn.qingchengfit.login.views;

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



import cn.qingchengfit.RxBus;
import cn.qingchengfit.login.LoConstants;
import cn.qingchengfit.login.R;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.event.SendMsgEvent;
import cn.qingchengfit.network.QcRestRepository;


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

	TextView compleGenderLabel;
	RadioButton compleGenderMale;
	RadioButton compleGenderFemale;
	RadioGroup compleGender;
	LinearLayout registeGender;
	Button registeBtn;
	LinearLayout registeRootview;
	PhoneEditText phoneNum;
	PasswordView checkcode;
	PasswordView password;
	EditText etUsername;
	CheckBox btnAgreeProtocol;
	LinearLayout layoutProtocol;
  private Observable<SendMsgEvent> RxObMsg;
  @Inject LoginPresenter loginPresenter;
  @Inject QcRestRepository qcRestRepository;
  @Inject IWXAPI api;

  public RegisteFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_regist, container, false);
    compleGenderLabel = view.findViewById(R.id.comple_gender_label);
    compleGenderMale = view.findViewById(R.id.comple_gender_male);
    compleGenderFemale = view.findViewById(R.id.comple_gender_female);
    compleGender = view.findViewById(R.id.comple_gender);
    registeGender = view.findViewById(R.id.registe_gender);
    registeBtn = view.findViewById(R.id.registe_btn);
    registeRootview = view.findViewById(R.id.registe_rootview);
    phoneNum = view.findViewById(R.id.phone_num);
    checkcode = view.findViewById(R.id.checkcode);
    password = view.findViewById(R.id.password);
    etUsername = view.findViewById(R.id.et_username);
    btnAgreeProtocol = view.findViewById(R.id.btn_agree_protocol);
    layoutProtocol = view.findViewById(R.id.layout_protocol);
    view.findViewById(R.id.btn_login_wx).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        loginWx();
      }
    });
    view.findViewById(R.id.btn_agree_protocol).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onAgree();
      }
    });
    view.findViewById(R.id.text_protocol_detail).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onProtocol();
      }
    });
    view.findViewById(R.id.registe_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onRegiste();
      }
    });

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
 public void loginWx(){
    if (!api.isWXAppInstalled()) {
      showAlert("您还未安装微信客户端");
      return;
    }
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = AppUtils.getCurAppName(getContext())+"_registe";
    api.sendReq(req);
  }

 public void onAgree() {
    registeBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

 public void onProtocol() {
    WebActivity.startWeb(qcRestRepository.getHost() + LoConstants.USER_PROTOCOL_URL,
        getContext());
  }

  /**
   * 确认注册
   */
 public void onRegiste() {

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

  @Override public void toInit(String openid) {

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
