package cn.qingchengfit.saasbase.mvvm_student.view.state;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageStudentStateInfoBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.inter.IncreaseType;
import cn.qingchengfit.saasbase.mvvm_student.items.SalerStudentInfoItem;
import cn.qingchengfit.saasbase.mvvm_student.widget.CountDateView;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/saler/student") public class StudentStateInfoPage
    extends StudentBaseFragment<PageStudentStateInfoBinding, StudentStateInfoViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentStateInfoBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    initTab();
    initData();
    return mBinding;
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


  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerView.setAdapter(adapter);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter.addListener(this);
  }

  private void initData() {
    List<SalerStudentInfoItem> items = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      items.add(new SalerStudentInfoItem());
    }
    adapter.updateDataSet(items);
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
}
