package cn.qingchengfit.card.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.databinding.CaSimpleCardListBinding;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.listener.ICardListFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import dagger.android.support.HasSupportFragmentInjector;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SimpleCardListFragment extends SaasCommonFragment
    implements ICardListFragment, FlexibleAdapter.OnItemClickListener, CardDetailPresenter.MVPView {
  CaSimpleCardListBinding mBinding;
  CommonFlexAdapter mAdapter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject CardDetailPresenter detailPresenter;

  @Inject ICardModel cardModel;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = DataBindingUtil.inflate(inflater, R.layout.ca_simple_card_list, container, false);
    initRecyclerView();
    delegatePresenter(detailPresenter, this);
    if (!TextUtils.isEmpty(studentID)) {
      onRefresh(studentID);
    }
    return mBinding.getRoot();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof Activity) {
      Application application = ((Activity) context).getApplication();
      if (application instanceof HasSupportFragmentInjector) {
        ((HasSupportFragmentInjector) application).supportFragmentInjector().inject(this);
      }
    }
  }

  private void initRecyclerView() {
    mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recycleView.setAdapter(mAdapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.swipe.setOnRefreshListener(() -> SimpleCardListFragment.this.onRefresh(studentID));
  }

  private void onRefresh(String studentID) {
    if (!TextUtils.isEmpty(studentID)) {
      mBinding.swipe.setRefreshing(true);
      loadStudentCards(studentID);
    }
  }

  private String studentID;

  private void loadStudentCards(String id) {
    RxRegiste(cardModel.qcGetStudentCards(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate(() -> mBinding.swipe.setRefreshing(false))
        .subscribe(new Action1<QcResponseStudentCards>() {
          @Override public void call(QcResponseStudentCards responseStudentCards) {
            if (responseStudentCards.status == 200) {
              if (responseStudentCards.data.cards != null
                  && responseStudentCards.data.cards.size() > 0) {
                updataItems(responseStudentCards.data.cards);
              } else {
                setNoDataItem();
              }
            } else {
              ToastUtils.show(responseStudentCards.msg);
            }
          }
        }, new NetWorkThrowable()));
  }

  private void setNoDataItem() {
    List<CommonNoDataItem> itemList = new ArrayList<>();
    itemList.add(new CommonNoDataItem(null, null, "当前会员名下无会员卡"));
    mAdapter.updateDataSet(itemList);
  }

  private void updataItems(List<Card> cards) {
    mBinding.cardNum.setText("共" + cards.size() + "张会员卡");
    List<CardItem> datas = new ArrayList<>();
    for (int i = 0; i < cards.size(); i++) {
      datas.add(new CardItem(cards.get(i)));
    }
    mAdapter.clear();
    mAdapter.updateDataSet(datas);
  }

  @Override public void setQcStudentId(String id) {
    studentID = id;
    if (mAdapter != null) {
      onRefresh(studentID);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = mAdapter.getItem(position);
    if (item instanceof CardItem) {
      Card realCard = ((CardItem) item).getRealCard();
      if(AppUtils.getCurApp(getContext())==0){
        detailPresenter.setCardId(realCard.getId());
        detailPresenter.queryCardDetail();
      }else{
        routeTo(realCard);
      }
    }
    return false;
  }

  @Override public void onCardDetail(Card card) {
    hideLoading();
    routeTo(card);
  }

  private void routeTo(Card card) {
    Bundle b = new Bundle();
    b.putParcelable("card", card);
    QcRouteUtil.setRouteOptions(
        new RouteOptions("card").setActionName("/charge/").addParam("data", b))
        .callAsync(new IQcRouteCallback() {
          @Override public void onResult(QCResult qcResult) {
            if (qcResult.isSuccess()) {
              getActivity().onBackPressed();
              getActivity().finish();
            }
          }
        });
  }
}
