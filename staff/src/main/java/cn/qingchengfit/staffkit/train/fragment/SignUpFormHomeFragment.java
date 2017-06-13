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
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import java.util.ArrayList;

/**
 * Created by fb on 2017/3/21.
 */

public class SignUpFormHomeFragment extends BaseFragment {

    @BindView(R.id.sign_up_tab) TabLayout tabLayout;
    @BindView(R.id.sign_up_viewpager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_home, container, false);
        unbinder = ButterKnife.bind(this, view);

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
