package cn.qingchengfit.staffkit.card.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import cn.qingchengfit.saasbase.cards.views.NewCardChargeParams;
import cn.qingchengfit.staffkit.R;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;

/**
 * 2018/3/2 web跳转native进行会员卡充值，1.权限的问题 2.web给的是Id
 * 1. 权限问题由web端来做判断
 * 2. 先通过Id load card data,然后进行跳转
 * Created by huangbaole on 2018/3/2.
 */

@Leaf(module = "card", path = "/charge_card/")

public class WebCardChargeFragment extends SaasBaseFragment implements CardDetailPresenter.MVPView {
  @Inject CardDetailPresenter presenter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter, this);
    String card_id = getActivity().getIntent().getData().getQueryParameter("card_id");
    presenter.setCardId(card_id);
    presenter.queryCardDetail();
    showLoadingTrans();
    return inflater.inflate(R.layout.fragment_web_charge_card,container,false);
  }

  @Override public void onCardDetail(Card card) {
    hideLoadingTrans();
    String web_action = getActivity().getIntent().getStringExtra("web_action");
    WebChargeFragment webChargeFragment=new WebChargeFragment();
    if(!TextUtils.isEmpty(web_action)){
      webChargeFragment.setWebAction(web_action);
    }
    webChargeFragment.setArguments(new NewCardChargeParams().card(card).build());
    stuff(R.id.fragment_container,webChargeFragment);
  }

  @Override public void onShowError(String e) {
    super.onShowError(e);
    getActivity().finish();
  }

}

