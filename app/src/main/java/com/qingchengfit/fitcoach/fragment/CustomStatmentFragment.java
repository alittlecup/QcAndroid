package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.CourseComparator;
import com.qingchengfit.fitcoach.Utils.StudentComparator;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcServiceDetialResponse;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
import com.qingchengfit.fitcoach.http.bean.QcStudentResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    TimeDialogWindow pwTime;
    @Bind(R.id.rootview)
    LinearLayout rootview;

    private Calendar date;
    private List<SpinnerBean> spinnerBeans = new ArrayList<>();
    private List<String> gymStrings = new ArrayList<>();
    private List<String> courseStrings = new ArrayList<>();
    private List<String> studentStrings = new ArrayList<>();
    private int chooseGymId = 0;
    private String chooseGymModel = "";
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
            Collections.sort(studentBeans, new StudentComparator());
            for (QcStudentBean studentBean : studentBeans) {
                if (TextUtils.isEmpty(studentBean.username))
                    studentStrings.add(studentBean.phone);
                else
                    studentStrings.add(studentBean.username);
            }
            studentStrings.add(0, "全部学员");

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
            Collections.sort(courses, new CourseComparator());
            for (QcCourseResponse.Course studentBean : courses) {
                courseStrings.add(studentBean.name);
            }

            courseStrings.add(0, "全部课程");
        }
    };
    private String model;
    private Subscription sp;

    public CustomStatmentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = Calendar.getInstance();
        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    List<CoachService> systems = qcCoachSystemResponse.data.services;
                    spinnerBeans.add(new SpinnerBean("", "全部健身房", 0, ""));

                    for (int i = 0; i < systems.size(); i++) {
                        CoachService system = systems.get(i);
                        spinnerBeans.add(new SpinnerBean(system.color, system.name, (int) system.id, system.model));
                        gymStrings.add(system.name);
                    }
//                    Collections.sort(gymStrings,new PinyinComparator());
                    gymStrings.add(0, "全部健身房");
                }, throwable -> {
                }, () -> {
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_statment, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("自定义课程报表");
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
        customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        customStatmentGym.setContent("选择健身房场馆");
        customStatmentStudent.setContent("所有学员");
    }

    @OnClick(R.id.custom_statment_course)
    public void onClickCourse() {
        if (courseStrings.size() < 1)
            return;
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择课程");
        dialogList.list(courseStrings, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                customStatmentCourse.setContent(courseStrings.get(position));
                if (position == 0) {
                    chooseCoursId = 0;
                } else {
                    chooseCoursId = courses.get(position - 1).id;
                }
            }
        });
        dialogList.show();
    }

    @OnClick(R.id.custom_statment_end)
    public void onClickEnd() {
//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH) + 1;
//        int day = date.get(Calendar.DAY_OF_MONTH);
//        if (!TextUtils.isEmpty(customStatmentEnd.getContent())) {
//            try {
//                String[] ss = customStatmentEnd.getContent().split("-");
//                year = Integer.parseInt(ss[0]);
//                month = Integer.parseInt(ss[1]);
//                day = Integer.parseInt(ss[2]);
//            } catch (Exception e) {
//
//            }
//        }
//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                String endStr = year + "-" + ++monthOfYear + "-" + dayOfMonth;
//                try {
//                    Date end = DateUtils.formatDateFromYYYYMMDD(endStr);
//                    Date start = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
//                    LogUtil.e(end.getTime() + "   " + start.getTime());
//                    if (end.getTime() - start.getTime() < 0) {
//                        ToastUtils.show(R.drawable.ic_share_fail, "结束日期不能早于开始日期");
//                    } else if ((end.getTime() - start.getTime()) > DateUtils.MONTH_TIME) {
//                        ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
//                    } else
//                        customStatmentEnd.setContent(endStr);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, year, month, day).show();

        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(date -> {
//            customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(date));
            try {
                Date start = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
                LogUtil.e(date.getTime() + "   " + start.getTime());
                if (date.getTime() - start.getTime() < 0) {
                    ToastUtils.show(R.drawable.ic_share_fail, "结束日期不能早于开始日期");
                } else if ((date.getTime() - start.getTime()) > DateUtils.MONTH_TIME) {
                    ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
                } else {
                    pwTime.dismiss();
                    customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(date));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent());
        } catch (Exception e) {

        }

        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);

    }

    @OnClick(R.id.custom_statment_start)
    public void onClickStart() {
//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH);
//        int day = date.get(Calendar.DAY_OF_MONTH);
//        if (!TextUtils.isEmpty(customStatmentStart.getContent())){
//            try {
//                String[] ss = customStatmentStart.getContent().split("-");
//                year = Integer.parseInt(ss[0]);
//                month = Integer.parseInt(ss[1]);
//                day = Integer.parseInt(ss[2]);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                customStatmentStart.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
//
//            }
//        }, year,month,day).show();
        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(date -> {
            try {
                Date end = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent());

                if (date.getTime() - end.getTime() > 0) {
                    ToastUtils.show(R.drawable.ic_share_fail, "开始时间不能晚于结束时间");
                } else if ((end.getTime() - date.getTime()) > DateUtils.MONTH_TIME) {
                    ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
                } else {
                    pwTime.dismiss();
                    customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(date));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
        } catch (Exception e) {

        }

        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
    }

    @OnClick(R.id.custom_statment_gym)
    public void onClickGym() {
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        dialogList.list(gymStrings, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                customStatmentGym.setContent(gymStrings.get(position));
                if (chooseGymId != spinnerBeans.get(position).id) {
                    chooseGymId = spinnerBeans.get(position).id;
                    chooseGymModel = spinnerBeans.get(position).model;
                    if (position == 0) {
                        customStatmentStudent.setVisibility(View.GONE);
                        customStatmentCourse.setVisibility(View.GONE);
                    } else {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("id", Integer.toString(chooseGymId));
                        params.put("model", chooseGymModel);

//                        QcCloudClient.getApi().getApi.qcGetSystemStudent(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(studentResponseObserver);
//                        QcCloudClient.getApi().getApi.qcGetSystemCourses(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(courseResponseObserver);
                        sp = QcCloudClient.getApi().getApi.qcGetServiceDetail(params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<QcServiceDetialResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(QcServiceDetialResponse qcServiceDetialResponse) {
                                        courses = qcServiceDetialResponse.data.service.courses;
                                        courseStrings.clear();
                                        Collections.sort(courses, new CourseComparator());
                                        for (QcCourseResponse.Course studentBean : courses) {
                                            if (TextUtils.isEmpty(studentBean.name))
                                                studentBean.name = "";
                                            courseStrings.add(studentBean.name);
                                        }

                                        courseStrings.add(0, "全部课程");
                                        studentBeans = qcServiceDetialResponse.data.service.users;
                                        studentStrings.clear();
                                        Collections.sort(studentBeans, new StudentComparator());
                                        for (QcStudentBean studentBean : studentBeans) {
                                            if (TextUtils.isEmpty(studentBean.username))
                                                studentStrings.add(studentBean.phone);
                                            else
                                                studentStrings.add(studentBean.username);

                                        }

                                        studentStrings.add(0, "全部学员");
                                    }
                                });
                        customStatmentStudent.setVisibility(View.VISIBLE);
                        customStatmentCourse.setVisibility(View.VISIBLE);
                        customStatmentStudent.setContent("所有学员");
                        customStatmentCourse.setContent("所有课程");
                        chooseUserId = 0;
                        chooseUserId = 0;
                    }
                }
            }
        });
        dialogList.show();
