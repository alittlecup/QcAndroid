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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
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

  @BindView(R2.id.workexpedit_start_time) CommonInputView workexpeditStartTime;
  @BindView(R2.id.workexpedit_start_end) CommonInputView workexpeditStartEnd;
  @BindView(R2.id.workexpedit_city) CommonInputView workexpeditCity;
  @BindView(R2.id.workexpedit_gym_name) TextView workexpeditGymName;
  @BindView(R2.id.workexpedit_position) CommonInputView workexpeditPosition;
  @BindView(R2.id.workexpedit_descripe) EditText workexpeditDescripe;
  @BindView(R2.id.workexpedit_group_class) CommonInputView workexpeditGroupClass;
  @BindView(R2.id.workexpedit_group_num) CommonInputView workexpeditGroupNum;
  @BindView(R2.id.workexpedit_private_class) CommonInputView workexpeditPrivateClass;
  @BindView(R2.id.workexpedit_private_num) CommonInputView workexpeditPrivateNum;
  @BindView(R2.id.workexpedit_sale) CommonInputView workexpeditSale;
  //@BindView(R2.id.workexpedit_comfirm_btn) Button workexpeditComfirmBtn;
  @BindView(R2.id.rootview) LinearLayout rootview;
  @BindView(R2.id.workexpedit_expe_layout) LinearLayout workexpeditExpeLayout;
  @BindView(R2.id.host_img) ImageView hostImg;
  @BindView(R2.id.host_qc_identify) ImageView hostQcIdentify;
  @BindView(R2.id.host_address) TextView hostAddress;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  TimeDialogWindow pwTime;
  @Inject QcRestRepository restRepository;
  @BindView(R2.id.sw_group) ExpandedLayout swGroup;
  @BindView(R2.id.sw_private) ExpandedLayout swPrivate;
  @BindView(R2.id.sw_sale) ExpandedLayout swSale;
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
    unbinder = ButterKnife.bind(this, view);
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

  @OnClick(R2.id.workexpedit_expe_layout) public void onDescripte() {
    workexpeditDescripe.requestFocus();
  }

  @OnClick(R2.id.host_layout) public void onClickGym() {
    Intent toSearch = new Intent(getActivity(), SearchActivity.class);
    toSearch.putExtra("type", SearchFragment.TYPE_GYM);
    startActivityForResult(toSearch, 10010);
  }

  @OnClick(R2.id.workexpedit_start_time) public void onStartTime() {
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

  @OnClick(R2.id.workexpedit_start_end) public void onEndTime() {
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

  @OnClick(R2.id.workexpedit_city) public void onCityClick() {
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
