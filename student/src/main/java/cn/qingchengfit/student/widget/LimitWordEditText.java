package cn.qingchengfit.student.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import cn.qingchengfit.student.R;

/**
 * 限制字数的EditTextView
 */
public class LimitWordEditText extends android.support.v7.widget.AppCompatEditText {
  private int limit;
  private CharSequence already;
  public LimitWordEditText(Context context) {
    this(context, null);
  }

  public LimitWordEditText(Context context, AttributeSet attrs) {
    this(context, attrs,-1);
  }

  public LimitWordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    if (attrs != null){
      TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LimitWordEditText);
      limit = array.getInteger(R.styleable.LimitWordEditText_limit, -1);
      array.recycle();
    }
    init();
  }

  public void setLimit(int limit){
    this.limit = limit;
  }

  private void init(){

    this.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        already = s;
      }

      @Override public void afterTextChanged(Editable s) {
        if (limit <= 0) {
          setText(s.toString());
          return;
        }
        if (already.length() > limit) {
          s.delete(getSelectionStart() - 1 < 0 ? 0 : getSelectionStart() - 1, getSelectionEnd());
          setSelection(getSelectionEnd());
        }
      }
    });
  }

}
