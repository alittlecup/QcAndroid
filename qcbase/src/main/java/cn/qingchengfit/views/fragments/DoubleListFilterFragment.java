package cn.qingchengfit.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.items.DoubleListFilterLeftItem;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.databinding.ViewDoubleListBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/23.
 */

public class DoubleListFilterFragment extends Fragment {

  private CommonFlexAdapter adapterRight;
  private CommonFlexAdapter adapterLeft;
  private List<IDoubleListData> datas = new ArrayList<>();
  private ViewDoubleListBinding mBinding;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.view_double_list, container, false);
    adapterLeft = new CommonFlexAdapter(new ArrayList(), leftClickListener);
    adapterRight = new CommonFlexAdapter(new ArrayList(), rightClickListener);
    adapterLeft.setMode(SelectableAdapter.Mode.SINGLE);
    adapterRight.setMode(SelectableAdapter.Mode.SINGLE);
    mBinding.rvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.rvLeft.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
    mBinding.rvRight.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
    mBinding.rvLeft.setAdapter(adapterLeft);
    mBinding.rvRight.setAdapter(adapterRight);
    updateLeftUI(datas);
    mBinding.btnReset.setOnClickListener(v -> {
      adapterLeft.clearSelection();
      adapterRight.clearSelection();
      adapterLeft.notifyDataSetChanged();
      adapterRight.notifyDataSetChanged();
    });
    mBinding.btnConfirm.setOnClickListener(v -> {
      if (doubleListSelect != null) {
        List<Integer> lefeSelection = adapterLeft.getSelectedPositions();
        List<Integer> rightSelection = adapterRight.getSelectedPositions();
        if (lefeSelection != null
            && !lefeSelection.isEmpty()
            && rightSelection != null
            && !rightSelection.isEmpty()) {

          doubleListSelect.onPositionSelected(adapterLeft.getSelectedPositions().get(0),
              adapterRight.getSelectedPositions().get(0));
        } else {
          doubleListSelect.onPositionSelected(-1, -1);
        }
      }
    });
    return mBinding.getRoot();
  }

  public void setDatas(List<IDoubleListData> datas) {
    this.datas.clear();
    this.datas.addAll(datas);
    updateLeftUI(this.datas);
  }

  private void updateLeftUI(List<IDoubleListData> datas) {
    if (adapterLeft == null) return;
    adapterLeft.clear();
    adapterLeft.clearSelection();
    List<DoubleListFilterLeftItem> items = new ArrayList<>();
    for (IDoubleListData data : datas) {
      items.add(new DoubleListFilterLeftItem(data.getText()));
    }
    adapterLeft.updateDataSet(items);
  }

  private void updateRightUI(List<String> datas) {
    if (adapterRight == null) return;
    adapterRight.clear();
    adapterRight.clearSelection();
    List<FilterCommonLinearItem> items = new ArrayList<>();
    for (String data : datas) {
      if (!TextUtils.isEmpty(data)) {
        items.add(new FilterCommonLinearItem(data));
      }
    }
    adapterRight.updateDataSet(items);
  }

  private FlexibleAdapter.OnItemClickListener leftClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          adapterLeft.toggleSelection(position);
          adapterLeft.notifyDataSetChanged();
          updateRightUI(datas.get(position).getChildText());
          return true;
        }
      };
  private FlexibleAdapter.OnItemClickListener rightClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          adapterRight.toggleSelection(position);
          adapterRight.notifyDataSetChanged();

          return true;
        }
      };

  public interface IDoubleListData {
    String getText();

    List<String> getChildText();
  }

  public void setDoubleListSelectListener(onDoubleListSelectListener doubleListSelect) {
    this.doubleListSelect = doubleListSelect;
  }

  private onDoubleListSelectListener doubleListSelect;

  public interface onDoubleListSelectListener {
    void onPositionSelected(int leftPos, int rightPos);
  }
}
