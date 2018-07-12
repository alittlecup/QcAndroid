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
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/student/birthday") public class StudentBirthdayPage
    extends StudentBaseFragment<StPageStudentBirthdayBinding, StudentBirthdayViewModel>
    implements CalendarView.OnDateSelectedListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

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
    adapter.updateDataSet(getItems());
  }

  private List getItems() {
    List<FilterCommonLinearItem> items = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      items.add(new FilterCommonLinearItem(String.valueOf(i) + ": position"));
    }
    return items;
  }

  private void initSelect() {
    List<Calendar> schemes = new ArrayList<>();
    int year = mBinding.calendarView.getCurYear();
    int month = mBinding.calendarView.getCurMonth();

    schemes.add(getSchemeCalendar(year, month, 3));
    schemes.add(getSchemeCalendar(year, month, 6));
    schemes.add(getSchemeCalendar(year, month, 9));
    schemes.add(getSchemeCalendar(year, month, 13));
    schemes.add(getSchemeCalendar(year, month, 14));
    schemes.add(getSchemeCalendar(year, month, 15));
    schemes.add(getSchemeCalendar(year, month, 18));
    schemes.add(getSchemeCalendar(year, month, 25));
    mBinding.calendarView.setSchemeDate(schemes);
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

  }
}
