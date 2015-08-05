package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paper.loginlibrary.LoginPresenter;
import com.paper.loginlibrary.LoginView;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.activity.RegisterActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.LoginBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

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
            public void onSucceed() {

            }

            @Override
            public void doLogin(String account, String code) {
                LoginBean bean = new LoginBean(account, code);
//                Observable
//                        .just(bean)
//                        .flatMap(new Func1<LoginBean, Observable<?>>() {
//                            @Override
//                            public Observable<?> call(LoginBean loginBean) {
//
//                                return null;
//                            }
//                        })
//                        .subscribe(bean1 -> {
//                            QcCloudClient.getApi()
//                                    .qcGetToken.qcGetToken()
//                                    .subscribe(qcResponToken -> {
//
//                                    });
//                        });


                QcCloudClient.getApi().qcGetToken
                        .qcGetToken()
//                        .observeOn(Schedulers.newThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(qcResponToken -> {
                            FileUtils.saveCache("token", qcResponToken.data.token);
                            PreferenceUtils.setPrefString(getActivity(), "token", qcResponToken.data.token);
                            QcCloudClient.getApi().qcCloudServer
                                    .qcLogin(new LoginBean("13601218507", "123456"))
                                    .subscribe(qcResponLogin -> {
                                        LogUtil.e("::" + qcResponLogin.data.session_id);
                                        PreferenceUtils.setPrefString(getActivity(),"session_id",qcResponLogin.data.session_id);
                                        Intent toMain = new Intent(getActivity(),MainActivity.class);
//                                        toMain.putExtra("session_id",qcResponLogin.data.session_id);
                                        startActivity(toMain);
                                    });

                        })

//                        .qcLogin(account, code)
//                        .subscribe(qcResponse -> LogUtil.e(qcResponse.data))
                ;
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
