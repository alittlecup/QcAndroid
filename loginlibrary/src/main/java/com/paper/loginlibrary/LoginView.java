package com.paper.loginlibrary;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
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

    Button mGetCodeBtn;
    Button mLoginBtn;
    TextInputLayout mPhoneNumInputLayout;
    TextInputLayout mCheckCodeInputLaout;
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
        mPhoneNumInputLayout.setHint(getResources().getString(R.string.logint_phonenum_hint));
        mCheckCodeInputLaout.setHint(getResources().getString(R.string.login_checkcode_hint));
        mCheckCodeInputLaout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() > 0 && text.length() <= 4) {
                    mCheckCodeInputLaout.setError("error");
                    mCheckCodeInputLaout.setErrorEnabled(true);
                }else {
                    mCheckCodeInputLaout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mGetCodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
//                    mPhoneNumInputLayout.setError(getResources().getString(R.string.login_err_no_account));
//                    mPhoneNumInputLayout.setErrorEnabled(true);
//
                    mPhoneNumInputLayout.setError("error");
                    mPhoneNumInputLayout.setErrorEnabled(true);
//                    mPhoneNumInputLayout.requestFocus();
                    return;
                }
                }
            }

            );
            mLoginBtn.setOnClickListener(new

            OnClickListener() {
                @Override
                public void onClick (View view){
                    Log.e("hshs", "click");
                    String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
                    String code = mCheckCodeInputLaout.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(account)) {
                        mPhoneNumInputLayout.setError(getResources().getString(R.string.login_err_no_account));
                        mPhoneNumInputLayout.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(code)) {
                        mCheckCodeInputLaout.setError(getResources().getString(R.string.login_err_no_checkcode));
                        mCheckCodeInputLaout.requestFocus();
                        return;
                    }
                    loginPresenter.doLogin(account, code);
            }
        });
    }

}
