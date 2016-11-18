package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.model.item.ChooseCoursePlanItem;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.bean.CoursePlan;
import cn.qingchengfit.staffkit.usecase.response.QcResponseCoursePlan;
import cn.qingchengfit.staffkit.usecase.response.ResponseConstant;
import cn.qingchengfit.staffkit.utils.GymUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
public class ChooseCoursePlanFragment extends BaseFragment implements
        FlexibleAdapter.OnItemClickListener{

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    CommonFlexAdapter mAdapter;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    @Inject
    RestRepository restRepository;
    @Inject
    CoachService coachService;
    @Inject
    Brand brand;
    private Long mChosenId = 0L;

    public static ChooseCoursePlanFragment newInstance(Long id) {

        Bundle args = new Bundle();
        args.putLong("id",id);
        ChooseCoursePlanFragment fragment = new ChooseCoursePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mChosenId = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_plan, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity){
            ((CourseActivity) getActivity()).getComponent().inject(this);

        }
        mCallbackActivity.setToolbar("选择默认课程计划",false,null,0,null);
        mAdapter = new CommonFlexAdapter(mDatas,this);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerview.setAdapter(mAdapter);
        RxRegiste(restRepository.getGet_api().qcGetCoursePlan(App.staffId, GymUtils.getParams(coachService,brand))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .filter(new Func1<QcResponseCoursePlan, Boolean>() {
//                    @Override
//                    public Boolean call(QcResponseCoursePlan qcResponseCoursePlan) {
//                        if (qcResponseCoursePlan == null || qcResponseCoursePlan.data == null || qcResponseCoursePlan.data.plans == null
//                                )
//                            return false;
//                        else return true;
//                    }
//                })
                .subscribe(new Action1<QcResponseCoursePlan>() {
                    @Override
                    public void call(QcResponseCoursePlan qcResponseCoursePlan) {
                        if (ResponseConstant.checkSuccess(qcResponseCoursePlan)){
                            mDatas.clear();
                            mDatas.add(new ChooseCoursePlanItem(false,new CoursePlan.Builder().id(0L).name("不使用任何课程计划模板").build()));
                            for (int i = 0; i < qcResponseCoursePlan.data.plans.size(); i++) {
                                mDatas.add(new ChooseCoursePlanItem(qcResponseCoursePlan.data.plans.get(i).getId().longValue() == mChosenId,qcResponseCoursePlan.data.plans.get(i)));
                            }
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.show("server error");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
        );
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override
    public String getFragmentName() {
        return ChooseCoursePlanFragment.class.getName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.copy_link)
    public void onClick() {
    }

    @Override
    public boolean onItemClick(int position) {
        getActivity().onBackPressed();
        if (mAdapter.getItem(position) instanceof ChooseCoursePlanItem)
            RxBus.getBus().post(((ChooseCoursePlanItem) mAdapter.getItem(position)).coursePlan);

        return true;
    }
}
