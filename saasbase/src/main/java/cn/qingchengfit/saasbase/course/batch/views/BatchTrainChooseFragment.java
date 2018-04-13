package cn.qingchengfit.saasbase.course.batch.views;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseFragment;
import cn.qingchengfit.saasbase.staff.items.CommonUserItem;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CrashUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2018/4/12.
 */

@Leaf(module = "course", path = "/batch/choose/trainer")
public class BatchTrainChooseFragment extends TrainerChooseFragment {

  @Need
  public String start;
  @Need
  public String end;

  @Override public void initData() {
    RxRegiste(staffModel.getTrainers()
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map( qcResponse -> {
          List<CommonUserItem> staffItems = new ArrayList<>();
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.staffships != null) {
              qcResponse.data.staffships.add(0, createAllCoachData());
              for (StaffShip coach : qcResponse.data.staffships) {
                try {
                  coach.id = coach.user.id;
                  staffItems.add(generateItem(coach));
                } catch (Exception e) {
                  CrashUtils.sendCrash(e);
                }
              }
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
          return staffItems;
        })
        .subscribe(new NetSubscribe<List<CommonUserItem>>() {
          @Override public void onNext(List<CommonUserItem> commonUserItems) {
            setDatas(commonUserItems, 1);
          }
        }));
  }

  private StaffShip createAllCoachData(){
    StaffShip ship = new StaffShip();
    Staff user = new Staff();
    user.setId("0");
    ship.setUser(user);
    ship.setAvatar("http://zoneke-img.b0.upaiyun.com/3fedf1fabde72ee00b8dd08e5547aa57.png");
    ship.setUsername("全部教练");
    return ship;
  }

}
