package cn.qingchengfit.staffkit.views.wardrobe.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.EditWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseRegionFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeDetailFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.concurrent.locks.Lock;
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
 * Created by Paper on 16/8/25.
 */
public class WardrobeEditFragment extends BaseFragment implements WardrobeEditPresenter.MVPView {

  @Inject WardrobeEditPresenter presenter;
  CommonInputView wardrobeNo;
  CommonInputView wardrobeDistrict;
  View permissonDendy;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  private Locker mLocker;
  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      showLoading();
      presenter.completeWardrobe(App.staffId, mLocker.id,
          new EditWardrobeBody.Builder().name(wardrobeNo.getContent())
              .region_id(region.id)
              .build());
      return true;
    }
  };
  private Toolbar toolbar;
  private TextView toolbarTitile;

  public static WardrobeEditFragment newInstance(Locker locker) {

    Bundle args = new Bundle();
    args.putParcelable("l", locker);
    WardrobeEditFragment fragment = new WardrobeEditFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLocker = getArguments().getParcelable("l");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_wardrobe, container, false);
    wardrobeNo = (CommonInputView) view.findViewById(R.id.wardrobe_no);
    wardrobeDistrict = (CommonInputView) view.findViewById(R.id.wardrobe_district);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    permissonDendy = (View) view.findViewById(R.id.permisson_dendy);
    view.findViewById(R.id.wardrobe_district).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeEditFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeEditFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.permisson_dendy).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeEditFragment.this.onClick(v);
      }
    });

    //
    delegatePresenter(presenter, this);
    //        CommonInputView wardrobeNo = (CommonInputView) view.findViewById(R.id.wardrobe_no);
    //        final CommonInputView wardrobeDistrict = (CommonInputView) view.findViewById(R.id.wardrobe_district);
    boolean permissionChg = serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
        PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE);
    //mCallbackActivity.setToolbar("编辑更衣柜", false, null, permissionChg ? R.menu.menu_compelete : 0, listener);
    if (permissionChg) {
      toolbar.inflateMenu(R.menu.menu_compelete);
      toolbar.setOnMenuItemClickListener(listener);
    }
    permissonDendy.setVisibility(permissionChg ? View.GONE : View.VISIBLE);
    wardrobeNo.setContent(mLocker.name);
    region = mLocker.region;
    wardrobeDistrict.setContent(mLocker.region.name);
    wardrobeDistrict.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), ChooseRegionFragment.newInstance())
            .addToBackStack(getFragmentName())
            .commit();
      }
    });
    RxBusAdd(LockerRegion.class).subscribe(new Action1<LockerRegion>() {
      @Override public void call(LockerRegion lockerRegion) {
        wardrobeDistrict.setContent(lockerRegion.name);
        region = lockerRegion;
      }
    });

    return view;
  }

  private LockerRegion region;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑更衣柜");
  }

  private Bundle saveState = new Bundle();

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    String name = saveState.getString("name");
    if (!TextUtils.isEmpty(name)) {
      wardrobeNo.setContent(name);
    }
  }

  @Override public String getFragmentName() {
    return WardrobeEditFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.wardrobe_district://选择区域
        saveState.putString("name", wardrobeNo.getContent());
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), ChooseRegionFragment.newInstance())
            .addToBackStack(getFragmentName())
            .commit();
        break;
      case R.id.btn_del:
        if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
            PermissionServerUtils.LOCKER_SETTING_CAN_DELETE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }

        new MaterialDialog.Builder(getContext()).content(
            getString(R.string.alert_del_wradrobe, mLocker.name))
            .positiveText(R.string.common_comfirm)
            .canceledOnTouchOutside(true)
            .negativeText(R.string.pickerview_cancel)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                showLoading();
                presenter.delWardrobe(App.staffId, mLocker.id);
              }
            })
            .show();

        break;
      case R.id.permisson_dendy:
        showAlert("您没有编辑更衣柜权限");
        break;
    }
  }

  @Override public void onSaveOk(Locker locker) {
    hideLoading();
    WardrobeDetailFragment.locker = locker;
    getActivity().onBackPressed();
  }

  @Override public void onDelOk() {
    hideLoading();
    getFragmentManager().popBackStack(WardrobeMainFragment.class.getName(),
        FragmentManager.POP_BACK_STACK_INCLUSIVE);
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }
}
