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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.CardComparator;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.event.EventChooseCourse;
import com.qingchengfit.fitcoach.fragment.statement.GymChooseCardtplFragmentBuilder;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCardsResponse;
import com.qingchengfit.fitcoach.http.bean.QcSystemCardsResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomSaleFragment extends BaseFragment {

    public static final String TAG = CustomSaleFragment.class.getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.custom_statment_gym) CommonInputView customStatmentGym;
    @BindView(R.id.custom_statment_start) CommonInputView customStatmentStart;
    @BindView(R.id.custom_statment_end) CommonInputView customStatmentEnd;
    @BindView(R.id.custom_statment_course) CommonInputView customStatmentCourse;
    @BindView(R.id.rootview) LinearLayout rootview;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    //    @BindView(R.id.custom_statment_student)
    //    CommonInputView customStatmentStudent;

    private Calendar date;
    private List<SpinnerBean> spinnerBeans = new ArrayList<>();
    private List<String> gymStrings = new ArrayList<>();
    private List<String> courseStrings = new ArrayList<>();
    private List<String> studentStrings = new ArrayList<>();
    private List<Integer> cardIntegers = new ArrayList<>();
    private int chooseGymId = 0;
    private String chooseCoursId = "";
    private List<QcSystemCardsResponse.Card> studentBeans;
    private Observer<QcSystemCardsResponse> studentResponseObserver = new Observer<QcSystemCardsResponse>() {
        @Override public void onCompleted() {
        }

        @Override public void onError(Throwable e) {
        }

        @Override public void onNext(QcSystemCardsResponse qcSystemCardsResponse) {
            //            studentBeans = qcSystemCardsResponse.data.card_tpls;
            //            studentStrings.clear();
            //            Collections.sort(studentBeans, new CardComparator());
            //            for (QcSystemCardsResponse.Card studentBean : studentBeans) {
            //                studentStrings.add(studentBean.name);
            //            }
            //            studentStrings.add(0, "全部会员卡");

        }
    };
    private List<QcCardsResponse.Card> cards = new ArrayList<>();
    private TimeDialogWindow pwTime;
    private Subscription cardSp;
    private Unbinder unbinder;

    public CustomSaleFragment() {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = Calendar.getInstance();
        //获取用户拥有系统信息
        //        QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.newThread())
        //                .subscribe(qcCoachSystemResponse -> {
        //                    List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
        //                    spinnerBeans.add(new SpinnerBean("", "全部健身房", 0,""));
        //
        //                    for (int i = 0; i < systems.size(); i++) {
        //                        QcCoachSystem system = systems.get(i);
        //                        spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id,""));
        //                        gymStrings.add(system.name);
        //                    }
        //                    gymStrings.add(0, "全部健身房");
        //                }, throwable -> {
        //                }, () -> {
        //                });

        cardSp = QcCloudClient.getApi().getApi.qcGetSaleCard(App.coachid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcCardsResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcCardsResponse qcCardsResponse) {
                    cards.clear();
                    for (QcCardsResponse.CardSystem sys : qcCardsResponse.data.systems) {
                        for (QcCardsResponse.Card card : sys.card_tpls) {
                            card.system_id = sys.system_id;
                        }
                        cards.addAll(sys.card_tpls);
                    }
                    Collections.sort(cards, new CardComparator());
                    courseStrings.add("全部会员卡");
                    for (QcCardsResponse.Card c : cards) {
                        courseStrings.add(c.name);
                        cardIntegers.add(c.system_id);
                    }
                }
            });
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_sale, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("自定义销售报表");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        initView();
        RxBusAdd(EventChooseCourse.class)
            .subscribe(new Action1<EventChooseCourse>() {
                @Override public void call(EventChooseCourse eventChooseCourse) {
                    chooseCoursId = eventChooseCourse.courseId;
                    customStatmentCourse.setContent(eventChooseCourse.courseName);
                }
            });
        return view;
    }

    private void initView() {
        customStatmentCourse.setContent("所有会员卡");
        customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        customStatmentGym.setContent("所有健身房");

        //        customStatmentStudent.setContent("所有学员");
    }

    @OnClick(R.id.custom_statment_course) public void onClickCourse() {
        customStatmentCourse.setClickable(false);
        if (getActivity() instanceof FragActivity) {
            new GymChooseCardtplFragmentBuilder(((FragActivity) getActivity()).getCoachService().getId()+""
                ,((FragActivity) getActivity()).getCoachService().getModel()
                ).build().show(getFragmentManager(), "");
        }
        customStatmentCourse.setClickable(true);

        //DialogList dialogList = new DialogList(getContext());
        //dialogList.title("请选择会员卡");
        //dialogList.list(courseStrings, new AdapterView.OnItemClickListener() {
        //    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        dialogList.dismiss();
        //        customStatmentCourse.setContent(courseStrings.get(position));
        //        if (position == 0) {
        //            chooseCoursId = 0;
        //        } else {
        //            chooseCoursId = cards.get(position - 1).id;
        //            chooseGymId = cards.get(position - 1).system_id;
        //        }
        //    }
        //});
        //dialogList.show();
    }

    @OnClick(R.id.custom_statment_end) public void onClickEnd() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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
        //        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
        //            @Override
        //            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //                customStatmentEnd.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
        //            }
        //        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.custom_statment_start) public void onClickStart() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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

        //        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
        //            @Override
        //            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //                customStatmentStart.setContent(year + "-" + ++monthOfYear + "-" + dayOfMonth);
        //            }
        //        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.custom_statment_gym) public void onClickGym() {
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        dialogList.list(gymStrings, new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                customStatmentGym.setContent(gymStrings.get(position));
                if (chooseGymId != spinnerBeans.get(position).id) {
                    chooseGymId = spinnerBeans.get(position).id;
                    if (position == 0) {

                        customStatmentCourse.setVisibility(View.GONE);
                    } else {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("system_id", Integer.toString(chooseGymId));

                        customStatmentCourse.setVisibility(View.VISIBLE);
                        customStatmentCourse.setContent("所有会员卡");
                        chooseCoursId = "";

                    }
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

    @OnClick(R.id.custom_statment_generate) public void onClickGenerate() {
        getFragmentManager().beginTransaction()
            .add(R.id.web_frag_layout,
                SaleDetailFragment.newInstance(3, customStatmentStart.getContent(), customStatmentEnd.getContent(), chooseGymId,
                    chooseCoursId, customStatmentCourse.getContent()))
            .addToBackStack(null)
            .commit();
    }

    @Override public void onDestroyView() {
        if (cardSp != null && !cardSp.isUnsubscribed()) cardSp.unsubscribe();
        super.onDestroyView();
        unbinder.unbind();
    }
}
