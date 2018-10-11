package cn.qingchengfit.saasbase.cards.item;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.MeasureUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchPayCardItem extends AbstractFlexibleItem<BatchPayCardItem.BatchPayCardVH>
     {

    int pos;
    boolean isPrivate;
    CardTpl cardTpl;
    private String cost;

    public BatchPayCardItem(String rule, int pos, CardTpl cardTpl, boolean isPrivate) {
        this.cost = rule;
        this.pos = pos;
        this.cardTpl = cardTpl;
        this.isPrivate = isPrivate;
    }

    public int getPos() {
        return pos;
    }

    public String getUnico() {
        return TextUtils.concat(cardTpl.getId(), Integer.toString(pos)).toString();
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_batch_pay_card;
    }

    @Override public BatchPayCardVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new BatchPayCardVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BatchPayCardVH holder, int position, List payloads) {
        if (cardTpl.getType() == Configs.CATEGORY_DATE) {
            holder.itemView.getLayoutParams().height = 0;
        } else {
            holder.itemView.getLayoutParams().height = MeasureUtils.dpToPx(44f, holder.itemView.getResources());
        }
        if (isPrivate) {
            holder.tvLable.setText(holder.tvLable.getContext().getString(R.string.card_content_lable, pos + 1) + holder.tvLable.getContext()
                .getString(cardTpl.getType() == 1 ? R.string.card_unit_value : R.string.card_unit_times));
        } else {
            holder.tvLable.setText(
                "单价" + holder.tvLable.getContext().getString(cardTpl.getType() == 1 ? R.string.card_unit_value : R.string.card_unit_times));
        }
        holder.etContent.setText(StringUtils.isEmpty(cost) ? "" : cost);
    }

    @Override public boolean equals(Object o) {
        if (o instanceof BatchPayCardItem) {
            return ((BatchPayCardItem) o).getUnico().equals(getUnico());
        } else {
            return false;
        }
    }


    public class BatchPayCardVH extends FlexibleViewHolder {
	TextView tvLable;
	EditText etContent;

        public BatchPayCardVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
          tvLable = (TextView) view.findViewById(R.id.tv_lable);
          etContent = (EditText) view.findViewById(R.id.et_content);

          etContent.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override public void afterTextChanged(Editable s) {
                    if (adapter.getItem(getAdapterPosition()) instanceof BatchPayCardItem) {
                        ((BatchPayCardItem) adapter.getItem(getAdapterPosition())).setCost(s.toString());
                    }
                }
            });
        }
    }
}