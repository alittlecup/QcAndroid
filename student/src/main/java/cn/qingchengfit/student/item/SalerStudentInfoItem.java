package cn.qingchengfit.student.item;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.SellerStat;
import cn.qingchengfit.student.databinding.ItemSalerStudentInfoBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SalerStudentInfoItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSalerStudentInfoBinding>> {
  private SellerStat data;
  private String type="";

  public SalerStudentInfoItem(SellerStat data,String type) {
    this.data = data;
    this.type=type;
  }

  public SellerStat getData() {
    return data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saler_student_info;
  }

  @Override public DataBindingViewHolder<ItemSalerStudentInfoBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemSalerStudentInfoBinding> holder, int position, List payloads) {
    ItemSalerStudentInfoBinding binding = holder.getDataBinding();
    if (data.getSeller() == null) {
      binding.imgAvatar.setImageResource(R.drawable.ic_nosales_normal);
      binding.tvName.setText("未分配");
    } else {
      PhotoUtils.smallCircle(binding.imgAvatar, data.getSeller().getAvatar());
      binding.tvName.setText(data.getSeller().getUsername());
    }

    binding.tvNotFollow.setText(String.valueOf(data.getInactive_count()));
    binding.tvFollowTotal.setText(String.valueOf(data.getTotal_count()));

    if (position == 0) {
      if (adapter instanceof CommonFlexAdapter) {
        ((CommonFlexAdapter) adapter).setTag("total", data.getTotal_count());
      }
      binding.progressBar.setMax(data.getTotal_count());
      LinearLayout.LayoutParams layoutParams =
          (LinearLayout.LayoutParams) binding.space.getLayoutParams();
      layoutParams.weight = 0;
      binding.space.setLayoutParams(layoutParams);
      binding.progressBar.setProgress(data.getInactive_count());
    } else {
      if (adapter instanceof CommonFlexAdapter) {
        int total = (int) ((CommonFlexAdapter) adapter).getTag("total");
        LinearLayout.LayoutParams layoutParams =
            (LinearLayout.LayoutParams) binding.space.getLayoutParams();
        layoutParams.weight = (total - data.getTotal_count()) * 100 / data.getTotal_count();
        binding.space.setLayoutParams(layoutParams);
        binding.progressBar.setMax(data.getTotal_count());
        binding.progressBar.setProgress(data.getInactive_count());
      }
    }
    switch (type){
      case IncreaseType.INCREASE_FOLLOWUP:
        binding.totalContent.setText("已接洽");
        break;
      case IncreaseType.INCREASE_MEMBER:
        binding.totalContent.setText("新注册");
        break;
      case IncreaseType.INCREASE_STUDENT:
        binding.totalContent.setText("会员");
        break;
    }
  }
}
