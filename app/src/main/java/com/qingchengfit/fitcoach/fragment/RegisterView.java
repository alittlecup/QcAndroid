package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/7/30 2015.
 */
public class RegisterView extends RelativeLayout {

//    Button mGetCodeBtn;
    Button mLoginBtn;
    TextInputLayout mPhoneNumInputLayout;
    TextInputLayout mCheckCodeInputLaout;
    LoginPresenter loginPresenter;

    public RegisterView(Context context) {
        super(context);
    }

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mGetCodeBtn = (Button) findViewById(R.id.login_getcode_btn);
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
                } else {
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
//        mGetCodeBtn.setOnClickListener(new OnClickListener() {
//                                           @Override
//                                           public void onClick(View view) {
//                                               String account = mPhoneNumInputLayout.getEditText().getText().toString().trim();
//                                               if (TextUtils.isEmpty(account)) {
//                                                   mPhoneNumInputLayout.setError("error");
//                                                   mPhoneNumInputLayout.setErrorEnabled(true);
//                                                   return;
//                                               }
//                                           }
//                                       }
//
//        );
        mLoginBtn.setOnClickListener(view -> {
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
        });
    }
}
