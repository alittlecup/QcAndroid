package cn.qingchengfit.staffkit.views.gym.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.RenewalHistory;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RenewalsHistoryItem extends AbstractFlexibleItem<RenewalsHistoryItem.RenewalsHistoryVH> {
    public RenewalHistory mHistory;
    public Context mContext;

    public RenewalsHistoryItem(RenewalHistory history, Context context) {
        mHistory = history;
        this.mContext = context;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_renewals_history;
    }

    @Override public RenewalsHistoryVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RenewalsHistoryVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, RenewalsHistoryVH holder, int position, List payloads) {
        holder.payTime.setText(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(mHistory.created_at)));
        //        if (mHistory.extra != null){
        holder.gymValidTime.setText(DateUtils.getYYYYMMDDfromServer(mHistory.start) + "至" + DateUtils.getYYYYMMDDfromServer(mHistory.end));
        //        }
        String price = "";
        if (mHistory.order != null && mHistory.order.price != null) {
            price = mHistory.order.price + mContext.getString(R.string.unit_yuan);
        }
        holder.gymRealMoney.setText(price);
        if (mHistory.created_by != null) {
            holder.operaUser.setText(mContext.getString(R.string.control_person, mHistory.created_by.getUsername()));
        }
        //        switch (mHistory.status){
        //            case 0:
        //                holder.payStatus.setText(mContext.getResources().getStringArray(R.array.renewal_status)[0]);
        //                holder.payStatus.setTextColor(CompatUtils.getColor(mContext,R.color.red));
        //                break;
        //            case 1:
        //
        //                break;
        //            default:
        //                holder.payStatus.setText("");
        //                break;
        //        }
        holder.payStatus.setText(mContext.getResources().getStringArray(R.array.renewal_status)[1]);
        holder.payStatus.setTextColor(CompatUtils.getColor(mContext, R.color.green));
        holder.payOnline.setText(mHistory.remark);
        //        switch (mHistory.channel){
        //            case Configs.DEAL_ADMIN:
        //                holder.payOnline.setText("线下续费");
        //                break;
        //            case Configs.DEAL_WECHAT:
        //            case Configs.DEAL_ALIPAY:
        //            case Configs.DEAL_WEIXIN_QR:
        //                holder.payOnline.setText("在线续费");
        //                break;
        //            default:
        //                break;
        //        }

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class RenewalsHistoryVH extends FlexibleViewHolder {
        @BindView(R.id.pay_time) TextView payTime;
        @BindView(R.id.pay_status) TextView payStatus;
        @BindView(R.id.gym_valid_time) TextView gymValidTime;
        @BindView(R.id.gym_real_money) TextView gymRealMoney;
        @BindView(R.id.pay_online) TextView payOnline;
        @BindView(R.id.opera_user) TextView operaUser;

        public RenewalsHistoryVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}