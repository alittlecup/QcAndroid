package cn.qingchengfit.utils;

import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/5/2.
 */

public abstract class CommonAllocateDetailItem<T extends CommonAllocateDetailItem.AllocateDetailVH> extends AbstractFlexibleItem<T>
    implements Parcelable {

    private QcStudentBean studentBean;
    private boolean isAlphaBet;

    public CommonAllocateDetailItem(QcStudentBean studentBean) {
        this.studentBean = studentBean;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_common_person;
    }

    public QcStudentBean getData() {
        return studentBean;
    }

    public void setAlphaBet(boolean alphaBet) {
        isAlphaBet = alphaBet;
    }

    @Override public T createViewHolder(View view, FlexibleAdapter adapter) {
        return (T) new AllocateDetailVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, T holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(studentBean.avatar))
            .asBitmap()
            .placeholder(R.drawable.ic_default_head_nogender)
            .error(R.drawable.ic_default_head_nogender)
            .into(new CircleImgWrapper(holder.itemPersonalHeader, holder.itemView.getContext()));
        holder.itemPersonalName.setText(studentBean.username());
        holder.itemPersonPhone.setText(studentBean.phone());
        holder.itemPersonalGender.setImageResource(
            studentBean.gender() == 1 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        StringUtils.studentStatus(holder.tvStudentStatus, studentBean.status);
        if (isAlphaBet) {
            holder.itemStudentModifyAlpha.setVisibility(View.VISIBLE);
            holder.itemStudentModifyAlpha.setText(studentBean.head());
        } else {
            holder.itemStudentModifyAlpha.setVisibility(View.GONE);
        }
    }

    public static class AllocateDetailVH extends FlexibleViewHolder {

	ImageView itemPersonalHeader;
	TextView itemPersonalName;
	ImageView itemPersonalGender;
	TextView tvStudentStatus;
	TextView itemPersonPhone;
	TextView itemStudentModifyAlpha;

        public AllocateDetailVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemPersonalHeader = (ImageView) view.findViewById(R.id.item_personal_header);
          itemPersonalName = (TextView) view.findViewById(R.id.item_personal_name);
          itemPersonalGender = (ImageView) view.findViewById(R.id.item_personal_gender);
          tvStudentStatus = (TextView) view.findViewById(R.id.tv_student_status);
          itemPersonPhone = (TextView) view.findViewById(R.id.item_person_phone);
          itemStudentModifyAlpha = (TextView) view.findViewById(R.id.item_student_modify_alpha);
        }
    }
}
