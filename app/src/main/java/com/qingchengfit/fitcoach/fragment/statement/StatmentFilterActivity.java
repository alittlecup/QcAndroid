package com.qingchengfit.fitcoach.fragment.statement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.DialogList;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.model.ClassStatmentFilterBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;

public class StatmentFilterActivity extends AppCompatActivity implements ClassStatmentFilterCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.start_day) CommonInputView startDay;
    @BindView(R.id.end_day) CommonInputView endDay;
    @BindView(R.id.all_student) CommonInputView allStudent;
    @BindView(R.id.rootview) LinearLayout rootview;
    @BindView(R.id.all_course) CommonInputView allCourse;
    private TimeDialogWindow pwTime;
    private ClassStatmentFilterBean filterBean;

    private ArrayList<CourseTypeSample> mFilterCourse = new ArrayList<>();
    private ArrayList<Staff> mFilterCoaches = new ArrayList<>();
    private ArrayList<StudentBean> mFilterUsers = new ArrayList<>();
    private Observable<CourseTypeSample> ObCourse;
    private String mStart;
    private String mEnd;
    private CourseChooseDialogFragment fragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statment_filter);
        ButterKnife.bind(this);
        //((App)getApplication()).getAppCompoent().inject(this);

        //handle intent
        filterBean = getIntent().getParcelableExtra("filter");
        mFilterCoaches = getIntent().getParcelableArrayListExtra("coach");
        mFilterCourse = getIntent().getParcelableArrayListExtra("course");
        mFilterUsers = getIntent().getParcelableArrayListExtra("user");
        mStart = getIntent().getStringExtra("start");
        mEnd = getIntent().getStringExtra("end");
        toolbar.setNavigationIcon(R.drawable.ic_cross_blace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if (filterBean != null) {
            //开始时间
            if (TextUtils.isEmpty(filterBean.start)) {
                startDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            } else {
                startDay.setContent(filterBean.start);
            }
            if (TextUtils.isEmpty(filterBean.end)) {
                endDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            } else {
                endDay.setContent(filterBean.end);
            }
            if (filterBean.course != null) allCourse.setContent(filterBean.course.getName());
            if (filterBean.student != null) allStudent.setContent(filterBean.student.username);
            if (filterBean.course == null ) {
                if (filterBean.course_type == -1) allCourse.setContent(getString(R.string.all_course));
                if (filterBean.course_type == -2) allCourse.setContent(getString(R.string.all_course_private));
                if (filterBean.course_type == -3) allCourse.setContent(getString(R.string.all_course_group));
            }
        } else {
            filterBean = new ClassStatmentFilterBean();
        }

        ObCourse = RxBus.getBus().register(CourseTypeSample.class);
        ObCourse.subscribe(new Action1<CourseTypeSample>() {
            @Override public void call(CourseTypeSample course) {
                float type = Float.parseFloat(course.getId());
                if (type < 0) {
                    if (type == -1) {
                        filterBean.course_type = -1;
                        filterBean.course = null;
                        allCourse.setContent(getString(R.string.all_course));
                    } else if (type == -2) {
                        filterBean.course_type = -2;
                        filterBean.course = null;
                        allCourse.setContent(getString(R.string.all_course_private));
                    } else if (type == -3) {
                        filterBean.course_type = -3;
                        filterBean.course = null;
                        allCourse.setContent(getString(R.string.all_course_group));
                    }
                } else {
                    filterBean.course_type = course.is_private?-2:-3;
                    filterBean.course = course;
                    allCourse.setContent(course.getName());
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

            }
        });
    }

    private void initView(){
        allCourse.setContent(getString(R.string.all_course));
        allStudent.setContent(getString(R.string.all_students));
    }

    @OnClick({ R.id.start_day, R.id.end_day
        , R.id.all_student, R.id.generate, R.id.all_course })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_day:
                if (pwTime == null) pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(1900, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        try {

                            if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(mStart).getTime()
                                || date.getTime() > DateUtils.formatDateFromYYYYMMDD(mEnd).getTime()) {
                                ToastUtils.show(R.drawable.ic_share_fail, "必须在范围内筛选");
                            } else {
                                pwTime.dismiss();
                                startDay.setContent(DateUtils.Date2YYYYMMDD(date));
                                filterBean.start = DateUtils.Date2YYYYMMDD(date);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Date date = new Date();
                try {
                    date = DateUtils.formatDateFromYYYYMMDD(startDay.getContent());
                } catch (Exception e) {
                }
                if (!pwTime.isShowing()) {
                    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
                }
                break;
            case R.id.end_day:
                if (pwTime == null) pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(1900, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        try {
                            Date end = DateUtils.formatDateFromYYYYMMDD(endDay.getContent());

                            if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(mStart).getTime()
                                || date.getTime() > DateUtils.formatDateFromYYYYMMDD(mEnd).getTime()) {
                                ToastUtils.show(R.drawable.ic_share_fail, "必须在范围内筛选");
                            } else {
                                pwTime.dismiss();
                                endDay.setContent(DateUtils.Date2YYYYMMDD(date));
                                filterBean.end = DateUtils.Date2YYYYMMDD(date);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Date dateend = new Date();
                try {
                    dateend = DateUtils.formatDateFromYYYYMMDD(endDay.getContent());
                } catch (Exception e) {
                }
                if (!pwTime.isShowing()) {
                    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, dateend);
                }
                break;
            case R.id.all_student://选择学员
                List<String> users = new ArrayList<>();
                users.add(getString(R.string.all_students));
                users.addAll(BusinessUtils.students2strs(mFilterUsers));
                new DialogList(this).list(users, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            allStudent.setContent(getString(R.string.all_students));
                            filterBean.student = null;
                        } else {
                            allStudent.setContent(mFilterUsers.get(position - 1).username);
                            filterBean.student = mFilterUsers.get(position - 1);
                        }
                    }
                }).title(getString(R.string.choose_student)).show();

                break;
            case R.id.all_course:
                fragment = CourseChooseDialogFragment.newInstance(mFilterCourse);
                if (!fragment.isVisible())
                    fragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.generate://生成
                if (DateUtils.formatDateFromYYYYMMDD(startDay.getContent()).getTime() > DateUtils.formatDateFromYYYYMMDD(
                    endDay.getContent()).getTime()) {
                    ToastUtils.show("开始时间必须小于结束时间");
                    return;
                }

                Intent ret = new Intent();
                ret.putExtra("filter", filterBean);
                setResult(Activity.RESULT_OK, ret);
                this.finish();
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(CourseTypeSample.class.getName(), ObCourse);
    }

    @Override public void setCourse(CourseTypeSample course) {

    }

    @Override public void setCoach(Staff coach) {
        //        filterBean.coach = coach;
        //        allCoach.setContent(coach.name);

    }

    @Override public void setStudent(StudentBean student) {
        filterBean.student = student;
        allStudent.setContent(student.username);
    }
}
