package cn.qingchengfit.student.view.state;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.bean.InactiveBean;
import cn.qingchengfit.student.bean.SellerStat;
import cn.qingchengfit.student.databinding.PageStudentStateInfoBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.item.SalerStudentInfoItem;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.widget.CountDateView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Leaf(module = "student", path = "/saler/student") public class StudentStateInfoPage
    extends StudentBaseFragment<PageStudentStateInfoBinding, StudentStateInfoViewModel>
    implements FlexibleAdapter.OnItemClickListener, CountDateView.OnCheckedChangeListener {
  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;
  List<SalerStudentListView> fragmentList = new ArrayList<>();

  @Override protected void subscribeUI() {
    mViewModel.inactiveStat.observe(this, inactiveStat -> {
      initTab(inactiveStat.getInactive());
    });
  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentStateInfoBinding.inflate(inflater, container, false);
    initToolbar();
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  private void initViewPager() {
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    mBinding.viewpager.setOnTouchListener((v, event) -> true);
    mBinding.viewpager.setClickable(false);
  }

  private CountDateView preChecked;
  private int[] Colors = new int[] {
      R.color.success_green, R.color.st_student_status_member, R.color.orange,
      R.color.danger_red_normal
  };

  private void initTab(List<InactiveBean> inactiveBeans) {
    if (inactiveBeans == null || inactiveBeans.isEmpty()) return;
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.weight = 1;
    ArrayList<PieEntry> entries = new ArrayList<>();
    List<Integer> colors = new ArrayList<>();

    for (int i = 0; i < inactiveBeans.size(); i++) {
      InactiveBean inactiveBean = inactiveBeans.get(i);
      CountDateView textView = new CountDateView(getContext());
      textView.setContent(inactiveBean.getPeriod());
      textView.setCount(String.valueOf(inactiveBean.getCount()));
      int color = getResources().getColor(Colors[i % 4]);
      colors.add(color);
      textView.setContentColor(color);
      textView.setOnCheckedChangeListener(this);
      textView.setTag(inactiveBean.getId());
      textView.setLayoutParams(layoutParams);
      textView.setGravity(Gravity.CENTER_HORIZONTAL);
      if (i == 0) {
        textView.setChecked(true);
      }
      mBinding.llStatInfo.addView(textView);
      fragmentList.add(generateListView(inactiveBean.getSeller_stat()));

      entries.add(new PieEntry(inactiveBean.getCount()));
    }
    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setDrawValues(false);
    dataSet.setSelectionShift(3f);
    dataSet.setSliceSpace(0f);
    dataSet.setColors(colors);
    PieData data = new PieData(dataSet);
    mBinding.pieChart.setData(data);
    mBinding.pieChart.highlightValue(null);
    mBinding.pieChart.invalidate();
    initViewPager();
  }

  private SalerStudentListView generateListView(List<SellerStat> seller_stat) {
    SalerStudentListView salerStudentListView = new SalerStudentListView();
    salerStudentListView.setOnItemClickListener(this);
    Collections.sort(seller_stat, (o1, o2) -> o2.getTotal_count() - o1.getTotal_count());
    List<SalerStudentInfoItem> items = new ArrayList<>();
    for (int i = 0; i < seller_stat.size(); i++) {
      items.add(new SalerStudentInfoItem(seller_stat.get(i)));
    }
    salerStudentListView.setItems(items);
    return salerStudentListView;
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = null;
    switch (curType) {
      case IncreaseType.INCREASE_FOLLOWUP:
        toolbarModel = new ToolbarModel("已接洽");
        mBinding.tvNotFollow.setText("未跟进时长分布 (人)");
        mBinding.tvAllContent.setText("全部已接洽 (人)");
        mViewModel.loadSource(1);

        break;
      case IncreaseType.INCREASE_STUDENT:
        toolbarModel = new ToolbarModel("会员");
        mBinding.tvNotFollow.setText("未出勤时长分布 (人)");
        mBinding.tvAllContent.setText("全部会员 (人)");
        mViewModel.loadSource(2);
        break;
      case IncreaseType.INCREASE_MEMBER:
        toolbarModel = new ToolbarModel("新注册用户");
        mBinding.tvNotFollow.setText("未跟进时长分布 (人)");
        mBinding.tvAllContent.setText("全部新注册 (人)");
        mViewModel.loadSource(0);
        break;
    }
    if (toolbarModel != null) {
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    int currentItem = mBinding.viewpager.getCurrentItem();
    Staff seller = mViewModel.inactiveStat.getValue()
        .getInactive()
        .get(currentItem)
        .getSeller_stat()
        .get(position)
        .getSeller();
    if (seller == null) {
      seller = new Staff();
      seller.setId("0");
      seller.setUsername("未分配");
    }
    routeTo("/student/seller_state", new SalerStudentStatePageParams().type(curType)
        .staff(seller)
        .beans(new ArrayList<>(mViewModel.inactiveStat.getValue().getInactive()))
        .build());
    return false;
  }

  @Override public void onCheckedChanged(CountDateView buttonView, boolean isChecked) {
    if (isChecked) {
      if (preChecked == buttonView) return;
      mBinding.viewpager.setCurrentItem(mBinding.llStatInfo.indexOfChild(buttonView));
      if (preChecked != null) {
        preChecked.setChecked(false);
      }
      preChecked = buttonView;
      mBinding.tvDetail.setText(buttonView.getContent() + "未跟进用户销售任务统计");
    }
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
