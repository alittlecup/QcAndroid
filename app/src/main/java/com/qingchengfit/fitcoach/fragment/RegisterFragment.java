package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.RecieveMsg;
import com.qingchengfit.fitcoach.bean.SendSmsCode;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.lang.ref.WeakReference;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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




    @BindView(R.id.registe_rootview)
    LinearLayout registeRootview;
    @BindView(R.id.comple_gender_label)
    TextView compleGenderLabel;
    @BindView(R.id.comple_gender_male)
    RadioButton compleGenderMale;
    @BindView(R.id.comple_gender_female)
    RadioButton compleGenderFemale;
    @BindView(R.id.comple_gender)
    RadioGroup compleGender;
    private Unbinder unbinder;
    @BindView(R.id.registe_gender)
    LinearLayout registeGender;
    @BindView(R.id.phone_num)
    PhoneEditText phoneNum;
    @BindView(R.id.checkcode)
    PasswordView checkcode;
    @BindView(R.id.password)
    PasswordView password;
    @BindView(R.id.registe_btn)
    Button registeBtn;
    @BindView(R.id.registe_username)
    EditText registeUsername;
    private InternalHandler handler;
    private Observable<RecieveMsg> mRecieveMsgOb;
    private Observable<SendSmsCode> mSendsmsOb;
    private Gson gson;
    private MaterialDialog materialDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registerview, null);
        unbinder=ButterKnife.bind(this, view);

        handler = new InternalHandler(getContext());

        registeGender.setOnClickListener(v -> {
            if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
                compleGender.check(R.id.comple_gender_male);
            } else compleGender.check(R.id.comple_gender_female);
        });
        registeRootview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getActivity() != null) {
                    AppUtils.hideKeyboard(getActivity());
                }
                return false;
            }
        });
        mSendsmsOb = RxBus.getBus().register(SendSmsCode.class.getName(), SendSmsCode.class);
        mSendsmsOb.subscribe(sendSmsCode ->
                handler.sendEmptyMessage(0));
        registeBtn.setOnClickListener(
                view1 -> {
                    String userName = "";
                    userName = registeUsername.getText().toString().trim();
                    if (TextUtils.isEmpty(userName)){
                        ToastUtils.showDefaultStyle(getString(R.string.please_input_phone_num));
                        return;
                    }
                    if (phoneNum.checkPhoneNum() && checkcode.checkValid() && password.checkValid()) {
                        materialDialog.show();
                        QcCloudClient.getApi()
                                .postApi
                                .qcRegister(new RegisteBean.Builder()
                                        .password(password.getCode())
                                        .phone(phoneNum.getPhoneNum())
                                        .area_code(phoneNum.getDistrictInt())
                                        .code(checkcode.getCode())
                                        .username(userName)
                                        .gender(compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female ? 1 : 0)
                                        .build())

                                .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(qcResponLogin ->
                                        {
                                            if (qcResponLogin.status == ResponseResult.SUCCESS) {
                                                //TODO 注册成功
                                                getActivity().runOnUiThread(() -> {
                                                    Toast.makeText(App.AppContex, "注册成功", Toast.LENGTH_SHORT).show();
//                                                RxBus.getBus().send(new com.qingchengfit.fitcoach.bean.LoginBean());
                                                    PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
                                                    PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
                                                    App.gUser = qcResponLogin.data.user;
                                                    PreferenceUtils.setPrefString(getActivity(), "coach", gson.toJson(qcResponLogin.data.coach));
                                                    App.coachid = Integer.parseInt(qcResponLogin.data.coach.id);
                                                    Intent toMain = new Intent(getActivity(), FragActivity.class);
                                                    toMain.putExtra("type",10);
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
                                        }, throwable -> {
                                        }, () -> {
                                        }
                                );

                    }

//                    if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
//                        bean.setGender(1); //女
//                    } else bean.setGender(0);//男
//                    materialDialog.show();
//                    QcCloudClient.getApi()
//                            .postApi
//                            .qcRegister(bean)
//                            .subscribeOn(Schedulers.newThread())
//                            .subscribe(qcResponLogin ->
//                                    {
//                                        if (qcResponLogin.status == ResponseResult.SUCCESS) {
//                                            //TODO 注册成功
//                                            getActivity().runOnUiThread(() -> {
//                                                Toast.makeText(App.AppContex, "注册成功", Toast.LENGTH_SHORT).show();
////                                                RxBus.getBus().send(new com.qingchengfit.fitcoach.bean.LoginBean());
//                                                PreferenceUtils.setPrefString(getActivity(), "session_id", qcResponLogin.data.session_id);
//                                                PreferenceUtils.setPrefString(getActivity(), "user_info", gson.toJson(qcResponLogin.data.user));
//                                                PreferenceUtils.setPrefString(getActivity(), "coach", gson.toJson(qcResponLogin.data.coach));
//                                                App.coachid = Integer.parseInt(qcResponLogin.data.coach.id);
//                                                Intent toMain = new Intent(getActivity(), FragActivity.class);
//                                                toMain.putExtra("type",10);
//                                                startActivity(toMain);
//                                                materialDialog.dismiss();
//                                                getActivity().finish();
//                                            });
//                                        } else {
//=======
                    }



        );
        checkcode.setOnClickListener(v -> {
            if (phoneNum.checkPhoneNum()){
                QcCloudClient.getApi()
                        .postApi
                        .qcGetCode(new GetCodeBean.Builder().phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt()).build())
                        .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(qcResponse -> {
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                LogUtil.d("succ");
                            } else {
                            }
                        }, throwable -> {
                        }, () -> {
                        })
                ;
                RxBus.getBus().post(new SendSmsCode());

            }
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
        unbinder.unbind();
        RxBus.getBus().unregister(RecieveMsg.class.getName(), mRecieveMsgOb);
        RxBus.getBus().unregister(SendSmsCode.class.getName(), mSendsmsOb);
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
            if (checkcode != null) {
                checkcode.setRightText(stringBuffer.toString());
                if (count == 60)
                    checkcode.blockRightClick(true);
                if (count > 0) {
                    count--;
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    count = 60;
                    checkcode.blockRightClick(false);
                    checkcode.setRightText(getResources().getString(R.string.login_getcode));
                }
            }
        }
    }
}
