package com.qingchengfit.fitcoach.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/9/22 2015.
 */
public class TextInputDialog extends Dialog {

    private View view;
    private Toolbar toolbar;
    private Button mOkbtn;
    private EditText mInputEt;

    public TextInputDialog(Context context) {
        super(context, R.style.ChoosePicDialogStyle);

        view = getLayoutInflater().inflate(R.layout.activity_text_input, null);
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout toolbarLayout = (RelativeLayout) view.findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) toolbarLayout.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        ((TextView) toolbarLayout.findViewById(R.id.toolbar_title)).setText("插入一段文字");
        toolbar.setNavigationOnClickListener(v -> this.dismiss());
        mOkbtn = (Button) view.findViewById(R.id.textinput_btn);
        mInputEt = (EditText) view.findViewById(R.id.textinput_et);
    }

    @Override public void show() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        super.show();
    }

    public void show(String content) {
        mInputEt.setText(content);
        show();
    }

    public void setOnOkListener(View.OnClickListener listener) {
        mOkbtn.setOnClickListener(listener);
    }

    public String getContent() {
        return mInputEt.getText().toString();
    }
}
