package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInLogEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInFooterItem extends AbstractFlexibleItem<SignInFooterItem.ItemViewHolder> {

    public SignInFooterItem() {
    }

    @Override public int getLayoutRes() {
        return R.layout.item_signin_header;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                RxBus.getBus().post(new SignInLogEvent());
            }
        });
        holder.tvSigninHeader.setText("查看3小时内签到记录");
        holder.tvSigninHeader.setBackgroundResource(R.drawable.bg_rect);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        @BindView(R.id.tv_signin_header) TextView tvSigninHeader;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}