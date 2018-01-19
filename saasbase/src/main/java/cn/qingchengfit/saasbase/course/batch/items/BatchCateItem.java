package cn.qingchengfit.saasbase.course.batch.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchCateItem extends AbstractFlexibleItem<BatchCateItem.BatchCateVH> {

  String title;
  String content;
  String id;
  String img;
  boolean outOfdate;
  boolean isPrivate;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }


  public BatchCateItem(String title, String content, String id, String img) {
    this.title = title;
    this.content = content;
    this.id = id;
    this.img = img;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_category;
  }

  @Override public BatchCateVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchCateVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, BatchCateVH holder, int position,
    List payloads) {
    holder.tvTitle.setText(title);
    holder.tvContent.setText(content);
    if (isPrivate) {
      PhotoUtils.smallCircle(holder.img, img);
      holder.img.setBackgroundResource(R.drawable.circle_outside);
    } else {
      PhotoUtils.small(holder.img,img);
      holder.img.setBackgroundResource(R.drawable.bg_rect_square_img);
    }
    holder.itemView.setAlpha(outOfdate?0.5f:1f);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof BatchCateItem && ((BatchCateItem) o).getId()!= null && id !=null){
      return ((BatchCateItem) o).getId().equalsIgnoreCase(id);
    }
    return false;
  }

  public class BatchCateVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.tv_content) TextView tvContent;
    @BindView(R2.id.img) ImageView img;
    public BatchCateVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}