package cn.qingchengfit.saasbase.bill.filter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.utils.MeasureUtils;
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

  public ItemFilterCommon(FilterModel filterModel) {
    this.filterModel = filterModel;
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
    holder.billFilterTitle.setText(filterModel.name);
    for (Content content : filterModel.content) {
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(MeasureUtils.dpToPx(100f, holder.itemView.getResources()),
              MeasureUtils.dpToPx(40f, holder.itemView.getResources()));

      CheckBoxButton button = new CheckBoxButton(holder.itemView.getContext());
      button.setContent(content.name);

      button.setLayoutParams(params);
      holder.billFilterCommon.addChild(button);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_common;
  }

  class ItemFilterVH extends FlexibleViewHolder {

    @BindView(R2.id.bill_filter_common) QcAutoLineRadioGroup billFilterCommon;
    @BindView(R2.id.bill_filter_title) TextView billFilterTitle;

    public ItemFilterVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      billFilterCommon.setSingleSelected(false);
      billFilterCommon.setOnCheckoutPositionListener(new QcAutoLineRadioGroup.OnCheckoutPositionListener() {
        @Override public void onCheckPosition(List<Integer> checkedList) {
          IFlexible item = adapter.getItem(getAdapterPosition());
          if (item instanceof ItemFilterCommon){
            ((ItemFilterCommon) item).setSelectPosition(checkedList);
          }
        }
      });
    }
  }
}
