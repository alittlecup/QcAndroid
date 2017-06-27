package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
 * Created by Paper on 2017/3/7.
 */
@FragmentWithArgs public class WorkExpSyncDetailFragment extends BaseFragment {

  @BindView(R2.id.host_img) ImageView hostImg;
  @BindView(R2.id.host_qc_identify) ImageView hostQcIdentify;
  @BindView(R2.id.workexpedit_gym_name) TextView workexpeditGymName;
  @BindView(R2.id.host_address) TextView hostAddress;
  @BindView(R2.id.host_layout) RelativeLayout hostLayout;
  @BindView(R2.id.workexpedit_start_time) CommonInputView workexpeditStartTime;
  @BindView(R2.id.workexpedit_start_end) CommonInputView workexpeditStartEnd;
  @BindView(R2.id.workexpedit_city) CommonInputView workexpeditCity;
  @BindView(R2.id.workexpedit_position) CommonInputView workexpeditPosition;
  @BindView(R2.id.workexpedit_descripe) EditText workexpeditDescripe;
  @BindView(R2.id.workexpedit_expe_layout) LinearLayout workexpeditExpeLayout;
  @BindView(R2.id.tv_group) TextView tvGroup;
  @BindView(R2.id.sw_group) ExpandedLayout swGroup;
  @BindView(R2.id.tv_private) TextView tvPrivate;
  @BindView(R2.id.sw_private) ExpandedLayout swPrivate;
  @BindView(R2.id.tv_sales) TextView tvSales;
  @BindView(R2.id.sw_sale) ExpandedLayout swSale;
  @BindView(R2.id.rootview) LinearLayout rootview;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;

  @Arg WorkExp experiencesEntity;
  @Inject QcRestRepository qcRestRepository;
  TimeDialogWindow pwTime;
  private DialogSheet mDialogSheet;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WorkExpSyncDetailFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_workexp_sync, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    toolbarTitile.setText("工作经历详情");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        putWorkExp();
        return false;
      }
    });
    workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
    Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
    Calendar c = Calendar.getInstance(Locale.getDefault());
    c.setTime(d);
    int year = c.get(Calendar.YEAR);
    if (year == 3000) {
      workexpeditStartEnd.setContent("至今");
    } else {
      workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(d));
    }

    workexpeditDescripe.setText(experiencesEntity.getDescription());
    workexpeditPosition.setContent(experiencesEntity.getPosition());
    if (experiencesEntity.getGym() != null) {
      workexpeditGymName.setText(experiencesEntity.getGym().getName());
      Glide.with(getContext()).load(experiencesEntity.getGym().getPhoto()).asBitmap().into(new CircleImgWrapper(hostImg, getContext()));
      hostAddress.setText(experiencesEntity.getGym().getAddress());
    }
    workexpeditDescripe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        CompatUtils.removeGlobalLayout(workexpeditDescripe.getViewTreeObserver(), this);
        swGroup.setExpanded(!experiencesEntity.isGroup_is_hidden());
        swPrivate.setExpanded(!experiencesEntity.isPrivate_is_hidden());
        swSale.setExpanded(!experiencesEntity.isSale_is_hidden());
        tvGroup.setText(getString(R.string.exp_group, experiencesEntity.getGroup_course(), experiencesEntity.getGroup_user()));
        tvPrivate.setText(getString(R.string.exp_group, experiencesEntity.getPrivate_course(), experiencesEntity.getPrivate_user()));
        tvSales.setText(getString(R.string.exp_sale, CmStringUtils.getMoneyStr(experiencesEntity.getSale())));
      }
    });
    return view;
  }

  void putWorkExp() {
    showLoading();
    RxRegiste(qcRestRepository.createGetApi(PostApi.class)
        .updateWorkExp(experiencesEntity.id, new WorkExp.Builder().group_is_hidden(!swGroup.isExpanded())
            .private_is_hidden(!swPrivate.isExpanded())
            .sale_is_hidden(!swSale.isExpanded())
            .description(workexpeditDescripe.getText().toString())
            .start(workexpeditStartTime.getContent())
            .end(workexpeditStartEnd.getContent().equals("至今") ? "3000-01-01" : workexpeditStartEnd.getContent())
            .position(workexpeditPosition.getContent())
            .build())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            hideLoading();
            if (qcResponse.status == 200) {
              RxBus.getBus().post(new EventResumeFresh());
              getActivity().onBackPressed();
            } else {
              ToastUtils.show(qcResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            hideLoading();
            ToastUtils.show(throwable.getMessage());
          }
        }));
  }

  @OnClick(R2.id.workexpedit_start_time) public void onStartTime() {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        if (date.getTime() > new Date().getTime()) {
          Toast.makeText(getContext(), "起始时间不能晚于今天", Toast.LENGTH_SHORT).show();
          return;
        }

        if (!TextUtils.equals("至今", workexpeditStartEnd.getContent())
            && DateUtils.formatDateFromYYYYMMDD(workexpeditStartEnd.getContent()).getTime()
            < DateUtils.formatDateFromYYYYMMDD(workexpeditStartTime.getContent()).getTime()) {
          Toast.makeText(getContext(), "起始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
          return;
        }

        workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
  }

  @OnClick(R2.id.workexpedit_start_end) public void onEndTime() {
    if (mDialogSheet == null) {
      mDialogSheet = DialogSheet.builder(getContext()).addButton("至今", new View.OnClickListener() {
        @Override public void onClick(View v) {
          mDialogSheet.dismiss();
          workexpeditStartEnd.setContent("至今");
        }
      }).addButton("选择日期", new View.OnClickListener() {
        @Override public void onClick(View v) {
          mDialogSheet.dismiss();
          if (pwTime == null) {
            pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
          }
          pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
          pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
              if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(
                  workexpeditStartTime.getContent()).getTime()) {
                Toast.makeText(getContext(), "结束时间不能早于结束时间", Toast.LENGTH_SHORT).show();
                return;
              }
              workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(date));
              pwTime.dismiss();
            }
          });
          pwTime.setRange(1900, 2100);
          pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
        }
      });
    }

    mDialogSheet.show();
  }


  @Override public String getFragmentName() {
    return WorkExpSyncDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
