package cn.qingchengfit.items;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.widgets.CommonInputView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInConfigItem extends AbstractFlexibleItem<SignInConfigItem.ItemViewHolder> {

    public SignInCardCostBean.CardCost bean;
    public boolean isSelect;
    public boolean isClickable = true;

    public SignInConfigItem(SignInCardCostBean.CardCost bean, boolean isClickable) {
        this.bean = bean;
        this.isClickable = isClickable;
    }

    public SignInConfigItem(SignInCardCostBean.CardCost bean) {
        this.bean = bean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_signin_config;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {

        isSelect = bean.isSelected();
        holder.swSigninConfig.setLabel(bean.getName());
        holder.swSigninConfig.setOpen(isSelect);
        holder.swSigninConfig.setClickable(isClickable);
        holder.swSigninConfig.setShowDivier(isSelect);
        holder.swSigninConfigFee.getEditText().setEnabled(isClickable);
        holder.swSigninConfigFee.setVisibility(bean.getType() != Configs.CATEGORY_DATE && bean.isSelected() ? View.VISIBLE : View.GONE);

        switch (bean.getType()) {
            case Configs.CATEGORY_VALUE:
                holder.swSigninConfigFee.setLabel(adapter.getRecyclerView().getContext().getString(R.string.sign_in_config_cost, "元"));
                holder.swSigninConfigFee.getEditText().setText(bean.isSelected() ? String.valueOf(bean.getCost()) : "");
                break;
            case Configs.CATEGORY_TIMES:
                holder.swSigninConfigFee.setLabel(adapter.getRecyclerView().getContext().getString(R.string.sign_in_config_cost, "次"));
                holder.swSigninConfigFee.getEditText().setText(bean.isSelected() ? String.valueOf(bean.getCost()) : "");
                break;
            case Configs.CATEGORY_DATE:
                holder.swSigninConfig.setShowDivier(false);
                break;
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        @BindView(R.id.sw_signin_config) SwitcherLayout swSigninConfig;
        @BindView(R.id.sw_signin_config_fee) CommonInputView swSigninConfigFee;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            swSigninConfig.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ((SignInConfigItem) mAdapter.getItem(getFlexibleAdapterPosition())).bean.setSelected(b);
                    if (((SignInConfigItem) mAdapter.getItem(getFlexibleAdapterPosition())).bean.getType() != Configs.CATEGORY_DATE) {
                        swSigninConfigFee.setVisibility(b ? View.VISIBLE : View.GONE);
                        swSigninConfig.setShowDivier(b);
                    }
                }
            });
            swSigninConfigFee.addTextWatcher(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override public void afterTextChanged(Editable editable) {
                    if ("".equals(editable.toString())) {
                        ((SignInConfigItem) mAdapter.getItem(getFlexibleAdapterPosition())).bean.setCost(0);
                    } else {
                        ((SignInConfigItem) mAdapter.getItem(getFlexibleAdapterPosition())).bean.setCost(
                            Float.valueOf(editable.toString().trim()));
                    }
                }
            });
        }
    }
}
