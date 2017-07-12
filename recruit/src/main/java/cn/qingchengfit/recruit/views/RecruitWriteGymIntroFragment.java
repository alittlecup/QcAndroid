package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventGymFacilities;
import cn.qingchengfit.recruit.network.body.RecruitGymBody;
import cn.qingchengfit.recruit.presenter.RecruitGymPresenter;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/7/4.
 */
@FragmentWithArgs public class RecruitWriteGymIntroFragment extends BaseFragment
    implements RecruitGymPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.civ_gym_area) CommonInputView civGymArea;
  @BindView(R2.id.civ_gym_staff_count) CommonInputView civGymStaffCount;
  @BindView(R2.id.civ_gym_trainer_count) CommonInputView civGymTrainerCount;
  @BindView(R2.id.civ_gym_member_count) CommonInputView civGymMemberCount;
  @BindView(R2.id.civ_gym_equip) CommonInputView civGymEquip;
  @BindView(R2.id.civ_gym_intro) CommonInputView civGymIntro;

  @Inject RecruitGymPresenter gymPresenter;
  @Inject RecruitRouter router;

  @Arg Gym gym;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitWriteGymIntroFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_write_gym_desc, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(gymPresenter, this);
    onDetail(gym);
    initBus();
    return view;
  }

  private void initBus() {
    RxBusAdd(EventGymFacilities.class).subscribe(new Action1<EventGymFacilities>() {
      @Override public void call(EventGymFacilities eventGymFacilities) {
        gym.facilities = eventGymFacilities.facilities;
        civGymEquip.setContent(CmStringUtils.List2Str(gym.facilities));
      }
    });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("场馆介绍");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        RecruitGymBody.Builder body = new RecruitGymBody.Builder();
        if (!civGymArea.isEmpty()) body.area(civGymArea.getContent());
        if (!civGymMemberCount.isEmpty()) {
          body.member_count(Integer.parseInt(civGymMemberCount.getContent()));
        }
        if (!civGymStaffCount.isEmpty()) {
          body.staff_count(Integer.parseInt(civGymStaffCount.getContent()));
        }
        if (!civGymTrainerCount.isEmpty()) {
          body.coach_count(Integer.parseInt(civGymTrainerCount.getContent()));
        }
        if (!TextUtils.isEmpty(gym.detail_description)) {
          body.detail_description(gym.detail_description);
        }
        if (!ListUtils.isEmpty(gym.facilities)) body.facilities(gym.facilities);
        showLoading();
        gymPresenter.saveInfo(gym.id, body.build());
        return false;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    gymPresenter.queryGymInfo(gym.id);
  }

  @Override public String getFragmentName() {
    return RecruitWriteGymIntroFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 健身房设备
   */
  @OnClick(R2.id.civ_gym_equip) public void onCivGymEquipClicked() {
    router.toGymEquip(ListUtils.list2array(gym.facilities));
  }

  /**
   * 详细介绍  富文本
   */
  @OnClick(R2.id.civ_gym_intro) public void onCivGymIntroClicked() {
    router.toWriteGymDetailDesc(gym.detail_description);
  }

  @Override public void onSaveOk() {

  }

  @Override public void onDetail(Gym gym) {
    if (gym == null) return;
    civGymArea.setContent(gym.area + "");
    civGymMemberCount.setContent(gym.member_count + "");
    civGymStaffCount.setContent(gym.staff_count + "");
    civGymTrainerCount.setContent(gym.coach_count + "");
    civGymEquip.setContent(CmStringUtils.List2Str(gym.facilities));
    civGymIntro.setContent(TextUtils.isEmpty(gym.detail_description) ? "" : "详情");
  }
}
