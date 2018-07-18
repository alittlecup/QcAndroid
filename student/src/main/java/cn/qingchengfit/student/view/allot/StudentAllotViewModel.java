package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.item.ChooseStaffItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentAllotViewModel
    extends FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseStaffItem, Map<String, ?>> {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository respository;
  public int type;
  public String salerId;

  public void setSalerId(String salerId) {
    this.salerId = salerId;
  }

  @Inject public StudentAllotViewModel() {

  }

  @NonNull @Override
  protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull Map<String, ?> map) {
    HashMap<String, Object> params = gymWrapper.getParams();
    if (!TextUtils.isEmpty(salerId)) {
      params.put("seller_id", salerId);
    }
    params.put("show_all", 1);

    if (!map.isEmpty()) {
      params.putAll(map);
    }

    String path = "";
    switch (type) {
      case 0:
        path = "sellers";
        break;
      case 1:
        path = "coaches";
        break;
    }
    return Transformations.map(respository.qcGetAllotStaffMembers(loginStatus.staff_id(), path, params), input -> {
      StudentListWrapper studentListWrapper = dealResource(input);
      if(studentListWrapper==null){
        return null;
      }else{
       return studentListWrapper.users;
      }
    });
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBeanWithFollow> qcStudentBeans) {
    return qcStudentBeans != null;
  }

  @Override protected List<ChooseStaffItem> map(@NonNull List< QcStudentBeanWithFollow> qcStudentBeans) {
    return FlexibleItemProvider.with(new ChooseStaffItemFactory(type)).from(qcStudentBeans);
  }

  static class ChooseStaffItemFactory
      implements FlexibleItemProvider.Factory< QcStudentBeanWithFollow, ChooseStaffItem> {
    private Integer type;

    public ChooseStaffItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseStaffItem create( QcStudentBeanWithFollow qcStudentBean) {
      return FlexibleFactory.create(ChooseStaffItem.class, qcStudentBean, type);
    }
  }
}
