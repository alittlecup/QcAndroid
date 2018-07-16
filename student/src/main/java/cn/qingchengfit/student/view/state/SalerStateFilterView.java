package cn.qingchengfit.student.view.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.List;

public class SalerStateFilterView extends BaseFilterFragment {
  FilterListStringFragment fragment;
  private List<MemberStat.UnAttacked> attackeds;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initFragments();
  }

  public void setUnAttackeds(List<MemberStat.UnAttacked> attackeds) {
    this.attackeds = attackeds;
    if (fragment != null) {
      fragment.setStrings(formatList(attackeds));
    }
  }

  private void initFragments() {
    fragment = new FilterListStringFragment();
    fragment.setStrings(formatList(attackeds));
    fragment.setOnSelectListener(i -> {

    });
  }

  private String[] formatList(List<MemberStat.UnAttacked> attackeds) {
    if (attackeds == null || attackeds.isEmpty()) return new String[0];
    String[] items = new String[attackeds.size()];
    for (int i = 0; i < attackeds.size(); i++) {
      items[i] = attackeds.get(i).getDesc() + "未跟进";
    }
    return items;
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
