package cn.qingchengfit.recruit.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.FragmentRecruitWriteGymDescBinding;
import cn.qingchengfit.recruit.event.EventGymFacilities;
import cn.qingchengfit.recruit.event.EventRichTextBack;
import cn.qingchengfit.recruit.network.body.RecruitGymBody;
import cn.qingchengfit.recruit.presenter.RecruitGymPresenter;
import cn.qingchengfit.recruit.views.resume.RecruitGymEquipFragment;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

/*  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.civ_gym_area) CommonInputView civGymArea;
  @BindView(R2.id.civ_gym_staff_count) CommonInputView civGymStaffCount;
  @BindView(R2.id.civ_gym_trainer_count) CommonInputView civGymTrainerCount;
  @BindView(R2.id.civ_gym_member_count) CommonInputView civGymMemberCount;
  @BindView(R2.id.civ_gym_equip) CommonInputView civGymEquip;
  @BindView(R2.id.civ_gym_intro) CommonInputView civGymIntro;*/

  @Inject RecruitGymPresenter gymPresenter;
  @Inject RecruitRouter router;

  @Arg Gym gym;
  FragmentRecruitWriteGymDescBinding db;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitWriteGymIntroFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_recruit_write_gym_desc, container, false);

    initToolbar(db.layoutToolbar.findViewById(R.id.toolbar));
    delegatePresenter(gymPresenter, this);
    onDetail(gym);
    initBus();
    getFragmentManager().registerFragmentLifecycleCallbacks(cb,false);
    db.civGymEquip.setOnClickListener(view -> onCivGymEquipClicked());
    db.civGymIntro.setOnClickListener(view -> onCivGymIntroClicked());
    return db.getRoot();
  }

  private FragmentManager.FragmentLifecycleCallbacks cb = new FragmentManager.FragmentLifecycleCallbacks() {
    @Override public void onFragmentDetached(FragmentManager fm, Fragment f) {
      super.onFragmentDetached(fm, f);
      if (f instanceof RecruitGymEquipFragment || f instanceof RecruitRichTextEditFragment){
        setBackPress();
      }
    }
  };

  private void initBus() {
    RxBusAdd(EventGymFacilities.class).subscribe(new Action1<EventGymFacilities>() {
      @Override public void call(EventGymFacilities eventGymFacilities) {
        gym.facilities = eventGymFacilities.facilities;
        db.civGymEquip.setContent(CmStringUtils.List2Str(gym.facilities));
      }
    });
    RxBusAdd(EventRichTextBack.class).subscribe(new Action1<EventRichTextBack>() {
      @Override public void call(EventRichTextBack eventRichTextBack) {
        gym.detail_description = eventRichTextBack.content;
        db.civGymIntro.setContent(getString(R.string.detail_text));
      }
    });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    ((TextView)db.layoutToolbar.findViewById(R.id.toolbar_title)).setText("场馆介绍");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        RecruitGymBody.Builder body = new RecruitGymBody.Builder();
        if (!db.civGymArea.isEmpty()) body.area(db.civGymArea.getContent());
        if (!db.civGymMemberCount.isEmpty()) {
          body.member_count(Integer.parseInt(db.civGymMemberCount.getContent()));
        }
        if (!db.civGymStaffCount.isEmpty()) {
          body.staff_count(Integer.parseInt(db.civGymStaffCount.getContent()));
        }
        if (!db.civGymTrainerCount.isEmpty()) {
          body.coach_count(Integer.parseInt(db.civGymTrainerCount.getContent()));
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
    getFragmentManager().unregisterFragmentLifecycleCallbacks(cb);
    setBackPressNull();
    super.onDestroyView();
  }

  /**
   * 健身房设备
   */
  public void onCivGymEquipClicked() {
    router.toGymEquip(ListUtils.list2array(gym.facilities));
  }

  /**
   * 详细介绍
   */
  public void onCivGymIntroClicked() {
    router.toWriteGymDetailDesc(gym.detail_description, "详细介绍", "请填写场馆介绍");
  }

  @Override public void onSaveOk() {
    hideLoading();
    ToastUtils.show("保存成功");
    getActivity().getSupportFragmentManager().popBackStackImmediate();
  }

  @Override public void onDetail(Gym gym) {
    if (gym == null) return;
    //特别蛋疼的为了 返回时添加一个是否 放弃编辑，但是新增的是不会有
    if (gym.name == null) {
      return;
    }
    this.gym = gym;
    int areInt = 0;
    try {
      areInt = (int) Float.parseFloat(gym.area);
    } catch (Exception e) {

    }
    db.civGymArea.setContent(CmStringUtils.getStringFromInt(areInt));
    db.civGymMemberCount.setContent(CmStringUtils.getStringFromInt(gym.member_count));
    db.civGymStaffCount.setContent(CmStringUtils.getStringFromInt(gym.staff_count));
    db.civGymTrainerCount.setContent(CmStringUtils.getStringFromInt(gym.coach_count));
    db.civGymEquip.setContent(CmStringUtils.List2Str(gym.facilities));
    db.civGymIntro.setContent(TextUtils.isEmpty(gym.detail_description) ? "" : "详情");
  }

  @Override public boolean onFragmentBackPress() {
    DialogUtils.instanceDelDialog(getContext(), "确定放弃所做修改？",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            getActivity().getSupportFragmentManager().popBackStackImmediate();
          }
        }).show();
    return true;
  }


}
