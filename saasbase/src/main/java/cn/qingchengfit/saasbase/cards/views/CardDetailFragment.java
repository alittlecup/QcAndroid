package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.items.ActionDescItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardActionsItem;
import cn.qingchengfit.saasbase.cards.item.CardDetailFunItem;
import cn.qingchengfit.saasbase.cards.item.CardDetailItem;
import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentParams;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/9/29.
 */
@Leaf(module = "card", path = "/detail/") public class CardDetailFragment extends BaseListFragment
    implements CardDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener,
    CardDetailFunItem.OnClickCardFunListener {
  @Need
  public String cardid;
  @Inject SerPermisAction serPermisAction;

  private String protocolUrl;
  private String alreadyInfo;

  private Card mCard;
  
  Toolbar toolbar;
  TextView toolbarTitle;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Inject CardDetailPresenter presenter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container,container,false);
    parent.addView(v,1);
    toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
    toolbarTitle =(TextView) parent.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    delegatePresenter(presenter,this);
    presenter.setCardId(cardid);
    initListener(this);
    RxBusAdd(EventRecycleClick.class)
        .subscribe(new BusSubscribe<EventRecycleClick>() {
          @Override public void onNext(EventRecycleClick eventRecycleClick) {
            if (eventRecycleClick.viewId == R.id.btn_charge){
              Bundle b = new Bundle();
              b.putParcelable("card",presenter.getmCard());
              routeTo("/charge/",b);
            }
          }
        });
    return parent;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("会员卡详情");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    onRefresh();
  }

  @Override protected void addDivider() {
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey)
            .addItemViewType(R.layout.item_card_action, 20)
            .addItemViewType(R.layout.item_card_detail_fun, 20)
            .addItemViewType(R.layout.item_action_desc,1)
            .withBottomEdge(true));
  }

  @Override public String getFragmentName() {
    return CardDetailFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return 0;
  }

  @Override public String getNoDataStr() {
    return null;
  }

  @Override public void onCardDetail(Card card) {
    mCard = card;
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(new CardDetailItem(card));
    items.add(new CardDetailFunItem(this));
    items.add(new CardActionsItem(card));
    items.add(new ActionDescItem.Builder().action(1)
        .icon(R.drawable.vd_card_member)
        .title("绑定会员")
        .desc(card.getBundleUsers())
        .build());
    items.add(new ActionDescItem.Builder().action(2)
        .icon(R.drawable.vd_card_gyms)
        .title("适用场馆")
        .clickable(false)
        .desc(card.getSupportGyms())
        .build());
    //items.add(new ActionDescItem.Builder().action(3)
    //    .icon(R.drawable.vd_card_bills)
    //    .title("消费记录")
    //    .clickable(false)
    //    .desc("todo")
    //    .build());
    items.add(new ActionDescItem.Builder().action(4)
        .icon(R.drawable.vd_card_no)
        .title("实体卡号")
        .desc(card.getCard_no())
        .build());
    if (card.is_open_service_term) {

      protocolUrl = card.card_tpl_service_term.content_link;
      if (card.card_tpl_service_term.is_read) {
        alreadyInfo = getResources().getString(R.string.card_protocol_user_read_info,
            card.card_tpl_service_term.service_term_version,
            card.card_tpl_service_term.created_at.replace("T", " ")
                + " "
                + card.card_tpl_service_term.created_by.username);
      } else {
        alreadyInfo = "未读";
      }
      items.add(new ActionDescItem.Builder().action(5)
          .icon(R.drawable.ic_card_number)
          .title("会员卡服务协议")
          .desc(alreadyInfo)
          .build());
    }
    clearItems();
    setDatas(items,1);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof ActionDescItem) {
      switch (((ActionDescItem) item).getAction()) {
        case 1://绑定会员
          routeTo(AppUtils.getRouterUri(getContext(),"/student/choose/student/"),new ChooseAndSearchStudentParams()
            .studentIdList((ArrayList<String>) presenter.getmCard().getUserIds())
            .build());
          break;
        case 2://适用场馆
          break;

        case 3://消费记录

          break;

        case 4://实体卡号
          routeTo("common","/input/",new CommonInputParams()
            .title("修改实体卡号")
            .hint(presenter.getmCard().getCard_no())
            .content(presenter.getmCard().getCard_no())
            .build());
          break;
        case 5:
          if (!TextUtils.isEmpty(protocolUrl)){
            CardProtocolActivity.startWeb(protocolUrl, getContext(), false, alreadyInfo);
          }
          break;
      }
    }

    return true;
  }

  @Override public void onRefresh() {
    presenter.queryCardDetail();
  }

  @Override public void onClickSpend() {
    routeTo(AppUtils.getRouterUri(getContext(), "/card/deduction/"),
        new CardRefundParams().card(mCard).build());
  }

  @Override public void onAskOffDay() {
    routeTo(AppUtils.getRouterUri(getContext(), "/card/offday/list"),
        new CardRefundParams().card(mCard).build());
  }

  @Override public void onClickMore() {
    final DialogSheet dialogSheet = new DialogSheet(getContext());
    dialogSheet.addButton(getString(R.string.unregiste_card), new View.OnClickListener() {

      @Override public void onClick(View v) {
        if (serPermisAction.checkNoOne(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        if (mCard.is_active()) {
          new MaterialDialog.Builder(getContext()).title(getString(R.string.unregiste_title))
              .content(getString(R.string.unregiste_text))
              .negativeText(R.string.pickerview_cancel)
              .positiveText(R.string.unregiste_Comfirm)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  presenter.unRegeister();
                  dialog.dismiss();
                }
              })
              .cancelable(true)
              .autoDismiss(true)
              .build()
              .show();
        }else{
          new MaterialDialog.Builder(getContext()).title(getString(R.string.resume_card_title))
              .content(getString(R.string.resume_card))
              .negativeText(R.string.pickerview_cancel)
              .positiveText(R.string.unregiste_Comfirm)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  presenter.resumeCard();
                  dialog.dismiss();
                }
              })
              .cancelable(true)
              .autoDismiss(true)
              .build()
              .show();
        }
      }
    });
    if (mCard.is_active()) {
      dialogSheet.addButton("修改会员卡有效期", new View.OnClickListener() {
        @Override public void onClick(View view) {
          routeTo(AppUtils.getRouterUri(getContext(), "card/modify/validate"), new CardFixValidDayParams().card(mCard).build());
          if (dialogSheet.isShowing()) {
            dialogSheet.dismiss();
          }
        }
      });
    }
    dialogSheet.show();
  }
}
