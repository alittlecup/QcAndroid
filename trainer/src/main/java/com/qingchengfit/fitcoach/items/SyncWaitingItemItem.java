package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.event.EventSyncDone;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SyncWaitingItemItem extends AbstractFlexibleItem<SyncWaitingItemItem.SyncWaitingItemVH> {

    public boolean isDone = false;

    @Override public int getLayoutRes() {
        return R.layout.item_sync_waiting;
    }

    @Override public SyncWaitingItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SyncWaitingItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SyncWaitingItemVH holder, int position, List payloads) {
        holder.waitHint.setVisibility(isDone ? View.GONE : View.VISIBLE);
        holder.btn.setVisibility((!isDone) ? View.GONE : View.VISIBLE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SyncWaitingItemVH extends FlexibleViewHolder {
	Button btn;
	TextView waitHint;

        public SyncWaitingItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          btn = (Button) view.findViewById(R.id.btn);
          waitHint = (TextView) view.findViewById(R.id.wait_hint);

          btn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    RxBus.getBus().post(new EventSyncDone());
                }
            });
        }
    }
}