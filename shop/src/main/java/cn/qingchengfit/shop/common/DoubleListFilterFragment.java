package cn.qingchengfit.shop.common;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ViewDoubleListBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
    mBinding.rvLeft.addItemDecoration(new FlexibleItemDecoration(getContext()).withDivider(
        cn.qingchengfit.saasbase.R.drawable.divider_qc_base_line).withBottomEdge(true));
    mBinding.rvRight.addItemDecoration(new FlexibleItemDecoration(getContext()).withDivider(
        cn.qingchengfit.saasbase.R.drawable.divider_grey_left_margin).withBottomEdge(true));
    mBinding.rvLeft.setAdapter(adapterLeft);
    mBinding.rvRight.setAdapter(adapterRight);
    updateLeftUI(datas);
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
      items.add(new FilterCommonLinearItem(data));
    }
    adapterRight.updateDataSet(items);
  }

  private FlexibleAdapter.OnItemClickListener leftClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          adapterLeft.toggleSelection(position);
          adapterLeft.notifyItemChanged(position);
          updateRightUI(datas.get(position).getChildText());
          return true;
        }
      };
  private FlexibleAdapter.OnItemClickListener rightClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          adapterRight.toggleSelection(position);
          adapterRight.notifyDataSetChanged();
          //int x = 0;
          //try {
          //  x = adapterLeft.getSelectedPositions().get(0);
          //} catch (Exception e) {
          //
          //}
          //IFlexible item = adapterRight.getItem(position);
          //if (item instanceof FilterCommonLinearItem) {
          //  RxBus.getBus().post(mAllDatas.get(cardCategory[x]).get(position));
          //}
          if (doubleListSelect != null) {
            doubleListSelect.onPositionSelected(adapterLeft.getSelectedPositions().get(0),
                adapterRight.getSelectedPositions().get(0));
          }
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
