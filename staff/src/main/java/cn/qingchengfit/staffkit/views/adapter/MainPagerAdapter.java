package cn.qingchengfit.staffkit.views.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.MainFirstFragment;
import cn.qingchengfit.staffkit.views.custom.TabView;
import cn.qingchengfit.staffkit.views.main.MainMsgFragment;
import cn.qingchengfit.staffkit.views.main.QcVipFragment;
import cn.qingchengfit.staffkit.views.main.SettingFragment;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/10.
 */
public class MainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
    private int[] mIconSelect = {
        //                R.drawable.ic_home_selected
        R.drawable.ic_gyms_selected, R.drawable.vector_tabbar_discover_active, R.drawable.vd_tabbar_message_active,
        R.drawable.ic_tabbar_account_active
    };
    private int[] mIconNormal = {
        //            R.drawable.ic_home
        R.drawable.ic_gyms, R.drawable.vector_tabbar_discover_nomal, R.drawable.vd_tabbar_message_normal, R.drawable.ic_tabbar_account_normal
    };
    private Context context;
    private int status = 0; // 0 正常状态 1 单场馆状态 2 未登录状态

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //List<Fragment> fgs = new ArrayList<>();
    public int getStatus() {
        return status;
    }

    @Override public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return QcVipFragment.newInstance(Configs.URL_QC_FIND);
            case 2:
                return new MainMsgFragment();
            case 3:
                return new SettingFragment();
            default:
                return new MainFirstFragment();
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override public int getCount() {
        return 4;
    }

    @Override public int[] onIconSelect(int position) {
        int icon[] = new int[2];
        icon[0] = mIconSelect[position];
        icon[1] = mIconNormal[position];
        return icon;
    }

    @Override public String onTextSelect(int position) {
        return context.getResources().getStringArray(R.array.home_tab)[position];
    }
}
