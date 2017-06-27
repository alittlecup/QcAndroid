package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventIntentCities;
import cn.qingchengfit.recruit.event.EventIntentJobs;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.model.ResumeIntents;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.network.response.ResumeIntentsWrap;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by Paper on 2017/6/13.
 */
@FragmentWithArgs public class ResumeIntentsFragment extends BaseFragment {
  public static final int MIN_SALARY = 1;
  public static final int MAX_SALARY = 99;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.civ_intent_status) CommonInputView civIntentStatus;
  @BindView(R2.id.civ_intent_postion) CommonInputView civIntentPostion;
  @BindView(R2.id.civ_intent_city) CommonInputView civIntentCity;
  @BindView(R2.id.civ_intent_salay) CommonInputView civIntentSalay;
  ArrayList<String> curStatusArray;
  @Inject RecruitRouter router;
  @Inject QcRestRepository qcRestRepository;
  ResumeBody body;
  ResumeIntents resumeHome;
  private SimpleScrollPicker simpleScrollPicker;
  private TwoScrollPicker twoScrollPicker;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeIntentsFragmentBuilder.injectArguments(this);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    twoScrollPicker = new TwoScrollPicker(getContext());
    curStatusArray = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.resume_self_status)));
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_intents, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    body = new ResumeBody.Builder().build();
    RxBusAdd(EventIntentCities.class).subscribe(new Action1<EventIntentCities>() {
      @Override public void call(EventIntentCities eventIntentCities) {
        if (body.exp_cities == null) body.exp_cities = new ArrayList<String>();
        body.exp_cities.clear();
        if (eventIntentCities != null && eventIntentCities.cityBeens.size() != 0) {
          civIntentCity.setContent(RecruitBusinessUtils.getStrFromCities(eventIntentCities.cityBeens));
          body.exp_cities = RecruitBusinessUtils.getIdsFromCities(eventIntentCities.cityBeens);
          resumeHome.exp_cities = eventIntentCities.cityBeens;
        } else {
          civIntentCity.setContent("不限");
        }
      }
    });
    RxBusAdd(EventIntentJobs.class).subscribe(new Action1<EventIntentJobs>() {
      @Override public void call(EventIntentJobs eventIntentJobs) {
        if (body.exp_jobs == null) body.exp_jobs = new ArrayList<String>();
        body.exp_jobs.clear();
        if (eventIntentJobs != null) body.exp_jobs.addAll(eventIntentJobs.jobs);
        civIntentPostion.setContent(CmStringUtils.List2Str(eventIntentJobs.jobs));
      }
    });

    return view;
  }

  public void setInofo(ResumeIntents resumeHome) {
    this.resumeHome = resumeHome;
    civIntentCity.setContent(RecruitBusinessUtils.getStrFromCities(resumeHome.exp_cities));
    int st = resumeHome.status % 5;
    body.status = st;
    if (st == 0) {
      civIntentStatus.setContent("未填写");
    } else {
      civIntentStatus.setContent(curStatusArray.get(st - 1));
    }
    civIntentPostion.setContent(CmStringUtils.List2Str(resumeHome.exp_jobs));
    if (resumeHome.min_salary != null && resumeHome.min_salary > 100000) {
      civIntentSalay.setContent("100K以上");
    } else {
      civIntentSalay.setContent(
          RecruitBusinessUtils.getSalary(resumeHome.min_salary, resumeHome.max_salary, "面议"));
    }
  }

  public void freshData() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryMyResumeIntents()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcDataResponse<ResumeIntentsWrap>>() {
          @Override public void call(QcDataResponse<ResumeIntentsWrap> resumeIntentsQcDataResponse) {
            if (resumeIntentsQcDataResponse.status == 200) {
              setInofo(resumeIntentsQcDataResponse.data.resume);
            } else {
              onShowError(resumeIntentsQcDataResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            onShowError(throwable.getMessage());
          }
        }));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    freshData();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑求职意向");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showLoading();
        RxRegiste(qcRestRepository.createGetApi(PostApi.class).updateResume(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(new Action1<QcResponse>() {
              @Override public void call(QcResponse qcResponse) {
                hideLoading();
                if (qcResponse.status == 200) {
                  RxBus.getBus().post(new EventResumeFresh());
                  getActivity().onBackPressed();
                } else {
                  ToastUtils.show(qcResponse.getMsg());
                }
              }
            }, new Action1<Throwable>() {
              @Override public void call(Throwable throwable) {
                hideLoading();
                ToastUtils.show(throwable.getMessage());
              }
            }));
        return false;
      }
    });
  }

  @Override public String getFragmentName() {
    return ResumeIntentsFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 当前状态
   */
  @OnClick(R2.id.civ_intent_status) public void onCivIntentStatusClicked() {
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civIntentStatus.setContent(curStatusArray.get(pos));
        body.status = pos + 1;
      }
    });
    simpleScrollPicker.show(curStatusArray,
        (body.status == null || body.status == 0) ? 1 : body.status - 1);
  }

  /**
   * 期望职位
   */
  @OnClick(R2.id.civ_intent_postion) public void onCivIntentPostionClicked() {
    router.toIntentPosition(resumeHome.exp_jobs);
  }

  /**
   * 期望城市
   */
  @OnClick(R2.id.civ_intent_city) public void onCivIntentCityClicked() {
    router.toIntentCities(resumeHome.exp_cities);
  }

  /**
   * 期望薪水
   */
  @OnClick(R2.id.civ_intent_salay) public void onCivIntentSalayClicked() {
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0) {
          civIntentSalay.setContent("面议");
          body.min_salary = -1000;
          body.max_salary = -1000;
        } else if (left == 100) {
          civIntentSalay.setContent("100K以上");
          body.min_salary = 100001;
          body.max_salary = 100001;
        } else {
          if (left > right) {
            ToastUtils.show("请选择正确的薪水区间");
            return;
          }
          civIntentSalay.setContent((left - MIN_SALARY) + "-" + (right - MIN_SALARY + 2) + "K");
          body.min_salary = (left - MIN_SALARY) * 1000;
          body.max_salary = (right - MIN_SALARY + 2) * 1000;
        }
      }
    });
    final ArrayList<String> l = new ArrayList<>();
    final ArrayList<String> r = new ArrayList<>();
    l.add("面议");
    for (int i = 0; i < 100; i++) {
      l.add(i + "K");
      r.add((i + 1) + "K");
    }
    l.add("100K以上");
    //if (body.max_salary == -1 || body.min_salary == -1){
    twoScrollPicker.show(l, r, 0, 0);
    //}else {
    //  twoScrollPicker.show(l, r, body.min_salary%101+1, body.max_salary%100);
    //}

  }
}
