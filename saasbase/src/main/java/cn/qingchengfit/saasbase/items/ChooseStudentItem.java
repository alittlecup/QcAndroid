package cn.qingchengfit.saasbase.items;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.student.widget.CircleView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
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
        return R.layout.item_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemPersonGender.setImageResource(
            user.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        if (adapter.hasSearchText()) {
            FlexibleUtils.highlightWords( holder.itemPersonName, user.getUsername(), adapter.getSearchText());
            FlexibleUtils.highlightWords( holder.itemPersonPhonenum, user.getPhone(), adapter.getSearchText());
        } else {
            holder.itemPersonName.setText(user.getUsername());
            holder.itemPersonPhonenum.setText(user.getPhone());
        }
        holder.itemPersonDesc.setText(holder.itemView.getContext().getString(R.string.sales_sb, user.getSellersStr()));
        studentStatus(holder.status, user.status);
        holder.cb.setChecked(adapter.isSelected(position));
        Glide.with(holder.itemView.getContext())
            .load(user.getAvatar())
            .asBitmap()
            .placeholder((user.gender() == null || user.gender() == 0 )? R.drawable.default_manage_male : R.drawable.default_manager_female)
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
	CheckBox cb;
	ImageView itemPersonHeader;
	RelativeLayout itemPersonHeaderLoop;
	TextView itemPersonName;
	ImageView itemPersonGender;
	TextView itemPersonPhonenum;
	TextView itemPersonDesc;
	TextView status;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          cb = (CheckBox) view.findViewById(R.id.cb);
          itemPersonHeader = (ImageView) view.findViewById(R.id.item_person_header);
          itemPersonHeaderLoop = (RelativeLayout) view.findViewById(R.id.item_person_header_loop);
          itemPersonName = (TextView) view.findViewById(R.id.item_person_name);
          itemPersonGender = (ImageView) view.findViewById(R.id.item_person_gender);
          itemPersonPhonenum = (TextView) view.findViewById(R.id.item_person_phonenum);
          itemPersonDesc = (TextView) view.findViewById(R.id.item_person_desc);
          status = (TextView) view.findViewById(R.id.status);
        }
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

}