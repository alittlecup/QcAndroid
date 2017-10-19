package cn.qingchengfit.pos.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/18.
 */

@Leaf(module = "setting",path = "/cashier/add/")
public class AddCashierFragment extends BaseFragment implements CashierPresenter.MVPView{

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.input_add_cashier_name) CommonInputView inputAddCashierName;
  @BindView(R.id.input_add_cashier_gender) CommonInputView inputAddCashierGender;
  @BindView(R.id.input_add_cashier_phone) CommonInputView inputAddCashierPhone;
  @BindView(R.id.btn_add_cashier) TextView btnExchange;
  @Inject CashierPresenter cashierPresenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_crashier, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(cashierPresenter, this);
    return view;
  }

  @OnClick(R.id.input_add_cashier_gender)
  public void onGender(){
    final ArrayList<String> genderList = new ArrayList<>();
    genderList.add("男");
    genderList.add("女");
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("", genderList);
    bottomListFragment.setListener(new BottomListFragment.ComfirmChooseListener() {
      @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
        inputAddCashierGender.setContent(genderList.get(selectedPos.get(0)));
      }
    });
    bottomListFragment.show(getFragmentManager(), null);
  }

  @OnClick(R.id.btn_add_cashier)
  public void onConfirmExchange(){
    if (TextUtils.isEmpty(inputAddCashierName.getContent()) || TextUtils.isEmpty(
        inputAddCashierGender.getContent()) || TextUtils.isEmpty(inputAddCashierPhone.getContent())){
      DialogUtils.showAlert(getContext(), "请完善信息");
      return;
    }
    cashierPresenter.onAddCashier(inputAddCashierName.getContent(),
        inputAddCashierPhone.getContent(), inputAddCashierGender.getContent().equals("男") ? 0 : 1);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onAddSuccess() {
    ToastUtils.show("添加成功");
    getActivity().onBackPressed();
  }

  @Override public void onDeleteSuccess() {

  }

  @Override public void onModifySuccess() {

  }

  @Override public void onGetCashier(List<Cashier> cashierList) {

  }
}
