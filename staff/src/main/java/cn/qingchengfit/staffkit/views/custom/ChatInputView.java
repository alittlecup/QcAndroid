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

  ImageView addOther;
  Button btnSend;
  LinearLayout addCamera;
  LinearLayout addPic;
  LinearLayout expandLayout;
  EditText inputEt;
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
    addOther = (ImageView) findViewById(R.id.add_other);
    btnSend = (Button) findViewById(R.id.btn_send);
    addCamera = (LinearLayout) findViewById(R.id.add_camera);
    addPic = (LinearLayout) findViewById(R.id.add_pic);
    expandLayout = (LinearLayout) findViewById(R.id.expand_layout);
    inputEt = (EditText) findViewById(R.id.input_et);
    findViewById(R.id.input_et).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChatInputView.this.onClick(v);
      }
    });
    findViewById(R.id.add_other).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChatInputView.this.onClick(v);
      }
    });
    findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChatInputView.this.onClick(v);
      }
    });
    findViewById(R.id.add_camera).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChatInputView.this.onClick(v);
      }
    });
    findViewById(R.id.add_pic).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChatInputView.this.onClick(v);
      }
    });
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

  public void onClick(View view) {
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
