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
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.item.StaffDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.view.home.StudentAllViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SalerStudentsViewModel
    extends FlexibleViewModel<List<QcStudentBean>, StaffDetailItem, Map<String, ?>> {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject StudentRepository studentRepository;

  public List<QcStudentBean> getStudentBeans() {
    return studentBeans;
  }

  private List<QcStudentBean> studentBeans;

  public int type;

  public void setSalerId(String salerId) {
    this.salerId = salerId;
  }

  private String salerId;

  @Inject SalerStudentsViewModel() {

  }

  @NonNull @Override
  protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, ?> map) {
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

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBean> qcStudentBeans) {
    return qcStudentBeans != null;
  }

  @Override protected List<StaffDetailItem> map(@NonNull List<QcStudentBean> qcStudentBeans) {
    this.studentBeans = qcStudentBeans;
    return FlexibleItemProvider.with(new StaffDetailItemFactory(type)).from(qcStudentBeans);
  }

  static class StaffDetailItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBean, StaffDetailItem> {
    private Integer type;

    public StaffDetailItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public StaffDetailItem create(QcStudentBean qcStudentBean) {
      return FlexibleFactory.create(StaffDetailItem.class, qcStudentBean, type);
    }
  }
}
