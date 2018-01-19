package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventFreshJobsList;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
 * Created by Paper on 2017/5/26.
 */
public class RecruitPositionDetailEmployerFragment extends RecruitPositionDetailFragment {

  //@BindView(R2.id.rv_demands) RecyclerView rvDemands;
  //@BindView(R2.id.rv_welfare) QcTagGroup rvWelfare;
  //@BindView(R2.id.img_recruit_gym_avatar) ImageView imgGym;
  //@BindView(R2.id.tv_recruit_gym_name) TextView tvGymName;
  //@BindView(R2.id.tv_address) TextView tvAddress;
  //@BindView(R2.id.img_right) ImageView imgRight;
  //@BindView(R2.id.toolbar) Toolbar toolbar;
  //@BindView(R2.id.toolbar_title) TextView toolbarTitile;
  //@BindView(R2.id.tv_position_name) TextView tvPositionName;
  //@BindView(R2.id.tv_salary) TextView tvSalary;
  //@BindView(R2.id.img_created_by) ImageView imgCreatedBy;
  //@BindView(R2.id.tv_created_by) TextView tvCreatedBy;
  //@BindView(R2.id.tv_starred) TextView tvStarred;
  //@BindView(R2.id.tv_position_crated_at) TextView tvPositionCratedAt;
  //@BindView(R2.id.tv_position_desc) TouchyWebView tvPositionDesc;
  //@BindView(R2.id.tv_position_require) TouchyWebView tvPositionRequire;
  //@BindView(R2.id.img_stared) ImageView imgStared;
  //@BindView(R2.id.btn_contact_him) Button btnContactHim;
  //@BindView(R2.id.btn_send_resume) Button btnSendResume;
  //@BindView(R2.id.layout_job_info) LinearLayout layoutJobInfo;
  //@BindView(R2.id.layout_employee_ctl) LinearLayout layoutEmployeeCtl;
  //@BindView(R2.id.layout_emloyer_ctl) LinearLayout layoutEmloyerCtl;
  //@BindView(R2.id.tv_dilive_positive) TextView tvDilivePositive;
  //@BindView(R2.id.tv_count_positive) TextView tvCountPositive;
  //@BindView(R2.id.tv_invited_position) TextView tvInvitedPositive;
  //@BindView(R2.id.tv_vp) TextView tvVp;
  //@BindView(R2.id.tv_sp) TextView tvSp;
  //@BindView(R2.id.tv_count_invited) TextView tvCountInvited;
  //@BindView(R2.id.btn_close_pos) Button btnClosePos;

  @Inject RecruitRouter router;
  @Inject JobPresenter presenter;
  @Inject ResumePresenter resumePresenter;
  @Inject QcRestRepository restRepository;


  private boolean isClosePosition;
  private boolean isShowShare;

