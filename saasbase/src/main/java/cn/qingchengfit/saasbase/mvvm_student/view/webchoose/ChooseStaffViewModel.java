package cn.qingchengfit.saasbase.mvvm_student.view.webchoose;

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
import cn.qingchengfit.saasbase.common.mvvm.SortViewModel;
import cn.qingchengfit.saasbase.mvvm_student.items.ChooseStaffItem;
import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRespository;
import cn.qingchengfit.saasbase.mvvm_student.usercase.FilterUserCase;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/5.
 */

public class ChooseStaffViewModel
    extends FlexibleViewModel<List<QcStudentBean>, ChooseStaffItem, Map<String, ? extends Object>> {
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
  @Inject LoginStatus loginStatus;
  @Inject StudentRespository respository;
  @Inject GymWrapper gymWrapper;



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
      items.set(items1);
      sortEvent.call();
    });
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, ? extends Object> map) {
    isLoading.set(true);
    HashMap<String, Object> params = new HashMap<>();


    if (!map.isEmpty()) {
      params.putAll(map);
    }

    return Transformations.map(respository.qcGetAllStudents(loginStatus.staff_id(), params),
        input -> input.users);
  }

  @Override public void loadSource(@NonNull Map<String, ?> stringMap) {
    this.identifier.setValue(stringMap);
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBean> studentBeans) {
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

  @Override protected List<ChooseStaffItem> map(@NonNull List<QcStudentBean> studentBeans) {

    return FlexibleItemProvider.with(new ChooseStaffItemFactory(0)).from(studentBeans);
  }

  static class ChooseStaffItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBean, ChooseStaffItem> {
    private Integer type;

    public ChooseStaffItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseStaffItem create(QcStudentBean qcStudentBean) {
      return FlexibleFactory.create(ChooseStaffItem.class, qcStudentBean, type);
    }
  }
}
