package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.LoginFragment;
import com.qingchengfit.fitcoach.fragment.RegisterFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_viewpager)
    ViewPager loginViewpager;
    @Bind(R.id.login_tabview)
    TabLayout loginTablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(loginTablayout));
        loginViewpager.setAdapter(new LoginFragAdapter(getSupportFragmentManager()));
        loginTablayout.setupWithViewPager(loginViewpager);

    }

    class LoginFragAdapter extends FragmentPagerAdapter{

        public LoginFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                return  new LoginFragment();
            }else return new RegisterFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
           if (position ==0 ){
               return getString(R.string.login_btn_label);

           }else return getString(R.string.registe_indicat_label);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
