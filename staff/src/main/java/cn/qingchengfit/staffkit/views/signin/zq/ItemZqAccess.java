package cn.qingchengfit.staffkit.views.signin.zq;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/9/15.
 */

public class ItemZqAccess extends AbstractFlexibleItem<ItemZqAccess.ZqAccessVH> {

  private Guard guard;

  public ItemZqAccess(Guard guard) {
    this.guard = guard;
  }

  public void setGuard(Guard guard) {
    this.guard = guard;
  }

  public Guard getData(){
    return guard;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ZqAccessVH holder, int position,
      List payloads) {
    holder.textAccessNo.setText(guard.name);
    holder.textAccessEquipNo.setText(
        holder.itemView.getResources().getString(R.string.item_zq_equip_no, guard.device_id));

    if (guard.behavior == 1) {
      holder.textAccessEquipNo.setText(
          holder.itemView.getResources().getString(R.string.item_zq_fun, "签到"));
    }else{
      holder.textAccessEquipNo.setText(
          holder.itemView.getResources().getString(R.string.item_zq_fun, "签出"));
    }

    holder.textSignoutTime.setText(holder.itemView.getResources()
        .getString(R.string.item_zq_sign_time, guard.start, guard.end));

    holder.textZqStatus.setText(judgeStatus(guard.status));
    holder.textZqStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), getStatusColor(guard.status)));
  }

  private int getStatusColor(int status){
    int color = 0;
    switch (status){
      case 1:
        color = R.color.shamrock_green;
        break;
      case 2:
        color =  R.color.orange;
        break;
      case 3:
        color = R.color.red;
        break;
    }
    return color;
  }

  private String judgeStatus(int status){
    String str = "";
    switch (status){
      case 1:
        return "正常";
      case 2:
        return "长关";
      case 3:
        return "常开";
    }
    return str;
  }

  @Override public ZqAccessVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ZqAccessVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_zq_access;
  }

  class ZqAccessVH extends FlexibleViewHolder {
    @BindView(R.id.text_access_no) TextView textAccessNo;
    @BindView(R.id.text_access_equip_no) TextView textAccessEquipNo;
    @BindView(R.id.text_equip_features) TextView textEquipFeatures;
    @BindView(R.id.text_signout_time) TextView textSignoutTime;
    @BindView(R.id.text_zq_status) TextView textZqStatus;

    public ZqAccessVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
