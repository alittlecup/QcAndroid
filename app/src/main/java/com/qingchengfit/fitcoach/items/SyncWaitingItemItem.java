package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.event.EventSyncDone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SyncWaitingItemItem extends AbstractFlexibleItem<SyncWaitingItemItem.SyncWaitingItemVH> {

    public boolean isDone = false;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.wait_hint)
    TextView waitHint;

    @Override
    public int getLayoutRes() {
        return R.layout.item_sync_waiting;
    }

    @Override
    public SyncWaitingItemVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SyncWaitingItemVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SyncWaitingItemVH holder, int position, List payloads) {
        waitHint.setVisibility(isDone ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class SyncWaitingItemVH extends FlexibleViewHolder {

        public SyncWaitingItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getBus().post(new EventSyncDone());
                }
            });
        }
    }
}