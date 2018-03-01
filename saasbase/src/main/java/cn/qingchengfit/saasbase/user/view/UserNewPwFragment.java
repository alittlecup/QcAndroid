package cn.qingchengfit.saasbase.user.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saasbase.user.bean.ModifyPwBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
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
 * Created by Paper on 2018/2/6.
 * 修改用户密码页面
 */
@Leaf(module = "user", path = "/new/pw/") public class UserNewPwFragment extends BaseFragment {

  @Inject IUserModel userModel;

  CommonInputView civOld;
  CommonInputView civNew;
  Button btn;

  @Need String code;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_user_pw, container, false);
    initToolbar(view.findViewById(R.id.toolbar));
    ((TextView)view.findViewById(R.id.toolbar_title)).setText("修改登录密码");
    civNew = view.findViewById(R.id.civ_pw_new);
    civOld = view.findViewById(R.id.civ_pw_old);
    btn = view.findViewById(R.id.btn_done);
    civOld.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    civNew.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    civOld.addTextWatcher(tw);
    civNew.addTextWatcher(tw);
    btn.setOnClickListener(view1 -> onChangePw());
    return view;
  }

  void onChangePw() {
    RxRegiste(userModel.newPw(new ModifyPwBody.Builder()
      .code(code)
      .password(civNew.getContent()).build())
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            popBack();
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));

  }

  private TextWatcher tw = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override public void afterTextChanged(Editable editable) {
      btn.setEnabled(civNew.getContent().equalsIgnoreCase(civOld.getCivContent()));
    }
  };

  @Override public String getFragmentName() {
    return UserNewPwFragment.class.getName();
  }
}
