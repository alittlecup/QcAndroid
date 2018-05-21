package cn.qingchengfit.staffkit.allocate.coach.item;

import android.os.Parcel;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/5/3.
 */

public class ChooseStudentItem extends CommonAllocateDetailItem<ChooseStudentItem.ChooseStudentVH> {

    private QcStudentBean data;

    public ChooseStudentItem(){
        super();
    }

    public ChooseStudentItem(QcStudentBean data) {
        super(data);
        this.data = data;
    }

    public QcStudentBean getData() {
        return data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_coach_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        holder.itemCheckbox.setChecked(adapter.isSelected(position));
        if (data.sellers == null || (data.sellers != null && data.sellers.size() == 0)) {
            holder.itemPersonDesc.setVisibility(View.GONE);
        } else {
            holder.itemPersonDesc.setVisibility(View.VISIBLE);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.coach_list, StringUtils.coachesNames(data.sellers)));
        }
    }

    class ChooseStudentVH extends CommonAllocateDetailItem.AllocateDetailVH {

	TextView itemStudentModifyAlpha;
	CheckBox itemCheckbox;
	TextView itemPersonDesc;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemStudentModifyAlpha = (TextView) view.findViewById(R.id.item_student_modify_alpha);
          itemCheckbox = (CheckBox) view.findViewById(R.id.item_checkbox);
          itemPersonDesc = (TextView) view.findViewById(R.id.item_person_desc);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    protected ChooseStudentItem(Parcel in) {
        this.data = in.readParcelable(QcStudentBean.class.getClassLoader());
    }

    public static final Creator<ChooseStudentItem> CREATOR = new Creator<ChooseStudentItem>() {
        @Override public ChooseStudentItem createFromParcel(Parcel source) {
            return new ChooseStudentItem(source);
        }

        @Override public ChooseStudentItem[] newArray(int size) {
            return new ChooseStudentItem[size];
        }
    };
}


