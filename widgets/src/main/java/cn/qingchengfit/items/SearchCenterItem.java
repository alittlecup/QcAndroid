package cn.qingchengfit.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SearchCenterItem extends AbstractFlexibleItem<SearchCenterItem.SearchCenterVH> {

  public boolean edEnable;
  public String hint;

  public SearchCenterItem(boolean edEnable, String hint) {
    this.edEnable = edEnable;
    this.hint = hint;
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_search_hint_center;
  }

  @Override public SearchCenterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new SearchCenterVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, SearchCenterVH holder, int position,
      List payloads) {
    holder.etSearch.setEnabled(edEnable);
    holder.etSearch.setHint(hint);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class SearchCenterVH extends FlexibleViewHolder {
    @BindView(R2.id.et_search) TextView etSearch;
    @BindView(R2.id.layout_earchView) LinearLayout layoutEarchView;

    public SearchCenterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}