package com.qingchengfit.fitcoach.fragment.course;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.action.SerPermisAction;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import cn.qingchengfit.bean.CourseDetail;
import com.qingchengfit.fitcoach.event.DelCourseEvent;
import com.qingchengfit.fitcoach.fragment.manage.StaffAppFragmentFragment;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseList;
import com.qingchengfit.fitcoach.items.CourseEmptyItem;
import com.qingchengfit.fitcoach.items.CourseItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 16/8/2.
 */
public class CourseListFragment extends VpFragment implements FlexibleAdapter.OnItemClickListener, View.OnClickListener {

    public CommonFlexAdapter mAdatper;
    @BindView(R.id.layout_toolbar) public RelativeLayout layoutToolbar;
    protected boolean mIsPrivate;
    protected boolean mIsload = false;
    @BindView(R.id.rv) RecycleViewWithNoImg rv;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    @Inject RestRepository restRepository;
    @Inject Brand brand;
    @Inject CoachService coachService;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private Unbinder unbinding;

    public static CourseListFragment newInstance(boolean isPrivate) {

        Bundle args = new Bundle();
        args.putBoolean("isPrivate", isPrivate);
        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsPrivate = getArguments().getBoolean("isPrivate");
        }
    }

    @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv_no_data_toolbar, container, false);
        unbinding = ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity) ((CourseActivity) getActivity()).getComponent().inject(this);
        View permissionV = judgePermission(inflater, container);
        if (permissionV != null) return permissionV;

        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbarTitle.setText(mIsPrivate ? "私教种类" : "团课种类");
        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(R.id.frag, AddCourseFragment.newInstance(mIsPrivate))
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
                return true;
            }
        });

        mAdatper = new CommonFlexAdapter(mDatas, this);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setAdapter(mAdatper);
        rv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });

        freshData();
        //删除监听
        RxBusAdd(DelCourseEvent.class).subscribe(new Action1<DelCourseEvent>() {
            @Override public void call(DelCourseEvent delCourseEvent) {
                if (delCourseEvent.isPrivate == mIsPrivate) {
                    delCourse(delCourseEvent.positon);
                }
            }
        });
        if (mIsload) {
            rv.stopLoading();
        }
        mIsload = true;
        return view;
    }

    public View judgePermission(LayoutInflater inflater, ViewGroup container) {
        if ((!mIsPrivate && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMSETTING)) || (mIsPrivate
            && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRISETTING))) {
            View v = inflater.inflate(R.layout.item_common_no_data, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            img.setImageResource(R.drawable.ic_no_permission);
            TextView hint = (TextView) v.findViewById(R.id.hint);
            hint.setText(R.string.sorry_for_no_permission);
            return v;
        } else {
            return null;
        }
    }

    public void freshData() {
        RxRegiste(restRepository.getGet_api()
            .qcGetCourses(App.coachid + "", GymUtils.getParams(coachService, brand), mIsPrivate ? 1 : 0)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(new Func1<QcResponseCourseList, Boolean>() {
                @Override public Boolean call(QcResponseCourseList qcResponseCourseList) {
                    if (qcResponseCourseList == null || qcResponseCourseList.data == null || qcResponseCourseList.data.courses == null) {
                        return false;
                    } else {
                        return true;
                    }
                }
            })
            .subscribe(new Action1<QcResponseCourseList>() {
                @Override public void call(QcResponseCourseList qcResponseCourseList) {
                    rv.stopLoading();
                    mDatas.clear();
                    if (qcResponseCourseList.data.courses == null || qcResponseCourseList.data.courses.size() == 0) {
                        mDatas.add(new CourseEmptyItem(CourseListFragment.this, String.format(Locale.CHINA, "暂无%s种类",
                            mIsPrivate ? getString(R.string.course_type_private) : getString(R.string.course_type_group)), mIsPrivate));
                    } else {
                        for (int i = 0; i < qcResponseCourseList.data.courses.size(); i++) {
                            mDatas.add(new CourseItem(qcResponseCourseList.data.courses.get(i)));
                        }
                    }
                    mAdatper.notifyDataSetChanged();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void delCourse(int pos) {
        CourseDetail courseDetail = ((CourseItem) mAdatper.getItem(pos)).courseDetail;

        //if (GymUtils.isInBrand(coachService)){
        //    if (courseDetail.getPermission() == 1){
        //        showDelDialog(String.format(Locale.CHINA, getString(R.string.alert_del_course), courseDetail.getShops().size()) ,pos);
        //    }else {
        //        showDelFailed(String.format(Locale.CHINA, getString(R.string.alert_del_course_forbid), courseDetail.getShops().size()));
        //    }
        //}else {
        if (courseDetail.getShops().size() > 1) {
            //showDelFailed("该课程适用于多家场馆,请到【连锁运营】里进行删除");
            StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
        } else {
            showDelDialog("删除后，已有的排期和课程预约都不会受到影响。", pos);
        }

        //}
    }

    public void showDelDialog(String content, final int pos) {
        final CourseDetail courseDetail = ((CourseItem) mAdatper.getItem(pos)).courseDetail;
        new MaterialDialog.Builder(getContext()).title("确定删除该课程类型")
            .content(content)
            .autoDismiss(true)
            .canceledOnTouchOutside(true)
            .positiveText(R.string.common_del)
            .negativeText(R.string.pickerview_cancel)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showLoading();
                    RxRegiste(restRepository.getPost_api()
                        .qcDelCourse(App.coachid + "", courseDetail.getId(), GymUtils.getParams(coachService, brand))
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<QcResponse>() {
                            @Override public void call(QcResponse qcResponse) {
                                hideLoading();
                                if (ResponseConstant.checkSuccess(qcResponse)) {
                                    mAdatper.removeItem(pos);
                                } else {
                                    ToastUtils.show("删除失败:" + qcResponse.getMsg());
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override public void call(Throwable throwable) {
                                hideLoading();
                            }
                        }));
                }
            })
            .show();
    }

    public void showDelFailed(String content) {
        new AlertDialogWrapper.Builder(getContext()).setTitle(R.string.permission_forbid_del)
            .setMessage(content)
            .setNegativeButton(R.string.common_i_konw, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .show();
    }

    public void setEditMode() {
        for (int i = 0; i < mAdatper.getItemCount(); i++) {
            if (mAdatper.getItem(i) instanceof CourseItem) ((CourseItem) mAdatper.getItem(i)).setEditable(true);
        }
        mAdatper.notifyDataSetChanged();
    }

    public void cancelEdit() {
        for (int i = 0; i < mAdatper.getItemCount(); i++) {
            if (((CommonFlexAdapter) rv.getAdapter()).getItem(i) instanceof CourseItem) {
                ((CourseItem) mAdatper.getItem(i)).setEditable(false);
            }
        }
        mAdatper.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return CourseListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinding.unbind();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdatper.getItem(position) instanceof CourseItem) {

            getFragmentManager().beginTransaction()
                .replace(R.id.frag, CourseDetailFragment.newInstance(((CourseItem) mAdatper.getItem(position)).courseDetail))
                .addToBackStack(null)
                .commit();
        }

        return true;
    }

    @Override public String getTitle() {
        return getArguments().getBoolean("isPrivate") ? "私教种类" : "团课种类";
    }

    @Override public void onClick(View view) {
        if (view.getId() == R.id.add_course_btn) {
            if ((!mIsPrivate && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMSETTING_CAN_WRITE)) || (mIsPrivate
                && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRISETTING_CAN_WRITE))) {
                showAlert(R.string.sorry_no_permission);
                return;
            }

            if (getActivity() instanceof CourseActivity) {
                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag, AddCourseFragment.newInstance(mIsPrivate))
                    .addToBackStack(getFragmentName())
                    .commit();
            }
        }
    }
}
