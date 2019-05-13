package cn.qingchengfit.staffkit.views.statement.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.ClassStatmentFilterBean;
import cn.qingchengfit.model.responese.CourseCardForm;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.CourseTypeform;
import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.RxNetWorkEvent;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.course.CourseReverseFragmentBuilder;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.statement.StatmentFilterActivity;
import cn.qingchengfit.staffkit.views.statement.excel.OutExcelFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.TypeUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementDetailFragment extends BaseFragment
    implements StatementDetailView, OnRecycleItemClickListener {
  public static final String TAG = StatementDetailFragment.class.getName();
  public static final int RESULT_FILTER = 4;

  public static final int TYPE_MONTH = 2;
  public static final int TYPE_WEEK = 1;
  public static final int TYPE_DAY = 0;
  ImageButton statementDetailLess;
  ImageButton statementDetailMore;
  TextView statementDetailTime;
  TextView statementDetailFilter;
  RecycleViewWithNoImg recyclerview;
  Button statementDetailChange;
  ViewPager viewpager;
  CircleIndicator indicator;
  CoordinatorLayout scrollRoot;
  @Inject StatementDetailPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView down;
  private StatementDetailAdapter mStatementDetailAdapter;
  private List<QcResponseStatementDetail.StatamentSchedule> statementBeans = new ArrayList<>();
  private List<QcResponseStatementDetail.StatamentSchedule> mAllSchedule = new ArrayList<>();
  //全部报表数据
  //筛选条件
  private ClassStatmentFilterBean mFilter = new ClassStatmentFilterBean();
  private ArrayList<CourseTypeSample> mFilterCourse = new ArrayList<>();
  private ArrayList<Staff> mFilterCoaches = new ArrayList<>();
  private ArrayList<QcStudentBean> mFilterUsers = new ArrayList<>();
  /**
   * 报表参数
   */

  private String start;
  private String end;
  private int mDividerType = 0;
  private Calendar curCalendar;
  private MaterialDialog loadingDialog;
  private String user_name;
  private String course_name;
  private String mTitle;
  private String curModel;
  private String mChooseShopId;
  private Observable<RxNetWorkEvent> mNetOb;
  private ArrayList<Fragment> fs = new ArrayList<>();
  private FragmentAdapter fragmentAdapter;
  private String course_id;
  private String teacher_id;
  private String course_extra;
  private int mDividerDay = 0;
  private Toolbar.OnMenuItemClickListener MenuClick = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      if (TextUtils.isEmpty(mFilter.start)) mFilter.start = start;
      if (TextUtils.isEmpty(mFilter.end)) mFilter.end = end;
      getFragmentManager().beginTransaction()
          .replace(mCallbackActivity.getFragId(),
              OutExcelFragment.newInstance(mFilter, mChooseShopId))
          .addToBackStack(getFragmentName())
          .commit();
      return true;
    }
  };

  public StatementDetailFragment() {

  }

  public static StatementDetailFragment newInstance(int type) {

    Bundle args = new Bundle();
    args.putInt("type", type);
    StatementDetailFragment fragment = new StatementDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static StatementDetailFragment newInstance(int type, String starttime, String endtime,
      String model, int sysId, String teacher_id, String courseId, String course_extra,
      ClassStatmentFilterBean bean) {
    Bundle args = new Bundle();
    args.putInt("type", type);
    args.putString("start", starttime);
    args.putString("end", endtime);
    args.putInt("system", sysId);
    args.putString("course", courseId);
    args.putString("teacher", teacher_id);
    args.putString("model", model);
    args.putString("course_extra", course_extra);
    args.putParcelable("filter", bean);
    StatementDetailFragment fragment = new StatementDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mDividerType = getArguments().getInt("type");
    }
    curCalendar = Calendar.getInstance();
    start = DateUtils.Date2YYYYMMDD(new Date());
    switch (mDividerType) {
      case 0:
        end = start;
        break;
      case 1:
        start = DateUtils.getMondayOfThisWeek(curCalendar.getTime());
        end = DateUtils.getSundayOfThisWeek(curCalendar.getTime());

        break;
      case 2:
        start = DateUtils.getStartDayOfMonth(curCalendar.getTime());
        end = DateUtils.getEndDayOfMonth(curCalendar.getTime());
        break;
      case 3:
        start = getArguments().getString("start");
        end = getArguments().getString("end");
        course_id = getArguments().getString("course");
        teacher_id = getArguments().getString("teacher");
        curModel = getArguments().getString("model");
        course_extra = getArguments().getString("course_extra");
        mDividerDay = DateUtils.interval(start, end);
        mFilter = getArguments().getParcelable("filter");
      default:
        break;
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_statement_detail, container, false);
    statementDetailLess = (ImageButton) view.findViewById(R.id.statement_detail_less);
    statementDetailMore = (ImageButton) view.findViewById(R.id.statement_detail_more);
    statementDetailTime = (TextView) view.findViewById(R.id.statement_detail_time);
    statementDetailFilter = (TextView) view.findViewById(R.id.statement_detail_filter);
    recyclerview = (RecycleViewWithNoImg) view.findViewById(R.id.recyclerview);
    statementDetailChange = (Button) view.findViewById(R.id.statement_detail_change);
    viewpager = (ViewPager) view.findViewById(R.id.viewpager);
    indicator = (CircleIndicator) view.findViewById(R.id.indicator);
    scrollRoot = (CoordinatorLayout) view.findViewById(R.id.scroll_root);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    down = (ImageView) view.findViewById(R.id.down);

    view.findViewById(R.id.statement_detail_less).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickLess();
      }
    });
    view.findViewById(R.id.statement_detail_more).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickMore();
      }
    });
    view.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        btnFilter();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);

    initView();
    mNetOb = RxBus.getBus().register(RxNetWorkEvent.class);
    mNetOb.subscribe(new Action1<RxNetWorkEvent>() {
      @Override public void call(RxNetWorkEvent rxNetWorkEvent) {
        if (rxNetWorkEvent.status == -1) {
          hideLoading();
        }
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    mTitle = getString(R.string.statement_order_all);
    if (gymWrapper.inBrand()) {
      toolbarTitile.setText(mTitle);
      down.setVisibility(View.VISIBLE);
      toolbar.inflateMenu(R.menu.menu_out_excel);
      toolbar.setOnMenuItemClickListener(MenuClick);
    } else {
      mTitle = getString(R.string.statement_order);
      toolbarTitile.setText(mTitle);
      toolbar.inflateMenu(R.menu.menu_out_excel);
      toolbar.setOnMenuItemClickListener(MenuClick);
    }
  }

  private void initView() {
    mStatementDetailAdapter = new StatementDetailAdapter(statementBeans);
    mStatementDetailAdapter.setListener(this);
    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.setAdapter(mStatementDetailAdapter);
    if (mDividerType == 3) {
      statementDetailChange.setVisibility(View.VISIBLE);
      statementDetailChange.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          getActivity().onBackPressed();
        }
      });
      statementDetailMore.setEnabled(
          DateUtils.interval(end, DateUtils.Date2YYYYMMDD(new Date())) > mDividerDay);
    } else {
      statementDetailFilter.setVisibility(View.GONE);
      statementDetailLess.setVisibility(View.VISIBLE);
      statementDetailMore.setVisibility(View.VISIBLE);
      statementDetailMore.setEnabled(false);//初始化右键不可点
    }
    recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        freshDate();
      }
    });
    recyclerview.stopLoading();
    statementDetailLess.setImageResource(R.drawable.triangle_left);
    statementDetailMore.setImageResource(R.drawable.triangle_right);

    fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fs);
    viewpager.setAdapter(fragmentAdapter);
    if (start.equalsIgnoreCase(end)) {
      statementDetailTime.setText(start);
    } else {
      statementDetailTime.setText(start + "至" + end);
    }
    freshDate();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        mChooseShopId = IntentUtils.getIntentString(data, 1);
        if (TextUtils.isEmpty(mChooseShopId)) {
          mCallbackActivity.setBar(new ToolbarBean(mTitle, true, new View.OnClickListener() {
            @Override public void onClick(View v) {
              ChooseGymActivity.start(StatementDetailFragment.this, 1,
                  PermissionServerUtils.COST_REPORT, getString(R.string.choose_gym), mChooseShopId);
            }
          }, R.menu.menu_out_excel, MenuClick));
        } else {
          mCallbackActivity.setBar(new ToolbarBean(IntentUtils.getIntentString(data, 0), true,
              new View.OnClickListener() {
                @Override public void onClick(View v) {
                  ChooseGymActivity.start(StatementDetailFragment.this, 1,
                      PermissionServerUtils.COST_REPORT, getString(R.string.choose_gym),
                      mChooseShopId);
                }
              }, R.menu.menu_out_excel, MenuClick));
        }
        showLoading();
        presenter.queryStatementDetail(start, end, course_id, course_extra, teacher_id,
            mChooseShopId);
      } else if (requestCode == RESULT_FILTER) {
        mFilter = data.getParcelableExtra("filter");
        if (mFilter.start.equalsIgnoreCase(mFilter.end)) {
          statementDetailTime.setText(mFilter.start);
        } else {
          statementDetailTime.setText(mFilter.start + "至" + mFilter.end);
        }

        if (mFilter.start.equals(start) && mFilter.end.equals(end)) {
          statementDetailMore.setEnabled(true);
          statementDetailLess.setEnabled(true);
        } else {
          statementDetailMore.setEnabled(false);
          statementDetailLess.setEnabled(false);
        }
        filter();
      }
    }
  }

  public void freshDate() {
    showLoading();

    presenter.queryStatementDetail(start, end, course_id, course_extra, teacher_id, mChooseShopId);
  }

  @Override public void onDestroyView() {
    hideLoading();
    RxBus.getBus().unregister(RxNetWorkEvent.class.getName(), mNetOb);
    presenter.unattachView();
    if (loadingDialog != null) loadingDialog.dismiss();

    super.onDestroyView();
  }

  public void onClickLess() {
    changeCalendar(-1);
  }

  public void onClickMore() {
    changeCalendar(1);
  }

  /**
   * 增加或者减少 +1 -1
   */
  private void changeCalendar(int symbol) {
    Observable.just(symbol)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map(new Func1<Integer, Integer>() {
          @Override public Integer call(Integer symbol) {
            switch (mDividerType) {
              case 0:
                curCalendar.add(Calendar.DAY_OF_YEAR, symbol);
                start = DateUtils.Date2YYYYMMDD(curCalendar.getTime());
                end = start;
                break;
              case 1:
                curCalendar.add(Calendar.WEEK_OF_YEAR, symbol);
                start = DateUtils.getMondayOfThisWeek(curCalendar.getTime());
                end = DateUtils.getSundayOfThisWeek(curCalendar.getTime());
                break;
              case 2:
                curCalendar.add(Calendar.MONTH, symbol);
                start = DateUtils.getStartDayOfMonth(curCalendar.getTime());
                end = DateUtils.getEndDayOfMonth(curCalendar.getTime());
                break;
              case 3:
                if (symbol < 0) {
                  end = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(start), 1);
                  start = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(end), mDividerDay);
                } else {
                  start = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(end), -1);
                  end = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(start), -mDividerDay);
                }
                break;
              default:
                break;
            }
            getActivity().runOnUiThread(new Runnable() {
              @Override public void run() {
                if (mDividerType < 3) {
                  if (DateUtils.Date2YYYYMMDD(curCalendar.getTime())
                      .equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))) {
                    statementDetailMore.setEnabled(false);
                  } else {
                    statementDetailMore.setEnabled(true);
                  }
                } else {
                  statementDetailMore.setEnabled(
                      DateUtils.interval(end, DateUtils.Date2YYYYMMDD(new Date())) > mDividerDay);
                }
                statementDetailTime.setText(start.equals(end) ? start : start + "至" + end);
              }
            });

            return symbol;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> freshDate(), throwable -> {

        });
  }

  public void btnFilter() {
    Intent toFilter = new Intent(getActivity(), StatmentFilterActivity.class);
    toFilter.putParcelableArrayListExtra("course", mFilterCourse);
    toFilter.putParcelableArrayListExtra("coach", mFilterCoaches);
    toFilter.putParcelableArrayListExtra("user", mFilterUsers);
    if (TextUtils.isEmpty(mFilter.start)) mFilter.start = start;
    if (TextUtils.isEmpty(mFilter.end)) mFilter.end = end;
    toFilter.putExtra("filter", mFilter);
    toFilter.putExtra("start", start);
    toFilter.putExtra("end", end);
    startActivityForResult(toFilter, RESULT_FILTER);
  }

  @Override public void onSuccess(List<QcResponseStatementDetail.StatamentSchedule> list) {
    hideLoading();
    recyclerview.setFresh(false);
    mAllSchedule.clear();
    mAllSchedule.addAll(list);
    //        recyclerview.setNoData(list.size() == 0);

    mFilterUsers.clear();
    mFilterCoaches.clear();
    mFilterCourse.clear();
    mFilter.start = start;
    mFilter.end = end;
    for (int i = 0; i < mAllSchedule.size(); i++) {
      QcResponseStatementDetail.StatamentSchedule statamentSchedule = mAllSchedule.get(i);

      //可供筛选的条件
      if (!mFilterCoaches.contains(statamentSchedule.teacher)) {
        mFilterCoaches.add(statamentSchedule.teacher);
      }
      if (!mFilterCourse.contains(statamentSchedule.course)) {
        mFilterCourse.add(statamentSchedule.course);
      }
      if (statamentSchedule.orders != null) {
        for (int j = 0; j < statamentSchedule.orders.size(); j++) {
          if (!mFilterUsers.contains(statamentSchedule.orders.get(j).user)) {
            mFilterUsers.add(statamentSchedule.orders.get(j).user);
          }
        }
      }
    }

    filter();
  }

  private void filter() {
    statementBeans.clear();

    HashMap<String, Float> caculator = new HashMap<>();
    caculator.put("group_count", 0f);
    caculator.put("private_count", 0f);
    caculator.put("group_server_count", 0f);
    caculator.put("private_server_count", 0f);
    caculator.put("value_ser_num", 0f);
    caculator.put("value_real_income", 0f);
    caculator.put("date_ser_num", 0f);
    caculator.put("date_real_income", 0f);
    caculator.put("time_ser_num", 0f);
    caculator.put("time_real_income", 0f);
    caculator.put("online_ser_num", 0f);
    caculator.put("online_real_income", 0f);
    caculator.put("online_course_income", 0f);
    caculator.put("value_course_income", 0f);
    caculator.put("time_course_income", 0f);
    for (int i = 0; i < mAllSchedule.size(); i++) {
      QcResponseStatementDetail.StatamentSchedule statamentSchedule = mAllSchedule.get(i);

      if ((mFilter.course_type == -1 || mFilter.course_type == 99 || ((mFilter.course_type == -2
          && statamentSchedule.course.is_private()) || (mFilter.course_type == -3
          && !statamentSchedule.course.is_private())))
          && (mFilter.course == null
          || mFilter.course.getId() == null
          || (mFilter.course.getId().equalsIgnoreCase(statamentSchedule.course.getId())))
          && (mFilter.coach == null
          || mFilter.coach.id == null
          || (TextUtils.equals(mFilter.coach.id, statamentSchedule.teacher.id)))
          && (mFilter.student == null || (statamentSchedule.getUsersIds()
          .contains(mFilter.student.getId())))
          && (DateUtils.formatDateFromYYYYMMDD(mFilter.start).getTime()
          <= DateUtils.formatDateFromServer(statamentSchedule.start).getTime())
          && ((DateUtils.formatDateFromYYYYMMDD(mFilter.end).getTime() + DateUtils.DAY_TIME)
          > DateUtils.formatDateFromServer(statamentSchedule.end).getTime())) {
        statementBeans.add(statamentSchedule);

        /**
         * 计算 团课私教 form
         */
        if (statamentSchedule.course.is_private()) {
          caculator.put("private_count", caculator.get("private_count") + 1);
          if (statamentSchedule.orders != null) {
            for (int j = 0; j < statamentSchedule.orders.size(); j++) {
              caculator.put("private_server_count",
                  caculator.get("private_server_count") + statamentSchedule.orders.get(j).count);
            }
          }
        } else {
          caculator.put("group_count", caculator.get("group_count") + 1);
          if (statamentSchedule.orders != null) {
            for (int j = 0; j < statamentSchedule.orders.size(); j++) {
              caculator.put("group_server_count",
                  caculator.get("group_server_count") + statamentSchedule.orders.get(j).count);
            }
          }
        }
        /**
         * 计算卡种类 form
         */
        if (statamentSchedule.orders != null) {
          for (int j = 0; j < statamentSchedule.orders.size(); j++) {
            QcResponseStatementDetail.Order order = statamentSchedule.orders.get(j);

            if (order.channel.equalsIgnoreCase(Configs.CHANNEL_CARD)) {
              /**
               * 会员卡支付
               */
              switch (order.card.card_type) {
                case Configs.CATEGORY_VALUE:
                  caculator.put("value_ser_num", caculator.get("value_ser_num") + order.count);
                  caculator.put("value_real_income",
                      caculator.get("value_real_income") + order.total_real_price);
                  caculator.put("value_course_income",
                      caculator.get("value_course_income") + order.total_price);
                  break;
                case Configs.CATEGORY_TIMES:
                  caculator.put("time_ser_num", caculator.get("time_ser_num") + order.count);
                  caculator.put("time_real_income",
                      caculator.get("time_real_income") + order.total_real_price);

                  break;
                case Configs.CATEGORY_DATE:
                  caculator.put("date_ser_num", caculator.get("date_ser_num") + order.count);
                  //                                caculator.put("time_real_income",caculator.get("time_real_income")+order.total_real_price);
                  break;
              }
            } else {
              /**
               * 在线支付
               */
              caculator.put("online_ser_num", caculator.get("online_ser_num") + order.count);
              caculator.put("online_real_income",
                  caculator.get("online_real_income") + order.total_real_price);
              caculator.put("online_course_income",
                  caculator.get("online_course_income") + order.total_price);
            }
          }
        }
        caculator.put("time_course_income",
            caculator.get("time_course_income") + statamentSchedule.total_times);
      }
    }

    List<CourseTypeform> courseTypeforms = new ArrayList<>();
    CourseTypeform form1 = new CourseTypeform(Configs.TYPE_PRIVATE,
        TypeUtils.Float2Int(caculator.get("private_count")),
        TypeUtils.Float2Int(caculator.get("private_server_count")));
    CourseTypeform form2 =
        new CourseTypeform(Configs.TYPE_GROUP, TypeUtils.Float2Int(caculator.get("group_count")),
            TypeUtils.Float2Int(caculator.get("group_server_count")));
    CourseTypeform form = new CourseTypeform(-1, form1.course_count + form2.course_count,
        form1.server_count + form2.server_count);
    courseTypeforms.add(form);
    courseTypeforms.add(form1);
    courseTypeforms.add(form2);

    List<CourseCardForm> courseCardForms = new ArrayList<>();
    CourseCardForm courseCardForm1 = new CourseCardForm(Configs.CATEGORY_VALUE,
        TypeUtils.Float2Int(caculator.get("value_ser_num")), caculator.get("value_course_income"),
        caculator.get("value_real_income"));
    CourseCardForm courseCardForm2 = new CourseCardForm(Configs.CATEGORY_TIMES,
        TypeUtils.Float2Int(caculator.get("time_ser_num")), caculator.get("time_course_income"),
        caculator.get("time_real_income"));
    CourseCardForm courseCardForm3 = new CourseCardForm(Configs.CATEGORY_DATE,
        TypeUtils.Float2Int(caculator.get("date_ser_num")), 0, 0);
    CourseCardForm courseCardForm4 =
        new CourseCardForm(0, TypeUtils.Float2Int(caculator.get("online_ser_num")),
            caculator.get("online_course_income"), caculator.get("online_real_income"));
    CourseCardForm courseCardForm = new CourseCardForm(-1, courseCardForm1.server_count
        + courseCardForm2.server_count
        + courseCardForm3.server_count
        + courseCardForm4.server_count, 0,
        courseCardForm1.real_income + courseCardForm2.real_income + courseCardForm4.real_income);
    courseCardForms.add(courseCardForm);
    courseCardForms.add(courseCardForm1);
    courseCardForms.add(courseCardForm2);
    courseCardForms.add(courseCardForm3);
    courseCardForms.add(courseCardForm4);

    CourseTypeFormFragment courseTypeFormFragment =
        CourseTypeFormFragment.newInstance(courseTypeforms);
    CourseCardFormFragment courseCardFormFragment =
        CourseCardFormFragment.newInstance(courseCardForms);
    fs.clear();
    fs.add(courseCardFormFragment);
    fs.add(courseTypeFormFragment);

    indicator.setViewPager(viewpager);
    fragmentAdapter.notifyDataSetChanged();
    mStatementDetailAdapter.notifyDataSetChanged();
    recyclerview.setNoData(statementBeans.size() == 0);
  }

  @Override public void onFailed(String e) {
    hideLoading();
    recyclerview.setFresh(false);
    ToastUtils.show(e);
  }

  @Override public String getFragmentName() {
    return StatementDetailFragment.class.getName();
  }

  @Override public void onItemClick(View v, int pos) {
    getFragmentManager().beginTransaction()
        .replace(mCallbackActivity.getFragId(),
            CourseReverseFragmentBuilder.newCourseReverseFragment(statementBeans.get(pos).id))
        .addToBackStack(null)
        .commit();
  }

  /**
   * recycle view
   */
  class StatementDetailVH extends RecyclerView.ViewHolder {
    View itemStatementDetailBottomdivier;
    View itemStatementDetailHeaderdivier;
    TextView itemStatementDetailDay;
    TextView itemStatementDetailMonth;
    LinearLayout itemStatementDetailDate;
    ImageView itemStatementDetailPic;
    TextView itemStatementDetailName;
    TextView itemStatementDetailContent;
    TextView itemUsers;
    TextView textRealIncome;
    ImageView imageCourseType;

    public StatementDetailVH(View view) {
      super(view);
      itemStatementDetailBottomdivier =
          (View) view.findViewById(R.id.item_statement_detail_bottomdivier);
      itemStatementDetailHeaderdivier =
          (View) view.findViewById(R.id.item_statement_detail_headerdivier);
      itemStatementDetailDay = (TextView) view.findViewById(R.id.item_statement_detail_day);
      itemStatementDetailMonth = (TextView) view.findViewById(R.id.item_statement_detail_month);
      itemStatementDetailDate = (LinearLayout) view.findViewById(R.id.item_statement_detail_date);
      itemStatementDetailPic = (ImageView) view.findViewById(R.id.item_statement_detail_pic);
      itemStatementDetailName = (TextView) view.findViewById(R.id.item_statement_detail_name);
      itemStatementDetailContent = (TextView) view.findViewById(R.id.item_statement_time_shop);
      itemUsers = (TextView) view.findViewById(R.id.item_card_cost);
      textRealIncome = (TextView) view.findViewById(R.id.text_real_income);
      imageCourseType = (ImageView) view.findViewById(R.id.image_course_type);
    }
  }

  class StatementDetailAdapter extends RecyclerView.Adapter<StatementDetailVH>
      implements View.OnClickListener {

    private List<QcResponseStatementDetail.StatamentSchedule> datas;
    private String day = "";
    private OnRecycleItemClickListener listener;

    public StatementDetailAdapter(List<QcResponseStatementDetail.StatamentSchedule> data) {
      this.datas = data;
    }

    public OnRecycleItemClickListener getListener() {
      return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
      this.listener = listener;
    }

    @Override public StatementDetailVH onCreateViewHolder(ViewGroup parent, int viewType) {
      StatementDetailVH holder = new StatementDetailVH(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_statement_detail, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    }

    @Override public void onBindViewHolder(StatementDetailVH holder, int position) {
      holder.itemView.setTag(position);
      QcResponseStatementDetail.StatamentSchedule bean = datas.get(position);
      Date date = DateUtils.formatDateFromServer(bean.start);
      Date endDate = DateUtils.formatDateFromServer(bean.end);
      String now = DateUtils.Date2MMDD(date);

      if (position == 0 || !now.equalsIgnoreCase(
          DateUtils.Date2MMDD(DateUtils.formatDateFromServer(datas.get(position - 1).start)))) {
        holder.itemStatementDetailHeaderdivier.setVisibility(View.VISIBLE);
        holder.itemStatementDetailDay.setVisibility(View.VISIBLE);
        holder.itemStatementDetailMonth.setVisibility(View.VISIBLE);
      } else {
        holder.itemStatementDetailHeaderdivier.setVisibility(View.INVISIBLE);
        holder.itemStatementDetailDay.setVisibility(View.INVISIBLE);
        holder.itemStatementDetailMonth.setVisibility(View.INVISIBLE);
      }

      if (position == getItemCount() - 1) {
        holder.itemStatementDetailBottomdivier.setVisibility(View.VISIBLE);
      } else {
        holder.itemStatementDetailBottomdivier.setVisibility(View.GONE);
      }

      holder.imageCourseType.setImageResource(
          bean.course.is_private ? R.drawable.ic_course_private_conner
              : R.drawable.ic_course_group_conner);
      holder.itemStatementDetailName.setText(bean.course.getName() + "-" + bean.teacher.username);
      holder.itemStatementDetailContent.setText(DateUtils.getTimeHHMM(date)
          + "-"
          + DateUtils.getTimeHHMM(endDate)
          + "  "
          + bean.shop.name);
      holder.itemStatementDetailDay.setText(now.substring(3, 5));
      holder.itemStatementDetailMonth.setText(now.substring(0, 2) + "月");
      String users = "";
      int count = 0;
      if (bean.orders != null) {
        for (int i = 0; i < bean.orders.size(); i++) {
          //                    if (bean.orders.get(i).count > 1) {
          count += bean.orders.get(i).count;
          users = users.concat(TextUtils.isEmpty(users) ? "" : "、")
              .concat(bean.orders.get(i).user.getUsername());
          //                    } else {
          //                        users = users.concat(TextUtils.isEmpty(users) ? "" : "、").concat(bean.orders.get(i).user.getUsername());
          //                    }
        }
      }
      users = users.concat(String.format(Locale.CHINA, "  %d人次", count));
      if (!bean.course.is_private()) {
        users = String.format(Locale.CHINA, "%d人次", count);
      }
      holder.itemUsers.setText(users);
      Glide.with(getContext())
          .load(PhotoUtils.getSmall(bean.course.getPhoto()))
          .asBitmap()
          .into(new CircleImgWrapper(holder.itemStatementDetailPic, getContext()));
      holder.textRealIncome.setText("¥" + StringUtils.getFloatDot2(bean.total_real_price));
    }

    @Override public int getItemCount() {
      return datas.size();
    }

    @Override public void onClick(View v) {
      if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }
  }
}
