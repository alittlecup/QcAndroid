package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;

public class CoachHomeActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);
        ButterKnife.bind(this);
        initActionbar();
    }

    private void initActionbar() {

    }
}
