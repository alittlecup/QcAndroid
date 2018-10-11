package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RecruitDemandItem extends AbstractFlexibleItem<RecruitDemandItem.RecruitDemandVH> {

  int resImg;
  String content;

  public RecruitDemandItem(int resImg, String content) {
    this.resImg = resImg;
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_recruit_demand;
  }

  @Override public RecruitDemandVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new RecruitDemandVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitDemandVH holder, int position, List payloads) {
    holder.imgRecruitDemand.setImageResource(resImg);
    holder.tvContent.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RecruitDemandVH extends FlexibleViewHolder {
	ImageView imgRecruitDemand;
	TextView tvContent;

    public RecruitDemandVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgRecruitDemand = (ImageView) view.findViewById(R.id.img_recruit_demand);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
    }
  }
}