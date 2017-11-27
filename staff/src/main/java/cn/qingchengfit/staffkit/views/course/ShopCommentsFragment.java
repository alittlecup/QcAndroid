package cn.qingchengfit.staffkit.views.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CourseType;
import cn.qingchengfit.model.responese.QcResponseShopComment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ShopCommentItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.RatingBarVectorFix;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/25.
 */
public class ShopCommentsFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, ShopCommentPresenter.ShopCommentView {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @Inject ShopCommentPresenter mPresenter;
    @BindView(R.id.student_judge_coach_score) TextView studentJudgeCoachScore;
    @BindView(R.id.student_judge_coach_star) RatingBarVectorFix studentJudgeCoachStar;
    @BindView(R.id.student_judge_course_score) TextView studentJudgeCourseScore;
    @BindView(R.id.student_judge_course_star) RatingBarVectorFix studentJudgeCourseStar;
    @BindView(R.id.server_score) TextView serverScore;
    @BindView(R.id.server_rate) RatingBarVectorFix serverRate;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private String mCourseId;
    private CommonFlexAdapter adapter;

    public static ShopCommentsFragment newInstance(String courseid) {

        Bundle args = new Bundle();
        args.putString("c", courseid);
        ShopCommentsFragment fragment = new ShopCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseId = getArguments().getString("c");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_comment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(mPresenter, this);
        mPresenter.queryShopComments(mCourseId);
        return view;
    }

    public void initRv() {
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        adapter = new CommonFlexAdapter(mData, this);

        adapter.setAnimationOnScrolling(true)
            .setAnimationInitialDelay(300L)
            .setAnimationInterpolator(new DecelerateInterpolator())
        ;
        recyclerview.setAdapter(adapter);
    }

    @Override public String getFragmentName() {
        return ShopCommentsFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mData.get(position) instanceof ShopCommentItem) {
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(),
                    CoachCommentListFragment.newInstance(mCourseId, ((ShopCommentItem) mData.get(position)).commentShop.id + ""))
                .addToBackStack(getFragmentName())
                .commit();
        }
        return false;
    }

    @Override public void onGetComment(List<QcResponseShopComment.CommentShop> shops) {
        if (shops != null) {
            mData.clear();

            for (int i = 0; i < shops.size(); i++) {

                mData.add(new ShopCommentItem(shops.get(i)));
            }
            initRv();
            //            adapter.notifyDataSetChanged();
        }
    }

    @Override public void onCourse(CourseType courseDetail) {
        mCallbackActivity.setToolbar(courseDetail.getName(), false, null, 0, null);
        studentJudgeCoachScore.setText(StringUtils.getFloatDot1(courseDetail.getTeacher_score()));
        studentJudgeCoachStar.setRating(courseDetail.getTeacher_score());
        studentJudgeCourseScore.setText(StringUtils.getFloatDot1(courseDetail.getCourse_score()));
        studentJudgeCourseStar.setRating(courseDetail.getCourse_score());
        serverScore.setText(StringUtils.getFloatDot1(courseDetail.getService_score()));
        serverRate.setRating(courseDetail.getService_score());
    }
}
