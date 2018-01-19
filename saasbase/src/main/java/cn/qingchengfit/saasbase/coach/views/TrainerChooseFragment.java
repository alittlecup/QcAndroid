package cn.qingchengfit.saasbase.coach.views;

import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.coach.event.EventStaffWrap;
import cn.qingchengfit.saasbase.staff.items.StaffSelectSingleItem;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/11/30.
 */
@Leaf(module = "staff", path = "/trainer/choose/")
public class TrainerChooseFragment extends CoachListFragment {

  @Need public String selectedId;

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof StaffSelectSingleItem){
      RxBus.getBus().post(new EventStaffWrap.Builder()
        .staff(((StaffSelectSingleItem) item).getStaff())
        .type(EventStaffWrap.TRAINER)
        .build());
    }
    popBack();
    return true;
  }

  @Override public void setDatas(List<? extends IFlexible> ds, int page) {
    if (TextUtils.isEmpty(selectedId)){
      commonFlexAdapter.updateDataSet(ds,true);
    }else {
      commonFlexAdapter.updateDataSet(ds,false);
    }
  }

  @Override public void onUpdateEmptyView(int size) {
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof StaffSelectSingleItem){
        if (TextUtils.equals(selectedId,((StaffSelectSingleItem) item).getStaff().id)){
          commonFlexAdapter.addSelection(i);
          commonFlexAdapter.notifyItemChanged(i);
          break;
        }
      }
    }
  }

}
