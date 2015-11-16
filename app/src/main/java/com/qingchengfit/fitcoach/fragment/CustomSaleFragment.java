package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcSystemCardsResponse;

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
public class CustomSaleFragment extends Fragment {

    public static final String TAG = CustomSaleFragment.class.getName();
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
    @Bind(R.id.rootview)
    LinearLayout rootview;
//    @Bind(R.id.custom_statment_student)
//    CommonInputView customStatmentStudent;

    private Calendar date;
    private List<SpinnerBean> spinnerBeans = new ArrayList<>();
    private List<String> gymStrings = new ArrayList<>();
    private List<String> courseStrings = new ArrayList<>();
    private List<String> studentStrings = new ArrayList<>();
    private int chooseGymId = 0;
    private int chooseUserId = 0;
    private int chooseCoursId = 0;
    private List<QcSystemCardsResponse.Card> studentBeans;
    private Observer<QcSystemCardsResponse> studentResponseObserver = new Observer<QcSystemCardsResponse>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(QcSystemCardsResponse qcSystemCardsResponse) {
            studentBeans = qcSystemCardsResponse.data.card_tpls;
            studentStrings.clear();
            studentStrings.add("全部学员");
            for (QcSystemCardsResponse.Card studentBean : studentBeans) {
                studentStrings.add(studentBean.name);
            }

        }
    };
    private List<QcSystemCardsResponse.Card> courses;
    private Observer<QcSystemCardsResponse> courseResponseObserver = new Observer<QcSystemCardsResponse>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(QcSystemCardsResponse qcSystemCardsResponse) {
            courses = qcSystemCardsResponse.data.card_tpls;
            courseStrings.clear();
            courseStrings.add("全部会员卡");
            for (QcSystemCardsResponse.Card studentBean : courses) {
                courseStrings.add(studentBean.name);
            }

        }
    };
    private TimeDialogWindow pwTime;

    public CustomSaleFragment() {

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
                }, throwable -> {
                }, () -> {
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_sale, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("自定义销售报表");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        initView();

        return view;
    }

    private void initView() {
        customStatmentCourse.setContent("所有会员卡");
        customStatmentStart.setContent(DateUtils.getServerDateDay(new Date()));
        customStatmentEnd.setContent(DateUtils.getServerDateDay(new Date()));
        customStatmentGym.setContent("所有健身房");
//        customStatmentStudent.setContent("所有学员");
    }

    @OnClick(R.id.custom_statment_course)
    public void onClickCourse() {
//        new MaterialDialog.Builder(getContext())
//                .title("请选择会员卡")
//                .items(courseStrings.toArray(new String[courseStrings.size()]))
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                        customStatmentCourse.setContent(charSequence.toString());
//                        if (i == 0) {
//                            chooseCoursId = 0;
//                        } else {
//                            chooseCoursId = courses.get(i - 1).id;
//                        }
//                    }
//                }).show();
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择会员卡");
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
        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(date -> {
//            customStatmentStart.setContent(DateUtils.getServerDateDay(date));
            try {
                Date start = DateUtils.formatDateFromString(customStatmentStart.getContent());
                LogUtil.e(date.getTime() + "   " + start.getTime());
                if (date.getTime() - start.getTime() < 0) {
                    ToastUtils.show(R.drawable.ic_share_fail, "结束日期不能早于开始日期");
                } else if ((date.getTime() - start.getTime()) > DateUtils.MONTH_TIME) {
                    ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
                } else {
                    pwTime.dismiss();
                    customStatmentEnd.setContent(DateUtils.getServerDateDay(date));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromString(customStatmentEnd.getContent());
        } catch (Exception e) {

        }

        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                customStatmentEnd.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
//            }
//        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.custom_statment_start)
    public void onClickStart() {
        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(date -> {
            customStatmentStart.setContent(DateUtils.getServerDateDay(date));
            pwTime.dismiss();
        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromString(customStatmentStart.getContent());
        } catch (Exception e) {

        }

        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);

//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                customStatmentStart.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
//            }
//        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
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
                chooseGymId = spinnerBeans.get(position).id;
                if (position == 0) {

                    customStatmentCourse.setVisibility(View.GONE);
                } else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("system_id", Integer.toString(chooseGymId));

                    QcCloudClient.getApi().getApi.qcGetSystemCard(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(courseResponseObserver);


                    customStatmentCourse.setVisibility(View.VISIBLE);
                }
            }
        });
        dialogList.show();
//        new MaterialDialog.Builder(getContext())
//                .title("请选择健身房")
//                .items(gymStrings.toArray(new String[gymStrings.size()]))
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        LogUtil.e("choose:" + which);
//                        customStatmentGym.setContent(text.toString());
//                        chooseGymId = spinnerBeans.get(which).id;
//                        if (which == 0) {
//
//                            customStatmentCourse.setVisibility(View.GONE);
//                        } else {
//                            HashMap<String, String> params = new HashMap<String, String>();
//                            params.put("system_id", Integer.toString(chooseGymId));
//
//                            QcCloudClient.getApi().getApi.qcGetSystemCard(App.coachid, params).subscribeOn(Schedulers.io()).subscribe(courseResponseObserver);
//
//
//                            customStatmentCourse.setVisibility(View.VISIBLE);
//                        }
//                    }
//                })
//                .show();
    }

//    @OnClick(R.id.custom_statment_student)
//    public void onClickStudent() {
//        new MaterialDialog.Builder(getContext())
//                .title("请选择学员")
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
//
//    }

    @OnClick(R.id.custom_statment_generate)
    public void onClickGenerate() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, SaleDetailFragment.newInstance(3,
                        customStatmentStart.getContent(), customStatmentEnd.getContent(),
                        chooseGymId, chooseCoursId))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
