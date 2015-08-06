package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CheckCode;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.schedulers.Schedulers;


/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/7/31 2015.
 */
public class RegisterFragment extends Fragment {

//    RegisterviewBinding mDataBinding;

    @Bind(R.id.login_phone_num)
    TextInputLayout loginPhoneNum;
    @Bind(R.id.registe_getcode_btn)
    TextView registeGetcodeBtn;
    @Bind(R.id.telephone_layout)
    RelativeLayout telephoneLayout;
    @Bind(R.id.registe_phone_verity)
    TextInputLayout registePhoneVerity;
    @Bind(R.id.registe_checkcode_layout)
    LinearLayout registeCheckcodeLayout;
    @Bind(R.id.registe_btn)
    Button registeBtn;

    public String mcode ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registerview, null);
//        mDataBinding = DataBindingUtil.bind(view);
        ButterKnife.bind(this, view);
        registeBtn.setOnClickListener(
                view1 -> {
//                    LogUtil.e("click");
//                    QcCloudClient.getApi().postApi.qcRegister(
//                        PreferenceUtils.getPrefString(getActivity(), "token", ""),
//                        new RegisteBean("13601218507", "aa", "123456"))
//                    .subscribe(qcResponse -> {
//                        LogUtil.e("responses");
//                    })
                    String phone = loginPhoneNum.getEditText().getText().toString().trim();
                    String code = registePhoneVerity.getEditText().getText().toString().trim();


                    QcCloudClient.getApi()
                            .postApi
                            .qcCheckCode(new CheckCode(phone, code))
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(qcResponCode -> {
                                if (qcResponCode.status == ResponseResult.SUCCESS) {
                                    LogUtil.e("succ");
                                    mcode = qcResponCode.data.code;
                                } else {
                                    LogUtil.e(qcResponCode.msg);
                                }
                            })
                    ;
                }

        );

        registeGetcodeBtn.setOnClickListener(view1 -> {
            QcCloudClient.getApi()
                    .postApi
                    .qcRegister(new RegisteBean("paper","123456",mcode,1))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(qcResponse -> {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            LogUtil.e("succ");
                        } else {
                            LogUtil.e(":" + qcResponse.msg);
                        }
                    })
            ;
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
