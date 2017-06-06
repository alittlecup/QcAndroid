package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.event.EventLoginChange;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.Utils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.PopFromBottomActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CurentPermissions;
import com.qingchengfit.fitcoach.bean.FunctionBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.component.ItemDecorationAlbumColumns;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
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
public class ManageFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    @BindView(R.id.recyclerview2) RecyclerView recyclerview2;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.angle_show) ImageView angleShow;
    @BindView(R.id.name_brand) TextView nameBrand;
    @BindView(R.id.address_phone) TextView addressPhone;
    @BindView(R.id.gym_info_layout) LinearLayout gymInfoLayout;
    @BindView(R.id.shop_img) ImageView shopImg;
    @BindView(R.id.gym_layout) LinearLayout gymLayout;


    @Inject GymWrapper gymWrapper;
    @Inject RepoCoachServiceImpl repoCoachService;


    private CommonFlexAdapter mAdapter;
    private Unbinder unbinder;
    private DialogList dialogList;
    private QuitGymFragment quiteGym;
    private Gson gson = new Gson();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manange, container, false);
        unbinder = ButterKnife.bind(this, view);
        mData.clear();
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.moudule_service_private).text("私教排期").build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.moudule_service_group).text("团课排期").build()));
        mData.add(
            new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_student).text(getString(R.string.student)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_img_statement_signin).text(getString(R.string.course_statement)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_sale_statement).text(getString(R.string.sale_statement)).build()));
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
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_card_grey).text(getString(R.string.student_card_manage)).build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_staff_grey).text(getString(R.string.manage_salers)).build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_sales_grey).text(getString(R.string.manage_staffs)).build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_authority_grey).text(getString(R.string.manage_permission)).build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_checkin_grey).text(getString(R.string.manage_signin)).build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_activity_grey).text(getString(R.string.manage_acitivity)).build()));
        data2.add(new UseStaffAppItem());
        CommonFlexAdapter adapter2 = new CommonFlexAdapter(data2, new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int position) {
                if (data2.get(position) instanceof UseStaffAppItem) {
                    try {
                        Utils.openApp(getActivity());
                    } catch (Exception e) {
                        try {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("http://fir.im/qcfit"));
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

        RxBusAdd(CoachService.class).subscribe(new Action1<CoachService>() {
            @Override public void call(CoachService coachService) {
                if (coachService != null) {
                    PreferenceUtils.setPrefLong(getContext(), "coachservice_id", gymWrapper.getCoachService().getId());
                    setGymInfo(coachService);
                }
            }
        }, throwable -> {
        });
        String s = PreferenceUtils.getPrefString(getContext(), App.coachid + "permission", "");
        if (s != null && !s.isEmpty()) {
            QcResponsePermission.Data d = gson.fromJson(s, QcResponsePermission.Data.class);
            updatePermission(d);
        }
        getServer();
        isInit = true;
        return view;
    }

    private void setGymInfo(CoachService coachService) {
        title.setText(coachService.name);
        Glide.with(getContext()).load(coachService.photo).asBitmap().into(new CircleImgWrapper(shopImg, getContext()));
        nameBrand.setText(coachService.brand_name);
        addressPhone.setText(coachService.getName());
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(QcCloudClient.getApi().getApi.qcGetPermission(App.coachid + "", params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponsePermission>() {
                @Override public void call(QcResponsePermission qcResponsePermission) {
                    if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                        String ps = gson.toJson(qcResponsePermission.data, QcResponsePermission.Data.class);
                        PreferenceUtils.setPrefString(getContext(), App.coachid + "permission", ps);
                        updatePermission(qcResponsePermission.data);
                    } else {
                        cn.qingchengfit.utils.ToastUtils.show("权限更新失败 :" + qcResponsePermission.getMsg());
                    }
                }
            }, throwable -> {
                cn.qingchengfit.utils.ToastUtils.show("权限更新失败");
            }));
    }

    public void updatePermission(QcResponsePermission.Data data) {
        CurentPermissions.newInstance().permissionList.clear();
        for (int i = 0; i < data.permissions.size(); i++) {
            CurentPermissions.newInstance().permissionList.put(data.permissions.get(i).key, data.permissions.get(i).value);
        }

    }

    @Override public void onResume() {
        super.onResume();
    }

    public void getServer() {
        long curCoachId = PreferenceUtils.getPrefLong(getContext(), "coachservice_id", 0l);
        repoCoachService.readAllServices().observeOn(AndroidSchedulers.mainThread()).subscribe(coachServices -> {
            if (coachServices.size() > 0) {
                gymWrapper.setCoachService(coachServices.get(0));
                for (CoachService s : coachServices) {
                    if (s.getId() == curCoachId) {
                        gymWrapper.setCoachService(s);
                        break;
                    }
                }
            } else {//无场馆状态
                RxBus.getBus().post(new EventLoginChange());
                }
        });

    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof DailyWorkItem) {
            if (((DailyWorkItem) mAdapter.getItem(position)).bean == null) return true;
            int res = ((DailyWorkItem) mAdapter.getItem(position)).bean.resImg;
            switch (res) {
                case R.drawable.moudule_service_group://排课
                    if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR)
                        || CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PRIARRANGE_CALENDAR)) {

                        Intent toGuide = new Intent(getActivity(), CourseActivity.class);
                        toGuide.putExtra("to", CourseActivity.TO_GROUP_BATCH);
                        toGuide.putExtra("service", gymWrapper.getCoachService());
                        startActivity(toGuide);
                    } else {
                        showAlert(getString(R.string.sorry_no_permission));
                    }

                    break;
                case R.drawable.moudule_service_private://课程种类
                    if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.TEAMSETTING)
                        || CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PRISETTING)) {

                        Intent toCourse = new Intent(getActivity(), CourseActivity.class);
                        toCourse.putExtra("service", gymWrapper.getCoachService());
                        toCourse.putExtra("to", CourseActivity.TO_PRIVATE_BATCH);
                        startActivity(toCourse);
                    } else {
                        showAlert(getString(R.string.sorry_no_permission));
                    }
                    break;
                case R.drawable.ic_users_student:
                    if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.MANAGE_MEMBERS)
                        || CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS)) {
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
                    if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_SALES_REPORT)) {
                        showAlert(R.string.alert_permission_forbid);
                        return true;
                    }
                    Intent tosale = new Intent(getActivity(), FragActivity.class);
                    tosale.putExtra("type", 1);
                    tosale.putExtra("service", gymWrapper.getCoachService());
                    startActivity(tosale);

                    break;
                case R.drawable.ic_template_coursepaln:
                    if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PLANSSETTING)) {
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

    @OnClick({ R.id.change_gym, R.id.title, R.id.angle_show }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_gym:
                Intent toGym = new Intent(getActivity(), PopFromBottomActivity.class);
                toGym.putExtra("service", gymWrapper.getCoachService());
                startActivity(toGym);
                break;
            case R.id.title:
            case R.id.angle_show:

                break;
        }
    }

    @OnClick(R.id.renewal) public void onClick() {
        if (gymWrapper.getCoachService() != null) {
            RxRegiste(QcCloudClient.getApi().getApi.qcStaffPmission(App.coachid + "", GymUtils.getParams(gymWrapper.getCoachService()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponsePermission>() {
                    @Override public void call(QcResponsePermission qcResponsePermission) {
                        if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                            if (qcResponsePermission.data.permissions != null && qcResponsePermission.data.permissions.size() > 0) {
                                for (int i = 0; i < qcResponsePermission.data.permissions.size(); i++) {
                                    if (qcResponsePermission.data.permissions.get(i).key.equalsIgnoreCase(
                                        PermissionServerUtils.STUDIO_LIST_CAN_CHANGE) && qcResponsePermission.data.permissions.get(
                                        i).value) {
                                        Intent toEdit = new Intent(getContext(), FragActivity.class);
                                        toEdit.putExtra("service", gymWrapper.getCoachService());
                                        toEdit.putExtra("type", 13);
                                        startActivity(toEdit);
                                        return;
                                    }
                                }
                                cn.qingchengfit.utils.ToastUtils.show(getString(R.string.alert_permission_forbid));
                            } else {
                                cn.qingchengfit.utils.ToastUtils.show(getString(R.string.alert_permission_forbid));
                            }
                        } else {
                            cn.qingchengfit.utils.ToastUtils.show(getString(R.string.alert_permission_forbid));
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        cn.qingchengfit.utils.ToastUtils.show(getString(R.string.alert_permission_forbid));
                    }
                }));
            //StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");

        }
    }

    @OnClick(R.id.action_flow) public void onClickFlow() {
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
                        RxRegiste(
                            QcCloudClient.getApi().postApi.qcQuitGym(App.coachid + "", GymUtils.getParams(gymWrapper.getCoachService()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<QcResponse>() {
                                    @Override public void call(QcResponse qcResponse) {
                                        hideLoading();
                                        if (ResponseConstant.checkSuccess(qcResponse)) {
                                            cn.qingchengfit.utils.ToastUtils.show("退出健身房成功！");
                                            repoCoachService.deleteServiceByIdModel(gymWrapper.id(), gymWrapper.model());
                                            getServer();
                                        } else {
                                            cn.qingchengfit.utils.ToastUtils.show(qcResponse.getMsg());
                                    }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override public void call(Throwable throwable) {
                                        hideLoading();
                                        cn.qingchengfit.utils.ToastUtils.show("error!");
                                    }
                                }));
                    }
                });
            }
            quiteGym.show(getFragmentManager(), "");
        }
    }
}
