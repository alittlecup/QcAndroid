package cn.qingchengfit.staffkit.views.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.CoursePlan;
import cn.qingchengfit.model.responese.CoursePlans;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ChooseCoursePlanItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
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
 * Created by Paper on 16/8/2.
 */
public class ChooseCoursePlanFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    public Long mChosenId = 0L;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    CommonFlexAdapter mAdapter;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    @Inject public ChooseCoursePlanFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_plan, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        mCallbackActivity.setToolbar("选择默认课程计划", false, null, 0, null);
        mAdapter = new CommonFlexAdapter(mDatas, this);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerview.setAdapter(mAdapter);
        RxRegiste(restRepository.getGet_api()
            .qcGetCoursePlan(loginStatus.staff_id(), gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CoursePlans>>() {
                @Override public void call(QcResponseData<CoursePlans> qcResponseCoursePlan) {
                    if (ResponseConstant.checkSuccess(qcResponseCoursePlan)) {
                        mDatas.clear();
                        mDatas.add(new ChooseCoursePlanItem(false, new CoursePlan.Builder().id(0L).name("不使用任何课程计划模板").build()));
                        for (int i = 0; i < qcResponseCoursePlan.data.plans.size(); i++) {
                            mDatas.add(new ChooseCoursePlanItem(qcResponseCoursePlan.data.plans.get(i).getId().longValue() == mChosenId,
                                qcResponseCoursePlan.data.plans.get(i)));
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show("server error");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("选择默认课程计划");
    }

    @Override public String getFragmentName() {
        return ChooseCoursePlanFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.copy_link) public void onClick() {
    }

    @Override public boolean onItemClick(int position) {
        getActivity().onBackPressed();
        if (mAdapter.getItem(position) instanceof ChooseCoursePlanItem) {
            RxBus.getBus().post(((ChooseCoursePlanItem) mAdapter.getItem(position)).coursePlan);
        }

        return true;
    }
}