//        new MaterialDialog.Builder(getContext())
//                .title("请选择健身房")
//                .autoDismiss(true)
//                .items(gymStrings.toArray(new String[gymStrings.size()]))
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        LogUtil.e("choose:" + which);
//
//                        customStatmentGym.setContent(text.toString());
//                        chooseGymId = spinnerBeans.get(which).id;
//                        if (which == 0) {
//                            customStatmentStudent.setVisibility(View.GONE);
//                            customStatmentCourse.setVisibility(View.GONE);
//                        } else {
//                            HashMap<String, String> params = new HashMap<String, String>();
//                            params.put("system_id", Integer.toString(chooseGymId));
//
//                            QcCloudClient.getApi().getApi.qcGetSystemStudent(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(studentResponseObserver);
//                            QcCloudClient.getApi().getApi.qcGetSystemCourses(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(courseResponseObserver);
//
//                            customStatmentStudent.setVisibility(View.VISIBLE);
//                            customStatmentCourse.setVisibility(View.VISIBLE);
//                        }
//                    }
//                })
//                .show();
    }

    @OnClick(R.id.custom_statment_student)
    public void onClickStudent() {
//        new MaterialDialog.Builder(getContext())
//                .title("请选择学员")
//                .titleColorRes(R.color.text_grey)
//                .items(studentStrings.toArray(new String[studentStrings.size()]))
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                        customStatmentStudent.setContent(charSequence.toString());
//                        if (i == 0) {
//                            chooseUserId = 0;
//                        } else {
//                            chooseUserId = studentBeans.get(i - 1).id;
//                        }
//                    }
//                }).show();

        if (studentStrings.size() < 1)
            return;
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择学员");
        dialogList.list(studentStrings, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                customStatmentStudent.setContent(studentStrings.get(position));
                if (position == 0) {
                    chooseUserId = 0;
                } else {
                    chooseUserId = Integer.parseInt(studentBeans.get(position - 1).id);
                }
            }
        });
        dialogList.show();

    }

    @OnClick(R.id.custom_statment_generate)
    public void onClickGenerate() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(3,
                        customStatmentStart.getContent(), customStatmentEnd.getContent(), chooseGymModel,
                        chooseGymId, chooseUserId, chooseCoursId, customStatmentStudent.getContent(), customStatmentCourse.getContent()))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        if (sp != null && !sp.isUnsubscribed())
            sp.unsubscribe();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
