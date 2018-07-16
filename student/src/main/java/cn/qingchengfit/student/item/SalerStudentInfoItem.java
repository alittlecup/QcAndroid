package cn.qingchengfit.student.item;

import android.view.View;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.ItemSalerStudentInfoBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SalerStudentInfoItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSalerStudentInfoBinding>> {
  private QcStudentBeanWithFollow data;

  public SalerStudentInfoItem(QcStudentBeanWithFollow data) {
    this.data = data;
  }

  public QcStudentBeanWithFollow getData() {
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

    PhotoUtils.smallCircle(binding.imgAvatar, data.avatar);
    binding.tvNotFollow.setText(data.unfollowed_count);
    binding.tvFollowTotal.setText(data.total_count);
    binding.tvName.setText(data.username);

    if (position == 0) {
      if (adapter instanceof CommonFlexAdapter) {
        ((CommonFlexAdapter) adapter).setTag("total", data.total_count);
      }
      binding.progressBar.setMax(data.total_count);
      binding.progressBar.setSecondaryProgress(data.total_count);
      binding.progressBar.setProgress(data.unfollowed_count);
    } else {
      if (adapter instanceof CommonFlexAdapter) {
        int total = (int) ((CommonFlexAdapter) adapter).getTag("total");
        binding.progressBar.setMax(total);
        binding.progressBar.setSecondaryProgress(data.total_count);
        binding.progressBar.setProgress(data.unfollowed_count);
      }
    }
  }
}
