package cn.qingchengfit.saasbase.login.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.login.bean.CheckCodeBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 2018/2/7.
 */
public class RegistInitFragment extends BaseFragment {

  @Inject ILoginModel loginModel;
  RegisteInitFirstFragment first;
  RegisteInitSecoundFragment secound;
  private String phone;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    first = new RegisteInitFirstFragment();
    secound = new RegisteInitSecoundFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_registe_init, container, false);
    first.setOnClickListener((district, phone, pw) -> {
        this.phone = phone;
        checkPhone(new CheckCodeBody.Builder().phone(phone).code(pw).build());
    });
    secound.setListener((name, gender, pw) -> {
      // TODO: 2018/2/8 利用微信号注册新用户
    });
    return view;
  }

  void checkPhone(CheckCodeBody body){
    RxRegiste(loginModel.checkPhoneBind(body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            next();
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    getChildFragmentManager().beginTransaction()
      .replace(R.id.frag,first)
      .commit();
  }

  void next(){
    getChildFragmentManager().beginTransaction()
      .replace(R.id.frag,secound)
      .commit();
  }

  @Override public String getFragmentName() {
    return RegistInitFragment.class.getName();
  }





}
