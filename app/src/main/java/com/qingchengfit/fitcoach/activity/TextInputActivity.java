package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.qingchengfit.fitcoach.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TextInputActivity extends AppCompatActivity {
    public static final String TAG = TextInputActivity.class.getName();
    /**
     * 输入title 默认为插入一段文字
     */
    public static final String TITLE = "TextInputActivity.title";
    /**
     * 原本的文字
     */
    public static final String ORIGIN = "TextInputActivity.origin";
    public static final String FIX_TEXT = "TextInputActivity.fixtext";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textinput_et)
    EditText textinputEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(-100);
            this.finish();
        });
        toolbar.setTitle("插入一段文字");
        if (getIntent() != null) {
            String title = getIntent().getStringExtra(TITLE);
            String origin = getIntent().getStringExtra(ORIGIN);
            if (title != null)
                toolbar.setTitle(title);
            if (origin != null) {
                textinputEt.setText(origin);
                textinputEt.setSelection(origin.length());
            }
        }


    }

    @OnClick(R.id.textinput_btn)
    public void onComfirm() {
        Intent it = new Intent();

        it.putExtra(FIX_TEXT, textinputEt.getText().toString());
        setResult(0, it);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        setResult(-100);
        this.finish();
    }
}
