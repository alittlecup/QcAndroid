package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.support.widgets.CompatTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeTitleItem extends AbstractHeaderItem<ResumeTitleItem.ResumeTitleVH> {

    int pos;
    String[] titles;
    boolean showEdit;

    public ResumeTitleItem(int pos, Context context, boolean showEdit) {
        this.pos = pos;
        this.titles = titles;
        this.showEdit = showEdit;
        titles = context.getResources().getStringArray(R.array.resume_items);

    }


    public int getPos() {
        return pos;
    }

    @Override public int getLayoutRes() {
        return R.layout.layout_resume_title;
    }

    @Override public ResumeTitleVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeTitleVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeTitleVH holder, int position, List payloads) {
        holder.tvTitle.setText(titles[pos]);
        holder.btnEdit.setVisibility(showEdit ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeTitleVH extends FlexibleViewHolder {

        @BindView(R2.id.tv_title) TextView tvTitle;
        @BindView(R2.id.btn_edit) CompatTextView btnEdit;

        public ResumeTitleVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (adapter.getItem(getAdapterPosition()) instanceof ResumeTitleItem) {
                        RxBus.getBus().post((ResumeTitleItem) adapter.getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }
}