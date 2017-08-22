package cn.qingchengfit.saas.views.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saas.presenters.BaseGymInfoPresenter;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.CommonInputTextFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import eu.davidea.flexibleadapter.items.IFilterable;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 2017/7/11.
 */
public class EditGymInfoFragment extends BaseFragment implements BaseGymInfoPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.header) ImageView header;
  @BindView(R2.id.gym_name) CommonInputView gymName;
  @BindView(R2.id.address) CommonInputView civAddress;
  @BindView(R2.id.phone) CommonInputView phone;
  @BindView(R2.id.descripe) CommonInputView descripe;
  @Inject BaseGymInfoPresenter presenter;
  private ChoosePictureFragmentNewDialog choosePicFragment;
  private Gym gym = new Gym();

  public static EditGymInfoFragment newInstance(String gymid) {
    Bundle args = new Bundle();
    args.putString("gymid", gymid);
    EditGymInfoFragment fragment = new EditGymInfoFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit_base_gym, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.setGymid(getArguments().getString("gymid", ""));
    initToolbar(toolbar);
    RxBusAdd(EventTxT.class).subscribe(new Action1<EventTxT>() {
      @Override public void call(EventTxT eventTxT) {
        descripe.setContent("详情");
        presenter.setGymDesc(eventTxT.txt);
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("基本信息");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (gymName.isEmpty()) {
          ToastUtils.show("请填写场馆名称");
          return true;
        }
        showLoading();
        presenter.setGymName(gymName.getContent());
        presenter.setGymPhone(phone.getContent());
        presenter.editGymInfo();
        return false;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    presenter.queryGymInfo();
  }

  @Override public String getFragmentName() {
    return EditGymInfoFragment.class.getName();
  }

  @Override public void onDestroyView() {
    setBackPressNull();
    super.onDestroyView();
  }

  @OnClick(R2.id.layout_gym_img) public void onLayoutGymImgClicked() {
    if (choosePicFragment == null) {
      choosePicFragment = ChoosePictureFragmentNewDialog.newInstance();
      choosePicFragment.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String filePath) {

        }

        @Override public void onUploadComplete(String filePaht, String url) {
          PhotoUtils.smallCircle(header, url);
          presenter.setGymPhoto(url);
        }
      });
    }
    if (!choosePicFragment.isVisible()) {
      choosePicFragment.show(getChildFragmentManager(), "");
    }
  }

  @OnClick(R2.id.address) public void onAddressClicked() {
    new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean aBoolean) {
            if (aBoolean) {
              BaseRouter.toChoose(getContext(), 71, gym);
            } else {
              ToastUtils.show("您拒绝了定位权限");
            }
          }
        });
  }

  @OnClick(R2.id.descripe) public void clickDesc() {
    if (getActivity() != null && getActivity() instanceof BaseActivity) {
      getFragmentManager().beginTransaction()
          .add(((BaseActivity) getActivity()).getFragId(),
              CommonInputTextFragment.newInstance("描述您的健身房", presenter.getGymDesc(), "请填写健身房描述"))
          .addToBackStack(null)
          .commit();
    }
  }

  @Override public void onGym(Gym gym) {
    if (gym != null) {
      this.gym = gym;
    }
    PhotoUtils.smallCircle(header, gym.getPhoto());
    gymName.setContent(gym.name);
    civAddress.setContent(gym.getAddressStr());
    phone.setContent(gym.phone);
    descripe.setContent(gym.description);
  }

  @Override public void onAddress(String s) {
    civAddress.setContent(s);
  }

  @Override public void onEditOk() {
    hideLoading();
    RxBus.getBus().post(new EventFreshGyms());
    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
      getActivity().getSupportFragmentManager().popBackStackImmediate();
    }else{
      getActivity().finish();
    }
  }

  @Override public boolean onFragmentBackPress() {
    DialogUtils.instanceDelDialog(getContext(), "确定放弃所做修改？",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
              getActivity().getSupportFragmentManager().popBackStackImmediate();
            }else{
              getActivity().finish();
            }
          }
        }).show();
    return true;
  }
}
