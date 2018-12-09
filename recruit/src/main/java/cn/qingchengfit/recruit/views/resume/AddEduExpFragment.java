package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * Created by Paper on 2017/6/13.
 */
@FragmentWithArgs public class AddEduExpFragment extends BaseFragment {
  @Arg(required = false) Education education;

	Toolbar toolbar;
	TextView toolbarTitile;
	CommonInputView civSchoolName;
	CommonInputView civSpeciality;
	CommonInputView civInTime;
	CommonInputView civOutTime;
	CommonInputView civDegree;
  @Inject QcRestRepository qcRestRepository;
  private SimpleScrollPicker simpleScrollPicker;
  private TimeDialogWindow timeDialogWindow;
  private int curDegree = 0;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AddEduExpFragmentBuilder.injectArguments(this);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    timeDialogWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_edu_exp, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    civSchoolName = (CommonInputView) view.findViewById(R.id.civ_school_name);
    civSpeciality = (CommonInputView) view.findViewById(R.id.civ_speciality);
    civInTime = (CommonInputView) view.findViewById(R.id.civ_in_time);
    civOutTime = (CommonInputView) view.findViewById(R.id.civ_out_time);
    civDegree = (CommonInputView) view.findViewById(R.id.civ_degree);
    view.findViewById(R.id.civ_in_time).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivInTimeClicked();
      }
    });
    view.findViewById(R.id.civ_out_time).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivOutTimeClicked();
      }
    });
    view.findViewById(R.id.civ_degree).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivDegreeClicked();
      }
    });

    initToolbar(toolbar);
    initView();
    return view;
  }

  private void initView() {
    if (education != null) {
      curDegree = education.education;
      civDegree.setContent(RecruitBusinessUtils.getDegree(getContext(), curDegree));
      civSchoolName.setContent(education.name);
      civSpeciality.setContent(education.major);
      civInTime.setContent(DateUtils.getYYMMfromServer(education.start));
      civOutTime.setContent(DateUtils.getYYMMfromServer(education.end));
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(education == null ? "添加教育经历" : "编辑教育经历");
    toolbar.inflateMenu(education == null ? R.menu.menu_compelete : R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (TextUtils.isEmpty(civSchoolName.getContent())) {
          showAlert("请填写学校名称");
          return true;
        }
        if (TextUtils.isEmpty(civSpeciality.getContent())) {
          showAlert("请填写学位名称");
          return true;
        }

        if (TextUtils.isEmpty(civDegree.getContent())) {
          showAlert("请选择职位");
          return true;
        }
        if (TextUtils.isEmpty(civInTime.getContent()) || TextUtils.isEmpty(civInTime.getContent()) || (DateUtils.YYMM2date(
            civInTime.getContent()).getTime() >= (DateUtils.YYMM2date(civOutTime.getContent()).getTime()))) {
          showAlert("请选择正确入校时间和毕业时间");
          return true;
        }
        showLoading();
        if (education == null) {
          RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
              .addEducation(new Education.Builder().education(curDegree)
                  .name(civSchoolName.getContent())
                  .major(civSpeciality.getContent())
                  .start(civInTime.getContent() + "-01")
                  .end(civOutTime.getContent() + "-01")
                  .build()).onBackpressureBuffer().subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                  hideLoading();
                  getActivity().onBackPressed();
                  RxBus.getBus().post(new EventResumeFresh());
                  onShowError("添加成功！");
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  hideLoading();
                  onShowError(throwable.getMessage());
                }
              }));
        } else {
          RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
              .updateEducation(education.id, new Education.Builder().education(curDegree)
                  .name(civSchoolName.getContent())
                  .major(civSpeciality.getContent())
                  .start(civInTime.getContent() + "-01")
                  .end(civOutTime.getContent() + "-01")
                  .build()).onBackpressureBuffer().subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                  hideLoading();
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                    getActivity().onBackPressed();
                    RxBus.getBus().post(new EventResumeFresh());
                    onShowError("修改成功！");
                  } else {
                    onShowError(qcResponse.getMsg());
                  }
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  hideLoading();
                  onShowError(throwable.getMessage());
                }
              }));
        }
        return false;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    if (education != null) {
      civSchoolName.setContent(education.name);
      civSpeciality.setContent(education.major);
      civInTime.setContent(DateUtils.date2YYMM(DateUtils.formatDateFromServer(education.start)));
      civOutTime.setContent(DateUtils.date2YYMM(DateUtils.formatDateFromServer(education.end)));
      civDegree.setContent(RecruitBusinessUtils.getDegree(getContext(), education.education));
      curDegree = education.education;
    }
  }

  @Override public String getFragmentName() {
    return AddEduExpFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 入校时间
   */
 public void onCivInTimeClicked() {
    timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        civInTime.setContent(DateUtils.Date2YYYYMM(date));
      }
    });
    timeDialogWindow.setRange(1980,2100);
    timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, DateUtils.YYMM2date(civInTime.getContent()));
  }

  /**
   * 毕业时间
   */
 public void onCivOutTimeClicked() {

    timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        civOutTime.setContent(DateUtils.date2YYMM(date));
      }
    });
    timeDialogWindow.setRange(1980,2100);
    timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, DateUtils.YYMM2date(civOutTime.getContent()));
  }

  /**
   * 学位
   */
 public void onCivDegreeClicked() {
    final ArrayList<String> d =
        new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.add_resume_education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        curDegree = pos + 1;
        civDegree.setContent(d.get(pos));
      }
    });
    simpleScrollPicker.show(d, curDegree > 0 ? curDegree - 1 : 0);
  }
}
