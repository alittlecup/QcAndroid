package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.adapter.ViewPaperEndlessAdapter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Trainer;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.adapter.CourseTeacherAdapter;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.bean.TeacherImpression;
import cn.qingchengfit.saasbase.course.course.presenters.CourseDetailPresenter;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.TouchyWebView;
import cn.qingchengfit.widgets.CircleIndicator;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.RatingBarVectorFix;
import co.hkm.soltag.TagContainerLayout;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "course", path = "/detail/") public class CourseDetailFragment
  extends SaasBaseFragment
  implements CourseDetailPresenter.CourseDetailView, ViewPager.OnPageChangeListener {

	TextView studentJudgeCoachScore;
	RatingBarVectorFix studentJudgeCoachStar;
	TextView studentJudgeCourseScore;
	RatingBarVectorFix studentJudgeCourseStar;
	TextView serverScore;
	RatingBarVectorFix serverRate;
	TagContainerLayout courseImpression;
	ViewPager jacketVp;
	TextView descHtml;
	TextView noImpression;
	RecyclerView courseTeacherRv;

	TextView noJacket;
	FrameLayout commentsDetail;
	TextView goToScan;
	TouchyWebView webDesc;
	TextView noTeacher;
	SwipeRefreshLayout srl;
	CircleIndicator splashIndicator;
	TextView editJacket;
	Toolbar toolbar;
	TextView toolbarTitile;
	NestedScrollView scrollView;
  boolean isJumped = false;
  @Inject CourseDetailPresenter mPresenter;
  @Inject GymWrapper gymWrapper;
  @Need public CourseType mCourseDetail;
  private ViewPaperEndlessAdapter viewpageradapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_course_detail, container, false);
    studentJudgeCoachScore = (TextView) view.findViewById(R.id.student_judge_coach_score);
    studentJudgeCoachStar = (RatingBarVectorFix) view.findViewById(R.id.student_judge_coach_star);
    studentJudgeCourseScore = (TextView) view.findViewById(R.id.student_judge_course_score);
    studentJudgeCourseStar = (RatingBarVectorFix) view.findViewById(R.id.student_judge_course_star);
    serverScore = (TextView) view.findViewById(R.id.server_score);
    serverRate = (RatingBarVectorFix) view.findViewById(R.id.server_rate);
    courseImpression = (TagContainerLayout) view.findViewById(R.id.course_impression);
    jacketVp = (ViewPager) view.findViewById(R.id.jacket_vp);
    descHtml = (TextView) view.findViewById(R.id.desc_html);
    noImpression = (TextView) view.findViewById(R.id.no_impression);
    courseTeacherRv = (RecyclerView) view.findViewById(R.id.course_teacher_rv);
    noJacket = (TextView) view.findViewById(R.id.no_jacket);
    commentsDetail = (FrameLayout) view.findViewById(R.id.comments_detail);
    goToScan = (TextView) view.findViewById(R.id.go_to_scan);
    webDesc = (TouchyWebView) view.findViewById(R.id.web_desc);
    noTeacher = (TextView) view.findViewById(R.id.no_teacher);
    srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    splashIndicator = (CircleIndicator) view.findViewById(R.id.splash_indicator);
    editJacket = (TextView) view.findViewById(R.id.edit_jacket);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    scrollView = (NestedScrollView) view.findViewById(R.id.scroll_root);
    view.findViewById(R.id.edit_jacket).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onJacketVp();
      }
    });
    view.findViewById(R.id.comments_detail).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkComments();
      }
    });
    view.findViewById(R.id.go_to_scan).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        gotoScan();
      }
    });
    view.findViewById(R.id.edit_base_info).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        editBaseInfo();
      }
    });

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
    editJacket.setCompoundDrawables(
      ContextCompat.getDrawable(getContext(), R.drawable.vd_edit_primary_20dp), null, null, null);
    srl.setOnRefreshListener(() -> mPresenter.queryDetail(mCourseDetail.getId()));
    if (savedInstanceState != null){
      scrollView.scrollTo(0,savedInstanceState.getInt("p",0));
    }
    return view;
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    try {
      outState.putInt("p",scrollView.getScrollY());
    }catch (Exception e){

    }

    super.onSaveInstanceState(outState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("课程种类详情");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(item -> {
      ArrayList<String> d = new ArrayList<>();
      d.add("删除该课程种类");
      new DialogList(getContext()).list(d,
        (adapterView, view, i, l) -> mPresenter.judgeDel(mCourseDetail,
          mCourseDetail.getShops() == null ? 1 : mCourseDetail.getShops().size(),getContext())).show();
      return true;
    });
  }

  private void initView(CourseType courseDetail) {
    getChildFragmentManager().beginTransaction()
      .replace(R.id.course_info, CourseBaseInfoShowFragment.newInstance(courseDetail))
      .commit();
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
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      PhotoUtils.origin(imageView,photos.get(i));
      //PhotoUtils.loadWidth(getContext(), photos.get(i), imageView,
      //  MeasureUtils.getScreenWidth(getResources()));
      final int curPos = i;
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          // TODO: 2017/11/30 图片预览
          //GalleryPhotoViewDialog dialog = new GalleryPhotoViewDialog(getContext());
          //dialog.setImage(photos);
          //dialog.setSelected(curPos);
          //dialog.show();
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
  public void setScore(float teacherScore, float courseScore, float serScore) {
    studentJudgeCoachScore.setText(StringUtils.getFloatDot1(teacherScore));
    studentJudgeCourseScore.setText(StringUtils.getFloatDot1(courseScore));
    serverScore.setText(StringUtils.getFloatDot1(serScore));
    studentJudgeCoachStar.setRating(teacherScore);
    studentJudgeCourseStar.setRating(courseScore);
    serverRate.setRating(serScore);
  }

  @Override public void setTeachers(List<Trainer> teachers) {
    if (teachers == null || teachers.size() == 0) {
      noTeacher.setVisibility(View.VISIBLE);
      return;
    }
    noTeacher.setVisibility(View.GONE);
    courseTeacherRv.setHasFixedSize(true);
    LinearLayoutManager manager =
      new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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
      courseImpression.setTags(impress2Str(impresssions));
    }
  }

  public List<String> impress2Str(List<TeacherImpression> impressions) {

    List<String> ret = new ArrayList<>();
    if (impressions == null) return ret;
    for (int i = 0; i < impressions.size(); i++) {
      ret.add(impressions.get(i).comment);
    }
    return ret;
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
      .onPositive((dialog, which) -> {
        showLoading();
        mPresenter.delCourse(mCourseDetail.getId());
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
    popBack();
  }

  @Override public void onDelfailed(String content) {
    srl.setRefreshing(false);
    hideLoading();
    ToastUtils.show(content);
  }

  /**
   * 封面管理
   */
 public void onJacketVp() {
    if (!mPresenter.hasAllEditPermission(mCourseDetail)) return;
    routeTo("/jacket/manager/",
      new cn.qingchengfit.saasbase.course.course.views.JacketManagerParams().courseid(
        mCourseDetail.getId())
        .randomOpen(mCourseDetail.isRandom_show_photos())
        .stringList((ArrayList<String>) mCourseDetail.getPhotos())
        .build());
  }

  /**
   * 查看评价详情
   */
 public void checkComments() {
    if (gymWrapper.inBrand()) {
      routeTo("/shop/comment/",
        new cn.qingchengfit.saasbase.course.course.views.ShopCommentsParams().mCourseId(
          mCourseDetail.getId()).build());
    } else {
      routeTo("/coach/comment/",CoachCommentListParams.builder().shop_id(gymWrapper.shop_id())
        .course_id(mCourseDetail.getId())
        .build());
    }
  }

  /**
   * 编辑简介 跳转二维码扫码
   */
 public void gotoScan() {
    if (!mPresenter.hasAllEditPermission(mCourseDetail)) return;
    QRActivity.start(getContext(), mCourseDetail.getEdit_url());
  }

  /**
   * 编辑基本信息
   */
 public void editBaseInfo() {
    routeTo("/edit/",
      new cn.qingchengfit.saasbase.course.course.views.EditCourseParams().courseType(mCourseDetail)
        .build());
  }

  @Override public String getFragmentName() {
    return CourseDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    if (position == viewpageradapter.getCount() - 2 && positionOffset > 0.4 && !isJumped) {
      isJumped = true;
      jacketVp.setCurrentItem(0);
      routeTo("/image/",
        new cn.qingchengfit.saasbase.course.course.views.CourseImagesParams().course_id(
          mCourseDetail.getId()).build());
    }
  }

  @Override public void onPageSelected(int position) {

  }

  @Override public void onPageScrollStateChanged(int state) {

  }
}