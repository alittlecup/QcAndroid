package com.qingchengfit.fitcoach.items;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.bean.SchedulePhoto;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

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




    public AllCourseImageItem(SchedulePhoto schedulePhoto, AllCourseImageHeaderItem mHeader) {
        this.schedulePhoto = schedulePhoto;
        this.mHeader = mHeader;
    }

    public SchedulePhoto schedulePhoto;



    @Override
    public int getLayoutRes() {
        return R.layout.item_all_course_image_view;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AllCourseImageItemHolder(inflater.inflate(R.layout.item_all_course_image_view, parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {
        if (holder instanceof AllCourseImageItemHolder) {
            if (schedulePhoto.getOwner() != null && !schedulePhoto.is_public()){
                ((AllCourseImageItemHolder) holder).reader.setVisibility(View.VISIBLE);
                ((AllCourseImageItemHolder) holder).reader.setText(schedulePhoto.getOwner().name);
            }else ((AllCourseImageItemHolder) holder).reader.setVisibility(View.GONE);

            if (schedulePhoto.getCreated_by() != null){
                ((AllCourseImageItemHolder) holder).uploader.setText(String.format(Locale.CHINA,"由%s上传",schedulePhoto.getCreated_by().name));
            }
            Glide.with(adapter.getRecyclerView().getContext()).load(PhotoUtils.getSmall(schedulePhoto.getPhoto())).fitCenter().into(((AllCourseImageItemHolder) holder).image);
            ((AllCourseImageItemHolder) holder).reader.setCompoundDrawables(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.ic_eye_white),null,null,null);
        }
    }




    AllCourseImageHeaderItem mHeader;


    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public IHeader getHeader() {
        return mHeader;
    }

    @Override
    public void setHeader(IHeader header) {
        this.mHeader = (AllCourseImageHeaderItem) header;
    }

    public static class AllCourseImageItemHolder extends FlexibleViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.reader)
        TextView reader;
        @BindView(R.id.uploader)
        TextView uploader;


        public AllCourseImageItemHolder(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this,view);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapter.getItem(getAdapterPosition()) instanceof AllCourseImageItem){
                        RxBus.getBus().post(adapter.getItem(getAdapterPosition()));
                    }

                }
            });
        }
    }


}
