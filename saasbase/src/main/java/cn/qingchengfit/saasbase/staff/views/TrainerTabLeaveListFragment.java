package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.staff.items.CommonUserItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2018/1/4.
 */

public class TrainerTabLeaveListFragment extends BaseStaffListFragment {

  @Inject IStaffModel staffModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v =  super.onCreateView(inflater, container, savedInstanceState);
    toolbarRoot.setVisibility(View.GONE);
    return v;
  }

  @Override protected void initData() {

  }



  @Override protected void onVisible() {
    if (commonFlexAdapter != null){
      commonFlexAdapter.clear();
      if (ret.size() == 0){
        addEmptyPage();
      }else commonFlexAdapter.updateDataSet(ret,true);
    }
  }

  @Override protected String getTitle() {
    return "";
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CommonUserItem && ((CommonUserItem) item).getUser() instanceof StaffShip){
      StaffShip staffShip = (StaffShip) ((CommonUserItem) item).getUser();
      routeTo("/trainer/leave/",StaffDetailParams.builder().staffShip(staffShip).build());
    }
    return true;
  }
}
