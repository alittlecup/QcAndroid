package cn.qingchengfit.saasbase.course.batch.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.Constants;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

import static cn.qingchengfit.Constants.WEEKS;

public class BatchLoopItem extends AbstractFlexibleItem<BatchLoopItem.BatchLoopVH> {

  BatchLoop batchLoop;
  boolean isPrivate;


  public BatchLoopItem(BatchLoop batchLoop, boolean isPrivate) {
    this.batchLoop = batchLoop;
    this.isPrivate = isPrivate;
  }

  public void setBatchLoop(BatchLoop batchLoop) {
    this.batchLoop = batchLoop;
  }

  public BatchLoop getBatchLoop() {
    return batchLoop;
  }

  public String getId(){
    if (batchLoop != null && batchLoop.id != null){
      return batchLoop.id;
    }else return "";
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_loop;
  }

  @Override public BatchLoopVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchLoopVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, BatchLoopVH holder, int position,
      List payloads) {
    StringBuffer sb = new StringBuffer("每个");
    for (int i = 0; i < batchLoop.week.size(); i++) {
      sb.append(WEEKS[batchLoop.week.get(i) - 1]).append(Constants.SEPARATE_CN);
    }
    holder.week.setText(sb.substring(0, sb.length() - 1));
    if (isPrivate){
      holder.tvSlice.setText("约课时间间隔为"+(batchLoop.slice/60)+"分钟");
    }else holder.tvSlice.setText("");
    if (isPrivate) {
      holder.time.setText(DateUtils.getTimeHHMM(batchLoop.dateStart) + "-" +(batchLoop.isCross?"次日":"")+ DateUtils.getTimeHHMM(batchLoop.dateEnd));
    } else {
      holder.time.setText(DateUtils.getTimeHHMM(batchLoop.dateStart));
    }
  }

  @Override public boolean equals(Object o) {
    if (o instanceof BatchLoopItem){
      return getId().equalsIgnoreCase(((BatchLoopItem) o).getId());
    }
    return false;
  }

  public class BatchLoopVH extends FlexibleViewHolder {

	ImageView delete;
	TextView time;
	TextView week;
	TextView tvSlice;

    public BatchLoopVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      delete = (ImageView) view.findViewById(R.id.delete);
      time = (TextView) view.findViewById(R.id.time);
      week = (TextView) view.findViewById(R.id.week);
      tvSlice = (TextView) view.findViewById(R.id.tv_slice);

      delete.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          adapter.removeItem(getAdapterPosition());
        }
      });
    }
  }
}