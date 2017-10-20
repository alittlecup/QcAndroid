package cn.qingchengfit.pos.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.pos.di.PosGymWrapper;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.bumptech.glide.Glide;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/13.
 */
@Leaf(module = "setting", path = "/home/") public class SettingFragment extends BaseFragment
    implements CashierPresenter.MVPView {

  @BindView(R.id.img_setting_gym) ImageView imgSettingGym;
  @BindView(R.id.tv_setting_gym_name) TextView tvSettingGymName;
  @BindView(R.id.layout_setting_gym) LinearLayout layoutSettingGym;
  @BindView(R.id.img_setting_staff) ImageView imgSettingStaff;
  @BindView(R.id.tv_setting_staff_name) TextView tvSettingStaffName;
  @BindView(R.id.tv_setting_staff_position) TextView tvSettingStaffPosition;
  @BindView(R.id.layout_setting_staff) LinearLayout layoutSettingStaff;
  @BindView(R.id.input_cashier) CommonInputView inputCashier;
  @Inject CashierPresenter presenter;
  @Inject PosGymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    setToolbar(toolbar);
    initView();
    presenter.qcGetCashier();
    return view;
  }

  private void initView(){
    Glide.with(getContext())
        .load(gymWrapper.getCoachService().photo)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(imgSettingGym, getContext()));

    tvSettingGymName.setText(gymWrapper.brand_name() + gymWrapper.name());

    Glide.with(getContext())
        .load(loginStatus.getLoginUser().getAvatar())
        .asBitmap()
        .placeholder(R.drawable.ic_default_head_nogender)
        .into(new CircleImgWrapper(imgSettingStaff, getContext()));

    tvSettingStaffName.setText(loginStatus.getLoginUser().username);
    tvSettingStaffPosition.setText("收银员");
  }

  private void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("设置");
  }

  @OnClick(R.id.layout_setting_gym)
  public void onGymDetail(){
    routeTo(AppUtils.getRouterUri(getContext(), "/setting/gym/detail/"), null);
  }

  @OnClick(R.id.layout_setting_staff)
  public void onStaffDetail(){
    Bundle b = new Bundle();
    Cashier cashier = new Cashier();
    cashier.id = loginStatus.getLoginUser().getId();
    cashier.username = loginStatus.getLoginUser().username;
    cashier.avatar = loginStatus.getLoginUser().avatar;
    cashier.gender = loginStatus.getLoginUser().gender;
    cashier.phone = loginStatus.getLoginUser().phone;
    b.putParcelable("cashier", cashier);
    routeTo(AppUtils.getRouterUri(getContext(), "setting/cashier/detail/"), b);
  }

  @OnClick(R.id.input_cashier) public void onCashier() {
    routeTo(AppUtils.getRouterUri(getContext(), "/setting/cashier/list/"), null);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onAddSuccess() {

  }

  @Override public void onDeleteSuccess() {

  }

  @Override public void onModifySuccess() {

  }

  @Override public void onGetCashier(List<Cashier> cashierList) {
    if (cashierList != null) {
      inputCashier.setContent(String.valueOf(cashierList.size()));
    }
  }
}
