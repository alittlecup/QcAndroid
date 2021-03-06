package cn.qingchengfit.student.view.state;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.student.Utils;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Leaf(module = "student", path = "/saler/student") public class StudentStateInfoPage
    extends StudentBaseFragment<PageStudentStateInfoBinding, StudentStateInfoViewModel>
    implements FlexibleAdapter.OnItemClickListener, CountDateView.OnCheckedChangeListener,
    OnChartValueSelectedListener {
  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;
  List<SalerStudentListView> fragmentList = new ArrayList<>();
  private boolean isNoData=false;
  @Override protected void subscribeUI() {
    mViewModel.inactiveStat.observe(this, inactiveStat -> {
      if(inactiveStat.getInactive().getTotal_count()==0){
        isNoData=true;
      }else{
        isNoData=false;
      }
      initTab(inactiveStat.getInactive().getStat_data());
    });
  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentStateInfoBinding.inflate(inflater, container, false);
    initToolbar();
    initPieChart();
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  private void initPieChart() {
    mBinding.pieChart.setOnChartValueSelectedListener(this);
    mBinding.pieChart.setHoleRadius(64f);
    mBinding.pieChart.setCenterTextColor(getResources().getColor(R.color.text_black));
  }

  private void initViewPager() {
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    mBinding.viewpager.setOffscreenPageLimit(4);
    mBinding.viewpager.setScrollble(false);
  }

  private CountDateView preChecked;

  List<Integer> colors = new ArrayList<>();

  private void initTab(List<InactiveBean> inactiveBeans) {
    if (inactiveBeans == null || inactiveBeans.isEmpty()) return;
    ArrayList<PieEntry> entries = new ArrayList<>();
    for (int i = 0; i < inactiveBeans.size(); i++) {
      InactiveBean inactiveBean = inactiveBeans.get(i);
      View childAt = mBinding.llCountDate.getChildAt(i * 2);
      if (childAt instanceof CountDateView) {
        ((CountDateView) childAt).setContent(inactiveBean.getPeriod());
        ((CountDateView) childAt).setCount(inactiveBean.getCount());
        ((CountDateView) childAt).setOnCheckedChangeListener(this);
      }
      dealFragmentList(inactiveBean, i);
      entries.add(new PieEntry(isNoData?1:inactiveBean.getCount(), i));
    }
    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setDrawValues(false);
    dataSet.setSelectionShift(8f);
    dataSet.setSliceSpace(0f);
    dataSet.setColors(colors);
    PieData data = new PieData(dataSet);
    mBinding.pieChart.setData(data);
    mBinding.pieChart.highlightValue(0, inactiveBeans.get(0).getCount(), 0, true);
    mBinding.pieChart.invalidate();
    initViewPager();

    ((CountDateView) mBinding.llCountDate.getChildAt(
        mBinding.viewpager.getCurrentItem() * 2)).setChecked(true);
  }

  private void dealFragmentList(InactiveBean inactiveBean, int position) {
    List<SellerStat> seller_stat = inactiveBean.getSeller_stat();
    if (fragmentList.isEmpty() || fragmentList.size() < 4) {
      fragmentList.add(generateListView(seller_stat));
    } else {
      SalerStudentListView salerStudentListView = fragmentList.get(position);
      Collections.sort(seller_stat, (o1, o2) -> o2.getTotal_count() - o1.getTotal_count());
      List<SalerStudentInfoItem> items = new ArrayList<>();
      for (int i = 0; i < seller_stat.size(); i++) {
        items.add(new SalerStudentInfoItem(seller_stat.get(i), curType));
      }
      salerStudentListView.setItems(items);
    }
  }

  private SalerStudentListView generateListView(List<SellerStat> seller_stat) {
    SalerStudentListView salerStudentListView = new SalerStudentListView();
    salerStudentListView.setOnItemClickListener(this);
    Collections.sort(seller_stat, (o1, o2) -> o2.getTotal_count() - o1.getTotal_count());
    List<SalerStudentInfoItem> items = new ArrayList<>();
    for (int i = 0; i < seller_stat.size(); i++) {
      items.add(new SalerStudentInfoItem(seller_stat.get(i), curType));
    }
    salerStudentListView.setItems(items);
    return salerStudentListView;
  }

  private String endSub = "未跟进";

  private void initToolbar() {
    ToolbarModel toolbarModel = null;
    switch (curType) {
      case IncreaseType.INCREASE_FOLLOWUP:
        toolbarModel = new ToolbarModel("已接洽");
        mViewModel.loadSource(1);
        mViewModel.backgroundColor.setValue(getResources().getColor(R.color.st_follow_ing_color));
        initColors(getResources().getColor(R.color.st_follow_ing_color));

        break;
      case IncreaseType.INCREASE_STUDENT:
        toolbarModel = new ToolbarModel("会员");
        mViewModel.loadSource(2);
        mViewModel.backgroundColor.setValue(getResources().getColor(R.color.st_new_student_color));
        initColors(getResources().getColor(R.color.st_new_student_color));

        break;
      case IncreaseType.INCREASE_MEMBER:
        toolbarModel = new ToolbarModel("新注册");
        mViewModel.loadSource(0);
        mViewModel.backgroundColor.setValue(getResources().getColor(R.color.st_new_member_color));
        initColors(getResources().getColor(R.color.st_new_member_color));

        break;
    }
    mBinding.tvNotFollow.setText("未跟进时长分布");
    endSub = "未跟进";
    mBinding.tvDesc.setText("未跟进天数");
    if (toolbarModel != null) {
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initColors(int baseColor) {
    colors.add(Utils.getColorWithAlpha(0.2f, baseColor));
    colors.add(Utils.getColorWithAlpha(0.4f, baseColor));
    colors.add(Utils.getColorWithAlpha(0.6f, baseColor));
    colors.add(Utils.getColorWithAlpha(1f, baseColor));
  }

  @Override public boolean onItemClick(int position) {
    int currentItem = mBinding.viewpager.getCurrentItem();
    Staff seller = mViewModel.inactiveStat.getValue()
        .getInactive()
        .getStat_data()
        .get(currentItem)
        .getSeller_stat()
        .get(position)
        .getSeller();
    if (seller == null) {
      seller = new Staff();
      seller.setUsername("未分配");
    }
    routeTo("/student/seller_state", new SalerStudentStatePageParams().type(curType)
        .staff(seller)
        .beans(new ArrayList<>(mViewModel.inactiveStat.getValue().getInactive().getStat_data()))
        .position(currentItem)
        .build());
    return false;
  }

  @Override public void onCheckedChanged(CountDateView buttonView, boolean isChecked) {
    if (isChecked) {
      if (preChecked == buttonView) return;
      int i = mBinding.llCountDate.indexOfChild(buttonView);
      mBinding.viewpager.setCurrentItem(i / 2);
      if (preChecked != null) {
        preChecked.setChecked(false);
      }
      preChecked = buttonView;
      mBinding.tvDetail.setText(buttonView.getContent() + "未跟进用户所属销售");
      mBinding.pieChart.highlightValue(i / 2, buttonView.getCount(), 0, true);
    }
  }

  @Override public void onValueSelected(Entry e, Highlight h) {
    int data = (int) e.getData();
    InactiveBean inactiveBean =
        mViewModel.inactiveStat.getValue().getInactive().getStat_data().get(data);
    SpannableStringBuilder text = new SpanUtils().append("" + inactiveBean.getCount())
        .setFontSize(72, true)
        .append("人")
        .setFontSize(30, true)
        .append("\n" + inactiveBean.getPeriod() + endSub)
        .setFontSize(36, true)
        .setForegroundColor(getResources().getColor(R.color.text_grey))
        .create();
    mBinding.pieChart.setCenterText(text);
    View childAt = mBinding.llCountDate.getChildAt(data * 2);
    if (childAt instanceof CountDateView) {
      ((CountDateView) childAt).setChecked(true);
    }
  }

  @Override public void onNothingSelected() {

  }

  class StateViewPager extends FragmentStatePagerAdapter {
    public StateViewPager(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return fragmentList.get(position);
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
