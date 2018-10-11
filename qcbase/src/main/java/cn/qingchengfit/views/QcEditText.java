package cn.qingchengfit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import cn.qingchengfit.widgets.R;

/**
 * Created by huangbaole on 2018/4/20.
 */

public class QcEditText extends android.support.v7.widget.AppCompatEditText {
  public QcEditText(Context context) {
    this(context, null);
  }

  public QcEditText(Context context, AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public QcEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
    ;
  }

  private void init() {
    addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        //EditText editText = QcEditText.this;
        //if (!checkEditTextContentOverflow(editText)) {
        //  holderText = editText.getText().toString();
        //} else {
        //  int i = calculateEditTextContainLength(editText);
        //  holderText = editText.getText().toString();
        //  String showString =
        //      "..." + holderText.substring(holderText.length() - i, (holderText.length() - 1));
        //  editText.setText(showString);
        //}
        if (s != null && !TextUtils.isEmpty(s.toString())) {
          setCursorVisible(true);
        } else {
          setCursorVisible(false);
        }
      }
    });
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (TextUtils.isEmpty(getText())) {
      int paddingRight = getPaddingRight();
      int right = getRight();
      Drawable drawable = getResources().getDrawable(R.drawable.drawable_left_20_20);
      drawable.setBounds(right - paddingRight - 2, getTop() - getPaddingTop(), right - paddingRight,
          getBottom() - getPaddingBottom());
      drawable.draw(canvas);
    }
  }

  /**
   * 判断当前edittext显示是否溢出
   *
   * @param editText UI上展示的输入框控件
   */
  private boolean checkEditTextContentOverflow(EditText editText) {
    if (null == editText) return false;

    int viewWidth = editText.getWidth();
    float contentWidth = editText.getPaint().measureText(editText.getText().toString());
    return viewWidth < contentWidth;
  }

  /**
   * 计算当前edittext可容纳的字符长度
   *
   * @param editText 输入控件
   * @return 可容纳字符长度
   */
  private int calculateEditTextContainLength(EditText editText) {
    if (null == editText) return 0;

    int viewWidth = editText.getWidth();
    String contentString = editText.getText().toString();
    float contentWidth = editText.getPaint().measureText(contentString);

    return viewWidth * contentString.length() / (int) contentWidth;
  }
}
