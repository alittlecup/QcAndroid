package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.MeetingFragment;

public class MeetActivity extends BaseAcitivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_meet, new MeetingFragment()).commit();
    }
}
