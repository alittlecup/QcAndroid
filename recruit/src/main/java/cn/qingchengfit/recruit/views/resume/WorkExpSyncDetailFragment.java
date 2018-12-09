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



import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;

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

	ImageView hostImg;
	ImageView hostQcIdentify;
	TextView workexpeditGymName;
	TextView hostAddress;
	RelativeLayout hostLayout;
	CommonInputView workexpeditStartTime;
	CommonInputView workexpeditStartEnd;
	CommonInputView workexpeditCity;
	CommonInputView workexpeditPosition;
	EditText workexpeditDescripe;
	LinearLayout workexpeditExpeLayout;
	TextView tvGroup;
	ExpandedLayout swGroup;
	TextView tvPrivate;
	ExpandedLayout swPrivate;
	TextView tvSales;
	ExpandedLayout swSale;
	LinearLayout rootview;
	Toolbar toolbar;
	TextView toolbarTitile;

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
    hostImg = (ImageView) view.findViewById(R.id.host_img);
    hostQcIdentify = (ImageView) view.findViewById(R.id.host_qc_identify);
    workexpeditGymName = (TextView) view.findViewById(R.id.workexpedit_gym_name);
    hostAddress = (TextView) view.findViewById(R.id.host_address);
    hostLayout = (RelativeLayout) view.findViewById(R.id.host_layout);
    workexpeditStartTime = (CommonInputView) view.findViewById(R.id.workexpedit_start_time);
    workexpeditStartEnd = (CommonInputView) view.findViewById(R.id.workexpedit_start_end);
    workexpeditCity = (CommonInputView) view.findViewById(R.id.workexpedit_city);
    workexpeditPosition = (CommonInputView) view.findViewById(R.id.workexpedit_position);
    workexpeditDescripe = (EditText) view.findViewById(R.id.workexpedit_descripe);
    workexpeditExpeLayout = (LinearLayout) view.findViewById(R.id.workexpedit_expe_layout);
    tvGroup = (TextView) view.findViewById(R.id.tv_group);
    swGroup = (ExpandedLayout) view.findViewById(R.id.sw_group);
    tvPrivate = (TextView) view.findViewById(R.id.tv_private);
    swPrivate = (ExpandedLayout) view.findViewById(R.id.sw_private);
    tvSales = (TextView) view.findViewById(R.id.tv_sales);
    swSale = (ExpandedLayout) view.findViewById(R.id.sw_sale);
    rootview = (LinearLayout) view.findViewById(R.id.rootview);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.workexpedit_start_time).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onStartTime();
      }
    });
    view.findViewById(R.id.workexpedit_start_end).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onEndTime();
      }
    });

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
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .updateWorkExp(experiencesEntity.id, new WorkExp.Builder().group_is_hidden(!swGroup.isExpanded())
            .private_is_hidden(!swPrivate.isExpanded())
            .sale_is_hidden(!swSale.isExpanded())
            .description(workexpeditDescripe.getText().toString())
            .start(workexpeditStartTime.getContent())
            .end(workexpeditStartEnd.getContent().equals("至今") ? "3000-01-01" : workexpeditStartEnd.getContent())
            .position(workexpeditPosition.getContent())
            .build()).onBackpressureBuffer().subscribeOn(Schedulers.io())
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

 public void onStartTime() {
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

 public void onEndTime() {
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
