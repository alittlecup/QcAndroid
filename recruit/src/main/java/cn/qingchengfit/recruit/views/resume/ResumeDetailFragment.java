package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.item.RecruitPositionChooseItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.recruit.presenter.ResumePermissionPresenter;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.views.JobSearchChatActivity;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.views.fragments.WebFragment;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tencent.qcloud.timchat.chatmodel.ResumeModel;
import com.tencent.qcloud.timchat.ui.qcchat.AddConversationProcessor;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/7/6.
 */

@FragmentWithArgs public class ResumeDetailFragment extends BaseFragment
    implements ResumePresenter.MVPView, JobPresenter.MVPView,
    BottomListFragment.ComfirmChooseListener, ResumePermissionPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.img_stared) ImageView imgStared;
  @BindView(R2.id.tv_starred) TextView tvStarred;
  @BindView(R2.id.btn_starred) LinearLayout btnStarred;
  @BindView(R2.id.btn_contact_him) Button btnContactHim;
  @BindView(R2.id.btn_send_invite) Button btnSendInvite;
  @BindView(R2.id.layout_employee_ctl) LinearLayout layoutEmployeeCtl;
  @BindView(R2.id.frag_resume_layout) FrameLayout fragResumeLayout;

  @Inject ResumePresenter resumePresenter;
  @Inject JobPresenter jobPresenter;
  @Inject ResumePermissionPresenter permissionPresenter;
  @Inject GymWrapper gymWrapper;

  @Arg String resumeId;
  @Arg String toUrl;

  private ResumeModel resumeModel;
  private String userId;
  private boolean isStarred;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeDetailFragmentBuilder.injectArguments(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(resumePresenter, this);
    delegatePresenter(jobPresenter, this);
    if (!toUrl.startsWith("http")) toUrl = "http://" + toUrl;
    toUrl = toUrl + resumeId;
    setToolbar();
    initView();
    resumePresenter.getResumeDetail(resumeId);
    return view;
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText("简历详情");
    toolbar.inflateMenu(R.menu.menu_share);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (resumeModel != null) {
          ShareDialogFragment.newInstance(resumeModel.username + "的简历",
              "期望职位: " + resumeModel.exp_job, resumeModel.avatar, toUrl)
              .show(getChildFragmentManager(), null);
        }
        return false;
      }
    });
  }

  private void initView() {
    WebFragment webFragment = WebFragment.newInstance(toUrl, true);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_resume_layout, webFragment)
        .commit();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.btn_starred) public void onStared() {
    if (isStarred) {
      resumePresenter.unStarResume(resumeId);
    } else {
      resumePresenter.starResume(resumeId);
    }
  }

  @OnClick(R2.id.btn_contact_him) public void onContact() {
    Intent intent = new Intent(getActivity(), JobSearchChatActivity.class);
    Gson gson = new Gson();
    if (resumeModel == null) {
      ToastUtils.show("请稍后重试");
      return;
    }
    String resumeStr = "{\"userAction\":1004, \"data\":" + gson.toJson(resumeModel) + "}";
    //TODO 需要获取userid 的接口
    intent.putExtra(RecruitConstants.IDENTIFY, ("qctest_") + userId);
    intent.putExtra(RecruitConstants.TEMP_CONVERSATION_TYPE, RecruitConstants.C2C);
    intent.putExtra(RecruitConstants.CHAT_JOB_RESUME, resumeStr);
    intent.putExtra(JobSearchChatActivity.INVITE_RESUME_ID, resumeId);
    intent.putExtra(RecruitConstants.SEND_RESUME, true);            //是否是发送简历
    //intent.putExtra(RecruitConstants.CHAT_JOB_ID, job.id);
    //intent.putExtra(RecruitConstants.CHAT_RECRUIT, jobStr);
    intent.putExtra(RecruitConstants.CHAT_JOB_SEARCH_OR_RECRUIT, RecruitConstants.RECRUIT);
    intent.putExtra(RecruitConstants.CHAT_RECRUIT_STATE, true);
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @OnClick(R2.id.btn_send_invite) public void onSendInvite() {
    permissionPresenter.queryChangeStatePermission(gymWrapper.id(), "resume");
  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {
    userId = resumeHome.user_id;
    isStarred = resumeHome.favorited;
    resumeModel = resumePresenter.dealResumeMessage(resumeHome);
  }

  @Override public void onWorkExpList(List<WorkExp> workExps) {

  }

  @Override public void onEduExpList(List<Education> eduExps) {

  }

  @Override public void onCertiList(List<Certificate> certificates) {

  }

  //收藏简历成功回调
  @Override public void starOk() {
    isStarred = true;
    ToastUtils.show("收藏成功");
    tvStarred.setText("已收藏");
    imgStared.setImageResource(R.drawable.vd_recruit_job_starred);
  }

  //取消收藏简历成功回调
  @Override public void unStartOk() {
    isStarred = false;
    tvStarred.setText("取消收藏");
    imgStared.setImageResource(R.drawable.vd_recruit_job_star);
  }

  @Override public void onJobList(List<Job> jobList) {
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("选择邀约职位", 2);
    bottomListFragment.setListener(this);
    List<AbstractFlexibleItem> itemList = new ArrayList<>();
    for (Job job : jobList) {
      itemList.add(new RecruitPositionChooseItem(job));
    }
    bottomListFragment.loadData(itemList);
    bottomListFragment.show(getChildFragmentManager(), null);
  }

  @Override public void onGymDetail(Gym gym) {

  }

  @Override public void onEditOk() {

  }

  @Override public void onJobDetail(Job job) {

  }

  @Override public void starOK() {

  }

  @Override public void unStarOk() {
  }

  @Override public void onPostResumeOk() {

  }

  @Override public void onInviteOk() {
    ToastUtils.show(R.drawable.vector_hook_white, "已发邀约");
  }

  @Override public void toEditJob() {

  }

  @Override public void onComfirmClick(List<IFlexible> dats) {
    List<String> jobList = new ArrayList<>();
    for (IFlexible item : dats) {
      if (item instanceof RecruitPositionChooseItem) {
        Job job = ((RecruitPositionChooseItem) item).getJob();
        if (job != null) {
          jobList.add(job.id);
          Gson gson = new Gson();
          AddConversationProcessor addConversationProcessor =
              new AddConversationProcessor(getContext().getApplicationContext());
          String recruit = "{\"userAction\":1003, \"data\":"
              + gson.toJson(jobPresenter.getRecruitModel(job))
              + "}";
          addConversationProcessor.sendResumeOrRecruit(("qctest_") + userId, "", recruit);
        }
      }
    }
    jobPresenter.invitePosition(jobList, resumeId);
  }

  @Override public void onCheckSuccess() {
    jobPresenter.getInviteJobs("");
  }
}
