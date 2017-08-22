package cn.qingchengfit.staffkit.train.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.GroupBean;
import cn.qingchengfit.staffkit.train.model.SignPersonalBean;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.AutoLineGroup;
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

public class AddGroupMemberItem extends AbstractFlexibleItem<AddGroupMemberItem.AddGroupMemberVH> {

    private SignPersonalBean data;
    private Context context;

    public AddGroupMemberItem(SignPersonalBean data, Context context) {
        this.data = data;
        this.context = context;
    }

    public QcStudentBean getData() {
        return data;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AddGroupMemberVH holder, int position, List payloads) {

        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(data.avatar()))
            .asBitmap()
            .into(new CircleImgWrapper(holder.headAddGroupMember, holder.itemView.getContext()));
        holder.textAddMemberName.setText(data.username());
        holder.textAddMemberPhone.setText(data.phone());
        holder.imageAddMemberGender.setImageResource(
            data.gender().equals("0") ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        holder.addMemberItemCheckbox.setChecked(adapter.isSelected(position));

        holder.groupAuto.removeAllViews();

        for (GroupBean bean : data.teams) {
            TextView textView = new TextView(context);
            holder.icformGroup.setBounds(0, 0, holder.icformGroup.getMinimumWidth(), holder.icformGroup.getMinimumHeight());
            textView.setPadding(MeasureUtils.dpToPx(8f, context.getResources()), 6, MeasureUtils.dpToPx(8f, context.getResources()), 6);
            textView.setCompoundDrawables(holder.icformGroup, null, null, null);
            textView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_border_cornor_grey));
            textView.setText(bean.name);
            textView.setTextSize(12);
            holder.groupAuto.addView(textView);
        }

        if (holder.groupAuto.getLayoutParams().height < 0) {
            holder.groupAuto.invalidate();
        }
    }

    @Override public int getLayoutRes() {
        return R.layout.item_add_group_member;
    }

    @Override public AddGroupMemberVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AddGroupMemberVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    class AddGroupMemberVH extends FlexibleViewHolder {

        @BindView(R.id.head_add_group_member) ImageView headAddGroupMember;
        @BindView(R.id.add_member_item_checkbox) CheckBox addMemberItemCheckbox;
        @BindView(R.id.text_add_member_name) TextView textAddMemberName;
        @BindView(R.id.image_add_member_gender) ImageView imageAddMemberGender;
        @BindView(R.id.text_add_member_phone) TextView textAddMemberPhone;
        @BindView(R.id.group_auto) AutoLineGroup groupAuto;
        @BindDrawable(R.drawable.ic_form_group) Drawable icformGroup;
        @BindView(R.id.rl_information) RelativeLayout rlInformation;

        public AddGroupMemberVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            rlInformation.setId(R.id.rl_information);
            groupAuto.setSpacing(16, 10);
            groupAuto.getLayoutParams().height = MeasureUtils.dpToPx(24f, getContentView().getContext().getResources());
        }
    }
}
