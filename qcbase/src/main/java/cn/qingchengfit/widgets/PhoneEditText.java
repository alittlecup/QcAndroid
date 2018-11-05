package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ToastUtils;

/**
 * TODO: document your custom view class.
 */
public class PhoneEditText extends LinearLayout
    implements View.OnClickListener, AdapterView.OnItemClickListener {

  private EditText mPhoneNum;
  private TextView mDistrict;
  private DialogList mDialog;
  private boolean showIcon = true;
  private boolean onlyMainland = false;
  //0: china
  //1: china taiwan
  private int mDistrictInt = 0;
  private ImageView mLeftIcon;
  private boolean noNull = false;
  private TextView noNullTv;
  private String hintStr;
  private OnEditFocusListener onEditFocusListener;

  public PhoneEditText(Context context) {
    super(context);
    setOrientation(LinearLayout.HORIZONTAL);
    inflate(context, R.layout.layout_login_edittext, this);
    init(null, 0);
    onFinishInflate();
  }

  public PhoneEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOrientation(LinearLayout.HORIZONTAL);
    inflate(context, R.layout.layout_login_edittext, this);
    init(attrs, 0);

    onFinishInflate();
  }

  public PhoneEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setOrientation(LinearLayout.HORIZONTAL);
    inflate(context, R.layout.layout_login_edittext, this);
    init(attrs, defStyle);

    onFinishInflate();
  }

  public void setOnEditFocusListener(OnEditFocusListener onEditFocusListener) {
    this.onEditFocusListener = onEditFocusListener;
  }

  private void init(AttributeSet attrs, int defStyle) {

    //        LayoutInflater.from(context).inflate(R.layout.layout_login_edittext, this, true);
    // Load attributes
    final TypedArray a =
        getContext().obtainStyledAttributes(attrs, R.styleable.PhoneEditText, defStyle, 0);
    showIcon = a.getBoolean(R.styleable.PhoneEditText_phone_show_icon, true);
    noNull = a.getBoolean(R.styleable.PhoneEditText_phone_nonull, false);
    hintStr = a.getString(R.styleable.PhoneEditText_phone_hint);
    onlyMainland = a.getBoolean(R.styleable.PhoneEditText_phone_only_mainland, false);
    a.recycle();

    if (isInEditMode()) {
      return;
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mPhoneNum = (EditText) findViewById(R.id.et_phone);
    mDistrict = (TextView) findViewById(R.id.tv_distict);
    mLeftIcon = (ImageView) findViewById(R.id.left_icon);
    if (!TextUtils.isEmpty(hintStr)) mPhoneNum.setHint(hintStr);
    noNullTv = (TextView) findViewById(R.id.nonull_tv);
    noNullTv.setVisibility(noNull ? VISIBLE : GONE);

    if (onlyMainland) {
      mLeftIcon.setVisibility(GONE);
      mDistrict.setText("手机号");
      findViewById(R.id.img_down).setVisibility(GONE);
      findViewById(R.id.img_divier).setVisibility(GONE);
    } else {
      findViewById(R.id.img_down).setOnClickListener(this);
      mDistrict.setOnClickListener(this);
      mLeftIcon.setVisibility(showIcon ? VISIBLE : GONE);
      mPhoneNum.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override public void onFocusChange(View v, boolean hasFocus) {
          if (onEditFocusListener != null) {
            onEditFocusListener.onFocusChange(hasFocus);
          }
        }
      });
    }
  }

  @Override public void onClick(View v) {
    if (mDialog == null) {
      mDialog = DialogList.builder(getContext())
          .list(getResources().getStringArray(R.array.country_and_districion_list), this);
    }
    if (!mDialog.isShowing()) mDialog.show();
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    try {
      mDistrict.setText(
          getResources().getStringArray(R.array.country_and_districion_list)[position]);
      mDistrictInt = position;
      if (onDistrictIntChangeListener != null) {
        onDistrictIntChangeListener.onDistrictIntChange(getDistrictInt());
      }
    } catch (Exception e) {
      CrashUtils.sendCrash(e);
    }
  }

  public boolean checkPhoneNum() {
    String phoneNum = mPhoneNum.getText().toString().trim();
    if (mDistrictInt == 0) {
      //china
      if (phoneNum.length() != 11 || !phoneNum.startsWith("1")) {
        ToastUtils.show(getResources().getString(R.string.err_login_phonenum));
        return false;
      }
    } else if (mDistrictInt == 1) {
      //china taiwan
      if (phoneNum.length() != 10 || !phoneNum.startsWith("09")) {
        ToastUtils.show(getResources().getString(R.string.err_login_phonenum));
        return false;
      }
    }
    return true;
  }

  public String getPhoneNum() {
    return mPhoneNum.getText().toString().trim();
  }

  public void setPhoneNum(String s) {
    mPhoneNum.setText(s);
  }

  public void addTextChangedListener(TextWatcher textWatcher) {
    mPhoneNum.addTextChangedListener(textWatcher);
  }

  public String getDistrictInt() {
    switch (mDistrictInt) {
      case 0:
        return "+86";
      case 1:
        return "+886";
      default:
        return "+86";
    }
  }

  public void setdistrictInt(String s) {
    if (TextUtils.isEmpty(s)) {
      mDistrictInt = 0;
    } else {
      if (s.equalsIgnoreCase("+86")) {
        mDistrictInt = 0;
      } else if (s.equalsIgnoreCase("+886")) {
        mDistrictInt = 1;
      } else {
        mDistrictInt = 0;
      }
    }
    mDistrict.setText(
        getResources().getStringArray(R.array.country_and_districion_list)[mDistrictInt]);
  }

  private onDistrictIntChangeListener onDistrictIntChangeListener;

  public void setOnDistrictIntChangeListener(
      PhoneEditText.onDistrictIntChangeListener onDistrictIntChangeListener) {
    this.onDistrictIntChangeListener = onDistrictIntChangeListener;
  }

  public interface onDistrictIntChangeListener {
    void onDistrictIntChange(String type);
  }

  public interface OnEditFocusListener {
    void onFocusChange(boolean isFocus);
  }

  public void setEditble(boolean editble) {
    mPhoneNum.setFocusable(editble);
    mPhoneNum.setFocusableInTouchMode(editble);
    if (editble) {
      mDistrict.setOnClickListener(this);
    } else {
      mDistrict.setOnClickListener(view -> {
      });
    }
    mPhoneNum.setAlpha(editble ? 1f : 0.5f);
    findViewById(R.id.img_down).setVisibility(editble?View.VISIBLE:View.GONE);
  }
}
