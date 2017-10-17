package cn.qingchengfit.saasbase.bill.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.widgets.CheckBoxButton;
import cn.qingchengfit.widgets.QcAutoLineRadioGroup;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/10/12.
 */

public class ItemFilterCommon extends AbstractFlexibleItem<ItemFilterCommon.ItemFilterVH> {

  private List<Content> contents;
  private OnCheckedSelectListener onCheckedSelectListener;

  public ItemFilterCommon(List<Content> contents, OnCheckedSelectListener onCheckedSelectListener) {
    this.contents = contents;
    this.onCheckedSelectListener = onCheckedSelectListener;
  }


  @Override public ItemFilterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    ItemFilterVH holder = new ItemFilterVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    holder.billFilterCommon.setOnCheckoutPositionListener(new QcAutoLineRadioGroup.OnCheckoutPositionListener() {
      @Override public void onCheckPosition(List<Integer> checkedList) {
        if (onCheckedSelectListener != null){
          List<Content> selectContent = new ArrayList<>();
          for (int i : checkedList) {
            selectContent.add(contents.get(i));
          }
          onCheckedSelectListener.onCheckedContent(selectContent);
        }
      }
    });
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemFilterVH holder, int position,
      List payloads) {
    for (Content content : contents){
      CheckBoxButton button = new CheckBoxButton(holder.itemView.getContext());
      button.setContent(content.name);
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

    public ItemFilterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnCheckedSelectListener{
    void onCheckedContent(List<Content> contentList);
  }

}
