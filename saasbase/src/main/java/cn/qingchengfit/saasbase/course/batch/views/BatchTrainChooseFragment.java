package cn.qingchengfit.saasbase.course.batch.views;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;
import cn.qingchengfit.saasbase.course.batch.event.BatchCoachEvent;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.utils.CrashUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2018/4/12.
 */

@Leaf(module = "course", path = "/batch/choose/trainer") public class BatchTrainChooseFragment
    extends TrainerChooseFragment {

  @Need public String start;
  @Need public String end;

  @Inject ViewModelProvider.Factory factory;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    return view;
  }

  @Override public void initData() {
    stopRefresh();
    ArrayList<BatchCopyCoach> dataList =
        (ArrayList<BatchCopyCoach>) getArguments().get("coachList");

    if (dataList == null || dataList.size() == 0) {
      datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无可选教练"));
      setDatas(datas, 1);
      return;
    }
    List<CommonUserItem> staffItems = new ArrayList<>();
    if (dataList != null && dataList.size() > 0) {
      for (BatchCopyCoach coach : dataList) {
        try {
          staffItems.add(generateItem(coach));
        } catch (Exception e) {
          CrashUtils.sendCrash(e);
        }
      }
      setDatas(staffItems, 1);
    }

    //RxRegiste(staffModel.getTrainers()
    //    .onBackpressureLatest()
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .map( qcResponse -> {
    //      List<CommonUserItem> staffItems = new ArrayList<>();
    //      if (ResponseConstant.checkSuccess(qcResponse)) {
    //        if (qcResponse.data.staffships != null && qcResponse.data.staffships.size() > 0) {
    //          qcResponse.data.staffships.add(0, createAllShip());
    //          for (StaffShip coach : qcResponse.data.staffships) {
    //            try {
    //              coach.id = coach.user.id;
    //              staffItems.add(generateItem(coach));
    //            } catch (Exception e) {
    //              CrashUtils.sendCrash(e);
    //            }
    //          }
    //        }else if (qcResponse.data.staffships != null && qcResponse.data.staffships.size() == 0){
    //          datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无可选教练"));
    //        }
    //      } else {
    //        onShowError(qcResponse.getMsg());
    //      }
    //      return staffItems;
    //    })
    //    .subscribe(new NetSubscribe<List<CommonUserItem>>() {
    //      @Override public void onNext(List<CommonUserItem> commonUserItems) {
    //        setDatas(commonUserItems, 1);
    //      }
    //    }));
  }

  @Override public boolean onItemClick(int position) {
    //return super.onItemClick(position);
    if (commonFlexAdapter.getItem(position) instanceof CommonUserItem) {
      if (((CommonUserItem) commonFlexAdapter.getItem(
          position)).getUser() instanceof BatchCopyCoach) {
        RxBus.getBus()
            .post(new BatchCoachEvent(
                (BatchCopyCoach) ((CommonUserItem) commonFlexAdapter.getItem(position)).getUser()));
      }
    }
    popBack();
    return true;
  }
}
