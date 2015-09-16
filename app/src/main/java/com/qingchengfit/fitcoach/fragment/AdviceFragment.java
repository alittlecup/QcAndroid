package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

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
        String email = settingAdviceMail.getText().toString();
        String content = settingAdviceContent.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(content)) {
            QcCloudClient.getApi().postApi.qcFeedBack(new FeedBackBean(email, content))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(qcResponse ->
                                    getActivity().runOnUiThread(() -> {
                                        if (qcResponse.status == ResponseResult.SUCCESS) {
                                            Toast.makeText(getContext(), "感谢您的反馈,我们会继续努力", Toast.LENGTH_SHORT).show();
                                            getActivity().onBackPressed();
                                        } else {
                                            Toast.makeText(getContext(), "服务器错误,请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                    );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
