package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.Utils;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/5.
 */

public class StudentHomeViewModel extends BaseViewModel {
  public final MediatorLiveData<String> increaseMembers = new MediatorLiveData<>();
  public final MediatorLiveData<String> increaseStudents = new MediatorLiveData<>();
  public final MediatorLiveData<String> increaseFollowers = new MediatorLiveData<>();
  public final MediatorLiveData<String> followStudents = new MediatorLiveData<>();
  public final MediatorLiveData<String> totalMembers = new MediatorLiveData<>();
  public final MediatorLiveData<String> register = new MediatorLiveData<>();
  public final MediatorLiveData<String> follow = new MediatorLiveData<>();
  public final MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
  public final MediatorLiveData<String> member = new MediatorLiveData<>();
  public final MediatorLiveData<String> birthDayMsg = new MediatorLiveData<>();
  public LiveData<StudentInfoGlance> glanceLiveData;
  @Inject LoginStatus loginStatus;
  @Inject StudentRepository repository;
  @Inject GymWrapper gymWrapper;
  private ActionLiveEvent loadSourceAction = new ActionLiveEvent();

  @Inject public StudentHomeViewModel() {
    glanceLiveData = Transformations.switchMap(loadSourceAction,
        aVoid -> Transformations.map(loadSourceFromRes(), input -> {
          showLoading.setValue(false);
          return dealResource(input);
        }));
    increaseMembers.addSource(glanceLiveData, info -> increaseMembers.setValue(
        String.valueOf(info == null ? "" : info.getNew_register_users_count())));
    increaseStudents.addSource(glanceLiveData, info -> increaseStudents.setValue(
        String.valueOf(info == null ? "" : info.getNew_member_users_count())));
    increaseFollowers.addSource(glanceLiveData, info -> increaseFollowers.setValue(
        String.valueOf(info == null ? "" : info.getNew_follow_users_count())));
    followStudents.addSource(glanceLiveData, info -> followStudents.setValue(
        String.valueOf(info == null ? "" : info.getNew_follow_member_users_count())));

    register.addSource(glanceLiveData, info -> register.setValue(
        String.valueOf(info == null ? "" : info.getRegistered_users_count()) + "人"));
    follow.addSource(glanceLiveData, info -> follow.setValue(
        String.valueOf(info == null ? "" : info.getFollowing_users_count()) + "人"));
    member.addSource(glanceLiveData, info -> member.setValue(
        String.valueOf(info == null ? "" : info.getMember_users_count()) + "人"));
    totalMembers.addSource(glanceLiveData, info -> {
      totalMembers.setValue(String.valueOf(info == null ? "" : info.getAll_users_count()));
    });
    birthDayMsg.addSource(glanceLiveData, info -> {
      if (info == null) return;
      List<String> today_birthday_users = info.getToday_birthday_users();
      if (today_birthday_users != null && !today_birthday_users.isEmpty()) {
        if (today_birthday_users.size() > 1) {
          birthDayMsg.setValue(today_birthday_users.get(0) + "等今日生日");
        } else {
          birthDayMsg.setValue(today_birthday_users.get(0) + "今日生日");
        }
      }else{
        birthDayMsg.setValue("");
      }
    });
  }

  void loadSource() {
    loadSourceAction.call();
    showLoading.setValue(true);
  }

  public void onRefresh() {
    loadSource();
  }

  private LiveData<Resource<StudentInfoGlance>> loadSourceFromRes() {
    return repository.qcGetHomePageInfo(loginStatus.staff_id(), gymWrapper.getParams());
  }
}
