package cn.qingchengfit.saasbase.user.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saasbase.user.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.user.bean.GetCodeBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
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
 * 验证用户验证码页面
 */
@Leaf(module = "user", path = "/check/code/")
public class UserCaptchaFragment extends BaseFragment {

  @Inject IUserModel userModel;
  @Inject LoginStatus loginStatus;

  Button tvCount;
  CommonInputView civCode;
  Button btnNext;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.f_user_captcha, container, false);
    initToolbar(v.findViewById(R.id.toolbar));
    ((TextView)v.findViewById(R.id.toolbar_title)).setText("修改密码");
    ((TextView)v.findViewById(R.id.tv_phone)).setText(loginStatus.getLoginUser().getPhone());
    tvCount = v.findViewById(R.id.tv_count);
    civCode = v.findViewById(R.id.civ_code);
    tvCount.setOnClickListener(view -> sendMsg());
    btnNext = v.findViewById(R.id.btn_next);
    btnNext.setOnClickListener(view -> doNext());
    civCode.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void afterTextChanged(Editable editable) {
        btnNext.setEnabled(editable.length() >= 6);
      }
    });
    return v;
  }

  /**
   * 发送验证码
   */
  void sendMsg() {
    civCode.setVisibility(View.VISIBLE);
    btnNext.setVisibility(View.VISIBLE);
    RxRegiste(userModel.getCode(
      new GetCodeBody.Builder().area_code(loginStatus.getLoginUser().area_code)
        .phone(loginStatus.getLoginUser().phone)
        .build()).onBackpressureDrop().flatMap(qcDataResponse -> {
      if (ResponseConstant.checkSuccess(qcDataResponse)) {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
          .take(60);
      } else {
        throw new RuntimeException("发送验证码失败");
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
      tvCount.setEnabled(aLong > 59);
      tvCount.setText(aLong >= 59 ? "重新发送"
        : Phrase.from("重新发送({time}s)").put("time", 60 - aLong.intValue()).format().toString());
    }, throwable -> {
      tvCount.setEnabled(true);
      onShowError(throwable.getMessage());
    }));
  }

  //下一步
  void doNext() {
    RxRegiste(userModel.checkCode(
      new CheckCodeBody.Builder().phone(loginStatus.getLoginUser().phone)
        .code(civCode.getContent())
        .area_code(loginStatus.getLoginUser().area_code)
        .build())
      .onBackpressureDrop()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            routeTo("/new/pw/?nostack=true", UserNewPwParams.builder().code(civCode.getContent())
              .phone(loginStatus.getLoginUser().getPhone())
              .build());
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }
}
