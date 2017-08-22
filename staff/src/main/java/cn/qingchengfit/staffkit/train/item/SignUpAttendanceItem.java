package cn.qingchengfit.staffkit.train.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.MemberAttendanceBean;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/4/1.
 */

public class SignUpAttendanceItem extends AbstractFlexibleItem<SignUpAttendanceItem.SignUpAttendanceVH> {

    private MemberAttendanceBean datas;
    private Context context;

    public SignUpAttendanceItem(MemberAttendanceBean datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public MemberAttendanceBean getDatas() {
        return datas;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_sign_up_attendance;
    }

    @Override public SignUpAttendanceVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        SignUpAttendanceVH vh = new SignUpAttendanceVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
        return vh;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SignUpAttendanceVH holder, int position, List payloads) {

        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(datas.user.avatar))
            .asBitmap()
            .placeholder(R.drawable.default_manage_male)
            .error(R.drawable.default_manage_male)
            .into(new CircleImgWrapper(holder.imgSignUpAttendanceHead, holder.itemView.getContext()));

        holder.tvSignUpAttendanceName.setText(datas.user.username);
        if (datas.day_count != null) {
            holder.tvSignDay0.setText(String.valueOf(datas.day_count));
            holder.tvSignAction0.setText(context.getString(R.string.lable_attendace));
            holder.labelSignUnit0.setVisibility(View.VISIBLE);
        }
        if (datas.checkin_count != null) {
            holder.tvSignDay1.setText(String.valueOf(datas.checkin_count));
            holder.tvSignAction1.setText(context.getString(R.string.label_checkin));
            holder.labelSignUnit1.setVisibility(View.VISIBLE);
        }
        if (datas.group_count != null) {
            holder.tvSignDay2.setText(String.valueOf(datas.group_count));
            holder.tvSignAction2.setText(context.getString(R.string.course_type_group));
            holder.labelSignUnit2.setVisibility(View.VISIBLE);
        }
        if (datas.private_count != null) {
            holder.tvSignDay3.setText(String.valueOf(datas.private_count));
            holder.tvSignAction3.setText(context.getString(R.string.attendance_private_course));
            holder.labelSignUnit3.setVisibility(View.VISIBLE);
        }
    }

    class SignUpAttendanceVH extends FlexibleViewHolder {

        @BindView(R.id.img_sign_up_attendance_head) ImageView imgSignUpAttendanceHead;
        @BindView(R.id.tv_sign_up_attendance_name) TextView tvSignUpAttendanceName;
        @BindView(R.id.tv_sign_day_0) TextView tvSignDay0;
        @BindView(R.id.tv_sign_action_0) TextView tvSignAction0;
        @BindView(R.id.label_sign_unit_1) TextView labelSignUnit1;
        @BindView(R.id.tv_sign_day_1) TextView tvSignDay1;
        @BindView(R.id.tv_sign_action_1) TextView tvSignAction1;
        @BindView(R.id.tv_sign_day_2) TextView tvSignDay2;
        @BindView(R.id.tv_sign_action_2) TextView tvSignAction2;
        @BindView(R.id.label_sign_unit_2) TextView labelSignUnit2;
        @BindView(R.id.tv_sign_day_3) TextView tvSignDay3;
        @BindView(R.id.tv_sign_action_3) TextView tvSignAction3;
        @BindView(R.id.label_sign_unit_3) TextView labelSignUnit3;
        @BindView(R.id.label_sign_unit_0) TextView labelSignUnit0;

        public SignUpAttendanceVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
