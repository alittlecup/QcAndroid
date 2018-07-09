package cn.qingchengfit.student.view.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.student.R;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

public class SalerStudentListView extends SaasCommonFragment {
  RecyclerView root;
  CommonFlexAdapter adapter;
  FlexibleAdapter.OnItemClickListener listener;
  List<? extends AbstractFlexibleItem> items;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    root = (RecyclerView) inflater.inflate(R.layout.empty_recyclerview, container, false);
    initRecyclerView();
    return root;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  public void setItems(List<? extends AbstractFlexibleItem> items) {
    if (adapter != null) {
      adapter.updateDataSet(items);
    } else {
      this.items = items;
    }
  }

  public boolean isEmpty() {
    if (adapter == null) {
      return items == null || items.isEmpty();
    } else {
      return adapter.isEmpty();
    }
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(items == null ? new ArrayList() : items);
    root.setAdapter(adapter);
    root.setLayoutManager(new LinearLayoutManager(getContext()));
    if (listener != null) {
      adapter.addListener(listener);
    }
  }

  public void setOnItemClickListener(FlexibleAdapter.OnItemClickListener listener) {
    if (adapter != null) {
      adapter.addListener(listener);
    } else {
      this.listener = listener;
    }
  }
}
