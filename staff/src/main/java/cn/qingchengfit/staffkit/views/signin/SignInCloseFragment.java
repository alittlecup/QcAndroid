package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.presenters.ModuleConfigsPresenter;
import cn.qingchengfit.staffkit.presenters.SignInConfigPresenter;
import cn.qingchengfit.staffkit.views.signin.config.SignInTypeFragmentBuilder;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/3/23.
 */
public class SignInCloseFragment extends BaseFragment
    implements SignInConfigPresenter.MVPView, ModuleConfigsPresenter.MVPView {
  @Inject cn.qingchengfit.staffkit.presenters.SignInConfigPresenter presenter;
  @Inject ModuleConfigsPresenter moduleConfigsPresenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_close, container, false);
    view.findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickOpen();
      }
    });
    TextView toolbarTitle = view.findViewById(R.id.toolbar_title);
    toolbarTitle.setText("入场签到");
    Toolbar toolbar = view.findViewById(R.id.toolbar);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    delegatePresenter(moduleConfigsPresenter, this);
    return view;
  }

  @Override public String getFragmentName() {
    return SignInCloseFragment.class.getName();
  }

  public void onClickOpen() {
    if (!serPermisAction.check(PermissionServerUtils.CHECKIN_SETTING)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    showLoading();
    moduleConfigsPresenter.putModuleConfigs(true);
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    ToastUtils.show(getString(e));
  }

  @Override public void onCardCost(List<SignInCardCostBean.CardCost> cardCosts) {
    hideLoading();
    boolean setted = false;
    if (cardCosts != null) {
      for (int i = 0; i < cardCosts.size(); i++) {
        if (cardCosts.get(i).isSelected()) {
          setted = true;
          break;
        }
      }
    }
    if (setted) {//有设置
      getFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_fade_out, R.anim.slide_fade_out,
              R.anim.slide_right_out)
          .replace(R.id.student_frag, SignInHomeFragment.newInstance())
          .commitAllowingStateLoss();
    } else {//，未设置
      getFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_fade_out, R.anim.slide_fade_out,
              R.anim.slide_right_out)
          .replace(R.id.student_frag, new SignInTypeFragmentBuilder().enterType(1).build())
          .commitAllowingStateLoss();
    }
  }

  @Override public void onModuleStatus(ScoreStatus.ModuleBean moduleBean) {

  }

  @Override public void onStatusSuccess() {
    presenter.getSignInConfigs();
  }
}
