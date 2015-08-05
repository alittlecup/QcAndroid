package com.paper.loginlibrary;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
 * <p/>
 * Created by Paper on 15/6/28 2015.
 */
public class LoginView extends RelativeLayout {
    Button mGoRegister;
    Button mGetCodeBtn;
    Button mLoginBtn;
    Button mForgotPwBtn;
    TextInputLayout mPhoneNumInputLayout;
    TextInputLayout mCheckCodeInputLaout;

    public void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    LoginPresenter loginPresenter;


    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mGetCodeBtn = (Button) findViewById(R.id.login_getcode_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mPhoneNumInputLayout = (TextInputLayout) findViewById(R.id.login_phone_num);
        mCheckCodeInputLaout = (TextInputLayout) findViewById(R.id.login_phone_verity);
        mForgotPwBtn = (Button)findViewById(R.id.forgetpw_btn);
        mGoRegister = (Button)findViewById(R.id.goregiste_btn);
        mPhoneNumInputLayout.setHint(getResources().getString(R.string.logint_phonenum_hint));
        mCheckCodeInputLaout.setHint(getResources().getString(R.string.login_password_hint));
        mForgotPwBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckCodeInputLaout.setPivotX(0);
                ViewCompat.animate(mCheckCodeInputLaout).rotationYBy(0.5f).setDuration(800).start();
                mGetCodeBtn.setVisibility(VISIBLE);
                mGetCodeBtn.setScaleX(0.5f);
                mGetCodeBtn.setScaleY(0.5f);
                ViewCompat.animate(mGetCodeBtn).scaleXBy(0.75f).scaleYBy(0.75f).setDuration(800).start();
            }
        });
        mCheckCodeInputLaout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {

                if (text.length() > 0 && text.length() <= 4) {
                    mCheckCodeInputLaout.setErrorEnabled(false);
                } else {
                    mCheckCodeInputLaout.setError("error");
                    mCheckCodeInputLaout.setErrorEnabled(true);
                }
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

        mGoRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.goRegister();
            }
        });
        mGetCodeBtn.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
                                               if (TextUtils.isEmpty(account)) {
                                                   mPhoneNumInputLayout.setError("error");
                                                   mPhoneNumInputLayout.setErrorEnabled(true);
                                                   return;
                                               }
                                           }
                                       }

        );
            mLoginBtn.setOnClickListener(new

                                                 OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
//                                                         String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
//                                                         String code = mCheckCodeInputLaout.getEditText().getText().toString().trim();
//                                                         if (TextUtils.isEmpty(account)) {
//                                                             mPhoneNumInputLayout.setError(getResources().getString(R.string.login_err_no_account));
//                                                             mPhoneNumInputLayout.requestFocus();
//                                                             return;
//                                                         }
//
//                                                         if (TextUtils.isEmpty(code)) {
//                                                             mCheckCodeInputLaout.setError(getResources().getString(R.string.login_err_no_checkcode));
//                                                             mCheckCodeInputLaout.requestFocus();
//                                                             return;
//                                                         }
//                                                         loginPresenter.doLogin(account, code);
                                                         loginPresenter.doLogin("", "");
                                                     }
                                                 });
    }

}
