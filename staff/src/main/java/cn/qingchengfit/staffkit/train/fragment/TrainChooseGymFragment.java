package cn.qingchengfit.staffkit.train.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.items.NoDataTxtBtnItem;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.presenters.QuerySuPresenter;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.train.item.ChooseGymItem;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.google.gson.Gson;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 2017/4/1.
 */
public class TrainChooseGymFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, QuerySuPresenter.MVPView {

  RecyclerView recyclerview;
  String suHint;
  String suStr;
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView imgInfo;
  TextView tvHint;
  LinearLayout btnAddGym;
  LinearLayout layoutHint;
  @Inject QuerySuPresenter presenter;
  @Inject GymWrapper gymWrapper;
  @Inject StaffRespository respository;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private CommonFlexAdapter adapter;
  private CoachService tmpCoachService;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_train_choose_gym, container, false);
    suHint = getResources().getString(R.string.only_su_can_competition);
    suStr = getResources().getString(R.string.su_manager);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    imgInfo = (ImageView) view.findViewById(R.id.img_info);
    tvHint = (TextView) view.findViewById(R.id.tv_hint);
    btnAddGym = (LinearLayout) view.findViewById(R.id.btn_add_gym);
    layoutHint = (LinearLayout) view.findViewById(R.id.layout_hint);
    view.findViewById(R.id.btn_add_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickAddGym();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    adapter = new CommonFlexAdapter(datas, this);
    //Utils.highlightText(tvHint, suHint, suStr);
    recyclerview.setAdapter(adapter);
    adapter.setStickyHeaders(true).setDisplayHeadersAtStartUp(true);
    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_horizon_left_44dp)
            .withOffset(1)
            .withBottomEdge(true));
    RxBusAdd(EventClickViewPosition.class).subscribe(new Action1<EventClickViewPosition>() {
      @Override public void call(EventClickViewPosition eventClickViewPosition) {
        if (eventClickViewPosition.getId() == R.id.btn_fun) {
          startActivity(new Intent(getActivity(), GuideActivity.class));
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {

      }
    });
    RxBusAdd(EventFreshGyms.class).subscribe(new Action1<EventFreshGyms>() {
      @Override public void call(EventFreshGyms eventFreshGyms) {
        freshData();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {

      }
    });
    tmpCoachService = gymWrapper.getCoachService();
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    freshData();
  }

  public void freshData() {
    RxRegiste(respository.getStaffAllApi()
        .qcGetCoachService(App.staffId, null)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .flatMap(new Func1<QcDataResponse<GymList>, Observable<List<CoachService>>>() {
          @Override public Observable<List<CoachService>> call(
              QcDataResponse<GymList> gymListQcResponseData) {
            if (ResponseConstant.checkSuccess(gymListQcResponseData)) {
              return Observable.just(gymListQcResponseData.getData().services)
                  .onBackpressureBuffer()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread());
            } else {
              List<CoachService> em = new ArrayList<CoachService>();
              return Observable.just(em)
                  .observeOn(AndroidSchedulers.mainThread())
                  .onBackpressureBuffer()
                  .subscribeOn(Schedulers.io());
            }
          }
        })
        .subscribe(new Action1<List<CoachService>>() {
          @Override public void call(List<CoachService> coachServices) {
            if (coachServices != null && coachServices.size() > 0) {
              adapter.clear();
              datas.clear();
              for (int i = 0; i < coachServices.size(); i++) {
                datas.add(new ChooseGymItem(coachServices.get(i)));
              }
              adapter.updateDataSet(datas, true);
              //layoutHint.setVisibility(View.VISIBLE);
              btnAddGym.setVisibility(View.VISIBLE);
            } else {//无场馆
              adapter.clear();
              datas.clear();
              datas.add(new NoDataTxtBtnItem("暂无场馆", "请先添加一个场馆并完善信息", "新增场馆"));
              adapter.updateDataSet(datas, true);
              //layoutHint.setVisibility(View.GONE);
              btnAddGym.setVisibility(View.GONE);
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("选择报名场馆");
  }

  @Override public String getFragmentName() {
    return TrainChooseGymFragment.class.getName();
  }

  /**
   * 新增场馆
   */
  public void onClickAddGym() {
    Intent toAdd = new Intent(getContext(), GuideActivity.class);

    toAdd.putExtra("trainer", true);
    startActivity(toAdd);
  }

  @Override public boolean onItemClick(int i) {
    if (adapter.getItem(i) instanceof ChooseGymItem) {
      ChooseGymItem gymItem = (ChooseGymItem) adapter.getItem(i);
      presenter.getPermission(gymItem.getCoachService());
    }
    return false;
  }

  /**
   * @param isSu 表示是否有修改权限（修改后）// comment @ 2017/9/5
   */

  @Override public void onGetSu(boolean isSu, CoachService coachService) {
    if (isSu) {
      if (coachService.getcityCode() <= 0) {
        Intent toGymInfo = new Intent(getActivity(), GymActivity.class);
        toGymInfo.putExtra(GymActivity.GYM_TO, GymActivity.GYM_INFO);
        toGymInfo.putExtra("su", true);
        gymWrapper.setCoachService(coachService);
        startActivity(toGymInfo);
      } else {
        Intent ret = new Intent();
        ret.putExtra("json", new Gson().toJson(coachService, CoachService.class));
        String aciton = getActivity().getIntent().getData().getHost() + getActivity().getIntent()
            .getData()
            .getPath();
        ret.putExtra("web_action", aciton);
        getActivity().setResult(Activity.RESULT_OK, ret);
        getActivity().onBackPressed();
      }
    } else {
      showAlert("抱歉，您无场馆信息的修改权限，请联系超级管理员");
    }
  }

  @Override public void onDetach() {
    gymWrapper.setCoachService(tmpCoachService);
    super.onDetach();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}