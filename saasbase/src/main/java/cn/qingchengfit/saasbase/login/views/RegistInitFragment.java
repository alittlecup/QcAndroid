package cn.qingchengfit.saasbase.login.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.login.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.login.bean.LoginBody;
import cn.qingchengfit.saasbase.login.bean.RegisteBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.google.gson.JsonObject;
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
  RegisteBody registeBody = new RegisteBody();
  boolean isRegisted = false;

  public static RegistInitFragment newInstance(String openid) {
    Bundle args = new Bundle();
    args.putString("openid", openid);
    RegistInitFragment fragment = new RegistInitFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    first = new RegisteInitFirstFragment();
    secound = new RegisteInitSecoundFragment();
    registeBody.wechat_openid = getArguments().getString("openid");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_registe_init, container, false);
    initToolbar(view.findViewById(R.id.toolbar));
    ((TextView)view.findViewById(R.id.toolbar_title)).setText("请完善信息");
    first.setOnClickListener(new RegisteInitFirstFragment.onCheckPhoneRegiste() {
      @Override public void onGetCode(String phoen, String distric) {
        RxRegiste(loginModel.checkPhoneBind(
          new CheckCodeBody.Builder().wechat_openid(registeBody.wechat_openid)
            .phone(phoen)
            .area_code(distric)
            .build())
          .onBackpressureLatest()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
            @Override public void onNext(QcDataResponse<JsonObject> qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                int x = qcResponse.data.get("status").getAsInt();
                if (x == 0) {//该手机号【未注册】过青橙用户
                  first.sendCode(phoen, distric);
                } else if (x == 1) {//该手机号【已经注册】过青橙用户并且【没有绑定】微信
                  isRegisted = true;
                  first.sendCode(phoen, distric);
                } else {//该用户【已经注册】过青橙用户【已经绑定微信】但是与【当前微信不一致】
                  showAlert("该手机号已绑定其他微信号，不能重复绑定");
                }
              } else {
                onShowError(qcResponse.getMsg());
              }
            }
          }));
      }

      @Override public void onCheckPhoneRegiste(String district, String phone, String pw) {
        registeBody.phone = phone;
        registeBody.area_code = district;
        registeBody.code = pw;
        checkPhone(new CheckCodeBody.Builder().phone(phone)
          .code(pw)
          .wechat_openid(registeBody.wechat_openid)
          .build());
      }
    });
    secound.setListener((name, gender, pw) -> {
      registeBody.username = name;
      registeBody.gender = gender;
      registeBody.setPassword(pw);
      registeBody.has_read_agreement = true;
      registeBody.session_config = true;
      RxBus.getBus().post(registeBody);
    });
    getChildFragmentManager().beginTransaction().replace(R.id.frag, first).commit();
    return view;
  }

  void checkPhone(CheckCodeBody body) {
    RxRegiste(loginModel.checkPhoneBind(body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
        @Override public void onNext(QcDataResponse<JsonObject> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            int x = qcResponse.data.get("status").getAsInt();
            if (x == 0) {//该手机号【未注册】过青橙用户
              next();
            } else if (x == 1) {//该手机号【已经注册】过青橙用户并且【没有绑定】微信 跳去登录
              RxBus.getBus().post(new LoginBody.Builder().area_code(registeBody.area_code)
                .has_read_agreement(true).phone(registeBody.phone).code(registeBody.code)
                .wechat_openid(registeBody.wechat_openid)
                .build());
            } else {//该用户【已经注册】过青橙用户【已经绑定微信】但是与【当前微信不一致】
              showAlert("该用户已绑定其他微信号");
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();

  }

  void next() {
    getChildFragmentManager().beginTransaction().replace(R.id.frag, secound).commit();
  }

  @Override public String getFragmentName() {
    return RegistInitFragment.class.getName();
  }
}
