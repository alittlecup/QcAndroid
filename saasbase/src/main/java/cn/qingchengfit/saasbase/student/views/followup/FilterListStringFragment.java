package cn.qingchengfit.saasbase.student.views.followup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.FilterFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FilterListStringFragment extends FilterFragment {
  private String[] datas;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (commonFlexAdapter == null) {
      commonFlexAdapter = new CommonFlexAdapter(new ArrayList());
    }
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rlPopWindowCommon.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
  }

  public void setStrings(@NonNull String[] strings) {
    List<FilterCommonLinearItem> items = new ArrayList<>(strings.length);
    datas = strings;
    for (String msg : strings) {
      items.add(new FilterCommonLinearItem(msg));
    }
    if (!items.isEmpty()) {
      if(commonFlexAdapter==null)commonFlexAdapter=new CommonFlexAdapter(items);
      commonFlexAdapter.updateDataSet(items);
    }
  }

  public int getItemCount() {
    return commonFlexAdapter.getItemCount();
  }

  public IFlexible getDataAtPosition(int position) {
    return commonFlexAdapter.getItem(position);
  }

  public void set(int position) {
    commonFlexAdapter.addSelection(position);
  }
}
