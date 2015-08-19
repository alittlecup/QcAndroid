package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.CompleteActivity;
import com.qingchengfit.fitcoach.bean.RecieveMsg;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CheckCode;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
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

    public String mcode;
    @Bind(R.id.registe_phone_num)
    TextInputLayout registePhoneNum;
    @Bind(R.id.registe_getcode_btn)
    TextView registeGetcodeBtn;
    @Bind(R.id.telephone_layout)
    RelativeLayout telephoneLayout;
    @Bind(R.id.registe_phone_verity)
    TextInputLayout registePhoneVerity;
    @Bind(R.id.registe_btn)
    Button registeBtn;
    Handler handler = new Handler() {
        int count = 60;

        @Override
        public void handleMessage(Message msg) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Integer.toString(count));
            stringBuffer.append(getString(R.string.login_resend_msg));

            registeGetcodeBtn.setText(stringBuffer.toString());
            if (count > 0) {
                count--;
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                count = 60;
                registeGetcodeBtn.setEnabled(true);
                registeGetcodeBtn.setText(getResources().getString(R.string.login_getcode));
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registerview, null);
//        mDataBinding = DataBindingUtil.bind(view);
        ButterKnife.bind(this, view);
        registePhoneNum.setHint(getString(R.string.logint_phonenum_hint));
        registePhoneVerity.setHint(getString(R.string.login_checkcode_hint));
        RxBus.getBus().toObserverable()
                .subscribe(o -> {
                    if (o instanceof RecieveMsg)
                        registePhoneVerity.getEditText().setText(((RecieveMsg) o).getCode());
                });
        registeBtn.setOnClickListener(
                view1 -> {
                    String phone = registePhoneNum.getEditText().getText().toString().trim();
                    String code = registePhoneVerity.getEditText().getText().toString().trim();
                    if (phone.length()<11){
                        registePhoneNum.setError(getString(R.string.err_login_phonenum));
                        return;
                    }else registePhoneNum.setError("");
                    if (code.length()<4){
                        registePhoneVerity.setError(getString(R.string.err_checkcode_length));
                        return;
                    }else registePhoneVerity.setError("");

                    QcCloudClient.getApi()
                            .postApi
                            .qcCheckCode(new CheckCode(phone, code))
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(qcResponCode -> {
                                if (qcResponCode.status == ResponseResult.SUCCESS) {
                                    Intent it = new Intent(getActivity(), CompleteActivity.class);
                                    it.putExtra("code",qcResponCode.data.code);
                                    startActivity(it);
//                                    mcode = qcResponCode.data.code;
                                } else {

                                    getActivity().runOnUiThread(() -> {
                                        registePhoneVerity.setError(qcResponCode.msg);
                                    });

                                    LogUtil.d(qcResponCode.msg);
                                }
                            })
                    ;
                }

        );

        registeGetcodeBtn.setOnClickListener(view1 -> {
            String phone = registePhoneNum.getEditText().getText().toString().trim();
            if (phone.length()<11){
                registePhoneNum.setError(getString(R.string.err_login_phonenum));
                return;
            }else registePhoneNum.setError("");

            QcCloudClient.getApi()
                    .postApi
                    .qcGetCode(new GetCodeBean(phone))
//                    .qcRegister(new RegisteBean("paper", "123456", mcode, 1))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(qcResponse -> {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            LogUtil.d("succ");
                        } else {
                            LogUtil.d(":" + qcResponse.msg);
                        }
                    })
            ;
            registeGetcodeBtn.setEnabled(false);
            handler.sendEmptyMessage(0);
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
