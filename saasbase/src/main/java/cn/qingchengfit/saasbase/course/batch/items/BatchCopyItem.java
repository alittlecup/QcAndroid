package cn.qingchengfit.saasbase.course.batch.items;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2018/4/20.
 */

public class BatchCopyItem extends AbstractFlexibleItem<BatchCopyItem.BatchCopyVH> {

  String date;
  private OnClickPrintListener listener;

  public BatchCopyItem(String date) {
    this.date = date;
  }

  public BatchCopyItem(String date, OnClickPrintListener listener) {
    this.date = date;
    this.listener = listener;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_copy_sticker;
  }

  @Override public BatchCopyVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchCopyVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, BatchCopyVH holder, int position,
      List payloads) {
    holder.tvStickerContent.setText(date);
  }

  @Override public boolean equals(Object o) {
      return false;
  }

  public class BatchCopyVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_sticker_content) TextView tvStickerContent;
    @BindView(R2.id.tv_print) TextView tvPrint;

    public BatchCopyVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      ButterKnife.bind(this, view);
    }

    @OnClick(R2.id.tv_print) public void onClickPrint() {
      if (listener != null){
        listener.onPrint();
      }
    }
  }

  public interface OnClickPrintListener{
    void onPrint();
  }

}
