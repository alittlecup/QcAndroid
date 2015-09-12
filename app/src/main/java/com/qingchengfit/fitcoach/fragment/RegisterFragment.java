package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.bean.RecieveMsg;
import com.qingchengfit.fitcoach.bean.SendSmsCode;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
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
    MaterialDialog materialDialog;
    @Bind(R.id.registe_gender)
    RelativeLayout registeGender;
    @Bind(R.id.comple_gender_label)
    TextView compleGenderLabel;
    @Bind(R.id.comple_gender_male)
    RadioButton compleGenderMale;
    @Bind(R.id.comple_gender_female)
    RadioButton compleGenderFemale;
    @Bind(R.id.comple_gender)
    RadioGroup compleGender;
    @Bind(R.id.comple_pw)
    TextInputLayout complePw;
    @Bind(R.id.comple_pw_re)
    TextInputLayout complePwRe;
    @Bind(R.id.registe_username)
    TextInputLayout registeUsername;
    Gson gson;
    private InternalHandler handler;
    private Observable<RecieveMsg> mRecieveMsgOb;
    private Observable<SendSmsCode> mSendsmsOb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registerview, null);
//        mDataBinding = DataBindingUtil.bind(view);
        ButterKnife.bind(this, view);
        handler = new InternalHandler(getContext());

        registeGender.setOnClickListener(v -> {
            if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
                compleGender.check(R.id.comple_gender_male);
            } else compleGender.check(R.id.comple_gender_female);
        });
        registePhoneNum.setHint(getString(R.string.logint_phonenum_hint));
        registePhoneVerity.setHint(getString(R.string.login_checkcode_hint));
        mRecieveMsgOb = RxBus.getBus().register(RecieveMsg.class);
        mRecieveMsgOb.subscribe(recieveMsg -> registePhoneVerity.getEditText().setText(recieveMsg.getCode()));

        mSendsmsOb = RxBus.getBus().register(SendSmsCode.class);
        mSendsmsOb.subscribe(sendSmsCode -> handler.sendEmptyMessage(0));
        registeBtn.setOnClickListener(
                view1 -> {
                    String userName = "";

                    if (registeUsername.getEditText() != null)
                        userName = registeUsername.getEditText().getText().toString();


                    String phone = "";
                    if (registePhoneNum.getEditText() != null)
                        phone = registePhoneNum.getEditText().getText().toString().trim();
                    String code = "";
                    if (registePhoneVerity.getEditText() != null)
                        code = registePhoneVerity.getEditText().getText().toString().trim();
                    if (phone.length() < 11) {
                        registePhoneNum.setError(getString(R.string.err_login_phonenum));
                        return;
                    } else registePhoneNum.setError("");

                    if (code.length() < 6) {
                        registePhoneVerity.setError(getString(R.string.err_checkcode_length));
                        return;
                    } else registePhoneVerity.setError("");
                    RegisteBean bean = new RegisteBean();
                    bean.setCode(code);
                    bean.setPhone(phone);
                    String pw = complePw.getEditText().getText().toString().trim();
                    String pwRe = complePwRe.getEditText().getText().toString().trim();
                    if (!pw.equals(pwRe)) {
                        complePwRe.setError(getString(R.string.err_password_not_match));
                        return;
                    }
                    if (pw.length() < 6 || pw.length() > 16) {
                        complePw.setError(getString(R.string.err_password_length));
                        return;
                    }
                    bean.setPassword(pw);
                    if (!TextUtils.isEmpty(userName))
                        bean.setUsername(userName);
                    else registeUsername.setError("请输入用户名");

                    if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
                        bean.setGender(1); //女
                    } else bean.setGender(0);//男
                    materialDialog.show();
                    QcCloudClient.getApi()
                            .postApi
                            .qcRegister(bean)
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(qcResponLogin ->
                                    {
                                        if (qcResponLogin.status == ResponseResult.SUCCESS) {
                                            //TODO 注册成功
                                            getActivity().runOnUiThread(() -> {
                                                Toast.makeText(App.AppContex, "注册成功", Toast.LENGTH_SHORT).show();
//                                                RxBus.getBus().send(new com.qingchengfit.fitcoach.bean.LoginBean());
                                                PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
                                                PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
                                                PreferenceUtils.setPrefString(getActivity(), "coach", gson.toJson(qcResponLogin.data.coach));
                                                Intent toMain = new Intent(getActivity(), MainActivity.class);
                                                startActivity(toMain);
                                                materialDialog.dismiss();
                                                getActivity().finish();
                                            });
                                        } else {

                                            getActivity().runOnUiThread(() -> {

                                                materialDialog.dismiss();
                                                Toast.makeText(App.AppContex, qcResponLogin.msg, Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    }
                            );
//                    QcCloudClient.getApi()
//                            .postApi
//                            .qcCheckCode(new CheckCode(phone, code))
//                            .subscribeOn(Schedulers.newThread())
//                            .subscribe(qcResponCode -> {
//                                if (qcResponCode.status == ResponseResult.SUCCESS) {
//                                    Intent it = new Intent(getActivity(), CompleteActivity.class);
//                                    it.putExtra("code", qcResponCode.data.code);
//                                    startActivity(it);
//                                } else {
//
//                                    getActivity().runOnUiThread(() -> {
//                                        registePhoneVerity.setError(qcResponCode.msg);
//                                    });
//
//                                    LogUtil.d(qcResponCode.msg);
//                                }
//                            })
//                    ;
                }

        );

        registeGetcodeBtn.setOnClickListener(view2 -> {
            String phone2 = registePhoneNum.getEditText().getText().toString().trim();
            if (phone2.length() < 11) {
                registePhoneNum.setError(getString(R.string.err_login_phonenum));
                return;
            } else registePhoneNum.setError("");

            QcCloudClient.getApi()
                    .postApi
                    .qcGetCode(new GetCodeBean(phone2))
//                    .qcRegister(new RegisteBean("paper", "123456", mcode, 1))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(qcResponse -> {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            LogUtil.d("succ");
                        } else {
//                            LogUtil.d(":" + qcResponse.msg);
                        }
                    })
            ;

            RxBus.getBus().send(new SendSmsCode());
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialDialog = new MaterialDialog.Builder(getActivity())
                .content("正在注册,请稍后")
                .progress(true, 0)
                .build();
        gson = new Gson();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        RxBus.getBus().unregister(RecieveMsg.class, mRecieveMsgOb);
        RxBus.getBus().unregister(SendSmsCode.class, mSendsmsOb);
    }

    public class InternalHandler extends Handler {
        WeakReference<Context> context;
        int count = 60;

        InternalHandler(Context c) {
            context = new WeakReference<Context>(c);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Integer.toString(count));
            stringBuffer.append(App.AppContex.getString(R.string.login_resend_msg));
            if (registeGetcodeBtn != null) {
                registeGetcodeBtn.setText(stringBuffer.toString());
                if (count == 60)
                    registeGetcodeBtn.setEnabled(false);
                if (count > 0) {
                    count--;
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    count = 60;
                    registeGetcodeBtn.setEnabled(true);
                    registeGetcodeBtn.setText(getResources().getString(R.string.login_getcode));
                }
            }
        }
    }
}
