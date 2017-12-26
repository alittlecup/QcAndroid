package cn.qingchengfit.staffkit.allocate.coach.item;

import android.os.Parcel;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/5/4.
 */

public class CoachStudentDetailItem extends CommonAllocateDetailItem<CoachStudentDetailItem.CoachStudentDetailVH> {

    private QcStudentBean studentBean;

    public CoachStudentDetailItem(QcStudentBean studentBean) {
        super(studentBean);
        this.studentBean = studentBean;
    }

    public QcStudentBean getData() {
        return studentBean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_coach_student_detail;
    }

    @Override public CoachStudentDetailVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CoachStudentDetailVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CoachStudentDetailVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        if (studentBean.sellers == null || (studentBean.sellers != null && studentBean.sellers.size() == 0)) {
            holder.itemPersonDesc.setVisibility(View.GONE);
        } else {
            holder.itemPersonDesc.setVisibility(View.VISIBLE);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.coach_list, StringUtils.coachesNames(studentBean.sellers)));
        }
    }

    class CoachStudentDetailVH extends CommonAllocateDetailItem.AllocateDetailVH {

        @BindView(R.id.item_person_desc) TextView itemPersonDesc;

        public CoachStudentDetailVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.studentBean, flags);
    }

    protected CoachStudentDetailItem(Parcel in) {
        this.studentBean = in.readParcelable(QcStudentBean.class.getClassLoader());
    }

    public static final Creator<CoachStudentDetailItem> CREATOR =
        new Creator<CoachStudentDetailItem>() {
            @Override public CoachStudentDetailItem createFromParcel(Parcel source) {
                return new CoachStudentDetailItem(source);
            }

            @Override public CoachStudentDetailItem[] newArray(int size) {
                return new CoachStudentDetailItem[size];
            }
        };
}
