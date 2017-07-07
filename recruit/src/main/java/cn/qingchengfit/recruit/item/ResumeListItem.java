package cn.qingchengfit.recruit.item;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.views.ResumeListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeListItem extends AbstractFlexibleItem<ResumeListItem.ResumeListVH> {
  Fragment fragment;
  ResumeListFragment resumeListFragment;

  public ResumeListItem(Fragment fragment) {
    this.fragment = fragment;
    resumeListFragment = ResumeListFragment.newResumeListInstance();
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_list;
  }

  @Override public ResumeListVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ResumeListVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeListVH holder, int position,
      List payloads) {
    fragment.getChildFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
        .replace(R.id.frag_item_resume, resumeListFragment)
        .commit();
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeListVH extends FlexibleViewHolder {

    public ResumeListVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}