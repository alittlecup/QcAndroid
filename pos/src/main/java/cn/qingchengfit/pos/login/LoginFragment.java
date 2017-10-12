package cn.qingchengfit.pos.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.pos.login.presenter.LoginPresenter;
import cn.qingchengfit.pos.main.MainActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import java.lang.ref.WeakReference;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginFragment extends BaseFragment implements  LoginPresenter.MVPView {

  @BindView(R.id.tv_login_gym_name) TextView tvLoginGymName;
  @BindView(R.id.edit_login_phone) EditText editLoginPhone;
  @BindView(R.id.edit_login_auth_code) EditText editLoginAuthCode;
  @BindView(R.id.login_btn) Button loginBtn;
  @BindView(R.id.btn_get_code) TextView btnGetCode;
  private TextView tvGetCode;
  private InternalHandler handler;
  @Inject LoginPresenter presenter;


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    unbinder = ButterKnife.bind(this, view);
    tvGetCode = (TextView) view.findViewById(R.id.btn_get_code);
    handler = new InternalHandler(getContext());
    return view;
  }

  @OnClick(R.id.btn_get_code)
  public void getCode(){
    handler.sendEmptyMessage(0);
  }

  @OnClick(R.id.login_btn)
  public void onLogin(){
    if (!TextUtils.isEmpty(editLoginPhone.getText()) && !TextUtils.isEmpty(
        editLoginAuthCode.getText())) {
      presenter.qcLogin(new LoginBody(editLoginPhone.getText().toString(),
          editLoginAuthCode.getText().toString()));
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onSuccess() {
    Intent intent = new Intent(getActivity(), MainActivity.class);
    getContext().startActivity(intent);
  }

  @Override public void onFailed(String s) {
    TipTextDialogFragment.newInstance(s,
        getResources().getString(R.string.dialog_confirm_login_failed),
        getResources().getString(R.string.tips_login_failed)).show(getFragmentManager(), null);
  }

  private class InternalHandler extends Handler {
    WeakReference<Context> context;
    //use for send msg interval 60s
    int count = 60;

    InternalHandler(Context c) {
      context = new WeakReference<Context>(c);
    }

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (tvGetCode != null) {
        tvGetCode.setText(String.format(getString(R.string.login_resend_msg), count));
        if (count == 60) tvGetCode.setClickable(true);
        if (count > 0) {
          count--;
          this.sendEmptyMessageDelayed(0, 1000);
        } else {
          count = 60;
          tvGetCode.setClickable(false);
          tvGetCode.setText(getResources().getString(R.string.login_getcode));
        }
      }
    }
  }
}
