package cn.qingchengfit.saasbase.items;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.student.widget.CircleView;
import cn.qingchengfit.utils.CircleImgWrapper;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class DelStudentItem extends AbstractFlexibleItem<DelStudentItem.ChooseStudentVH> {
    private Personage user;

    private boolean isSignuUp;

    public DelStudentItem(Personage user) {
        this.user = user;
    }

    public Personage getUser() {
        return user;
    }

    public void setUser(QcStudentBean user) {
        this.user = user;
    }

    public void setSignuUp(boolean signuUp) {
        isSignuUp = signuUp;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_delete;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemPersonGender.setImageResource(
            user.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        holder.itemPersonName.setText(user.getUsername());
        holder.itemPersonPhonenum.setText(user.getPhone());

        if (user instanceof QcStudentBean) {
            holder.status.setVisibility(View.VISIBLE);
            holder.itemPersonDesc.setVisibility(View.VISIBLE);
            studentStatus(holder.status, ((QcStudentBean) user).status);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.sales_sb, ((QcStudentBean) user).getSellersStr()));
            if (!isSignuUp) {
                holder.itemPersonDesc.setText(
                    holder.itemView.getContext().getString(R.string.sales_sb, ((QcStudentBean) user).getSellersStr()));
            }
        } else {
            holder.itemPersonDesc.setVisibility(View.GONE);
            holder.status.setVisibility(View.GONE);
        }
        Glide.with(holder.itemView.getContext())
            .load(user.getAvatar())
            .asBitmap()
            .placeholder(user.getGender() == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .into(new CircleImgWrapper(holder.itemPersonHeader, holder.itemView.getContext()));
    }

    /**
     * 变更销售-会员状态
     *
     * @param view view
     * @param status status
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public static void studentStatus(TextView view, int status) {
        String statuStr = "";
        Drawable drawable = null;
        switch (Integer.valueOf(status)) {
            case 0:
                statuStr = "新注册";
                drawable = new CircleView(view.getContext().getResources().getColor(R.color.qc_student_status_0));
                break;
            case 1:
                statuStr = "已接洽";
                drawable = new CircleView(view.getContext().getResources().getColor(R.color.qc_student_status_1));
                break;
            case 2:
                statuStr = "会员";
                drawable = new CircleView(view.getContext().getResources().getColor(R.color.qc_student_status_2));
                break;
            default:
                statuStr = "未知";
                drawable = new CircleView(view.getContext().getResources().getColor(R.color.qc_student_status_0));
                break;
        }
        view.setText(statuStr);
        drawable.setBounds(0, 0, 26, 26);
        view.setCompoundDrawablePadding(10);
        view.setCompoundDrawables(drawable, null, null, null);
    }


    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseStudentVH extends FlexibleViewHolder {
	ImageView del;
	ImageView itemPersonHeader;
	RelativeLayout itemPersonHeaderLoop;
	TextView itemPersonName;
	ImageView itemPersonGender;
	TextView itemPersonPhonenum;
	TextView itemPersonDesc;
	TextView status;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          del = (ImageView) view.findViewById(R.id.del);
          itemPersonHeader = (ImageView) view.findViewById(R.id.item_person_header);
          itemPersonHeaderLoop = (RelativeLayout) view.findViewById(R.id.item_person_header_loop);
          itemPersonName = (TextView) view.findViewById(R.id.item_person_name);
          itemPersonGender = (ImageView) view.findViewById(R.id.item_person_gender);
          itemPersonPhonenum = (TextView) view.findViewById(R.id.item_person_phonenum);
          itemPersonDesc = (TextView) view.findViewById(R.id.item_person_desc);
          status = (TextView) view.findViewById(R.id.status);

          del.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            if (mAdapter.mItemClickListener != null) {
                mAdapter.mItemClickListener.onItemClick(getAdapterPosition());
            }
            super.onClick(view);
        }
    }
}