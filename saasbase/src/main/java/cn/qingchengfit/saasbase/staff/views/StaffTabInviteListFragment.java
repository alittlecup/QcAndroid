package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.items.CommonUserItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.DialogSheet;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2018/1/4.
 */

public class StaffTabInviteListFragment extends BaseStaffListFragment {

  @Inject IStaffModel staffModel;
  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    toolbarRoot.setVisibility(View.GONE);
    return v;
  }

  @Override protected void initData() {

  }



  @Override public int getFbIcon() {
    return R.drawable.ic_add_staff;
  }

  @Override public void onClickFab() {
    if (!permissionModel.check(PermissionServerUtils.MANAGE_STAFF_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo("/add/",null);
  }

  @Override protected String getTitle() {
    return null;
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CommonUserItem && ((CommonUserItem) item).getUser() instanceof Invitation){
      Invitation invitation = (Invitation) ((CommonUserItem) item).getUser();
      if (invitation.getStatus() ==2 || invitation.getStatus() == 3)
        return true;
      DialogSheet.builder(getContext())
        .addButton("撤销邀请", R.color.red,view -> {
          RxRegiste(staffModel.cancelInvite(invitation.getId())
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetSubscribe<QcDataResponse>() {
              @Override public void onNext(QcDataResponse qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                  invitation.setStatus(3);
                  ((CommonUserItem) item).setUser(invitation);
                  commonFlexAdapter.notifyItemChanged(position);
                } else {
                  onShowError(qcResponse.getMsg());
                }
              }
            }));
        })
        .addButton("重新发送邀请",R.color.text_dark,view -> {
          routeTo("/reinvite/",StaffReInviteParams.builder().invitation(invitation).build());
        })
        .show();
    }
    return true;
  }
}
