package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentStaffDetailSassBinding;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.staff.beans.StaffLoginMethod;
import cn.qingchengfit.saasbase.staff.event.EventAddStaffDone;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.presenter.StaffDetailPresenter;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
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
public class StaffDetailFragment extends SaasBaseFragment implements StaffDetailPresenter.MVPView {

  @Inject public StaffDetailPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  @Need StaffShip staffShip;

  FragmentStaffDetailSassBinding db;
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    db = FragmentStaffDetailSassBinding.inflate(inflater);
    delegatePresenter(presenter, this);
    presenter.setStaff(staffShip);
    presenter.queryPositions();
    initToolbar(db.layoutToolbar.toolbar);
    db.btnDel.setVisibility(View.VISIBLE);
    db.btnAdd.setVisibility(View.GONE);
    db.setMethods(new StaffLoginMethod.Builder().phone(staffShip.user.phone)
      .phone_active(staffShip.user.isPhone_active())
      .wx(staffShip.user.getWeixin())
      .wx_active(staffShip.user.isWeixin_active())
      .build()
    );
    initClick();
    return db.getRoot();
  }



  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.setToolbarModel(new ToolbarModel("工作人员详情"));
    db.getToolbarModel().setMenu(R.menu.menu_save);
    db.getToolbarModel().setListener(item -> {
      if (db.phoneNum.checkPhoneNum())
        presenter.editStaff();
      return true;
    });
  }

  protected void initClick(){
    if (serPermisAction.check(PermissionServerUtils.MANAGE_STAFF_CAN_CHANGE)){
      db.denyLayout.setVisibility(View.GONE);
    }else {
      db.denyLayout.setVisibility(View.VISIBLE);
    }
    db.denyLayout.setOnClickListener(view -> onDeny());
    db.position.setOnClickListener(view -> onPositionClicked());
    db.headerLayout.setOnClickListener(view -> onHeaderLayoutClicked());
    db.civGender.setOnClickListener(view -> clickGender());
    db.btnAdd.setOnClickListener(view -> onBtnAddClicked());
    db.btnDel.setOnClickListener(view -> onBtnDelClicked());
  }

  public void onDeny() {
    showAlert(R.string.alert_permission_forbid);
  }

  @Override public String getFragmentName() {
    return StaffDetailFragment.class.getName();
  }




  @Override public void onFixSuccess() {
    hideLoading();
    popBack();
  }

  @Override public void onAddSuccess() {
    hideLoading();
    RxBus.getBus().post(new EventAddStaffDone());
    popBack();
  }

  @Override public void onDelSuccess() {
    hideLoading();
    popBack();
  }

  @Override public void onStaff(StaffShip staff) {
    if (staff != null){
      PhotoUtils.smallCircle(db.headerImg,staff.getAvatar());
      db.username.setContent(staff.getUsername());
      db.phoneNum.setPhoneNum(staff.getPhone());
      db.civGender.setContent(getResources().getStringArray(R.array.gender_list)[staff.gender]);
      if (staff.position != null)
        db.position.setContent(staff.position.getName());
      if (staff.is_coach && !staff.is_staff){
        onPosition("教练");
      }else {
        if (staff.position != null){
          onPosition(staff.position.getName());
        }
      }
    }
  }

  @Override public void onFailed(String s) {
    hideLoading();
  }

  @Override public void onPosition(String positon) {
    this.db.position.setContent(positon);
  }

  @Override public String getName() {
    return this.db.username.getContent();
  }

  @Override public String getPhone() {
    return this.db.phoneNum.getPhoneNum();
  }

  @Override public String getAreaCode() {
    return db.phoneNum.getDistrictInt();
  }

  public void clickGender(){
    new DialogList(getContext()).list(getResources().getStringArray(R.array.gender_list), new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.setGender(position);
        db.civGender.setContent(getResources().getStringArray(R.array.gender_list)[position]);
      }
    }).show();
  }

   public void onHeaderLayoutClicked() {
    ChoosePictureFragmentNewDialog dialog = ChoosePictureFragmentNewDialog.newInstance(true);
    dialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
      @Override public void onChoosefile(String filePath) {
        PhotoUtils.smallCircle(db.headerImg,filePath);
      }

      @Override public void onUploadComplete(String filePaht, String url) {
        PhotoUtils.smallCircle(db.headerImg,url);
        presenter.setAvatar(url);
      }
    });
      dialog.show(getChildFragmentManager(),"");
  }

  public void onPositionClicked() {
    presenter.choosePosition();
  }



  //@OnClick(R2.id.go_to_web) public void onGoToWebClicked() {
  //  //跳去扫码页面
  //  QRActivity.start(getContext(),QRActivity.MODULE_MANAGE_STAFF);
  //}

  public void onBtnDelClicked() {
    if (!serPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_DELETE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    DialogUtils.instanceDelDialog(getActivity(), String.format("确定将%s设为离职吗？",staffShip.getUsername()),"离职后\n"
        + "1.该用户不能登录到当前场馆后台。\n"
        + "2.跟该工作人员相关的过往业务数据将被保留。\n"
        + "3.可以由健身房工作人员复职", (dialog, which) -> {
          showLoading();
          presenter.delStaff();
        }).show();
  }


 public void onBtnAddClicked() {
    presenter.addStaff();
  }
}
