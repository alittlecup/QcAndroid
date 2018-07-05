package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/4.
 */

public class AllotMultiStaffItem extends AbstractFlexibleItem<AllotMultiStaffItem.ViewHolder> {
    public static final int TYPE_ADAPTER_CHOOSE = 0;
    public static final int TYPE_ADAPTER_DELETE = 1;
    QcStudentBean studentBean;
    int type = 0;

    public AllotMultiStaffItem(QcStudentBean studentBeans, int type) {
        this.studentBean = studentBeans;
        this.type = type;
    }

    public QcStudentBean getData() {
        return studentBean;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AllotMultiStaffItem && ((AllotMultiStaffItem) o).getData().equals(studentBean);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_allot_multi_staff;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {

        switch (type) {
            case TYPE_ADAPTER_CHOOSE:
                holder.itemCheckbox.setVisibility(View.VISIBLE);
                holder.itemDelete.setVisibility(View.GONE);
                holder.itemView.setTag(position);
                break;
            case TYPE_ADAPTER_DELETE:
                holder.itemCheckbox.setVisibility(View.GONE);
                holder.itemDelete.setVisibility(View.VISIBLE);
                holder.itemDelete.setTag(position);
                break;
        }

        // 销售names
        if (studentBean.sellers == null || studentBean.sellers.size() == 0) {
            holder.itemPersonDesc.setText(holder.itemView.getContext().getResources().getString(R.string.qc_student_saleids, " "));
        } else {
            holder.itemPersonDesc.setText(holder.itemView.getContext()
                    .getResources()
                    .getString(R.string.qc_student_saleids, StringUtils.sellersNames(studentBean.sellers)));
        }

        holder.itemPersonName.setText(studentBean.getUsername());
        holder.itemPersonPhonenum.setText(studentBean.getPhone());

        if (studentBean.gender==0) {//男
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_female);
        }
//        if (studentBean.isTag) {
//            holder.itemStudentModifyAlpha.setVisibility(View.VISIBLE);
//            holder.itemStudentModifyAlpha.setText(getItem(position).head);
//        } else {
//            holder.itemStudentModifyAlpha.setVisibility(View.GONE);
//        }

        holder.status.setCompoundDrawablesWithIntrinsicBounds(
                StudentBusinessUtils.getStudentStatusDrawable(holder.itemView.getContext(),
                        studentBean.getStatus() % 3), null, null, null);

        PhotoUtils.smallCircle(holder.itemPersonHeader,studentBean.avatar(),R.drawable.ic_default_head_nogender,R.drawable.ic_default_head_nogender);
    }


    class ViewHolder extends FlexibleViewHolder {

        TextView itemStudentModifyAlpha;

        CheckBox itemCheckbox;

        ImageView itemDelete;

        ImageView itemPersonHeader;

        TextView itemPersonName;

        ImageView itemPersonGender;

        TextView tvReferrerCount;

        TextView itemPersonPhonenum;

        TextView itemPersonDesc;

        TextView status;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemStudentModifyAlpha = (TextView) view.findViewById(R.id.item_student_modify_alpha);
          itemCheckbox = (CheckBox) view.findViewById(R.id.item_checkbox);
          itemDelete = (ImageView) view.findViewById(R.id.item_delete);
          itemPersonHeader = (ImageView) view.findViewById(R.id.item_person_header);
          itemPersonName = (TextView) view.findViewById(R.id.item_person_name);
          itemPersonGender = (ImageView) view.findViewById(R.id.item_person_gender);
          tvReferrerCount = (TextView) view.findViewById(R.id.tv_referrer_count);
          itemPersonPhonenum = (TextView) view.findViewById(R.id.item_person_phonenum);
          itemPersonDesc = (TextView) view.findViewById(R.id.item_person_desc);
          status = (TextView) view.findViewById(R.id.status);
        }
    }
}
