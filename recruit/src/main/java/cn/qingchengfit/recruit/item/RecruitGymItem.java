package cn.qingchengfit.recruit.item;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.GymHasResume;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RecruitGymItem extends AbstractFlexibleItem<RecruitGymItem.RecruitGymVH> {

  GymHasResume gym;
  private boolean hasUndo = false;

  public RecruitGymItem(GymHasResume gym) {
    this.gym = gym;
  }

  public Gym getGym() {
    return (Gym) gym;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_recruit_gym;
  }

  @Override public RecruitGymVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new RecruitGymVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitGymVH holder, int position, List payloads) {
    PhotoUtils.small(holder.imgGym, gym.photo);
    holder.tvGymName.setText(gym.name);
    holder.tvAddress.setText(gym.brand.getName());
    if (holder.tv_has_todo != null) {
      if (gym.has_new_resume) {
        holder.tv_has_todo.setVisibility(View.VISIBLE);
        holder.tv_has_todo.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dot_red), null, null, null);
      } else {
        holder.tv_has_todo.setVisibility(View.GONE);
      }
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RecruitGymVH extends FlexibleViewHolder {

    @BindView(R2.id.img_gym) ImageView imgGym;
    @BindView(R2.id.tv_gym_name) TextView tvGymName;
    @BindView(R2.id.tv_address) TextView tvAddress;
    @BindView(R2.id.img_right) ImageView imgRight;
    @BindView(R2.id.layout_gym_info) LinearLayout layoutGymInfo;
    @BindView(R2.id.tv_has_undo) TextView tv_has_todo;

    public RecruitGymVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}