package cn.qingchengfit.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/27.
 */
public class ChooseGymItem extends AbstractFlexibleItem<ChooseGymItem.GymVH> {

  private Gym gym;

  public ChooseGymItem(Gym gym) {
    this.gym = gym;
  }

  public Gym getGym() {
    return gym;
  }

  public void setGym(Gym gym) {
    this.gym = gym;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_base_choose_gym;
  }

  @Override public GymVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new GymVH(view,adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, GymVH holder, int position, List payloads) {

    Glide.with(holder.itemView.getContext())
        .load(gym.getPhoto())
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(holder.imgAvatar, holder.imgAvatar.getContext()));
    holder.tvGymName.setText(gym.getName());
    holder.tvGymBrand.setText(gym.getBrand_name());
    if (isCompeleted()) {
      holder.tvCompleteInfo.setVisibility(View.GONE);
      holder.icChooseGymArrow.setVisibility(View.GONE);
    } else {
      holder.tvCompleteInfo.setVisibility(View.VISIBLE);
      holder.icChooseGymArrow.setVisibility(View.VISIBLE);
    }
  }

  public boolean isCompeleted() {
    return gym.getcityCode() != 0;
  }

  public class GymVH extends FlexibleViewHolder {
    @BindView(R2.id.img_avatar) ImageView imgAvatar;
    @BindView(R2.id.tv_gym_name) TextView tvGymName;
    @BindView(R2.id.tv_complete_info) TextView tvCompleteInfo;
    @BindView(R2.id.tv_gym_brand) TextView tvGymBrand;
    @BindView(R2.id.ic_choose_gym_arrow) ImageView icChooseGymArrow;

    public GymVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
