package cn.qingchengfit.student.view.choose;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.item.ChooseStaffItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.usercase.FilterUserCase;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/5.
 */

public class ChooseStaffViewModel
    extends FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseStaffItem, Map<String, Object>> {
  public final ObservableField<List<StudentItem>> items = new ObservableField<>();
  public final MutableLiveData<List<QcStudentBean>> selectStudents = new MutableLiveData<>();
  public final ObservableInt bottomTextCount=new ObservableInt();
  public final ObservableBoolean isLoading = new ObservableBoolean(false);
  public final ActionLiveEvent sortEvent = new ActionLiveEvent();

  public SortViewModel getSortViewModel() {
    return sortViewModel;
  }

  public final ActionLiveEvent completeClick = new ActionLiveEvent();
  public final ActionLiveEvent bottomClick = new ActionLiveEvent();

  SortViewModel sortViewModel;
  @Inject StudentRepository respository;



  public MutableLiveData<String> getEditAfterTextChange() {
    return editAfterTextChange;
  }

  private MutableLiveData<String> editAfterTextChange = new MutableLiveData<>();

  /**
   * 输入框输入监听
   */
  public void onEditTextChange(String text) {
    editAfterTextChange.setValue(text);
  }

  public void setShopid(String shopid) {
    this.shopid = shopid;
  }

  String shopid;

  @Inject FilterUserCase filterUserCase;

  @Inject public ChooseStaffViewModel() {
    sortViewModel = new SortViewModel();
    selectStudents.setValue(new ArrayList<>());
    sortViewModel.setListener(items1 -> {
      items.set((List<StudentItem>) items1);
      sortEvent.call();
    });
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull Map<String,  Object> map) {
    isLoading.set(true);

    map.put("show_all",1);
    return Transformations.map(respository.qcGetAllStudents( map),
        input -> {
          StudentListWrapper studentListWrapper = dealResource(input);
          if(studentListWrapper!=null)return studentListWrapper.users;
          return null;
        });
  }

  @Override public void loadSource(@NonNull Map<String, Object> stringMap) {
    this.identifier.setValue(stringMap);
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBeanWithFollow> studentBeans) {
    return studentBeans != null && !studentBeans.isEmpty();
  }

  /**
   * 刷新数据
   */
  public void refresh() {
    this.identifier.setValue(identifier.getValue());
  }

  /**
   * 完成按钮点击事件
   */
  public void onComplete() {
    completeClick.call();
  }

  /**
   * 底部展示选中的按钮点击事件
   */
  public void onBottomShowSelected() {
    bottomClick.call();
  }

  @Override protected List<ChooseStaffItem> map(@NonNull List<QcStudentBeanWithFollow> studentBeans) {

    return FlexibleItemProvider.with(new ChooseStaffItemFactory(0)).from(studentBeans);
  }

  static class ChooseStaffItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseStaffItem> {
    private Integer type;

    public ChooseStaffItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseStaffItem create(QcStudentBeanWithFollow qcStudentBean) {
      return FlexibleFactory.create(ChooseStaffItem.class, qcStudentBean, type);
    }
  }
}
