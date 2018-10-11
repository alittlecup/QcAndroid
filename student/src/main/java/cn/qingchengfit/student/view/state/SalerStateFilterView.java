package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import cn.qingchengfit.student.bean.InactiveBean;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.List;

public class SalerStateFilterView extends BaseFilterFragment {
  FilterListStringFragment fragment;
  private List<InactiveBean> inactiveBeans;
  SalerStudentStateViewModel viewModel;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initFragments();
    viewModel = ViewModelProviders.of(getParentFragment()).get(SalerStudentStateViewModel.class);
  }

  public void setInactiveBeans(List<InactiveBean> inactiveBeans) {
    this.inactiveBeans = inactiveBeans;
    if (fragment != null) {
      fragment.setStrings(formatList(inactiveBeans));
    }
  }

  @Override public void dismiss() {
    viewModel.filterVisible.setValue(false);
  }

  private void initFragments() {
    fragment = new FilterListStringFragment();
    fragment.setStrings(formatList(inactiveBeans));
    fragment.setOnSelectListener(i -> {
      viewModel.setCurAttack(inactiveBeans.get(i));
      dismiss();
    });
  }


  private String[] formatList(List<InactiveBean> inactiveBeans) {
    if (inactiveBeans == null || inactiveBeans.isEmpty()) return new String[0];
    String[] items = new String[inactiveBeans.size()];
    for (int i = 0; i < inactiveBeans.size(); i++) {
      items[i] = inactiveBeans.get(i).getPeriod() + "未跟进";
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
