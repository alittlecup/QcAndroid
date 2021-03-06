package cn.qingchengfit.staffkit.views.signin.config;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.GetApi;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class SignInConfigScreenFragment extends BaseFragment {

  @Inject QcRestRepository mRestRepository;
  @Inject GymFunctionFactory gymFunctionFactory;
  TextView tvScreenUrl;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  Toolbar toolbar;
  TextView toolbarTitile;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_screen, container, false);
    tvScreenUrl = (TextView) view.findViewById(R.id.tv_screen_url);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.layout_screen).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SignInConfigScreenFragment.this.onClick();
      }
    });
    view.findViewById(R.id.btn_cpoy_link).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCopyLink();
      }
    });

    initToolbar(toolbar);
    RxRegiste(mRestRepository.createRxJava1Api(GetApi.class)
        .qcGetGymExtra(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(new Action1<QcDataResponse<GymExtra>>() {
          @Override public void call(QcDataResponse<GymExtra> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.getData().urls != null) {
                for (int i = 0; i < qcResponse.getData().urls.size(); i++) {
                  if (qcResponse.getData().urls.get(i).name.equalsIgnoreCase("checkin")) {
                    tvScreenUrl.setText(qcResponse.getData().urls.get(i).url);
                  }
                }
              }
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
          }
        }));
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("签到大屏幕");
  }

  @Override public String getFragmentName() {
    return SignInConfigScreenFragment.class.getName();
  }

  public void onClick() {
    if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
        PermissionServerUtils.CHECKIN_SCREEN_CAN_CHANGE)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    gymFunctionFactory.goQrScan(this, QRActivity.SIGNIN_SCREEN, null, gymWrapper.getCoachService());
  }

  public void onCopyLink() {
    ClipboardManager cmb =
        (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    cmb.setText(tvScreenUrl.getText());
    ToastUtils.showS(getString(R.string.link_has_copied));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
