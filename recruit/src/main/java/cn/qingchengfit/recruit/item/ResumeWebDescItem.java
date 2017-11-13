package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.utils.CmStringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeWebDescItem extends AbstractFlexibleItem<ResumeWebDescItem.ResumeWebDescVH> {

  String desc;

  public ResumeWebDescItem(String desc) {
    this.desc = desc;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_desc_detail;
  }

  @Override public ResumeWebDescVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeWebDescVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeWebDescVH holder, int position, List payloads) {
    holder.webview.loadData(CmStringUtils.getMobileHtml(desc), "text/html; charset=UTF-8", null);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeWebDescVH extends FlexibleViewHolder {
    @BindView(R2.id.webview) WebView webview;

    public ResumeWebDescVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}