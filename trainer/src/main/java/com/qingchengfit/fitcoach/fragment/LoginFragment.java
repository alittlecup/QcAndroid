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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
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
public class LoginFragment extends BaseFragment {
    LoginPresenter loginPresenter;

    @BindView(R.id.loginview) LoginView loginview;
    Gson gson = new Gson();

    @Inject LoginStatus loginStatus;
    MaterialDialog materialDialog;
    private Observable mObservable;
    private Unbinder unbinder;
    private View debugView;

    public LoginFragment() {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialDialog = new MaterialDialog.Builder(getActivity()).content("登录中请稍后").progress(true, 0).build();

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
                String channelid = PreferenceUtils.getPrefString(getActivity(), PushReciever.BD_CHANNELID, null);
                if (userid != null) bean.setPush_id(userid);
                if (channelid != null) bean.setPush_channel_id(channelid);
                bean.setDevice_type("android");
                materialDialog.show();
              QcCloudClient.getApi().postApi.qcLogin(bean)
                  .onBackpressureBuffer()
                  .subscribeOn(Schedulers.newThread())
                  .flatMap(qcResponLogin -> {
                    if (qcResponLogin.status == ResponseResult.SUCCESS) {

                        if (qcResponLogin.data.coach != null && qcResponLogin.data.coach.id != null) {
                            PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
                            PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
                            App.gUser = qcResponLogin.data.user;
                            PreferenceUtils.setPrefString(getActivity(), "coach", gson.toJson(qcResponLogin.data.coach));
                            App.coachid = Integer.parseInt(qcResponLogin.data.coach.id);
                            PreferenceUtils.setPrefBoolean(getActivity(), "first", false);
                            PreferenceUtils.setPrefString(getActivity(), qcResponLogin.data.coach.id + "hostarray", "");
                            loginStatus.setUserId(qcResponLogin.data.user.getId());
                            loginStatus.setSession(qcResponLogin.data.session_id);
                            loginStatus.setLoginUser(new Staff(App.gUser, App.coachid + ""));
                            return rx.Observable.just(true);
                        } else {
                            SnackbarOnUiThread("该号码未注册教练");
                            return rx.Observable.just(false);
                        }
                    } else {
                        SnackbarOnUiThread(qcResponLogin.msg);
                        return rx.Observable.just(false);
                    }
                }).subscribe(aBoolean -> {
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

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (BuildConfig.DEBUG) {
            debugView = inflater.inflate(R.layout.layout_debug, null);
            EditText et = (EditText) debugView.findViewById(R.id.et_ip);
            debugView.findViewById(R.id.test).setOnClickListener(v -> et.setText("cloudtest.qingchengfit.cn"));
            debugView.findViewById(R.id.dev).setOnClickListener(v -> et.setText("dev.qingchengfit.cn"));
            debugView.findViewById(R.id.clear).setOnClickListener(v -> et.setText(""));
            if (view instanceof LinearLayout) {
                ((LinearLayout) view).addView(debugView, 0);
            }
        }
        loginview.setLoginPresenter(loginPresenter);
        loginview.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                if (getActivity() != null) AppUtils.hideKeyboard(getActivity());
                return false;
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        if (loginview != null) loginview.unRegiste();
        super.onDestroyView();
        unbinder.unbind();
    }

    public void SnackbarOnUiThread(String msg) {
        getActivity().runOnUiThread(() -> {
            Snackbar.make(loginview, msg, Snackbar.LENGTH_LONG).show();
        });
    }
}