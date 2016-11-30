package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.ChooseAddressFragment;
import com.qingchengfit.fitcoach.fragment.guide.AddCycleFragment;
import com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragment;
import com.qingchengfit.fitcoach.fragment.schedule.ChooseScheduleGymFragment;

public class ChooseActivity extends BaseAcitivity {

    public static final int TO_CHOSSE_ADDRESS = 0;
    public static final int TO_CHOSSE_CIRCLE = 1;
    public static final int TO_CHOSSE_GYM = 2;
    public static final int TO_CHOSSE_GYM_SCHEDULE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        Fragment fragment = new ChooseAddressFragment();
        switch (getIntent().getIntExtra("to",0)){
            case 1:
                fragment = new AddCycleFragment();
                break;
            case TO_CHOSSE_GYM:
                fragment = new ChooseGymFragment();
                break;
            case TO_CHOSSE_GYM_SCHEDULE:
                fragment = new ChooseScheduleGymFragment();
                break;
            default:
                fragment = new ChooseAddressFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_choose_address,fragment)
                .commit();
    }
}
