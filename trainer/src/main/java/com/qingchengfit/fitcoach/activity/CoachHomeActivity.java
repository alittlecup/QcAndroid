package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;

public class CoachHomeActivity extends BaseActivity {

	Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);
      toolbar = (Toolbar) findViewById(R.id.toolbar);

      initActionbar();
    }

    private void initActionbar() {

    }
}
