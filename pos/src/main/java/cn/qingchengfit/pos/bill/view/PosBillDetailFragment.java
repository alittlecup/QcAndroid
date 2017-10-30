package cn.qingchengfit.pos.bill.view;

import cn.qingchengfit.pos.RongPrinter;
import cn.qingchengfit.saasbase.bill.items.BillKvCommonItem;
import cn.qingchengfit.saasbase.bill.view.BillDetailFragment;
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
 * Created by Paper on 2017/10/29.
 */

public class PosBillDetailFragment extends BillDetailFragment {
  @Override public void onBtnPrintClicked() {
    RongPrinter.Builder printB = new RongPrinter.Builder();
    for (int i = 0; i < commonAdapter.getItemCount(); i++) {
      IFlexible item = commonAdapter.getItem(i);
      if (item instanceof BillKvCommonItem){
        printB.first(((BillKvCommonItem) item).getKey(),((BillKvCommonItem) item).getValue());
      }
    }
    for (int i = 0; i < extraAdapter.getItemCount(); i++) {
      IFlexible item = extraAdapter.getItem(i);
      if (item instanceof BillKvCommonItem){
        printB.secoud(((BillKvCommonItem) item).getKey(),((BillKvCommonItem) item).getValue());
      }
    }
    try {
      startActivity(printB.build().print(getContext()));
    }catch (Exception e){
      showAlert("打印错误，请检查POS机状态");
    }

  }

}
