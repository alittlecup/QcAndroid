package cn.qingchengfit.pos.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.pos.login.presenter.LoginPresenter;
import cn.qingchengfit.pos.main.MainActivity;
import cn.qingchengfit.saasbase.SaasConstant;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginFragment extends BaseFragment implements LoginPresenter.MVPView {

  @BindView(R.id.tv_login_gym_name) TextView tvLoginGymName;
  @BindView(R.id.edit_login_phone) AutoCompleteTextView editLoginPhone;
  @BindView(R.id.edit_login_auth_code) EditText editLoginAuthCode;
  @BindView(R.id.login_btn) Button loginBtn;
  @BindView(R.id.btn_get_code) TextView btnGetCode;
  private TextView tvGetCode;
  @Inject LoginPresenter presenter;
  @Inject GymWrapper gymWrapper;
  private String[] autoStr;
  private String editStr;
  private Subscription sp;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    delegatePresenter(presenter, this);
    unbinder = ButterKnife.bind(this, view);
    initAutoView();
    tvGetCode = (TextView) view.findViewById(R.id.btn_get_code);
    tvLoginGymName.setText(gymWrapper.brand_name() + gymWrapper.name());
    return view;
  }

  private void initAutoView(){
    editStr = PreferenceUtils.getPrefString(getContext(), SaasConstant.LOGIN_ACCOUNT_PHONE, "");
    autoStr = editStr.split(",");
    ArrayAdapter<String> adapter =
      new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
        autoStr);
    editLoginPhone.setAdapter(adapter);
    editLoginPhone.setThreshold(2);
  }


  private void saveHistory(String phone) {
    StringBuffer tempSb = new StringBuffer();
    for (int i = 0; i < autoStr.length; i++) {
      if (i == 0) {
        tempSb.append(autoStr[i]);
      } else {
        tempSb.append("," + autoStr[i]);
      }
    }
    tempSb.append("," + phone);
    PreferenceUtils.setPrefString(getContext(), SaasConstant.LOGIN_ACCOUNT_PHONE,
      tempSb.toString());
  }

  @OnClick(R.id.btn_get_code) public void getCode() {
    if (!TextUtils.isEmpty(editLoginPhone.getText()) && AppUtils.isMobiPhoneNum(
      editLoginPhone.getText().toString())) {
      presenter.qcGetCode(editLoginPhone.getText().toString());
    } else {
      DialogUtils.showAlert(getContext(),
        getResources().getString(R.string.tips_phone_not_correct));
    }
  }

  @OnClick(R.id.login_btn) public void onLogin() {
    if (!TextUtils.isEmpty(editLoginPhone.getText())) {
      presenter.qcLogin(new LoginBody.Builder().phone(editLoginPhone.getText().toString())
        .code(editLoginAuthCode.getText().toString())
        .area_code("+86")
        .gym_id(gymWrapper.id())
        .build());
    } else {
      DialogUtils.showAlert(getContext(),
        getResources().getString(R.string.tips_phone_not_correct));
    }
  }

  @OnClick(R.id.tv_btn_login_question)
  public void onQuestion(){
    //TODO 像老司机要链接
    WebActivity.startWeb("", getContext());
  }

  @Override public void onDestroyView() {
    if (sp != null && !sp.isUnsubscribed()){
      sp.unsubscribe();
    }
    super.onDestroyView();
  }

  @Override public void onSuccess() {
    saveHistory(editLoginPhone.getText().toString());
    Intent intent = new Intent(getActivity(), MainActivity.class);
    getContext().startActivity(intent);
    getActivity().finish();
  }

  @Override public void onFailed(String s) {
    TipTextDialogFragment.newInstance(s,
      getResources().getString(R.string.dialog_confirm_login_failed),
      getResources().getString(R.string.tips_login_failed)).show(getFragmentManager(), null);
  }

  @Override public void onGetCodeSuccess() {
    if (sp != null && !sp.isUnsubscribed()){
      sp.unsubscribe();
    }
    sp = rx.Observable.interval(1, TimeUnit.SECONDS)
      .filter(new Func1<Long, Boolean>() {
        @Override public Boolean call(Long aLong) {
          return aLong <= 60;
        }
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Long>() {
        @Override public void onNext(Long count) {
          if (count < 60) {
            tvGetCode.setText(String.format(getString(R.string.login_resend_msg), 60 - count));
            tvGetCode.setClickable(false);
          }else {
            tvGetCode.setClickable(true);
            tvGetCode.setText(getResources().getString(R.string.login_getcode));
          }
        }
      });
  }

  @Override public void onGetCodeFailed(String s) {
    TipTextDialogFragment.newInstance(s,
      getResources().getString(R.string.dialog_confirm_login_failed),
      getResources().getString(R.string.tips_get_code)).show(getFragmentManager(), null);
  }


}
