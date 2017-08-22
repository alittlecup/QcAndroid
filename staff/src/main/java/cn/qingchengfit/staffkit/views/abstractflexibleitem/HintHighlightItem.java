package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.utils.Utils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class HintHighlightItem extends AbstractFlexibleItem<HintHighlightItem.HintHighlightVH>
    implements IHeader<HintHighlightItem.HintHighlightVH> {

    @DrawableRes int drawabele = R.drawable.ic_circle_info;
    String hintStr;
    String hightLightStr;

    public HintHighlightItem(String hintStr) {
        this.hintStr = hintStr;
    }

    public HintHighlightItem(String hintStr, String hightLightStr) {
        this.hintStr = hintStr;
        this.hightLightStr = hightLightStr;
    }

    public HintHighlightItem(int drawabele, String hintStr) {
        this.drawabele = drawabele;
        this.hintStr = hintStr;
    }

    public HintHighlightItem(int drawabele, String hintStr, String hightLightStr) {
        this.drawabele = drawabele;
        this.hintStr = hintStr;
        this.hightLightStr = hightLightStr;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_hint_hightlight;
    }

    @Override public HintHighlightVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new HintHighlightVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, HintHighlightVH holder, int position, List payloads) {
        holder.imgInfo.setImageResource(drawabele);
        Utils.highlightText(holder.tvHint, hintStr, hightLightStr);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class HintHighlightVH extends FlexibleViewHolder {
        @BindView(R.id.img_info) ImageView imgInfo;
        @BindView(R.id.tv_hint) TextView tvHint;

        public HintHighlightVH(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);
            ButterKnife.bind(this, view);
        }
    }
}