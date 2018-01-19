package cn.qingchengfit.recruit.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.events.EventChooseGym;
import cn.qingchengfit.items.StickerHintItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.FragmentRecruitManageBinding;
import cn.qingchengfit.recruit.item.RecruitGymItem;
import cn.qingchengfit.recruit.model.GymHasResume;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.GymListWrap;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.view.RxView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
public class RecruitManageFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {


  @Inject QcRestRepository qcRestRepository;
  @Inject RecruitRouter router;

  List<AbstractFlexibleItem> items = new ArrayList<>();
  CommonFlexAdapter commonFlexAdapter;

  int jobCount = 1;
  FragmentRecruitManageBinding db;

  public static RecruitManageFragment newInstance(int jobcount) {
    Bundle args = new Bundle();
    args.putInt("count", jobcount);
    RecruitManageFragment fragment = new RecruitManageFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      jobCount = getArguments().getInt("count");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_recruit_manage,container,false);
    initToolbar(db.layoutToolbar.toolbar);
    commonFlexAdapter = new CommonFlexAdapter(items, this);
    db.rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
    db.rvGyms.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    db.rvGyms.setAdapter(commonFlexAdapter);
    RxBusAdd(EventChooseGym.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventChooseGym>() {
          @Override public void call(EventChooseGym eventChooseGym) {
            if (eventChooseGym.gym != null) {
              //去发布职位
              router.toPublishPosition(eventChooseGym.gym.getId(), null,
                  RecruitPublishJobFragment.PUBLISH_POSITION);
            } else {
              ToastUtils.show("没有选择场馆");
            }
          }
        });
    RxView.clicks(db.layoutStarredResume)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          onLayoutStarredResumeClicked();
        }
      });
    RxView.clicks(db.btnPublishNewPosition)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          onBtnPublishNewPositionClicked();
        }
      });
    return db.getRoot();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    ToolbarModel tbm = new ToolbarModel("招聘管理");
    db.setToolbarModel(tbm);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    refreshData();
  }

  protected void refreshData() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryManageGyms()
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymListWrap>>() {
          @Override public void call(QcDataResponse<GymListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              commonFlexAdapter.clear();
              if (qcResponse.data.gyms != null) {
                Collections.sort(qcResponse.data.gyms, new Comparator<GymHasResume>() {
                  @Override public int compare(GymHasResume o1, GymHasResume o2) {
                    int a1 = o1.has_jobs ? 0 : 1;
                    int a2 = o2.has_jobs ? 0 : 1;
                    return a1 - a2;
                  }
                });
                if (qcResponse.data.gyms.size() > 0) {
                  boolean hasJob = qcResponse.data.gyms.get(0).has_jobs;

                  commonFlexAdapter.addItem(new StickerHintItem(hasJob ? "正在招聘的场馆" : "未发布职位的场馆"));
                  for (GymHasResume gym : qcResponse.data.gyms) {
                    if (gym.has_jobs != hasJob){
                      commonFlexAdapter.addItem(new StickerHintItem("未发布职位的场馆"));
                      hasJob = gym.has_jobs;
                    }
                    commonFlexAdapter.addItem(new RecruitGymItem(gym));
                  }
                }
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public String getFragmentName() {
    return RecruitManageFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onLayoutStarredResumeClicked() {
    router.toStarredResumes();
  }

  void onBtnPublishNewPositionClicked() {
    router.chooseGym();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item instanceof RecruitGymItem) {
      router.toGymdetailEmployer(((RecruitGymItem) item).getGym());
    }
    return false;
  }
}
