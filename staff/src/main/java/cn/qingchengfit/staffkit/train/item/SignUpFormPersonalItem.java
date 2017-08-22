package cn.qingchengfit.staffkit.train.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.GroupBean;
import cn.qingchengfit.staffkit.train.model.SignPersonalBean;
import cn.qingchengfit.utils.DateUtils;
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
 * Created by fb on 2017/3/22.
 */

public class SignUpFormPersonalItem extends AbstractFlexibleItem<SignUpFormPersonalItem.PersonalVH> {

    private SignPersonalBean datas;
    private Context context;

    public SignUpFormPersonalItem(Context context, SignPersonalBean datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public SignPersonalBean getData() {
        return datas;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_sign_up;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, PersonalVH holder, int position, List payloads) {

        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(datas.avatar()))
            .asBitmap()
            .placeholder(datas.gender().equals("0") ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .error(R.drawable.default_manage_male)
            .into(new CircleImgWrapper(holder.circleImg, holder.itemView.getContext()));

        holder.textSignName.setText(datas.username());
        holder.imgSignGender.setImageDrawable(
            datas.gender().equals("0") ? ContextCompat.getDrawable(context, R.drawable.ic_gender_signal_male)
                : ContextCompat.getDrawable(context, R.drawable.ic_gender_signal_female));
        holder.textSignPhone.setText(datas.phone());
        holder.textSignTime.setText(
            context.getString(R.string.sign_up_personal_time, DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(datas.created_at))));
        holder.textSignCost.setText(context.getString(R.string.sign_up_personal_cost, datas.order == null ? "0" : datas.order.getPrice()));

        holder.llSignGroup.removeAllViews();

        for (GroupBean bean : datas.teams) {
            TextView textView = new TextView(holder.itemView.getContext());
            holder.icformGroup.setBounds(0, 0, holder.icformGroup.getMinimumWidth(), holder.icformGroup.getMinimumHeight());
            textView.setPadding(MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()), 6,
                MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()), 6);
            textView.setCompoundDrawables(holder.icformGroup, null, null, null);
            textView.setCompoundDrawablePadding(MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()));
            textView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_border_cornor_grey));
            textView.setText(bean.name);
            textView.setTextSize(12);
            holder.llSignGroup.addView(textView);
        }
    }

    @Override public PersonalVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new PersonalVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    class PersonalVH extends FlexibleViewHolder {

        @BindView(R.id.image_sign_head) ImageView circleImg;
        @BindView(R.id.text_sign_name) TextView textSignName;
        @BindView(R.id.img_sign_gender) ImageView imgSignGender;
        @BindView(R.id.text_sign_phone) TextView textSignPhone;
        @BindView(R.id.text_sign_time) TextView textSignTime;
        @BindView(R.id.text_sign_cost) TextView textSignCost;
        @BindView(R.id.ll_sign_group) AutoLineGroup llSignGroup;
        @BindDrawable(R.drawable.ic_form_group) Drawable icformGroup;

        public PersonalVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            llSignGroup.setSpacing(16, 10);
        }
    }
}
