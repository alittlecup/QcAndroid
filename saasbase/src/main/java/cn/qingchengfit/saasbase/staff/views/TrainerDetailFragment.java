package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import com.anbillon.flabellum.annotations.Leaf;
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
 * Created by Paper on 2018/1/16.
 */
@Leaf(module = "staff", path = "/trainer/detail/") public class TrainerDetailFragment
    extends StaffDetailFragment {
  @Inject IStaffModel staffModel;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    db.position.setVisibility(View.GONE);
    return v;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.setToolbarModel(
        new ToolbarModel.Builder().title("教练详情").menu(R.menu.menu_save).listener(item -> {
          ManagerBody body = presenter.getBody();
          body.setArea_code(getAreaCode());
          body.setPhone(getPhone());
          body.setUsername(getName());
          boolean b = db.phoneNum.checkPhoneNum();
          if (!b) {
            return false;
          }
          RxRegiste(staffModel.editTrainer(staffShip.id, body)
              .onBackpressureLatest()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new NetSubscribe<QcDataResponse>() {
                @Override public void onNext(QcDataResponse qcResponse) {
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                    onShowError("保存成功");
                    popBack();
                  } else {
                    onShowError(qcResponse.getMsg());
                  }
                }
              }));
          return true;
        }).build());
  }

  @Override public void onBtnDelClicked() {
    if (!serPermisAction.check(PermissionServerUtils.COACHSETTING_CAN_DELETE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    DialogUtils.instanceDelDialog(getActivity(),
        String.format("确定将%s设为离职吗？", staffShip.getUsername()),
        "离职后\n" + "1.该用户不能登录到当前场馆后台。\n" + "2.跟该教练相关的过往业务数据将被保留。\n" + "3.可以由健身房工作人员复职",
        (dialog, which) -> {
          showLoading();
          RxRegiste(staffModel.delTrainer(staffShip.getId())
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new NetSubscribe<QcDataResponse>() {
                @Override public void onNext(QcDataResponse qcDataResponse) {
                  if (ResponseConstant.checkSuccess(qcDataResponse)) {
                    onShowError("离职成功");
                    popBack();
                  } else {
                    onShowError(qcDataResponse.getMsg());
                  }
                }
              }));
        }).show();
  }
}
