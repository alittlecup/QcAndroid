package cn.qingchengfit.student.view.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.databinding.FragmentClassRecordTempBinding;
import cn.qingchengfit.student.item.AttendanceAnalysItem;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.views.fragments.TitleFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassRecordTempFragment
    extends StudentBaseFragment<FragmentClassRecordTempBinding, ClassRecordTempVM>
    implements TitleFragment {
  FragmentClassRecordTempBinding mBinding;
  CommonFlexAdapter adapter;

  public static ClassRecordTempFragment getInstanceWithStudentID(String id) {
    ClassRecordTempFragment fragment = new ClassRecordTempFragment();
    fragment.setArguments(new BundleBuilder().withString("student_id", id).build());
    return fragment;
  }

  @Override protected void subscribeUI() {
  }

  @Override public FragmentClassRecordTempBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = FragmentClassRecordTempBinding.inflate(inflater, container, false);
    mBinding.setFragment(this);
    initRecyclerView();
    loadData();
    return mBinding;
  }

  private void loadData() {
    Map<String, Object> params = new HashMap<>();
    params.put("shop_ids", "0");
    String student_id = getArguments().getString("student_id", "");
    mViewModel.loadParams(student_id, params).observe(this, classRecords -> {
      if (classRecords != null) {
        updateItem(classRecords.stat);
      }
    });
  }

  private void initRecyclerView() {
    SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getContext(), 4);
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerView.setLayoutManager(manager);
  }

  private List<AbstractFlexibleItem> items = new ArrayList<>();

  public void updateItem(ClassRecords.Stat stat) {
    if (stat == null) return;
    items.clear();
    items.add(new AttendanceAnalysItem(stat.days + "", "出勤",
        CompatUtils.getColor(getContext(), R.color.orange), getString(R.string.unit_day)));
    items.add(new AttendanceAnalysItem(stat.checkin + "", "签到", Color.parseColor("#8cb5ba"),
        getString(R.string.unit_time)));
    items.add(new AttendanceAnalysItem(stat.group + "", "团课",
        CompatUtils.getColor(getContext(), R.color.blue), "节"));
    items.add(new AttendanceAnalysItem(stat.private_count + "", "私教",
        CompatUtils.getColor(getContext(), R.color.purple), "节"));

    adapter.updateDataSet(items);
  }

  public void routeToDetail(View v) {
    routeTo("student", "/student/class/record", null);
  }

  @Override public String getTitle() {
    return "训练记录";
  }
}
