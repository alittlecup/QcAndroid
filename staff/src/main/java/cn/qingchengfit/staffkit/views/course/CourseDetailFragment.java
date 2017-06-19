package cn.qingchengfit.staffkit.views.course;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.model.responese.CourseType;
import cn.qingchengfit.model.responese.TeacherImpression;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.usecase.bean.CourseDetailTeacher;
import cn.qingchengfit.staffkit.views.QRActivity;
import cn.qingchengfit.staffkit.views.adapter.CourseTeacherAdapter;
import cn.qingchengfit.staffkit.views.adapter.ViewPaperEndlessAdapter;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.custom.GalleryPhotoViewDialog;
import cn.qingchengfit.staffkit.views.custom.RatingBarVectorFix;
import cn.qingchengfit.staffkit.views.custom.ScaleWidthWrapper;
import cn.qingchengfit.staffkit.views.custom.TouchyWebView;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import co.hkm.soltag.TagContainerLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class CourseDetailFragment extends BaseFragment implements CourseDetailPresenter.CourseDetailView, ViewPager.OnPageChangeListener {

    public static final int RESULT_DEL = 0;

    @Inject GymWrapper gymWrapper;
    @BindView(R.id.student_judge_coach_score) TextView studentJudgeCoachScore;
    @BindView(R.id.student_judge_coach_star) RatingBarVectorFix studentJudgeCoachStar;
    @BindView(R.id.student_judge_course_score) TextView studentJudgeCourseScore;
    @BindView(R.id.student_judge_course_star) RatingBarVectorFix studentJudgeCourseStar;
    @BindView(R.id.server_score) TextView serverScore;
    @BindView(R.id.server_rate) RatingBarVectorFix serverRate;
    @BindView(R.id.course_impression) TagContainerLayout courseImpression;

    @BindView(R.id.jacket_vp) ViewPager jacketVp;
    @BindView(R.id.desc_html) TextView descHtml;
    @BindView(R.id.no_impression) TextView noImpression;
    @BindView(R.id.course_teacher_rv) RecyclerView courseTeacherRv;

    @Inject CourseDetailPresenter mPresenter;
    @BindView(R.id.no_jacket) TextView noJacket;
    @BindView(R.id.comments_detail) FrameLayout commentsDetail;
    @BindView(R.id.go_to_scan) TextView goToScan;
    @BindView(R.id.web_desc) TouchyWebView webDesc;
    @BindView(R.id.no_teacher) TextView noTeacher;
    @BindView(R.id.srl) SwipeRefreshLayout srl;
    @BindView(R.id.splash_indicator) CircleIndicator splashIndicator;
    boolean isJumped = false;
    @BindView(R.id.edit_jacket) TextView editJacket;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private CourseType mCourseDetail;
    private ViewPaperEndlessAdapter viewpageradapter;

    public static CourseDetailFragment newInstance(CourseType courseDetail) {
        Bundle args = new Bundle();
        args.putParcelable("c", courseDetail);
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseDetail = getArguments().getParcelable("c");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(mPresenter, this);
        initView(mCourseDetail);
        setJacket(mCourseDetail.getPhotos());
        setTeachers(mCourseDetail.getTeachers());
        setCourseDescripe(mCourseDetail.getDescription());
        setImpress(mCourseDetail.getImpressions());
        mPresenter.queryDetail(mCourseDetail.getId());
        isJumped = false;
        webDesc.getSettings().setUseWideViewPort(true);
        webDesc.getSettings().setLoadWithOverviewMode(true);
        webDesc.setHorizontalScrollBarEnabled(false);
        editJacket.setCompoundDrawables(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit), null, null, null);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.queryDetail(mCourseDetail.getId());
            }
        });
        //        setScore(mCourseDetail.getTeacher_score(),mCourseDetail.getCourse_score(),mCourseDetail.getService_score());
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("课程种类详情");
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                ArrayList<String> d = new ArrayList<>();
                d.add("删除该课程种类");
                BottomSheetListDialogFragment.start(CourseDetailFragment.this, RESULT_DEL, d);
                return true;
            }
        });
    }

    private void initView(CourseType courseDetail) {
        getFragmentManager().beginTransaction().replace(R.id.course_info, CourseBaseInfoShowFragment.newInstance(courseDetail)).commit();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_flow, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        ArrayList<String> d = new ArrayList<>();
        d.add("删除该课程种类");
        BottomSheetListDialogFragment.start(this, RESULT_DEL, d);
        return true;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_DEL://删除课程
                    mPresenter.judgeDel(mCourseDetail, mCourseDetail.getShops() == null ? 1 : mCourseDetail.getShops().size());
                    break;
                default:
                    break;
            }
        }
    }

    @Override public void setCourseInfo(CourseType course) {
        srl.setRefreshing(false);
        mCourseDetail = course;
        initView(course);
    }

    @Override public void setJacket(final List<String> photos) {
        List<View> data = new ArrayList<>();
        if (photos.size() == 0) {
            noJacket.setVisibility(View.VISIBLE);
        } else {
            noJacket.setVisibility(View.GONE);
        }
        for (int i = 0; i < photos.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_vp_image, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            Glide.with(getContext())
                .load(photos.get(i))
                .asBitmap()
                .into(new ScaleWidthWrapper(imageView, MeasureUtils.getScreenWidth(getResources())));
            final int curPos = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    GalleryPhotoViewDialog dialog = new GalleryPhotoViewDialog(getContext());
                    dialog.setImage(photos);
                    dialog.setSelected(curPos);
                    dialog.show();
                }
            });
            data.add(view);
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_vp_loadmore, null);
        data.add(view);
        viewpageradapter = new ViewPaperEndlessAdapter(data);
        jacketVp.addOnPageChangeListener(this);
        jacketVp.setAdapter(viewpageradapter);
        splashIndicator.setViewPager(jacketVp);
    }

    /**
     * 填写评分
     *
     * @param teacherScore teacher score
     * @param courseScore course score
     * @param serScore server score
     */
    public void setScore(@NonNull float teacherScore, @NonNull float courseScore, @NonNull float serScore) {
        studentJudgeCoachScore.setText(StringUtils.getFloatDot1(teacherScore));
        studentJudgeCourseScore.setText(StringUtils.getFloatDot1(courseScore));
        serverScore.setText(StringUtils.getFloatDot1(serScore));
        studentJudgeCoachStar.setRating(teacherScore);
        studentJudgeCourseStar.setRating(courseScore);
        serverRate.setRating(serScore);
    }

    @Override public void setTeachers(List<CourseDetailTeacher> teachers) {
        if (teachers == null || teachers.size() == 0) {
            noTeacher.setVisibility(View.VISIBLE);
            return;
        }
        noTeacher.setVisibility(View.GONE);
        courseTeacherRv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        courseTeacherRv.setLayoutManager(manager);
        CourseTeacherAdapter adapter = new CourseTeacherAdapter(teachers);
        courseTeacherRv.setAdapter(adapter);
    }

    /**
     * 课程映像
     */
    public void setImpress(List<TeacherImpression> impresssions) {
        if (impresssions == null || impresssions.size() == 0) {
            noImpression.setVisibility(View.VISIBLE);
            courseImpression.setVisibility(View.GONE);
        } else {
            noImpression.setVisibility(View.GONE);
            courseImpression.setVisibility(View.VISIBLE);
            courseImpression.removeAllTags();
            courseImpression.setTags(BusinessUtils.impress2Str(impresssions));
        }
    }

    @Override public void setCourseDescripe(String descripe) {
        if (TextUtils.isEmpty(descripe)) {
            descHtml.setVisibility(View.VISIBLE);
            descHtml.setText("暂无介绍");
        } else {
            descHtml.setVisibility(View.GONE);
            webDesc.loadData("<html>\n"
                + "<head>\n"
                + "\t<title>容器</title>\n"
                + "\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=no\">\n"
                + "\t<style type=\"text/css\">\n"
                + "\t\tbody{overflow-x:hidden;overflow-y:auto;}\n"
                + "\t\t.richTxtCtn{margin:0;padding:0;}\n"
                + "\t\t.richTxtCtn *{max-width:100% !important;}\n"
                + "\t</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\t<div class=\"richTxtCtn\">"
                + descripe
                + "</div></body></html>", "text/html; charset=UTF-8", null);
        }
    }

    @Override public void showDelDialog(String content) {

        new MaterialDialog.Builder(getContext()).title("确定删除该课程类型")
            .content(content)
            .autoDismiss(true)
            .canceledOnTouchOutside(true)
            .positiveText(R.string.common_del)
            .negativeText(R.string.pickerview_cancel)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showLoading();
                    mPresenter.delCourse(App.staffId, mCourseDetail.getId());
                }
            })
            .show();
    }

    @Override public void showDelFailed(String content) {
        showAlert(content);
    }

    @Override public void showDelFailed(@StringRes int content) {
        showAlert(content);
    }

    @Override public void onDelSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onDelfailed(String content) {
        srl.setRefreshing(false);
        hideLoading();
        ToastUtils.show(content);
    }

    /**
     * 封面管理
     */
    @OnClick(R.id.edit_jacket) public void onJacketVp() {
        if (!mPresenter.hasAllEditPermission(mCourseDetail)) return;
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(),
                JacketManagerFragment.newInstance(mCourseDetail.getPhotos(), mCourseDetail.getId(), !mCourseDetail.isRandom_show_photos()))
            .addToBackStack(getFragmentName())
            .commit();
        //            }
        //        }

    }

    /**
     * 查看评价详情
     */
    @OnClick(R.id.comments_detail) public void checkComments() {
        if (gymWrapper.inBrand()) {
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), ShopCommentsFragment.newInstance(mCourseDetail.getId()))
                .addToBackStack(getFragmentName())
                .commit();
        } else {
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), CoachCommentListFragment.newInstance(mCourseDetail.getId()))
                .addToBackStack(getFragmentName())
                .commit();
        }
    }

    /**
     * 编辑简介 跳转二维码扫码
     */
    @OnClick(R.id.go_to_scan) public void gotoScan() {
        if (!mPresenter.hasAllEditPermission(mCourseDetail)) return;
        new RxPermissions(getActivity()).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
                if (aBoolean) {
                    Intent toScan = new Intent(getActivity(), QRActivity.class);
                    toScan.putExtra(QRActivity.LINK_URL, mCourseDetail.getEdit_url());
                    startActivity(toScan);
                } else {
                    ToastUtils.show(getString(R.string.please_open_camera));
                }
            }
        });
    }

    /**
     * 编辑基本信息
     */
    @OnClick(R.id.edit_base_info) public void editBaseInfo() {
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), EditCourseFragment.newInstance(mCourseDetail))
            .addToBackStack(getFragmentName())
            .commit();
    }

    @Override public String getFragmentName() {
        return CourseDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (position == viewpageradapter.getCount() - 2 && positionOffset > 0.4 && !isJumped) {
            isJumped = true;
            jacketVp.setCurrentItem(0);
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), CourseImagesFragment.newInstance(mCourseDetail.getId()))
                .addToBackStack(getFragmentName())
                .commit();
        }
    }

    @Override public void onPageSelected(int position) {

    }

    @Override public void onPageScrollStateChanged(int state) {

    }
}
