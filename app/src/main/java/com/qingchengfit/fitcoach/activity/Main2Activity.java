package com.qingchengfit.fitcoach.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.ChooseAddressFragment;
import com.qingchengfit.fitcoach.fragment.ScheduesFragment;
import com.qingchengfit.fitcoach.fragment.WebFragment;
import com.qingchengfit.fitcoach.fragment.mine.MineFragmentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.TabView;

public class Main2Activity extends AppCompatActivity implements WebActivityInterface {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tabview)
    TabView tabview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setupVp();
    }

    private void setupVp() {
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
        tabview.setViewPager(viewpager);
    }

    @Override
    public void onfinish() {

    }


    public class MainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
        private int[] mIconSelect = {R.drawable.ic_achievement_group
                , R.drawable.ic_achievement_private
                , R.drawable.ic_achievement_sale
                , R.drawable.ic_actionbar_settings
        };
        private int[] mIconNormal = {R.drawable.ic_action_delete
                , R.drawable.ic_action_rest
                , R.drawable.ic_action_editor
                , R.drawable.ic_actionbar_settings
        };
        private Context context;

        MainPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ScheduesFragment();
            } else if (position == 1) {
                return new ChooseAddressFragment();
            } else if (position == 2) {
                return WebFragment.newInstance("http://www.baidu.com");
            } else
                return new MineFragmentFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int[] onIconSelect(int position) {
            int icon[] = new int[2];
            icon[0] = mIconSelect[position];
            icon[1] = mIconNormal[position];
            return icon;
        }

        @Override
        public String onTextSelect(int position) {
            return context.getResources().getStringArray(R.array.home_tab)[position];
        }

    }


}
