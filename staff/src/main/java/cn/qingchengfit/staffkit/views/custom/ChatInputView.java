package cn.qingchengfit.staffkit.views.custom;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.AppUtils;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/26 2016.
 */
public class ChatInputView extends LinearLayout {

    @BindView(R.id.add_other) ImageView addOther;
    @BindView(R.id.btn_send) Button btnSend;
    @BindView(R.id.add_camera) LinearLayout addCamera;
    @BindView(R.id.add_pic) LinearLayout addPic;
    @BindView(R.id.expand_layout) LinearLayout expandLayout;
    @BindView(R.id.input_et) EditText inputEt;
    OnSendCallback callback;

    public ChatInputView(Context context) {
        super(context);
        init();
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_chatinput, this, true);
    }

    public void close() {
        expandLayout.setVisibility(GONE);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btnSend.setVisibility(GONE);
                } else {
                    btnSend.setVisibility(VISIBLE);
                }
            }
        });
    }

    @OnClick({ R.id.input_et, R.id.add_other, R.id.btn_send, R.id.add_camera, R.id.add_pic }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_et://输入
                expandLayout.setVisibility(GONE);
                break;
            case R.id.add_other:
                if (expandLayout.getVisibility() == VISIBLE) {
                    expandLayout.setVisibility(GONE);
                } else {
                    expandLayout.setVisibility(VISIBLE);
                }
                if (getContext() instanceof Activity) {
                    AppUtils.hideKeyboard((Activity) getContext());
                }
                break;
            case R.id.btn_send:
                if (callback != null) {
                    callback.onSendMsg(inputEt.getText().toString());
                    inputEt.setText("");
                }
                break;
            case R.id.add_camera:
                if (callback != null) {
                    callback.onCamara();
                }
                break;
            case R.id.add_pic:
                if (callback != null) {
                    callback.onPicture();
                }
                break;
        }
    }

    public void setSendCallback(OnSendCallback callback) {
        this.callback = callback;
    }

    public interface OnSendCallback {
        void onSendMsg(String s);

        void onPicture();

        void onCamara();
    }
}
