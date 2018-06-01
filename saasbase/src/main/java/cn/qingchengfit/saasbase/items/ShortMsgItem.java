package cn.qingchengfit.saasbase.items;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.saasbase.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ShortMsgItem extends AbstractFlexibleItem<ShortMsgItem.ShortMsgVH> {

  ShortMsg shortMsg;

  public ShortMsgItem(ShortMsg shortMsg) {
    this.shortMsg = shortMsg;
  }

  public ShortMsg getShortMsg() {
    return shortMsg;
  }

  public void setShortMsg(ShortMsg shortMsg) {
    this.shortMsg = shortMsg;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_short_msg;
  }

  @Override public ShortMsgVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ShortMsgVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ShortMsgVH holder, int position,
    List payloads) {

    if (TextUtils.isEmpty(adapter.getSearchText())) {
      holder.tvTitle.setText(shortMsg.getShortTitle());
      holder.tvContent.setText(shortMsg.getContent());
    } else {
      FlexibleUtils.highlightWords(holder.tvTitle, shortMsg.getShortTitle(), adapter.getSearchText(), Color.RED);
      FlexibleUtils.highlightWords(holder.tvContent, shortMsg.getContent(), adapter.getSearchText(), Color.RED);
    }

    holder.imgStatus.setImageResource(
      shortMsg.status == 1 ? R.drawable.vector_hook_green : R.drawable.ic_msg_short_draft);
    holder.tvStatus.setText(shortMsg.status == 1 ? R.string.has_send : R.string.draft);
    holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
      shortMsg.status == 1 ? R.color.colorPrimary : R.color.orange_yellow));
    holder.tvTime.setText(cn.qingchengfit.utils.DateUtils.Date2YYYYMMDDHHmm(
      cn.qingchengfit.utils.DateUtils.formatDateFromServer(shortMsg.created_at)));
  }


  @Override public boolean equals(Object o) {
    if (o instanceof ShortMsgItem){
      return ((ShortMsgItem) o).getShortMsg().id.equals(shortMsg.id);
    }
    return false;
  }

  //@Override public boolean filter(String constraint) {
  //  return true;
  //  //if (shortMsg != null && (!TextUtils.isEmpty(shortMsg.getShortTitle()) || !TextUtils.isEmpty(shortMsg.getContent()))){
  //  //  return (shortMsg .getShortTitle() != null && shortMsg.getShortTitle().contains(constraint))
  //  //    || (shortMsg.getContent() != null && shortMsg.getContent().contains(constraint));
  //  //}else return false;
  //}

  public class ShortMsgVH extends FlexibleViewHolder {
	TextView tvTitle;
	TextView tvContent;
	ImageView imgStatus;
	TextView tvStatus;
	TextView tvTime;

    public ShortMsgVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
      imgStatus = (ImageView) view.findViewById(R.id.img_status);
      tvStatus = (TextView) view.findViewById(R.id.tv_status);
      tvTime = (TextView) view.findViewById(R.id.tv_time);
    }
  }
}