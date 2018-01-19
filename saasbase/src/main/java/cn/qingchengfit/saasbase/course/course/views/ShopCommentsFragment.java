package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.CommentShop;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.items.ShopCommentItem;
import cn.qingchengfit.saasbase.course.course.presenters.ShopCommentPresenter;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.RatingBarVectorFix;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "course", path = "/shop/comment/") public class ShopCommentsFragment
  extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener, ShopCommentPresenter.ShopCommentView {

  @BindView(R2.id.recyclerview) RecyclerView recyclerview;
  @BindView(R2.id.student_judge_coach_score) TextView studentJudgeCoachScore;
  @BindView(R2.id.student_judge_coach_star) RatingBarVectorFix studentJudgeCoachStar;
  @BindView(R2.id.student_judge_course_score) TextView studentJudgeCourseScore;
  @BindView(R2.id.student_judge_course_star) RatingBarVectorFix studentJudgeCourseStar;
  @BindView(R2.id.server_score) TextView serverScore;
  @BindView(R2.id.server_rate) RatingBarVectorFix serverRate;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;

  @Inject ShopCommentPresenter mPresenter;
  private List<AbstractFlexibleItem> mData = new ArrayList<>();
  private CommonFlexAdapter adapter;
  @Need public String mCourseId;



  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_shop_comment_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(mPresenter, this);
    mPresenter.queryShopComments(mCourseId);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("课程评价");
  }

  public void initRv() {
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerview.setHasFixedSize(true);
    adapter = new CommonFlexAdapter(mData, this);

    adapter.setAnimationOnScrolling(true)
      .setAnimationInitialDelay(300L)
      .setAnimationInterpolator(new DecelerateInterpolator());
    recyclerview.setAdapter(adapter);
  }

  @Override public String getFragmentName() {
    return ShopCommentsFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    if (mData.get(position) instanceof ShopCommentItem) {
      routeTo("/coach/comment/",new cn.qingchengfit.saasbase.course.course.views.CoachCommentListParams()
        .course_id(mCourseId)
        .shop_id(((ShopCommentItem) mData.get(position)).commentShop.id+"")
        .build());
    }
    return false;
  }

  @Override public void onGetComment(List<CommentShop> shops) {
    if (shops != null) {
      mData.clear();
      for (int i = 0; i < shops.size(); i++) {
        mData.add(new ShopCommentItem(shops.get(i)));
      }
      initRv();
    }
  }

  @Override public void onCourse(CourseType courseDetail) {
    toolbarTitile.setText(courseDetail.getName());
    studentJudgeCoachScore.setText(StringUtils.getFloatDot1(courseDetail.getTeacher_score()));
    studentJudgeCoachStar.setRating(courseDetail.getTeacher_score());
    studentJudgeCourseScore.setText(StringUtils.getFloatDot1(courseDetail.getCourse_score()));
    studentJudgeCourseStar.setRating(courseDetail.getCourse_score());
    serverScore.setText(StringUtils.getFloatDot1(courseDetail.getService_score()));
    serverRate.setRating(courseDetail.getService_score());
  }
}