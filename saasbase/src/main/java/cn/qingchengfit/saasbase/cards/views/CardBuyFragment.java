package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardTplOptionForBuy;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionOhterItem;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
@Leaf(module = "card", path = "/pay/") public class CardBuyFragment extends SaasBaseFragment
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
  @BindView(R2.id.civ_start_time) CommonInputView civStartTime;
  @BindView(R2.id.civ_end_time) CommonInputView civEndTime;
  @BindView(R2.id.el_auto_open) ExpandedLayout elAutoOpen;
  @BindView(R2.id.el_need_valid) ExpandedLayout elNeedValid;
  @BindView(R2.id.civ_real_card_num) CommonInputView civRealCardNum;
  @BindView(R2.id.civ_mark) CommonInputView civMark;
  @BindView(R2.id.civ_charge_money) CommonInputView civChargeMoney;
  @BindView(R2.id.civ_real_money) CommonInputView civRealMoney;
  @BindView(R2.id.lo_input_money) LinearLayout loInputMoney;
  @BindView(R2.id.tv_card_append) TextView tvCardAppend;

  @Inject public CardBuyPresenter presenter;
  @Need public CardTpl cardTpl;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private CommonFlexAdapter commonFlexAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    presenter.setmCardTplId(cardTpl.getId());
    presenter.setCardCate(cardTpl.getType());
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    commonFlexAdapter.setMode(SelectableAdapter.MODE_SINGLE);
    GridLayoutManager manager =
      new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_item_count));
    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    rv.setNestedScrollingEnabled(false);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(6)
      .withRightEdge(true)
      .withBottomEdge(true));
    rv.setLayoutManager(manager);
    rv.setAdapter(commonFlexAdapter);
    civRealMoney.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        setPayMoney(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("购卡详情");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.getCardTplDetail();
    presenter.queryOption();
  }

  @Override public int getLayoutRes() {
    return R.id.frag_charge_content;
  }

  @Override public String getFragmentName() {
    return CardBuyFragment.class.getName();
  }

  /**
   * 点击支付
   */
  @OnClick(R2.id.btn_pay) public void onViewClicked() {
    presenter.buyCard();
  }

  /**
   * 选择规格
   */
  @Override public boolean onItemClick(int position) {
    presenter.selectOption(position);
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyDataSetChanged();
    return true;
  }

  @Override public void onGetOptions(List<CardTplOption> options) {
    commonFlexAdapter.clear();
    for (CardTplOption option : options) {
        commonFlexAdapter.addItem(new CardTplOptionForBuy(option, cardTpl.type));
    }
    // TODO: 2017/9/30 判断权限
    commonFlexAdapter.addItem(new CardtplOptionOhterItem());
    onItemClick(0);
  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(cardTpl.getId());
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type, getContext()));
    tvGymName.setText(cardTpl.getShopNames());
    cardview.setBackground(
      DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
    tvCardAppend.setText(cardTpl.getLimit());
  }

  @OnClick(R2.id.civ_bind_menbers) public void onCivBindMenbersClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/student/choose/student/"), null);
  }

  @OnClick(R2.id.civ_saler) public void onCivSalerClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
  }

  @OnClick(R2.id.civ_start_time) public void onCivStartTimeClicked() {
    chooseTime(civStartTime);
  }

  @OnClick(R2.id.civ_end_time) public void onCivEndTimeClicked() {
    chooseTime(civEndTime);
  }

  @OnClick(R2.id.civ_mark) public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
      new CommonInputParams().title("会员卡备注")
        .content(presenter.getRemarks())
        .hint(presenter.getRemarks())
        .build());
  }

  @Override public void showInputMoney(boolean other, int cardCate, boolean validDay) {
    if (cardCate == Configs.CATEGORY_DATE) {//期限卡
      loInputMoney.setVisibility(View.VISIBLE);
      elNeedValid.hideHeader();
      civEndTime.setVisibility(other ? View.VISIBLE : View.GONE);
      civChargeMoney.setVisibility(View.GONE);
      civRealMoney.setVisibility(View.GONE);
      elNeedValid.resizeContent((int)getResources().getDimension(R.dimen.qc_item_height)*(other?3:2)+1);
    } else {//储值卡 或者次卡
      if (!other && validDay) {
        loInputMoney.setVisibility(View.VISIBLE);
        elNeedValid.hideHeader();
        civEndTime.setVisibility(View.GONE);
        civChargeMoney.setVisibility(View.GONE);
        civRealMoney.setVisibility(View.GONE);
        elNeedValid.resizeContent((int)getResources().getDimension(R.dimen.qc_item_height)*2+1);
      } else {
        loInputMoney.setVisibility(other ? View.VISIBLE : View.GONE);
        civChargeMoney.setVisibility(View.VISIBLE);
        civChargeMoney.setUnit(CardBusinessUtils.getCardTypeCategoryUnit(cardCate,getContext()));
        civRealMoney.setVisibility(View.VISIBLE);
      }
    }
  }

  @Override public void bindStudent(String student) {
    civBindMenbers.setContent(student);
  }

  @Override public void bindSaler(String saler) {
    civSaler.setContent(saler);
  }

  @Override public void remark(boolean remark) {
    civMark.setHint(remark ? "已填写" : getString(R.string.please_input));
  }

  @Override public void onBusinessOrder(PayBusinessResponse payBusinessResponse) {

  }

  @Override public void setPayMoney(String s) {
    tvPayMoney.setText(s);
  }

  @Override public String realCardNum() {
    return civRealCardNum.getContent();
  }

  @Override public String realMoney() {
    return civRealMoney.getContent();
  }

  @Override public String chargeMoney() {
    return civChargeMoney.getContent();
  }

  @Override public boolean openValidDay() {
    return elNeedValid.isExpanded();
  }

  @Override public String startDay() {
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
