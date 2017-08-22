package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.utils.ToastUtils;


/**
 * TODO: document your custom view class.
 */
public class PasswordView extends LinearLayout implements View.OnClickListener {

    private boolean isPwMode = true;

    private EditText mNum;
    private TextView mBtnGetCode;
    private ImageView mImgLeft;
    private TextView mTvPw;
    private AnimatedButton mOpenEye;
    private boolean mBlock = false;
    private OnClickListener mOnClickListener;
    private boolean isShowLeft = true;
    private boolean noNull = false;
    private TextView noNullTv;

    public PasswordView(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(context, R.layout.layout_password, this);
        init(null, 0);
        onFinishInflate();
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(context, R.layout.layout_password, this);
        init(attrs, 0);

        onFinishInflate();
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(context, R.layout.layout_password, this);
        init(attrs, defStyle);

        onFinishInflate();
    }

    private void init(AttributeSet attrs, int defStyle) {

//        LayoutInflater.from(context).inflate(R.layout.layout_login_edittext, this, true);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PasswordView, defStyle, 0);
        isPwMode = a.getBoolean(R.styleable.PasswordView_pw_mode, true);
        isShowLeft = a.getBoolean(R.styleable.PasswordView_pw_show_left, true);
        noNull = a.getBoolean(R.styleable.PasswordView_pw_nonull,false);
        a.recycle();

        if (isInEditMode()) {
            return;
        }

    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public boolean checkValid() {
        String text = mNum.getText().toString().trim();
        if (isPwMode) {
            if (text.length() < 6) {
                ToastUtils.show(getResources().getString(R.string.err_password_length));
                return false;
            }
        } else {
            if (text.length() < 6) {
                ToastUtils.show(getResources().getString(R.string.err_checkcode_length));
                return false;
            }
        }
        return true;
    }

    public String getCode() {
        return mNum.getText().toString().trim();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImgLeft = (ImageView) findViewById(R.id.img_pw);
        mNum = (EditText) findViewById(R.id.et_pw);
        mBtnGetCode = (TextView) findViewById(R.id.btn_getcode);
        mTvPw = (TextView) findViewById(R.id.tv_password);
        noNullTv = (TextView) findViewById(R.id.nonull_tv);
        mOpenEye = (AnimatedButton) findViewById(R.id.open_eye);
        mBtnGetCode.setOnClickListener(this);
        mOpenEye.setChecked(false);
        mImgLeft.setVisibility(isShowLeft ? VISIBLE : GONE);
        mImgLeft.setImageResource(isPwMode?R.drawable.ic_login_password:R.drawable.ic_login_verifycode);
        noNullTv.setVisibility(noNull?VISIBLE:GONE);
        if (isPwMode) {
            mBtnGetCode.setVisibility(GONE);
            mTvPw.setText(R.string.login_pw);
            mOpenEye.setVisibility(VISIBLE);
            mNum.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            mBtnGetCode.setText(getResources().getString(R.string.login_getcode));
            mTvPw.setText(R.string.phone_code);
            mOpenEye.setVisibility(GONE);
            mBtnGetCode.setVisibility(VISIBLE);
        }
        mOpenEye.setOnClickListener(this);

    }

    public boolean isPwMode() {
        return isPwMode;
    }

    public void setRightText(String text) {
        mBtnGetCode.setText(text);
    }

    public void blockRightClick(boolean b) {
        mBlock = b;
    }

    public void toggle() {
        isPwMode = !isPwMode;
        mNum.setText("");
        mImgLeft.setImageResource(isPwMode?R.drawable.ic_login_password:R.drawable.ic_login_verifycode);
        if (isPwMode) {
            mBtnGetCode.setVisibility(GONE);
            mTvPw.setText(R.string.login_pw);
            mOpenEye.setVisibility(VISIBLE);
            mNum.setInputType(InputType.TYPE_CLASS_TEXT | (mOpenEye.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        } else {
            mBtnGetCode.setVisibility(VISIBLE);
            mNum.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            mBtnGetCode.setText(getResources().getString(R.string.login_getcode));
            mTvPw.setText(R.string.phone_code);
            mOpenEye.setVisibility(GONE);
            mBlock = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_getcode) {
            if (!mBlock) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(v);
            }
        } else if (v.getId() == R.id.open_eye) {
            mOpenEye.toggle();

            if (mOpenEye.isChecked()) {
                //开眼
                mNum.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                //关闭
                mNum.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            mNum.setSelection(mNum.getText().length());
        }
    }


}
