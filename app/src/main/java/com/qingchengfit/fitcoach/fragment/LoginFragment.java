package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.activity.RegisterActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    LoginPresenter loginPresenter;

    @Bind(R.id.loginview)
    LoginView loginview;

    public LoginFragment() {
        loginPresenter = new LoginPresenter() {
            @Override
            public void onPhoneDone() {

            }

            @Override
            public void doLogin(String account, String code) {

//                Intent toMain = new Intent(getActivity(), MainActivity.class);
//                startActivity(toMain);
//                getActivity().finish();
                LoginBean bean = new LoginBean(account, code);
                QcCloudClient.getApi()
                        .postApi
                        .qcLogin(bean)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(qcResponLogin -> {
                            if (qcResponLogin.status == ResponseResult.SUCCESS) {
                                PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
                                Gson gson = new Gson();
                                PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
                                Intent toMain = new Intent(getActivity(), MainActivity.class);
                                startActivity(toMain);
                                getActivity().finish();
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), qcResponLogin.msg, Toast.LENGTH_SHORT).show();
                                        Snackbar
                                                .make(loginview, qcResponLogin.msg, Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                });


                            }


                        });
            }

            @Override
            public void getCode(String account) {
                QcCloudClient.getApi()
                        .postApi
                        .qcGetCode(new GetCodeBean(account))
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(qcResponse -> {
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                LogUtil.d("send msg success!");
                            } else {
                                LogUtil.d(qcResponse.msg);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), qcResponse.msg, Toast.LENGTH_SHORT).show();
                                        Snackbar
                                                .make(loginview, qcResponse.msg, Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }
                        });
            }

            @Override
            public void goRegister() {
                getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));
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
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
