package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentSendMsgInviteBinding;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxView;
import java.util.concurrent.TimeUnit;
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
 * Created by Paper on 2018/1/1.
 */
@Leaf(module = "staff", path = "/invite/sendmsg/") public class InviteSendMsgFragment
  extends SaasBaseFragment {

  @Need Invitation invitation;
  @Inject IStaffModel staffModel;
  FragmentSendMsgInviteBinding db;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = FragmentSendMsgInviteBinding.inflate(inflater);
    initToolbar(db.layoutToolbar.toolbar);
    db.setToolbarModel(new ToolbarModel.Builder().title("发送邀请短信").build());
    db.setUsername(invitation.getUsername());
    RxView.clicks(db.btn)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          if (db.phone.checkPhoneNum()) {
            RxRegiste(staffModel.inviteBySms(invitation.getUuid(), db.phone.getDistrictInt(),
              db.phone.getPhoneNum())
              .onBackpressureLatest()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new NetSubscribe<QcDataResponse>() {
                @Override public void onNext(QcDataResponse qcResponse) {
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                    ToastUtils.show("短信已发送");
                    popBack();
                  } else {
                    onShowError(qcResponse.getMsg());
                  }
                }
              }));
          }
        }
      }); return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    db.phone.requestFocus();
    AppUtils.showKeyboard(getContext(),db.phone);
  }

  @Override public String getFragmentName() {
    return InviteSendMsgFragment.class.getName();
  }
}
