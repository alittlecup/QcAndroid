package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.guide.GuideFragment;

public class GuideActivity extends BaseAcitivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag,new GuideFragment())
                .commit();

    }




}
