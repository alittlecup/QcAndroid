package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoachHomeActivity extends BaseAcitivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);
        ButterKnife.bind(this);
        initActionbar();
    }

    private void initActionbar() {


    }
}
