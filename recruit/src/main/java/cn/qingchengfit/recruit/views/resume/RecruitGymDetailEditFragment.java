//package cn.qingchengfit.recruit.views.resume;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.model.base.Gym;
//import cn.qingchengfit.recruit.R;
//
//import cn.qingchengfit.recruit.RecruitRouter;
//import cn.qingchengfit.recruit.presenter.RecruitGymPresenter;
//import cn.qingchengfit.utils.CmStringUtils;
//import cn.qingchengfit.utils.ListUtils;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.widgets.CommonInputView;
//import java.util.ArrayList;
//import javax.inject.Inject;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/6/28.
// */
//public class RecruitGymDetailEditFragment extends BaseFragment
//    implements RecruitGymPresenter.MVPView {
//
//  @BindView(R2.id.toolbar) Toolbar toolbar;
//  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
//  @BindView(R2.id.civ_gym_area) CommonInputView civGymArea;
//  @BindView(R2.id.civ_gym_staff_count) CommonInputView civGymStaffCount;
//  @BindView(R2.id.civ_gym_trainer_count) CommonInputView civGymTrainerCount;
//  @BindView(R2.id.civ_gym_member_count) CommonInputView civGymMemberCount;
//  @BindView(R2.id.civ_gym_equip) CommonInputView civGymEquip;
//  @BindView(R2.id.civ_gym_intro) CommonInputView civGymIntro;
//
//  @Inject RecruitGymPresenter presenter;
//  @Inject RecruitRouter router;
//
//  private Gym gym;
//
//  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
//      Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.fragment_recruit_write_gym_desc, container, false);
//    unbinder = ButterKnife.bind(this, view);
//    delegatePresenter(presenter, this);
//    return view;
//  }
//
//  @Override public void initToolbar(@NonNull Toolbar toolbar) {
//    super.initToolbar(toolbar);
//    toolbarTitle.setText("场馆介绍");
//    toolbar.inflateMenu(R.menu.menu_save);
//    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//      @Override public boolean onMenuItemClick(MenuItem item) {
//        return false;
//      }
//    });
//  }
//
//  @Override public String getFragmentName() {
//    return RecruitGymDetailEditFragment.class.getName();
//  }
//
//  @Override public void onDestroyView() {
//    super.onDestroyView();
//  }
//
//  /**
//   * 会场设施
//   */
//  @OnClick(R2.id.civ_gym_equip) public void onCivGymEquipClicked() {
//    router.toGymEquip((ArrayList<String>) gym.facilities);
//  }
//
//  /**
//   * 详细介绍
//   */
//  @OnClick(R2.id.civ_gym_intro) public void onCivGymIntroClicked() {
//    router.toWriteGymIntro(gym);
//  }
//
//  @Override public void onSaveOk() {
//    hideLoading();
//    ToastUtils.show("保存成功");
//    getActivity().onBackPressed();
//  }
//
//  @Override public void onDetail(Gym gym) {
//    this.gym = gym;
//    civGymArea.setContent(gym.area + "m²");
//    civGymMemberCount.setContent(gym.member_count + "人");
//    civGymStaffCount.setContent(gym.staff_count + "人");
//    civGymTrainerCount.setContent(gym.coach_count + "人");
//    civGymEquip.setContent(
//        ListUtils.isEmpty(gym.facilities) ? "" : CmStringUtils.List2Str(gym.facilities));
//    civGymIntro.setContent(CmStringUtils.isEmpty(gym.detail_description) ? "" : "详情");
//  }
//}
