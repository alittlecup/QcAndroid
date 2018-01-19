package cn.qingchengfit.items;

import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Staff;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseStaffItem extends AbstractFlexibleItem<ChooseStaffItem.ChooseStaffVH>
    implements ISectionable<ChooseStaffItem.ChooseStaffVH, AlphabetHeaderItem>, IFilterable {
    public static final int COMMON = 0;
    public static final int UNDER_GYM = 1;
    Staff staff;
    int choosetype; // 0 在全部场馆下  1在场馆下
    private AlphabetHeaderItem headerItem;

    public ChooseStaffItem(Staff staff) {
        this.staff = staff;
    }

    public ChooseStaffItem(Staff staff, AlphabetHeaderItem headerItem) {
        this.staff = staff;
        this.headerItem = headerItem;
    }

    public ChooseStaffItem(Staff staff, int choosetype) {
        this.staff = staff;
        this.choosetype = choosetype;
    }

    public Staff getStaff() {
        return staff;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_staff;
    }

    @Override public ChooseStaffVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStaffVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStaffVH holder, int position, List payloads) {
        holder.checkbox.setChecked(DirtySender.studentList.contains(staff));
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(staff.getAvatar()))
            .asBitmap()
            .placeholder(staff.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .into(new CircleImgWrapper(holder.imgAvatar, holder.itemView.getContext()));
        if (choosetype == 0) {
            holder.tvName.setText(staff.getUsername());
            holder.tvContent.setText(staff.getPhone());
            holder.space.setVisibility(View.GONE);
        } else if (choosetype == 1) {
            holder.space.setVisibility(View.VISIBLE);
            holder.tvName.setText(staff.getUsername() + "(" + staff.getPhone() + ")");
            //holder.tvContent.setText();职位

        }
    }

    @Override public boolean equals(Object o) {
        if (o instanceof ChooseStaffItem) {
            return ((ChooseStaffItem) o).staff.getId().equals(staff.getId());
        } else {
            return false;
        }
    }

    @Override public AlphabetHeaderItem getHeader() {
        return headerItem;
    }

    @Override public void setHeader(AlphabetHeaderItem item) {
        this.headerItem = item;
    }

    @Override public boolean filter(String s) {
        return staff.getUsername().contains(s) || staff.getPhone().contains(s);
    }

    public class ChooseStaffVH extends FlexibleViewHolder {
        @BindView(R.id.space) Space space;
        @BindView(R.id.checkbox) CheckBox checkbox;
        @BindView(R.id.img_avatar) ImageView imgAvatar;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_content) TextView tvContent;

        public ChooseStaffVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkbox.setActivated(b);
                }
            });
        }
    }
}