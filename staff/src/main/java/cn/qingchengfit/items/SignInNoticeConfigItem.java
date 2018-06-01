package cn.qingchengfit.items;

import android.view.View;
import android.widget.CompoundButton;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.SigninNoticeConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInNoticeConfigEvent;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.widgets.CommonInputView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInNoticeConfigItem extends AbstractFlexibleItem<SignInNoticeConfigItem.ItemViewHolder> {

    public SigninNoticeConfig bean;
    public boolean isSelect;

    public SignInNoticeConfigItem(SigninNoticeConfig bean) {
        this.bean = bean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_signin_config;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {
        isSelect = bean.getValue() == 1;
        holder.swSigninConfig.setLabel(bean.getShop().getName());
        holder.swSigninConfig.setOpen(isSelect);
        holder.swSigninConfig.setShowDivier(false);
        holder.swSigninConfigFee.setVisibility(View.GONE);
        holder.swSigninConfig.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // TODO
                //                    ((SignInNoticeConfigItem) mAdapter.getItem(getFlexibleAdapterPosition())).bean.setValue(b ? 1 : 0);
                SignInNoticeConfigEvent event = new SignInNoticeConfigEvent();
                event.setBrandId(bean.getBrand().getId());
                event.setShopId(bean.getShop().getId());
                event.setId(bean.getId());
                event.setValue(b ? 1 : 0);
                RxBus.getBus().post(event);
            }
        });
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

	SwitcherLayout swSigninConfig;
	CommonInputView swSigninConfigFee;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          swSigninConfig = (SwitcherLayout) view.findViewById(R.id.sw_signin_config);
          swSigninConfigFee = (CommonInputView) view.findViewById(R.id.sw_signin_config_fee);
        }
    }
}
