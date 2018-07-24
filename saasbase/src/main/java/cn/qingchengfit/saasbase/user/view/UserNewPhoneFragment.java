package cn.qingchengfit.saasbase.user.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saasbase.user.bean.FixPhoneBody;
import cn.qingchengfit.saasbase.user.bean.GetCodeBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.anbillon.flabellum.annotations.Leaf;
import com.squareup.phrase.Phrase;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
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
 * Created by Paper on 2018/2/6.
 * 修改手机号字段
 */
@Leaf(module = "user", path = "/new/phone/")
public class UserNewPhoneFragment extends BaseFragment {

  @Inject IUserModel userModel;

  PhoneEditText pet;
  PasswordView pw;
  PasswordView pwOld;
  Button btnDone;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_user_phone, container, false);
    initToolbar(view.findViewById(R.id.toolbar));
    ((TextView)view.findViewById(R.id.toolbar_title)).setText("修改手机号");
    pet = view.findViewById(R.id.pet);
    pw = view.findViewById(R.id.pw_view);
    pwOld = view.findViewById(R.id.pw_old);
    btnDone = view.findViewById(R.id.btn_done);
    btnDone.setOnClickListener(view1 -> changePhone());
    pw.setOnClickListener(view1 -> sendMsg());
    return view;
  }

  /**
   * 发送验证码
   */
  void sendMsg() {
    if (pet.checkPhoneNum()) {
      RxRegiste(userModel.getCode(new GetCodeBody.Builder().area_code(pet.getDistrictInt())
        .phone(pet.getPhoneNum())
        .build()).onBackpressureDrop().flatMap(qcDataResponse -> {
        if (ResponseConstant.checkSuccess(qcDataResponse)) {
          return Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(60);
        } else {
          throw new RuntimeException("发送验证码失败");
        }
      }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
        pw.blockRightClick(aLong < 59);
        pw.setRightText(aLong >= 59 ? "获取验证码"
          : Phrase.from("重新发送({time}s)").put("time", 60 - aLong.intValue()).format().toString());
      }, throwable -> {
        pw.blockRightClick(false);
        onShowError(throwable.getMessage());
      }));
    }
  }

  void changePhone() {
    if (pet.checkPhoneNum() && pw.checkValid() && pwOld.checkValid()) {
      RxRegiste(userModel.newPhone(new FixPhoneBody.Builder()
        .password(pwOld.getCode())
        .phone(pet.getPhoneNum())
        .area_code(pet.getDistrictInt())
        .code(pw.getCode())
        .build())
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  @Override public String getFragmentName() {
    return UserNewPhoneFragment.class.getName();
  }
}
