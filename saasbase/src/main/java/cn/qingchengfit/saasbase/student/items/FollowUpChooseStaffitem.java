package cn.qingchengfit.saasbase.student.items;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FollowUpChooseStaffitem extends ChooseStaffItem {
    public FollowUpChooseStaffitem(Staff staff) {
        super(staff);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ChooseStaffVH holder, int position, List payloads) {
        if ("-1".equals(staff.id)) {// 全部
            int avatarRes=-1;
            //if (adapter.isSelected(position)) {
            //    avatarRes = R.drawable.ic_all_selected;
            //} else {
            //    avatarRes = R.drawable.ic_all_normal;
            //}
            Glide.with(holder.itemView.getContext())
                    .load(avatarRes)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        } else if ("0".equals(staff.id)) { // 未分配销售
            int avatarRes;
            if (adapter.isSelected(position)) {
                avatarRes = R.drawable.ic_nosales_selected;
            } else {
                avatarRes = R.drawable.ic_nosales_normal;
            }
            Glide.with(holder.itemView.getContext())
                    .load(avatarRes)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        }
        super.bindViewHolder(adapter, holder, position, payloads);


    }
}