package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.presenters.ModuleConfigsPresenter;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import cn.qingchengfit.staffkit.views.signin.zq.presenter.ZqAccessPresenter;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import java.util.List;
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
    implements ModuleConfigsPresenter.MVPView, ZqAccessPresenter.MVPView {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject RestRepositoryV2 restRepository;
  @Inject ModuleConfigsPresenter presenter;
  @Inject IPermissionModel permissionModel;
	TextView tvSigninTypeSetted;
	TextView btnHowToUse;
	LinearLayout layoutSigninType;
	LinearLayout layoutSigninWardrobe;
	LinearLayout layoutSigninScreen;
	ExpandedLayout swOpen;
	Toolbar toolbar;
	TextView toolbarTitile;
	CommonInputView inputSignUseCard;
	CommonInputView inputSignUseQrcode;
	CommonInputView inputSignUseZq;
  @Inject ZqAccessPresenter zqPresenter;
  private boolean setted;
  private boolean isJumping = false;
  private boolean isAutoOpen = false;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_config_list, container, false);
    tvSigninTypeSetted = (TextView) view.findViewById(R.id.tv_signin_type_setted);
    btnHowToUse = (TextView) view.findViewById(R.id.btn_how_to_use);
    layoutSigninType = (LinearLayout) view.findViewById(R.id.layout_signin_type);
    layoutSigninWardrobe = (LinearLayout) view.findViewById(R.id.layout_signin_wardrobe);
    layoutSigninScreen = (LinearLayout) view.findViewById(R.id.layout_signin_screen);
    swOpen = (ExpandedLayout) view.findViewById(R.id.sw_open);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    inputSignUseCard = (CommonInputView) view.findViewById(R.id.input_sign_use_card);
    inputSignUseQrcode = (CommonInputView) view.findViewById(R.id.input_sign_use_qrcode);
    inputSignUseZq = (CommonInputView) view.findViewById(R.id.input_sign_use_zq);
    view.findViewById(R.id.btn_how_to_use).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickHowUse();
      }
    });
    view.findViewById(R.id.input_sign_use_card).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSignUseCard();
      }
    });
    view.findViewById(R.id.input_sign_use_qrcode).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSignUseQrCode();
      }
    });
    view.findViewById(R.id.input_sign_use_zq).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSignUseZq();
      }
    });
    view.findViewById(R.id.layout_signin_type).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SigninConfigListFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.layout_signin_wardrobe).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SigninConfigListFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.layout_signin_screen).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SigninConfigListFragment.this.onClick(v);
      }
    });

    delegatePresenter(presenter, this);
    delegatePresenter(zqPresenter, this);
    initToolbar(toolbar);
    swOpen.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (!isAutoOpen) presenter.putModuleConfigs(isChecked);
      layoutSigninScreen.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      layoutSigninType.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      layoutSigninWardrobe.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      isAutoOpen = false;
    });
    swOpen.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(swOpen.getViewTreeObserver(), this);
            presenter.getModuleConfigs();
            zqPresenter.getAccess();
          }
        });
    swOpen.setEnabled(permissionModel.check(PermissionServerUtils.CHECKIN_SETTING_CAN_CHANGE));

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

 public void onClickHowUse() {
    WebActivity.startWeb(Router.WEB_HOW_TO_USE_SIGNIN, getContext());
  }


  public void onSignUseCard(){
    ContainerActivity.router(QRActivity.SIGN_IN_CARD, getContext());
  }


  public void onSignUseQrCode(){
    ContainerActivity.router(QRActivity.SIGN_IN_CODE, getContext());
  }


  public void onSignUseZq(){
    ContainerActivity.router(QRActivity.ZQ_ACCESS, getContext());
  }


  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.layout_signin_type:
        if (!permissionModel.check(PermissionServerUtils.CHECKIN_HELP)) {
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
        if (!permissionModel.check(PermissionServerUtils.CHECKIN_LOCKER_LINK_NEW)) {
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
        if (!permissionModel.check(PermissionServerUtils.CHECKIN_SCREEN)) {
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

  @Override public void onResume() {
    super.onResume();
    zqPresenter.getAccess();
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

  @Override public void onGetAccess(List<Guard> guardList) {
    inputSignUseZq.setContent(guardList.size() +  "个");
  }

  @Override public void changeStatusOk() {

  }

  @Override public void onDeleteOk() {

  }

  @Override public void onAddOk() {

  }

  @Override public void onEditOk() {

  }
}
