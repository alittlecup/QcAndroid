package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.ViewModelProviders;

public class StudentCoachFilterView extends StudentFilterView {

  private String name = "";
  private String value = "";

  @Override protected void subscribeUI() {
    mViewModel = ViewModelProviders.of(this, factory).get(FilterCoachVM.class);
    ((FilterCoachVM) mViewModel).changePosition.observe(this, integer -> {
      if (integer != null)
        adapter.notifyDataSetChanged();
    });
    super.subscribeUI();
  }

  @Override public void onSelectedItem() {
    if (!name.isEmpty() && !value.isEmpty())
      ((FilterCoachVM)mViewModel).setFilterItem(name, value);
  }

  public void setFilterItem(String name, String value){
    this.name = name;
    this.value = value;
  }

}
