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



import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionChooseItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.views.JobSearchChatActivity;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.views.fragments.TipDialogFragment;
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
    BottomListFragment.ComfirmChooseListener, TipDialogFragment.OnDialogListener {

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	ImageView imgStared;
	TextView tvStarred;
	LinearLayout btnStarred;
	Button btnContactHim;
	Button btnSendInvite;
	LinearLayout layoutEmployeeCtl;
	FrameLayout fragResumeLayout;

  @Inject ResumePresenter resumePresenter;
  @Inject JobPresenter jobPresenter;
  @Inject RecruitRouter router;
  @Inject LoginStatus loginStatus;

  @Arg String resumeId;
  @Arg String toUrl;
  @Arg(required = false) JobFair jobFair;

  private ResumeModel resumeModel;
  private String userId;
  private boolean isStarred;
  private ResumeHome resumeHome;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeDetailFragmentBuilder.injectArguments(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_detail, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    imgStared = (ImageView) view.findViewById(R.id.img_stared);
    tvStarred = (TextView) view.findViewById(R.id.tv_starred);
    btnStarred = (LinearLayout) view.findViewById(R.id.btn_starred);
    btnContactHim = (Button) view.findViewById(R.id.btn_contact_him);
    btnSendInvite = (Button) view.findViewById(R.id.btn_send_invite);
    layoutEmployeeCtl = (LinearLayout) view.findViewById(R.id.layout_employee_ctl);
    fragResumeLayout = (FrameLayout) view.findViewById(R.id.frag_resume_layout);
    view.findViewById(R.id.btn_starred).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onStared();
      }
    });
    view.findViewById(R.id.btn_contact_him).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onContact();
      }
    });
    view.findViewById(R.id.btn_send_invite).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSendInvite();
      }
    });

    delegatePresenter(resumePresenter, this);
    delegatePresenter(jobPresenter, this);
    if (!toUrl.startsWith("http")) toUrl = "http://" + toUrl;
    toUrl = toUrl + resumeId;
    setToolbar();
    initView();
    if (jobFair != null) {
      resumePresenter.getResumeDetail(resumeId, jobFair.id);
    }else {
      resumePresenter.getResumeDetail(resumeId, "");
    }
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

 public void onStared() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    if(resumeHome != null && !resumeHome.sign_up){
      showTips();
      return;
    }
    if (isStarred) {
      resumePresenter.unStarResume(resumeId);
    } else {
      resumePresenter.starResume(resumeId);
    }
  }

 public void onContact() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    if(resumeHome != null && !resumeHome.sign_up){
      showTips();
      return;
    }
    Intent intent = new Intent(getActivity(), JobSearchChatActivity.class);
    Gson gson = new Gson();
    if (resumeModel == null) {
      ToastUtils.show("请稍后重试");
      return;
    }
    String resumeStr = "{\"userAction\":1004, \"data\":" + gson.toJson(resumeModel) + "}";
    intent.putExtra(RecruitConstants.IDENTIFY, getString(R.string.chat_user_id_header,userId));
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

 public void onSendInvite() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    if (resumeHome != null && !resumeHome.sign_up){
      showTips();
      return;
    }
    jobPresenter.getInviteJobs(jobFair == null ? null : jobFair.id);
  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {
    this.resumeHome = resumeHome;
    userId = resumeHome.user_id;
    isStarred = resumeHome.favorited;
    tvStarred.setText(isStarred ? "已收藏" : "收藏简历");
    imgStared.setImageResource(
        isStarred ? R.drawable.vd_recruit_job_starred : R.drawable.vd_recruit_job_star);
    resumeModel = resumePresenter.dealResumeMessage(resumeHome);
  }

  private void showTips(){
    TipDialogFragment dialogFragment =
        TipDialogFragment.newInstance(getString(R.string.tips_not_join_fair), "申请参加招聘会",
            R.drawable.ic_dialog_hire_warning);
    dialogFragment.setCancelable(false);
    dialogFragment.setOnDialogListener(this);
    dialogFragment.show(getChildFragmentManager(), null);
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
    tvStarred.setText("收藏简历");
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

  @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> poss) {
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
          addConversationProcessor.sendResumeOrRecruit(getString(R.string.chat_user_id_header, userId), "", recruit);
        }
      }
    }
    jobPresenter.invitePosition(jobList, resumeId);
  }

  @Override public void onDoClick(View v) {
    router.toSignUpFair(jobFair);
  }

  @Override public void onDismissListener() {

  }
}
