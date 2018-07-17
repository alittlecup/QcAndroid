package cn.qingchengfit.student.view.state;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.student.databinding.PageStudentStateInfoBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.item.SalerStudentInfoItem;
import cn.qingchengfit.student.widget.CountDateView;
import cn.qingchengfit.student.widget.CountTextView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Leaf(module = "student", path = "/saler/student") public class StudentStateInfoPage
    extends StudentBaseFragment<PageStudentStateInfoBinding, StudentStateInfoViewModel>
    implements FlexibleAdapter.OnItemClickListener, CountDateView.OnCheckedChangeListener {
  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;
  List<SalerStudentListView> fragmentList = new ArrayList<>();

  @Override protected void subscribeUI() {
    mViewModel.statInfo.observe(this, statInfo -> {
      hideLoading();
      if (statInfo == null) return;
      if (preChecked != null) return;
      mBinding.tvAllStudent.setText(String.valueOf(statInfo.getCount()));
      initTab(statInfo.getUnattacked());
    });

    mViewModel.getLiveItems().observe(this, items -> {
      hideLoading();
    });
  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentStateInfoBinding.inflate(inflater, container, false);
    initToolbar();
    return mBinding;
  }

  private void initViewPager() {
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    mBinding.viewpager.setOnTouchListener((v, event) -> true);
  }

  private CountDateView preChecked;
  private int[] Colors = new int[] {
      R.color.success_green, R.color.st_student_status_member, R.color.orange,
      R.color.danger_red_normal
  };

  private void initTab(List<MemberStat.UnAttacked> attackeds) {
    if (attackeds == null || attackeds.isEmpty()) return;
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.weight = 1;
    for (int i = 0; i < attackeds.size(); i++) {
      MemberStat.UnAttacked attacked = attackeds.get(i);
      CountDateView textView = new CountDateView(getContext());
      textView.setContent(attacked.getDesc());
      textView.setCount(String.valueOf(attacked.getCount()));
      textView.setContentColor(getResources().getColor(Colors[i % 4]));
      textView.setOnCheckedChangeListener(this);
      textView.setTag(attacked.getId());
      textView.setLayoutParams(layoutParams);
      textView.setGravity(Gravity.CENTER_HORIZONTAL);

      if (i == 0) {
        preChecked = textView;
        textView.setChecked(true);
      }
      mBinding.llStatInfo.addView(textView);

      SalerStudentListView salerStudentListView = new SalerStudentListView();
      salerStudentListView.setOnItemClickListener(this);

      fragmentList.add(salerStudentListView);
    }
    initViewPager();
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
    showLoading();
    mViewModel.setCurType(curType);
    if (toolbarModel != null) {
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    routeTo("/student/seller_state", new SalerStudentStatePageParams().type(curType)
        .attackeds(new ArrayList<>(mViewModel.statInfo.getValue().getUnattacked()))
        .staff(mViewModel.getLiveItems().getValue().get(position).getData())
        .build());
    return false;
  }

  @Override public void onCheckedChanged(CountDateView buttonView, boolean isChecked) {
    if (isChecked) {
      if (preChecked == buttonView) return;
      mBinding.viewpager.setCurrentItem(mBinding.llStatInfo.indexOfChild(buttonView));
      preChecked.setChecked(false);
      preChecked = buttonView;

      Map<String, Object> params = new HashMap<>();
      params.put("time_period_id", buttonView.getTag());
      showLoading();
      mViewModel.loadSource(params);
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
