package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.item.ResumeBaseInfoItem;
import cn.qingchengfit.recruit.item.ResumeCertificateItem;
import cn.qingchengfit.recruit.item.ResumeEduExpItem;
import cn.qingchengfit.recruit.item.ResumeEmptyItem;
import cn.qingchengfit.recruit.item.ResumeIntentImgShowItem;
import cn.qingchengfit.recruit.item.ResumeIntentItem;
import cn.qingchengfit.recruit.item.ResumeTitleItem;
import cn.qingchengfit.recruit.item.ResumeWebDescItem;
import cn.qingchengfit.recruit.item.ResumeWorkExpItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Organization;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.presenter.ResumePermissionPresenter;
import cn.qingchengfit.recruit.presenter.ResumePostPresenter;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 2017/6/9.
 */
public class ResumeHomeFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, ResumePresenter.MVPView, ResumePostPresenter.MVPView, ResumePermissionPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.tv_resume_completed) TextView tvResumeCompleted;
  @BindView(R2.id.tv_resume_open) TextView tvResumeOpen;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.btn_open_resume) Button btnOpen;
  @BindView(R2.id.srl) SwipeRefreshLayout srl;
  CommonFlexAdapter commonFlexAdapter;

  @Inject ResumePresenter presenter;
  @Inject ResumePostPresenter postPresenter;
  @Inject RecruitRouter router;
  @Inject LoginStatus loginStatus;
  @Inject QcRestRepository qcRestRepository;
  @Inject ResumePermissionPresenter permissionPresenter;
  @Inject GymWrapper gymWrapper;
  ResumeHome resumeHome;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    delegatePresenter(postPresenter, this);
    delegatePresenter(permissionPresenter, this);
    initToolbar(toolbar);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    srl.setColorSchemeColors(AppUtils.getPrimaryColor(getContext()));
    srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        srl.setRefreshing(false);
        presenter.queryResumeHome();
      }
    });
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.addItemDecoration(
        new QcLeftRightDivider(getContext(), 1, R.layout.item_resume_base_info, 0, 0));
    rv.addItemDecoration(
        new QcLeftRightDivider(getContext(), 1, R.layout.item_resume_work_intent, 0, 0));
    rv.addItemDecoration(new QcLeftRightDivider(getContext(), 1, R.layout.item_edu_exp, 0, 0));

    rv.setAdapter(commonFlexAdapter);
    presenter.queryResumeHome();
    RxBusAdd(ResumeTitleItem.class).subscribe(new Action1<ResumeTitleItem>() {
      @Override public void call(ResumeTitleItem resumeTitleItem) {
        if (resumeHome == null) return;
        switch (resumeTitleItem.getPos()) {
          case 0:
            router.editResumeInfo(new ResumeBody.Builder().username(resumeHome.username)
                .avatar(resumeHome.avatar)
                .birthday(resumeHome.birthday)
                .gender(resumeHome.gender)
                .height(resumeHome.height)
                .weight(resumeHome.weight)
                .brief_description(resumeHome.brief_description)
                .work_year(resumeHome.work_year)
                .gd_district(resumeHome.gd_district)
                .build());
            break;
          case 1:
            router.toExpect(resumeHome);
            break;
          case 2:
            router.editImages(resumeHome.photos);
            break;
          case 3:
            router.listWorkList();
            break;
          case 4:
            router.listCertifaciton();
            break;
          case 5:
            router.listEdulist();
            break;
          case 6:
            router.brief(resumeHome.self_description);
            break;
        }
      }
    });
    RxBusAdd(EventResumeFresh.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EventResumeFresh>() {
      @Override public void call(EventResumeFresh eventResumeFresh) {
        presenter.queryResumeHome();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("我的简历");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  /**
   * 公开简历
   */
  @OnClick(R2.id.btn_open_resume) public void openResume() {
    onCheckSuccess();
  }

  /**
   * 预览简历
   */
  @OnClick(R2.id.btn_preview_resume) public void previewResume() {
    BaseRouter.routerToWeb(qcRestRepository.getHost() + "mobile/resume/?id=" + resumeHome.id,
        getContext());
  }

  @Override public String getFragmentName() {
    return ResumeHomeFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof ResumeEmptyItem) {
      if (resumeHome == null) return true;
      switch (((ResumeEmptyItem) commonFlexAdapter.getItem(i)).getPos()) {
        case 1:
          router.toExpect(resumeHome);
          break;
        case 2:
          router.editImages(resumeHome.photos);
          break;
        case 3:
          Intent toSearch = new Intent(getActivity(), SearchActivity.class);
          toSearch.putExtra("type", SearchFragment.TYPE_GYM);
          startActivityForResult(toSearch, 10010);
          break;
        case 4:
          int requestCode = 10012;
          Intent toSearch2 = new Intent(getActivity(), SearchActivity.class);
          toSearch2.putExtra("type", SearchFragment.TYPE_ORGANASITON);
          startActivityForResult(toSearch2, requestCode);
          break;
        case 5:
          router.addEduExp();
          break;
        case 6:
          router.brief(resumeHome.self_description);
          break;
      }
    }
    return true;
  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {
    this.resumeHome = resumeHome;
    commonFlexAdapter.clear();
    tvResumeCompleted.setText(resumeHome.completion + "%");
    tvResumeCompleted.setTextColor(
        ContextCompat.getColor(getContext(), resumeHome.completion >= RecruitConstants.RESUME_COMPLETED ? R.color.text_dark : R.color.red));
    tvResumeOpen.setText(resumeHome.is_share ? "状态：已公开" : "状态：未公开");
    btnOpen.setText(resumeHome.is_share ? "隐藏我的简历" : "公开我的简历");
    commonFlexAdapter.addItem(new ResumeTitleItem(0, getContext(), true));
    commonFlexAdapter.addItem(new ResumeBaseInfoItem(resumeHome));

    if (ListUtils.isEmpty(resumeHome.exp_cities)
        && ListUtils.isEmpty(resumeHome.exp_jobs)
        && resumeHome.min_salary == null
        && resumeHome.max_salary == null) {
      commonFlexAdapter.addItem(new ResumeTitleItem(1, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(1, getContext()));
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(1, getContext(), true));
      commonFlexAdapter.addItem(new ResumeIntentItem(getContext(), resumeHome.exp_jobs, resumeHome.exp_cities,
          RecruitBusinessUtils.getSalary(resumeHome.min_salary, resumeHome.max_salary, "面议"),
          resumeHome.status));
    }
    if (resumeHome.photos != null && resumeHome.photos.size() != 0) {
      commonFlexAdapter.addItem(new ResumeTitleItem(2, getContext(), true));
      commonFlexAdapter.addItem(new ResumeIntentImgShowItem(resumeHome.photos));
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(2, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(2, getContext()));
    }

    if (resumeHome.experiences != null && resumeHome.experiences.size() != 0) {
      commonFlexAdapter.addItem(new ResumeTitleItem(3, getContext(), true));
      for (WorkExp w : resumeHome.experiences) {
        commonFlexAdapter.addItem(new ResumeWorkExpItem(w, getContext()));
      }
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(3, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(3, getContext()));
    }
    if (resumeHome.certificates != null && resumeHome.certificates.size() != 0) {
      commonFlexAdapter.addItem(new ResumeTitleItem(4, getContext(), true));
      for (Certificate c : resumeHome.certificates) {
        commonFlexAdapter.addItem(new ResumeCertificateItem(c));
      }
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(4, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(4, getContext()));
    }

    if (resumeHome.educations != null && resumeHome.educations.size() != 0) {
      commonFlexAdapter.addItem(new ResumeTitleItem(5, getContext(), true));
      for (Education e : resumeHome.educations) {
        commonFlexAdapter.addItem(new ResumeEduExpItem(e));
      }
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(5, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(5, getContext()));
    }

    if (!TextUtils.isEmpty(resumeHome.self_description)) {
      commonFlexAdapter.addItem(new ResumeTitleItem(6, getContext(), true));
      commonFlexAdapter.addItem(new ResumeWebDescItem(resumeHome.self_description));
    } else {
      commonFlexAdapter.addItem(new ResumeTitleItem(6, getContext(), false));
      commonFlexAdapter.addItem(new ResumeEmptyItem(6, getContext()));
    }
  }

  @Override public void onWorkExpList(List<WorkExp> workExps) {

  }

  @Override public void onEduExpList(List<Education> eduExps) {

  }

  @Override public void onCertiList(List<Certificate> certificates) {

  }

  @Override public void starOk() {

  }

  @Override public void unStartOk() {

  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10010) {
      if (resultCode > 0) {
        Gym gym = new Gym();
        gym.setId(data.getLongExtra("id", 0) + "");
        gym.setName(data.getStringExtra("username"));
        gym.setAddress(data.getStringExtra("address"));
        gym.setPhoto(data.getStringExtra("pic"));
        router.addWorkExp(gym);
        return;
      }
    }

    if (resultCode > 0 && requestCode == 10012) {
      Organization gym = new Organization();
      gym.setId(data.getLongExtra("id", 0) + "");
      gym.setName(data.getStringExtra("username"));
      gym.setPhoto(data.getStringExtra("pic"));
      gym.setContact(data.getStringExtra("address"));
      router.addCertification(2, gym);
      return;
    }
  }

  @Override public void onPostOk() {
    hideLoading();
    presenter.queryResumeHome();
  }

  @Override public void onCheckSuccess() {
    DialogUtils.instanceDelDialog(getContext(), resumeHome.is_share ? "确定要隐藏简历？" : "确定要公开简历？",
        resumeHome.is_share ? "隐藏简历后，您的简历将不会显示在人才市场上，招聘方将不能主动联系您" : "公开简历后，您的简历将显示在人才市场上，招聘方可以主动联系您",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            showLoading();
            postPresenter.editResume(new ResumeBody.Builder().is_share(!resumeHome.is_share).build());
          }
        }).show();
  }
}
