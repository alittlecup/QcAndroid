package cn.qingchengfit.items;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.utils.BusinessUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseStudentItem extends AbstractFlexibleItem<ChooseStudentItem.ChooseStudentVH>
    implements IFilterable, ISectionable<ChooseStudentItem.ChooseStudentVH, AlphabetHeaderItem> {
    private QcStudentBean user;

    private AlphabetHeaderItem headerItem;

    public ChooseStudentItem(QcStudentBean user, AlphabetHeaderItem item) {
        this.user = user;
        this.headerItem = item;
    }

    public String getId() {
        return user.getId();
    }

    public QcStudentBean getUser() {
        return user;
    }

    public void setUser(QcStudentBean user) {
        this.user = user;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_student_new;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemPersonGender.setImageResource(
            user.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        if (adapter.hasSearchText()) {
            FlexibleUtils.highlightWords( holder.itemPersonName, user.getUsername(), adapter.getSearchText(),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            FlexibleUtils.highlightWords( holder.itemPersonPhonenum, user.getPhone(), adapter.getSearchText(),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        } else {
            holder.itemPersonName.setText(user.getUsername());
            holder.itemPersonPhonenum.setText(user.getPhone());
        }
        holder.itemPersonDesc.setText(holder.itemView.getContext().getString(R.string.sales_sb, user.getSellersStr()));
        BusinessUtils.studentStatus(holder.status, user.status);
        holder.cb.setChecked(adapter.isSelected(position));
        Glide.with(holder.itemView.getContext())
            .load(user.getAvatar())
            .asBitmap()
            .placeholder(user.gender().equals("0") ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .into(new CircleImgWrapper(holder.itemPersonHeader, holder.itemView.getContext()));
    }

    @Override public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof ChooseStudentItem) {
            return ((ChooseStudentItem) o).getId().equals(this.getId());
        } else if (o instanceof QcStudentBean) {
            return ((QcStudentBean) o).getId().equals(this.getId());
        }
        return false;
    }

    @Override public boolean filter(String constraint) {
        if (constraint == null || constraint.equals("")) return true;
        return user.username().contains(constraint) || user.getPhone().contains(constraint);
    }

    @Override public AlphabetHeaderItem getHeader() {
        return headerItem;
    }

    @Override public void setHeader(AlphabetHeaderItem header) {
        this.headerItem = header;
    }

    public class ChooseStudentVH extends FlexibleViewHolder {
        @BindView(R.id.cb) CheckBox cb;
        @BindView(R.id.item_person_header) ImageView itemPersonHeader;
        @BindView(R.id.item_person_header_loop) RelativeLayout itemPersonHeaderLoop;
        @BindView(R.id.item_person_name) TextView itemPersonName;
        @BindView(R.id.item_person_gender) ImageView itemPersonGender;
        @BindView(R.id.item_person_phonenum) TextView itemPersonPhonenum;
        @BindView(R.id.item_person_desc) TextView itemPersonDesc;
        @BindView(R.id.status) TextView status;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}