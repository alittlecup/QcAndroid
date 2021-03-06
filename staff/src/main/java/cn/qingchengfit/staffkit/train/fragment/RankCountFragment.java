package cn.qingchengfit.staffkit.train.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.AttendanceBean;
import cn.qingchengfit.staffkit.train.model.RankTypeBean;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CircleTextView;

/**
 * Created by fb on 2017/3/22.
 */

public class RankCountFragment extends BaseFragment {

	LinearLayout llEmptyRank;
	LinearLayout llAccountRank;
	CircleTextView cirAttendance;
	TextView textAttendanceRank;
	CircleTextView cirSignal;
	TextView textSignalRank;
	CircleTextView cirGroup;
	TextView textGroupRank;
	CircleTextView cirPrivate;
	TextView textPrivateRank;
	LinearLayout llRankFirstLine;
	LinearLayout llRankSecondLine;
	LinearLayout llRankAttendance;
	LinearLayout llRankSignal;
	LinearLayout llRankGroup;
	LinearLayout llRankPrivate;
	TextView textGroupAttendance;
	TextView textGroupClass;
	TextView textGroupPrivate;

  private boolean isLow;
  private boolean isShowTogether;

  public static RankCountFragment newInstance(AttendanceBean attendanceBean, boolean isShowTogether) {

    Bundle args = new Bundle();
    args.putParcelable("rank", attendanceBean);
    args.putBoolean("isShow", isShowTogether);
    RankCountFragment fragment = new RankCountFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static RankCountFragment newInstance(AttendanceBean attendanceBean) {

    Bundle args = new Bundle();
    args.putParcelable("rank", attendanceBean);
    RankCountFragment fragment = new RankCountFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_sign_up_count, container, false);
    llEmptyRank = (LinearLayout) view.findViewById(R.id.sign_up_empty_rank);
    llAccountRank = (LinearLayout) view.findViewById(R.id.ll_account_sign_up);
    cirAttendance = (CircleTextView) view.findViewById(R.id.circle_text_attendance);
    textAttendanceRank = (TextView) view.findViewById(R.id.text_attendance_rank);
    cirSignal = (CircleTextView) view.findViewById(R.id.circle_text_signal);
    textSignalRank = (TextView) view.findViewById(R.id.text_signal_rank);
    cirGroup = (CircleTextView) view.findViewById(R.id.circle_text_group);
    textGroupRank = (TextView) view.findViewById(R.id.text_group_rank);
    cirPrivate = (CircleTextView) view.findViewById(R.id.circle_text_private);
    textPrivateRank = (TextView) view.findViewById(R.id.text_private_rank);
    llRankFirstLine = (LinearLayout) view.findViewById(R.id.ll_rank_first_line);
    llRankSecondLine = (LinearLayout) view.findViewById(R.id.ll_rank_second_line);
    llRankAttendance = (LinearLayout) view.findViewById(R.id.ll_rank_attendance);
    llRankSignal = (LinearLayout) view.findViewById(R.id.ll_rank_signal);
    llRankGroup = (LinearLayout) view.findViewById(R.id.ll_rank_group);
    llRankPrivate = (LinearLayout) view.findViewById(R.id.ll_rank_private);
    textGroupAttendance = (TextView) view.findViewById(R.id.text_group_attendance);
    textGroupClass = (TextView) view.findViewById(R.id.text_group_class);
    textGroupPrivate = (TextView) view.findViewById(R.id.text_group_private);

    if (getArguments().containsKey("isShow")) {
      isShowTogether = getArguments().getBoolean("isShow");
    }
    setViewData((AttendanceBean) getArguments().getParcelable("rank"));
    return view;
  }

  private void setViewData(AttendanceBean attendanceBean) {
    llRankFirstLine.removeAllViews();
    llRankSecondLine.removeAllViews();

    if (attendanceBean == null) {
      llEmptyRank.setVisibility(View.VISIBLE);
      llAccountRank.setVisibility(View.GONE);
    } else {
      llEmptyRank.setVisibility(View.GONE);
      llAccountRank.setVisibility(View.VISIBLE);

      if (isShowTogether){
        textGroupAttendance.setVisibility(View.VISIBLE);
        textGroupClass.setVisibility(View.VISIBLE);
        textGroupPrivate.setVisibility(View.VISIBLE);
        textGroupAttendance.setText(getString(R.string.rank_type_group_attendance, attendanceBean.days.together_count));
        textGroupClass.setText(getString(R.string.rank_type_group_attendance, attendanceBean.group_course.together_count));
        textGroupPrivate.setText(getString(R.string.rank_type_group_attendance, attendanceBean.private_course.together_count));
      }else{
        textGroupAttendance.setVisibility(View.GONE);
        textGroupClass.setVisibility(View.GONE);
        textGroupPrivate.setVisibility(View.GONE);
      }

      setDataWithView(cirAttendance, textAttendanceRank, attendanceBean.days, llRankAttendance);

      setDataWithView(cirGroup, textGroupRank, attendanceBean.group_course, llRankGroup);

      setDataWithView(cirPrivate, textPrivateRank, attendanceBean.private_course, llRankPrivate);

      setDataWithView(cirSignal, textSignalRank, attendanceBean.checkin, llRankSignal);

      initLinearLayout();
    }
  }

  private void setDataWithView(CircleTextView circleView, TextView tv, RankTypeBean bean,
      ViewGroup viewGroup) {
    if (bean != null) {
      circleView.setText(String.valueOf(bean.count));
      tv.setText(getString(R.string.sign_up_detail_rank, bean.rank_country, bean.rank_gym));
      if (llRankFirstLine.getChildCount() < 2) {
        llRankFirstLine.addView(viewGroup);
      } else {
        llRankSecondLine.addView(viewGroup);
      }
    } else {
      circleView.setVisibility(View.GONE);
      tv.setVisibility(View.GONE);
      isLow = true;
    }
  }

  private void initLinearLayout() {
    if (isLow) {
      llAccountRank.removeAllViews();
      if (llRankFirstLine.getChildCount() == 2) {
        llAccountRank.addView(llRankSecondLine);
        llAccountRank.addView(llRankFirstLine);
      } else {
        llAccountRank.addView(llRankFirstLine);
        llAccountRank.addView(llRankSecondLine);
      }
    }
  }

  @Override public String getFragmentName() {
    return RankCountFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
