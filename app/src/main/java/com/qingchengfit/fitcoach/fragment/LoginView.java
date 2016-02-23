package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.SendSmsCode;

import java.lang.ref.WeakReference;

import rx.Observable;

/**
 * ,==.              |~~~
 * /  66\             |
 * \c  -_)         |~~~
 * `) (           |
 * /   \       |~~~
 * /   \ \      |
 * ((   /\ \_ |~~~
 * \\  \ `--`|
 * / / /  |~~~
 * ___ (_(___)_|
 * <p>
 * Created by Paper on 15/6/28 2015.
 */

public class LoginView extends RelativeLayout {
    Button mGoRegister;
    TextView mGetCodeBtn;
    Button mLoginBtn;
    ToggleButton mForgotPwBtn;
    TextInputLayout mPhoneNumInputLayout;
    TextInputLayout mCheckCodeInputLaout;
    LoginPresenter loginPresenter;
    private InternalHandler handler;
    private Observable<SendSmsCode> mObservable;

    public LoginView(Context context) {
        super(context);
    }

//    public static Handler handler = new Handler() {
//
//
//        @Override
//        public void handleMessage(Message msg) {
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(Integer.toString(count));
//            stringBuffer.append(getContext().getString(R.string.login_resend_msg));
//
//            mGetCodeBtn.setText(stringBuffer.toString());
//            if (count == 60)
//                mGetCodeBtn.setEnabled(false);
//            if (count>0){
//                count--;
//                handler.sendEmptyMessageDelayed(0,1000);
//            }else {
//                count=60;
//                mGetCodeBtn.setEnabled(true);
//                mGetCodeBtn.setText(getResources().getString(R.string.login_getcode));
//            }
//        }
//    };


    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        handler = new InternalHandler(getContext());
        mGetCodeBtn = (TextView) findViewById(R.id.login_getcode_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mPhoneNumInputLayout = (TextInputLayout) findViewById(R.id.login_phone_num);
        mCheckCodeInputLaout = (TextInputLayout) findViewById(R.id.login_phone_verity);
        mForgotPwBtn = (ToggleButton) findViewById(R.id.forgetpw_btn);
        mGoRegister = (Button) findViewById(R.id.goregiste_btn);
        mPhoneNumInputLayout.setHint(getResources().getString(R.string.logint_phonenum_hint));
        mCheckCodeInputLaout.setHint(getResources().getString(R.string.login_password_hint));

        mCheckCodeInputLaout.setErrorEnabled(true);
//        mForgotPwBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCheckCodeInputLaout.setPivotX(0);
//                ViewCompat.animate(mCheckCodeInputLaout).rotationYBy(0.5f).setDuration(800).start();
//                mGetCodeBtn.setVisibility(VISIBLE);
//                mGetCodeBtn.setScaleX(0.5f);
//                mGetCodeBtn.setScaleY(0.5f);
//                ViewCompat.animate(mGetCodeBtn).scaleXBy(0.75f).scaleYBy(0.75f).setDuration(800).start();
//            }
//        });

        mForgotPwBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mCheckCodeInputLaout.getEditText() != null) {
                    if (b) {

                        mGetCodeBtn.setVisibility(VISIBLE);
                        mCheckCodeInputLaout.setHint(getResources().getString(R.string.login_checkcode_hint));

                        mCheckCodeInputLaout.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        mGetCodeBtn.setVisibility(GONE);
                        mCheckCodeInputLaout.setHint(getResources().getString(R.string.login_password_hint));
                        mCheckCodeInputLaout.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    mCheckCodeInputLaout.getEditText().setText("");
                }
            }
        });
        mForgotPwBtn.toggle();

        mPhoneNumInputLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    loginPresenter.onPhoneDone();
                }
            }
        });
        mCheckCodeInputLaout.getEditText().setOnEditorActionListener((textView, i, keyEvent) ->
                {
                    if (i == EditorInfo.IME_ACTION_GO) {
                        doLogin();
                    }
                    return false;
                }
        );
        //验证码输入框检测
        mCheckCodeInputLaout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {

//                if (text.length() > 0 && text.length() <= 4) {
//                    mCheckCodeInputLaout.setErrorEnabled(false);
//                } else {
//                    mCheckCodeInputLaout.setError("error");
//                    mCheckCodeInputLaout.setErrorEnabled(true);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPhoneNumInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGoRegister.setOnClickListener(view -> loginPresenter.goRegister());

        mGetCodeBtn.setOnClickListener(view -> {
                    String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(account) || account.length() < 11) {
                        mPhoneNumInputLayout.setError(getResources().getString(R.string.err_login_phonenum));
                        return;
                    } else {
                        mPhoneNumInputLayout.setError("");
                        loginPresenter.getCode(account);
                        RxBus.getBus().post(new SendSmsCode());
                    }
                }

        );
        mObservable = RxBus.getBus().register(SendSmsCode.class.getName(), SendSmsCode.class);
        mObservable.subscribe(sendSmsCode -> handler.sendEmptyMessage(0));
//        RxBus.getBus().toObserverable().subscribe(o -> {
//            if (o instanceof SendSmsCode) {
//                handler.sendEmptyMessage(0);
//            }
//        });

        mLoginBtn.setOnClickListener(view -> doLogin());

    }

    public void unRegiste() {
        RxBus.getBus().unregister(SendSmsCode.class.getName(), mObservable);
    }


    public void doLogin() {

        String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
        String code = mCheckCodeInputLaout.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(account) || account.length() < 11) {
            mPhoneNumInputLayout.setError(getResources().getString(R.string.login_err_no_account));
//                mPhoneNumInputLayout.setErrorEnabled(true);
            mPhoneNumInputLayout.requestFocus();
            return;
        } else mPhoneNumInputLayout.setError("");

        if (TextUtils.isEmpty(code)) {
            mCheckCodeInputLaout.setError(getResources().getString(R.string.login_err_no_checkcode));
            mCheckCodeInputLaout.requestFocus();
            return;
        } else mCheckCodeInputLaout.setError("");
        loginPresenter.doLogin(account, code);
//          loginPresenter.doLogin("", "");


    }

    public void onError(String err) {
        mCheckCodeInputLaout.setError(err);

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
            stringBuffer.append(getContext().getString(R.string.login_resend_msg));
            if (mGetCodeBtn != null) {
                mGetCodeBtn.setText(stringBuffer.toString());
                if (count == 60)
                    mGetCodeBtn.setEnabled(false);
                if (count > 0) {
                    count--;
                    this.sendEmptyMessageDelayed(0, 1000);
                } else {
                    count = 60;
                    mGetCodeBtn.setEnabled(true);
                    mGetCodeBtn.setText(getResources().getString(R.string.login_getcode));
                }
            }
        }
    }


}
