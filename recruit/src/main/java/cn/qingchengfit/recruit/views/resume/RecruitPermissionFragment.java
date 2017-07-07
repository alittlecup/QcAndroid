package cn.qingchengfit.recruit.views.resume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.RecruitPermission;
import cn.qingchengfit.recruit.network.body.EditPermissionBody;
import cn.qingchengfit.recruit.network.response.PermissionUserWrap;
import cn.qingchengfit.recruit.presenter.RecruitPermissionPresenter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/6/28.
 */
public class RecruitPermissionFragment extends BaseFragment
    implements RecruitPermissionPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.tv_pm_manage) TextView tvPmManage;
  @BindView(R2.id.tv_handle_resume) TextView tvHandleResume;
  @BindView(R2.id.tv_job_fair) TextView tvJobFair;
  @BindView(R2.id.tv_pm_setting) TextView tvPmSetting;
  @Inject RecruitPermissionPresenter permissionPresenter;
  @Inject RecruitRouter router;
  String gymid;

  public static RecruitPermissionFragment newInstance(String gmid) {
    Bundle args = new Bundle();
    args.putString("gymid", gmid);
    RecruitPermissionFragment fragment = new RecruitPermissionFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      gymid = getArguments().getString("gymid");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_permission_setting, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(permissionPresenter, this);
    initData();
    return view;
  }

  private void initData() {
    permissionPresenter.queryCommonstaff(gymid);
    permissionPresenter.queryPermissionUsers(gymid);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.setTitle(R.string.t_permission_setting);
  }

  @Override public String getFragmentName() {
    return RecruitPermissionFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 职位管理
   */
  @OnClick(R2.id.layout_pm_manage) public void onLayoutPmManageClicked() {
    if (permissionPresenter.getPermissonUsers(0) != null) {
      permissionPresenter.chooseStaff(0);
    }
  }

  /**
   * 处理简历
   */
  @OnClick(R2.id.layout_handle_resume) public void onLayoutHandleResumeClicked() {
  }

  /**
   * 报名参加招聘会
   */
  @OnClick(R2.id.layout_job_fair) public void onLayoutJobFairClicked() {
  }

  /**
   * 权限设置
   */
  @OnClick(R2.id.layout_pm_setting) public void onLayoutPmSettingClicked() {
  }

  @Override public void onPermssionList(List<RecruitPermission> permissions) {

  }

  @Override public void onPermssionCoutnt(PermissionUserWrap permissionUserWrap) {
    tvHandleResume.setText(getString(R.string.handle_resume_staff,
        permissionUserWrap.permissions.resume.users.size()));
    tvPmManage.setText(
        getString(R.string.manage_job_staff, permissionUserWrap.permissions.job.users.size()));
    tvJobFair.setText(getString(R.string.atendence_jobfair_staff,
        permissionUserWrap.permissions.fair.users.size()));
    tvPmSetting.setText(getString(R.string.permission_setting_staff,
        permissionUserWrap.permissions.setting.users.size()));
  }

  @Override public void onEditOk() {
    hideLoading();
    ToastUtils.show("修改成功");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 11) {
        showLoading();
        permissionPresenter.editPermssionUsers(new EditPermissionBody.Builder().gym_id(gymid)
            .key(permissionPresenter.getCurKey())
            .user_ids(data.getStringArrayListExtra("ids"))
            .build());
      }
    }
  }
}
