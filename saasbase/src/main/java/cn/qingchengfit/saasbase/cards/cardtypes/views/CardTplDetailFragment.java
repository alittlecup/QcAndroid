package cn.qingchengfit.saasbase.cards.cardtypes.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasRouter;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTplStandard;
import cn.qingchengfit.saasbase.cards.cardtypes.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.cardtypes.item.CardtplStandardItem;
import cn.qingchengfit.saasbase.cards.cardtypes.presenters.CardTplDetailPresenter;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.ConnerTag;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * Created by Paper on 2017/8/23.
 */
public class CardTplDetailFragment extends BaseFragment
    implements CardTplDetailPresenter.MVPView ,FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.cardname) TextView cardname;
  @BindView(R2.id.cardid) TextView cardid;
  @BindView(R2.id.support_gyms) TextView supportGyms;
  @BindView(R2.id.limit) TextView limit;
  @BindView(R2.id.intro) TextView intro;
  @BindView(R2.id.type) TextView type;
  @BindView(R2.id.card_bg) LinearLayout cardBg;
  @BindView(R2.id.img_stutus) ConnerTag imgStutus;
  @BindView(R2.id.cardview) CardView cardview;
  @BindView(R2.id.recycleview) RecyclerView recycleview;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;

  @Inject CardTplDetailPresenter presenter;
  @Inject SaasRouter saasRouter;

  CommonFlexAdapter comonAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtpl_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    comonAdapter = new CommonFlexAdapter(new ArrayList(),this);
    recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recycleview.setAdapter(comonAdapter);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("会员卡种类详情");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showBottomList();
        return false;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryCardtpl();
    presenter.queryCardtplOption();
  }

  @Override public String getFragmentName() {
    return CardTplDetailFragment.class.getName();
  }

  /**
   * 展示底部选择框
   */
  protected void showBottomList() {
    DialogList.builder(getContext())
        .list(getResources().getStringArray(
            presenter.isCardTplEnable() ? R.array.card_tpl_flow : R.array.card_tpl_flow_resume),
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//编辑
                  presenter.editCardTpl();
                } else if (position == 1) {
                  if (presenter.isCardTplEnable()) {
                    alertDisableCardtpl();
                  } else {
                    alertEnableCardtpl();
                  }
                }
              }
            })
        .show();
  }

  public void alertDisableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认停用",
        "1.停用后，该会员卡种类无法用于开卡\n2.停用后，该会员卡种类无法用于排课支付\n3.不影响已发行会员卡的使用和显示",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.disable();
          }
        }).show();
  }

  public void alertEnableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认恢复",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.enable();
          }
        }).show();
  }

  /**
   * 获取会员卡基本信息
   */
  @Override public void onGetCardTypeInfo(CardTpl card_tpl) {
    imgStutus.setText("已停卡");
    imgStutus.setVisibility(card_tpl.is_enable ? View.GONE : View.VISIBLE);
    cardname.setText(card_tpl.getName());
    cardid.setText("ID:" + card_tpl.getId());
    cardview.setCardBackgroundColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
    limit.setText(card_tpl.getLimit());
    intro.setText(card_tpl.getDescription());
    supportGyms.setText("适用于: " + card_tpl.getShopNames());
    type.setText(CardBusinessUtils.getCardTypeCategoryStr(card_tpl.getType(),getContext()));
  }

  @Override public void onGetStandards(List<CardTplStandard> cardStandards) {
    if (cardStandards != null){
      comonAdapter.clear();
      comonAdapter.setStatus(presenter.isCardTplEnable()?0:1);
      for (CardTplStandard cardStandard : cardStandards) {
        comonAdapter.addItem(new CardtplStandardItem(cardStandard));
      }
      if (presenter.isCardTplEnable()){
        comonAdapter.addItem(new AddCardtplStantardItem());
      }
    }
  }


  @Override public void onDelSucceess() {
    onFinishAnimation();
    hideLoading();
    ToastUtils.show("已停卡");

  }



  @Override public void onResumeOk() {
    onFinishAnimation();
    hideLoading();
    ToastUtils.show("已恢复");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = comonAdapter.getItem(i);
    if (item instanceof CardtplStandardItem){
      saasRouter.routerTo("/cardtpl/standard/?id="+((CardtplStandardItem) item).getStandard().getId());
    }else if (item instanceof AddCardtplStantardItem){
      saasRouter.routerTo("/cardtpl/standard/add/?cardtplid="+presenter.getCardtplId());
    }
    return true;
  }
}
