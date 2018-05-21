package cn.qingchengfit.staffkit.views.student.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.views.CardDetailParams;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/3/19 2016.
 */
public class StudentsCardsFragment extends BaseFragment implements StudentsCardsView, TitleFragment, FlexibleAdapter.OnItemClickListener {

	TextView cardnum;
	RecycleViewWithNoImg recycleview;

    @Inject StudentsCardsPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;

    List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter adatper;
    private CardTpl mChooseCardtpl;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students_cars, container, false);
      cardnum = (TextView) view.findViewById(R.id.cardnum);
      recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recycleview);

      delegatePresenter(presenter, this);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        adatper = new CommonFlexAdapter(datas, this);
        recycleview.setAdapter(adatper);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querData();
            }
        });
        if (serPermisAction.checkAtLeastOne(PermissionServerUtils.CARDSETTING)) {
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
        cardnum.setText(cards.size() + "张会员卡");
        datas.clear();
        if (cards == null) return;
        for (int i = 0; i < cards.size(); i++) {
            datas.add(new CardItem(cards.get(i)));
        }
        adatper.clear();
        adatper.updateDataSet(datas);
        recycleview.setNoData(datas.size() == 0);
    }

    @Override public String getTitle() {
        return "会员卡";
    }

    @Override public String getFragmentName() {
        return StudentsCardsFragment.class.getName();
    }

    //@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //    if (resultCode == android.app.Activity.RESULT_OK) {
    //        if (requestCode == 10) {
    //            mChooseCardtpl = (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
    //            //                presenter.buyCard(getContext(), (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE), StudentsCardsFragment.this);
    //            if (gymWrapper.inBrand()) {
    //                if (mChooseCardtpl.getShopIds().size() > 1) {
    //                    MutiChooseGymFragment.start(StudentsCardsFragment.this, true, null, 9);
    //                } else if (mChooseCardtpl.getShopIds().size() == 1) {
    //                    CoachService gym = gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), mChooseCardtpl.getShopIds().get(0));
    //                    Intent it = new Intent(context, BuyCardActivity.class);
    //                    it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
    //                    context.startActivity(it);
    //                }
    //            } else {
    //                Intent it = new Intent(context, BuyCardActivity.class);
    //                it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
    //                context.startActivity(it);
    //            }
    //        }
    //    } else if (requestCode == 9) {
    //        Shop shop = data.getParcelableExtra(IntentUtils.RESULT);
    //        CoachService gym =
    //            gymBaseInfoAction.getGymByShopIdNow(PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""), shop.id);
    //        Intent it = new Intent(context, BuyCardActivity.class);
    //        it.putExtra(Configs.EXTRA_GYM_SERVICE, gym);
    //
    //        it.putExtra(Configs.EXTRA_CARD_TYPE, mChooseCardtpl);
    //        context.startActivity(it);
    //    }
    //}

    @Override public boolean onItemClick(int i) {
        if (adatper.getItem(i) instanceof CardItem) {
            Card realCard = ((CardItem) adatper.getItem(i)).getRealCard();
            if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS)) {
                showAlert(R.string.alert_permission_forbid);
                return true;
            }
            routeTo("card","/detail/", CardDetailParams.builder().cardid(realCard.getId()).build());
        }

        return false;
    }
}
