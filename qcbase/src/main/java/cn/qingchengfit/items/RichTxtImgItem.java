package cn.qingchengfit.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RichTxtImgItem extends AbstractFlexibleItem<RichTxtImgItem.RichTxtImgVH> {

  String url;

  public RichTxtImgItem(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_rich_img;
  }

  @Override public RichTxtImgVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new RichTxtImgVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RichTxtImgVH holder, int position,
      List payloads) {
    Context context = holder.img.getContext();
    PhotoUtils.loadWidth(context, url, holder.img,
        MeasureUtils.getScreenWidth(context.getResources()) - MeasureUtils.dpToPx(30f,
            context.getResources()));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RichTxtImgVH extends FlexibleViewHolder {
	ImageView img;

    public RichTxtImgVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = (ImageView) view.findViewById(R.id.img);
    }
  }
}