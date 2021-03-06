package cn.qingchengfit.staffkit.views.gym.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.model.responese.RenewalHistory;
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

    @Override public RenewalsHistoryVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new RenewalsHistoryVH(view, adapter);
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
        holder.payStatus.setText(mContext.getResources().getStringArray(R.array.renewal_status)[1]);
        holder.payStatus.setTextColor(CompatUtils.getColor(mContext, R.color.green));
        holder.payOnline.setText(mHistory.remark);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class RenewalsHistoryVH extends FlexibleViewHolder {
	TextView payTime;
	TextView payStatus;
	TextView gymValidTime;
	TextView gymRealMoney;
	TextView payOnline;
	TextView operaUser;

        public RenewalsHistoryVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          payTime = (TextView) view.findViewById(R.id.pay_time);
          payStatus = (TextView) view.findViewById(R.id.pay_status);
          gymValidTime = (TextView) view.findViewById(R.id.gym_valid_time);
          gymRealMoney = (TextView) view.findViewById(R.id.gym_real_money);
          payOnline = (TextView) view.findViewById(R.id.pay_online);
          operaUser = (TextView) view.findViewById(R.id.opera_user);
        }
    }
}