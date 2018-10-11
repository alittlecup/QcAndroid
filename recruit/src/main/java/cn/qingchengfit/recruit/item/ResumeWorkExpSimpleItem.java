package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeWorkExpSimpleItem extends AbstractFlexibleItem<ResumeWorkExpSimpleItem.ResumeWorkExpVH> {

  WorkExp workExp;
  Context context;
  boolean showAll;

  public ResumeWorkExpSimpleItem(WorkExp workExp, Context context) {
    this.workExp = workExp;
    this.context = context;
  }

  public WorkExp getWorkExp() {
    return workExp;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_work_exp_simple;
  }

  @Override public ResumeWorkExpVH createViewHolder(View view, FlexibleAdapter adapter) {
    ResumeWorkExpVH vh = new ResumeWorkExpVH(view, adapter);
    return vh;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeWorkExpVH holder, int position, List payloads) {
    if (workExp.gym != null) {
      holder.tvGymName.setText(workExp.gym.name);
      //健身房信息
      if (workExp.gym.brand != null) {
        PhotoUtils.smallCircle(holder.img, workExp.gym.getPhoto());
      }
    }
    holder.tvDuring.setText(DateUtils.getYYMMfromServer(workExp.start) + "至" + DateUtils.getYYMMfromServer(workExp.end));
    holder.tvGymName.setCompoundDrawablesWithIntrinsicBounds(null, null,
        workExp.is_authenticated ? ContextCompat.getDrawable(context, R.drawable.vd_qc_logo) : null, null);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeWorkExpVH extends FlexibleViewHolder {
	ImageView img;
	CompatTextView tvGymName;
	TextView tvDuring;

    public ResumeWorkExpVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      img = (ImageView) view.findViewById(R.id.img);
      tvGymName = (CompatTextView) view.findViewById(R.id.tv_gym_name);
      tvDuring = (TextView) view.findViewById(R.id.tv_during);
    }
  }
}