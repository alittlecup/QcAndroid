package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.saasbase.report.bean.CourseReportDetail;
import cn.qingchengfit.saasbase.report.bean.CourseReportOrder;
import cn.qingchengfit.saasbase.report.bean.CourseReportSchedule;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.RatingBarVectorFix;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.statement.item.CourseReverseDetailItem;
import com.qingchengfit.fitcoach.fragment.statement.presenter.CourseReversePresenter;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * Created by fb on 2017/5/11.
 */

@FragmentWithArgs public class CourseReverseFragment extends BaseFragment
    implements CourseReversePresenter.CourseView {

  @Arg String sechduleId;

	ImageView imageReportDetailHead;
	TextView textReportDetailName;
	TextView textReportDetailStudioName;
	ImageView imageReportDetailCoachHead;
	TextView imageReportDetailCoachName;
	RatingBarVectorFix studentJudgeCoachStar;
	TextView textReportReverseNumber;
	TextView textReportReverseIncome;
	TextView textReportReverseFact;
	RecyclerView recyclerReportDetail;
	Toolbar toolbar;

  @Inject CourseReversePresenter presenter;
	TextView toolbarTitle;
  private CommonFlexAdapter adapter;
  private List<CourseReverseDetailItem> itemList = new ArrayList<>();

  @Override public String getFragmentName() {
    return CourseReverseFragment.class.getName();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
    imageReportDetailHead = (ImageView) view.findViewById(R.id.image_report_detail_head);
    textReportDetailName = (TextView) view.findViewById(R.id.text_report_detail_name);
    textReportDetailStudioName = (TextView) view.findViewById(R.id.text_report_detail_studio_name);
    imageReportDetailCoachHead = (ImageView) view.findViewById(R.id.image_report_detail_coach_head);
    imageReportDetailCoachName = (TextView) view.findViewById(R.id.image_report_detail_coach_name);
    studentJudgeCoachStar = (RatingBarVectorFix) view.findViewById(R.id.student_judge_coach_star);
    textReportReverseNumber = (TextView) view.findViewById(R.id.text_report_reverse_number);
    textReportReverseIncome = (TextView) view.findViewById(R.id.text_report_reverse_income);
    textReportReverseFact = (TextView) view.findViewById(R.id.text_report_reverse_fact);
    recyclerReportDetail = (RecyclerView) view.findViewById(R.id.recycler_report_detail);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

    delegatePresenter(presenter, this);
    setToolbar();

    presenter.qcGetCourseReverse(sechduleId);
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerReportDetail.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerReportDetail.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withBottomEdge(true).withOffset(5));
    recyclerReportDetail.setAdapter(adapter);
    return view;
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText("课程预约");
  }

  @Override protected boolean isfitSystemPadding() {
    return false;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onGetSuccessed(CourseReportDetail courseReportDetail) {
    itemList.clear();
    for (CourseReportOrder order : courseReportDetail.orders) {
      itemList.add(new CourseReverseDetailItem(order));
    }
    adapter.updateDataSet(itemList);

    CourseReportSchedule schedule = courseReportDetail.schedule;
    Glide.with(getContext())
        .load(PhotoUtils.getSmall(schedule.course.getPhoto()))
        .asBitmap()
        .into(new CircleImgWrapper(imageReportDetailHead, getContext()));

    textReportDetailName.setText(schedule.course.getName());
    textReportDetailStudioName.setText(
        DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(schedule.start))
            + "     "
            + schedule.shop.name);
    textReportReverseNumber.setText(getSb(schedule.count + "人次"));
    textReportReverseNumber.setTextSize(15);
    textReportReverseIncome.setTextSize(15);
    if (formatView(courseReportDetail)) {
      textReportReverseIncome.setText("－－");
      textReportReverseFact.setText("－－");
    } else {
      setInCome(schedule);
      textReportReverseFact.setText("¥" + StringUtils.getFloatDot2(schedule.total_real_price));
    }
    Glide.with(getContext())
        .load(PhotoUtils.getSmall(schedule.teacher.avatar))
        .asBitmap()
        .into(new CircleImgWrapper(imageReportDetailCoachHead, getContext()));
    imageReportDetailCoachName.setText(schedule.teacher.username);
    studentJudgeCoachStar.setRating(schedule.teacher.score);
  }

  private boolean formatView(CourseReportDetail detail) {
    boolean isTime = true;
    for (CourseReportOrder order : detail.orders) {
      if (order.card != null && order.card.card_type == 3) {
      } else {
        isTime = false;
        break;
      }
    }
    return isTime;
  }

  private void setInCome(CourseReportSchedule schedule) {
    if (schedule.total_account != 0) {
      if (schedule.total_times != 0) {
        textReportReverseIncome.setText(getSb(
            StringUtils.getFloatDot2(schedule.total_account) + "元+" + CmStringUtils.getMaybeInt(
                schedule.total_times) + "次"));
      } else {
        textReportReverseIncome.setText(getSb(StringUtils.getFloatDot2(schedule.total_account) + "元"));
      }
    } else if (schedule.total_times != 0) {
      textReportReverseIncome.setText(getSb(CmStringUtils.getMaybeInt(schedule.total_times) + "次"));
    }
  }

  private SpannableString getSb(String str) {
    SpannableString sb = new SpannableString(str);
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(sb);
    while (matcher.find()) {
      sb.setSpan(new AbsoluteSizeSpan(20, true), matcher.start(), matcher.end(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    return sb;
  }

  @Override public void onGetFailed(String msg) {
    ToastUtils.show(msg);
  }
}
