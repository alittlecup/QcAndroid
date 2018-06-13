package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.response.WorkExpWrap;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
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
 * Created by Paper on 2017/6/24.
 */
public class ResumeWorkExpPreviewFragment extends BaseFragment {

  ImageView gymImg;
  TextView gymTime;
  TextView workexpDetailHiden;
  TextView workexpDetailPosition;
  TextView workexpDetailDesc;
  TextView workexpDetailGroupCount;
  TextView workexpDetailGroupServer;
  LinearLayout workexpDetailGroupLayout;
  TextView workexpDetailPrivateCount;
  TextView workexpDetailPrivateServer;
  LinearLayout workexpDetailPrivateLayout;
  TextView workexpDetailSale;
  LinearLayout workexpDetailSaleLayout;
  TextView gymName;
  TextView gymAddress;
  ImageView gymIdentify;

  @Inject QcRestRepository qcRestRepository;

  Toolbar toolbar;
  TextView toolbarTitle;
  @Inject RecruitRouter recruitRouter;
  WorkExp experiencesEntity;
  private String mExpId;
  private DialogSheet dialogSheet;
  private DialogSheet menuDialog;
  private MaterialDialog delDialog;
  private boolean isHiden = false;

  public ResumeWorkExpPreviewFragment() {
  }

