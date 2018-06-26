package cn.qingchengfit.saasbase.mvvm_student.view.state;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageStudentStateInfoBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.inter.IncreaseType;
import cn.qingchengfit.saasbase.mvvm_student.items.SalerStudentInfoItem;
import cn.qingchengfit.saasbase.mvvm_student.widget.CountDateView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/saler/student") public class StudentStateInfoPage
    extends StudentBaseFragment<PageStudentStateInfoBinding, StudentStateInfoViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;
  List<SalerStudentListView> fragmentList = new ArrayList<>();

  @Override protected void subscribeUI() {

  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentStateInfoBinding.inflate(inflater, container, false);
    initToolbar();
    initTab();
    initFragments();
    initViewPager();
    return mBinding;
  }

  private void initFragments() {
    fragmentList.add(new SalerStudentListView());
    fragmentList.add(new SalerStudentListView());
    fragmentList.add(new SalerStudentListView());
    fragmentList.add(new SalerStudentListView());
  }

  private void initViewPager() {
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    fragmentList.get(0).setOnItemClickListener(this);
    fragmentList.get(1).setOnItemClickListener(this);
    fragmentList.get(2).setOnItemClickListener(this);
    fragmentList.get(3).setOnItemClickListener(this);

    fragmentList.get(0).setItems(getItems());
    fragmentList.get(1).setItems(getItems());
    fragmentList.get(2).setItems(getItems());
    fragmentList.get(3).setItems(getItems());


  }

  private List<? extends AbstractFlexibleItem> getItems() {
    List<SalerStudentInfoItem> items=new ArrayList<>();
    items.add(new SalerStudentInfoItem());
    items.add(new SalerStudentInfoItem());
    items.add(new SalerStudentInfoItem());
    items.add(new SalerStudentInfoItem());
    return items;
  }

  private CountDateView preChecked;

  private void initTab() {
    mBinding.countOne.setContent("1-3天");
    mBinding.countOne.setCount("1000");
    mBinding.countOne.setContentColor(getResources().getColor(R.color.success_green));
    mBinding.countOne.setChecked(true);
    preChecked = mBinding.countOne;

    mBinding.countFour.setContent("4-7天");
    mBinding.countFour.setCount("150");
    mBinding.countFour.setContentColor(getResources().getColor(R.color.registe_line_color));

    mBinding.countEight.setContent("8-14天");
    mBinding.countEight.setCount("60");
    mBinding.countEight.setContentColor(getResources().getColor(R.color.orange));

    mBinding.countFourteen.setContent(">14天");
    mBinding.countFourteen.setCount("60");
    mBinding.countFourteen.setContentColor(getResources().getColor(R.color.danger_red_normal));

  }


  private void initToolbar() {
    ToolbarModel toolbarModel = null;
    switch (curType) {
      case IncreaseType.INCREASE_FOLLOWUP:
        toolbarModel = new ToolbarModel("已接洽");
        break;
      case IncreaseType.INCREASE_STUDENT:
        toolbarModel = new ToolbarModel("会员");

        break;
      case IncreaseType.INCREASE_MEMBER:
        toolbarModel = new ToolbarModel("新注册用户");
        break;
    }
    if (toolbarModel != null) {
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {

    return false;
  }

  class StateViewPager extends FragmentStatePagerAdapter {

    public StateViewPager(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      if (position < fragmentList.size()) {
        return fragmentList.get(position);
      } else {
        return new Fragment();
      }
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
