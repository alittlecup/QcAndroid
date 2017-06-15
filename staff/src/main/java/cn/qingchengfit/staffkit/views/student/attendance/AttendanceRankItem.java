package cn.qingchengfit.staffkit.views.student.attendance;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Attendance;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.CompatUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * 需要设置 status 代表着 顺序 设置Adapter的tag revert 为ture 表示逆序
 */

public class AttendanceRankItem extends AbstractFlexibleItem<AttendanceRankItem.AttendanceRankVH> {

    Attendance attendance;
    Context context;
    @ColorRes private int[][] colorRes = new int[][] {
        {
            R.color.reddish_orange, R.color.orange_yellow, R.color.golden_yellow,
        }, {
        R.color.turquoise_blue, R.color.seafoam_blue, R.color.light_teal,
    }, {
        R.color.azure, R.color.lightblue, R.color.lightblue_two,
    }, {
        R.color.iris, R.color.perrywinkle, R.color.light_blue_grey,
    },
    };
    @ColorRes private int[] revertColor = new int[] {
        R.color.pinkish_grey, R.color.warm_grey_2, R.color.greyish_brown
    };

    public AttendanceRankItem(Attendance attendance, Context context) {
        this.attendance = attendance;
        this.context = context;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_attendance_rank;
    }

    @Override public AttendanceRankVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AttendanceRankVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AttendanceRankVH holder, int position, List payloads) {
        if (adapter instanceof CommonFlexAdapter) {
            int type = ((CommonFlexAdapter) adapter).getStatus();
            switch (type) {
                case 1:
                    holder.labelUnit1.setText(context.getString(R.string.unit_day));
                    holder.labelUnit0.setText(context.getString(R.string.unit_time));
                    holder.labelUnit2.setText(context.getString(R.string.unit_class));
                    holder.labelUnit3.setText(context.getString(R.string.unit_class));

                    holder.tvAction1.setText(context.getString(R.string.lable_attendace));
                    holder.tvAction0.setText(context.getString(R.string.label_checkin));
                    holder.tvAction2.setText(context.getString(R.string.course_type_group));
                    holder.tvAction3.setText(context.getString(R.string.attendance_private_course));

                    holder.tvDay1.setText(String.valueOf(attendance.days));
                    holder.tvDay0.setText(String.valueOf(attendance.checkin));
                    holder.tvDay2.setText(String.valueOf(attendance.group_num));
                    holder.tvDay3.setText(String.valueOf(attendance.private_num));
                    break;
                case 2:
                    holder.labelUnit1.setText(context.getString(R.string.unit_day));
                    holder.labelUnit2.setText(context.getString(R.string.unit_time));
                    holder.labelUnit0.setText(context.getString(R.string.unit_class));
                    holder.labelUnit3.setText(context.getString(R.string.unit_class));

                    holder.tvAction1.setText(context.getString(R.string.lable_attendace));
                    holder.tvAction2.setText(context.getString(R.string.label_checkin));
                    holder.tvAction0.setText(context.getString(R.string.course_type_group));
                    holder.tvAction3.setText(context.getString(R.string.attendance_private_course));

                    holder.tvDay1.setText(String.valueOf(attendance.days));
                    holder.tvDay2.setText(String.valueOf(attendance.checkin));
                    holder.tvDay0.setText(String.valueOf(attendance.group_num));
                    holder.tvDay3.setText(String.valueOf(attendance.private_num));
                    break;
                case 3:
                    holder.labelUnit1.setText(context.getString(R.string.unit_day));
                    holder.labelUnit2.setText(context.getString(R.string.unit_time));
                    holder.labelUnit3.setText(context.getString(R.string.unit_class));
                    holder.labelUnit0.setText(context.getString(R.string.unit_class));

                    holder.tvAction1.setText(context.getString(R.string.lable_attendace));
                    holder.tvAction2.setText(context.getString(R.string.label_checkin));
                    holder.tvAction3.setText(context.getString(R.string.course_type_group));
                    holder.tvAction0.setText(context.getString(R.string.attendance_private_course));

                    holder.tvDay1.setText(String.valueOf(attendance.days));
                    holder.tvDay2.setText(String.valueOf(attendance.checkin));
                    holder.tvDay3.setText(String.valueOf(attendance.group_num));
                    holder.tvDay0.setText(String.valueOf(attendance.private_num));
                    break;
                default:
                    holder.labelUnit0.setText(context.getString(R.string.unit_day));
                    holder.labelUnit1.setText(context.getString(R.string.unit_time));
                    holder.labelUnit2.setText(context.getString(R.string.unit_class));
                    holder.labelUnit3.setText(context.getString(R.string.unit_class));

                    holder.tvAction0.setText(context.getString(R.string.lable_attendace));
                    holder.tvAction1.setText(context.getString(R.string.label_checkin));
                    holder.tvAction2.setText(context.getString(R.string.course_type_group));
                    holder.tvAction3.setText(context.getString(R.string.attendance_private_course));

                    holder.tvDay0.setText(String.valueOf(attendance.days));
                    holder.tvDay1.setText(String.valueOf(attendance.checkin));
                    holder.tvDay2.setText(String.valueOf(attendance.group_num));
                    holder.tvDay3.setText(String.valueOf(attendance.private_num));
                    break;
            }
            if (adapter instanceof CommonFlexAdapter) {
                Object revert = ((CommonFlexAdapter) adapter).getTag("revert");
                if (revert instanceof Boolean) {
                    boolean isRevert = (boolean) revert;
                    if (position < 3) {//根据类型和位置设置颜色
                        @ColorRes int c = isRevert ? revertColor[position] : colorRes[type][position];
                        holder.tvDay0.setTextColor(CompatUtils.getColor(context, c));
                        holder.labelUnit0.setTextColor(CompatUtils.getColor(context, c));
                        holder.tvAction0.setTextColor(CompatUtils.getColor(context, c));
                    } else {
                        holder.tvDay0.setTextColor(CompatUtils.getColor(context, R.color.text_black));
                        holder.labelUnit0.setTextColor(CompatUtils.getColor(context, R.color.text_grey));
                        holder.tvAction0.setTextColor(CompatUtils.getColor(context, R.color.text_grey));
                    }
                }
            }
        }

        if (attendance != null) {
            if (attendance.student != null) {
                Glide.with(holder.itemView.getContext())
                    .load(PhotoUtils.getSmall(attendance.student.getAvatar()))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.imgAvatar, holder.imgAvatar.getContext()));
                holder.icGender.setImageResource(
                    attendance.student.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
                holder.tvName.setText(attendance.student.getUsername());
                holder.tvPhone.setText(attendance.student.getPhone());
            }
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AttendanceRankVH extends FlexibleViewHolder {
        @BindView(R.id.tv_day_0) TextView tvDay0;
        @BindView(R.id.tv_action_0) TextView tvAction0;
        @BindView(R.id.label_unit_0) TextView labelUnit0;
        @BindView(R.id.img_avatar) ImageView imgAvatar;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_phone) TextView tvPhone;
        @BindView(R.id.ic_gender) ImageView icGender;
        @BindView(R.id.tv_day_1) TextView tvDay1;
        @BindView(R.id.tv_action_1) TextView tvAction1;
        @BindView(R.id.label_unit_1) TextView labelUnit1;
        @BindView(R.id.tv_day_2) TextView tvDay2;
        @BindView(R.id.tv_action_2) TextView tvAction2;
        @BindView(R.id.label_unit_2) TextView labelUnit2;
        @BindView(R.id.tv_day_3) TextView tvDay3;
        @BindView(R.id.tv_action_3) TextView tvAction3;
        @BindView(R.id.label_unit_3) TextView labelUnit3;

        public AttendanceRankVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}