package cn.qingchengfit.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.utils.BusinessUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class DelStudentItem extends AbstractFlexibleItem<DelStudentItem.ChooseStudentVH> {
    private Personage user;

    public DelStudentItem(Personage user) {
        this.user = user;
    }

    public Personage getUser() {
        return user;
    }

    public void setUser(QcStudentBean user) {
        this.user = user;
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
            BusinessUtils.studentStatus(holder.status, ((QcStudentBean) user).status);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.sales_sb, ((QcStudentBean) user).getSellersStr()));
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
            super.onClick(view);
            if (mAdapter.mItemClickListener != null) {
                mAdapter.mItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}