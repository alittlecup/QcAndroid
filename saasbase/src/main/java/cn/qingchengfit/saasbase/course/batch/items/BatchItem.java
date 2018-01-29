package cn.qingchengfit.saasbase.course.batch.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchItem extends AbstractFlexibleItem<BatchItem.BatchVH> {

  private BatchItemModel batchModel;

  public BatchItem(BatchItemModel batchModel) {
    this.batchModel = batchModel;
  }

  public BatchCourse getBatchCourse() {
    if (batchModel instanceof BatchCourse) {
      return (BatchCourse) batchModel;
    } else {
      return null;
    }
  }

  public BatchCoach getBatchCoach() {
    if (batchModel instanceof BatchCoach){
      return (BatchCoach)batchModel;
    }else return null;
  }

  public BatchItemModel getBatchModel() {
    return batchModel;
  }

  public String getId() {
    return batchModel.getId();
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_batch;
  }

  @Override public BatchVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BatchVH holder, int position, List payloads) {
    PhotoUtils.small(holder.img, batchModel.getAvatar());
    holder.text1.setText(batchModel.getTitle());
    holder.text2.setText(batchModel.getText());
  }

  @Override public boolean equals(Object o) {
    return batchModel.equals(o);
  }

  public interface BatchItemModel {
    String getId();

    String getAvatar();

    String getTitle();

    String getText();

    boolean equals(Object o);
  }

  public class BatchVH extends FlexibleViewHolder {

    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.text1) TextView text1;
    @BindView(R2.id.text2) TextView text2;

    public BatchVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}