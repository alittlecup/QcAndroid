package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.event.EventTabHeaderChange;
import cn.qingchengfit.recruit.item.ResumeItem;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.presenter.ResumeMarketPresenter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
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
 * Created by Paper on 2017/7/4.
 */

public class ResumeHandleFragment extends ResumeListFragment {
  @Inject ResumeMarketPresenter presenter;
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
    delegatePresenter(presenter, this);
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
    if (resumes != null) {
      if (page == 1) {
        commonFlexAdapter.clear();
        if (resumes.size() == 0) {
          hasItem = false;
          setDatas(new ArrayList<AbstractFlexibleItem>(), 1);
        } else {
          hasItem = true;
        }
      }
      commonFlexAdapter.setEndlessTargetCount(total);
      for (Resume resume : resumes) {
        commonFlexAdapter.addItem(new ResumeItem(resume));
      }
    }
    RxBus.getBus().post(new EventTabHeaderChange());
    stopLoadMore();
  }

  public void setChooseMode(boolean canChoose) {
    commonFlexAdapter.setStatus(canChoose ? -1 : 0);
    commonFlexAdapter.clearSelection();
    commonFlexAdapter.setMode(SelectableAdapter.MODE_MULTI);
    commonFlexAdapter.notifyDataSetChanged();
  }

  public List<String> getChooseIds() {
    List<String> ret = new ArrayList<>();
    for (Integer integer : commonFlexAdapter.getSelectedPositions()) {
      ret.add(((ResumeItem) commonFlexAdapter.getItem(integer)).getResume().id);
    }
    return ret;
  }

}
