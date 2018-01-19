package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ShortMsgItem extends AbstractFlexibleItem<ShortMsgItem.ShortMsgVH> {

    ShortMsg shortMsg;

    public ShortMsgItem(ShortMsg shortMsg) {
        this.shortMsg = shortMsg;
    }

    public ShortMsg getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(ShortMsg shortMsg) {
        this.shortMsg = shortMsg;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_short_msg;
    }

    @Override public ShortMsgVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ShortMsgVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ShortMsgVH holder, int position, List payloads) {

        if (adapter instanceof CommonFlexAdapter) {
            String f = (String) ((CommonFlexAdapter) adapter).getTag("filter");
            if (StringUtils.isEmpty(f)) {
                holder.tvTitle.setText(shortMsg.getShortTitle());
                holder.tvContent.setText(shortMsg.getContent());
            } else {
                FlexibleUtils.highlightWords(holder.tvTitle, shortMsg.getShortTitle(), f, Color.RED);
                FlexibleUtils.highlightWords(holder.tvContent, shortMsg.getContent(), f, Color.RED);
            }
        }

        holder.imgStatus.setImageResource(shortMsg.status == 1 ? R.drawable.vector_hook_green : R.drawable.ic_msg_short_draft);
        holder.tvStatus.setText(shortMsg.status == 1 ? R.string.has_send : R.string.draft);
        holder.tvStatus.setTextColor(
            ContextCompat.getColor(holder.itemView.getContext(), shortMsg.status == 1 ? R.color.colorPrimary : R.color.orange_yellow));
        holder.tvTime.setText(
            cn.qingchengfit.utils.DateUtils.Date2YYYYMMDDHHmm(cn.qingchengfit.utils.DateUtils.formatDateFromServer(shortMsg.created_at)));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ShortMsgVH extends FlexibleViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_content) TextView tvContent;
        @BindView(R.id.img_status) ImageView imgStatus;
        @BindView(R.id.tv_status) TextView tvStatus;
        @BindView(R.id.tv_time) TextView tvTime;

        public ShortMsgVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}