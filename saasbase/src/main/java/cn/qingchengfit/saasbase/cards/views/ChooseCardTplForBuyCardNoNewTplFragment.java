package cn.qingchengfit.saasbase.cards.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.IFlexible;

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
 * Created by Paper on 2017/8/16.
 */
@Leaf(module = "card", path = "/cardtpl/nonew/")
public class ChooseCardTplForBuyCardNoNewTplFragment extends CardTplsHomeInGymFragment {

  @Need QcStudentBean qcStudentBean;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        ChooseCardTplForBuyCardNoNewTplFragment.this.getActivity().onBackPressed();
      }
    });
    if (!CompatUtils.less21()
        && toolbar.getParent() instanceof ViewGroup
        && this.isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
    }
    toolbarTitle.setText("选择会员卡种类");
  }

  /**
   * 点击事件发送消息
   */
  @Override public boolean onItemClick(int i) {
    IFlexible item = fragmentList.get(viewpager.getCurrentItem()).getItem(i);
    if (item instanceof CardTplItem) {
      if (((CardTplItem) item).getCardTpl().is_enable) {
        routeTo("/pay/", new CardBuyParams().cardTpl(((CardTplItem) item).getCardTpl())
            .qcStudentBean(qcStudentBean)
            .build());
      } else {
        showAlert("已停用的会员卡种类不能用于购买会员卡");
      }
    }
    return true;
  }
}
