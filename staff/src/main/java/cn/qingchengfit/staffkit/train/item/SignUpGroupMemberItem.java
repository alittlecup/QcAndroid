package cn.qingchengfit.staffkit.train.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
 * Created by fb on 2017/3/23.
 */

//
public class SignUpGroupMemberItem extends AbstractFlexibleItem<SignUpGroupMemberItem.SignUpGroupMemberVH> {

    private QcStudentBean studentBean;
    private boolean isOperation = false;

    public SignUpGroupMemberItem(QcStudentBean studentBean) {
        this.studentBean = studentBean;
    }

    public QcStudentBean getStudentBean() {
        return studentBean;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SignUpGroupMemberVH holder, int position, List payloads) {

        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(studentBean.avatar()))
            .asBitmap()
            .into(new CircleImgWrapper(holder.headGroupMember, holder.itemView.getContext()));

        holder.textMemberName.setText(studentBean.username());
        holder.textMemberPhone.setText(studentBean.phone());
        holder.imageMemberGender.setImageResource(
            studentBean.gender().equals("0") ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        if (isOperation) {
            holder.itemDeleteMember.setVisibility(View.VISIBLE);
        } else {
            holder.itemDeleteMember.setVisibility(View.GONE);
        }
    }

    @Override public int getLayoutRes() {
        return R.layout.item_group_member;
    }

    @Override public SignUpGroupMemberVH createViewHolder(View view, FlexibleAdapter adapter) {
        SignUpGroupMemberVH vh = new SignUpGroupMemberVH(view, adapter);
        if (isOperation) {
            vh.itemDeleteMember.setVisibility(View.VISIBLE);
        } else {
            vh.itemDeleteMember.setVisibility(View.GONE);
        }
        return vh;
    }

    class SignUpGroupMemberVH extends FlexibleViewHolder {

        @BindView(R.id.head_group_member) ImageView headGroupMember;
        @BindView(R.id.text_member_name) TextView textMemberName;
        @BindView(R.id.image_member_gender) ImageView imageMemberGender;
        @BindView(R.id.text_member_phone) TextView textMemberPhone;
        @BindView(R.id.item_delete_member) ImageView itemDeleteMember;

        public SignUpGroupMemberVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
