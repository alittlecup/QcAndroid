package cn.qingchengfit.saasbase.login.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.constant.WebRouters;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.saasbase.login.bean.LoginBody;
import cn.qingchengfit.saasbase.login.bean.RegisteBody;
import cn.qingchengfit.saasbase.login.event.SendMsgEvent;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment
    implements CheckProtocolPresenter.MVPView, LoginView {

  @BindView(R2.id.forgetpw_btn) ToggleButton mForgetpwBtn;
  @BindView(R2.id.login_btn) Button mLoginBtn;
  Observable<SendMsgEvent> RxObMsg;
  @BindView(R2.id.root_view) LinearLayout rootView;
  @BindView(R2.id.login_phone) PhoneEditText loginPhone;
  @BindView(R2.id.pw_view) PasswordView pwView;
  @BindView(R2.id.btn_agree_protocol) CheckBox btnAgreeProtocol;
  @BindView(R2.id.layout_protocol) LinearLayout layoutProtocol;
  @Inject CheckProtocolPresenter presenter;
  @Inject LoginPresenter loginPresenter;
  @Inject QcRestRepository restRepository;
  @Inject IWXAPI api;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RxBus.getBus().register(SendAuth.Resp.class)
      .compose(bindToLifecycle())
      .onBackpressureDrop()
      .delay(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<SendAuth.Resp>() {
        @Override public void onNext(SendAuth.Resp resp) {
          LogUtil.d("code ==== "+resp.code);
          loginPresenter.loginWx(resp.code);
        }
      });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_login, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    delegatePresenter(loginPresenter, this);

    changeHost();

    view.setOnTouchListener((v, event) -> {
      if (getActivity() != null) AppUtils.hideKeyboard(getActivity());
      return false;
    });
    pwView.setOnClickListener(v -> {
      if (loginPhone.checkPhoneNum()) {
        pwView.blockRightClick(true);
        RxBus.getBus().post(new SendMsgEvent());
        loginPresenter.queryCode(new GetCodeBody.Builder().phone(loginPhone.getPhoneNum())
            .area_code(loginPhone.getDistrictInt())
            .build());
      }
    });

    loginPhone.setOnEditFocusListener(isFocus -> {
      if (!isFocus) {
        if (loginPhone != null && (loginPhone.getPhoneNum().length() == 11
            || loginPhone.getPhoneNum().length() == 10)) {
          presenter.getIsAgree(loginPhone.getPhoneNum());
        }
      }
    });

    final InternalHandler handler = new InternalHandler(getActivity());
    RxObMsg = RxBus.getBus().register(SendMsgEvent.class);
    RxObMsg.observeOn(AndroidSchedulers.mainThread()).subscribe(
      sendMsgEvent -> handler.sendEmptyMessage(0));

    /*
     * 注册
     */
    RxBusAdd(RegisteBody.class)
      .subscribe(new BusSubscribe<RegisteBody>() {
        @Override public void onNext(RegisteBody registeBody) {
          loginPresenter.registe(registeBody);
        }
      });

    RxBusAdd(LoginBody.class)
      .subscribe(new BusSubscribe<LoginBody>() {
        @Override public void onNext(LoginBody registeBody) {
          loginPresenter.doLogin(registeBody);
        }
      });
    return view;
  }

  private void changeHost(){
    if (loginPresenter.isDebug()) {
      final EditText et = new EditText(getContext());
      et.setMaxLines(1);
      String ip = PreferenceUtils.getPrefString(getContext(), "debug_ip", Constants.ServerDebug);
      if (!TextUtils.isEmpty(ip)) {
        Constants.ServerDebug = ip;
        et.setText(ip);
      }
      List<AbstractFlexibleItem> itemList = new ArrayList<>();
      final List<String> list = new ArrayList<>();
      list.add("cloudtest.qingchengfit.cn");
      list.add("cloudtest01.qingchengfit.cn");
      list.add("c1.qingchengfit.cn");
      list.add("c2.qingchengfit.cn");
      list.add("自定义");
      for (String str : list) {
        itemList.add(new SimpleTextItemItem(str));
      }
      final BottomListFragment listFragment = BottomListFragment.newInstance("选择IP");
      listFragment.loadData(itemList);
      listFragment.setListener(new BottomListFragment.ComfirmChooseListener() {
        @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
          if (selectedPos.get(0) == list.size() - 1){
            et.setText("");
            et.setHint("请输入测试环境(如：cloudtest、c1等)");
          }else{
            et.setText("http://" + list.get(selectedPos.get(0)) + "/");
          }
        }
      });
      et.setOnClickListener(v -> {
        if (!TextUtils.isEmpty(et.getText())) {
          listFragment.show(getFragmentManager(), null);
        }
      });
      final Button btnChange = new Button(getContext());
      btnChange.setText("切换Ip");
      rootView.addView(et, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          MeasureUtils.dpToPx(50f, getResources())));
      rootView.addView(btnChange, 1,
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              MeasureUtils.dpToPx(50f, getResources())));
      mLoginBtn.setEnabled(false);
      btnChange.setOnClickListener(v -> {
            String host = et.getText().toString();

            if (!et.getText().toString().contains("http")) {
              Constants.ServerDebug = "http://" + et.getText().toString().trim() + (isStartWithNumber(host)?"":".qingchengfit.cn/");
              PreferenceUtils.setPrefString(getContext(), "debug_ip",
                  Constants.ServerDebug);
            }else{
              Constants.ServerDebug = et.getText().toString().trim();
              PreferenceUtils.setPrefString(getContext(), "debug_ip",
                  Constants.ServerDebug);
            }
            restRepository.changeHost(Constants.ServerDebug);
            ToastUtils.show("修改成功");

        });
    }
  }
  public static boolean isStartWithNumber(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str.charAt(0)+"");
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  @OnClick(R2.id.btn_agree_protocol) public void onAgree() {
    mLoginBtn.setEnabled(btnAgreeProtocol.isChecked());
  }

  @OnClick(R2.id.text_protocol_detail) public void onProtocol() {
    WebActivity.startWeb(restRepository.getHost() + WebRouters.USER_PROTOCOL_URL,
        getContext());
  }
  @OnClick(R2.id.btn_login_wx) public void loginWx(){
    if (!api.isWXAppInstalled()) {
      showAlert("您还未安装微信客户端");
      return;
    }
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = AppUtils.getCurAppName(getContext())+"_login";
    api.sendReq(req);
  }

  @OnClick(R2.id.forgetpw_btn) public void toggle() {
    pwView.toggle();
  }

  @OnClick(R2.id.login_btn) public void onClick() {
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
   * 跳去初始化页面，这个操作太花式
   * @param
   */
  @Override public void toInit(String openid) {
    if (getActivity() != null){
      getActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frag,RegistInitFragment.newInstance(openid))
        .addToBackStack("")
        .commitAllowingStateLoss();
    }
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
