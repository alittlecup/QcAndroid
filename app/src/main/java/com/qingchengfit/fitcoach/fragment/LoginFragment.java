package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paper.loginlibrary.LoginPresenter;
import com.paper.loginlibrary.LoginView;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RegisterActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;

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
            public void onSucceed() {

            }

            @Override
            public void doLogin(String account, String code) {
                QcCloudClient.getApi().qcCloudServer
                        .qcGetToken()
//                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(qcResponToken -> {
                            FileUtils.saveCache("token",qcResponToken.data.token);
                            PreferenceUtils.setPrefString(getActivity(),"token",qcResponToken.data.token);
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
