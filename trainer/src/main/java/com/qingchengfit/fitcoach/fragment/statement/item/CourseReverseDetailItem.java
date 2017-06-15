package com.qingchengfit.fitcoach.fragment.statement.item;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.statement.model.CourseReportOrder;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fb on 2017/5/11.
 */

public class CourseReverseDetailItem extends AbstractFlexibleItem<CourseReverseDetailItem.CourseReverseVH> {

    private CourseReportOrder order;
    private SpannableString sb;
    private Pattern pattern;
    private Matcher matcher;

    public CourseReverseDetailItem(CourseReportOrder order) {
        this.order = order;
    }

    public CourseReportOrder getData() {
        return order;
    }

    @Override public CourseReverseVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CourseReverseVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CourseReverseVH holder, int position, List payloads) {

        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(order.user.avatar))
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemPersonalHeader, holder.itemView.getContext()));
        holder.itemPersonalName.setText(order.user.username());
        holder.itemPersonalGender.setImageResource(
            order.user.gender() == 1 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        holder.itemPersonPhone.setText(order.user.phone());
        holder.tvStudentStatus.setText(setStatus(order.status));
        holder.textReverseTime.setText(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(order.created_at)));
        setCardIcon(holder, order.channel);

        holder.textReportReverseNumber.setText(String.valueOf(order.count));
        holder.textReportReverseNumber.setTextSize(15);
        if (order.card != null) {
            if (order.card.card_type == 3) {
                holder.textReportReverseIncome.setText("－－");
                holder.textReportReverseFact.setText("－－");
            } else {
                holder.textReportReverseIncome.setText(StringUtils.getFloatDot2(order.total_price) + setIncomeUnit(order.card.card_type));
                holder.textReportReverseFact.setText("¥" + StringUtils.getFloatDot2(order.total_real_price));
            }
        } else {
            holder.textReportReverseIncome.setText(StringUtils.getFloatDot2(order.total_price) + "元");
            holder.textReportReverseFact.setText("¥" + StringUtils.getFloatDot2(order.total_real_price));
        }
    }

    private String setStatus(int status) {
        String s = "";
        switch (status) {
            case 1:
                s = "已完成";
                break;
            case 2:
                s = "已取消";
                break;
            case 3:
                s = "失败";
                break;
            case 4:
                s = "已预约";
                break;
        }
        return s;
    }

    private void setCardIcon(CourseReverseVH holder, String cardType) {
        int drawable = 0;
        switch (cardType) {
            case "CARD":
                drawable = R.drawable.ic_credit_pay_yellow;
                holder.textPayWay.setText(order.card.card_name + " ( ID:" + order.card.id + ")");
                break;
            case "FREE":
                drawable = R.drawable.ic_payment_free;
                holder.textPayWay.setText("无需结算");
                break;
            case "ONLINE":
                //drawable = R.drawable.ic_pay_online;
                //holder.textPayWay.setText("在线支付");
                //break;
            case "WEIDONG":
            case "WEIXIN":
            case "WEIXIN_QRCODE":
            default:
                drawable = R.drawable.ic_pay_wechat;
                holder.textPayWay.setText("微信支付");
                break;
        }
        holder.imgPayWay.setImageResource(drawable);
    }

    private String setIncomeUnit(int type) {
        String unit = "";
        switch (type) {
            case 1:
                unit = "元";
                break;
            case 2:
                unit = "次";
                break;
            case 3:
                break;
        }
        return unit;
    }

    private SpannableString getSb(String str) {
        sb = new SpannableString(str);
        pattern = Pattern.compile("\\d+");
        matcher = pattern.matcher(sb);
        while (matcher.find()) {
            sb.setSpan(new AbsoluteSizeSpan(20, true), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sb;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_reserve_report;
    }

    class CourseReverseVH extends FlexibleViewHolder {

        @BindView(R.id.item_personal_header)
        ImageView itemPersonalHeader;
        @BindView(R.id.item_personal_name)
        TextView itemPersonalName;
        @BindView(R.id.item_personal_gender)
        ImageView itemPersonalGender;
        @BindView(R.id.tv_referrer_count)
        TextView tvReferrerCount;
        @BindView(R.id.tv_student_status)
        TextView tvStudentStatus;
        @BindView(R.id.item_person_phone)
        TextView itemPersonPhone;
        @BindView(R.id.text_reverse_time)
        TextView textReverseTime;
        @BindView(R.id.text_pay_way)
        TextView textPayWay;
        @BindView(R.id.text_report_reverse_number)
        TextView textReportReverseNumber;
        @BindView(R.id.text_report_reverse_income)
        TextView textReportReverseIncome;
        @BindView(R.id.text_report_reverse_fact)
        TextView textReportReverseFact;
        @BindView(R.id.img_pay_way)
        ImageView imgPayWay;

        public CourseReverseVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
