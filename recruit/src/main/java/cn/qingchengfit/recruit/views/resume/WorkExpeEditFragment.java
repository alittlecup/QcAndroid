package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;




import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/8 2015.
 */
@FragmentWithArgs public class WorkExpeEditFragment extends BaseFragment {

  @Arg(required = false) WorkExp workExp;
  @Arg Gym curGym;

	CommonInputView workexpeditStartTime;
	CommonInputView workexpeditStartEnd;
	CommonInputView workexpeditCity;
	TextView workexpeditGymName;
	CommonInputView workexpeditPosition;
	EditText workexpeditDescripe;
	CommonInputView workexpeditGroupClass;
	CommonInputView workexpeditGroupNum;
	CommonInputView workexpeditPrivateClass;
	CommonInputView workexpeditPrivateNum;
	CommonInputView workexpeditSale;
  //@BindView(R2.id.workexpedit_comfirm_btn) Button workexpeditComfirmBtn;
	LinearLayout rootview;
	LinearLayout workexpeditExpeLayout;
	ImageView hostImg;
	ImageView hostQcIdentify;
	TextView hostAddress;
	Toolbar toolbar;
	TextView toolbarTitile;
  TimeDialogWindow pwTime;
  @Inject QcRestRepository restRepository;
	ExpandedLayout swGroup;
	ExpandedLayout swPrivate;
	ExpandedLayout swSale;
  private MaterialDialog delDialog;
  private DialogSheet mDialogSheet;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WorkExpeEditFragmentBuilder.injectArguments(this);
    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_workexepedit, container, false);
    workexpeditStartTime = (CommonInputView) view.findViewById(R.id.workexpedit_start_time);
    workexpeditStartEnd = (CommonInputView) view.findViewById(R.id.workexpedit_start_end);
    workexpeditCity = (CommonInputView) view.findViewById(R.id.workexpedit_city);
    workexpeditGymName = (TextView) view.findViewById(R.id.workexpedit_gym_name);
    workexpeditPosition = (CommonInputView) view.findViewById(R.id.workexpedit_position);
    workexpeditDescripe = (EditText) view.findViewById(R.id.workexpedit_descripe);
    workexpeditGroupClass = (CommonInputView) view.findViewById(R.id.workexpedit_group_class);
    workexpeditGroupNum = (CommonInputView) view.findViewById(R.id.workexpedit_group_num);
    workexpeditPrivateClass = (CommonInputView) view.findViewById(R.id.workexpedit_private_class);
    workexpeditPrivateNum = (CommonInputView) view.findViewById(R.id.workexpedit_private_num);
    workexpeditSale = (CommonInputView) view.findViewById(R.id.workexpedit_sale);
    rootview = (LinearLayout) view.findViewById(R.id.rootview);
    workexpeditExpeLayout = (LinearLayout) view.findViewById(R.id.workexpedit_expe_layout);
    hostImg = (ImageView) view.findViewById(R.id.host_img);
    hostQcIdentify = (ImageView) view.findViewById(R.id.host_qc_identify);
    hostAddress = (TextView) view.findViewById(R.id.host_address);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    swGroup = (ExpandedLayout) view.findViewById(R.id.sw_group);
    swPrivate = (ExpandedLayout) view.findViewById(R.id.sw_private);
    swSale = (ExpandedLayout) view.findViewById(R.id.sw_sale);
    view.findViewById(R.id.workexpedit_expe_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDescripte();
      }
    });
    view.findViewById(R.id.host_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickGym();
      }
    });
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
    view.findViewById(R.id.workexpedit_city).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCityClick();
      }
    });

    initToolbar(toolbar);
    toolbarTitile.setText(workExp == null ? "添加工作经验" : "编辑工作经验");

    if (curGym != null) {
      setGym(curGym);
      if (workExp != null) workExp.gym = curGym;
    }
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    setInfo();
  }

  public void setInfo() {
    if (workExp != null) {
      workexpeditStartTime.setContent(
          DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(workExp.getStart())));
      Date d = DateUtils.formatDateFromServer(workExp.getEnd());
      Calendar c = Calendar.getInstance(Locale.getDefault());
      c.setTime(d);
      int year = c.get(Calendar.YEAR);
      if (year == 3000) {
        workexpeditStartEnd.setContent("至今");
      } else {
        workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(d));
      }
      workexpeditDescripe.setText(workExp.getDescription());
      workexpeditPosition.setContent(workExp.getPosition());
      if (workExp.getGym() != null) {
        setGym(workExp.getGym());
      }
      workexpeditGroupClass.setContent(Integer.toString(workExp.getGroup_course()));
      workexpeditGroupNum.setContent(Integer.toString(workExp.getGroup_user()));
      workexpeditPrivateClass.setContent(Integer.toString(workExp.getPrivate_course()));
      workexpeditPrivateNum.setContent(Integer.toString(workExp.getPrivate_user()));
      workexpeditSale.setContent(workExp.getSale() + "");
      swGroup.setExpanded(!workExp.isGroup_is_hidden());
      swPrivate.setExpanded(!workExp.isPrivate_is_hidden());
      swSale.setExpanded(!workExp.isSale_is_hidden());
    } else {
      workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
      workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        onComfirm();
        return true;
      }
    });
  }

  public void setGym(Gym gym) {
    workexpeditGymName.setText(gym.getName());
    Glide.with(getContext())

        .load(gym.getPhoto())
        .asBitmap()
        .into(new CircleImgWrapper(hostImg, getActivity()));
    hostAddress.setText(gym.getAddress());
  }

  public void onComfirm() {

    String starttime = DateUtils.formatDateToServer(workexpeditStartTime.getContent());
    String endtime = DateUtils.formatDateToServer(workexpeditStartEnd.getContent());
    String postion = workexpeditPosition.getContent();
    String description = workexpeditDescripe.getText().toString();
    String groupCount = workexpeditGroupClass.getContent();
    String groupNum = workexpeditGroupNum.getContent();
    String privateClass = workexpeditPrivateClass.getContent();
    String privateNum = workexpeditPrivateNum.getContent();
    String sale = workexpeditSale.getContent();
    //String gym = workexpeditGymName.getText().toString().trim();

    if (TextUtils.isEmpty(postion)) {
      Toast.makeText(getContext(), "请填写职位信息", Toast.LENGTH_SHORT).show();
      return;
    }
    if (curGym == null || TextUtils.isEmpty(curGym.getId())) {
      Toast.makeText(getContext(), "请选择健身房", Toast.LENGTH_SHORT).show();
      return;
    }

    if (endtime.equalsIgnoreCase("至今")) {
      endtime = "3000-1-1";
    }
    if (DateUtils.formatDateFromYYYYMMDD(starttime).getTime() > DateUtils.formatDateFromYYYYMMDD(
        endtime).getTime()) {
      Toast.makeText(getContext(), "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
      return;
    }

    Observable<QcResponse> ob;
    WorkExp.Builder body = new WorkExp.Builder().start(starttime)
        .end(endtime)
        .description(description)
        .position(postion)
        .group_is_hidden(!swGroup.isExpanded())
        .private_is_hidden(!swPrivate.isExpanded())
        .sale_is_hidden(!swSale.isExpanded());

    if (swGroup.isExpanded()) {
      try {
        body.group_course(Integer.parseInt(groupCount));
        body.group_user(Integer.parseInt(groupNum));
      } catch (Exception e) {
        showAlert("请填写团课业绩");
        return;
      }
    }
    if (swPrivate.isExpanded()) {
      try {
        body.private_course(Integer.parseInt(privateClass));
        body.private_user(Integer.parseInt(privateNum));
      } catch (Exception e) {
        showAlert("请填写私教业绩");
        return;
      }
    }
    if (swSale.isExpanded()) {
      try {
        body.sale(Float.parseFloat(sale));
      } catch (Exception e) {
        showAlert("请填写私教业绩");
        return;
      }
    }

    if (workExp != null) {
      ob = restRepository.createGetApi(PostApi.class).updateWorkExp(workExp.id, body.build());
    } else {
      body.gym_id(curGym.id);
      ob = restRepository.createGetApi(PostApi.class).addWorkExp(body.build());
    }
    showLoading();
    RxRegiste(ob.observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(qcResponse -> {
          hideLoading();
          if (qcResponse.status == 200) {
            RxBus.getBus().post(new EventResumeFresh());
            ToastUtils.show(workExp == null ? "添加成功" : "修改成功");
            getActivity().onBackPressed();
          } else {
            ToastUtils.show(qcResponse.getMsg());
          }
        }, throwable -> {
          hideLoading();
          ToastUtils.show(throwable.getMessage());
        }));
  }

 public void onDescripte() {
    workexpeditDescripe.requestFocus();
  }

 public void onClickGym() {
    Intent toSearch = new Intent(getActivity(), SearchActivity.class);
    toSearch.putExtra("type", SearchFragment.TYPE_GYM);
    startActivityForResult(toSearch, 10010);
  }

 public void onStartTime() {
    pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
    pwTime.setOnTimeSelectListener(date -> {
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
      }).addButton("选择日期", v -> {
        mDialogSheet.dismiss();
        pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
        pwTime.setOnTimeSelectListener(date -> {
          if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(
              workexpeditStartTime.getContent()).getTime()) {
            Toast.makeText(getContext(), "结束时间不能早于结束时间", Toast.LENGTH_SHORT).show();
            return;
          }
          workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(date));
          pwTime.dismiss();
        });
        pwTime.setRange(1900, 2100);
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
      });
    }
    mDialogSheet.show();
  }

 public void onCityClick() {
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10010 && resultCode > 0) {
      curGym.setName(data.getStringExtra("username"));
      curGym.setId(data.getIntExtra("id", 0) + "");
      boolean isAuth = data.getBooleanExtra("isauth", false);
      if (isAuth) {
        hostQcIdentify.setVisibility(View.VISIBLE);
      } else {
        hostQcIdentify.setVisibility(View.GONE);
      }
      String address = data.getStringExtra("address");
      if (isEmpty(address)) {
        hostAddress.setVisibility(View.GONE);
      } else {
        hostAddress.setVisibility(View.VISIBLE);
        hostAddress.setText(address);
      }
      Glide.with(getContext())
          .load(data.getStringExtra("pic"))
          .asBitmap()
          .into(new CircleImgWrapper(hostImg, getContext()));
    }
  }
}
