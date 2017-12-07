package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.items.types.ICmLRTxt;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CmLRTxtItem extends AbstractFlexibleItem<CmLRTxtItem.CmLRTxtVH> {
  ICmLRTxt cmLRTxt;

  public CmLRTxtItem(ICmLRTxt cmLRTxt) {
    this.cmLRTxt = cmLRTxt;
  }

  public Object getData(){
    return cmLRTxt;
  }

  public void setCmLRTxt(ICmLRTxt cmLRTxt) {
    this.cmLRTxt = cmLRTxt;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cm_lr_txt;
  }

  @Override public CmLRTxtVH createViewHolder(View view,FlexibleAdapter adapter) {
    return new CmLRTxtVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CmLRTxtVH holder, int position,
    List payloads) {
    holder.tvL.setText(cmLRTxt.getLeftTxt());
    holder.tvR.setText(cmLRTxt.getRightTxt());
    if (cmLRTxt.getLeftIcon() > 0)
      holder.imgL.setImageResource(cmLRTxt.getLeftIcon());
    if (cmLRTxt.getRightIcon() > 0)
      holder.imgR.setImageResource(cmLRTxt.getRightIcon());
  }

  @Override public boolean equals(Object o) {
    if (o instanceof ICmLRTxt){
      return ((ICmLRTxt) o).getId().equalsIgnoreCase(cmLRTxt.getId());
    }
    return false;
  }

  public class CmLRTxtVH extends FlexibleViewHolder {
    public TextView tvL;
    public TextView tvR;
    public ImageView imgL;
    public ImageView imgR;


    public CmLRTxtVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvL = view.findViewById(R.id.tv_left);
      tvR = view.findViewById(R.id.tv_right);
      imgL = view.findViewById(R.id.img_left);
      imgR = view.findViewById(R.id.img_right);
    }
  }
}