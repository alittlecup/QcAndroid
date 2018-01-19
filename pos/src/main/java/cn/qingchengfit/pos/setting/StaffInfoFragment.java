package cn.qingchengfit.pos.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.login.LoginActivity;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxMenuItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/13.
 */

@Leaf(module = "setting", path = "/cashier/detail/")
public class StaffInfoFragment extends SaasBaseFragment implements CashierPresenter.MVPView{

  @BindView(R.id.tv_setting_staff_detail_name) TextView tvSettingStaffDetailName;
  @BindView(R.id.input_setting_staff_detail_name) CommonInputView inputSettingStaffDetailName;
  @BindView(R.id.input_setting_staff_gender) CommonInputView inputSettingStaffGender;
  @BindView(R.id.input_setting_staff_phone) CommonInputView inputSettingStaffPhone;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.btn_delete_cashier) TextView btnDeleteCashier;
  @BindView(R.id.img_staff_head) ImageView imgStaffHead;
  @Inject CashierPresenter presenter;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Need public Staff cashier;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting_staff, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    Glide.with(getContext())
        .load(cashier.avatar)
        .asBitmap()
        .placeholder(AppUtils.getHeaderDrawable(loginStatus.getLoginUser().gender))
        .into(new CircleImgWrapper(imgStaffHead, getContext()));
    inputSettingStaffDetailName.setContent(cashier.username);
    inputSettingStaffGender.setContent(cashier.gender == 1 ? "女" : "男");
    inputSettingStaffPhone.setContent(cashier.phone);

    if (loginStatus.getLoginUser().getPhone() != null &&
      cashier != null
    &&loginStatus.getLoginUser().getPhone().equalsIgnoreCase(cashier.phone)){
      btnDeleteCashier.setText("退出登录");
    }else{
      btnDeleteCashier.setText("删除");
    }
    setToolbar(toolbar);
    return view;
  }

  private void setToolbar(Toolbar toolbar){
    initToolbar(toolbar);
    toolbarTitle.setText("收银员");
    toolbar.inflateMenu(R.menu.menu_save);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          if (check()){
            DialogUtils.showAlert(getContext(), "请完善信息");
          }else{
            presenter.onModifyCashier(cashier.id, inputSettingStaffDetailName.getContent(),
              inputSettingStaffPhone.getContent(), inputSettingStaffGender.getContent().equals("男") ? 0 : 1);
          }
        }
      });

  }

  @OnClick(R.id.input_setting_staff_gender)
  public void onGender(){
    final ArrayList<String> genderList = new ArrayList<>();
    genderList.add("男");
    genderList.add("女");
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("", genderList);
    bottomListFragment.setListener(new BottomListFragment.ComfirmChooseListener() {
      @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
        inputSettingStaffGender.setContent(genderList.get(selectedPos.get(0)));
      }
    });
    bottomListFragment.show(getFragmentManager(), null);
  }

  private boolean check(){
    return TextUtils.isEmpty(inputSettingStaffDetailName.getContent()) || TextUtils.isEmpty(
        inputSettingStaffGender.getContent()) || TextUtils.isEmpty(
        inputSettingStaffDetailName.getContent());
  }

  @OnClick(R.id.btn_delete_cashier)
  public void onDelete(){
    if (loginStatus.getLoginUser().getPhone() != null &&
        cashier != null
        &&loginStatus.getLoginUser().getPhone().equalsIgnoreCase(cashier.phone)) {
      DialogUtils.instanceDelDialog(getContext(), getResources().getString(R.string.tips_confirm_logout),
          new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull
                DialogAction which) {
              if (which == DialogAction.POSITIVE) {
                logOut();
              }
            }
          }).show();
    }else{
      DialogUtils.instanceDelDialog(getContext(), getResources().getString(R.string.tips_confirm_delete_cashier),
          new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull
                DialogAction which) {
              if (which == DialogAction.POSITIVE) {
                presenter.onDelete(cashier.id, gymWrapper.id());
              }
            }
          }).show();
    }
  }

  private void logOut(){
    PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
    PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION_ID, "");
    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    getContext().startActivity(intent);
    getActivity().finish();
  }


  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onAddSuccess() {

  }

  @Override public void onDeleteSuccess() {
    ToastUtils.show("删除成功");
    RxBus.getBus().post(new EventSaasFresh.StaffList());
    getActivity().onBackPressed();
  }

  @Override public void onModifySuccess() {
    if (loginStatus.getLoginUser().getPhone() != null &&
        cashier != null
        &&loginStatus.getLoginUser().getPhone().equalsIgnoreCase(cashier.phone)){

      Staff staff = new Staff(inputSettingStaffDetailName.getContent(),
          inputSettingStaffPhone.getContent(), loginStatus.getLoginUser().avatar,
      inputSettingStaffGender.getContent().equals("男") ? 0 : 1);
      staff.setId(cashier.id);
      loginStatus.setLoginUser(staff);
      RxBus.getBus().post(new EventSaasFresh.LoginUserModify());
    }
    RxBus.getBus().post(new EventSaasFresh.StaffList());
    getActivity().onBackPressed();
  }

  @Override public void onGetCashier(List<Staff> cashierList) {

  }
}
