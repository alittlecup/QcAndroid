package cn.qingchengfit.staffkit.views.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CourseType;
import cn.qingchengfit.model.responese.CourseTypes;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.DelCourseEvent;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CourseEmptyItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CourseItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class CourseListFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, TitleFragment, View.OnClickListener {

    @BindView(R.id.rv) RecycleViewWithNoImg rv;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;

    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    @Inject RestRepository restRepository;
    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;

    private CommonFlexAdapter mAdatper;
    private boolean mIsPrivate;
    private boolean isLoad = false;

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
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        if ((!mIsPrivate && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMSETTING)) || (mIsPrivate
            && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRISETTING))) {
            View v = inflater.inflate(R.layout.item_common_no_data, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            img.setImageResource(R.drawable.ic_no_permission);
            TextView hint = (TextView) v.findViewById(R.id.hint);
            hint.setText(R.string.sorry_for_no_permission);
            return v;
        }

        initBus();

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
        if (isLoad) rv.stopLoading();
        isLoad = true;

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);

        if (getParentFragment() != null) {
            toolbarLayout.setVisibility(View.GONE);
        } else {
            toolbarLayout.setVisibility(View.VISIBLE);
            toolbarTitile.setText(mIsPrivate ? "私教" : "团课");
            toolbar.inflateMenu(R.menu.menu_add);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    onClickAddCourse();
                    return false;
                }
            });
        }
        //toolbarLayout.setVisibility(getParentFragment() == null ?View.GONE:View.VISIBLE);
    }

    private void initBus(){
        RxBusAdd(RefreshCourseEvent.class)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<RefreshCourseEvent>() {
                @Override public void call(RefreshCourseEvent refreshCourseEvent) {
                    freshData();
                }
            });
    }

    public void freshData() {
        RxRegiste(restRepository.getGet_api()
            .qcGetCourses(App.staffId, gymWrapper.getParams(), mIsPrivate ? 1 : 0)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(new Func1<QcDataResponse<CourseTypes>, Boolean>() {
                @Override public Boolean call(QcDataResponse<CourseTypes> qcResponseCourseList) {
                    if (qcResponseCourseList == null || qcResponseCourseList.data == null || qcResponseCourseList.data.courses == null) {
                        return false;
                    } else {
                        return true;
                    }
                }
            })
            .subscribe(new Action1<QcDataResponse<CourseTypes>>() {
                @Override public void call(QcDataResponse<CourseTypes> qcResponseCourseList) {
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

    public void onClickAddCourse() {
        Intent it = new Intent(getActivity(), CourseActivity.class);
        it.putExtra(Configs.EXTRA_COURSE_TYPE, mIsPrivate ? CourseActivity.ADD_PRIVATE_COURSE : CourseActivity.ADD_GROUP_COURSE);
        startActivityForResult(it, 1);
        //Intent it = new Intent(getActivity(), CourseActivity.class);
        //it.putExtra(Configs.EXTRA_BRAND, brand);
        //it.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
        //it.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
        //it.putExtra(Configs.EXTRA_COURSE_TYPE, mIsPrivate ? CourseActivity.ADD_PRIVATE_COURSE : CourseActivity.ADD_GROUP_COURSE);
        //startActivityForResult(it, 1);
    }

    public void delCourse(int pos) {
        CourseType courseDetail = ((CourseItem) mAdatper.getItem(pos)).courseDetail;

        if (gymWrapper.inBrand()) {
            if (courseDetail.getPermission() == 1) {
                showDelDialog(String.format(Locale.CHINA, getString(R.string.alert_del_course), courseDetail.getShops().size()), pos);
            } else {
                showDelFailed(String.format(Locale.CHINA, getString(R.string.alert_del_course_forbid), courseDetail.getShops().size()));
            }
        } else {
            if (courseDetail.getShops().size() > 1) {
                showDelFailed("该课程适用于多家场馆,请到【连锁运营】里进行删除");
            } else {
                showDelDialog("删除后，已有的排期和课程预约都不会受到影响。", pos);
            }
        }
    }

    public void showDelDialog(String content, final int pos) {
        final CourseType courseDetail = ((CourseItem) mAdatper.getItem(pos)).courseDetail;
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
                        .qcDelCourse(App.staffId, courseDetail.getId(), gymWrapper.getParams())
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
    }

    @Override public boolean onItemClick(int position) {
        if (mAdatper.getItem(position) instanceof CourseItem) {
            if (getParentFragment() instanceof CourseFragment) {
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .replace(mCallbackActivity.getFragId(),
                        CourseDetailFragment.newInstance(((CourseItem) mAdatper.getItem(position)).courseDetail))
                    .addToBackStack(((CourseFragment) getParentFragment()).getFragmentName())
                    .commit();
            } else {
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(),
                        CourseDetailFragment.newInstance(((CourseItem) mAdatper.getItem(position)).courseDetail))
                    .addToBackStack(getFragmentName())
                    .commit();
            }
        }

        return true;
    }

    @Override public String getTitle() {
        return getArguments().getBoolean("isPrivate") ? "私教种类" : "团课种类";
    }

    @Override public void onClick(View view) {
        if (view.getId() == R.id.add_course_btn) {
            if (getParentFragment() instanceof CourseFragment) {
                ((CourseFragment) getParentFragment()).addCourse(mIsPrivate);
            }
        }
    }
}
