package cn.qingchengfit.saasbase.login.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
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
 * Created by Paper on 2018/2/7.
 */
public class RegisteInitFirstFragment extends BaseFragment {
  PhoneEditText pet;
  PasswordView pw;
  @Inject ILoginModel loginModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_registe_init_first, container, false);
    pet = view.findViewById(R.id.pet);
    pw = view.findViewById(R.id.pw);
    pw.setOnClickListener(view1 -> {
      if (pet.checkPhoneNum() ){
        if (onClickListener != null) onClickListener.onGetCode(pet.getPhoneNum(),pet.getDistrictInt());
      }
    });
    view.findViewById(R.id.btn_comfirm).setOnClickListener(view1 -> {
      if (pet.checkPhoneNum() && pw.checkValid()) {
        if (onClickListener != null)
          onClickListener.onCheckPhoneRegiste(pet.getDistrictInt(),pet.getPhoneNum(),pw.getCode());
      }
    });
    return view;
  }

  public void sendCode(String phone,String code){
    RxRegiste(loginModel.getCode(
      new GetCodeBody.Builder().area_code(code)
        .phone(phone)
        .build()).onBackpressureDrop().flatMap(qcDataResponse -> {
      if (ResponseConstant.checkSuccess(qcDataResponse)) {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
          .take(60);
      } else {
        throw new RuntimeException("发送验证码失败:"+qcDataResponse.getMsg());
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
      pw.blockRightClick(aLong < 60);
      pw.setRightText(aLong > 59 ? "获取验证码"
        : Phrase.from("重新发送({time}s)").put("time", 60 - aLong.intValue()).format().toString());
    }, throwable -> {
      pw.blockRightClick(false);
      onShowError(throwable.getMessage());
    }));
  }


  public onCheckPhoneRegiste onClickListener;

  public void setOnClickListener(onCheckPhoneRegiste onClickListener) {
    this.onClickListener = onClickListener;
  }

  public void setPet(PhoneEditText pet) {
    this.pet = pet;
  }
  public interface onCheckPhoneRegiste{
    void onGetCode(String phone,String disrict);
    void onCheckPhoneRegiste(String district,String phone,String pw);
  }
  @Override public String getFragmentName() {
    return RegisteInitFirstFragment.class.getName();
  }
}
