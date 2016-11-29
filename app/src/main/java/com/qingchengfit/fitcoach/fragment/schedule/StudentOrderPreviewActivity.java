package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;

public class StudentOrderPreviewActivity extends BaseAcitivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_order_preview);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_student_order_preview,StudentPreview.newInstance(getIntent().getStringExtra("url")))
            .commit();
    }



}
