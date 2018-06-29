package cn.qingchengfit.saasbase.mvvm_student.view.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;

public class SalerStateFilterView extends BaseFilterFragment {
  FilterListStringFragment fragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initFragments();
  }

  private void initFragments() {
    fragment = new FilterListStringFragment();
    String[] strings = new String[] { "1-3天未跟进", "4-7天未跟进", "8-14天未跟进", ">14天未跟进" };
    fragment.setStrings(strings);
    fragment.setOnSelectListener(i -> {

    });
  }

  @Override protected String[] getTags() {
    return new String[] { "list" };
  }

  @Override protected Fragment getFragmentByTag(String s) {
    if (getTags()[0].equals(s)) {
      return fragment;
    }
    return new EmptyFragment();
  }
}
