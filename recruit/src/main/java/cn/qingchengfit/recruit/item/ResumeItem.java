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

public class ResumeItem extends AbstractFlexibleItem<ResumeItem.ResumeVH> {

  @Override public int getLayoutRes() {
    return R.layout.item_resume;
  }

  @Override public ResumeVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new ResumeVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeVH holder, int position, List payloads) {
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeVH extends FlexibleViewHolder {
    @BindView(R2.id.img_avatar) ImageView imgAvatar;
    @BindView(R2.id.tv_name) TextView tvName;
    @BindView(R2.id.img_gender) ImageView imgGender;
    @BindView(R2.id.tv_requirement) TextView tvRequirement;
    @BindView(R2.id.tv_except) TextView tvExcept;

    public ResumeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}