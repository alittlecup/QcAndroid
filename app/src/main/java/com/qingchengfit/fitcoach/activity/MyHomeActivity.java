package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.MyHomeFragment;

public class MyHomeActivity extends AppCompatActivity {
    public static final String TAG = MyHomeActivity.class.getName();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.myhome_fraglayout, new MyHomeFragment()).commit();
    }

}
