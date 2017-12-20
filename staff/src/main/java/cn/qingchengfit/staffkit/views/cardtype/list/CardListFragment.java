package cn.qingchengfit.staffkit.views.cardtype.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.cardtype.detail.EditCardTypeFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/14 2016.
 */
public class CardListFragment extends BaseFragment {
    @BindView(R.id.tab) TabLayout tab;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    private String mChooseShopId, mId, mModel;
    private CardViewpagerAdapter adapter;
    /**
     * 记录筛选 卡是否停用 0的时候是正常
     */
    private int mCardDisable = 0;

    public int getCardDisable() {
        return mCardDisable;
    }

    public void setCardDisable(int pos) {
        this.mCardDisable = pos;
        adapter.notifyDataSetChanged();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardtype_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("会员卡种类", false, new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        }, R.menu.menu_add, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (serPermisAction.checkNoOne(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), EditCardTypeFragment.newInstance(0))
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
                return true;
            }
        });
        viewpager.setOffscreenPageLimit(4);
        adapter = new CardViewpagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.setupWithViewPager(viewpager);
        return view;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 9) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    mCallbackActivity.setToolbar(getString(R.string.all_gyms), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(CardListFragment.this, 9, getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, R.menu.menu_add, new Toolbar.OnMenuItemClickListener() {
                        @Override public boolean onMenuItemClick(MenuItem item) {
                            getFragmentManager().beginTransaction()
                                .replace(mCallbackActivity.getFragId(), EditCardTypeFragment.newInstance(0))
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                            return true;
                        }
                    });
                    mId = mModel = null;
                } else {
                    mCallbackActivity.setToolbar(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(CardListFragment.this, 9, getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, 0, null);
                    CoachService gym =
                        gymBaseInfoAction.getGymByShopIdNow(PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""),
                            mChooseShopId);
                    mId = gym.getId();
                    mModel = gym.getModel();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return CardListFragment.class.getName();
    }

    class CardViewpagerAdapter extends FragmentStatePagerAdapter {

        public CardViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

        @Override public Fragment getItem(int position) {
            return CardTypeListFragment.newInstance(position, mId, mModel);
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override public int getCount() {
            return 4;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.cardtype_all);
                case 1:
                    return getString(R.string.cardtype_value);
                case 2:
                    return getString(R.string.cardtype_times);
                case 3:
                    return getString(R.string.cardtype_date);
                default:
                    return getString(R.string.cardtype_all);
            }
        }
    }
}
