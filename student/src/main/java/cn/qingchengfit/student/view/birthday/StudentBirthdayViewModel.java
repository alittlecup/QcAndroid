package cn.qingchengfit.student.view.birthday;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saascommon.calendar.Calendar;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.QcStudentWithUsers;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.DateUtils;
import com.airbnb.lottie.L;
import com.google.zxing.aztec.detector.Detector;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentBirthdayViewModel
    extends FlexibleViewModel<QcStudentBirthdayWrapper, ChooseDetailItem, Map<String, Object>> {

  @Inject StudentRepository studentRepository;
  public final MutableLiveData<String> date = new MutableLiveData<>();
  public final MutableLiveData<String> count = new MutableLiveData<>();
  public final LiveData<List<String>> dates;
  public final MutableLiveData<Calendar> selectedCalendar = new MutableLiveData<>();
  private final MutableLiveData<Map<String, Object>> loadDates = new MutableLiveData<>();

  @Inject public StudentBirthdayViewModel() {
    dates = Transformations.switchMap(loadDates,
        dates -> Transformations.map(getSource(dates), input -> {
          List<String> datess = new ArrayList<>();
          if (input == null) return datess;
          List<QcStudentWithUsers> birthday = input.getBirthday();
          if (birthday == null) return datess;
          for (QcStudentWithUsers users : birthday) {
            if (users.getCount() > 0) {
              datess.add(users.getDate());
            }
          }
          return datess;
        }));
  }

  public void loadSchemeDates(Map<String, Object> params) {
    loadDates.setValue(params);
  }

  @NonNull @Override
  protected LiveData<QcStudentBirthdayWrapper> getSource(@NonNull Map<String, Object> params) {

    return Transformations.map(studentRepository.qcGetStudentBirthday(params), this::dealResource);
  }

  @Override
  protected boolean isSourceValid(@Nullable QcStudentBirthdayWrapper qcStudentBirthdayWrapper) {
    return qcStudentBirthdayWrapper != null;
  }

  @Override
  protected List<ChooseDetailItem> map(@NonNull QcStudentBirthdayWrapper qcStudentBirthdayWrapper) {
    FollowUpItemFactory itemFactory = new FollowUpItemFactory(-1);
    List<ChooseDetailItem> items = new ArrayList<>();
    List<QcStudentWithUsers> birthday = qcStudentBirthdayWrapper.getBirthday();
    for (QcStudentWithUsers users : birthday) {
      List<ChooseDetailItem> from = FlexibleItemProvider.with(itemFactory).from(users.getUsers());
      if (!from.isEmpty()) {
        from.get(0).setHeader(new StickerDateItem(users.getDate()));
        items.addAll(from);
      }
    }
    if (birthday.size() > 7) {
      String date = birthday.get(0).getDate();
      Date date1 = DateUtils.formatDateFromYYYYMMDD(date);
      java.util.Calendar calendar = java.util.Calendar.getInstance();
      calendar.setTime(date1);
      int i = calendar.get(java.util.Calendar.MONTH);
      this.date.setValue((i + 1) + "月生日");
      count.setValue(qcStudentBirthdayWrapper.getTotal_count() + "");
    } else {
      this.date.setValue(
          birthday.get(0).getDate() + "至" + birthday.get(birthday.size() - 1).getDate() + "生日");
      count.setValue(qcStudentBirthdayWrapper.getTotal_count() + "");
    }
    return items;
  }

  static class FollowUpItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {
    private Integer type;

    public FollowUpItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseDetailItem create(QcStudentBeanWithFollow beanWithFollow) {
      return FlexibleFactory.create(ChooseDetailItem.class, beanWithFollow, type);
    }
  }
}
