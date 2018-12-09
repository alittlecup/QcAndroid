package cn.qingchengfit.recruit.views.jobfair;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseGym;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
 * Created by Paper on 2017/7/8.
 */
public class JobfairSignUpFragment extends BaseFragment {

	Toolbar toolbar;
	TextView toolbarTitle;
	TextView tvFairName;
	CommonInputView civGym;
	CommonInputView civName;
	CommonInputView civPhone;
	Button btnJoin;
  @Inject QcRestRepository qcRestRepository;
  @Inject RecruitRouter router;
  @Inject LoginStatus loginStatus;
  private Gym gym;
  private JobFair jobfair;
  private TextWatcher tw = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      checkBtnEnable();
    }
  };

  public static JobfairSignUpFragment newInstance(JobFair fairId) {
    Bundle args = new Bundle();
    args.putParcelable("fair", fairId);
    JobfairSignUpFragment fragment = new JobfairSignUpFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      jobfair = getArguments().getParcelable("fair");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signup_job_fair, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tvFairName = (TextView) view.findViewById(R.id.tv_fair_name);
    civGym = (CommonInputView) view.findViewById(R.id.civ_gym);
    civName = (CommonInputView) view.findViewById(R.id.civ_name);
    civPhone = (CommonInputView) view.findViewById(R.id.civ_phone);
    btnJoin = (Button) view.findViewById(R.id.btn_join);
    view.findViewById(R.id.btn_join).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClicked();
      }
    });
    view.findViewById(R.id.civ_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClickedGym();
      }
    });

    civName.addTextWatcher(tw);
    civPhone.addTextWatcher(tw);
    initToolbar(toolbar);
    initBus();
    tvFairName.setText(jobfair.name);
    if (loginStatus != null && loginStatus.isLogined()) {
      civName.setContent(loginStatus.getLoginUser().getUsername());
      civPhone.setContent(loginStatus.getLoginUser().getPhone());
    }
    return view;
  }

  private void initBus() {
    RxBusAdd(EventChooseGym.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventChooseGym>() {
          @Override public void call(EventChooseGym eventChooseGym) {
            gym = eventChooseGym.gym;
            civGym.setContent(gym.getName());
            checkBtnEnable();
          }
        });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("申请参加");
  }

  @Override public String getFragmentName() {
    return JobfairSignUpFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

 public void onViewClicked() {
    HashMap<String, Object> p = new HashMap<>();
    p.put("fair_id", jobfair.id);
    p.put("gym_id", gym.getId());
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .joinFair(p).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              //getFragmentManager().beginTransaction().remove(JobfairSignUpFragment.this)
              //    .add(R.id.frag_recruit,JobFairSuccessFragment.newInstance(gym))
              //    .addToBackStack(null)

              router.toJoinFairSuc(gym);
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  private void checkBtnEnable() {
    if (gym != null
        && !civName.isEmpty()
        && !civPhone.isEmpty()
        && civPhone.getContent().length() >= 10) {
      btnJoin.setEnabled(true);
    } else {
      btnJoin.setEnabled(false);
    }
  }

 public void onViewClickedGym() {
    router.chooseGymForJobfair();
  }

}
