//package cn.qingchengfit.saasbase.cards.views;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.model.base.CardTplOption;
//import cn.qingchengfit.saasbase.R;
//
//import cn.qingchengfit.saasbase.cards.bean.Card;
//import cn.qingchengfit.saasbase.cards.item.CardTplOptionForBuy;
//import cn.qingchengfit.saasbase.cards.item.CardtplOptionOhterItem;
//import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
//import cn.qingchengfit.saasbase.cards.presenters.CardChargePresenter;
//import cn.qingchengfit.saasbase.common.views.CommonInputParams;
//import cn.qingchengfit.saascommon.constant.Configs;
//import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
//import cn.qingchengfit.utils.AppUtils;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.widgets.CommonFlexAdapter;
//import cn.qingchengfit.widgets.CommonInputView;
//import cn.qingchengfit.widgets.ExpandedLayout;
//import com.anbillon.flabellum.annotations.Leaf;
//import com.anbillon.flabellum.annotations.Need;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.SelectableAdapter;
//import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import java.util.ArrayList;
//import java.util.List;
//import javax.inject.Inject;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/9/30.
// */
//@Leaf(module = "card", path = "/charge/") public class CardChargeFragment extends BaseFragment
//  implements FlexibleAdapter.OnItemClickListener, CardChargePresenter.MVPView {
//
//  @BindView(R2.id.rv) RecyclerView rv;
//  @BindView(R2.id.civ_charge_money) CommonInputView civChargeMoney;
//  @BindView(R2.id.civ_real_money) CommonInputView civRealMoney;
//  @BindView(R2.id.civ_start_time) CommonInputView civStartTime;
//  @BindView(R2.id.civ_end_time) CommonInputView civEndTime;
//  @BindView(R2.id.el_need_valid) ExpandedLayout elNeedValid;
//  @BindView(R2.id.lo_input_money) LinearLayout loInputMoney;
//  @BindView(R2.id.civ_saler) CommonInputView civSaler;
//  @BindView(R2.id.civ_mark) CommonInputView civMark;
//  @BindView(R2.id.tv_money_label) TextView tvMoneyLabel;
//  @BindView(R2.id.tv_pay_money) TextView tvPayMoney;
//  @BindView(R2.id.toolbar) Toolbar toolbar;
//  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
//  @BindView(R2.id.btn_pay) Button btnPay;
//  @Need public Card card;
//
//  public static CardChargeFragment newInstance(Card card) {
//    Bundle args = new Bundle();
//    args.putParcelable("card", card);
//    CardChargeFragment fragment = new CardChargeFragment();
//    fragment.setArguments(args);
//    return fragment;
//  }
//
//  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    card = getArguments().getParcelable("card");
//    initBackBus();
//  }
//
//  private void initBackBus() {
//  }
//
//  private CommonFlexAdapter commonFlexAdapter;
//  @Inject CardChargePresenter presenter;
//
//  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
//    Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.frag_card_order_content, container, false);
//    unbinder = ButterKnife.bind(this, view);
//    initToolbar(toolbar);
//    delegatePresenter(presenter, this);
//    presenter.setmCard(card);
//    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
//    commonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
//    GridLayoutManager manager =
//      new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_item_count));
//    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//      @Override public int getSpanSize(int position) {
//        return 1;
//      }
//    });
//    rv.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(6)
//      .withBottomEdge(true)
//      .withRightEdge(true));
//    rv.setLayoutManager(manager);
//    rv.setAdapter(commonFlexAdapter);
//    civRealMoney.addTextWatcher(new TextWatcher() {
//      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//      }
//
//      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//        setPayMoney(s.toString());
//      }
//
//      @Override public void afterTextChanged(Editable s) {
//
//      }
//    });
//    return view;
//  }
//
//  @Override public void initToolbar(@NonNull Toolbar toolbar) {
//    super.initToolbar(toolbar);
//    toolbarTitle.setText("续卡");
//  }
//
//  @Override protected void onFinishAnimation() {
//    super.onFinishAnimation();
//    presenter.queryOption();
//  }
//
//  @Override public boolean onItemClick(int position) {
//    presenter.selectOption(position);
//    commonFlexAdapter.toggleSelection(position);
//    commonFlexAdapter.notifyDataSetChanged();
//    return true;
//  }
//
//  @Override public String getFragmentName() {
//    return CardChargeFragment.class.getName();
//  }
//
//  @Override public void onDestroyView() {
//    super.onDestroyView();
//  }
//
//  @OnClick(R2.id.civ_saler) public void onCivSalerClicked() {
//    routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
//  }
//
//  @OnClick(R2.id.civ_start_time) public void onCivStartTimeClicked() {
//    chooseTime(civStartTime);
//  }
//
//  @OnClick(R2.id.civ_end_time) public void onCivEndTimeClicked() {
//    chooseTime(civEndTime);
//  }
//
//  @OnClick(R2.id.civ_mark) public void onCivMarkClicked() {
//    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
//      new CommonInputParams().title("续卡备注").build());
//  }
//
//  @OnClick(R2.id.btn_pay) public void clickPayMoney() {
//    presenter.chargeCard();
//  }
//
//  @Override public void onGetOptions(List<CardTplOption> options) {
//    List<AbstractFlexibleItem> items = new ArrayList<>();
//    for (CardTplOption option : options) {
//      items.add(new CardTplOptionForBuy(option, card.getType()));
//    }
//    // TODO: 2017/9/30 判断权限
//    items.add(new CardtplOptionOhterItem());
//    commonFlexAdapter.clear();
//    commonFlexAdapter.updateDataSet(items);
//    onItemClick(0);
//  }
//
//  @Override public void showInputMoney(boolean other, int cardCate, boolean hasValid) {
//    if (cardCate == Configs.CATEGORY_DATE) {//期限卡
//      loInputMoney.setVisibility(View.VISIBLE);
//      elNeedValid.hideHeader(true);
//      civEndTime.setVisibility(other ? View.VISIBLE : View.GONE);
//      civChargeMoney.setVisibility(View.GONE);
//      civRealMoney.setVisibility(View.GONE);
//      elNeedValid.resizeContent(
//        (int) getResources().getDimension(R.dimen.qc_item_height) * (other ? 2 : 1) + 1);
//      civRealMoney.setVisibility(other?View.VISIBLE:View.GONE);
//    } else {//储值卡 或者次卡
//      if (!other && hasValid) {
//        loInputMoney.setVisibility(View.VISIBLE);
//        elNeedValid.hideHeader(true);
//        civEndTime.setVisibility(View.GONE);
//        civChargeMoney.setVisibility(View.GONE);
//        civRealMoney.setVisibility(View.GONE);
//        elNeedValid.resizeContent(
//          (int) getResources().getDimension(R.dimen.qc_item_height)  + 1);
//      } else {
//        elNeedValid.hideHeader(false);
//        civEndTime.setVisibility(View.VISIBLE);
//        loInputMoney.setVisibility(other ? View.VISIBLE : View.GONE);
//        civChargeMoney.setVisibility(View.VISIBLE);
//        civChargeMoney.setUnit(CardBusinessUtils.getCardTypeCategoryUnit(cardCate, getContext()));
//        civRealMoney.setVisibility(View.VISIBLE);
//        elNeedValid.resizeContent(ViewGroup.LayoutParams.WRAP_CONTENT);
//      }
//    }
//  }
//
//  @Override public void setPayMoney(String m) {
//    tvPayMoney.setText(getString(R.string.pay_money, m));
//  }
//
//  @Override public void bindStudent(String student) {
//  }
//
//  @Override public void bindSaler(String saler) {
//    civSaler.setContent(saler);
//  }
//
//  @Override public void remark(boolean remark) {
//    civMark.setContent(remark ? "已设置" : "未设置");
//  }
//
//  @Override public void onBusinessOrder(PayBusinessResponse payBusinessResponse) {
//
//  }
//
//  @Override public String chargeMoney() {
//    return civChargeMoney.getContent();
//  }
//
//  public String realMoney() {
//    return civRealMoney.getContent();
//  }
//
//  public boolean openValidDay() {
//    return elNeedValid.isExpanded();
//  }
//
//  public String startDay() {
//    return civStartTime.getContent();
//  }
//
//  public String endDay() {
//    return civEndTime.getContent();
//  }
//}