  public static RecruitPositionDetailEmployerFragment newInstance(Job job) {
    Bundle args = new Bundle();
    args.putParcelable("job", job);
    RecruitPositionDetailEmployerFragment fragment = new RecruitPositionDetailEmployerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static RecruitPositionDetailEmployerFragment newInstance(Job job, boolean isShowShare) {
    Bundle args = new Bundle();
    args.putParcelable("job", job);
    args.putBoolean("isShowShare", isShowShare);
    RecruitPositionDetailEmployerFragment fragment = new RecruitPositionDetailEmployerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    job = getArguments().getParcelable("job");
    if (getArguments().containsKey("isShowShare")){
      isShowShare = getArguments().getBoolean("isShowShare", false);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    initdatabding(inflater, container);
    delegatePresenter(presenter, this);
    delegatePresenter(resumePresenter, this);
    if (isShowShare) {
      String title =
          job.gym != null ? job.gym.getBrand_name() + job.gym.getName() + "正在招聘" + job.name + "职位"
              : "";
      String content = "【薪资】"
          + RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary)
          + "\n【坐标】"
          + job.gym.getAddressStr();
      String pic = job.gym != null ? job.gym.photo
          : "http://zoneke-img.b0.upaiyun.com/977ad17699c4e4212b52000ed670091a.png";
      String url = restRepository.getHost() + "mobile/job/" + job.id + "/";
      RecruitPublishShareDialog fragment =
          RecruitPublishShareDialog.newInstance(title, content, pic, url);
      fragment.setCancelable(false);
      fragment.show(getChildFragmentManager(), null);
    }
    initToolbar(db.layoutToolbar.toolbar);
    db.layoutEmloyerCtl.setVisibility(View.VISIBLE);
    db.layoutEmployeeCtl.setVisibility(View.GONE);
    db.layoutJobInfo.setVisibility(View.VISIBLE);
    RxBusAdd(EventFreshJobsList.class).onBackpressureDrop()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventFreshJobsList>() {
          @Override public void call(EventFreshJobsList eventFreshJobsList) {
            presenter.queryStaffJob(job.id);
          }
        });
    db.btnClosePos.setOnClickListener(view1 -> onBtnClosePosClicked());
    db.btnEditPostion.setOnClickListener(view1 -> onBtnEditPostionClicked());
    db.layoutDiliverd.setOnClickListener(view1 -> onLayoutDiliverdClicked());
    db.layoutInvited.setOnClickListener(view1 -> onLayoutInvitedClicked());
    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    onJobDetail(job);
    onGym(job.gym);
    presenter.queryStaffJob(job.id);
  }

  @Override public void onJobDetail(Job job) {
    super.onJobDetail(job);
    /**
     * 招聘端 相关数据
     */
    db.btnClosePos.setText(job.published ? "关闭该职位" : "再次开启");
    db.tvDilivePositive.setCompoundDrawablesWithIntrinsicBounds(null, null,
        job.has_new_delivery ? ContextCompat.getDrawable(getContext(), R.drawable.red_dot) : null,
        null);
    db.tvInvitedPosition.setCompoundDrawablesWithIntrinsicBounds(null, null,
        job.has_new_invite ? ContextCompat.getDrawable(getContext(), R.drawable.red_dot) : null,
        null);
    db.tvCountInvited.setText(job.invitation_count + "");
    db.tvCountPositive.setText(job.delivery_count + "");
    db.tvVp.setText(job.view + "人查看");
    db.tvSp.setText(job.favorite_count + "人收藏");
  }

  @Override public String getFragmentName() {
    return RecruitPositionDetailEmployerFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 关闭职位
   */
  public void onBtnClosePosClicked() {
    DialogUtils.instanceDelDialog(getContext(), job.published ? "确定关闭该职位？" : "确定开启该职位",
        job.published ? "关闭后，该职位将不再收到简历" : "开启后，该职位将会收到投递简历",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            showLoading();
            isClosePosition = true;
            presenter.queryEditPermiss(job.gym.id, "job");
          }
        }).show();
  }

  @Override public void onEditOk() {
    hideLoading();
    presenter.queryJob(job.id);
    ToastUtils.show(R.drawable.vector_hook_white, job.published ? "已关闭" : "已开启");
  }

  @Override public void toEditJob() {
    if (!isClosePosition) {
      super.toEditJob();
    }else{
      isClosePosition = false;
      if (job.published) {
        presenter.editJob(job.id, new JobBody.Builder().published(false).build());
      } else {
        presenter.editJob(job.id, new JobBody.Builder().published(true).build());
      }
    }
  }

  /**
   * 编辑职位
   */
  public void onBtnEditPostionClicked() {
    presenter.queryEditPermiss(job.gym.id, "job");
  }

  /**
   * 主动投递
   */
  public void onLayoutDiliverdClicked() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    router.toRecieveResumes(job);
  }

  /**
   * 邀约列表
   */
  public void onLayoutInvitedClicked() {
    router.toInvitedResumes(job);
  }

  @Override public void showAlert(int res) {
    super.showAlert(res);
    hideLoading();
  }

  @Override public void showAlert(String res) {
    super.showAlert(res);
    hideLoading();
  }
}
