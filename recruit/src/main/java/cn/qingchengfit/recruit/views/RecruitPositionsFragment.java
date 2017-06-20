package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/5/23.
 */
public class RecruitPositionsFragment extends VpFragment {

<<<<<<< HEAD
  @BindView(R2.id.recycleview) RecyclerView recycleview;
  CommonFlexAdapter commonFlexAdapter;
  List<AbstractFlexibleItem> items = new ArrayList<>();
  Object listener;
  ProgressItem progressItem;

  @Inject public RecruitPositionsFragment() {
  }

  @Override public String getTitle() {
    return "招聘职位";
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    progressItem = new ProgressItem(getContext());
    commonFlexAdapter = new CommonFlexAdapter(items, listener);
    if (listener instanceof FlexibleAdapter.EndlessScrollListener) {
      commonFlexAdapter.setEndlessScrollListener((FlexibleAdapter.EndlessScrollListener) listener, progressItem)
          .setEndlessScrollThreshold(6);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_position_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
    recycleview.setAdapter(commonFlexAdapter);
    return view;
  }

  public void setTotalCount(int totalCount) {
    if (commonFlexAdapter != null) commonFlexAdapter.setEndlessTargetCount(totalCount);
  }

  public void setData(List<Job> datas) {
    commonFlexAdapter.clear();
    if (datas != null) {
      for (Job j : datas) {
        commonFlexAdapter.addItem(generatItem(j));
      }
    }
    if (datas.size() == 0) commonFlexAdapter.addItem(new CommonNoDataItem(getNoDataRes(), getNoDataStr()));
  }

  public void stopLoadMore() {
    if (commonFlexAdapter == null) return;
    commonFlexAdapter.removeItem(commonFlexAdapter.getGlobalPositionOf(progressItem));
    commonFlexAdapter.onLoadMoreComplete(null);
  }

  public void addData(List<Job> datas) {
    if (commonFlexAdapter == null) return;
    List<IFlexible> items = new ArrayList<>();
    for (Job j : datas) {
      items.add(generatItem(j));
    }
    commonFlexAdapter.onLoadMoreComplete(items);
  }

  public Object getListener() {
    return listener;
  }

  public void setListener(Object listener) {
    this.listener = listener;
    if (commonFlexAdapter != null) commonFlexAdapter.addListener(listener);
  }

  public IFlexible getItem(int pos) {
    if (commonFlexAdapter != null && pos < commonFlexAdapter.getItemCount()) {
      return commonFlexAdapter.getItem(pos);
    } else {
      return null;
    }
  }

  @Override public String getFragmentName() {
    return RecruitPositionsFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  protected AbstractFlexibleItem generatItem(Job job) {
    return new RecruitPositionItem(job);
  }

  protected int getNoDataRes() {
    return 0;
  }

  protected String getNoDataStr() {
    return "";
  }
=======
    @BindView(R2.id.recycleview) RecyclerView recycleview;
    CommonFlexAdapter commonFlexAdapter;
    List<AbstractFlexibleItem> items = new ArrayList<>();
    Object listener;
    ProgressItem progressItem;

    @Inject public RecruitPositionsFragment() {
    }

    @Override public boolean isBlockTouch() {
        return false;
    }

    @Override public String getTitle() {
        return "招聘职位";
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressItem = new ProgressItem(getContext());
        commonFlexAdapter = new CommonFlexAdapter(items, listener);
        if (listener instanceof FlexibleAdapter.EndlessScrollListener) {
            commonFlexAdapter.setEndlessScrollListener((FlexibleAdapter.EndlessScrollListener) listener, progressItem)
                .setEndlessScrollThreshold(6);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruit_position_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.setAdapter(commonFlexAdapter);
        return view;
    }

    public void setTotalCount(int totalCount) {
        if (commonFlexAdapter != null) commonFlexAdapter.setEndlessTargetCount(totalCount);
    }

    public void setData(List<Job> datas) {
        commonFlexAdapter.clear();
        if (datas != null) {
            for (Job j : datas) {
                commonFlexAdapter.addItem(generatItem(j));
            }
        }
        if (datas.size() == 0) commonFlexAdapter.addItem(new CommonNoDataItem(getNoDataRes(), getNoDataStr()));
    }

    public void stopLoadMore() {
        if (commonFlexAdapter == null) return;
        commonFlexAdapter.removeItem(commonFlexAdapter.getGlobalPositionOf(progressItem));
        commonFlexAdapter.onLoadMoreComplete(null);
    }

    public void addData(List<Job> datas) {
        if (commonFlexAdapter == null) return;
        List<IFlexible> items = new ArrayList<>();
        for (Job j : datas) {
            items.add(generatItem(j));
        }
        commonFlexAdapter.onLoadMoreComplete(items);
    }

    public Object getListener() {
        return listener;
    }

    public void setListener(Object listener) {
        this.listener = listener;
        if (commonFlexAdapter != null) commonFlexAdapter.addListener(listener);
    }

    public IFlexible getItem(int pos) {
        if (commonFlexAdapter != null && pos < commonFlexAdapter.getItemCount()) {
            return commonFlexAdapter.getItem(pos);
        } else {
            return null;
        }
    }

    @Override public String getFragmentName() {
        return RecruitPositionsFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    protected AbstractFlexibleItem generatItem(Job job) {
        return new RecruitPositionItem(job);
    }

    protected int getNoDataRes() {
        return 0;
    }

    protected String getNoDataStr() {
        return "";
    }
>>>>>>> b5b0c51f4a1d5648c96eb911660d28e2a6be34f1
}
