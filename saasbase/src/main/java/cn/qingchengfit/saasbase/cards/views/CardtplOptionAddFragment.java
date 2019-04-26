package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.presenters.AddCardtplStandardPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
@Leaf(module = "card", path = "/cardtpl/option/add/") public class CardtplOptionAddFragment
    extends SaasBaseFragment implements AddCardtplStandardPresenter.MVPView {

  @Inject AddCardtplStandardPresenter presenter;
	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	CommonInputView civChargeMoney;
	CommonInputView civRealMoney;
	CommonInputView civValidDay;
	ExpandedLayout elValidDay;
	ExpandedLayout elUseCreate;
	ExpandedLayout elUseCharge;
	ExpandedLayout elOnlyStaff;

  @Need public String cardTplId;
  @Need public Integer cardCate;
	CommonInputView civPriceDesc;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtpl_option_add, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    civChargeMoney = (CommonInputView) view.findViewById(R.id.civ_charge_money);
    civRealMoney = (CommonInputView) view.findViewById(R.id.civ_real_money);
    civValidDay = (CommonInputView) view.findViewById(R.id.civ_valid_day);
    elValidDay = (ExpandedLayout) view.findViewById(R.id.el_valid_day);
    elUseCreate = (ExpandedLayout) view.findViewById(R.id.el_use_create);
    elUseCharge = (ExpandedLayout) view.findViewById(R.id.el_use_charge);
    elOnlyStaff = (ExpandedLayout) view.findViewById(R.id.el_only_staff);
    civPriceDesc = (CommonInputView) view.findViewById(R.id.civ_price_desc);
    view.findViewById(R.id.civ_price_desc).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDesc();
      }
    });
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        delOption();
      }
    });

    delegatePresenter(presenter, this);
    presenter.setTplId(cardTplId);
    initToolbar(toolbar);
    initView();
    return view;
  }

  public void initView() {
    civChargeMoney.setUnit(CardBusinessUtils.getCardTypeCategoryUnit(cardCate));
    //期限卡不需要设置有效期
    elValidDay.setVisibility(cardCate == 3 ? View.GONE : View.VISIBLE);
    if (cardCate == 3) {
      elValidDay.setVisibility(View.GONE);
    } else {
      elValidDay.setVisibility(View.VISIBLE);
    }
    elUseCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setCanCharge(isChecked);
      }
    });
    elUseCreate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setCanCreated(isChecked);
      }
    });
    elOnlyStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.canOnlyStaff(isChecked);
      }
    });
    elValidDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.setLimit(isChecked);
      }
    });

    RxBus.getBus()
        .register(EventTxT.class)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventTxT>() {
          @Override public void onNext(EventTxT eventTxT) {
            presenter.setDesc(eventTxT.txt);
            if (civPriceDesc != null) {
              civPriceDesc.setContent("已填写");
            }
          }
        });

  }


  public void onDesc(){
    routeTo("common", "/input/",
        new CommonInputParams().content(presenter.getDesc())
            .title("添加说明")
            .hint("填写说明")
            .build());
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    elUseCharge.setExpanded(true);
    elUseCreate.setExpanded(true);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("添加价格");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("添加").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        //是否开启有效期 未开启直接置位0
        if (elValidDay.isExpanded()) {
          presenter.setLimitDay(civValidDay.getContent());
        } else {
          presenter.setLimitDay("0");
        }

        presenter.setChargeAndReal(civRealMoney.getContent(), civChargeMoney.getContent());
        presenter.addOption();
        return false;
      }
    });
  }

 public void delOption() {
  }

  @Override public String getFragmentName() {
    return CardtplOptionAddFragment.class.getName();
  }

  @Override public void onSaveOk() {
    onShowError("添加成功");
    getActivity().onBackPressed();
  }

  @Override public void onDelOk() {
    onShowError("删除成功");
    getActivity().onBackPressed();
  }
}
