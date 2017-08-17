package cn.qingchengfit.staffkit.views.gym.config;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class HintExpandItem extends AbstractFlexibleItem<HintExpandItem.HintExpandVH> {

    public SignInConfig.Config mConfig;
    public String hint;
    public boolean showDivier;
    public boolean spanabale;
    public String title;
    public String contentLable;

    public HintExpandItem(SignInConfig.Config config, String hint, boolean showDivier, boolean spanabale, String title,
        String contentLable) {
        mConfig = config;
        this.hint = hint;
        this.showDivier = showDivier;
        this.spanabale = spanabale;
        this.title = title;
        this.contentLable = contentLable;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_hint_expand;
    }

    @Override public HintExpandVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new HintExpandVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, HintExpandVH holder, int position, List payloads) {
        if (TextUtils.isEmpty(hint)) {
            holder.hint.setVisibility(View.GONE);
        } else {
            holder.hint.setVisibility(View.VISIBLE);
            holder.hint.setText(hint);
        }
        holder.swContent.setVisibility(spanabale ? View.VISIBLE : View.GONE);
        holder.divider.setVisibility(showDivier ? View.VISIBLE : View.GONE);
        holder.swKey.setLabel(title);
        holder.swContent.setLabel(contentLable);
        holder.swContent.setContent(mConfig.getValue() + "");
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class HintExpandVH extends FlexibleViewHolder {
        @BindView(R.id.hint) TextView hint;
        @BindView(R.id.sw_content) CommonInputView swContent;
        @BindView(R.id.sw_key) ExpandedLayout swKey;
        @BindView(R.id.divider) View divider;

        public HintExpandVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}