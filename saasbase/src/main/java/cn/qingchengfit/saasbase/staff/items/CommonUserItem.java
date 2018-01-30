package cn.qingchengfit.saasbase.staff.items;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CommonUserItem extends AbstractFlexibleItem<CommonUserItem.CommonUserVH>
  implements IFilterable {

  ICommonUser user;

  public CommonUserItem(ICommonUser user) {
    this.user = user;
  }

  public ICommonUser getUser() {
    return user;
  }

  public void setUser(ICommonUser user) {
    this.user = user;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_common_user;
  }

  @Override public CommonUserVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CommonUserVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CommonUserVH holder, int position,
    List payloads) {
    if (adapter.getMode() != FlexibleAdapter.Mode.IDLE) {
      holder.cb.setVisibility(View.VISIBLE);
    } else {
      holder.cb.setVisibility(View.GONE);
    }
    PhotoUtils.smallCircle(holder.itemStudentHeader, user.getAvatar());
    holder.tvEnd.setText(user.getRight());
    holder.tvEnd.setTextColor(ContextCompat.getColor(holder.tvEnd.getContext(),
      user.getRightColor() > 0 ? user.getRightColor() : R.color.text_dark));
    holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
      ContextCompat.getDrawable(holder.tvTitle.getContext(),
        user.getGender() == 0 ? R.drawable.ic_gender_signal_male
          : R.drawable.ic_gender_signal_female), null);
    if (CmStringUtils.isEmpty(adapter.getSearchText())) {
      holder.tvTitle.setText(user.getTitle());
      holder.tvSubTitle.setText(user.getSubTitle());
      holder.tvSubContent.setText(user.getContent());
    } else {
      FlexibleUtils.highlightWords(holder.tvTitle, user.getTitle(), adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.tvSubTitle, user.getSubTitle(), adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.tvSubContent, user.getContent(), adapter.getSearchText());
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof CommonUserItem && TextUtils.equals(((CommonUserItem) o).getUser().getId(),
      getUser().getId());
  }

  @Override public boolean filter(String constraint) {
    return user.filter(constraint);
  }

  public class CommonUserVH extends FlexibleViewHolder {
    @BindView(R2.id.cb) CheckBox cb;
    @BindView(R2.id.item_student_header) ImageView itemStudentHeader;
    @BindView(R2.id.item_student_header_loop) RelativeLayout itemStudentHeaderLoop;
    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.tv_sub_title) TextView tvSubTitle;
    @BindView(R2.id.tv_sub_content) TextView tvSubContent;
    @BindView(R2.id.tv_end) TextView tvEnd;
    @BindView(R2.id.icon_right) ImageView iconRight;

    public CommonUserVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      cb.setClickable(false);
    }
  }
}