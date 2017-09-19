package cn.qingchengfit.recruit.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.item.RecruitPositionChooseItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.views.fragments.BottomListFragment;
import com.google.gson.Gson;
import com.tencent.qcloud.timchat.ui.qcchat.ChatActivity;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static android.view.View.GONE;

/**
 * Created by fb on 2017/7/6.
 */

public class JobSearchChatActivity extends ChatActivity
    implements JobPresenter.MVPView, HasActivityInjector,
    BottomListFragment.ComfirmChooseListener {

  public static final String INVITE_RESUME_ID = "resume_id";
  @Inject DispatchingAndroidInjector<Activity> dispatchFragmentInjector;

  @Inject JobPresenter presenter;
  private int type;
  private String resumeId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);
    presenter.attachView(this);
    type = getIntent().getIntExtra(RecruitConstants.CHAT_JOB_SEARCH_OR_RECRUIT, 0);
    if (!TextUtils.isEmpty(getIntent().getStringExtra(INVITE_RESUME_ID))) {
      resumeId = getIntent().getStringExtra(INVITE_RESUME_ID);
    }
    initView();
  }

  private void initView() {
    chatSendResume.setVisibility(GONE);
    tvSendResume.setVisibility(GONE);
    if (getIntent().getBooleanExtra(RecruitConstants.CHAT_RECRUIT_STATE, false)) {
      chatSendResume.setVisibility(View.VISIBLE);
      tvSendResume.setVisibility(View.VISIBLE);
      switch (type) {
        case RecruitConstants.JOB_SEARCH:
          tvSendResume.setText("投递简历");
          break;
        case RecruitConstants.RECRUIT:
          tvSendResume.setText("发送邀约");
          break;
        default:
          break;
      }
    } else {
      chatSendResume.setVisibility(GONE);
    }
  }

  @Override public void sendResume() {
    //发送简历
    if (type == RecruitConstants.JOB_SEARCH) {
      super.sendResume();
      String jobId = getIntent().getStringExtra(RecruitConstants.CHAT_JOB_ID);
      if (!TextUtils.isEmpty(jobId)) {
        presenter.sendResume(jobId);
      }
    }
    //发起邀约
    if (type == RecruitConstants.RECRUIT) {
      presenter.getInviteJobs(null);
    }
  }

  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {

  }

  @Override public void showAlert(String s) {

  }

  @Override public void showAlert(@StringRes int s) {

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
    if (type == RecruitConstants.JOB_SEARCH) {
      chatSendResume.setVisibility(GONE);
      tvSendResume.setVisibility(GONE);
    }
  }

  @Override public void onInviteOk() {
  }

  @Override public void toEditJob() {

  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchFragmentInjector;
  }

  @Override public void onJobList(List<Job> jobList) {
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("选择邀约职位", 2);
    bottomListFragment.setListener(this);
    List<AbstractFlexibleItem> itemList = new ArrayList<>();
    for (Job job : jobList) {
      itemList.add(new RecruitPositionChooseItem(job));
    }
    bottomListFragment.loadData(itemList);
    bottomListFragment.show(getSupportFragmentManager(), null);
  }

  @Override public void onGymDetail(Gym gym) {

  }

  @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> poss) {
    List<String> jobList = new ArrayList<>();
    for (IFlexible item : dats) {
      if (item instanceof RecruitPositionChooseItem) {
        Job job = ((RecruitPositionChooseItem) item).getJob();
        if (job != null) {
          jobList.add(job.id);
          Gson gson = new Gson();
          String recruit =
              "{userAction:1003, data:" + gson.toJson(presenter.getRecruitModel(job)) + "}";
          sendRercuitMessage(recruit);
        }
      }
    }
    presenter.invitePosition(jobList, resumeId);
  }
}
