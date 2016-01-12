package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BodyTestFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BodyTestActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_test);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, BodyTestFragment.newInstance("", ""))
                .commit();

    }



}
