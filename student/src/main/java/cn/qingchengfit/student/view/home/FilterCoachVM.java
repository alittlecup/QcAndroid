package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.filter.ItemFilterCommon;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.student.usercase.FilterCoachUserCase;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class FilterCoachVM extends StudentFilterViewModel {

  @Inject FilterCoachUserCase coachUserCase;
  public MutableLiveData<Integer> changePosition = new MutableLiveData<>();

  @Inject public FilterCoachVM() {
  }

  @Override public void resetFilterUserCase() {
    setFilterUserCase(coachUserCase);
  }

  //初始筛选项
  public void setFilterItem(String name, String value) {
    if (!value.isEmpty()) {
      for (int j = 0; j < items.getValue().size(); j++) {
        AbstractFlexibleItem item = items.getValue().get(j);
        if (item instanceof ItemFilterCommon) {
          FilterModel model = ((ItemFilterCommon) item).getData();
          if (model.name.equals(name)) {
            for (int i = 0; i < model.content.size(); i++) {
              if (model.content.get(i).value.equals(value)) {
                List<Integer> positionList = new ArrayList<>();
                positionList.add(i);
                ((ItemFilterCommon)items.getValue().get(j)).setSelectPosition(positionList);
                filterMap.put(model.key, value);
                changePosition.setValue(j);
                break;
              }
            }
            break;
          }
        }
      }
      onConfirm();
    }
  }
}
