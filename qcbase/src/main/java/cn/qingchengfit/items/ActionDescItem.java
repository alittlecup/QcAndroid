package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ActionDescItem extends AbstractFlexibleItem<ActionDescItem.ActionDescVH> {

  int action;
  String title,desc;
  @DrawableRes int icon;
  boolean clickable = true;

  private ActionDescItem(Builder builder) {
    setAction(builder.action);
    title = builder.title;
    desc = builder.desc;
    icon = builder.icon;
    clickable = builder.clickable;
  }

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_action_desc;
  }

  @Override public ActionDescVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ActionDescVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ActionDescVH holder, int position,
      List payloads) {
    holder.imgAction.setImageResource(icon);
    holder.title.setText(title);
    holder.desc.setText(desc);
    holder.right.setVisibility(clickable?View.VISIBLE:View.GONE);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof ActionDescItem){
      return ((ActionDescItem) o).getAction() == action;
    }else return false;
  }

  public class ActionDescVH extends FlexibleViewHolder {
    //@BindView(R2.id.img_action)   ImageView imgAction;
    //@BindView(R2.id.title)        TextView title;
    //@BindView(R2.id.tv_desc)      TextView desc;
    //@BindView(R2.id.img_right)      ImageView right;
    ImageView imgAction;
    TextView title;
    TextView desc;
    ImageView right;
    public ActionDescVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public static final class Builder {
    private int action;
    private String title;
    private String desc;
    private int icon;
    private boolean clickable= true;

    public Builder() {
    }

    public Builder action(int val) {
      action = val;
      return this;
    }

    public Builder title(String val) {
      title = val;
      return this;
    }

    public Builder desc(String val) {
      desc = val;
      return this;
    }

    public Builder icon(int val) {
      icon = val;
      return this;
    }

    public Builder clickable(boolean val) {
      clickable = val;
      return this;
    }

    public ActionDescItem build() {
      return new ActionDescItem(this);
    }
  }
}