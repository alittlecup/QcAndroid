package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.events.EventChooseGym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitGymItem;
import cn.qingchengfit.recruit.model.GymHasResume;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.GymListWrap;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
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
public class RecruitManageFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.rv_gyms) RecyclerView rvGyms;

  @Inject QcRestRepository qcRestRepository;
  @Inject RecruitRouter router;

  List<AbstractFlexibleItem> items = new ArrayList<>();
  CommonFlexAdapter commonFlexAdapter;

  int jobCount = 1;

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
    View view = inflater.inflate(R.layout.fragment_recruit_manage, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    commonFlexAdapter = new CommonFlexAdapter(items, this);
    rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
    rvGyms.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    rvGyms.setAdapter(commonFlexAdapter);
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
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("招聘管理");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    refreshData();
    if (jobCount == 0) {
      onBtnPublishNewPositionClicked();
    }
  }

  protected void refreshData() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryManageGyms()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymListWrap>>() {
          @Override public void call(QcDataResponse<GymListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              commonFlexAdapter.clear();
              if (qcResponse.data.gyms != null) {
                for (GymHasResume gym : qcResponse.data.gyms) {
                  commonFlexAdapter.addItem(new RecruitGymItem(gym));
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

  @OnClick(R2.id.layout_starred_resume) public void onLayoutStarredResumeClicked() {
    router.toStarredResumes();
  }

  @OnClick(R2.id.btn_publish_new_position) public void onBtnPublishNewPositionClicked() {
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
