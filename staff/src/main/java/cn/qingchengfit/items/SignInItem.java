package cn.qingchengfit.items;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import cn.qingchengfit.staffkit.views.adapter.SignInCourseAdapter;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
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
public class SignInItem extends AbstractFlexibleItem<SignInItem.ItemViewHolder> {

    public SignInTasks.SignInTask bean;

    public SignInItem(SignInTasks.SignInTask bean) {
        this.bean = bean;
    }

    public SignInTasks.SignInTask getBean() {
        return bean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_signin_untreated;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {

        holder.tvSigninItemName.setText(bean.getUserName());
        holder.tvSigninItemPhone.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_phone, bean.getUserPhone()));
        if (bean.getUserGender() == 0) {//男
            holder.ivSigninItemGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.ivSigninItemGender.setImageResource(R.drawable.ic_gender_signal_female);
        }

        Glide.with(adapter.getRecyclerView().getContext())
            .load(PhotoUtils.getSmall(bean.getCheckinAvatar()))
            .asBitmap()
            .placeholder(R.drawable.img_default_checkinphoto)
            .error(R.drawable.img_default_checkinphoto)
            .into(holder.imgSigninItemFace);

        //        if (!TextUtils.isEmpty(bean.getCheckinAvatar())) {
        //            Glide.with(App.context).load(PhotoUtils.getSmall(bean.getCheckinAvatar())).asBitmap()
        //                    .into(holder.imgSigninItemFace);
        //        } else {
        //            Glide.with(App.context).load(R.drawable.img_default_checkinphoto).asBitmap()
        //                    .into(holder.imgSigninItemFace);
        //        }

        String balance = "";
        if (bean.getCard().getCardType() != null && bean.getCard().getBalance() != null) {
            if (bean.getCard().getCardType() == Configs.CATEGORY_VALUE) {
                holder.tvSigninItemFee.setVisibility(View.VISIBLE);
                balance = TextUtils.concat("(余额:", String.valueOf(bean.getCard().getBalance()), "元)").toString();
            } else if (bean.getCard().getCardType() == Configs.CATEGORY_TIMES) {
                holder.tvSigninItemFee.setVisibility(View.VISIBLE);
                balance = TextUtils.concat("(余额:", String.valueOf(bean.getCard().getBalance()), "次)").toString();
            } else {
                balance =
                    TextUtils.concat("(有效期:", bean.getCard().getStart().split("T")[0], "--", bean.getCard().getEnd().split("T")[0], ")")
                        .toString();
                holder.tvSigninItemFee.setVisibility(View.GONE);
            }
        }

