package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.SignInEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignOutManualEvent;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignOutItem extends AbstractFlexibleItem<SignOutItem.ItemViewHolder> {

    public SignInTasks.SignInTask bean;
    public boolean isManual = false;

    public SignOutItem(SignInTasks.SignInTask bean) {
        this.bean = bean;
    }

    public SignOutItem(SignInTasks.SignInTask bean, boolean isManual) {
        this.bean = bean;
        this.isManual = isManual;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_signout_untreated;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {

        holder.tv_signout_item_name.setText(bean.getUserName());
        holder.tv_signout_item_phone.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_phone, bean.getUserPhone()));
        if (bean.getUserGender() == 0) {//男
            holder.iv_signout_item_gender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.iv_signout_item_gender.setImageResource(R.drawable.ic_gender_signal_female);
        }

        Glide.with(adapter.getRecyclerView().getContext())
            .load(PhotoUtils.getSmall(bean.getCheckinAvatar()))
            .asBitmap()
            .placeholder(R.drawable.img_default_checkinphoto)
            .error(R.drawable.img_default_checkinphoto)
            .into(holder.img_signout_item_face);

        if (!TextUtils.isEmpty(bean.getCheckinAvatar())) {
            Glide.with(adapter.getRecyclerView().getContext())
                .load(PhotoUtils.getSmall(bean.getCheckinAvatar()))
                .asBitmap()
                .into(holder.img_signout_item_face);
        } else {
            Glide.with(adapter.getRecyclerView().getContext())
                .load(R.drawable.img_default_checkinphoto)
                .asBitmap()
                .into(holder.img_signout_item_face);
        }

        String balance = "";
        if (bean.getCard().getCardType() != null && bean.getCard().getBalance() != null) {
            if (bean.getCard().getCardType() == Configs.CATEGORY_VALUE) {
                holder.tv_signout_item_fee.setVisibility(View.VISIBLE);
                balance = TextUtils.concat("(余额:", String.valueOf(bean.getCard().getBalance()), "元)").toString();
            } else if (bean.getCard().getCardType() == Configs.CATEGORY_TIMES) {
                holder.tv_signout_item_fee.setVisibility(View.VISIBLE);
                balance = TextUtils.concat("(余额:", String.valueOf(bean.getCard().getBalance()), "次)").toString();
            } else {
                balance =
                    TextUtils.concat("(有效期:", bean.getCard().getStart().split("T")[0], "--", bean.getCard().getEnd().split("T")[0], ")")
                        .toString();
                holder.tv_signout_item_fee.setVisibility(View.GONE);
            }
        }

        StringBuffer card = new StringBuffer();
        card.append(bean.getCard().getName()).append(balance);
        holder.tv_signout_item_card.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_card, card.toString()));
        holder.tv_signout_item_fee.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_fee, bean.getCost() + bean.getUnit()));
        holder.tv_signout_item_gui.setText(adapter.getRecyclerView()
            .getContext()
            .getString(R.string.sign_out_untreated_gui, TextUtils.isEmpty(bean.getLocker().name) ? "" : bean.getLocker().name));
        holder.tv_signout_item_time.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_out_untreated_time, bean.getCreatedAt().replace("T", " ")));

        if (isManual) {
            holder.ll_signout_item_ignor.setVisibility(View.GONE);
        }

        Drawable drawableIgnor = ContextCompat.getDrawable(adapter.getRecyclerView().getContext(), R.drawable.ic_sign_in_ignor);
        drawableIgnor.setBounds(0, 0, drawableIgnor.getMinimumWidth(), drawableIgnor.getMinimumHeight());
        holder.tv_signout_item_ignor.setCompoundDrawables(drawableIgnor, null, null, null);

        Drawable drawableS = ContextCompat.getDrawable(adapter.getRecyclerView().getContext(), R.drawable.ic_sign_out_confirm);
        drawableS.setBounds(0, 0, drawableS.getMinimumWidth(), drawableS.getMinimumHeight());
        holder.tv_signout_item_confirm.setCompoundDrawables(drawableS, null, null, null);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        @BindView(R.id.img_signout_item_face) ImageView img_signout_item_face;
        @BindView(R.id.tv_signout_item_name) TextView tv_signout_item_name;
        @BindView(R.id.tv_signout_item_phone) TextView tv_signout_item_phone;
        @BindView(R.id.tv_signout_item_card) TextView tv_signout_item_card;
        @BindView(R.id.tv_signout_item_fee) TextView tv_signout_item_fee;
        @BindView(R.id.iv_signout_item_gender) ImageView iv_signout_item_gender;

        @BindView(R.id.ll_signout_item_ignor) LinearLayout ll_signout_item_ignor;
        @BindView(R.id.ll_signout_item_confirm) LinearLayout ll_signout_item_confirm;

        @BindView(R.id.tv_signout_item_ignor) TextView tv_signout_item_ignor;
        @BindView(R.id.tv_signout_item_confirm) TextView tv_signout_item_confirm;

        @BindView(R.id.tv_signout_item_gui) TextView tv_signout_item_gui;
        @BindView(R.id.tv_signout_item_time) TextView tv_signout_item_time;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            ll_signout_item_ignor.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    int checkId = ((SignOutItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                    RxBus.getBus()
                        .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNOUT_IGNOR)
                            .position(getAdapterPosition())
                            .checkInId(checkId)
                            .build());
                }
            });
            ll_signout_item_confirm.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    if (((SignOutItem) mAdapter.getItem(getAdapterPosition())).isManual) {
                        int checkId = ((SignOutItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                        RxBus.getBus().post(new SignOutManualEvent(getAdapterPosition(), checkId));
                    } else {
                        int checkId = ((SignOutItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                        RxBus.getBus()
                            .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNOUT_CONFIRM)
                                .position(getAdapterPosition())
                                .checkInId(checkId)
                                .build());
                    }
                }
            });
            img_signout_item_face.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    if (((SignOutItem) mAdapter.getItem(getAdapterPosition())).isManual) {
                        int checkId = ((SignOutItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                        RxBus.getBus()
                            .post(new SignOutManualEvent.Builder().checkInId(checkId)
                                .position(getAdapterPosition())
                                .type(SignInEvent.ACTION_SIGNOUT_MANUAL)
                                .build());
                    } else {
                        int checkId = ((SignOutItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                        RxBus.getBus()
                            .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNOUT_ADD_IMG)
                                .position(getAdapterPosition())
                                .checkInId(checkId)
                                .build());
                    }
                }
            });
        }
    }
}