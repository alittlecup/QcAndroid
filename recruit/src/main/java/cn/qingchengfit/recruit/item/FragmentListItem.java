package cn.qingchengfit.recruit.item;

import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FragmentListItem extends AbstractFlexibleItem<FragmentListItem.ResumeListVH> {
  Fragment fragment;
  Fragment childFragment;

  public FragmentListItem(Fragment fragment, Fragment child) {
    this.fragment = fragment;
    this.childFragment = child;
  }

  public Fragment getChildFragment() {
    return childFragment;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_list;
  }

  @Override public ResumeListVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeListVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeListVH holder, int position,
      List payloads) {
    fragment.getChildFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
        .replace(R.id.frag_item_resume, childFragment)
        .commit();

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeListVH extends FlexibleViewHolder {
	NestedScrollView frameLayout;
    public ResumeListVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      frameLayout = (NestedScrollView) view.findViewById(R.id.frag_item_resume);

      //frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      //  @Override public void onGlobalLayout() {
      //    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
      //    if (frameLayout.getParent() instanceof RecyclerView){
      //      RecyclerView rv =  (RecyclerView) frameLayout.getParent();
      //      final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
      //      rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
      //        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      //          super.onScrollStateChanged(recyclerView, newState);
      //        }
      //
      //        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      //          super.onScrolled(recyclerView, dx, dy);
      //          // TODO: 2017/7/13 硬编码
      //          if (lm.findFirstCompletelyVisibleItemPosition() >= 2){
      //            //if (frameLayout.canScroll(-1)){
      //              frameLayout.requestDisallowInterceptTouchEvent(true);
      //            //}else frameLayout.requestDisallowInterceptTouchEvent(false);
      //          }else {
      //            frameLayout.requestDisallowInterceptTouchEvent(false);
      //          }
      //        }
      //      });
      //    }
      //  }
      //});


    }
  }
}