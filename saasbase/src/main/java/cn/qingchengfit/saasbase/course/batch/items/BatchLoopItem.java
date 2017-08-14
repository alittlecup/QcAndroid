package cn.qingchengfit.saasbase.course.batch.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.Constants;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
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

  @Override public int getLayoutRes() {
    return R.layout.item_batch_loop;
  }

  @Override public BatchLoopVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new BatchLoopVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, BatchLoopVH holder, int position,
      List payloads) {
    StringBuffer sb = new StringBuffer("每个");
    for (int i = 0; i < batchLoop.week.size(); i++) {
      sb.append(WEEKS[batchLoop.week.get(i) - 1]).append(Constants.SEPARATE_CN);
    }
    holder.week.setText(sb.substring(0, sb.length() - 1));
    if (batchLoop.slice > 0){
      holder.tvSlice.setText("约课时间间隔为"+(batchLoop.slice/60)+"分钟");
    }else holder.tvSlice.setText("");
    if (isPrivate) {
      holder.time.setText(DateUtils.getTimeHHMM(batchLoop.dateStart) + "-" + DateUtils.getTimeHHMM(batchLoop.dateEnd));
    } else {
      holder.time.setText(DateUtils.getTimeHHMM(batchLoop.dateStart));
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class BatchLoopVH extends FlexibleViewHolder {

    @BindView(R2.id.delete) ImageView delete;
    @BindView(R2.id.time) TextView time;
    @BindView(R2.id.week) TextView week;
    @BindView(R2.id.tv_slice) TextView tvSlice;

    public BatchLoopVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      delete.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          adapter.removeItem(getAdapterPosition());
        }
      });
    }
  }
}