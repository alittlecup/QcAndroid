package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.RatingBarVectorFix;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.statement.item.CourseReverseDetailItem;
import com.qingchengfit.fitcoach.fragment.statement.model.CourseReportDetail;
import com.qingchengfit.fitcoach.fragment.statement.model.CourseReportOrder;
import com.qingchengfit.fitcoach.fragment.statement.model.CourseReportSchedule;
import com.qingchengfit.fitcoach.fragment.statement.presenter.CourseReversePresenter;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * Created by fb on 2017/5/11.
 */

@FragmentWithArgs public class CourseReverseFragment extends BaseFragment implements CourseReversePresenter.CourseView {

    @Arg String sechduleId;

    @BindView(R.id.image_report_detail_head) ImageView imageReportDetailHead;
    @BindView(R.id.text_report_detail_name) TextView textReportDetailName;
    @BindView(R.id.text_report_detail_studio_name) TextView textReportDetailStudioName;
    @BindView(R.id.image_report_detail_coach_head) ImageView imageReportDetailCoachHead;
    @BindView(R.id.image_report_detail_coach_name) TextView imageReportDetailCoachName;
    @BindView(R.id.student_judge_coach_star) RatingBarVectorFix studentJudgeCoachStar;
    @BindView(R.id.text_report_reverse_number) TextView textReportReverseNumber;
    @BindView(R.id.text_report_reverse_income) TextView textReportReverseIncome;
    @BindView(R.id.text_report_reverse_fact) TextView textReportReverseFact;
    @BindView(R.id.recycler_report_detail) RecyclerView recyclerReportDetail;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject CourseReversePresenter presenter;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private CommonFlexAdapter adapter;
    private List<CourseReverseDetailItem> itemList = new ArrayList<>();

    @Override public String getFragmentName() {
        return CourseReverseFragment.class.getName();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        setToolbar();

        presenter.qcGetCourseReverse(sechduleId);
        adapter = new CommonFlexAdapter(itemList, this);
        recyclerReportDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReportDetail.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider_card_list, 5));
        recyclerReportDetail.setAdapter(adapter);
        return view;
    }

    private void setToolbar() {
        initToolbar(toolbar);
        toolbarTitle.setText("课程预约");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onGetSuccessed(CourseReportDetail courseReportDetail) {
        itemList.clear();
        for (CourseReportOrder order : courseReportDetail.orders) {
            itemList.add(new CourseReverseDetailItem(order));
        }
        adapter.notifyDataSetChanged();

        CourseReportSchedule schedule = courseReportDetail.schedule;
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(schedule.course.getPhoto()))
            .asBitmap()
            .into(new CircleImgWrapper(imageReportDetailHead, getContext()));

        textReportDetailName.setText(schedule.course.getName());
        textReportDetailStudioName.setText(
            DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(schedule.start)) + "     " + schedule.shop.name);
        textReportReverseNumber.setText(getSb(schedule.count + "人次"));
        textReportReverseNumber.setTextSize(15);
        textReportReverseIncome.setTextSize(15);
        if (formatView(courseReportDetail)) {
            textReportReverseIncome.setText("－－");
            textReportReverseFact.setText("－－");
        } else {
            setInCome(schedule);
            textReportReverseFact.setText("¥" + schedule.total_real_price);
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
        if (!TextUtils.isEmpty(schedule.total_account)) {
            if (schedule.total_times != 0) {
                textReportReverseIncome.setText(getSb(schedule.total_account + "元+" + schedule.total_times + "次"));
            } else {
                textReportReverseIncome.setText(getSb(schedule.total_account + "元"));
            }
        } else if (schedule.total_times != 0) {
            textReportReverseIncome.setText(getSb(schedule.total_times + "次"));
        }
    }

    private SpannableString getSb(String str) {
        SpannableString sb = new SpannableString(str);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(sb);
        while (matcher.find()) {
            sb.setSpan(new AbsoluteSizeSpan(20, true), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sb;
    }

    @Override public void onGetFailed(String msg) {
        ToastUtils.show(msg);
    }
}
