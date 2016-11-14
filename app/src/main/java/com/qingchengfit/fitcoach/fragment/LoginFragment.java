package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.MutiSysSession;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.reciever.PushReciever;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    LoginPresenter loginPresenter;

    @Bind(R.id.loginview)
    LoginView loginview;
    Gson gson = new Gson();

    MaterialDialog materialDialog;
    private Observable mObservable;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialDialog = new MaterialDialog.Builder(getActivity())
                .content("登录中请稍后")
                .progress(true, 0)
                .build();

        loginPresenter = new LoginPresenter() {
            @Override
            public void onPhoneDone() {

            }

            @Override
            public void doLogin(String account, String code) {
//                Uri uri = Uri.fromParts("weixin", "wxpay/bizpayurl", "appid=wxc6a77068671948fa&mch_id=1229776802&nonce_str=ZTvdtqVJwKbe5Ua&product_id=122&time_stamp=1440998807&sign=461F09C69743638397CFEA2422516656");
//                Uri uri = Uri.parse("weixin://wxpay/bizpayurl?appid=wxc6a77068671948fa&mch_id=1229776802&nonce_str=ZTvdtqVJwKbe5Ua&product_id=122&time_stamp=1440998807&sign=461F09C69743638397CFEA2422516656");
//                Intent toWeixin = new Intent(Intent.ACTION_VIEW);
//                toWeixin.setData(uri);
//                getActivity().startActivity(toWeixin);
//                return;
//
// Intent toMain = new Intent(getActivity(), MainActivity.class);
//                startActivity(toMain);
//                getActivity().finish();
                List<MutiSysSession> systems = new ArrayList<>();
                LoginBean bean = new LoginBean();
                bean.setPhone(account);
                if (loginview.mGetCodeBtn.getVisibility() == View.VISIBLE)
                    bean.setCode(code);
                else
                    bean.setPassword(code);
                String userid = PreferenceUtils.getPrefString(getActivity(), PushReciever.BD_USERLID, null);
                String channelid = PreferenceUtils.getPrefString(getActivity(), PushReciever.BD_CHANNELID, null);
                if (userid != null)
                    bean.setPush_id(userid);
                if (channelid != null)
                    bean.setPush_channel_id(channelid);
                bean.setDevice_type("android");
                materialDialog.show();
                QcCloudClient.getApi()
                        .postApi
                        .qcLogin(bean)
                        .subscribeOn(Schedulers.newThread())
                        .flatMap(qcResponLogin -> {
                            if (qcResponLogin.status == ResponseResult.SUCCESS) {
                                if (qcResponLogin.data.coach != null && qcResponLogin.data.coach.id != null) {
                                    PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
                                    PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
                                    PreferenceUtils.setPrefString(getActivity(), "coach", gson.toJson(qcResponLogin.data.coach));
                                    PreferenceUtils.setPrefBoolean(getActivity(), "first", false);
                                    PreferenceUtils.setPrefString(getActivity(), qcResponLogin.data.coach.id + "hostarray", "");
                                    return rx.Observable.just(true);
                                } else {
                                    SnackbarOnUiThread("该号码未注册教练");
                                    return rx.Observable.just(false);
                                }
                            } else {
                                SnackbarOnUiThread(qcResponLogin.msg);
                                return rx.Observable.just(false);
                            }
                        })
//                        .flatMap(qcResponLogin -> {
//                            if (qcResponLogin != null) {
//                                return QcCloudClient.getApi().getApi.qcGetSystem(qcResponLogin.data.coach.id, "session_id=" + qcResponLogin.data.session_id);
//                            } else return null;
//                        })
//                        .flatMap(qcResponCoachSys -> {
//                            if (qcResponCoachSys != null && qcResponCoachSys.getData() != null && qcResponCoachSys.getData().getSystems() != null && qcResponCoachSys.getData().getSystems().size() > 0) {
//                                return Observable.just(qcResponCoachSys.getData().getSystems())
//                                        .subscribeOn(Schedulers.newThread())
//                                        .flatMapIterable(systemsEntities -> systemsEntities)
//                                        .flatMap(systemsEntity -> {
//                                            String url = systemsEntity.getUrl();
//                                            return Observable.just(url);
//                                        })
//                                        .flatMap(s -> new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
//                                                        .setEndpoint(s)
//                                                        .setRequestInterceptor(request ->
//                                                                {
//                                                                    QcResponToken responToken = null;
//                                                                    try {
//                                                                        responToken = QcCloudClient.getApi().getApi.qcGetToken();
//                                                                    } catch (Exception e) {
//                                                                    }
//                                                                    if (responToken != null) {
//                                                                        request.addHeader("X-CSRFToken", responToken.data.token);
//                                                                        request.addHeader("Cookie", "csrftoken=" + responToken.data.token);
//                                                                        request.addHeader("Cache-Control", "max-age=0");
//                                                                    }
//                                                                }
//                                                        )
//                                                        .build()
//                                                        .create(QcCloudClient.MutiSystemApi.class)
//                                                        .qcGetSession(new GetSysSessionBean(account, PreferenceUtils.getPrefString(getActivity(), "session_id", "")))
//                                        )
//                                        .flatMap(qcResponSystem -> {
//                                            MutiSysSession sysSession = new MutiSysSession();
//                                            sysSession.session_id = qcResponSystem.getData().getSession_id();
//                                            sysSession.url = qcResponSystem.getData().getHost();
//                                            systems.add(sysSession);
//                                            return Observable.just(sysSession);
//                                        })
//                                        .last()
//                                        .flatMap(sysSession -> {
//                                            PreferenceUtils.setPrefString(getActivity(), "sessions", gson.toJson(systems));
//                                            return Observable.just(true);
//                                        });
//                            } else return Observable.just(false);
//
//                        })
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                Intent toMain = new Intent(getActivity(), MainActivity.class);
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

            @Override
            public void getCode(String account) {
//                QcCloudClient.getApi().getRestAdapter()
//                        .setErrorHandler(cause -> {
//                                    LogUtil.e(cause.toString());
//                                    return null;
//                                }
//                        )
//                        .build().create(QcCloudClient.PostApi.class)
//                        .qcGetCode(new GetCodeBean(account))
                QcCloudClient.getApi()
                        .postApi
                        .qcGetCode(new GetCodeBean(account))
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

            @Override
            public void goRegister() {
//                getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        loginview.setLoginPresenter(loginPresenter);
        loginview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getActivity() != null)
                    AppUtils.hideKeyboard(getActivity());
                return false;
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        if (loginview != null)
            loginview.unRegiste();
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    public void SnackbarOnUiThread(String msg) {
        getActivity().runOnUiThread(() -> {
            Snackbar
                    .make(loginview, msg, Snackbar.LENGTH_LONG)
                    .show();
        });
    }
}
