package cn.qingchengfit.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.ShareExtends;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by fb on 2017/9/11.
 */

public class ShareDialogExtendsItem
    extends AbstractFlexibleItem<ShareDialogExtendsItem.ShareDialogExtendsVH> {

  private ShareExtends shareExtends;
  private OnShareExtendClickListener listener;

  public ShareDialogExtendsItem(ShareExtends shareExtends, OnShareExtendClickListener listener) {
    this.shareExtends = shareExtends;
    this.listener = listener;
  }

  public String getKey() {
    return shareExtends.key;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_share_dialog_extends;
  }

  @Override
  public ShareDialogExtendsVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    ShareDialogExtendsVH holder =
        new ShareDialogExtendsVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    if (shareExtends.type.equals(ShareDialogFragment.SHARE_TYPE_ACTION)) {
      holder.textFunNameShare.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (listener != null) {
            listener.onClickItem((int) v.getTag());
          }
        }
      });
    }
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ShareDialogExtendsVH holder, int position,
      List payloads) {
    Glide.with(holder.itemView.getContext()).load(shareExtends.icon).into(holder.imgFunShare);
    holder.textFunNameShare.setText(shareExtends.title);
    if (shareExtends.type.equals(ShareDialogFragment.SHARE_TYPE_INFO)) {
      holder.textContentShareExtends.setVisibility(View.VISIBLE);
      holder.textContentShareExtends.setText(shareExtends.desc);
      holder.imgRightArrowShare.setVisibility(GONE);
    } else {
      holder.textContentShareExtends.setVisibility(View.GONE);
      holder.imgRightArrowShare.setVisibility(View.VISIBLE);
    }
    holder.textFunNameShare.setTag(position);
  }

  class ShareDialogExtendsVH extends FlexibleViewHolder {

    @BindView(R2.id.img_fun_share) ImageView imgFunShare;
    @BindView(R2.id.text_fun_name_share) TextView textFunNameShare;
    @BindView(R2.id.img_right_arrow_share) ImageView imgRightArrowShare;
    @BindView(R2.id.text_content_share_extends) TextView textContentShareExtends;

    public ShareDialogExtendsVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnShareExtendClickListener {
    void onClickItem(int position);
  }
}
