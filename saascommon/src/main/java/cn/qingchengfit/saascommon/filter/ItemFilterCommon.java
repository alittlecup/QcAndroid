package cn.qingchengfit.saascommon.filter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.filter.model.Content;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.widgets.CheckBoxButton;
import cn.qingchengfit.widgets.QcAutoLineRadioGroup;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/10/12.
 */

public class ItemFilterCommon extends AbstractFlexibleItem<ItemFilterCommon.ItemFilterVH> {

  private FilterModel filterModel;
  private List<Integer> selectPosition = new ArrayList<>();
  private List<String> valueList = new ArrayList<>();
  private Boolean isSingleSelected=false;

  public ItemFilterCommon(FilterModel filterModel) {
    this.filterModel = filterModel;
  }

  public ItemFilterCommon(FilterModel filterModel,Boolean isSingleSelected) {
    this.filterModel = filterModel;
    this.isSingleSelected=isSingleSelected;
  }


  @Override public ItemFilterVH createViewHolder(View view, FlexibleAdapter adapter) {
    ItemFilterVH holder =
        new ItemFilterVH(view, adapter);
    return holder;
  }

  public FilterModel getData() {
    return filterModel;
  }

  public List<Content> getCheckedContent() {
    List<Content> tempList = new ArrayList<>();
    for (int position : selectPosition) {
      tempList.add(filterModel.content.get(position));
    }
    return tempList;
  }

  public List<Integer> getSelectPosition() {
    return selectPosition;
  }

  public void setValueList(List<String> valueList) {
    this.valueList = valueList;
  }

  public void setSelectPosition(List<Integer> selectPosition) {
    this.selectPosition = selectPosition;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemFilterVH holder, int position,
      List payloads) {
    if (adapter.isSelected(position)){
      holder.billFilterCommon.clearCheck();
      adapter.removeSelection(position);
      return;
    }
    holder.billFilterCommon.setSingleSelected(isSingleSelected);
    holder.billFilterTitle.setText(filterModel.name);
    if (holder.billFilterCommon.getChildCount()  >= filterModel.content.size()){
      return;
    }
    boolean isEmpty = false;
    for (int i = 0; i < filterModel.content.size(); i++) {
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT);

      if (!isEmpty && valueList.size() > 0){
        if (valueList.contains(filterModel.content.get(i).value)){
          selectPosition.add(i);
        }
      }else{
        isEmpty = true;
      }
      CheckBoxButton button = new CheckBoxButton(holder.itemView.getContext());
      button.setContent(filterModel.content.get(i).name);

      button.setLayoutParams(params);
      holder.billFilterCommon.addChild(button);
    }
    if (selectPosition.size() > 0){
      for (int p : selectPosition) {
        holder.billFilterCommon.setCheckPos(p);
      }
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_common;
  }

  class ItemFilterVH extends FlexibleViewHolder {

	QcAutoLineRadioGroup billFilterCommon;
	TextView billFilterTitle;

    public ItemFilterVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      billFilterCommon = view.findViewById(R.id.filter_common);
      billFilterTitle = view.findViewById(R.id.filter_title);

      billFilterCommon.setSingleSelected(false);
      billFilterCommon.setOnCheckoutPositionListener(checkedList -> {
        IFlexible item = adapter.getItem(getAdapterPosition());
        if (item instanceof ItemFilterCommon){
          ((ItemFilterCommon) item).setSelectPosition(checkedList);
        }
      });
    }
  }
}
