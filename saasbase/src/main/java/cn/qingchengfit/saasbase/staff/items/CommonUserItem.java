package cn.qingchengfit.saasbase.staff.items;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;

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

    holder.tvSubTitle.setVisibility(TextUtils.isEmpty(user.getSubTitle()) ? View.GONE : View.VISIBLE);
    holder.tvSubContent.setVisibility(TextUtils.isEmpty(user.getContent()) ? View.GONE : View.VISIBLE);

    holder.tvSubContent.setVisibility(View.GONE);
    if (user.getId().equalsIgnoreCase("0")){
      holder.tvTitle.setText(user.getTitle());
      return;
    }
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
    if (o instanceof CommonUserItem && ((CommonUserItem) o).getUser() == null){
      return true;
    }
    return o instanceof CommonUserItem && TextUtils.equals(((CommonUserItem) o).getUser().getId(),
      getUser().getId());
  }

  @Override public boolean filter(String constraint) {
    return user.filter(constraint);
  }

  public class CommonUserVH extends FlexibleViewHolder {
	CheckBox cb;
	ImageView itemStudentHeader;
	RelativeLayout itemStudentHeaderLoop;
	TextView tvTitle;
	TextView tvSubTitle;
	TextView tvSubContent;
	TextView tvEnd;
	ImageView iconRight;

    public CommonUserVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      cb = (CheckBox) view.findViewById(R.id.cb);
      itemStudentHeader = (ImageView) view.findViewById(R.id.item_student_header);
      itemStudentHeaderLoop = (RelativeLayout) view.findViewById(R.id.item_student_header_loop);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvSubTitle = (TextView) view.findViewById(R.id.tv_sub_title);
      tvSubContent = (TextView) view.findViewById(R.id.tv_sub_content);
      tvEnd = (TextView) view.findViewById(R.id.tv_end);
      iconRight = (ImageView) view.findViewById(R.id.icon_right);

      cb.setClickable(false);
    }
  }
}