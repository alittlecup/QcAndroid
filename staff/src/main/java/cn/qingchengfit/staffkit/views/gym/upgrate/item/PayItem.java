package cn.qingchengfit.staffkit.views.gym.upgrate.item;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PayItem extends AbstractFlexibleItem<PayItem.PayVH> {
    String mStrTime;//充值时长
    String mOriPrice;//原价
    String mPrice;//现价
    int monthCount;

    public PayItem(String strTime, String oriPrice, String price, int monthCount) {
        mStrTime = strTime;
        mOriPrice = oriPrice;
        mPrice = price;
        this.monthCount = monthCount;
    }

    public String getStrTime() {
        return mStrTime;
    }

    public void setStrTime(String strTime) {
        mStrTime = strTime;
    }

    public String getOriPrice() {
        return mOriPrice;
    }

    public void setOriPrice(String oriPrice) {
        mOriPrice = oriPrice;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    public boolean hasDiscount() {
        return !TextUtils.isEmpty(mOriPrice);
    }

    @Override public int getLayoutRes() {
        return R.layout.item_pay;
    }

    @Override public PayVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new PayVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, PayVH holder, int position, List payloads) {
        holder.tvTime.setText(mStrTime);
        if (StringUtils.isEmpty(mOriPrice)) {
            holder.tvOriginPrice.setVisibility(View.GONE);
        } else {
            holder.tvOriginPrice.setVisibility(View.VISIBLE);
            holder.tvOriginPrice.setText(mOriPrice + "元");
            holder.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvOriginPrice.getPaint().setColor(ContextCompat.getColor(holder.tvOriginPrice.getContext(), R.color.bg_grey));
        }
        holder.tvPrice.setText(mPrice + "元");
        holder.rootView.setBackgroundResource(adapter.isSelected(position) ? R.color.colorPrimary : R.color.white);
        holder.tvTime.setTextColor(
            CompatUtils.getColor(holder.tvTime.getContext(), adapter.isSelected(position) ? R.color.white : R.color.text_black));
        holder.tvOriginPrice.setTextColor(
            CompatUtils.getColor(holder.tvTime.getContext(), adapter.isSelected(position) ? R.color.white : R.color.text_black));
        holder.tvPrice.setTextColor(
            CompatUtils.getColor(holder.tvTime.getContext(), adapter.isSelected(position) ? R.color.white : R.color.text_grey));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class PayVH extends FlexibleViewHolder {
        @BindView(R.id.tv_time) TextView tvTime;
        @BindView(R.id.tv_origin_price) TextView tvOriginPrice;
        @BindView(R.id.tv_price) TextView tvPrice;
        @BindView(R.id.root_view) RelativeLayout rootView;

        public PayVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}