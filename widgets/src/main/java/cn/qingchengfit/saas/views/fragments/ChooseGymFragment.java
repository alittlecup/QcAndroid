package cn.qingchengfit.saas.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventChooseGym;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.items.ChooseGymItem;
import cn.qingchengfit.items.NoDataTxtBtnItem;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saas.network.GetApi;
import cn.qingchengfit.saas.response.GymListWrap;
import cn.qingchengfit.saas.response.SuWrap;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
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
 * Created by Paper on 2017/7/4.
 */
public class ChooseGymFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.toolbar) protected Toolbar toolbar;
  @BindView(R2.id.toolbar_title) protected TextView toolbarTitle;
  @Inject protected QcRestRepository qcRestRepository;
  @BindView(R2.id.recyclerview) RecyclerView recyclerview;
  @BindView(R2.id.btn_add_gym) LinearLayout btnAddGym;
  private CommonFlexAdapter adapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_choose_gym_for_other, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter = new CommonFlexAdapter(new ArrayList(), this);
    recyclerview.setAdapter(adapter);
    RxBusAdd(EventClickViewPosition.class).subscribe(new Action1<EventClickViewPosition>() {
      @Override public void call(EventClickViewPosition eventClickViewPosition) {
        if (eventClickViewPosition.getId() == R.id.btn_fun) {
          onViewClicked();
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
    });
    freshData();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择场馆");
  }

  @Override public void onResume() {
    super.onResume();
  }

  protected void freshData() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryPermissionGyms()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymListWrap>>() {
          @Override public void call(QcDataResponse<GymListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.gyms != null) {
                adapter.clear();
                for (Gym gym : qcResponse.data.gyms) {
                  adapter.addItem(new ChooseGymItem(gym));
                }
                if (adapter.getItemCount() == 0) {
                  adapter.addItem(new NoDataTxtBtnItem("暂无场馆", "请先添加一个场馆并完善信息", "新增场馆"));
                  btnAddGym.setVisibility(View.GONE);
                }
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public String getFragmentName() {
    return ChooseGymFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.btn_add_gym) public void onViewClicked() {
    try {
      Intent toChooseBrand = new Intent(getContext().getPackageName() + ".chooseBrand");
      startActivityForResult(toChooseBrand, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        Brand brand = (Brand) data.getParcelableExtra("fragment_result");
        if (brand != null) {
          try {
            Intent guide = new Intent(getContext().getPackageName() + ".guide");
            guide.putExtra("brand", brand);
            guide.putExtra("workexp", true);
            startActivityForResult(guide, 2);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } else if (requestCode == 2) {
        freshData();
      }
    }
  }

  @Override public boolean onItemClick(int i) {
    if (adapter.getItem(i) instanceof ChooseGymItem) {
      ChooseGymItem gymItem = (ChooseGymItem) adapter.getItem(i);
      if (gymItem.isCompeleted()) {
        queryPermiss(gymItem.getGym());
      } else {
        querySu(gymItem.getGym());
      }
    }
    return false;
  }

  protected void goEditGym(Gym gym) {

  }

  protected void querySu(final Gym gym) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .querySu(gym.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SuWrap>>() {
          @Override public void call(QcDataResponse<SuWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.is_superuser) {
                goEditGym(gym);
              } else {
                hasNoPermission();
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  protected void queryPermiss(final Gym gym) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class).querySu(gym.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SuWrap>>() {
          @Override public void call(QcDataResponse<SuWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.is_superuser) {
                hasPermission(gym);
              } else {
                hasNoPermission();
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void hasPermission(Gym gym) {
    RxBus.getBus().post(new EventChooseGym(gym));
    getActivity().onBackPressed();
  }

  public void hasNoPermission() {
    showAlert("抱歉，您无该功能权限，请联系超级管理员");
  }
}
