package cn.qingchengfit.student.usercase;

import cn.qingchengfit.saascommon.filter.model.Content;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.student.bean.CoachStudentFilter;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.google.gson.Gson;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * 教练App会员筛选项
 */
public class FilterCoachUserCase extends FilterUserCase {

  @Inject public FilterCoachUserCase(){

  }

  @Override public void excute(String id, String sellerId, HashMap<String, Object> params) {
    List<FilterModel> filterList = new ArrayList<>(
        new Gson().fromJson(FileUtils.getJsonFromAssert("coachfilter.json", application),
            FilterWrapper.class).filters);

    remoteService.qcGetCoachStudentPtagFilter().subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).map(coachStudentFilterWrapperQcDataResponse -> {
          List<FilterModel> tempList = new ArrayList<>();
          if (coachStudentFilterWrapperQcDataResponse.status == 200){
            tempList.add(assemblyFilter(coachStudentFilterWrapperQcDataResponse.data.getMEMBER_TRAINING_FEEDBACK(), "ptag_training_feedback_type"));
            tempList.add(assemblyFilter(coachStudentFilterWrapperQcDataResponse.data.getMEMBER_TRAIN_OBJECTIVES(), "ptag_training_objective_type"));
          }else{
            ToastUtils.show(coachStudentFilterWrapperQcDataResponse.msg);
          }
          return tempList;
        }).subscribe(filterModels -> {
          filterList.addAll(filterModels);
          filterModel.setValue(filterList);
        });
  }

  private FilterModel assemblyFilter(CoachStudentFilter remoteFilter, String key){
    FilterModel filterModel = new FilterModel();
    filterModel.name = remoteFilter.getTitle();
    filterModel.type = 1;
    filterModel.key = key;
    List<Content> contentList = new ArrayList<>();
    for (CoachStudentFilter.Filter filter : remoteFilter.getFilters()){
      contentList.add(new Content(filter.getName(), filter.getId(), null));
    }
    filterModel.content = contentList;
    return filterModel;
  }
}
