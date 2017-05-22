package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.statement.CustomStatementView;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.presenter.CustomStatmentPresenter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomStatmentFragment extends BaseFragment implements CustomStatementView {

  public static final String TAG = CustomStatmentFragment.class.getName();

  TimeDialogWindow pwTime;
  @BindView(R.id.rootview) LinearLayout rootview;

  @Inject CustomStatmentPresenter presenter;
  @BindView(R.id.custom_statment_start) CommonInputView customStatmentStart;
  @BindView(R.id.custom_statment_end) CommonInputView customStatmentEnd;
  @BindView(R.id.custom_statment_course) CommonInputView customStatmentCourse;
  @BindView(R.id.custom_statment_generate) Button customStatmentGenerate;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;

  private Calendar date;
  private List<String> gymStrings = new ArrayList<>();
  private List<String> courseStrings = new ArrayList<>();
  private List<String> studentStrings = new ArrayList<>();

  public CustomStatmentFragment() {

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    date = Calendar.getInstance();
    //获取用户拥有系统信息
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_custom_statment, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    initView();
    RxBusAdd(CourseTypeSample.class).subscribe(new Action1<CourseTypeSample>() {
      @Override public void call(CourseTypeSample course) {
        float type = Float.parseFloat(course.getId());

        if (type < 0) {

          if (type == -1) {
            presenter.setCourse_id(null);
            customStatmentCourse.setContent(getString(R.string.all_course));
          } else if (type == -2) {
            presenter.setCourse_id(null);
            presenter.setCourse_extra("all_private");
            customStatmentCourse.setContent(getString(R.string.all_course_private));
          } else if (type == -3) {
            presenter.setCourse_id(null);
            presenter.setCourse_extra("all_public");
            customStatmentCourse.setContent(getString(R.string.all_course_group));
          }
        } else {
          presenter.setCourse_extra(null);
          presenter.setCourse_id(course.getId());
          customStatmentCourse.setContent(course.getName());
        }
      }
    });

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("自定义课程报表");
  }

  private void initView() {
    customStatmentCourse.setContent("所有课程");
    customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    //        customStatmentGym.setContent("选择健身房场馆");
    //        customStatmentStudent.setContent("所有学员");
  }

  @OnClick(R.id.custom_statment_course) public void onClickCourse() {
    CourseChooseDialogFragment fragment = new CourseChooseDialogFragment();
    fragment.show(getFragmentManager(), "");
  }

  @OnClick(R.id.custom_statment_end) public void onClickEnd() {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(1900, 2100);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        try {
          Date start = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
          if (date.getTime() - start.getTime() < 0) {
            ToastUtils.show(R.drawable.ic_share_fail, "结束日期不能早于开始日期");
          } else if ((date.getTime() - start.getTime()) > DateUtils.MONTH_TIME) {
            ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
          } else {
            pwTime.dismiss();
            customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(date));
            presenter.selectEndTime(customStatmentEnd.getContent());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    Date date = new Date();
    try {
      date = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent());
    } catch (Exception e) {

    }

    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
  }

  @OnClick(R.id.custom_statment_start) public void onClickStart() {

    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(1900, 2100);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        try {
          Date end = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent());

          //                    if (date.getTime() - end.getTime() > 0) {
          //                        ToastUtils.show(R.drawable.ic_share_fail, "开始时间不能晚于结束时间");
          //                    } else {
          pwTime.dismiss();
          customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(date));
          presenter.selectStartTime(customStatmentStart.getContent());
          //                    }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    Date date = new Date();
    try {
      date = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
    } catch (Exception e) {

    }

    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
  }

      //@OnClick(R.id.custom_statment_student)
      //public void onClickStudent() {
      //    if (studentStrings.size() < 1)
      //        return;
      //    final DialogList dialogList = new DialogList(getContext());
      //    dialogList.title("请选择学员");
      //    dialogList.list(studentStrings, new AdapterView.OnItemClickListener() {
      //        @Override
      //        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //            dialogList.dismiss();
      //            customStatmentStudent.setContent(studentStrings.get(position));
      //            presenter.selectStudent(position);
      //        }
      //    });
      //    dialogList.show();
      //
      //}

  @OnClick(R.id.custom_statment_generate) public void onClickGenerate() {
    try {
      long start = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent()).getTime();
      long end = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent()).getTime();
      if (start > end) {
        ToastUtils.show(R.drawable.ic_share_fail, "结束日期不能早于开始日期");
        return;
      }
      if (end - start > 30 * DateUtils.DAY_TIME) {
        ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
        return;
      }
    } catch (Exception e) {
      ToastUtils.show(R.drawable.ic_share_fail, "自定义时间错误");
      return;
    }

    presenter.completedCustom(getFragmentManager(), R.id.web_frag_layout);
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @Override public void onGetCars(List<String> cards) {
    courseStrings.clear();
    courseStrings.addAll(cards);
  }

  @Override public void onGetGyms(List<String> gyms) {
    gymStrings.clear();
    gymStrings.add("全部健身房");
    gymStrings.addAll(gyms);
  }

  @Override public void setSelectCard(String card) {
    //        customStatmentCourse.setContent(card);
  }

  @Override public void setSelectGyms(String gyms) {
    //        customStatmentGym.setContent(gyms);
  }

  @Override public void setSelectStudents(String student) {
    //        customStatmentStudent.setContent(student);
  }

  @Override public void onGetStudents(List<String> studentss) {
    studentStrings.clear();
    studentStrings.addAll(studentss);
  }

  @Override public String getFragmentName() {
    return CustomStatmentFragment.class.getName();
  }
}
