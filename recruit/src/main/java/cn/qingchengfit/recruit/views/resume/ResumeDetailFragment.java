package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import butterknife.Unbinder;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.views.JobSearchChatActivity;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CustomSwipeRefreshLayout;
import com.google.android.flexbox.BuildConfig;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tencent.qcloud.timchat.chatmodel.ResumeModel;
import com.tencent.smtt.sdk.WebView;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/7/6.
 */

@FragmentWithArgs
public class ResumeDetailFragment extends BaseFragment implements ResumePresenter.MVPView{

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.webview) WebView webview;
  @BindView(R2.id.refresh) CustomSwipeRefreshLayout refresh;
  @BindView(R2.id.img_stared) ImageView imgStared;
  @BindView(R2.id.tv_starred) TextView tvStarred;
  @BindView(R2.id.btn_starred) LinearLayout btnStarred;
  @BindView(R2.id.btn_contact_him) Button btnContactHim;
  @BindView(R2.id.btn_send_invite) Button btnSendInvite;
  @BindView(R2.id.layout_employee_ctl) LinearLayout layoutEmployeeCtl;

  @Inject ResumePresenter resumePresenter;
  @Arg String resumeId = "gmqPQM30pyJn1Dw61Z8VjO6x7RAz5Wod";

  private ResumeModel resumeModel;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(resumePresenter, this);
    setToolbar();
    resumePresenter.getResumeDetail(resumeId);
    return view;
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbarTitle.setText("简历详情");
  }

  private void initView(){
    webview.loadUrl("");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.img_stared)
  public void onStared(){
  }

  @OnClick(R2.id.btn_contact_him)
  public void onContact(){
    Intent intent = new Intent(getActivity(), JobSearchChatActivity.class);
    Gson gson = new Gson();
    if (resumeModel == null){
      ToastUtils.show("请稍后重试");
      return;
    }
    String resumeStr = "{userAction:1004, data:"
        + gson.toJson(resumeModel)
        + "}";
    //TODO 需要获取userid 的接口
    //intent.putExtra(RecruitConstants.IDENTIFY, (BuildConfig.DEBUG ? "qctest_" : "qc_") + job.created_by.id);
    intent.putExtra(RecruitConstants.TEMP_CONVERSATION_TYPE, RecruitConstants.C2C);
    intent.putExtra(RecruitConstants.CHAT_JOB_RESUME, resumeStr);
    //intent.putExtra(RecruitConstants.CHAT_JOB_ID, job.id);
    //intent.putExtra(RecruitConstants.CHAT_RECRUIT, jobStr);
    intent.putExtra(RecruitConstants.CHAT_JOB_SEARCH_OR_RECRUIT, RecruitConstants.RECRUIT);
    //intent.putExtra(RecruitConstants.CHAT_RECRUIT_STATE, job.deliveried);
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @OnClick(R2.id.btn_send_invite)
  public void onSendResume(){

  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {
    resumeModel = resumePresenter.dealResumeMessage(resumeHome);
  }

  @Override public void onWorkExpList(List<WorkExp> workExps) {

  }

  @Override public void onEduExpList(List<Education> eduExps) {

  }

  @Override public void onCertiList(List<Certificate> certificates) {

  }
}
