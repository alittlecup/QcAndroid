package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
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

  int maxLength = 100;
  private TextView label;
  private EditText edit;
  private String str_label;
  private ImageView rightview;
  private int textColor = 0x9b9b9b;

  private String str_hint;
  private View disableView;
  private boolean isNum;
  private boolean canClick;
  private boolean canBeNull;
  private boolean showDivier;
  private boolean showRight;
  private boolean enable;
  @DrawableRes
  private int leftDrawable;
  private String strUnit;
  private int contentColor;
  private CharSequence str_content;
  private float labelWidth;
  @DrawableRes private int rightDrawable;

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
    rightDrawable =
      ta.getResourceId(R.styleable.CommonInputView_civ_right_icon, R.drawable.ic_arrow_right);
    maxLength = ta.getResourceId(R.styleable.CommonInputView_civ_max_length, 100);
    textColor = ta.getColor(R.styleable.CommonInputView_civ_text_color,
      CompatUtils.getColor(getContext(), R.color.text_grey));
    contentColor = ta.getColor(R.styleable.CommonInputView_civ_content_color,
      CompatUtils.getColor(getContext(), R.color.text_black));
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

  public void setEnable(boolean enable) {
    this.enable = enable;
    disableView.setVisibility(enable ? GONE : VISIBLE);
  }

  public void setUnit(String u) {
    this.strUnit = u;
    String str_label_unit = str_label;
    if (!TextUtils.isEmpty(strUnit)) {
      str_label_unit = TextUtils.concat(str_label, "（", strUnit, "）").toString().trim();
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

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    label = (TextView) findViewById(R.id.commoninput_lable);
    edit = (EditText) findViewById(R.id.commoninput_edit);
    edit.setFreezesText(false);
    View divider = findViewById(R.id.commoninput_divider);
    rightview = (ImageView) findViewById(R.id.commoninput_righticon);
    disableView = findViewById(R.id.disable);
    if (leftDrawable != 0){
      label.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
      label.setCompoundDrawablePadding(MeasureUtils.dpToPx(10f, getResources()));
    }
    String str_label_unit = str_label;
    if (!TextUtils.isEmpty(strUnit)) {
      str_label_unit = TextUtils.concat(str_label, "（", strUnit, "）").toString().trim();
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
    edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
    if (showRight) {
      rightview.setVisibility(VISIBLE);
      edit.setPadding(edit.getPaddingLeft(), edit.getPaddingTop(),
        MeasureUtils.dpToPx(20f, getResources()), edit.getPaddingBottom());
    } else {
      rightview.setVisibility(GONE);
      edit.setPadding(edit.getPaddingLeft(), edit.getPaddingTop(),
        MeasureUtils.dpToPx(0f, getResources()), edit.getPaddingBottom());
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
    edit.setTextColor(contentColor);
    edit.setText(str_content);
    edit.setHint(str_hint);
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

  public void setContent(String c) {
    edit.setText(c);
    if (c != null) edit.setSelection(c.length() > 20 ? 20 : c.length());
  }

  public void setContentColor(int color) {
    contentColor = color;
    edit.setTextColor(color);
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
    if (state == null ){
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

  //public class SavedState extends BaseSavedState {
  //    public final Creator<CommonInputView.SavedState> CREATOR = new Creator<CommonInputView.SavedState>() {
  //        @Override public CommonInputView.SavedState createFromParcel(Parcel in) {
  //            return new CommonInputView.SavedState(in);
  //        }
  //
  //        @Override public CommonInputView.SavedState[] newArray(int size) {
  //            return new CommonInputView.SavedState[size];
  //        }
  //    };
  //    String content;
  //    String label;
  //
  //    public String getContent() {
  //        return content;
  //    }
  //
  //    public void setContent(String content) {
  //        this.content = content;
  //    }
  //
  //    public String getLabel() {
  //        return label;
  //    }
  //
  //    public void setLabel(String label) {
  //        this.label = label;
  //    }
  //
  //    public SavedState(Parcelable superState) {
  //        super(superState);
  //    }
  //
  //    private SavedState(Parcel in) {
  //        super(in);
  //        content = in.readString();
  //        label = in.readString();
  //    }
  //
  //    @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
  //        super.writeToParcel(dest, flags);
  //        dest.writeString(content);
  //        dest.writeString(label);
  //    }
  //}
}
