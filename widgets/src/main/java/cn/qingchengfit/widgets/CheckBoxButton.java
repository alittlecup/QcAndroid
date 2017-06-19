package cn.qingchengfit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/11/11.
 */
@Deprecated
public class CheckBoxButton extends LinearLayout implements QcCheckable{

    /*选中时字体颜色*/
    private int mTextColorSelect = 0xffffffff;
    /*未选中时字体颜色*/
    private int mTextColorNormal = 0xff999999;
    /*字体大小*/
    private int mTextSize = 12;

    /*选中时checkbox图标*/
    private int mCheckboxIconSelect;
    /*为选中时checkbox图标*/
    private int mCheckboxIconNormal;

    /*选中时控件背景*/
    private int mBackgroundSelect;
    /*文选中时控件背景*/
    private int mBackgroundNormal;

    /*控件显示的文字内容*/
    private String mContent = "";

    private String mHookIconLocation = "";

    private TextView content;
    private CheckBox checkBox;
    //private RelativeLayout root;

    /*控件是否选中*/
    private boolean isChecked = false;

    private Context mContext;

    private OnClickListener onClickListener;

    public CheckBoxButton(Context context) {
        super(context);
        inflate(context, R.layout.qcw_layout_checkable_button_left, this);
        init(context, null, 0);
        onFinishInflate();
    }

    public CheckBoxButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.qcw_layout_checkable_button_left, this);
        init(context, attrs, 0);
        onFinishInflate();
    }

    public CheckBoxButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.qcw_layout_checkable_button_left, this);
        init(context, attrs, defStyleAttr);
        onFinishInflate();
    }

    @TargetApi(21)
    public CheckBoxButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.qcw_layout_checkable_button_left, this);
        init(context, attrs, defStyleAttr);
        onFinishInflate();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckableButton);

        mTextColorSelect =
                ta.getColor(R.styleable.CheckableButton_cb_text_select_color, mTextColorSelect);
        mTextColorNormal =
                ta.getColor(R.styleable.CheckableButton_cb_text_normal_color, mTextColorNormal);

        mTextSize = (int) ta.getDimension(R.styleable.CheckableButton_cb_text_size, mTextSize);

        mCheckboxIconSelect = ta.getResourceId(R.styleable.CheckableButton_cb_hook_icon_select,
                R.drawable.checkbox_hook);
        mCheckboxIconNormal = ta.getResourceId(R.styleable.CheckableButton_cb_hook_icon_normal, 0);

        mBackgroundSelect = ta.getResourceId(R.styleable.CheckableButton_cb_background_select,
                R.drawable.qcw_shape_bgcenter_green);
        mBackgroundNormal = ta.getResourceId(R.styleable.CheckableButton_cb_background_normal,
                R.drawable.qcw_shape_bgcenter_white);

        mContent = ta.getString(R.styleable.CheckableButton_cb_text_content);
        //mHookIconLocation = ta.getString(R.styleable.CheckableButton_cb_hook_icon_location);

        isChecked = ta.getBoolean(R.styleable.CheckableButton_cb_select, false);
        ta.recycle();
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        content = (TextView) findViewById(R.id.tv_content);
        checkBox = (CheckBox) findViewById(R.id.cb_hoot);
        //root = (RelativeLayout) findViewById(R.id.rl_root);

        content.setText(mContent);
        content.setTextSize(mTextSize);

        checkBox.setChecked(isChecked);
        setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                checkBox.toggle();
                checkBox.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
                setTextColorAndbackGround();
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
                for (CompoundButton.OnCheckedChangeListener lister : mListener) {
                    if (view instanceof CheckBoxButton)
                        lister.onCheckedChanged(((CheckBoxButton) view).checkBox,checkBox.isChecked());
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            checkBox.setButtonDrawable(mCheckboxIconSelect);
        setTextColorAndbackGround();
    }

    /**
     * 设置文字颜色和背景
     */
    public void setTextColorAndbackGround() {
        setBackgroundResource(checkBox.isChecked() ? mBackgroundSelect : mBackgroundNormal);
        content.setTextColor(checkBox.isChecked() ? mTextColorSelect : mTextColorNormal);
    }

    public void toggle() {
        checkBox.setChecked(!checkBox.isChecked());
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
        checkBox.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
        setTextColorAndbackGround();
    }


    public void setClick(OnClickListener listener) {
        this.onClickListener = listener;
    }

    public void setContent(String contentStr) {
        content.setText(contentStr);
    }

    public String getContent(){
        return content.getText().toString();
    }

    private List<CompoundButton.OnCheckedChangeListener> mListener = new ArrayList<>();

    @Override public void addCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mListener.add(listener);
    }

    @Override public boolean isOrContainCheck(View v) {
        return checkBox.equals(v);
    }
}
