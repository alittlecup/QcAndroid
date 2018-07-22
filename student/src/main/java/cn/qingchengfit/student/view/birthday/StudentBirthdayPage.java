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
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.StPageStudentBirthdayBinding;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.IFlexible;
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
    mViewModel.dates.observe(this, items -> {
      if (items == null || items.isEmpty()) return;
      List<Calendar> calendars = new ArrayList<>();

      for (String date : items) {
        calendars.add(getSchemeCaleander(date));
      }
      mBinding.calendarView.setSchemeDate(calendars);
    });
    mViewModel.selectedCalendar.observe(this,calender->{
      mBinding.calendarView.setSelectedWeekDate(calender);
      loadStudents(mBinding.calendarView.getSelectDates());
    });
  }

  @Override
  public StPageStudentBirthdayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageStudentBirthdayBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
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
      loadMonthScheme(year, month);
    });

    mBinding.bottomAllot.allotCoach.setOnClickListener(v -> {
      ArrayList<QcStudentBeanWithFollow> qcStudents = new ArrayList<>(getStudents());
      if (qcStudents.isEmpty()) {
        return;
      }
      routeTo("/student/allot", new StudentAllotPageParams().items(new ArrayList<>(getStudents()))
          .curType(StudentListView.TRAINER_TYPE)
          .sortVisible(false)
          .build());
    });
    mBinding.bottomAllot.allotSale.setOnClickListener(v -> {
      ArrayList<QcStudentBeanWithFollow> qcStudents = new ArrayList<>(getStudents());
      if (qcStudents.isEmpty()) {
        return;
      }
      routeTo("/student/allot", new StudentAllotPageParams().items(new ArrayList<>(getStudents()))
          .curType(StudentListView.SELLER_TYPE)
          .sortVisible(false)
          .build());
    });
  }

  private void loadMonthScheme(int year, int month) {
    Map<String, Object> dates = new HashMap<>();
    dates.put("start", DateUtils.getStartDayOfMonth(year, month - 1));
    dates.put("end", DateUtils.getEndDayOfMonthNew(year, month - 1));
    mViewModel.loadSchemeDates(dates);
  }

  private List<QcStudentBeanWithFollow> getStudents() {
    List mainItems = adapter.getMainItems();
    List<QcStudentBeanWithFollow> studentBeanWithFollows = new ArrayList<>();
    if (mainItems != null && !mainItems.isEmpty()) {
      for (int i = 0; i < mainItems.size(); i++) {
        IFlexible item = adapter.getItem(i);
        if (item instanceof ChooseDetailItem) {
          studentBeanWithFollows.add(((ChooseDetailItem) item).getData());
        }
      }
    }
    return studentBeanWithFollows;
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter.setTag("choose", -1);
  }

  private void initSelect() {
    Calendar calendar = new Calendar();
    calendar.setYear(mBinding.calendarView.getCurYear());
    calendar.setMonth(mBinding.calendarView.getCurMonth());
    calendar.setDay(mBinding.calendarView.getCurDay());
    if (mViewModel.selectedCalendar.getValue() != null) {
      onDateSelected(mViewModel.selectedCalendar.getValue(), true);
    }else{
      onDateSelected(calendar, true);
    }
  }

  private Calendar getSchemeCaleander(String date) {
    Date date1 = DateUtils.formatDateFromYYYYMMDD(date);
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTime(date1);
    return getSchemeCalendar(calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH) + 1, calendar.get(java.util.Calendar.DAY_OF_MONTH));
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
      mViewModel.selectedCalendar.setValue(calendar);
    }
  }

  private void loadStudents(List<Calendar> calendars) {
    Calendar start = calendars.get(0);
    Calendar end = calendars.get(calendars.size() - 1);
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.set(start.getYear(), start.getMonth() - 1, start.getDay());
    Map<String, Object> params = new HashMap<>();
    params.put("start", DateUtils.Date2YYYYMMDD(calendar.getTime()));

    calendar.set(end.getYear(), end.getMonth() - 1, end.getDay());
    params.put("end", DateUtils.Date2YYYYMMDD(calendar.getTime()));

    mViewModel.loadSource(params);
  }
}
