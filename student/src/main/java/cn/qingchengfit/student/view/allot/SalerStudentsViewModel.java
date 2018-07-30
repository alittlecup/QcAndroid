package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.CommonItemFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SalerStudentsViewModel
    extends FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseDetailItem, Map<String, ?>> {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject StudentRepository studentRepository;

  public List<QcStudentBeanWithFollow> getStudentBeans() {
    return studentBeans;
  }

  private List<QcStudentBeanWithFollow> studentBeans;

  public int type;

  public void setSalerId(String salerId) {
    this.salerId = salerId;
  }

  private String salerId;

  @Inject SalerStudentsViewModel() {

  }

  @NonNull @Override
  protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull Map<String, ?> map) {
    HashMap<String, Object> params = gymWrapper.getParams();

    params.put("show_all", 1);
    if (!map.isEmpty()) {
      params.putAll(map);
      if (params.containsKey("status_ids")) {
        params.put("status", params.get("status_ids"));
        params.remove("status_ids");
      }
    }
    String path = "";
    switch (type) {
      case 0:
        path = "sellers";
        if (!TextUtils.isEmpty(salerId)) {
          params.put("seller_id", salerId);
        }
        break;
      case 1:
        path = "coaches";
        if (!TextUtils.isEmpty(salerId)) {
          params.put("coach_id", salerId);
        }
        break;
    }
    return Transformations.map(
        studentRepository.qcGetAllotStaffMembers(loginStatus.staff_id(), path, params), input -> {
          StudentListWrapper studentListWrapper = dealResource(input);
          if (studentListWrapper == null) {
            return null;
          } else {
            return studentListWrapper.users;
          }
        });
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBeanWithFollow> qcStudentBeans) {
    return qcStudentBeans != null;
  }

  @Override protected List<ChooseDetailItem> map(@NonNull List<QcStudentBeanWithFollow> qcStudentBeans) {
    this.studentBeans = qcStudentBeans;
    return FlexibleItemProvider.with(new StaffDetailItemFactory(type)).from(qcStudentBeans);
  }

  static class StaffDetailItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {
    private Integer type;

    public StaffDetailItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseDetailItem create(QcStudentBeanWithFollow qcStudentBean) {
      return FlexibleFactory.create(ChooseDetailItem.class, qcStudentBean, type);
    }
  }
}
