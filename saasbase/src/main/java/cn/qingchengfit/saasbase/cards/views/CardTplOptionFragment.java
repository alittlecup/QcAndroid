package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

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
 * Created by Paper on 2017/10/9.
 */
@Leaf(module = "card",path = "/cardtpl/option/")
public class CardTplOptionFragment extends CardtplOptionAddFragment {

  @Need public CardTplOption cardTplOption;

  public static CardTplOptionFragment newInstance(CardTplOption option) {
    Bundle args = new Bundle();
    args.putParcelable("option",option);
    CardTplOptionFragment fragment = new CardTplOptionFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater,container,savedInstanceState);
    if (view != null && view.findViewById(R.id.btn_del) != null)
      view.findViewById(R.id.btn_del).setVisibility(View.VISIBLE);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    if (cardTplOption != null && !CmStringUtils.isEmpty(cardTplOption.id)) {
      presenter.setOptionId(cardTplOption.id);
      if (cardTplOption.card_tpl != null)
        presenter.setTplId(cardTplOption.card_tpl.getId());
    }
    onDetail(cardTplOption);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("编辑会员卡价格");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        presenter.setLimitDay(civValidDay.getContent());
        presenter.setChargeAndReal(civRealMoney.getContent(), civChargeMoney.getContent());
        presenter.saveOption();
        return false;
      }
    });
  }

  /**
   * 展示会员卡价格详情
   */
  public void onDetail(CardTplOption option){
    if (option == null){
      errorRecorder.e("卡价格为空");
      return;
    }

    civChargeMoney.setContent(option.charge);
    civRealMoney.setContent(CmStringUtils.getMoneyStr(option.price) + "元");
    elValidDay.setExpanded(option.limit_days);
    civValidDay.setContent(option.days+"");
    elUseCharge.setExpanded(option.can_charge);
    elUseCreate.setExpanded(option.can_create);
    elOnlyStaff.setExpanded(option.for_staff);
  }

  /**
   * 删除会员卡价格
   */
  @Override public void delOption() {
    presenter.delOption();
  }

  /**
   * 保存成功
   */
  @Override public void onSaveOk() {
    onShowError("保存成功");
    getActivity().onBackPressed();
  }

  ///**
  // * 删除成功
  // */
  //@Override public void OnDelOk() {
  //
  //}
}
