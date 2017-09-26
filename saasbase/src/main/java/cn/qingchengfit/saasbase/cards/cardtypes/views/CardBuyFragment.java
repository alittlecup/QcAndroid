package cn.qingchengfit.saasbase.cards.cardtypes.views;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/9/26.
 */
public class CardBuyFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, CardBuyPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.tv_card_tpl_type) TextView tvCardTplType;
  @BindView(R2.id.tv_cardtpl_name) TextView tvCardtplName;
  @BindView(R2.id.tv_gym_name) TextView tvGymName;
  @BindView(R2.id.tv_card_id) TextView tvCardId;
  @BindView(R2.id.cardview) RelativeLayout cardview;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.tv_money_label) TextView tvMoneyLabel;
  @BindView(R2.id.tv_pay_money) TextView tvPayMoney;
  @BindView(R2.id.btn_pay) Button btnPay;
  @BindView(R2.id.civ_bind_menbers) CommonInputView civBindMenbers;
  @BindView(R2.id.civ_saler) CommonInputView civSaler;
  @BindView(R2.id.civ_real_card_num) CommonInputView civRealCardNum;
  @BindView(R2.id.civ_start_time) CommonInputView civStartTime;
  @BindView(R2.id.civ_end_time) CommonInputView civEndTime;
  @BindView(R2.id.el_need_valid) ExpandedLayout elNeedValid;
  @BindView(R2.id.el_auto_open) ExpandedLayout elAutoOpen;

  private CommonFlexAdapter commonFlexAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
    unbinder = ButterKnife.bind(this, view);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    commonFlexAdapter.setMode(SelectableAdapter.MODE_SINGLE);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);

    return view;
  }

  @Override public String getFragmentName() {
    return CardBuyFragment.class.getName();
  }

  @OnClick(R2.id.btn_pay) public void onViewClicked() {

  }

  //绑定会员
  @OnClick(R2.id.civ_bind_menbers) public void onCivBindMenbersClicked() {

  }

  //销售业绩归属
  @OnClick(R2.id.civ_saler) public void onCivSalerClicked() {

  }

  //实体卡号
  @OnClick(R2.id.civ_real_card_num) public void onCivRealCardNumClicked() {

  }

  /**
   * 开始日期
   */
  @OnClick(R2.id.civ_start_time) public void onStartTime(){

  }

  /**
   * 结束日期
   */
  @OnClick(R2.id.civ_end_time) public void onEndTime(){

  }


  /**
   * 选择规格
   */
  @Override public boolean onItemClick(int position) {

    return true;
  }

  @Override public void onGetOptions(List<CardTplOption> options) {

  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(cardTpl.getId());
    tvCardTplType.setText(
        CardBusinessUtils.getCardTypeCategoryStr(cardTpl.type, getContext()).substring(0, 1));
    tvGymName.setText(cardTpl.getShopNames());
  }

  @Override public void showInputMoney() {

  }

  @Override public void bindStudent(String student) {
    civBindMenbers.setContent(student);
  }

  @Override public void bindSaler(String saler) {
    civSaler.setContent(saler);
  }

  @Override public String realCardNum() {
    return civRealCardNum.getContent();
  }

  @Override public boolean needValidDay() {
    return elNeedValid.isExpanded();
  }

  @Override public String stardDay() {
    return civStartTime.getContent();
  }

  @Override public String endDay() {
    return civEndTime.getContent();
  }

  @Override public boolean autoOpen() {
    return elAutoOpen.isExpanded();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }


}
