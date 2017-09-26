package cn.qingchengfit.saasbase.cards.cardtypes.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.presenters.AddCardtplStandardPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.CommonInputTextFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;
import rx.functions.Action1;

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
@Leaf(module = "Card", path = "/cardtpl/standard/add/" )
public class CardtplOptionAddFragment extends BaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.civ_charge) CommonInputView civCharge;
  @BindView(R2.id.income) CommonInputView income;
  @BindView(R2.id.validdate) CommonInputView validdate;
  @BindView(R2.id.for_staff) ExpandedLayout forStaff;
  @BindView(R2.id.support_new_buy) ExpandedLayout supportNewBuy;
  @BindView(R2.id.support_charge) ExpandedLayout supportCharge;
  @BindView(R2.id.el_limit) ExpandedLayout elLimit;
  @BindView(R2.id.civ_desc) CommonInputView civDesc;
  @BindView(R2.id.btn_del) TextView btnDel;

  @Inject AddCardtplStandardPresenter presenter;
  //@Inject SaasRouter saasRouter;


  public static CardtplOptionAddFragment newInstance(int type,String cardtplid) {
    Bundle args = new Bundle();
    args.putInt("type",type);
    args.putString("id",cardtplid);
    CardtplOptionAddFragment fragment = new CardtplOptionAddFragment();
    fragment.setArguments(args);
    return fragment;
  }


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_card_standard, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    presenter.setCardtplId(getArguments().getString("id"));
    presenter.setCardtplType(getArguments().getInt("type"));
    initView();
    return view;
  }


  public void initView(){
    RxBusAdd(EventTxT.class)
        .onBackpressureLatest()
        .subscribe(new Action1<EventTxT>() {
          @Override public void call(EventTxT eventTxT) {
            presenter.setDesc(eventTxT.txt);
            civDesc.setContent("已填写");
          }
        });

    supportCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setCanCharge(isChecked);
      }
    });
    supportNewBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setCanCreated(isChecked);
      }
    });
    forStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.canOnlyStaff(isChecked);
      }
    });
    elLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setLimit(isChecked);
      }
    });

  }


  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("添加规格");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("添加").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        presenter.setLimitDay(validdate.getContent());
        presenter.setChargeAndReal(income.getContent(),civCharge.getContent());
        presenter.addOption();
        return false;
      }
    });
  }

  @Override public String getFragmentName() {
    return CardtplOptionAddFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * {@link  CommonInputTextFragment}
   */
  @OnClick(R2.id.civ_desc) public void onCivDescClicked() {
    //saasRouter.routerTo("/common/inputtext/?t=规格描述&hint=请填写规格描述&c="+presenter.getDesc());
  }

}