        StringBuffer card = new StringBuffer();
        card.append(bean.getCard().getName()).append(balance);
        holder.tvSigninItemCard.setText(adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_card, card.toString()));
        holder.tvSigninItemFee.setText(
            adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_fee, bean.getCost() + bean.getUnit()));

        List<SignInTasks.Schedule> list = bean.getSchedules();
        if (list != null && !list.isEmpty()) {
            holder.llSigninItemCourse.setVisibility(View.VISIBLE);
            holder.tvSigninItemCourse.setText(
                adapter.getRecyclerView().getContext().getString(R.string.sign_in_untreated_class, list.size()));
            SignInCourseAdapter courseAdapter = new SignInCourseAdapter(list);
            holder.recyclerviewSigninItemCourse.setLayoutManager(new LinearLayoutManager(adapter.getRecyclerView().getContext()));
            holder.recyclerviewSigninItemCourse.setAdapter(courseAdapter);
            holder.cardView.setCardElevation(0);
        } else {
            holder.llSigninItemCourse.setVisibility(View.GONE);
            holder.cardView.setCardElevation(6);
        }

        //        Bitmap bmp= BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_bottom_line);
        //        BitmapDrawable background = new BitmapDrawable(App.context.getResources(),bmp);
        //        background.setTileModeX(Shader.TileMode.REPEAT);
        //        holder.ll_line.setBackground(background);

        if (bean.getLocker() != null) {
            holder.tvSigninItemGui.setText(bean.getLocker().name);
        } else {
            holder.tvSigninItemGui.setText("");
        }

        if (SignInActivity.checkinWithLocker == 1) {
            holder.llSigninUntreatedLocker.setVisibility(View.VISIBLE);
        } else {
            holder.llSigninUntreatedLocker.setVisibility(View.GONE);
        }

        Drawable drawableIgnor = ContextCompat.getDrawable(adapter.getRecyclerView().getContext(), R.drawable.ic_sign_in_ignor);
        drawableIgnor.setBounds(0, 0, drawableIgnor.getMinimumWidth(), drawableIgnor.getMinimumHeight());
        holder.tvSigninItemIgnor.setCompoundDrawables(drawableIgnor, null, null, null);

        Drawable drawableS = ContextCompat.getDrawable(adapter.getRecyclerView().getContext(), R.drawable.ic_sign_in_selected);
        drawableS.setBounds(0, 0, drawableS.getMinimumWidth(), drawableS.getMinimumHeight());
        holder.tvSigninItemConfirm.setCompoundDrawables(drawableS, null, null, null);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        @BindView(R.id.cardview) CardView cardView;
        @BindView(R.id.img_signin_item_face) ImageView imgSigninItemFace;
        @BindView(R.id.tv_signin_item_name) TextView tvSigninItemName;
        @BindView(R.id.iv_signin_item_gender) ImageView ivSigninItemGender;
        @BindView(R.id.tv_signin_item_phone) TextView tvSigninItemPhone;
        @BindView(R.id.tv_signin_item_card) TextView tvSigninItemCard;
        @BindView(R.id.tv_signin_item_fee) TextView tvSigninItemFee;
        @BindView(R.id.tv_signin_item_gui) TextView tvSigninItemGui;
        @BindView(R.id.tv_signin_item_ignor) TextView tvSigninItemIgnor;
        @BindView(R.id.ll_signin_item_ignor) LinearLayout llSigninItemIgnor;
        @BindView(R.id.tv_signin_item_confirm) TextView tvSigninItemConfirm;
        @BindView(R.id.ll_signin_item_confirm) LinearLayout llSigninItemConfirm;
        @BindView(R.id.ll_signin_item_course) LinearLayout llSigninItemCourse;
        @BindView(R.id.ll_line) LinearLayout ll_line;
        @BindView(R.id.tv_signin_item_course) TextView tvSigninItemCourse;
        @BindView(R.id.recyclerview_signin_item_course) RecyclerView recyclerviewSigninItemCourse;
        @BindView(R.id.ll_signin_untreated_locker) LinearLayout llSigninUntreatedLocker;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            llSigninItemIgnor.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    int checkId = ((SignInItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                    RxBus.getBus()
                        .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNIN_IGNOR)
                            .position(getAdapterPosition())
                            .checkInId(checkId)
                            .build());
                }
            });
            llSigninItemConfirm.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    int checkId = ((SignInItem) mAdapter.getItem(getAdapterPosition())).bean.getId();
                    RxBus.getBus()
                        .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNIN_CONFIRM)
                            .position(getAdapterPosition())
                            .checkInId(checkId)
                            .build());
                }
            });
            tvSigninItemGui.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    RxBus.getBus()
                        .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNIN_LOCKER).position(getAdapterPosition()).build());
                }
            });
            imgSigninItemFace.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (!PreferenceUtils.getPrefBoolean(App.context, "showNotice" + App.staffId, false)) {
                        ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
                        return;
                    }
                    RxBus.getBus()
                        .post(new SignInEvent.Builder().action(SignInEvent.ACTION_SIGNIN_ADD_IMG).position(getAdapterPosition()).build());
                }
            });
        }
    }
}
