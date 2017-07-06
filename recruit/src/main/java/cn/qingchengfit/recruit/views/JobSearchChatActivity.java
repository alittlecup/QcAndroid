package cn.qingchengfit.recruit.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import com.tencent.qcloud.timchat.ui.qcchat.ChatActivity;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

import static android.view.View.GONE;

/**
 * Created by fb on 2017/7/6.
 */

public class JobSearchChatActivity extends ChatActivity implements JobPresenter.MVPView,
    HasActivityInjector {

  @Inject DispatchingAndroidInjector<Activity> dispatchFragmentInjector;

  @Inject
  JobPresenter presenter;
  private int type;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);
    presenter.attachView(this);
    type = getIntent().getIntExtra(RecruitConstants.CHAT_JOB_SEARCH_OR_RECRUIT,0);
    initView();
  }

  private void initView() {
    chatSendResume.setVisibility(GONE);
    tvSendResume.setVisibility(GONE);
    if (getIntent().getBooleanExtra(RecruitConstants.CHAT_RECRUIT_STATE, false)){
      chatSendResume.setVisibility(View.VISIBLE);
      tvSendResume.setVisibility(View.VISIBLE);
      switch (type){
        case RecruitConstants.JOB_SEARCH:
          tvSendResume.setText("投递简历");
          break;
        case RecruitConstants.RECRUIT:
          tvSendResume.setText("发送邀约");
          break;
        default:
          break;
      }
    }else{
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
    if (type == RecruitConstants.RECRUIT){

    }
  }

  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {

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
    }
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchFragmentInjector;
  }
}
