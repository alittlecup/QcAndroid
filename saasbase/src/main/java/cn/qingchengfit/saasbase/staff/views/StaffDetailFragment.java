package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.staff.event.EventAddStaffDone;
import cn.qingchengfit.saasbase.staff.presenter.StaffDetailPresenter;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/12 2016.
 */
@Leaf(module = "staff",path = "/detail/")
public class StaffDetailFragment extends BaseFragment implements StaffDetailPresenter.MVPView {

  @BindView(R2.id.toolbar) protected Toolbar toolbar;
  @BindView(R2.id.toolbar_title)protected TextView toolbarTitle;
  @BindView(R2.id.header_img)protected ImageView headerImg;
  @BindView(R2.id.header_layout)protected RelativeLayout headerLayout;
  @BindView(R2.id.username)protected CommonInputView username;
  @BindView(R2.id.civ_gender)protected CommonInputView civGender;
  @BindView(R2.id.phone_num)protected PhoneEditText phoneNum;
  @BindView(R2.id.position)protected CommonInputView position;
  @BindView(R2.id.deny_layout)protected View denyLayout;
  @BindView(R2.id.go_to_web)protected TextView goToWeb;
  @BindView(R2.id.btn_del)protected RelativeLayout btnDel;
  @BindView(R2.id.btn_add)protected Button btnAdd;

  @Inject StaffDetailPresenter presenter;
  @Inject SerPermisAction serPermisAction;



  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_staff_detail_sass, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    SpannableString ss = new SpannableString(getString(R.string.hint_login_web));
    ss.setSpan(new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_orange)), 4,
      7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
    goToWeb.setText(ss, TextView.BufferType.SPANNABLE);

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("工作人员详情");
  }

  @OnClick(R2.id.deny_layout) public void onDeny() {
    showAlert(R.string.alert_permission_forbid);
  }

  @Override public String getFragmentName() {
    return StaffDetailFragment.class.getName();
  }



  @OnClick(R2.id.position) public void onClick() {
    //if (mPositionStrList.size() > 0) {
    //  final DialogList dialogList = new DialogList(getContext());
    //  dialogList.list(mPositionStrList, new AdapterView.OnItemClickListener() {
    //    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //      dialogList.dismiss();
    //    }
    //  });
    //  dialogList.show();
    //} else {
    //  presenter.queryPostions(staffId);
    //  ToastUtils.show("获取不到职位列表,请重试");
    //}
  }

  @Override public void onFixSuccess() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onAddSuccess() {
    hideLoading();
    RxBus.getBus().post(new EventAddStaffDone());
    getActivity().onBackPressed();
  }

  @Override public void onDelSuccess() {
    hideLoading();
    popBack();
  }

  @Override public void onStaff(Staff staff) {
    if (staff != null){
      username.setContent(staff.getUsername());
      phoneNum.setPhoneNum(staff.getPhone());
      civGender.setContent(getResources().getStringArray(R.array.gender_list)[staff.gender]);
      position.setContent(staff.position_str);
    }
  }

  @Override public void onFailed(String s) {
    hideLoading();
  }

  @Override public void onPositions(List<StaffPosition> positions) {
    hideLoading();
    //mPostions = positions;
    //mPositionStrList.clear();
    //for (int i = 0; i < positions.size(); i++) {
    //  mPositionStrList.add(positions.get(i).name);
    //}
    //if (mPostions.size() > 0) {
    //  if (mStaff != null && mStaff.position != null) {
    //    for (StaffPosition p : mPostions) {
    //      if (p.id.equals(mStaff.position.id)) {
    //        position.setContent(p.name);
    //        break;
    //      }
    //    }
    //  } else {
    //    position.setContent(mPostions.get(0).name);
    //    body.setPosition_id(mPostions.get(0).id);
    //  }
    //}
  }



  @OnClick(R2.id.header_layout) public void onHeaderLayoutClicked() {
    ChoosePictureFragmentNewDialog.newInstance(true).show(getChildFragmentManager(),"");
  }

  @OnClick(R2.id.position) public void onPositionClicked() {
    // TODO: 2017/10/18 展示职位列表
  }

  @OnClick(R2.id.deny_layout) public void onDenyLayoutClicked() {
    showAlert(R.string.sorry_for_no_permission);
  }

  @OnClick(R2.id.go_to_web) public void onGoToWebClicked() {
    //gymFunctionFactory.goQrScan(this, StaffConstant.PERMISSION_STAFF, null, null);
    //跳去扫码页面
  }

  @OnClick(R2.id.btn_del) public void onBtnDelClicked() {
    if (!serPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_DELETE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }

    DialogUtils.instanceDelDialog(getActivity(), "确定删除此工作人员?",
      new MaterialDialog.SingleButtonCallback() {
        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          showLoading();
          presenter.delStaff();
        }
      }).show();
  }

  @OnClick(R2.id.btn_add) public void onBtnAddClicked() {
    presenter.addStaff();
  }
}
