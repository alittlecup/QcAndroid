package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CoursePlan;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import cn.qingchengfit.model.base.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcAllCoursePlanResponse;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * Created by Paper on 16/8/2.
 */
public class ChooseCoursePlanFragment extends BaseFragment implements
        FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    CommonFlexAdapter mAdapter;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    @Inject
    RestRepository restRepository;
    @Inject
    CoachService coachService;
    @Inject
    Brand brand;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Long mChosenId = 0L;
    private Unbinder unbinder;

    public static ChooseCoursePlanFragment newInstance(Long id) {

        Bundle args = new Bundle();
        args.putLong("id", id);
        ChooseCoursePlanFragment fragment = new ChooseCoursePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChosenId = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_plan, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);

        }
        toolbarTitle.setText("选择默认课程计划");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        mAdapter = new CommonFlexAdapter(mDatas, this);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerview.setAdapter(mAdapter);
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",coachService.getId());
        params.put("model",coachService.getModel());
        RxRegiste(restRepository.getGet_api().qcGetAllPlans(App.coachid, params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcAllCoursePlanResponse>() {
                    @Override
                    public void call(QcAllCoursePlanResponse qcResponseCoursePlan) {
                        if (ResponseConstant.checkSuccess(qcResponseCoursePlan)) {
                            mDatas.clear();
                            mDatas.add(new ChooseCoursePlanItem(false, new CoursePlan.Builder().id(0L).name("不使用任何课程计划模板").build()));
                            for (int i = 0; i < qcResponseCoursePlan.data.plans.size(); i++) {
                                mDatas.add(new ChooseCoursePlanItem(qcResponseCoursePlan.data.plans.get(i).getId().longValue() == mChosenId, qcResponseCoursePlan.data.plans.get(i)));
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
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
        //unbinder.unbind();
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