  public static ResumeWorkExpPreviewFragment newInstance(String id) {

    Bundle args = new Bundle();
    args.putString("id", id);
    ResumeWorkExpPreviewFragment fragment = new ResumeWorkExpPreviewFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) mExpId = getArguments().getString("id");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_work_exp_detail, container, false);
    gymImg = (ImageView) view.findViewById(R.id.gym_img);
    gymTime = (TextView) view.findViewById(R.id.gym_time);
    workexpDetailHiden = (TextView) view.findViewById(R.id.workexp_detail_hiden);
    workexpDetailPosition = (TextView) view.findViewById(R.id.workexp_detail_position);
    workexpDetailDesc = (TextView) view.findViewById(R.id.workexp_detail_desc);
    workexpDetailGroupCount = (TextView) view.findViewById(R.id.workexp_detail_group_count);
    workexpDetailGroupServer = (TextView) view.findViewById(R.id.workexp_detail_group_server);
    workexpDetailGroupLayout = (LinearLayout) view.findViewById(R.id.workexp_detail_group_layout);
    workexpDetailPrivateCount = (TextView) view.findViewById(R.id.workexp_detail_private_count);
    workexpDetailPrivateServer = (TextView) view.findViewById(R.id.workexp_detail_private_server);
    workexpDetailPrivateLayout =
        (LinearLayout) view.findViewById(R.id.workexp_detail_private_layout);
    workexpDetailSale = (TextView) view.findViewById(R.id.workexp_detail_sale);
    workexpDetailSaleLayout = (LinearLayout) view.findViewById(R.id.workexp_detail_sale_layout);
    gymName = (TextView) view.findViewById(R.id.gym_name);
    gymAddress = (TextView) view.findViewById(R.id.gym_address);
    gymIdentify = (ImageView) view.findViewById(R.id.gym_identify);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

    initToolbar(toolbar);
    freshData();
    RxBusAdd(EventResumeFresh.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventResumeFresh>() {
          @Override public void call(EventResumeFresh eventResumeFresh) {
            if (eventResumeFresh.flag >= 0) freshData();
          }
        });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("工作经历详情");
    toolbar.inflateMenu(R.menu.menu_work_exp);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        dialogSheet = new DialogSheet(getContext());
        if (experiencesEntity != null && experiencesEntity.is_authenticated) {
          dialogSheet.addButton(isHiden ? "取消隐藏" : "隐藏", new View.OnClickListener() {
            @Override public void onClick(View v) {
              dialogSheet.dismiss();
              if (isHiden) {
                showExp();
              } else {
                hideExp();
              }
            }
          });
        } else {
          dialogSheet.addButton("删除工作经历", new View.OnClickListener() {
            @Override public void onClick(View v) {
              dialogSheet.dismiss();
              showDialog();
            }
          });
        }
        dialogSheet.addButton("编辑", new View.OnClickListener() {
          @Override public void onClick(View v) {
            dialogSheet.dismiss();
            recruitRouter.editWorkExp(experiencesEntity);
          }
        });

        dialogSheet.show();
        return false;
      }
    });
  }

  private void freshData() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryWorkExp(mExpId)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcDataResponse<WorkExpWrap>>() {
          @Override public void call(QcDataResponse<WorkExpWrap> qcResponse) {
            Gym gym = qcResponse.data.experience.getGym();
            gymName.setText(gym.getName());
            if (!TextUtils.isEmpty(gym.getCityName())) {
              gymAddress.setText("|" + gym.getCityName());
            }
            //设置工作时间
            experiencesEntity = qcResponse.data.experience;
            isHiden = experiencesEntity.is_hidden;
            String start = DateUtils.Date2YYYYMMDD(
                DateUtils.formatDateFromServer(experiencesEntity.getStart()));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            int ye = c.get(Calendar.YEAR);
            if (ye == 3000) {
              start = start + "至今";
            } else {
              start = start + "至" + DateUtils.Date2YYYYMMDD(d);
            }
            gymTime.setText(start);
            if (experiencesEntity.is_authenticated()) {
              gymIdentify.setVisibility(View.VISIBLE);
            } else {
              gymIdentify.setVisibility(View.GONE);
            }
            Glide.with(getContext())
                .load(gym.getPhoto())
                .asBitmap()
                .into(new CircleImgWrapper(gymImg, getContext()));

            //是否隐藏
            if (experiencesEntity.is_authenticated()) { //认证条目
              workexpDetailHiden.setVisibility(View.VISIBLE);
              if (experiencesEntity.is_hidden()) {
                workexpDetailHiden.setBackgroundResource(R.color.orange);
                workexpDetailHiden.setText(getString(R.string.workexp_detail_hiden));
              } else {
                workexpDetailHiden.setBackgroundResource(R.color.colorPrimary);
                workexpDetailHiden.setText(getString(R.string.workexp_detail_unhiden));
              }
            } else { //自己添加的条目
              workexpDetailHiden.setVisibility(View.GONE);
            }

            workexpDetailPosition.setText(experiencesEntity.getPosition());
            workexpDetailDesc.setText(experiencesEntity.getDescription());
            if (!experiencesEntity.isGroup_is_hidden()) {
              workexpDetailGroupLayout.setVisibility(View.VISIBLE);
              workexpDetailGroupCount.setText(experiencesEntity.getGroup_course() + "");
              workexpDetailGroupServer.setText(experiencesEntity.getGroup_user() + "");
            } else {
              workexpDetailGroupLayout.setVisibility(View.GONE);
            }
            if (!experiencesEntity.isPrivate_is_hidden()) {
              workexpDetailPrivateLayout.setVisibility(View.VISIBLE);
              workexpDetailPrivateCount.setText(experiencesEntity.getPrivate_course() + "");
              workexpDetailPrivateServer.setText(experiencesEntity.getPrivate_user() + "");
            } else {
              workexpDetailPrivateLayout.setVisibility(View.GONE);
            }
            if (!experiencesEntity.isSale_is_hidden()) {
              workexpDetailSaleLayout.setVisibility(View.VISIBLE);
              workexpDetailSale.setText(experiencesEntity.getSale() + "");
            } else {
              workexpDetailSaleLayout.setVisibility(View.GONE);
            }
          }
        }, new NetWorkThrowable()));
  }

  private void showDialog() {
    if (delDialog == null) {
      delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
          .content("删除此条工作经历?")
          .positiveText("确定")
          .negativeText("取消")
          .onPositive((materialDialog, dialogAction) -> {
            showLoading();
            RxRegiste(qcRestRepository.createGetApi(PostApi.class)
                .delWorkExp(mExpId)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                  @Override public void call(QcResponse qcResponse) {
                    hideLoading();
                    if (qcResponse.status == 200) {
                      ToastUtils.show("删除成功");
                      RxBus.getBus().post(new EventResumeFresh(-1));
                      getActivity().onBackPressed();
                    } else {
                      ToastUtils.show("删除失败");
                    }
                  }
                }, new NetWorkThrowable()));
            materialDialog.dismiss();
          })
          .onNegative((materialDialog, dialogAction) -> materialDialog.dismiss())
          .cancelable(false)
          .build();
    }
    delDialog.show();
  }

  private void showExp() {

    RxRegiste(qcRestRepository.createGetApi(PostApi.class)
        .updateWorkExp(mExpId, new WorkExp.Builder().is_hidden(false).build())
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            hideLoading();
            if (qcResponse.status == 200) {
              freshData();
              RxBus.getBus().post(new EventResumeFresh(-1));
              ToastUtils.show("已取消隐藏");
            } else {
              ToastUtils.show("操作失败");
            }
          }
        }, new NetWorkThrowable()));
  }

  private void hideExp() {
    RxRegiste(qcRestRepository.createGetApi(PostApi.class)
        .updateWorkExp(mExpId, new WorkExp.Builder().is_hidden(true).build())
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            hideLoading();
            if (qcResponse.status == 200) {
              freshData();
              RxBus.getBus().post(new EventResumeFresh());
              ToastUtils.show("已隐藏");
            } else {
              ToastUtils.show("操作失败");
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public String getFragmentName() {
    return ResumeWorkExpPreviewFragment.class.getName();
  }
}
