package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
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

  @Override public RecruitDemandVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new RecruitDemandVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitDemandVH holder, int position, List payloads) {
    holder.imgRecruitDemand.setImageResource(resImg);
    holder.tvContent.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RecruitDemandVH extends FlexibleViewHolder {
    @BindView(R2.id.img_recruit_demand) ImageView imgRecruitDemand;
    @BindView(R2.id.tv_content) TextView tvContent;

    public RecruitDemandVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}