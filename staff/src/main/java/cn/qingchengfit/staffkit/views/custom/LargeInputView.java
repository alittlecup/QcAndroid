package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;

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
 * Created by Paper on 16/1/27 2016.
 */
public class LargeInputView extends LinearLayout {
    EditText edit;
    TextView title;
    String label;

    public LargeInputView(Context context) {
        super(context);
        inflate(context, R.layout.layout_large_inputview, this);
        onFinishInflate();
    }

    public LargeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LargeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_large_inputview, this, true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LargeInputView);
        label = ta.getString(R.styleable.LargeInputView_liv_lable);
        ta.recycle();
    }

    public EditText getEditText() {
        return edit;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        title = ButterKnife.findById(this, R.id.title);
        edit = ButterKnife.findById(this, R.id.edit);
        title.setText(label);
    }

    public void setLabel(String s) {
        label = s;
        title.setText(s);
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        edit.addTextChangedListener(textWatcher);
    }

    public String getContent() {
        if (edit != null) {
            return edit.getText().toString();
        } else {
            return "";
        }
    }

    public void setContent(String content) {
        if (edit != null) edit.setText(content);
    }
}
