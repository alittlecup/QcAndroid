package cn.qingchengfit.items;

import android.view.View;
import android.widget.Button;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PrimaryBtnItem extends AbstractFlexibleItem<PrimaryBtnItem.PrimaryBtnVH> {
  String content;

  public PrimaryBtnItem(String content) {
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_primary_rect_stroke_btn;
  }

  @Override public PrimaryBtnVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PrimaryBtnVH(view,adapter);
  }


  @Override public void bindViewHolder(FlexibleAdapter adapter, PrimaryBtnVH holder, int position,
      List payloads) {
    holder.btn.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PrimaryBtnVH extends FlexibleViewHolder {
	Button btn;

    public PrimaryBtnVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      btn = (Button) view.findViewById(R.id.btn);

      btn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RxBus.getBus().post(new EventClickViewPosition.Builder().id(getLayoutRes()).build());
        }
      });
    }
  }
}