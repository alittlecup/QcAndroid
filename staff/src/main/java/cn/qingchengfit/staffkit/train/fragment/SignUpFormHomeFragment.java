package cn.qingchengfit.staffkit.train.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;

/**
 * Created by fb on 2017/3/21.
 */

public class SignUpFormHomeFragment extends BaseFragment {

	TabLayout tabLayout;
	ViewPager viewPager;
	Toolbar toolbar;
	TextView toolbarTitile;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_home, container, false);
      tabLayout = (TabLayout) view.findViewById(R.id.sign_up_tab);
      viewPager = (ViewPager) view.findViewById(R.id.sign_up_viewpager);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);

      initToolbar(toolbar);

        ArrayList<Fragment> fragments = new ArrayList<>();
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragments);

        if (adapter.getCount() <= 0) {
            fragments.add(new SignUpFormPersonalFragment());
            fragments.add(new SignUpFormGroupFragment());
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("报名表");
    }

    @Override public String getFragmentName() {
        return SignUpFormHomeFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
