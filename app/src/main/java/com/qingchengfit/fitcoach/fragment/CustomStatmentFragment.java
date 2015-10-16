package com.qingchengfit.fitcoach.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
import com.qingchengfit.fitcoach.http.bean.QcStudentResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomStatmentFragment extends Fragment {

    public static final String TAG = CustomStatmentFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.custom_statment_gym)
    CommonInputView customStatmentGym;
    @Bind(R.id.custom_statment_start)
    CommonInputView customStatmentStart;
    @Bind(R.id.custom_statment_end)
    CommonInputView customStatmentEnd;
    @Bind(R.id.custom_statment_course)
    CommonInputView customStatmentCourse;
    @Bind(R.id.custom_statment_student)
    CommonInputView customStatmentStudent;

    private Calendar date;
    private List<SpinnerBean> spinnerBeans = new ArrayList<>();
    private List<String> gymStrings = new ArrayList<>();
    private List<String> courseStrings = new ArrayList<>();
    private List<String> studentStrings = new ArrayList<>();
    private int chooseGymId = 0;
    private int chooseUserId = 0;
    private int chooseCoursId = 0;
    private List<QcStudentBean> studentBeans;
    private Observer<QcStudentResponse> studentResponseObserver = new Observer<QcStudentResponse>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(QcStudentResponse qcStudentResponse) {
            studentBeans = qcStudentResponse.data.users;
            studentStrings.clear();
            studentStrings.add("全部学员");
            for (QcStudentBean studentBean : studentBeans) {
                studentStrings.add(studentBean.username);
            }

        }
    };
    private List<QcCourseResponse.Course> courses;
    private Observer<QcCourseResponse> courseResponseObserver = new Observer<QcCourseResponse>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(QcCourseResponse qcCourseResponse) {
            courses = qcCourseResponse.data.courses;
            courseStrings.clear();
            courseStrings.add("全部课程");
            for (QcCourseResponse.Course studentBean : courses) {
                courseStrings.add(studentBean.name);
            }

        }
    };

    public CustomStatmentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = Calendar.getInstance();
        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
                    spinnerBeans.add(new SpinnerBean("", "全部健身房", 0));
                    gymStrings.add("全部健身房");
                    for (int i = 0; i < systems.size(); i++) {
                        QcCoachSystem system = systems.get(i);
                        spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id));
                        gymStrings.add(system.name);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_statment, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("自定义报表");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        view.setOnTouchListener((v, event) -> {
            return true;
        });
        initView();

        return view;
    }

    private void initView() {
        customStatmentCourse.setContent("所有课程");
        customStatmentStart.setContent(DateUtils.getServerDateDay(new Date()));
        customStatmentEnd.setContent(DateUtils.getServerDateDay(new Date()));
        customStatmentGym.setContent("选择健身房");
        customStatmentStudent.setContent("所有学员");
    }

    @OnClick(R.id.custom_statment_course)
    public void onClickCourse() {
        new MaterialDialog.Builder(getContext())
                .title("请选择课程")
                .items(courseStrings.toArray(new String[courseStrings.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        customStatmentCourse.setContent(charSequence.toString());
                        if (i == 0) {
                            chooseCoursId = 0;
                        } else {
                            chooseCoursId = courses.get(i - 1).id;
                        }
                    }
                }).show();
    }

    @OnClick(R.id.custom_statment_end)
    public void onClickEnd() {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                customStatmentEnd.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
            }
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.custom_statment_start)
    public void onClickStart() {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                customStatmentStart.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
            }
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.custom_statment_gym)
    public void onClickGym() {
        new MaterialDialog.Builder(getContext())
                .title("请选择健身房")
                .items(gymStrings.toArray(new String[gymStrings.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        LogUtil.e("choose:" + which);
                        customStatmentGym.setContent(text.toString());
                        chooseGymId = spinnerBeans.get(which).id;
                        if (which == 0) {
                            customStatmentStudent.setVisibility(View.GONE);
                            customStatmentCourse.setVisibility(View.GONE);
                        } else {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("system_id", Integer.toString(chooseGymId));

                            QcCloudClient.getApi().getApi.qcGetSystemStudent(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(studentResponseObserver);
                            QcCloudClient.getApi().getApi.qcGetSystemCourses(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(courseResponseObserver);

                            customStatmentStudent.setVisibility(View.VISIBLE);
                            customStatmentCourse.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .show();
    }

    @OnClick(R.id.custom_statment_student)
    public void onClickStudent() {
        new MaterialDialog.Builder(getContext())
                .title("请选择学员")
                .items(studentStrings.toArray(new String[studentStrings.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        customStatmentStudent.setContent(charSequence.toString());
                        if (i == 0) {
                            chooseUserId = 0;
                        } else {
                            chooseUserId = studentBeans.get(i - 1).id;
                        }
                    }
                }).show();

    }

    @OnClick(R.id.custom_statment_generate)
    public void onClickGenerate() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(3,
                        customStatmentStart.getContent(), customStatmentEnd.getContent(),
                        chooseGymId, chooseUserId, chooseCoursId))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
