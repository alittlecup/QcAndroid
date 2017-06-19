package cn.qingchengfit.staffkit.views.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventUnlogBackPress;
import cn.qingchengfit.staffkit.rxbus.event.EventUnloginHomeLevel;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.adapter.MainPagerAdapter;
import cn.qingchengfit.staffkit.views.custom.TabView;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import rx.functions.Action1;

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
 * Created by Paper on 2017/2/22.
 */
@FragmentWithArgs public class UnLoginFragment extends BaseFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.brand_manage) TextView brandManage;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabview) TabView tabview;

    @Arg int currentPage = 0;
    @Arg String url;

    private int level = 0;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chain, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(new UnloginAdapter(getChildFragmentManager(), getContext()));
        viewpager.setCurrentItem(currentPage);
        brandManage.setVisibility(View.GONE);
        RxBusAdd(EventUnloginHomeLevel.class).subscribe(new Action1<EventUnloginHomeLevel>() {
            @Override public void call(EventUnloginHomeLevel eventUnloginHomeLevel) {
                level = eventUnloginHomeLevel.getLevel();
                setNavgitIcon();
            }
        });

        tabview.setViewPager(viewpager);
        tabview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f) {
                    if (position == 0) {
                        toolbarTitile.setText("健身房");

                        setNavgitIcon();
                    } else if (position == 1) {
                        toolbarTitile.setText("发现");
                        toolbar.setNavigationIcon(null);
                        toolbar.setNavigationOnClickListener(null);
                    } else {
                        toolbarTitile.setText("我");
                        toolbar.setNavigationIcon(null);
                        toolbar.setNavigationOnClickListener(null);
                    }
                }
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
        return view;
    }

    void setNavgitIcon() {
        if (level > 0) {

            toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    RxBus.getBus().post(new EventUnlogBackPress(level));
                }
            });
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
    }

    @Override public String getFragmentName() {
        return UnLoginFragment.class.getName();
    }

    public class UnloginAdapter extends MainPagerAdapter {

        public UnloginAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeUnLoginFragment();
            } else if (position == 1) {
                return QcVipFragment.newInstance(Configs.URL_QC_FIND);
            } else {
                return new SettingFragment();
            }
        }

        @Override public long getItemId(int position) {
            return POSITION_NONE;
        }
    }
}
