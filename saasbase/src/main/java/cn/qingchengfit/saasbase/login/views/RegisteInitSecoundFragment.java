package cn.qingchengfit.saasbase.login.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.PasswordView;

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
public class RegisteInitSecoundFragment extends BaseFragment {

  PasswordView pw;
  CommonInputView civName;
  CommonInputView civGender;
  private int gender = 0;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.f_registe_init_secound, container, false);
    pw = view.findViewById(R.id.pw);
    civName = view.findViewById(R.id.civ_name);
    civGender = view.findViewById(R.id.civ_gender);
    view.findViewById(R.id.civ_gender).setOnClickListener(v -> onGender());
    view.findViewById(R.id.btn_done).setOnClickListener(view1 -> onDone());
    return view;
  }

  void onGender() {
    new DialogList(getContext()).list(getResources().getStringArray(R.array.gender_list),
      (parent, view, position, id) -> {
        civGender.setContent(getResources().getStringArray(R.array.gender_list)[position % 2]);
        gender = position % 2;
      }).show();
  }

  void onDone() {
    if (TextUtils.isEmpty(civName.getContent())) {
      showAlert("用户名不能为空");
      return;
    }
    if (!pw.checkValid()) {
      return;
    }
    listener.onInfoDone(civName.getContent(), gender, pw.getCode());
  }

  private RegisteDone listener;

  public void setListener(RegisteDone listener) {
    this.listener = listener;
  }

  public interface RegisteDone {
    void onInfoDone(String name, int gender, String pw);
  }

  @Override public String getFragmentName() {
    return RegisteInitSecoundFragment.class.getName();
  }
}
