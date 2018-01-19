package cn.qingchengfit.saasbase.course.course.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;
import java.util.Locale;

public class AllCourseImageItem extends AbstractFlexibleItem implements ISectionable {

  public SchedulePhoto schedulePhoto;
  AllCourseImageHeaderItem mHeader;

  public AllCourseImageItem(SchedulePhoto schedulePhoto, AllCourseImageHeaderItem mHeader) {
    this.schedulePhoto = schedulePhoto;
    this.mHeader = mHeader;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_all_course_image_view;
  }

  @Override public RecyclerView.ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    return new AllCourseImageItemHolder(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position,
    List payloads) {
    if (holder instanceof AllCourseImageItemHolder) {
      if (schedulePhoto.getOwner() != null && !schedulePhoto.is_public()) {
        ((AllCourseImageItemHolder) holder).reader.setVisibility(View.VISIBLE);
        ((AllCourseImageItemHolder) holder).reader.setText(schedulePhoto.getOwner().username);
      } else {
        ((AllCourseImageItemHolder) holder).reader.setVisibility(View.GONE);
      }

      if (schedulePhoto.getCreated_by() != null) {
        ((AllCourseImageItemHolder) holder).uploader.setText(
          String.format(Locale.CHINA, "由%s上传", schedulePhoto.getCreated_by().username));
      }
      Glide.with(adapter.getRecyclerView().getContext())
        .load(PhotoUtils.getSmall(schedulePhoto.getPhoto()))
        .fitCenter()
        .into(((AllCourseImageItemHolder) holder).image);
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof AllCourseImageItem
      && ((AllCourseImageItem) o).schedulePhoto.getId().longValue() == schedulePhoto.getId();
  }

  @Override public IHeader getHeader() {
    return mHeader;
  }

  @Override public void setHeader(IHeader header) {
    this.mHeader = (AllCourseImageHeaderItem) header;
  }

  public static class AllCourseImageItemHolder extends FlexibleViewHolder {

    @BindView(R2.id.image) ImageView image;
    @BindView(R2.id.reader) TextView reader;
    @BindView(R2.id.uploader) TextView uploader;

    public AllCourseImageItemHolder(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      image.setOnClickListener(view1 -> {
        if (adapter.getItem(getAdapterPosition()) instanceof AllCourseImageItem) {
          RxBus.getBus().post(adapter.getItem(getAdapterPosition()));
        }
      });
    }
  }
}
