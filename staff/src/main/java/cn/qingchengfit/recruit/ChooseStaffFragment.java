package cn.qingchengfit.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.network.response.ChatGymWrap;
import cn.qingchengfit.saas.network.GetApi;
import cn.qingchengfit.saasbase.chat.ConversationFriendsFragment;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import cn.qingchengfit.saasbase.common.bottom.BottomStudentsFragment;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.staff.event.EventAddStaffDone;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventChoosePerson;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 2017/7/6.
 */

public class ChooseStaffFragment extends ConversationFriendsFragment {
  @Inject QcRestRepository qcRestRepository;
  @Inject GymWrapper gymWrapper;
  @Inject QcDbManager qcDbManager;
  @Inject GymFunctionFactory gymFunctionFactory;
  private ChatGym chatGym;
  //@Inject QCDbManager qcDbManager;

  public static ChooseStaffFragment newInstance(ChatGym chatGym) {
    Bundle args = new Bundle();
    args.putParcelable("gym", chatGym);
    ChooseStaffFragment fragment = new ChooseStaffFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      chatGym = getArguments().getParcelable("gym");
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择工作人员");
    toolbar.getMenu().clear();
    toolbar.inflateMenu(R.menu.menu_add_txt);
    toolbar.setOnMenuItemClickListener(item -> {
      RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
          .querySu(chatGym.id)
          .onBackpressureBuffer()
          .flatMap(suWrapQcDataResponse -> {
            if (suWrapQcDataResponse.status == 200) {
              if (suWrapQcDataResponse.data.is_superuser) {
                CoachService service = qcDbManager.getGymByGymIdNow(chatGym.id);
                List<CoachService> coachServices = new ArrayList<>();
                coachServices.add(service);
                return Observable.just(coachServices);
              } else {

              }
            } else {
              getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                  ToastUtils.show(suWrapQcDataResponse.getMsg());
                }
              });
            }
            List<CoachService> ret = new ArrayList<CoachService>();
            return Observable.just(ret);
          })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<List<CoachService>>() {
            @Override public void call(List<CoachService> coachServices) {
              if (coachServices.size() > 0) {
                //Intent toStatement = new Intent(getActivity(), ContainerActivity.class);
                //toStatement.putExtra("router", GymFunctionFactory.MODULE_MANAGE_STAFF_ADD);
                //toStatement.putExtra("")
                //startActivity(toStatement);
                gymWrapper.setCoachService(coachServices.get(0));
                gymFunctionFactory.getJumpIntent(QRActivity.MODULE_MANAGE_STAFF_ADD,
                    coachServices.get(0), null, null, ChooseStaffFragment.this);
              } else {
                ToastUtils.show("找不到对应场馆");
              }
            }
          }, new NetWorkThrowable()));
      return false;
    });
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
    }
  }

  @Override protected void initView() {
    getChildFragmentManager().beginTransaction()
        .replace(R.id.chat_friend_frag, ChooseStaffInGymFragment.newInstance(chatGym))
        .commit();
    RxBusAdd(EventChoosePerson.class).subscribe(new Action1<EventChoosePerson>() {
      @Override public void call(EventChoosePerson eventChoosePerson) {
        tvAllotsaleSelectCount.setText(
            DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + "");
      }
    });
    tvAllotsaleSelectCount.setText(
        DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + "");

    RxBusAdd(EventAddStaffDone.class).flatMap(
        new Func1<EventAddStaffDone, Observable<QcDataResponse<ChatGymWrap>>>() {
          @Override
          public Observable<QcDataResponse<ChatGymWrap>> call(EventAddStaffDone eventAddStaffDone) {
            return qcRestRepository.createRxJava1Api(cn.qingchengfit.recruit.network.GetApi.class)
                .queryCommonStaffs(chatGym.id)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<ChatGymWrap>>() {
          @Override public void call(QcDataResponse<ChatGymWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              chatGym.staffs = qcResponse.data.users.staffs;
              chatGym.coaches = qcResponse.data.users.coaches;
              getChildFragmentManager().beginTransaction()
                  .replace(R.id.chat_friend_frag, ChooseStaffInGymFragment.newInstance(chatGym))
                  .commit();
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable());
  }

  @Override public void onViewClicked() {
    BottomStaffsBanRvSuFragment selectSutdentFragment = new BottomStaffsBanRvSuFragment();
    selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
      @Override public void onBottomStudents(List<Personage> list) {
        DirtySender.studentList.clear();
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
        RxBus.getBus().post(new EventFresh());
      }
    });
    selectSutdentFragment.setDatas(DirtySender.studentList);
    selectSutdentFragment.show(getFragmentManager(), "");
  }

  @Override public void onDone() {
    Intent ret = new Intent();
    ret.putExtra("ids", (ArrayList<String>) BusinessUtils.PersonIdsExSu(DirtySender.studentList));
    getActivity().setResult(Activity.RESULT_OK, ret);
    DirtySender.studentList.clear();
    getActivity().finish();
  }

  @Override public void onDestroyView() {
    //必须是在全局情况下
    gymWrapper.setCoachService(null);
    super.onDestroyView();
  }
}
