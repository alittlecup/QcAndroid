package cn.qingchengfit.staffkit.views.card.cardlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.BalanceDetail;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CardItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.card.filter.FilterHeadCommonFragment;
import cn.qingchengfit.staffkit.views.cardtype.CardTypeActivity;
import cn.qingchengfit.staffkit.views.cardtype.ChooseCardTypeActivity;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.EndlessRecyclerOnScrollListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.export.ImportExportActivity;
import cn.qingchengfit.staffkit.views.export.ImportExportFragment;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

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
 * Created by Paper on 16/3/17 2016.
 */
public class RealCardListFragment extends FilterHeadCommonFragment
    implements RealCardListView, RealCardListPresenter.OnSettingBalanceListener, FlexibleAdapter.OnItemClickListener {
    public static final int RESULT_FLOW = 8;

    @BindView(R.id.cardcount_lable) TextView cardcountLable;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
    @BindView(R.id.cards_total) TextView cardsTotal;
    @BindView(R.id.card_list_shadow) View cardListShadow;
    @BindView(R.id.rl_balance_short) LinearLayout rlBalanceShort;
    @BindView(R.id.text_balance_cards) TextView textBalanceNumbers;

    @Inject RealCardListPresenter realCardListPresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject RealcardWrapper realcardWrapper;

    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter adapter;
    private String cardTpl_id, keyword;
    private int status_id;
    private String cardTpl_name;
    private String mChooseShopId;
    private CardTpl card_tpl;//买卡时候选的
    private CoachService mChooseShop; //买卡先选场馆
    private int card_status;
    private int card_tpl_type;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_cardlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(realCardListPresenter, this);
        realcardWrapper.setRealCard(null);
        isBalance = false;
        setView(view);
        filterHeadPresenter.getCardFilterType(false);
        super.onCreateView(inflater, container, savedInstanceState);
        realCardListPresenter.setOnSettingBalanceListener(this);
        if (gymWrapper.inBrand()) {
            mCallbackActivity.setToolbar("全部场馆", true, new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ChooseGymActivity.start(RealCardListFragment.this, 9, PermissionServerUtils.MANAGE_COSTS,
                        getString(R.string.choose_gym), mChooseShopId);
                }
            }, R.menu.menu_search_flow, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_search) {
                        mCallbackActivity.openSeachView("输入学员姓名或者手机号查找会员卡", new Action1<CharSequence>() {
                            @Override public void call(CharSequence charSequence) {
                                ToastUtils.showDefaultStyle(charSequence.toString());
                                keyword = charSequence.toString();
                                realCardListPresenter.initPage();
                                realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, status_id, mChooseShopId);
                            }
                        });
                    } else if (item.getItemId() == R.id.action_flow) {

                        BottomSheetListDialogFragment.start(RealCardListFragment.this, RESULT_FLOW, new String[] { "会员卡种类" });
                    }
                    return true;
                }
            });
        } else {
            mCallbackActivity.setToolbar("会员卡", false, null, R.menu.menu_search_flow, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_search) {
                        mCallbackActivity.openSeachView("输入学员姓名或者手机号查找会员卡", new Action1<CharSequence>() {
                            @Override public void call(CharSequence charSequence) {
                                ToastUtils.showDefaultStyle(charSequence.toString());
                                keyword = charSequence.toString();
                                realCardListPresenter.initPage();
                                realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, status_id, mChooseShopId);
                            }
                        });
                    } else if (item.getItemId() == R.id.action_flow) {
                        BottomSheetListDialogFragment.start(RealCardListFragment.this, RESULT_FLOW, new String[] { "会员卡种类" });
                    }
                    return true;
                }
            });
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(manager);
        adapter = new CommonFlexAdapter(datas, this);
        recycleview.setAdapter(adapter);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                realCardListPresenter.initPage();
                realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, card_status, mChooseShopId);
            }
        });
        recycleview.addScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override public void onLoadMore() {
                Observable.just("").throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        showLoading();
                        realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, card_status, mChooseShopId);
                    }
                });
            }
        });

        realCardListPresenter.filterByCardtype(null, null, 0, 0, mChooseShopId);
        realCardListPresenter.queryBalanceCount();
        return view;
    }

    @OnClick({ R.id.rl_balance_short }) public void onFilter() {
        if (!serPermisAction.check(PermissionServerUtils.CARDBALANCE)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            .replace(R.id.student_frag, new BalanceCardListFragment())
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.layout_card_export) public void onCardExport(){
        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.CARD_EXPORT)) {
            showAlert(R.string.sorry_for_no_permission);
            return;
        }

        Intent i = new Intent(getActivity(), ImportExportActivity.class);
        i.putExtra("type", ImportExportActivity.TYPE_CARD);
        startActivity(i);
    }

    @OnClick(R.id.fab_add_card) public void addCard() {
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }
        if (gymWrapper.inBrand()) {
            MutiChooseGymFragment.start(RealCardListFragment.this, true, null, PermissionServerUtils.MANAGE_COSTS_CAN_WRITE, 10);
        } else {
            Intent toCardType = new Intent(getActivity(), ChooseCardTypeActivity.class);
            startActivityForResult(toCardType, 1);
        }
    }

    @Override public void onDestroyView() {
        realCardListPresenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onFailed(String s) {
        hideLoading();
        recycleview.setFresh(false);
    }

    @Override public void onSuccees(int size, int curPage, List<Card> cards) {
        hideLoading();
        if (curPage == 1) datas.clear();
        recycleview.setFresh(false);
        if (cards == null) return;
        for (int i = 0; i < cards.size(); i++) {
            datas.add(new CardItem(cards.get(i)));
        }
        //datas.addAll(cards);
        adapter.notifyDataSetChanged();
        recycleview.setNoData(datas.isEmpty());
        cardsTotal.setText(String.format(Locale.CHINA, "%d", size));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                card_tpl = data.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
                if (gymWrapper.inBrand() && mChooseShop != null) {
                    Intent toBuyCard = new Intent(getActivity(), BuyCardActivity.class);
                    toBuyCard.putExtra(Configs.EXTRA_CARD_TYPE, card_tpl);
                    startActivityForResult(toBuyCard, 2);
                } else {
                    Intent toBuyCard = new Intent(getActivity(), BuyCardActivity.class);
                    toBuyCard.putExtra(Configs.EXTRA_CARD_TYPE, card_tpl);
                    startActivityForResult(toBuyCard, 2);
                }
            } else if (requestCode == 2) {//创建新卡
                recycleview.setFresh(true);
                realCardListPresenter.initPage();
                realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, status_id, mChooseShopId);
            } else if (requestCode == RESULT_FLOW) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                if (pos == 0) { //会员卡种类
                    if (serPermisAction.check(PermissionServerUtils.CARDSETTING)) {

                        Intent it = new Intent(getActivity(), CardTypeActivity.class);
                        startActivity(it);
                    } else {
                        showAlert(getString(R.string.alert_permission_forbid));
                    }
                } else {//发卡

                }
            } else if (requestCode == 9) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    mCallbackActivity.setToolbar("全部会员卡", true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(RealCardListFragment.this, 9, PermissionServerUtils.MANAGE_COSTS,
                                getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, R.menu.menu_student, new Toolbar.OnMenuItemClickListener() {
                        @Override public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.action_search) {
                                mCallbackActivity.openSeachView("输入学员姓名或者手机号查找会员卡", new Action1<CharSequence>() {
                                    @Override public void call(CharSequence charSequence) {
                                        ToastUtils.showDefaultStyle(charSequence.toString());
                                        keyword = charSequence.toString();
                                        realCardListPresenter.initPage();
                                        realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, status_id,
                                            mChooseShopId);
                                    }
                                });
                            } else if (item.getItemId() == R.id.action_add) {
                                Intent toChoose = new Intent(getActivity(), ChooseCardTypeActivity.class);
                                startActivityForResult(toChoose, 1);
                            }
                            return true;
                        }
                    });
                } else {
                    mCallbackActivity.setToolbar(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            //                            Intent intent = new Intent(getActivity(), ChooseGymActivity.class);
                            //                            IntentUtils.instanceStringsIntent(getString(R.string.choose_gym), mChooseShopId);
                            //                            startActivityForResult(intent, 9);
                            ChooseGymActivity.start(RealCardListFragment.this, 9, PermissionServerUtils.MANAGE_COSTS,
                                getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, R.menu.menu_student, new Toolbar.OnMenuItemClickListener() {
                        @Override public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.action_search) {
                                mCallbackActivity.openSeachView("输入学员姓名或者手机号查找会员卡", new Action1<CharSequence>() {
                                    @Override public void call(CharSequence charSequence) {
                                        ToastUtils.showDefaultStyle(charSequence.toString());
                                        keyword = charSequence.toString();
                                        realCardListPresenter.initPage();
                                        realCardListPresenter.filterByCardtype(keyword, cardTpl_id, card_tpl_type, status_id,
                                            mChooseShopId);
                                    }
                                });
                            } else if (item.getItemId() == R.id.action_add) {
                                Intent toChoose = new Intent(getActivity(), ChooseCardTypeActivity.class);
                                startActivityForResult(toChoose, 1);
                            }
                            return true;
                        }
                    });
                }
                realCardListPresenter.initPage();
                realCardListPresenter.filterByCardtype(keyword, cardTpl_id, this.card_tpl_type, status_id, mChooseShopId);
            } else if (requestCode == 10) {//购卡选场馆

                Shop shop = (Shop) IntentUtils.getParcelable(data);
                mChooseShop =
                    GymBaseInfoAction.getGymByShopIdNow(PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""), shop.id);

                if (mChooseShop != null) {
                    gymWrapper.setCoachService(mChooseShop);
                    Intent toChoose = new Intent(getActivity(), ChooseCardTypeActivity.class);
                    startActivityForResult(toChoose, 1);
                }
            }
        }
    }

    @Override public void showAsDropDown(PopupWindow mPopupWindow, View view) {
        refreshView();
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(view);
            cardListShadow.setVisibility(View.VISIBLE);
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override public void refreshData(String card_tpl_id, int cardtplTpype, int cardStatus) {
        this.card_tpl_type = cardtplTpype;
        card_status = cardStatus;
        this.cardTpl_id = card_tpl_id;
        realCardListPresenter.queryBalanceCount();
        realCardListPresenter.initPage();
        realCardListPresenter.filterByCardtype(keyword, card_tpl_id, card_tpl_type, card_status, null);
    }

    @Override public void dismissPopupWindow() {
        cardListShadow.setVisibility(View.GONE);
    }

    @Override public String getFragmentName() {
        return RealCardListFragment.class.getName();
    }

    @Override public void onSettingSuccess() {

    }

    @Override public void onSettingFailed() {

    }

    @Override public void onGetBalance(List<BalanceDetail> balanceDetailList) {

    }

    @Override public void onGetCardCount(int count) {
        textBalanceNumbers.setText(String.format(Locale.CHINA, "%d", count) + "张");
    }

    @Override public boolean onItemClick(int position) {
        if (datas.get(position) instanceof CardItem) {
            realcardWrapper.setRealCard(((CardItem) datas.get(position)).getRealCard());
            Intent intent = new Intent(getActivity(), CardDetailActivity.class);
            intent.putExtra(Configs.EXTRA_REAL_CARD, ((CardItem) datas.get(position)).getRealCard());
            startActivity(intent);
        }
        return false;
    }
}
