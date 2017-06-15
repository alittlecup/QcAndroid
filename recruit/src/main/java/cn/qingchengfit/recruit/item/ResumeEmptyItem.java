package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeEmptyItem extends AbstractFlexibleItem<ResumeEmptyItem.ResumeEmptyVH> {

    int pos;
    String[] txts;

    public ResumeEmptyItem(int pos, Context context) {
        this.pos = pos;
        txts = context.getResources().getStringArray(R.array.resume_empty_items);
    }

    public int getPos() {
        return pos;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_resume_empty;
    }

    @Override public ResumeEmptyVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeEmptyVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeEmptyVH holder, int position, List payloads) {
        holder.tv.setText(txts[pos]);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeEmptyVH extends FlexibleViewHolder {
        @BindView(R2.id.tv) TextView tv;

        public ResumeEmptyVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}