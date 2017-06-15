package cn.qingchengfit.staffkit.views.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.TabView;
import cn.qingchengfit.staffkit.views.main.SettingFragment;

/**
 * Created by Paper on 16/8/10.
 */
public class SigleModeMainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
    private int[] mIconSelect = {
        R.drawable.ic_gyms_selected, R.drawable.vector_tabbar_discover_active, R.drawable.ic_tabbar_account_active
    };
    private int[] mIconNormal = {
        R.drawable.ic_gyms, R.drawable.vector_tabbar_discover_nomal, R.drawable.ic_tabbar_account_normal
    };
    private Context context;
    private CoachService coachService;
    private Brand mBrand;

    public SigleModeMainPagerAdapter(FragmentManager fm, Context context, CoachService coachService, Brand brand) {
        super(fm);
        this.context = context;
        this.mBrand = brand;
        this.coachService = coachService;
    }

    @Override public Fragment getItem(int position) {
        //if (position == 0) {
        //    return GymDetailFragment.newInstance(coachService,mBrand, true);
        //} else if (position == 1) {
        //    return QcVipFragment.newInstance(Configs.URL_QC_FIND, true);
        //} else {
        return new SettingFragment();
        //}
    }

    @Override public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override public int getCount() {
        return 3;
    }

    @Override public int[] onIconSelect(int position) {
        int icon[] = new int[2];
        icon[0] = mIconSelect[position];
        icon[1] = mIconNormal[position];
        return icon;
    }

    @Override public String onTextSelect(int position) {
        return context.getResources().getStringArray(R.array.home_tab_sigle)[position];
    }
}
