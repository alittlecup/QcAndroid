package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchItem extends AbstractFlexibleItem<BatchItem.BatchVH> {

    QcResponseGroupDetail.GroupBatch batch;

    public void setBatch(QcResponseGroupDetail.GroupBatch batch) {
        this.batch = batch;
    }

    public QcResponseGroupDetail.GroupBatch getBatch() {
        return batch;
    }

    public BatchItem(QcResponseGroupDetail.GroupBatch batch) {
        this.batch = batch;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_batch;
    }

    @Override public BatchVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new BatchVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BatchVH holder, int position, List payloads) {
        holder.courseName.setText(batch.course.getName());
        Glide.with(holder.itemView.getContext()).load(PhotoUtils.getSmall(batch.course.getPhoto())).error(R.drawable.img_default_course).into(holder.img);
        holder.title.setText(batch.from_date + "至" + batch.to_date);
        holder.outofdate.setVisibility(DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(batch.to_date))? View.VISIBLE: View.GONE);
    }

    @Override public boolean equals(Object o) {

        if (o instanceof BatchItem){
            try {
                return ((BatchItem) o).batch.id.equals(batch.id);
            }catch (Exception e){
                return false;
            }

        }else return false;

    }

    public class BatchVH extends FlexibleViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.course_name) TextView courseName;
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.outofdate) View outofdate;

        public BatchVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}