package cn.qingchengfit.staffkit.views.student.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.MutiChooseCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachBean;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EditStudentEvent;
import cn.qingchengfit.staffkit.rxbus.event.UpdateEvent;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.student.ChooseOriginActivityIntentBuilder;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.staffkit.views.student.bodytest.BodyTestListFragment;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreDetailActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/19 2016.
 */
public class StudentMoreInfoFragment extends BaseFragment
    implements TitleFragment, StudentMoreInfoView {
  public static final int RESULT_SALAER = 3;
  public static final int RESULT_ORIGIN = 11;
  public static final int RESULT_REFERRER = 12;
  public static final int RESULT_SCORE = 13;
  public static final int RESULT_COACH = 14;

  @BindView(R.id.baseinfo_label) TextView baseinfoLabel;
  @BindView(R.id.bodytest_img) ImageView bodytestImg;
  @BindView(R.id.update_info) TextView updateInfo;
  @BindView(R.id.signin_img) ImageView signinImg;
  @BindView(R.id.no_checkin_img) TextView noCheckinImg;
  @BindView(R.id.sales_name) TextView mSalesName;
  @BindView(R.id.tv_student_source) TextView tvStudentSource;
  @BindView(R.id.tv_student_referrer) TextView tvStudentReferrer;
  @BindView(R.id.tv_student_score_value) TextView tvStudentScoreValue;

  @Inject StudentMoreInfoPresenter presenter;
  @Inject StudentWrapper studentBean;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;

  EditStudentInfoFragment editStudentInfoFragment;
  @BindView(R.id.coach_layout) LinearLayout coachLayout;
  @BindView(R.id.coach_name) TextView coachName;
  @BindView(R.id.body_layout) LinearLayout bodyLayout;
  @BindView(R.id.baseinfo_layout) RelativeLayout baseinfoLayout;
  @BindView(R.id.rl_score) RelativeLayout rlScore;

  private User_Student user_student;
  private Observable<EditStudentEvent> mObEdit;
  private String score;

  @Inject public StudentMoreInfoFragment() {
    editStudentInfoFragment = new EditStudentInfoFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_student_moreinfo, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    onData(new User_Student.Builder().id(studentBean.id())
        .gender(studentBean.gender())
        .avatar(studentBean.avatar())
        .checkin_avatar(studentBean.checkin_avatar())
        .build());
    presenter.queryStudents();
    mObEdit = RxBus.getBus().register(EditStudentEvent.class);
    mObEdit.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EditStudentEvent>() {
      @Override public void call(EditStudentEvent editStudentEvent) {
        if (user_student != null) {
          if (editStudentEvent.getType() == EditStudentEvent.EDIT) {
            editStudentInfoFragment.isAdd = false;
            editStudentInfoFragment.user = user_student;
            getParentFragment().getFragmentManager()
                .beginTransaction().replace(mCallbackActivity.getFragId(), editStudentInfoFragment)
                .addToBackStack(null)
                .commit();
          } else if (editStudentEvent.getType() == EditStudentEvent.DEL) {
            presenter.delStudent(editStudentEvent.getShop_ids());
          }
        }
      }
    });
    RxBusAdd(UpdateEvent.class).subscribe(new Action1<UpdateEvent>() {
      @Override public void call(UpdateEvent eventChooseImage) {
        presenter.queryStudents();
      }
    });
    return view;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
  }

  @OnClick(R.id.baseinfo_layout) public void oneditStudent() {
    boolean hasP = serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE);
    if (hasP) {
      editStudentInfoFragment.isAdd = false;
      editStudentInfoFragment.user = user_student;
      getParentFragment().getFragmentManager()
          .beginTransaction()
          .add(mCallbackActivity.getFragId(), editStudentInfoFragment)
          .addToBackStack(null)
          .commit();
    } else {
      showAlert(getString(R.string.alert_permission_forbid));
    }
  }

  @Override public String getTitle() {
    return "更多信息";
  }

  @Override public void onData(User_Student user_student) {
    this.user_student = user_student;
    if (user_student.getSellers() != null && user_student.getSellers().size() > 0) {
      List<String> salername = new ArrayList<>();
      for (int i = 0; i < user_student.getSellers().size(); i++) {
        salername.add(user_student.getSellers().get(i).username);
      }
      mSalesName.setText(StringUtils.List2Str(salername));
    } else {
      mSalesName.setText(R.string.no_saler);
    }

    if (!StringUtils.isEmpty(user_student.getCheckin_avatar())) {
      signinImg.setVisibility(View.VISIBLE);
      noCheckinImg.setVisibility(View.GONE);
      Glide.with(getContext())
          .load(PhotoUtils.getSmall(user_student.getCheckin_avatar()))
          .into(signinImg);
    } else {
      signinImg.setVisibility(View.GONE);
      noCheckinImg.setVisibility(View.VISIBLE);
      if (gymWrapper.inBrand()) {
        noCheckinImg.setText("暂无会员照片");
      } else {
        noCheckinImg.setText(Html.fromHtml(getString(R.string.no_checkin_img)));
      }
      noCheckinImg.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onSignInImg();
        }
      });
    }
    tvStudentSource.setText(user_student.getOrigin());
    tvStudentReferrer.setText(user_student.getRecommend_by());
    List<CoachBean> coaches = user_student.getCoaches();
    if (coaches != null) {
      if (coaches.size() > 0) {
        coachName.setText(
            coaches.size() > 2 ? coaches.get(0).username + "、" + coaches.get(1).username
                : coaches.get(0).username);
      } else {
        coachName.setText("无教练");
      }
    }
    presenter.getScoreStatus();
  }

  @Override public void onSuccess() {
    getActivity().finish();
  }

  @Override public void onEditSuccess() {
    presenter.queryStudents();
  }

  @Override public void onFailed(String s) {
    ToastUtils.show(s);
  }

  @Override public void onStudentScore(String score) {
    this.score = score;
    tvStudentScoreValue.setText(score);
  }

  @Override public void onStudentScoreFail(String s) {

  }

  @Override public void onScoreStatus(boolean score) {
    if (score) {
      presenter.getScore(user_student.getId());
      rlScore.setVisibility(View.VISIBLE);
    } else {
      rlScore.setVisibility(View.GONE);
    }
  }

  @Override public void onScoreStatusFail(String s) {

  }

  @Override public void onDestroyView() {
    RxBus.getBus().unregister(EditStudentEvent.class.getName(), mObEdit);
    presenter.unattachView();
    super.onDestroyView();
  }

  @OnClick(R.id.body_layout) public void onClickBodyTest() {
    if (user_student == null) {
      Timber.e("获取学员信息失败");
      return;
    }
    getParentFragment().getFragmentManager()
        .beginTransaction()
        .add(mCallbackActivity.getFragId(),
            BodyTestListFragment.newInstance(user_student.getGender()))
        .addToBackStack(null)
        .commit();
  }

  @Override public String getFragmentName() {
    return StudentMoreInfoFragment.class.getName();
  }

  @OnClick(R.id.layout_signin_img) public void onSignInImg() {
    boolean hasP = serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE);
    if (hasP) {
      getParentFragment().getFragmentManager()
          .beginTransaction()
          .add(mCallbackActivity.getFragId(), StudentSignInImageFragment.newInstance())
          .addToBackStack(null)
          .commit();
    } else {
      showAlert(R.string.sorry_for_no_permission);
    }
  }

  @OnClick(R.id.rl_score) public void scoreClick(View view) {
    // // 跳转会员积分
    Intent toScore = new Intent(getContext(), ScoreDetailActivity.class);
    startActivityForResult(toScore, RESULT_SCORE);
  }

  @OnClick(R.id.salers_layout) public void allotSaler() {
    if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
      changeSaler();
    } else {
      showAlert(R.string.alert_permission_forbid);
    }
  }

  @OnClick(R.id.coach_layout) public void allocateCoach() {
    if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
      Intent toChooseSaler = new Intent(getActivity(), MutiChooseCoachActivity.class);
      toChooseSaler.putExtra("hasReturn", false);
      toChooseSaler.putStringArrayListExtra(MutiChooseCoachActivity.INPUT_STUDENT,
          new ArrayList<String>(Arrays.asList(user_student.getId())));
      toChooseSaler.putStringArrayListExtra(MutiChooseCoachActivity.INPUT_COACHES,
          (ArrayList<String>) StringUtils.coachIds(user_student.getCoaches()));
      startActivityForResult(toChooseSaler, RESULT_COACH);
    }
  }

  @OnClick(R.id.ll_student_source) public void sourceClick(View view) {
    boolean hasP = serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE);
    if (hasP) {
      startActivityForResult(new ChooseOriginActivityIntentBuilder().build(getContext()),
          RESULT_ORIGIN);
    } else {
      showAlert(R.string.alert_permission_forbid);
    }
  }

  @OnClick(R.id.ll_student_referrer) public void referrerClick(View view) {
    boolean hasP = serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE);
    if (hasP) {
      startActivityForResult(new Intent(getActivity(), ChooseReferrerActivity.class),
          RESULT_REFERRER);
    } else {
      showAlert(R.string.alert_permission_forbid);
    }
  }

  private void changeSaler() {
    Intent toChooseSaler = new Intent(getActivity(), MutiChooseSalersActivity.class);
    toChooseSaler.putStringArrayListExtra(MutiChooseSalersActivity.INPUT_STUDENT,
        new ArrayList<String>(Arrays.asList(user_student.getId())));
    toChooseSaler.putStringArrayListExtra(MutiChooseSalersActivity.INPUT_SALERS,
        (ArrayList) StringUtils.Str2List(user_student.getSeller_ids()));

    startActivityForResult(toChooseSaler, RESULT_SALAER);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == RESULT_SALAER) {
        presenter.queryStudents();
      } else if (requestCode == RESULT_ORIGIN) {
        StudentSourceBean sourceBean = (StudentSourceBean) IntentUtils.getParcelable(data);
        user_student.setOrigin(sourceBean.name);
        // // POST 修改数据
        presenter.editStudent(user_student);
        // 刷新数据
      } else if (requestCode == RESULT_REFERRER) {
        StudentReferrerBean referrerBean = (StudentReferrerBean) IntentUtils.getParcelable(data);
        user_student.setRecommend_by_id(referrerBean.id);
        // // POST 修改数据
        presenter.editStudent(user_student);
        // 刷新数据
      } else if (requestCode == RESULT_SCORE) {
        presenter.getScore(user_student.getId());
      } else if (requestCode == RESULT_COACH) {
        presenter.queryStudents();
      }
    }
  }
}
