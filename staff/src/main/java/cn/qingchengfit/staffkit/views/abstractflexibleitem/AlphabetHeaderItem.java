package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AlphabetHeaderItem extends AbstractHeaderItem<AlphabetHeaderItem.AlphabetHeaderVH> {

    private String s;

    public AlphabetHeaderItem(String s) {
        this.s = s;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_fun_header;
    }

    @Override public AlphabetHeaderVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AlphabetHeaderVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AlphabetHeaderVH holder, int position, List payloads) {
        holder.title.setText(s);
        holder.title.setTextColor(ContextCompat.getColor(holder.title.getContext(), R.color.red));
    }

    @Override public boolean equals(Object o) {
        if (o instanceof AlphabetHeaderItem) {
            return ((AlphabetHeaderItem) o).s.equals(this.s);
        } else {
            return false;
        }
    }

    public class AlphabetHeaderVH extends FlexibleViewHolder {
        @BindView(R.id.title) TextView title;

        public AlphabetHeaderVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}