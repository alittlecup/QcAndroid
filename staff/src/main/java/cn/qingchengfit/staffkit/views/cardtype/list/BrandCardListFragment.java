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
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.RxCardTypeEvent;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.cardtype.detail.EditCardTypeFragment;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
public class BrandCardListFragment extends BaseFragment {
    @BindView(R.id.tab) TabLayout tab;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @Inject RestRepository restRepository;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private String mChooseShopId, mChooseShopName;//, mId, mModel;
    private CardViewpagerAdapter adapter;
    private int mCardtype = 0;
    private int mCardDisable = 0;
    private int type;//卡种类

    public int getCardDisable() {
        return mCardDisable;
    }

    public void setCardDisable(int cardDisable) {
        this.mCardDisable = cardDisable;
        refresh();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar(TextUtils.isEmpty(mChooseShopName) ? getString(R.string.all_gyms) : mChooseShopName, true,
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ChooseGymActivity.start(BrandCardListFragment.this, 9, PermissionServerUtils.CARDSETTING,
                        getString(R.string.choose_gym), mChooseShopId);
                }
            }, R.menu.menu_add, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (SerPermisAction.checkNoOne(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
                        showAlert(R.string.alert_permission_forbid);
                        return true;
                    }
                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), EditCardTypeFragment.newInstance(0))
                        .addToBackStack(null)
                        .commit();
                    return true;
                }
            });
        viewpager.setOffscreenPageLimit(3);
        adapter = new CardViewpagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.setupWithViewPager(viewpager);
        refresh();
        return view;
    }

    public int getmCardtype() {
        return mCardtype;
    }

    public void setmCardtype(int mCardtype) {
        this.mCardtype = mCardtype;
        refresh();
    }

    public void refresh() {
        RxRegiste(restRepository.getGet_api()
            .qcGetCardTpls(App.staffId, gymWrapper.getParams(), mCardtype == 0 ? null : Integer.toString(mCardtype),
                mCardDisable == 0 ? "1" : "0")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcResponseData<CardTpls>>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcResponseData<CardTpls> qcResponseCardTpls) {
                    if (qcResponseCardTpls.getStatus() == ResponseConstant.SUCCESS) {
                        List<CardTpl> card_tpls = new ArrayList<>();
                        for (CardTpl card_tpl : qcResponseCardTpls.data.card_tpls) {
                            if (type == 0 || card_tpl.getType() == type) {
                                if (card_tpl.is_limit()) {
                                    StringBuffer ss = new StringBuffer();
                                    if (card_tpl.getPre_times() != 0) {
                                        ss.append("限制: 可提前预约");
                                        ss.append(card_tpl.getPre_times());
                                        ss.append("节课");
                                    }
                                    if (card_tpl.getDay_times() != 0) {
                                        if (card_tpl.getPre_times() != 0) ss.append(",");
                                        ss.append("每天共计可上").append(card_tpl.getDay_times()).append("节课");
                                    } else if (card_tpl.getWeek_times() != 0) {
                                        if (card_tpl.getPre_times() != 0) ss.append(",");
                                        ss.append("每周共计可上").append(card_tpl.getWeek_times()).append("节课");
                                    } else if (card_tpl.getMonth_times() != 0) {
                                        if (card_tpl.getPre_times() != 0) ss.append(",");
                                        ss.append("每月共计可上").append(card_tpl.getMonth_times()).append("节课");
                                    }
                                    if (TextUtils.isEmpty(ss.toString())) {
                                        card_tpl.setLimit("限制: 无");
                                    } else {
                                        card_tpl.setLimit(ss.toString());
                                    }
                                } else {
                                    card_tpl.setLimit("限制: 无");
                                }
                                if (TextUtils.isEmpty(card_tpl.getDescription())) {
                                    card_tpl.setDescription("简介: 无");
                                } else {
                                    card_tpl.setDescription(TextUtils.concat("简介: ", card_tpl.getDescription()).toString());
                                }
                                if (TextUtils.isEmpty(mChooseShopId) || (!TextUtils.isEmpty(mChooseShopId) && card_tpl.getShopIds()
                                    .contains(mChooseShopId))) {
                                    card_tpls.add(card_tpl);
                                }
                            }
                        }
                        RxBus.getBus().post(new RxCardTypeEvent(card_tpls, mCardtype));
                    } else {
                        Timber.e(qcResponseCardTpls.getMsg());
                    }
                }
            }));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 9) {
                mChooseShopName = IntentUtils.getIntentString(data, 0);
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    mCallbackActivity.setToolbar(getString(R.string.all_gyms), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(BrandCardListFragment.this, 9, PermissionServerUtils.CARDSETTING,
                                getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, R.menu.menu_add, new Toolbar.OnMenuItemClickListener() {
                        @Override public boolean onMenuItemClick(MenuItem item) {
                            getFragmentManager().beginTransaction()
                                .replace(mCallbackActivity.getFragId(), EditCardTypeFragment.newInstance(0))
                                .addToBackStack(null)
                                .commit();
                            return true;
                        }
                    });
                } else {
                    mCallbackActivity.setToolbar(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(BrandCardListFragment.this, 9, PermissionServerUtils.CARDSETTING,
                                getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, 0, null);
                }
                refresh();
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return BrandCardListFragment.class.getName();
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
            return BrandCardTypeListFragment.newInstance(position);
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override public int getCount() {
            return 3;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "全部";
                case 1:
                    return "多场馆通卡";
                case 2:
                    return "单场馆卡";
                default:
                    return "全部";
            }
        }
    }
}
