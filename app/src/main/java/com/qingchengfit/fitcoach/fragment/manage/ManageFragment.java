package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CurentPermissions;
import com.qingchengfit.fitcoach.bean.FunctionBean;
import com.qingchengfit.fitcoach.bean.base.PermissionServerUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.ItemDecorationAlbumColumns;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.batch.BatchActivity;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponsePermission;
import com.qingchengfit.fitcoach.items.DailyWorkItem;
import com.qingchengfit.fitcoach.items.ManageWorkItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ManageFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

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
    private CommonFlexAdapter mAdapter;
    private Unbinder unbinder;
    private CoachService mCoachService;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manange, container, false);
        unbinder = ButterKnife.bind(this, view);
        mData.add(
            new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_weight).text(getString(R.string.course_batch)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_category_course).text(getString(R.string.course_types)).build()));
        mData.add(
            new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_student).text(getString(R.string.student)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_img_statement_signin).text(getString(R.string.course_statement)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_sale_statement).text(getString(R.string.sale_statement)).build()));
        mData.add(new DailyWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_template_coursepaln).text(getString(R.string.course_plan)).build()));
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
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_staff_grey).text(getString(R.string.manage_salers))
                .build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_sales_grey).text(getString(R.string.manage_staffs)).build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_module_authority_grey)
            .text(getString(R.string.manage_permission))
            .build()));
        data2.add(new ManageWorkItem(
            new FunctionBean.Builder().resImg(R.drawable.ic_module_checkin_grey).text(getString(R.string.manage_signin)).build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_module_activity_grey)
            .text(getString(R.string.manage_acitivity))
            .build()));
        CommonFlexAdapter adapter2 = new CommonFlexAdapter(data2, new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int position) {
                StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
                return true;
            }
        });

        //GridLayoutManager manager2 = new GridLayoutManager(getContext(), 2);
        //recyclerview2.addItemDecoration(new ItemDecorationAlbumColumns(1, 2));
        recyclerview2.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayout.VERTICAL));
        recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview2.setHasFixedSize(true);
        recyclerview2.setNestedScrollingEnabled(false);
        recyclerview2.setAdapter(adapter2);
        RxBusAdd(CoachService.class).subscribe(new Action1<CoachService>() {
            @Override public void call(CoachService coachService) {
                if (coachService != null) {
                    mCoachService = coachService;
                    ((Main2Activity) getActivity()).setCoachService(mCoachService);
                    title.setText(coachService.name);
                    Glide.with(getContext()).load(coachService.photo).asBitmap().into(new CircleImgWrapper(shopImg,getContext()));
                    nameBrand.setText(coachService.brand_name);
                    addressPhone.setText(coachService.getName());


                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("id", mCoachService.getId());
                    params.put("model", mCoachService.getModel());
                    RxRegiste(QcCloudClient.getApi().getApi.qcGetPermission(App.coachid + "", params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<QcResponsePermission>() {
                            @Override public void call(QcResponsePermission qcResponsePermission) {
                                if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                                    CurentPermissions.newInstance().permissionList.clear();
                                    for (int i = 0; i < qcResponsePermission.data.permissions.size(); i++) {
                                        CurentPermissions.newInstance().permissionList.put(qcResponsePermission.data.permissions.get(i).key,
                                            qcResponsePermission.data.permissions.get(i).value);
                                    }
                                } else {
                                    cn.qingchengfit.widgets.utils.ToastUtils.show("权限更新失败 :" + qcResponsePermission.getMsg());
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override public void call(Throwable throwable) {
                                cn.qingchengfit.widgets.utils.ToastUtils.show("权限更新失败");
                            }
                        }));
                }
            }
        });

        return view;
    }




    @Override public void onResume() {
        super.onResume();
        getServer();
    }

    public void getServer() {
        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcCoachServiceResponse>() {
                @Override public void call(QcCoachServiceResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.services == null || qcResponse.data.services.size() == 0) {
                            Intent toGuide = new Intent(getActivity(), GuideActivity.class);
                            startActivity(toGuide);
                            getActivity().finish();
                            return;
                        }
                        if (qcResponse.data.services != null && qcResponse.data.services.size() > 0) {
                            if (getActivity() instanceof Main2Activity && ((Main2Activity) getActivity()).getCoachService() != null) {
                                CoachService coachService = ((Main2Activity) getActivity()).getCoachService();
                                for (int i = 0; i < qcResponse.data.services.size(); i++) {
                                    if (coachService.id == qcResponse.data.services.get(i).id && coachService.model.equalsIgnoreCase(
                                        qcResponse.data.services.get(i).model)) {
                                        mCoachService = qcResponse.data.services.get(i);
                                        break;
                                    }
                                }
                            } else {
                                mCoachService = qcResponse.data.services.get(0);
                            }
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("id", mCoachService.getId());
                            params.put("model", mCoachService.getModel());
                            RxRegiste(QcCloudClient.getApi().getApi.qcGetPermission(App.coachid + "", params)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<QcResponsePermission>() {
                                    @Override public void call(QcResponsePermission qcResponsePermission) {
                                        if (ResponseConstant.checkSuccess(qcResponsePermission)) {
                                            CurentPermissions.newInstance().permissionList.clear();
                                            for (int i = 0; i < qcResponsePermission.data.permissions.size(); i++) {
                                                CurentPermissions.newInstance().permissionList.put(qcResponsePermission.data.permissions.get(i).key,
                                                    qcResponsePermission.data.permissions.get(i).value);
                                            }
                                        } else {
                                            cn.qingchengfit.widgets.utils.ToastUtils.show("权限更新失败 :" + qcResponsePermission.getMsg());
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override public void call(Throwable throwable) {
                                        cn.qingchengfit.widgets.utils.ToastUtils.show("权限更新失败");
                                    }
                                }));

                            title.setText(mCoachService.name);
                            addressPhone.setText(mCoachService.getName());
                            nameBrand.setText(mCoachService.getBrand_name());
                            Glide.with(getContext()).load(mCoachService.photo).asBitmap().into(new CircleImgWrapper(shopImg,getContext()));
                        } else {
                            ToastUtils.show("服务器错误");
                        }
                    } else {
                        ToastUtils.show("服务器错误");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    ToastUtils.show("服务器错误");
                }
            }));
    }
    //
    //@OnClick({ R.id.title, R.id.angle_show }) public void onTilteClick() {
    //    gymLayout.setPivotY(0);
    //    if (gymLayout.getVisibility() == View.VISIBLE) {
    //        gymLayout.setVisibility(View.GONE);
    //        ViewCompat.setRotation(angleShow, 180);
    //    } else {
    //        gymLayout.setVisibility(View.VISIBLE);
    //        ViewCompat.setRotation(angleShow, 0);
    //    }
    //    ViewCompat.animate(angleShow).rotationBy(180).setDuration(300).start();
    //}

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof DailyWorkItem) {
            int res = ((DailyWorkItem) mAdapter.getItem(position)).bean.resImg;
            switch (res) {
                case R.drawable.ic_weight://排课
                    if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR)
                        || CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PRIARRANGE_CALENDAR)) {

                        Intent toGuide = new Intent(getActivity(), BatchActivity.class);
                        toGuide.putExtra("service", mCoachService);
                        startActivity(toGuide);
                    } else {
                        showAlert(getString(R.string.sorry_no_permission));
                    }

                    break;
                case R.drawable.ic_category_course://课程种类
                    if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.TEAMSETTING)
                        || CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PRISETTING)) {

                        Intent toCourse = new Intent(getActivity(), CourseActivity.class);
                        toCourse.putExtra("service", mCoachService);
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
                        toStudent.putExtra("service", mCoachService);
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
                    toCourseStatement.putExtra("service", mCoachService);
                    startActivity(toCourseStatement);
                    break;
                case R.drawable.ic_sale_statement:
                    if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_SALES_REPORT)) {
                        showAlert(R.string.alert_permission_forbid);
                        return true;
                    }
                    Intent tosale = new Intent(getActivity(), FragActivity.class);
                    tosale.putExtra("type", 1);
                    tosale.putExtra("service", mCoachService);
                    startActivity(tosale);

                    break;
                case R.drawable.ic_template_coursepaln:
                    if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PLANSSETTING)) {
                        showAlert(R.string.alert_permission_forbid);
                        return true;
                    }
                    Intent toPlan = new Intent(getActivity(), FragActivity.class);
                    toPlan.putExtra("type", 8);
                    toPlan.putExtra("service", mCoachService);
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
                Intent toGym = new Intent(getActivity(), ChooseActivity.class);
                toGym.putExtra("to", ChooseActivity.TO_CHOSSE_GYM);
                toGym.putExtra("service",mCoachService);
                startActivity(toGym);
                break;
            case R.id.title:
            case R.id.angle_show:

                break;
        }
    }

    @OnClick(R.id.renewal) public void onClick() {
        StaffAppFragmentFragment.newInstance().show(getFragmentManager(),"");
        //Intent toEdit = new Intent(getContext(),FragActivity.class);
        //toEdit.putExtra("service",mCoachService);
        //toEdit.putExtra("type",13);
        //startActivity(toEdit);
    }
}
