package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ShareDialogFragment;

public class StudentOrderPreviewActivity extends BaseAcitivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_order_preview);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_student_order_preview, StudentPreview.newInstance(getIntent().getStringExtra("url")))
            .commit();
    }

    @OnClick(R.id.send_friend) public void onClick() {
        ShareDialogFragment.newInstance("约课","约课", App.gUser.avatar,getIntent().getStringExtra("url"));
    }
}
