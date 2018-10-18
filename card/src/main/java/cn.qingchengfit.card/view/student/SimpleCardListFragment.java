package cn.qingchengfit.card.view.student;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.databinding.CaSimpleCardListBinding;
import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.listener.ICardListFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import dagger.android.support.HasSupportFragmentInjector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SimpleCardListFragment extends SaasCommonFragment implements ICardListFragment {
  CaSimpleCardListBinding mBinding;
  CommonFlexAdapter mAdapter;
  @Inject QcRestRepository restRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = DataBindingUtil.inflate(inflater, R.layout.ca_simple_card_list, container, false);
    initRecyclerView();
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
    mBinding.swipe.setOnRefreshListener(SimpleCardListFragment.this::onRefresh);
  }

  private void onRefresh() {
    if (!TextUtils.isEmpty(studentID)) {
      loadStudentCards(studentID);
    }
  }

  private String studentID;

  private void loadStudentCards(String id) {
    RxRegiste(restRepository.createGetApi(CardApi.class)
        .qcGetStudentCards(loginStatus.staff_id(), id, gymWrapper.id(), gymWrapper.model(),
            gymWrapper.brand_id())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate(() -> mBinding.swipe.setRefreshing(false))
        .subscribe(new Action1<QcResponseStudentCards>() {
          @Override public void call(QcResponseStudentCards responseStudentCards) {
            if (responseStudentCards.status == 200) {
              if (responseStudentCards.data.cards != null
                  && responseStudentCards.data.cards.size() > 0) {
                updataItems(responseStudentCards.data.cards);
              }
            } else {
              ToastUtils.show(responseStudentCards.msg);
            }
          }
        }, new NetWorkThrowable()));
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
    this.studentID = id;
    if (isAdded()) {
      onRefresh();
    }
  }
}
