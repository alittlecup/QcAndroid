package com.qingchengfit.fitcoach.items;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import cn.qingchengfit.bean.SchedulePhoto;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;
import java.util.Locale;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/27.
 */
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

    @Override public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {
        if (holder instanceof AllCourseImageItemHolder) {
            if (schedulePhoto.getOwner() != null && !schedulePhoto.is_public()) {
                ((AllCourseImageItemHolder) holder).reader.setVisibility(View.VISIBLE);
                ((AllCourseImageItemHolder) holder).reader.setText(schedulePhoto.getOwner().name);
            } else {
                ((AllCourseImageItemHolder) holder).reader.setVisibility(View.GONE);
            }

            if (schedulePhoto.getCreated_by() != null) {
                ((AllCourseImageItemHolder) holder).uploader.setText(
                    String.format(Locale.CHINA, "由%s上传", schedulePhoto.getCreated_by().name));
            }
            Glide.with(adapter.getRecyclerView().getContext())
                .load(PhotoUtils.getSmall(schedulePhoto.getPhoto()))
                .fitCenter()
                .into(((AllCourseImageItemHolder) holder).image);
            ((AllCourseImageItemHolder) holder).reader.setCompoundDrawables(
                ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_eye_white), null, null, null);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public IHeader getHeader() {
        return mHeader;
    }

    @Override public void setHeader(IHeader header) {
        this.mHeader = (AllCourseImageHeaderItem) header;
    }

    public static class AllCourseImageItemHolder extends FlexibleViewHolder {

	ImageView image;
	TextView reader;
	TextView uploader;

        public AllCourseImageItemHolder(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
          image = (ImageView) view.findViewById(R.id.image);
          reader = (TextView) view.findViewById(R.id.reader);
          uploader = (TextView) view.findViewById(R.id.uploader);

          image.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (adapter.getItem(getAdapterPosition()) instanceof AllCourseImageItem) {
                        RxBus.getBus().post(adapter.getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
