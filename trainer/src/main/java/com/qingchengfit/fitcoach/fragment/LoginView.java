package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import cn.qingchengfit.bean.SendSmsCode;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
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

public class LoginView extends LinearLayout {
    Button mGoRegister;
    Button mLoginBtn;
    ToggleButton mForgotPwBtn;
    LoginPresenter loginPresenter;
    private InternalHandler handler;
    private Observable<SendSmsCode> mObservable;
    private PhoneEditText mPhoneNumView;
    private PasswordView mPasswordView;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        handler = new InternalHandler(getContext());
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mForgotPwBtn = (ToggleButton) findViewById(R.id.forgetpw_btn);
        mGoRegister = (Button) findViewById(R.id.goregiste_btn);
        mPasswordView = (PasswordView) findViewById(R.id.pw_view);
        mPhoneNumView = (PhoneEditText) findViewById(R.id.phone_view);
        mForgotPwBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        mForgotPwBtn.toggle();
        mPasswordView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (mPhoneNumView.checkPhoneNum()) {
                    loginPresenter.getCode(
                        new GetCodeBean.Builder().phone(mPhoneNumView.getPhoneNum()).area_code(mPhoneNumView.getDistrictInt()).build());
                    RxBus.getBus().post(new SendSmsCode());
                    mPasswordView.blockRightClick(true);
                }
            }
        });
        mForgotPwBtn.setOnClickListener(v -> {
            mPasswordView.toggle();
        });

        mObservable = RxBus.getBus().register(SendSmsCode.class.getName(), SendSmsCode.class);
        mObservable.subscribe(sendSmsCode -> handler.sendEmptyMessage(0));

        mLoginBtn.setOnClickListener(view -> doLogin());
    }

    public void unRegiste() {
        RxBus.getBus().unregister(SendSmsCode.class.getName(), mObservable);
    }

    public void doLogin() {
        if (mPhoneNumView.checkPhoneNum() && mPasswordView.checkValid()) {
            loginPresenter.doLogin(mPhoneNumView.getDistrictInt(), mPhoneNumView.getPhoneNum(), mPasswordView.getCode());
        }
    }

    public boolean isPassword() {
        return mPasswordView.isPwMode();
    }

    public void onError(String err) {
        ToastUtils.showDefaultStyle(err);
    }

    public class InternalHandler extends Handler {
        WeakReference<Context> context;
        int count = 60;

        InternalHandler(Context c) {
            context = new WeakReference<Context>(c);
        }

        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Integer.toString(count));
            stringBuffer.append(getContext().getString(R.string.login_resend_msg));
            if (mPasswordView != null) {
                mPasswordView.setRightText(stringBuffer.toString());
                if (count == 60) mPasswordView.blockRightClick(true);
                if (count > 0) {
                    count--;
                    this.sendEmptyMessageDelayed(0, 1000);
                } else {
                    count = 60;
                    mPasswordView.blockRightClick(false);
                    mPasswordView.setRightText(getResources().getString(R.string.login_getcode));
                }
            }
        }
    }
}
