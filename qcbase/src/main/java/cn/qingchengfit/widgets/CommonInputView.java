package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;

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
 * Created by Paper on 15/9/8 2015.
 */
public class CommonInputView extends RelativeLayout {

  int maxLength = -1;
  private TextView label;
  private TextView unit;
  private EditText edit;
  private String str_label;
  private ImageView rightview;
  private View cursor;
  private int textColor = 0x9b9b9b;

  private String str_hint;
  private View disableView;
  private boolean isNum;
  private boolean canClick;
  private boolean canBeNull;
  private boolean showDivier;
  private boolean showRight;
  private boolean enable = true;
  @DrawableRes private int leftDrawable;
  private String strUnit;
  private ColorStateList contentColor;
  private CharSequence str_content;
  private float labelWidth;
  @DrawableRes private int rightDrawable;
  private OnTextChangedListener onTextChangedListener;
  private boolean editable;

  public CommonInputView(Context context) {
    super(context);
    showDivier = true;
    inflate(context, R.layout.layout_commoninput, this);
    textColor = CompatUtils.getColor(getContext(), R.color.text_dark);
    onFinishInflate();
  }

  public CommonInputView(Context context, boolean isNum, String content) {
    super(context);
    str_content = content;
    showDivier = true;
    this.setIsNum(isNum);
    textColor = CompatUtils.getColor(getContext(), R.color.text_dark);

    inflate(context, R.layout.layout_commoninput, this);
    onFinishInflate();
  }

  public CommonInputView(Context context, boolean isNum, boolean canBeNull, String content) {
    super(context);
    str_content = content;
    showDivier = true;
    this.canBeNull = canBeNull;
    this.setIsNum(isNum);
    textColor = CompatUtils.getColor(getContext(), R.color.text_dark);

    inflate(context, R.layout.layout_commoninput, this);
    onFinishInflate();
  }

  public CommonInputView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public CommonInputView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  public boolean isNum() {
    return isNum;
  }

  public void setIsNum(boolean isNum) {
    this.isNum = isNum;
  }

