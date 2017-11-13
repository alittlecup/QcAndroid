package cn.qingchengfit.saasbase.course.batch.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchItem extends AbstractFlexibleItem<BatchItem.BatchVH> {

  String img, title, txt;

  private  BatchCourse batchCourse;
  private BatchCoach batchCoach;

  /**
   * 团课
   */
  public BatchItem(BatchCourse course) {
    img = course.getPhoto();
    title = course.getName();
    txt = DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(course.to_date)) ? "无有效排期"
        : (course.from_date + "至" + course.to_date + "," + course.count + "节课程");
    batchCourse = course;
  }

  /**
   * 私教
   */
  public BatchItem(BatchCoach course) {
    img = course.avatar;
    title = course.username;
    txt = DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(course.to_date)) ? "无有效排期"
        : (course.from_date + "至" + course.to_date + "," + course.courses_count + "节课程");
    batchCoach = course;
  }

  public BatchCourse getBatchCourse() {
    return batchCourse;
  }

  public BatchCoach getBatchCoach() {
    return batchCoach;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch;
  }

  @Override public BatchVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BatchVH holder, int position, List payloads) {
    PhotoUtils.small(holder.img, img);
    holder.title.setText(title);
    holder.text2.setText(txt);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class BatchVH extends FlexibleViewHolder {

    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.title) TextView title;
    @BindView(R2.id.text2) TextView text2;

    public BatchVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}