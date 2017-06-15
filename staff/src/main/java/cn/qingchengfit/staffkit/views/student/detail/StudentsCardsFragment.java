package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CardItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static cn.qingchengfit.staffkit.App.context;

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
 * Created by Paper on 16/3/19 2016.
 */
public class StudentsCardsFragment extends BaseFragment implements StudentsCardsView, TitleFragment, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.cardnum) TextView cardnum;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;

    @Inject StudentsCardsPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter adatper;
    private CardTpl mChooseCardtpl;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students_cars, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        adatper = new CommonFlexAdapter(datas, this);
        recycleview.setAdapter(adatper);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querData();
            }
        });
        if (SerPermisAction.checkAtLeastOne(PermissionServerUtils.CARDSETTING)) {
            presenter.querData();
        } else {
            recycleview.setNoDataImgRes(R.drawable.ic_no_permission);
            recycleview.setNodataHint(getString(R.string.alert_permission_forbid));
            recycleview.setNoData(true);
        }

        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onData(int count, List<Card> cards) {
        cardnum.setText(count + "张会员卡");
        datas.clear();
        if (cards == null) return;
        for (int i = 0; i < cards.size(); i++) {
            datas.add(new CardItem(cards.get(i)));
        }
        adatper.notifyDataSetChanged();
        recycleview.setNoData(datas.size() == 0);
    }

    @Override public String getTitle() {
        return "会员卡";
    }

    @Override public String getFragmentName() {
        return StudentsCardsFragment.class.getName();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK) {
            if (requestCode == 10) {
                mChooseCardtpl = (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
                //                presenter.buyCard(getContext(), (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE), StudentsCardsFragment.this);
                if (gymWrapper.inBrand()) {
                    if (mChooseCardtpl.getShopIds().size() > 1) {
                        MutiChooseGymFragment.start(StudentsCardsFragment.this, true, null, 9);
                    } else if (mChooseCardtpl.getShopIds().size() == 1) {
                        CoachService gym = GymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), mChooseCardtpl.getShopIds().get(0));
                        Intent it = new Intent(context, BuyCardActivity.class);

                        it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
                        context.startActivity(it);
                    }
                } else {
                    Intent it = new Intent(context, BuyCardActivity.class);
                    it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
                    context.startActivity(it);
                }
            }
        } else if (requestCode == 9) {
            Shop shop = data.getParcelableExtra(IntentUtils.RESULT);
            CoachService gym =
                GymBaseInfoAction.getGymByShopIdNow(PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""), shop.id);
            Intent it = new Intent(context, BuyCardActivity.class);
            it.putExtra(Configs.EXTRA_GYM_SERVICE, gym);

            it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
            context.startActivity(it);
        }
    }

    @Override public boolean onItemClick(int i) {
        if (adatper.getItem(i) instanceof CardItem) {
            Card realCard = ((CardItem) adatper.getItem(i)).getRealCard();
            if (SerPermisAction.checkNoOne(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                showAlert(R.string.alert_permission_forbid);
                return true;
            }
            Intent it = new Intent(getActivity(), CardDetailActivity.class);
            it.putExtra(Configs.EXTRA_REAL_CARD, realCard);
            startActivity(it);
        }

        return false;
    }
}