  public void init(Context context, AttributeSet attrs) {
    setSaveEnabled(true);
    LayoutInflater.from(context).inflate(R.layout.layout_commoninput, this, true);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonInputView);
    str_label = ta.getString(R.styleable.CommonInputView_civ_lable);
    str_content = ta.getString(R.styleable.CommonInputView_civ_content);
    str_hint = ta.getString(R.styleable.CommonInputView_civ_hint);
    isNum = ta.getBoolean(R.styleable.CommonInputView_civ_inputnum, false);
    canClick = ta.getBoolean(R.styleable.CommonInputView_civ_clickable, false);
    canBeNull = ta.getBoolean(R.styleable.CommonInputView_civ_nonnull, false);
    showDivier = ta.getBoolean(R.styleable.CommonInputView_civ_showdivier, true);
    showRight = ta.getBoolean(R.styleable.CommonInputView_civ_showright, false);
    editable = ta.getBoolean(R.styleable.CommonInputView_civ_edit_able, true);
    rightDrawable = ta.getResourceId(R.styleable.CommonInputView_civ_right_icon,
        R.drawable.vd_arrow_right_grey);
    maxLength = ta.getInt(R.styleable.CommonInputView_civ_max_length, -1);
    textColor = ta.getColor(R.styleable.CommonInputView_civ_text_color,
        CompatUtils.getColor(getContext(), R.color.text_dark));
    contentColor = ta.getColorStateList(R.styleable.CommonInputView_civ_content_color);
    strUnit = ta.getString(R.styleable.CommonInputView_civ_unit);
    labelWidth = ta.getDimension(R.styleable.CommonInputView_civ_label_width, -1);
    leftDrawable = ta.getResourceId(R.styleable.CommonInputView_civ_left_icon, 0);
    ta.recycle();
  }

  public boolean isShowRight() {
    return showRight;
  }

  public void setShowRight(boolean showRight) {
    this.showRight = showRight;
    rightview.setVisibility(showRight ? VISIBLE : GONE);
  }

  public void toggle() {
    setShowRight(!isShowRight());
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEditable(boolean editable) {
    edit.setEnabled(editable);

    if (editable) {
      edit.setCursorVisible(false);
    }
  }

  public boolean isEditable() {
    return edit.isEnabled();
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
    edit.setFocusable(enable);
    edit.setFocusableInTouchMode(enable);
    disableView.setVisibility(enable ? GONE : VISIBLE);

  }

  public void setLabelColor(@ColorRes int labelColor) {
    label.setTextColor(getResources().getColor(labelColor));
  }

  public void setLabelDrawable(@DrawableRes int drawable) {
    View image1 = findViewById(R.id.im_icon1);
    if (image1 instanceof ImageView) {
      ((ImageView) image1).setImageResource(drawable);
      image1.setVisibility(VISIBLE);
    }
  }
  public void setLabelDrawable(@DrawableRes int firstDrawable, @DrawableRes int secondDrawable) {
    View image1 = findViewById(R.id.im_icon1);
    View image2 = findViewById(R.id.im_icon2);
    if (image1 instanceof ImageView) {
      ((ImageView) image1).setImageResource(firstDrawable);
      image1.setVisibility(VISIBLE);
    }
    if (image2 instanceof ImageView) {
      ((ImageView) image2).setImageResource(secondDrawable);
      image2.setVisibility(VISIBLE);
    }
  }

  public void setUnit(String u) {
    this.strUnit = u;
    String str_label_unit = str_label;
    if (!TextUtils.isEmpty(strUnit)) {
      rightview.setVisibility(GONE);
      unit.setVisibility(VISIBLE);
      unit.setText(strUnit);
    } else {
      rightview.setVisibility(VISIBLE);
      unit.setText("");
      unit.setVisibility(GONE);
    }
    if (!canBeNull) {
      label.setText(str_label_unit);
    } else {
      SpannableString s = new SpannableString(str_label_unit + " *");
      int l = s.length();
      s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l,
          Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
      label.setText(s);
    }
    invalidate();
  }

  public void setNoSaved() {
    if (edit != null) {
      edit.setSaveEnabled(false);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    label = (TextView) findViewById(R.id.commoninput_lable);
    edit = (EditText) findViewById(R.id.commoninput_edit);
    View divider = findViewById(R.id.commoninput_divider);
    cursor = findViewById(R.id.edit_cursor);
    unit = findViewById(R.id.text_unit);
    rightview = (ImageView) findViewById(R.id.commoninput_righticon);
    disableView = findViewById(R.id.disable);
    if (leftDrawable != 0) {
      label.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
      label.setCompoundDrawablePadding(MeasureUtils.dpToPx(10f, getResources()));
    }
    String str_label_unit = str_label;
    if (!TextUtils.isEmpty(strUnit)) {
      rightview.setVisibility(GONE);
      unit.setVisibility(VISIBLE);
      unit.setText(strUnit);
    } else {
      rightview.setVisibility(VISIBLE);
      unit.setText("");
      unit.setVisibility(GONE);
    }
    if (!canBeNull) {
      label.setText(str_label_unit);
    } else {
      SpannableString s = new SpannableString(str_label_unit + " *");
      int l = s.length();
      s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l,
          Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
      label.setText(s);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      label.setTextAppearance(R.style.QcTextStyleStandardDark);
    } else {
      label.setTextAppearance(getContext(), R.style.QcTextStyleStandardDark);
    }
    label.setTextColor(textColor);

    if (canClick) {
      edit.setFocusable(false);
      edit.setFocusableInTouchMode(false);
    } else {
      edit.setFocusableInTouchMode(true);
      edit.setFocusable(true);
      setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          edit.setClickable(true);
          edit.requestFocus();
          AppUtils.showKeyboard(getContext(), edit);
        }
      });
    }

    if (showDivier) {
      divider.setVisibility(VISIBLE);
    } else {
      divider.setVisibility(GONE);
    }
    rightview.setImageResource(rightDrawable);
    if (maxLength != -1) {
      edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
    }
    if (showRight) {
      rightview.setVisibility(VISIBLE);
    } else {
      rightview.setVisibility(GONE);
    }
    if (isNum) {
      edit.setInputType(InputType.TYPE_CLASS_PHONE);
      if (labelWidth > 0) {
        label.setMaxWidth(MeasureUtils.dpToPx(labelWidth, getResources()));
      } else {
        label.setMaxWidth((int) getResources().getDimension(R.dimen.qc_civ_label_max_width_phone));
      }
    } else {
      edit.setInputType(InputType.TYPE_CLASS_TEXT);
      if (labelWidth > 0) {
        label.setMaxWidth(MeasureUtils.dpToPx(labelWidth, getResources()));
      } else {
        label.setMaxWidth((int) getResources().getDimension(R.dimen.qc_civ_label_max_width_txt));
      }
    }
    if (contentColor != null) {
      edit.setTextColor(contentColor);
    }
    edit.setText(str_content);
    edit.setHint(str_hint);
    setEditable(editable);
    if (canClick) {
      edit.setKeyListener(null);
    }
    edit.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s != null && !TextUtils.isEmpty(s.toString())) {
          edit.setCursorVisible(true);
          cursor.setVisibility(GONE);
        } else {
          if (edit.isFocused()) {
            cursor.setVisibility(VISIBLE);
            edit.setCursorVisible(false);
          }
        }
      }
    });
    edit.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          if (TextUtils.isEmpty(edit.getText())) {
            cursor.setVisibility(VISIBLE);
            edit.setCursorVisible(false);
          } else {
            edit.setCursorVisible(true);
          }
        } else {
          cursor.setVisibility(GONE);
        }
        if(focusChangeListener!=null){
          focusChangeListener.onFocusChange(v, hasFocus);
        }
      }
    });
    cursor.setTag(cursor.getVisibility());
    cursor.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
      int newVis = cursor.getVisibility();
      if (((int) cursor.getTag()) != newVis) {
        switch (newVis) {
          case View.VISIBLE:
            AlphaAnimation alphaAnimation1 = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation1.setDuration(400);
            alphaAnimation1.setInterpolator(new LinearInterpolator());
            alphaAnimation1.setRepeatCount(Animation.INFINITE);
            alphaAnimation1.setRepeatMode(Animation.REVERSE);
            cursor.setAnimation(alphaAnimation1);
            alphaAnimation1.start();
            break;
          case View.GONE:
          case View.INVISIBLE:
            cursor.clearAnimation();
            break;
        }
        cursor.setTag(newVis);
      }
    });

    label.setOnTouchListener((view, motionEvent) -> false);
  }

  public void setOnEditNoClick(boolean enable) {
    this.enable = enable;
    this.canClick = enable;
    if (!enable) edit.setTextColor(getResources().getColor(R.color.text_grey));
  }

  public void setCanClick(boolean canClick) {
    this.canClick = canClick;
    if (canClick) {
      edit.setClickable(false);
    } else {
      edit.setEnabled(true);
      edit.setClickable(true);
      setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          edit.setClickable(true);
          edit.requestFocus();
          AppUtils.showKeyboard(getContext(), edit);
        }
      });
    }
  }

  public void addTextWatcher(TextWatcher watcher) {
    edit.addTextChangedListener(watcher);
  }

  public void setLabel(String s) {
    if (!canBeNull) {
      label.setText(s);
    } else {
      SpannableString sj = new SpannableString(s + " *");
      int l = sj.length();
      sj.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l,
          Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
      label.setText(sj);
    }
  }

  public void setHint(String s) {
    if (edit != null) edit.setHint(s);
  }

  public EditText getEditText() {
    return edit;
  }

  public String getLable() {
    return label.getText().toString();
  }

  public void setMaxLines(int maxLines) {
    if (edit != null) {
      edit.setMaxLines(maxLines);
      edit.setMaxLines(Integer.MAX_VALUE);
      edit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
    }
  }

  public String getContent() {
    return edit.getText().toString().trim();
  }

  public String getCivContent() {
    return getContent();
  }

  public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
    this.onTextChangedListener = onTextChangedListener;
  }

  public OnTextChangedListener getOnTextChangedListener() {
    return onTextChangedListener;
  }

  public void removeTextChangedListener() {
    this.onTextChangedListener = null;
  }

  public void setContent(String c) {
    if (!TextUtils.isEmpty(c) && c.equalsIgnoreCase(edit.getText().toString())) {
      return;
    }
    edit.setText(c);
    if (c != null) edit.setSelection(edit.getText().length() > 20 ? 20 : edit.getText().length());
    if (onTextChangedListener != null) {
      onTextChangedListener.onTextChanged();
    }
  }

  public void setCivContent(String c) {
    setContent(c);
  }

  public void setContent(SpannableString c) {
    edit.setText(c);
    if (c != null) edit.setSelection(edit.getText().length() > 20 ? 20 : edit.getText().length());
  }

  public void setContentColor(int color) {
    edit.setTextColor(color);
  }

  public void setContentType(int type) {
    edit.setInputType(type);
  }

  public boolean isEmpty() {
    return TextUtils.isEmpty(edit.getText());
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (canClick) return true;
    return super.onInterceptTouchEvent(ev);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  @Override public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);

    ss.childrenStates = new SparseArray();
    for (int i = 0; i < getChildCount(); i++) {
      getChildAt(i).saveHierarchyState(ss.childrenStates);
    }
    return ss;
  }

  @Override public void onRestoreInstanceState(Parcelable state) {
    if (state == null) {
      return;
    }
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    for (int i = 0; i < getChildCount(); i++) {
      getChildAt(i).restoreHierarchyState(ss.childrenStates);
    }
  }

  @Override protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
    dispatchFreezeSelfOnly(container);
  }

  @Override protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
    dispatchThawSelfOnly(container);
  }

  static class SavedState extends BaseSavedState {
    public static final ClassLoaderCreator<SavedState> CREATOR =
        new ClassLoaderCreator<SavedState>() {
          @Override public SavedState createFromParcel(Parcel source, ClassLoader loader) {
            return new SavedState(source, loader);
          }

          @Override public SavedState createFromParcel(Parcel source) {
            return createFromParcel(null);
          }

          public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
    SparseArray childrenStates;

    SavedState(Parcelable superState) {
      super(superState);
    }

    private SavedState(Parcel in, ClassLoader classLoader) {
      super(in);
      childrenStates = in.readSparseArray(classLoader);
    }

    @Override public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeSparseArray(childrenStates);
    }
  }

  public interface OnTextChangedListener {
    void onTextChanged();
  }

  public void setOnFocusChangeListener(OnFocusChangeListener focusChangeListener) {
    this.focusChangeListener = focusChangeListener;
  }

  private OnFocusChangeListener focusChangeListener;
}
