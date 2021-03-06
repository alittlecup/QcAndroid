package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DrawableUtils;
import com.bigkoo.pickerview.lib.DensityUtil;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.ArrayList;
import java.util.List;

public class BatchPayCardHeaderItem
    extends AbstractFlexibleItem<BatchPayCardHeaderItem.BatchPayCardHeaderVH>
    implements IExpandable<BatchPayCardHeaderItem.BatchPayCardHeaderVH, BatchPayCardItem>,
    ISectionable<BatchPayCardHeaderItem.BatchPayCardHeaderVH, IHeader> {

  public List<BatchPayCardItem> childItems = new ArrayList<>();
  CardTpl cardTpl;
  boolean hasOrdered;
  private boolean expanded;

  public BatchPayCardHeaderItem(CardTpl cardTpl, List<BatchPayCardItem> list) {
    this.cardTpl = cardTpl;
    this.childItems.addAll(list);
  }

  public BatchPayCardHeaderItem(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  public boolean isHasOrdered() {
    return hasOrdered;
  }

  public void setHasOrdered(boolean hasOrdered) {
    this.hasOrdered = hasOrdered;
  }

  public void addChild(BatchPayCardItem item) {
    childItems.add(item);
  }

  public void clearChild() {
    childItems.clear();
  }

  public String getCardId() {
    return cardTpl.id;
  }

  public String getCardname() {
    return cardTpl.getName();
  }

  public int getCardtype() {
    return cardTpl.type;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_pay_card_header;
  }

  @Override public BatchPayCardHeaderVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchPayCardHeaderVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BatchPayCardHeaderVH holder, int position,
      List payloads) {
    holder.sw.setChecked(expanded);
    holder.tvTitle.setText(cardTpl.getName());
    holder.tvStatus.setVisibility(
        (cardTpl.is_enable && !isHasOrdered()) ? View.GONE : View.VISIBLE);
    if (isHasOrdered()) {
      holder.tvStatus.setText("已预约");
      setEnabled(false);
    } else if (!cardTpl.is_enable) {
      holder.tvStatus.setText("已停用");
      setEnabled(true);
    } else {
      holder.tvStatus.setText("");
      setEnabled(true);
    }
    holder.linearLayout.setBackground(
        DrawableUtils.generateBg(DensityUtil.dip2px(holder.itemView.getContext(), 5),
            CardBusinessUtils.getDefaultCardbgColor(cardTpl.getType())));
  }

  @Override public boolean equals(Object o) {
    if (o instanceof BatchPayCardHeaderItem) {
      return ((BatchPayCardHeaderItem) o).getCardId().equals(cardTpl.getId());
    } else {
      return false;
    }
  }

  @Override public boolean isExpanded() {
    return expanded;
  }

  @Override public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  @Override public int getExpansionLevel() {
    return 0;
  }

  @Override public List<BatchPayCardItem> getSubItems() {
    return childItems;
  }

  @Override public IHeader getHeader() {
    return header;
  }
  private  IHeader header;
  @Override public void setHeader(IHeader header) {
    this.header=header;
  }

  public class BatchPayCardHeaderVH extends ExpandableViewHolder {
    TextView tvStatus;
    TextView tvTitle;
    Switch sw;
    LinearLayout linearLayout;

    public BatchPayCardHeaderVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvStatus = (TextView) view.findViewById(R.id.tv_status);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      sw = (Switch) view.findViewById(R.id.sw);
      linearLayout = view.findViewById(R.id.ll_content_container);
    }
  }
}