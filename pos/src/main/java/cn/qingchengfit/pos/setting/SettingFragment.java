package cn.qingchengfit.pos.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/13.
 */
@Leaf(module = "setting",path = "/home/")
public class SettingFragment extends BaseFragment implements CashierPresenter.MVPView{

  @BindView(R.id.img_setting_gym) ImageView imgSettingGym;
  @BindView(R.id.tv_setting_gym_name) TextView tvSettingGymName;
  @BindView(R.id.layout_setting_gym) LinearLayout layoutSettingGym;
  @BindView(R.id.input_setting_pos_no) CommonInputView inputSettingPosNo;
  @BindView(R.id.img_setting_staff) ImageView imgSettingStaff;
  @BindView(R.id.tv_setting_staff_name) TextView tvSettingStaffName;
  @BindView(R.id.tv_setting_staff_position) TextView tvSettingStaffPosition;
  @BindView(R.id.layout_setting_staff) LinearLayout layoutSettingStaff;
  @BindView(R.id.input_cashier) CommonInputView inputCashier;
  @Inject CashierPresenter presenter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.qcGetCashier();
    return view;
  }

  @OnClick(R.id.input_cashier)
  public void onCashier(){
     //TODO 收银员列表页面
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
    if (cashierList != null){
      inputCashier.setContent(String.valueOf(cashierList.size()));
    }
  }
}
