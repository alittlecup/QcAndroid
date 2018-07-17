package cn.qingchengfit.student.view.birthday;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.calendar.Calendar;
import cn.qingchengfit.saascommon.calendar.CalendarView;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageStudentBirthdayBinding;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Leaf(module = "student", path = "/student/birthday") public class StudentBirthdayPage
    extends StudentBaseFragment<StPageStudentBirthdayBinding, StudentBirthdayViewModel>
    implements CalendarView.OnDateSelectedListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      adapter.updateDataSet(items);
    });
  }

  @Override
  public StPageStudentBirthdayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageStudentBirthdayBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    initCalendarView();
    initSelect();
    initListener();
    return mBinding;
  }

  private int year;
  private int month;

  private void initListener() {
    mBinding.imgNext.setOnClickListener(view -> {
      mBinding.calendarView.scrollToNext(true);
    });
    mBinding.imgPre.setOnClickListener(view -> {
      mBinding.calendarView.scrollToPre(true);
    });
    mBinding.tvSelectMonth.setOnClickListener(view -> {
      mBinding.calendarView.setSelectedMonthDate(year, month);
      loadStudents(mBinding.calendarView.getSelectDates());
    });
    mBinding.calendarView.setOnMonthChangeListener((year, month) -> {
      this.year = year;
      this.month = month;
      mBinding.tvMonth.setText(year + "年" + month + "月");
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  private void initSelect() {
    Calendar calendar = new Calendar();
    calendar.setYear(mBinding.calendarView.getCurYear());
    calendar.setMonth(mBinding.calendarView.getCurMonth());
    calendar.setDay(mBinding.calendarView.getCurDay());
    onDateSelected(calendar, true);
  }

  private Calendar getSchemeCalendar(int year, int month, int day) {
    Calendar calendar = new Calendar();
    calendar.setYear(year);
    calendar.setMonth(month);
    calendar.setDay(day);
    return calendar;
  }

  private void initCalendarView() {
    year = mBinding.calendarView.getCurYear();
    month = mBinding.calendarView.getCurMonth();
    mBinding.calendarView.setOnDateSelectedListener(this);
    mBinding.calendarView.setWeekStarWithMon();
    mBinding.calendarView.setFixMode();
    mBinding.calendarView.updateWeekBar();
    mBinding.tvMonth.setText(
        mBinding.calendarView.getCurYear() + "年" + mBinding.calendarView.getCurMonth() + "月");
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("会员生日"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public void onDateSelected(Calendar calendar, boolean b) {
    if (b) {
      mBinding.calendarView.setSelectedWeekDate(calendar);
      loadStudents(mBinding.calendarView.getSelectDates());
    }
  }

  private void loadStudents(List<Calendar> calendars) {
    Calendar start = calendars.get(0);
    Calendar end = calendars.get(calendars.size() - 1);
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.set(start.getYear(), start.getMonth(), start.getDay());
    Map<String, Object> params = new HashMap<>();
    params.put("start", DateUtils.Date2YYYYMMDD(calendar.getTime()));

    calendar.set(end.getYear(), end.getMonth(), end.getDay());
    params.put("end", DateUtils.Date2YYYYMMDD(calendar.getTime()));

    mViewModel.loadSource(params);
  }
}
