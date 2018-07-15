package cn.qingchengfit.items;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SearchCenterItem extends AbstractFlexibleItem<SearchCenterItem.SearchCenterVH>
    //implements IHeader<SearchCenterItem.SearchCenterVH>
{

  public boolean edEnable;
  public String hint;

  public SearchCenterItem(boolean edEnable, String hint) {
    this.edEnable = edEnable;
    this.hint = hint;
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_search_hint_center;
  }

  @Override public SearchCenterVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new SearchCenterVH(view,adapter);
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
	TextView etSearch;
	LinearLayout layoutEarchView;

    public SearchCenterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      etSearch = (TextView) view.findViewById(R.id.et_search);
      layoutEarchView = (LinearLayout) view.findViewById(R.id.layout_earchView);
    }
  }
}