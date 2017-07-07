package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.recruit.model.Resume;
import java.util.List;

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
 * Created by Paper on 2017/7/4.
 */

public class ResumeHandleFragment extends ResumeListFragment {
  private int type;// 0 是主动投递的  1是我邀约的
  private int status;
  private String jobId;

  public static ResumeHandleFragment newRecieved(int status, String jobid) {
    Bundle args = new Bundle();
    args.putInt("type", 0);
    args.putInt("status", status);
    args.putString("jobid", jobid);
    ResumeHandleFragment fragment = new ResumeHandleFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static ResumeHandleFragment newInvited(int status, String jobid) {
    Bundle args = new Bundle();
    args.putInt("type", 1);
    args.putInt("status", status);
    args.putString("jobid", jobid);
    ResumeHandleFragment fragment = new ResumeHandleFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getInt("type");
    status = getArguments().getInt("status");
    jobId = getArguments().getString("jobid");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    onRefresh();
    return v;
  }

  @Override public void onRefresh() {
    initLoadMore();
    if (type == 0) {
      presenter.queryRecieveResume(true, jobId, status);
    } else {
      presenter.queryInvitedResume(true, jobId, status);
    }
  }

  @Override public void onLoadMore(int i, int i1) {
    if (type == 0) {
      presenter.queryRecieveResume(false, jobId, status);
    } else {
      presenter.queryInvitedResume(false, jobId, status);
    }
  }

  @Override public void onResumeList(List<Resume> resumes, int total, int page) {
    super.onResumeList(resumes, total, page);
    stopLoadMore();
  }
}
