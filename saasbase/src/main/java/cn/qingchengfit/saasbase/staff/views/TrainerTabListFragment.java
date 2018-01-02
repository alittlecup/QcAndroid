package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
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

public class TrainerTabListFragment extends BaseStaffListFragment {

  @Inject IStaffModel staffModel;
  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v =  super.onCreateView(inflater, container, savedInstanceState);
    toolbarRoot.setVisibility(View.GONE);
    return v;
  }

  @Override protected void initData() {

  }

  @Override public int getFbIcon() {
    return R.drawable.ic_add_staff;
  }

  @Override public void onClickFab() {
    if (!permissionModel.check(PermissionServerUtils.COACHSETTING_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo("/trainer/add/",null);
  }

  @Override protected String getTitle() {
    return null;
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CommonUserItem && ((CommonUserItem) item).getUser() instanceof StaffShip){
      StaffShip staffShip = (StaffShip) ((CommonUserItem) item).getUser();
      routeTo("/trainer/detail/",TrainerDetailParams.builder().staffShip(staffShip).build());
    }
    return true;
  }
}
