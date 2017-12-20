package cn.qingchengfit.saasbase.course.batch.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.items.BatchCateItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.DateUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 2017/11/29.
 */
@Leaf(module = "course",path = "/batch/cate/private/")
public class BatchListCategoryPrivateFragment extends IBatchListCategoryFragment {

  @Need String trainer_id;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("私教排期");
  }


  @Override public void onRefresh() {
    RxRegiste(courseModel.qcGetPrivateCoaches(trainer_id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<QcResponsePrivateDetail>>() {
        @Override public void onNext(QcDataResponse<QcResponsePrivateDetail> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.batches != null){
              List<AbstractFlexibleItem> datas = new ArrayList<>();
              datas.add(new BatchItem(qcResponse.data.coach));
              for (QcResponsePrivateDetail.PrivateBatch coach : qcResponse.data.batches) {
                try {
                  datas.add(new BatchCateItem(DateUtils.getDuringFromServer(coach.from_date,coach.to_date)
                    ,coach.course.getName(),coach.id,coach.course.getPhoto()
                  ));
                }catch (Exception e){
                  CrashUtils.sendCrash(e);
                }
              }
              setDatas(datas,1);
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));

  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item == null) return true;
    if (item instanceof BatchCateItem){
      routeTo("/batch/edit/",new cn.qingchengfit.saasbase.course.batch.views.EditBatchParams()
        .batchId(((BatchCateItem) item).getId())
        .isPrvite(true)
        .build()
      );
    }
    return true;
  }
}
