package cn.qingchengfit.staffkit.views.signin.config;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.presenters.ModuleConfigsPresenter;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

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
 * Created by Paper on 2017/2/13.
 */
public class SigninConfigListFragment extends BaseFragment
    implements ModuleConfigsPresenter.MVPView {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject RestRepositoryV2 restRepository;
  @Inject ModuleConfigsPresenter presenter;
  @BindView(R.id.tv_signin_type_setted) TextView tvSigninTypeSetted;
  @BindView(R.id.btn_how_to_use) TextView btnHowToUse;
  @BindView(R.id.layout_signin_type) LinearLayout layoutSigninType;
  @BindView(R.id.layout_signin_wardrobe) LinearLayout layoutSigninWardrobe;
  @BindView(R.id.layout_signin_screen) LinearLayout layoutSigninScreen;
  @BindView(R.id.sw_open) ExpandedLayout swOpen;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitile;
  @BindView(R.id.input_sign_use_card) CommonInputView inputSignUseCard;
  @BindView(R.id.input_sign_use_qrcode) CommonInputView inputSignUseQrcode;
  @BindView(R.id.input_sign_use_zq) CommonInputView inputSignUseZq;
  private boolean setted;
  private boolean isJumping = false;
  private boolean isAutoOpen = false;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_config_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    delegatePresenter(zqPresenter, this);
    initToolbar(toolbar);
    swOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isAutoOpen) presenter.putModuleConfigs(isChecked);
        layoutSigninScreen.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        layoutSigninType.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        layoutSigninWardrobe.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        isAutoOpen = false;
      }
    });
    swOpen.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(swOpen.getViewTreeObserver(), this);
            presenter.getModuleConfigs();
          }
        });

    btnHowToUse.setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_grey), null, null, null);
    isLoading = true;
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    mCallbackActivity.setToolbar(getString(R.string.title_signin_config), false, null, 0, null);
    toolbarTitile.setText(R.string.title_signin_config);
  }

  @Override public String getFragmentName() {
    return SigninConfigListFragment.class.getName();
  }

  @OnClick(R.id.btn_how_to_use) public void onClickHowUse() {
    WebActivity.startWeb(Router.WEB_HOW_TO_USE_SIGNIN, getContext());
  }

  @OnClick(R.id.input_sign_use_card)
  public void onSignUseCard(){
    ContainerActivity.router(GymFunctionFactory.SIGN_IN_CARD, getContext());
  }

  @OnClick(R.id.input_sign_use_qrcode)
  public void onSignUseQrCode(){
    ContainerActivity.router(GymFunctionFactory.SIGN_IN_CODE, getContext());
  }

  @OnClick(R.id.input_sign_use_zq)
  public void onSignUseZq(){
    ContainerActivity.router(GymFunctionFactory.ZQ_ACCESS, getContext());
  }

  @OnClick({ R.id.layout_signin_type, R.id.layout_signin_wardrobe, R.id.layout_signin_screen })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.layout_signin_type:
        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
            PermissionServerUtils.CHECKIN_HELP)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
            .replace(mCallbackActivity.getFragId(), new SignInTypeFragment())
            .addToBackStack(null)
            .commit();
        break;
      case R.id.layout_signin_wardrobe:
        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
            PermissionServerUtils.CHECKIN_LOCKER_LINK)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
            .replace(mCallbackActivity.getFragId(), new SinginConfigWardrobeFragment())
            .addToBackStack(null)
            .commit();
        break;
      case R.id.layout_signin_screen:
        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
            PermissionServerUtils.CHECKIN_SCREEN)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
            .replace(mCallbackActivity.getFragId(), new SignInConfigScreenFragment())
            .addToBackStack(null)
            .commit();
        break;
    }
  }
  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {
  }

  @Override public void onModuleStatus(ScoreStatus.ModuleBean moduleBean) {
    if (moduleBean.isCheckin()) isAutoOpen = true;
    swOpen.setExpanded(moduleBean.isCheckin());

    Observable observable =
        ((Get_Api) restRepository.getApi(Get_Api.class)).qcGetSignInCostConfig(App.staffId,
            gymWrapper.getParams());
    RxRegiste(HttpUtil.getInstance()
        .toSubscribe(observable, new ResultSubscribe<SignInCardCostBean.Data>() {
          @Override protected void _onNext(SignInCardCostBean.Data signInCardCostBean) {
            setted = false;
            if (signInCardCostBean.card_costs != null) {
              for (int i = 0; i < signInCardCostBean.card_costs.size(); i++) {
                if (signInCardCostBean.card_costs.get(i).isSelected()) {
                  setted = true;
                  break;
                }
              }
            }
            tvSigninTypeSetted.setText(
                setted ? R.string.common_have_setting : R.string.common_un_setting);
          }

          @Override protected void _onError(String message) {
            Timber.e(message);
          }
        }));
  }

  @Override public void onStatusSuccess() {
    if (swOpen.isExpanded()) {
      if (!setted) {
        //isJumping = true;
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_in)
            .replace(mCallbackActivity.getFragId(),
                new SignInTypeFragmentBuilder().autoIn(1).build())
            .addToBackStack(null)
            .commit();
      }
    } else {

    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
