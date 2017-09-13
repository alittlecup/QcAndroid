package cn.qingchengfit.staffkit.views.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SendMsgEvent;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.RegisteBody;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.AppUtils;
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
public class RegisteFragment extends BaseFragment {

  @BindView(R.id.comple_gender_label) TextView compleGenderLabel;
  @BindView(R.id.comple_gender_male) RadioButton compleGenderMale;
  @BindView(R.id.comple_gender_female) RadioButton compleGenderFemale;
  @BindView(R.id.comple_gender) RadioGroup compleGender;

  @BindView(R.id.registe_gender) LinearLayout registeGender;
  @BindView(R.id.registe_btn) Button registeBtn;
  @BindView(R.id.registe_rootview) LinearLayout registeRootview;
  @BindView(R.id.phone_num) PhoneEditText phoneNum;
  @BindView(R.id.checkcode) PasswordView checkcode;
  @BindView(R.id.password) PasswordView password;
  @BindView(R.id.et_username) EditText etUsername;
  @BindView(R.id.btn_agree_protocol) CheckBox btnAgreeProtocol;
  @BindView(R.id.layout_protocol) LinearLayout layoutProtocol;
  private Observable<SendMsgEvent> RxObMsg;
  @Inject LoginPresenter loginPresenter;
  @Inject QcRestRepository qcRestRepository;

  public RegisteFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_registe, container, false);
    unbinder = ButterKnife.bind(this, view);
    registeBtn.setEnabled(false);
    registeGender.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
          compleGender.check(R.id.comple_gender_male);
        } else {
          compleGender.check(R.id.comple_gender_female);
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
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        AppUtils.hideKeyboard(getActivity());
        return false;
      }
    });
    checkcode.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getCode();
      }
    });
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

  @OnClick(R.id.btn_agree_protocol) public void onAgree() {
    registeBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

  @OnClick(R.id.text_protocol_detail) public void onProtocol() {
    WebActivity.startWeb(qcRestRepository.getHost() + GymFunctionFactory.USER_PROTOCOL_URL,
        getContext());
  }

  /**
   * 确认注册
   */
  @OnClick(R.id.registe_btn) public void onRegiste() {

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
    }
  }

  @Override public void onDestroyView() {
    RxBus.getBus().unregister(SendMsgEvent.class.getName(), RxObMsg);
    super.onDestroyView();
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
