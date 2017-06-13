package cn.qingchengfit.staffkit.views.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.adapter.SigleModeMainPagerAdapter;
import cn.qingchengfit.staffkit.views.custom.TabView;

/**
 * power by
 * Created by yangming on 16/11/2.
 */
public class SigleModeHomeFragment extends BaseFragment {

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabview) TabView tabview;

    private CoachService mCoachService;
    private Brand mBrand;
    private String url;

    public static SigleModeHomeFragment newInstance(CoachService coachService, Brand brand) {
        Bundle args = new Bundle();
        SigleModeHomeFragment fragment = new SigleModeHomeFragment();
        args.putParcelable(Configs.EXTRA_GYM_SERVICE, coachService);
        args.putParcelable(Configs.EXTRA_BRAND, brand);

        fragment.setArguments(args);
        return fragment;
    }

    public static SigleModeHomeFragment newInstance(CoachService coachService, Brand brand, String open_url) {
        Bundle args = new Bundle();
        SigleModeHomeFragment fragment = new SigleModeHomeFragment();
        args.putParcelable(Configs.EXTRA_GYM_SERVICE, coachService);
        args.putParcelable(Configs.EXTRA_BRAND, brand);
        args.putString("open_url", open_url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sigle_mode_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCoachService = getArguments().getParcelable(Configs.EXTRA_GYM_SERVICE);
        mBrand = getArguments().getParcelable(Configs.EXTRA_BRAND);
        url = getArguments().getString("open_url");
        initTab();
        return view;
    }

    private void initTab() {
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new SigleModeMainPagerAdapter(getChildFragmentManager(), getContext(), mCoachService, mBrand));

        tabview.setViewPager(viewpager);
        tabview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override public void onPageSelected(int position) {
            }

            @Override public void onPageScrollStateChanged(int state) {
            }
        });
        if (!TextUtils.isEmpty(url)) {
            tabview.setCurrentItem(1);
            WebActivity.startWeb(url, getActivity());
        }
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public String getFragmentName() {
        return SigleModeHomeFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
