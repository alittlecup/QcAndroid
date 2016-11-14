package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.ChooseAddressFragment;
import com.qingchengfit.fitcoach.fragment.guide.AddCycleFragment;

public class ChooseActivity extends AppCompatActivity {

    public static final int TO_CHOSSE_ADDRESS = 0;
    public static final int TO_CHOSSE_CIRCLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        Fragment fragment = new ChooseAddressFragment();
        switch (getIntent().getIntExtra("to",0)){
            case 1:
                fragment = new AddCycleFragment();

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
