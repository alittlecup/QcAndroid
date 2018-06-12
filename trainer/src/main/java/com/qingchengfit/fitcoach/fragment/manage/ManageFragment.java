package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.bean.FunctionBean;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saasbase.course.batch.views.BatchListTrainerSpanParams;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.Utils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.PopFromBottomActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.component.ItemDecorationAlbumColumns;
import com.qingchengfit.fitcoach.event.EventChooseGym;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponsePermission;
import com.qingchengfit.fitcoach.items.DailyWorkItem;
import com.qingchengfit.fitcoach.items.ManageWorkItem;
import com.qingchengfit.fitcoach.items.UseStaffAppItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Paper on 16/11/10.
 */
public class ManageFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

	RecyclerView recyclerview;
  List<AbstractFlexibleItem> mData = new ArrayList<>();
	RecyclerView recyclerview2;
	TextView title;
	ImageView angleShow;
	TextView nameBrand;
	TextView addressPhone;
	LinearLayout gymInfoLayout;
	ImageView shopImg;
	LinearLayout gymLayout;

  @Inject GymWrapper gymWrapper;
  @Inject RepoCoachServiceImpl repoCoachService;
  @Inject LoginStatus loginStatus;
	TextView changeGym;
	TextView dataoff;
	TextView renewal;

  private CommonFlexAdapter mAdapter;

  private DialogList dialogList;
  private QuitGymFragment quiteGym;
  private Gson gson = new Gson();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_manange, container, false);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    recyclerview2 = (RecyclerView) view.findViewById(R.id.recyclerview2);
    title = (TextView) view.findViewById(R.id.title);
    angleShow = (ImageView) view.findViewById(R.id.angle_show);
    nameBrand = (TextView) view.findViewById(R.id.name_brand);
    addressPhone = (TextView) view.findViewById(R.id.address_phone);
    gymInfoLayout = (LinearLayout) view.findViewById(R.id.gym_info_layout);
    shopImg = (ImageView) view.findViewById(R.id.shop_img);
    gymLayout = (LinearLayout) view.findViewById(R.id.gym_layout);
    changeGym = (TextView) view.findViewById(R.id.change_gym);
    dataoff = (TextView) view.findViewById(R.id.dataoff);
    renewal = (TextView) view.findViewById(R.id.renewal);
    view.findViewById(R.id.change_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ManageFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ManageFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.angle_show).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ManageFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.renewal).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ManageFragment.this.onClick();
      }
    });
    view.findViewById(R.id.action_flow).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ManageFragment.this.onClickFlow();
      }
    });

    mData.clear();
    mData.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.moudule_service_private)
            .text("私教排期")
            .build()));
    mData.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.moudule_service_group).text("团课排期").build()));
    mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_student)
        .text(getString(R.string.student))
        .build()));
    mData.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ic_img_statement_signin)
            .text(getString(R.string.course_statement))
            .build()));
    mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_sale_statement)
        .text(getString(R.string.sale_statement))
        .build()));
    mData.add(new DailyWorkItem(null));
    //mData.add(new DailyWorkItem(
    //    new FunctionBean.Builder().resImg(R.drawable.ic_template_coursepaln).text(getString(R.string.course_plan)).build()));
    mAdapter = new CommonFlexAdapter(mData, this);
    GridLayoutManager manager1 = new GridLayoutManager(getContext(), 3);
    manager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    recyclerview.setLayoutManager(manager1);
    recyclerview.addItemDecoration(new ItemDecorationAlbumColumns(1, 3));
    recyclerview.setHasFixedSize(true);
    recyclerview.setNestedScrollingEnabled(false);
    recyclerview.setAdapter(mAdapter);

    List<AbstractFlexibleItem> data2 = new ArrayList<>();
    data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_module_card_grey)
        .text(getString(R.string.student_card_manage))
        .build()));
    data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_module_staff_grey)
        .text(getString(R.string.manage_salers))
        .build()));
    data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_module_sales_grey)
        .text(getString(R.string.manage_staffs))
        .build()));
    data2.add(new ManageWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ic_module_authority_grey)
            .text(getString(R.string.manage_permission))
            .build()));
    data2.add(new ManageWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ic_module_checkin_grey)
            .text(getString(R.string.manage_signin))
            .build()));
    data2.add(new ManageWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ic_module_activity_grey)
            .text(getString(R.string.manage_acitivity))
            .build()));
    data2.add(new UseStaffAppItem());
    CommonFlexAdapter adapter2 =
        new CommonFlexAdapter(data2, new FlexibleAdapter.OnItemClickListener() {
          @Override public boolean onItemClick(int position) {
            if (data2.get(position) instanceof UseStaffAppItem) {
              try {
                Utils.openApp(getActivity());
              } catch (Exception e) {
                try {
                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse("http://fir.im/qcfit"));
                  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(i);
                } catch (Exception e2) {

                }
              }
            }
            return true;
          }
        });

    GridLayoutManager manager2 = new GridLayoutManager(getContext(), 2);
    recyclerview2.addItemDecoration(new ItemDecorationAlbumColumns(1, 2));
    manager2.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        if (data2.get(position) instanceof ManageWorkItem) {
          return 1;
        } else {
          return 2;
        }
      }
    });
    recyclerview2.setLayoutManager(manager2);
    recyclerview2.setHasFixedSize(true);
    recyclerview2.setNestedScrollingEnabled(false);
    recyclerview2.setAdapter(adapter2);

    RxBusAdd(EventChooseGym.class).subscribe(new Action1<EventChooseGym>() {
      @Override public void call(EventChooseGym coachService) {
        if (coachService != null) {
          gymWrapper.setCoachService(coachService.getCoachService());
          PreferenceUtils.setPrefString(getContext(), "coachservice_id_str",
              gymWrapper.getCoachService().getId());
          setGymInfo(coachService.getCoachService());
        }
      }
    }, new HttpThrowable());
    String s = PreferenceUtils.getPrefString(getContext(), App.coachid + "permission", "");
    if (s != null && !s.isEmpty()) {
      QcResponsePermission.Data d = gson.fromJson(s, QcResponsePermission.Data.class);
      updatePermission(d);
    }
    getServer();
    isInit = true;

    changeGym.setOnClickListener(v -> {
      Intent toGym = new Intent(getActivity(), PopFromBottomActivity.class);
      toGym.putExtra("service", gymWrapper.getCoachService());
      startActivity(toGym);
    });

    return view;
  }

  private void setGymInfo(CoachService coachService) {
    if (getContext() == null) return;
    title.setText(coachService.name);
    Glide.with(getContext())
        .load(coachService.photo)
        .asBitmap()
        .into(new CircleImgWrapper(shopImg, getContext()));
    nameBrand.setText(coachService.brand_name);
    addressPhone.setText(coachService.getName());
    HashMap<String, Object> params = gymWrapper.getParams();
    showLoading();
    if (loginStatus.isLogined()) {
      RxRegiste(QcCloudClient.getApi().getApi.qcGetPermission(App.coachid + "", params)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponsePermission>() {
            @Override public void call(QcResponsePermission qcResponsePermission) {
              hideLoading();
              if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                String ps = gson.toJson(qcResponsePermission.data, QcResponsePermission.Data.class);
                PreferenceUtils.setPrefString(getContext(), App.coachid + "permission", ps);
                updatePermission(qcResponsePermission.data);
              } else {
                ToastUtils.show("权限更新失败 :" + qcResponsePermission.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    }
  }

  public void updatePermission(QcResponsePermission.Data data) {
    CurentPermissions.newInstance().permissionList.clear();
    for (int i = 0; i < data.permissions.size(); i++) {
      CurentPermissions.newInstance().permissionList.put(data.permissions.get(i).key,
          data.permissions.get(i).value);
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  public void getServer() {
    //兼容低版本
    Long ccid = 0l;
    try {
      ccid = PreferenceUtils.getPrefLong(getContext(), "coachservice_id", 0L);
    } catch (Exception e) {

    }
    String newCcid = PreferenceUtils.getPrefString(getContext(), "coachservice_id_str", "");
    if (ccid != 0 && TextUtils.isEmpty(newCcid)) {
      newCcid = ccid + "";
    }

    final String curCoachId = newCcid;
    repoCoachService.readAllServices()
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .subscribe(coachServices -> {
          if (coachServices.size() > 0) {
            gymWrapper.setCoachService(coachServices.get(0));
            for (CoachService s : coachServices) {
              if (s.getId().equals(curCoachId)) {
                gymWrapper.setCoachService(s);
                break;
              }
            }
            setGymInfo(gymWrapper.getCoachService());
          } else {//无场馆状态
            RxBus.getBus().post(new EventLoginChange());
          }
        },new HttpThrowable());
  }

  @Override protected void lazyLoad() {

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if (mAdapter.getItem(position) instanceof DailyWorkItem) {
      if (((DailyWorkItem) mAdapter.getItem(position)).bean == null) return true;
      int res = ((DailyWorkItem) mAdapter.getItem(position)).bean.resImg;
      switch (res) {
        case R.drawable.moudule_service_group://排课
          if (CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR)) {

            routeTo("course", "/batch/list/",
                BatchListTrainerSpanParams.builder().mType(0).build());
          } else {
            showAlert(getString(R.string.sorry_no_permission));
          }

          break;
        case R.drawable.moudule_service_private://课程种类
          if (CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PRIARRANGE_CALENDAR)) {

            routeTo("course", "/batch/list/",
                BatchListTrainerSpanParams.builder().mType(1).build());
          } else {
            showAlert(getString(R.string.sorry_no_permission));
          }
          break;
        case R.drawable.ic_users_student:
          if (CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS)
              || CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS)) {
            Intent toStudent = new Intent(getActivity(), FragActivity.class);
            toStudent.putExtra("type", 9);
            toStudent.putExtra("service", gymWrapper.getCoachService());
            startActivity(toStudent);
          } else {
            showAlert(getString(R.string.sorry_no_permission));
          }
          break;
        case R.drawable.ic_img_statement_signin:
          if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.COST_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return true;
          }
          Intent toCourseStatement = new Intent(getActivity(), FragActivity.class);
          toCourseStatement.putExtra("type", 0);
          toCourseStatement.putExtra("service", gymWrapper.getCoachService());
          startActivity(toCourseStatement);
          break;
        case R.drawable.ic_sale_statement:
          if (!CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PERSONAL_SALES_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return true;
          }
          Intent tosale = new Intent(getActivity(), FragActivity.class);
          tosale.putExtra("type", 1);
          tosale.putExtra("service", gymWrapper.getCoachService());
          startActivity(tosale);

          break;
        case R.drawable.ic_template_coursepaln:
          if (!CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PLANSSETTING)) {
            showAlert(R.string.alert_permission_forbid);
            return true;
          }
          Intent toPlan = new Intent(getActivity(), FragActivity.class);
          toPlan.putExtra("type", 8);
          toPlan.putExtra("service", gymWrapper.getCoachService());
          startActivity(toPlan);
          break;
        default:
      }
    }
    return true;
  }

 public void onClick(View view) {
    switch (view.getId()) {
      case R.id.change_gym:
        //Intent toGym = new Intent(getActivity(), PopFromBottomActivity.class);
        //toGym.putExtra("service", gymWrapper.getCoachService());
        //startActivity(toGym);
        break;
      case R.id.title:
      case R.id.angle_show:

        break;
    }
  }

 public void onClick() {
    if (gymWrapper.getCoachService() != null) {
      RxRegiste(QcCloudClient.getApi().getApi.qcStaffPmission(App.coachid + "",
          GymUtils.getParams(gymWrapper.getCoachService()))
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponsePermission>() {
            @Override public void call(QcResponsePermission qcResponsePermission) {
              if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                if (qcResponsePermission.data.permissions != null
                    && qcResponsePermission.data.permissions.size() > 0) {
                  for (int i = 0; i < qcResponsePermission.data.permissions.size(); i++) {
                    if (qcResponsePermission.data.permissions.get(i).key.equalsIgnoreCase(
                        PermissionServerUtils.STUDIO_LIST_CAN_CHANGE)
                        && qcResponsePermission.data.permissions.get(i).value) {
                      Intent toEdit = new Intent(getContext(), FragActivity.class);
                      toEdit.putExtra("service", gymWrapper.getCoachService());
                      toEdit.putExtra("type", 13);
                      startActivity(toEdit);
                      return;
                    }
                  }
                  ToastUtils.show(getString(R.string.alert_permission_forbid));
                } else {
                  ToastUtils.show(getString(R.string.alert_permission_forbid));
                }
              } else {
                ToastUtils.show(getString(R.string.alert_permission_forbid));
              }
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
              ToastUtils.show(getString(R.string.alert_permission_forbid));
            }
          }));
      //StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");

    }
  }

 public void onClickFlow() {
    if (dialogList == null) {
      dialogList = new DialogList(getContext());
      ArrayList<String> flows = new ArrayList<>();
      flows.add("离职退出该场馆");
      dialogList.list(flows, this);
    }
    dialogList.show();
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (dialogList != null) {
      dialogList.dismiss();

      if (quiteGym == null) {
        quiteGym = new QuitGymFragmentBuilder(gymWrapper.getCoachService()).build();
        quiteGym.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            showLoading();
            RxRegiste(QcCloudClient.getApi().postApi.qcQuitGym(App.coachid + "",
                GymUtils.getParams(gymWrapper.getCoachService()))
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                  @Override public void call(QcResponse qcResponse) {
                    hideLoading();
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                      ToastUtils.show("退出健身房成功！");
                      repoCoachService.deleteServiceByIdModel(gymWrapper.id(), gymWrapper.model());
                      getServer();
                    } else {
                      ToastUtils.show(qcResponse.getMsg());
                    }
                  }
                }, new Action1<Throwable>() {
                  @Override public void call(Throwable throwable) {
                    hideLoading();
                    ToastUtils.show("error!");
                  }
                }));
          }
        });
      }
      quiteGym.show(getFragmentManager(), "");
    }
  }
}
