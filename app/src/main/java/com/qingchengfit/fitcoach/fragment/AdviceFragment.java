package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceFragment extends BaseSettingFragment {


    @Bind(R.id.setting_advice_mail)
    EditText settingAdviceMail;
    @Bind(R.id.setting_advice_content)
    EditText settingAdviceContent;

    public AdviceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "意见反馈");

        return view;
    }

    @OnClick(R.id.setting_advice_btn)
    public void onAdvice() {
        Observable.just("")
                .subscribe(s ->
                                LogUtil.d("advice")
                )

        ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
