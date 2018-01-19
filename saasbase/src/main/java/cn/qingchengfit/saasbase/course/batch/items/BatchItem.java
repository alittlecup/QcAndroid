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
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchItem extends AbstractFlexibleItem<BatchItem.BatchVH> {

  String img, title, txt,id;

  private  BatchCourse batchCourse;
  private BatchCoach batchCoach;

  /**
   * 团课
   */
  public BatchItem(BatchCourse course) {
    img = course.getPhoto();
    title = course.getName();
    if (course.to_date == null)
      txt = "课程时长"+course.length/60+"分钟";
    else
      txt = (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(course.to_date)) )? "无有效排期"
        : (course.from_date + "至" + course.to_date + "," + course.count + "节课程");
    batchCourse = course;
  }

  public BatchItem(QcResponsePrivateDetail.PrivateCoach course) {
    img = course.avatar;
    title = course.username;
    txt = "累计"+course.course_count+"节, 服务"+course.users_count+"人次";
    id = course.id;
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

  public String getId() {
    if (batchCourse != null)
      return batchCourse.getId();
    if (batchCoach != null)
      return batchCoach.id;
    return id;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_batch;
  }

  @Override public BatchVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BatchVH holder, int position, List payloads) {
    PhotoUtils.small(holder.img, img);
    holder.text1.setText(title);
    holder.text2.setText(txt);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof BatchItem){
      if (((BatchItem) o).getBatchCoach() != null && getBatchCoach() != null)
        return ((BatchItem) o).getBatchCoach().id.equalsIgnoreCase(getBatchCoach().id);
      if (((BatchItem) o).getBatchCourse() != null && getBatchCourse() != null)
        return ((BatchItem) o).getBatchCourse().id.equalsIgnoreCase(getBatchCourse().id);
      if (id != null && ((BatchItem) o).getId() != null)
        return id.equalsIgnoreCase(((BatchItem) o).getId());
    }
    return false;
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