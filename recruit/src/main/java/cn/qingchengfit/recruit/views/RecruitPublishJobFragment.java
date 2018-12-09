package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.FragmentRecruitPublishJobBinding;
import cn.qingchengfit.recruit.event.EventFreshJobsList;
import cn.qingchengfit.recruit.event.EventPulishPosition;
import cn.qingchengfit.recruit.event.EventRichTextBack;
import cn.qingchengfit.recruit.event.EventSetName;
import cn.qingchengfit.recruit.event.EventWelFare;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.saas.network.GetApi;
import cn.qingchengfit.saas.response.SuWrap;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TwoScrollPicker;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2017/6/7.
 */
@FragmentWithArgs public class RecruitPublishJobFragment extends BaseFragment
    implements JobPresenter.MVPView {

  public final static int PUBLISH_POSITION = 0x11;      //发布职位
  public final static int MODIFY_POSITION = 0x12;       //编辑职位
  public final static int POSITION_DESCRIPTION = 51;
  public final static int POSITION_REQUIREMENT = 52;

  //@BindView(R2.id.toolbar) Toolbar toolbar;
  //@BindView(R2.id.toolbar_title) TextView toolbarTitile;
  //@BindView(R2.id.civ_position_name) CommonInputView civPositionName;
  //@BindView(R2.id.civ_salary_rank) CommonInputView civSalaryRank;
  //@BindView(R2.id.civ_position_desc) CommonInputView civPositionDesc;
  //@BindView(R2.id.civ_position_demands) CommonInputView civPositionDemands;
  //@BindView(R2.id.civ_position_require) CommonInputView civPositionRequire;
  //@BindView(R2.id.civ_position_welfare) CommonInputView civPositionWelfare;
  //@BindView(R2.id.civ_gym_desc) CommonInputView civGymDesc;
  //@BindView(R2.id.btn_publish) Button btnPublish;

  @Inject RecruitRouter router;
  @Inject JobPresenter presenter;
  @Inject QcRestRepository qcRestRepository;
  @Arg String gymId;
  @Arg int type;
  @Arg Job job;


  FragmentRecruitPublishJobBinding db;
  private JobBody body;

  private TwoScrollPicker twoScrollPicker;
  private HashMap<String, Object> params = new HashMap<>();

  private String gymContent;
  private FragmentManager.FragmentLifecycleCallbacks fmCb =
      new FragmentManager.FragmentLifecycleCallbacks() {
        @Override public void onFragmentDetached(FragmentManager fm, Fragment f) {
          super.onFragmentDetached(fm, f);
          if (!(f instanceof RecruitPublishJobFragment)) setBackPress();
        }
      };

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitPublishJobFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
     db = FragmentRecruitPublishJobBinding.inflate(inflater);
     initClick();
    //unbinder = ButterKnife.bind(this, view);
    twoScrollPicker = new TwoScrollPicker(getContext());
    body = new JobBody();
    db.civGymDesc.setContent("详情");
    db.civGymDesc.setContentColor(ContextCompat.getColor(getContext(), R.color.qc_text_grey));
    delegatePresenter(presenter, this);
    initToolbar(db.layoutToolbar.toolbar);
    initContent();
    initRxBus();
    ToolbarModel tbm = new ToolbarModel("发布职位");
    if (type == PUBLISH_POSITION) {
      tbm.setTitle("发布职位");
    } else {
      tbm.setTitle("编辑职位");
      presenter.queryGymDetail(gymId);
    }
    db.setToolbarModel(tbm);
    getFragmentManager().registerFragmentLifecycleCallbacks(fmCb, false);
    return db.getRoot();
  }

  private void initContent() {
    if (type == MODIFY_POSITION) {
      if (job != null) {
        db.civPositionName.setContent(job.name);
        body.name = job.name;
        db.civSalaryRank.setContent(RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary));
        body.min_salary = job.min_salary;
        body.max_salary = job.max_salary;
        db.civPositionDemands.setContent(TextUtils.isEmpty(job.requirement) ? "请填写" : "详情");
        body.requirement = job.requirement;
        db.civPositionDesc.setContent(TextUtils.isEmpty(job.description) ? "请填写" : "详情");
        body.description = job.description;
        db.civPositionRequire.setContent(
            RecruitBusinessUtils.getWorkYear(job.min_work_year, job.max_work_year)
                + "/"
                + RecruitBusinessUtils.getGender(job.gender)
                + "/"
                + RecruitBusinessUtils.getDegree(getContext(), job.education));
        body.min_work_year = job.min_work_year;
        body.max_work_year = job.max_work_year;
        body.gender = job.gender;
        body.education = job.education;
        if (job.welfare != null) {
          db.civPositionWelfare.setContent(RecruitBusinessUtils.getPositionDamen(job.welfare));
        }
        body.welfare = job.welfare;
        body.min_age = job.min_age;
        body.max_age = job.max_age;
        body.min_height = job.min_height;
        body.max_height = job.max_height;
        body.min_weight = job.min_weight;
        body.min_weight = job.min_weight;
      }
      if (db.getToolbarModel() != null)
        db.getToolbarModel().setMenu(R.menu.menu_save);
      db.layoutToolbar.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          presenter.modifyJob(job.id, body);
          return false;
        }
      });
      db.btnPublish.setVisibility(View.GONE);
    }
  }

  @Override public boolean onFragmentBackPress() {
    if (job != null) {
      DialogUtils.instanceDelDialog(getContext(), "确定放弃所做修改？",
          new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              dialog.dismiss();
              getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
          }).show();
      return true;
    } else {
      return false;
    }
  }

  private void initRxBus() {
    RxBusAdd(EventPulishPosition.class).subscribe(new Action1<EventPulishPosition>() {
      @Override public void call(EventPulishPosition eventPulishPosition) {
        //setBackPress();
        if (eventPulishPosition != null) {
          body = eventPulishPosition.body;
          refreshCivContent(eventPulishPosition.body);
        }
      }
    });

    RxBusAdd(EventSetName.class)
        //.delay(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<EventSetName>() {
      @Override public void call(EventSetName eventSetName) {
        //setBackPress();
        if (eventSetName != null) {
          body.name = eventSetName.name;
          db.civPositionName.setContent(eventSetName.name);
          db.civPositionName.setContentColor(getResources().getColor(R.color.qc_text_grey));
        }
      }
    });

    RxBusAdd(EventRichTextBack.class)
        //.delay(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<EventRichTextBack>() {
      @Override public void call(EventRichTextBack eventRichTextBack) {
        //setBackPress();
        if (eventRichTextBack != null) {
          switch (eventRichTextBack.type) {
            case POSITION_DESCRIPTION:
              body.description = eventRichTextBack.content;
              db.civPositionDesc.setContent("详情");
              db.civPositionDesc.setContentColor(getResources().getColor(R.color.qc_text_grey));
              break;
            case POSITION_REQUIREMENT:
              body.requirement = eventRichTextBack.content;
              db.civPositionDemands.setContent("详情");
              db.civPositionDemands.setContentColor(
                  ContextCompat.getColor(getContext(), R.color.qc_text_grey));
              break;
          }
        }
      }
    });

    RxBusAdd(EventWelFare.class)
        //.delay(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<EventWelFare>() {
      @Override public void call(EventWelFare eventWelFare) {
        //setBackPress();
        if (eventWelFare != null) {
          body.welfare = eventWelFare.welfareList;
          if (body.welfare.size() > 0) {
            db.civPositionWelfare.setContent(RecruitBusinessUtils.getPositionDamen((body.welfare)));
            db.civPositionWelfare.setContentColor(
                ContextCompat.getColor(getContext(), R.color.qc_text_grey));
          }
        }
      }
    });
  }

  private void refreshCivContent(JobBody body) {

    db.civPositionRequire.setContent(
        RecruitBusinessUtils.getWorkYear(body.min_work_year, body.max_work_year)
            + "/"
            + RecruitBusinessUtils.getGender(body.gender)
            + "/"
            + RecruitBusinessUtils.getDegree(getContext(), body.education));
    db.civPositionRequire.setContentColor(getResources().getColor(R.color.qc_text_grey));
  }

  @Override public String getFragmentName() {
    return RecruitPublishJobFragment.class.getName();
  }

  @Override public void onDestroyView() {
    getFragmentManager().unregisterFragmentLifecycleCallbacks(fmCb);
    setBackPressNull();
    super.onDestroyView();
  }

  /**
   * 职位名称
   */
   public void onCivPositionNameClicked() {
    router.toSetRequireName("职位名称", body.name, "");
  }

  /**
   * 薪资范围
   */
  public void onCivSalaryRankClicked() {
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0) {
          db.civSalaryRank.setContent("面议");
          body.min_salary = -1;
          body.max_salary = -1;
        } else if (left == 100) {
          db.civSalaryRank.setContent("100K以上");
          body.min_salary = 100001;
          body.max_salary = 100001;
        } else {
          if (right != 0 && left > right) {
            ToastUtils.show("请选择正确的薪水区间");
            return;
          }
          body.min_salary = (left - 1) * 1000;
          if (right == 0){
            body.max_salary = -1;
          }else {
            body.max_salary = right * 1000;
          }
          db.civSalaryRank.setContent(
              RecruitBusinessUtils.getSalary(body.min_salary, body.max_salary));
        }
      }
    });
    db.civSalaryRank.setContentColor(getResources().getColor(R.color.qc_text_grey));
    final ArrayList<String> l = new ArrayList<>();
    final ArrayList<String> r = new ArrayList<>();
    l.add("面议");
    r.add("不限");
    for (int i = 0; i < 100; i++) {
      l.add(i + "K");
      r.add((i + 1) + "K");
    }
    l.add("100K以上");
    twoScrollPicker.show(l, r, body.min_salary + 1, body.max_salary);
  }
  void initClick(){
    db.civPositionDesc.setOnClickListener(view -> onCivPositionDescClicked());
    db.civPositionDemands.setOnClickListener(view -> onCivPositionDemandsClicked());
    db.civPositionRequire.setOnClickListener(view -> onCivPositionRequireClicked());
    db.civPositionWelfare.setOnClickListener(view -> onCivPositionWelfareClicked());
    db.civGymDesc.setOnClickListener(view -> onCivGymDescClicked());
    db.btnPublish.setOnClickListener(view -> onBtnPublishClicked());
    db.civPositionName.setOnClickListener(view -> onCivPositionNameClicked());
    db.civSalaryRank.setOnClickListener(view -> onCivSalaryRankClicked());
  }
  /**
   * 职位描述
   */
  public void onCivPositionDescClicked() {
    router.toEditRecruitDesc(body.description, "职位描述", "请填写职位描述", POSITION_DESCRIPTION);
  }

  /**
   * 任职要求
   */
   public void onCivPositionDemandsClicked() {
    router.toEditRecruitDesc(body.requirement, "任职要求", "请填写任职要求", POSITION_REQUIREMENT);
  }

  /**
   * 职位要求
   */
  public void onCivPositionRequireClicked() {
    router.toRequireOfJob(body);
  }

  /**
   * 职位福利
   */
  public void onCivPositionWelfareClicked() {
    if (body.welfare == null) {
      body.welfare = new ArrayList<>();
    }
    router.toPositionWalfare(new ArrayList<String>(body.welfare), "职位福利");
  }

  /**
   * 场馆介绍
   */
  public void onCivGymDescClicked() {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .querySu(gymId)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SuWrap>>() {
          @Override public void call(QcDataResponse<SuWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.is_superuser) {
                if (job != null) {
                  router.toWriteGymIntro(job.gym);
                } else if (gymId != null) {
                  router.toWriteGymIntro(new Gym.Builder().id(gymId).build());
                }
              } else {
                showAlert("抱歉，您没有该功能呢权限，请联系超级管理员");
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  /**
   * 发布职位按钮
   */
   public void onBtnPublishClicked() {
    if (TextUtils.isEmpty(body.name) || TextUtils.isEmpty(body.description) || TextUtils.isEmpty(
        body.requirement)) {
      DialogUtils.showAlert(getContext(), "请完善职位信息");
      return;
    }
    presenter.queryEditPermiss(gymId, "job");
  }

  @Override public void onEditOk() {
    ToastUtils.show("修改成功");
    RxBus.getBus().post(new EventFreshJobsList());
    getFragmentManager().popBackStackImmediate();
  }

  @Override public void onJobDetail(Job job) {
    ToastUtils.show("发布成功");
    getActivity().onBackPressed();
    router.tojobDetailEmployer(job, true);
  }

  @Override public void starOK() {

  }

  @Override public void unStarOk() {

  }

  @Override public void onPostResumeOk() {

  }

  @Override public void onInviteOk() {

  }

  @Override public void toEditJob() {
    body.gym_id = gymId;
    body.published = true;
    presenter.publishJob(body);
  }

  @Override public void onJobList(List<Job> jobList) {

  }

  @Override public void onGymDetail(Gym gym) {
    gymContent = gym.description;
  }
}
