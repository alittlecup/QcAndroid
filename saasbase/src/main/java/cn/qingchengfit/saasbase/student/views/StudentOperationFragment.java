package cn.qingchengfit.saasbase.student.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class StudentOperationFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

    @BindView(R2.id.recycleview) RecyclerView recycleview;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter mCommonFlexAdapter;
    private boolean proGym = false;

    @Inject public StudentOperationFragment() {
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_operation, container, false);
        unbinder = ButterKnife.bind(this, v);

        RxRegiste(
            gymBaseInfoAction.getGymByModel(gymWrapper.id(),gymWrapper.model())
            .filter(new Func1<List<CoachService>, Boolean>() {
            @Override public Boolean call(List<CoachService> list) {
                return list != null && list.size() > 0;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
            @Override public void call(List<CoachService> list) {
                CoachService now = list.get(0);
                proGym = GymUtils.getSystemEndDay(now) >= 0;
                datas.clear();
                datas.add(
                    new StudentOperationItem(R.drawable.vector_student_management_sales, R.string.qc_student_allotsale, proGym, true));
                datas.add(
                    new StudentOperationItem(R.drawable.vector_student_management_coach, R.string.qc_student_allot_coach, proGym, true));
                datas.add(
                    new StudentOperationItem(R.drawable.vector_student_management_follow, R.string.qc_student_follow_up, proGym, true));
                datas.add(new StudentOperationItem(R.drawable.vd_student_transfer, R.string.qc_student_follow_transfer, proGym, true));
                datas.add(
                    new StudentOperationItem(R.drawable.vector_student_management_attend, R.string.qc_student_attendance, proGym, true));
                datas.add(new StudentOperationItem(R.drawable.vector_student_send_sms, R.string.qc_student_send_sms, proGym, true));
                datas.add(
                    new StudentOperationItem(R.drawable.vector_student_management_birthday, R.string.qc_student_birthday_notice, proGym,
                        false));
                datas.add(new StudentOperationItem(R.drawable.vector_student_management_tag,
                    R.string.qc_student_vip, proGym, false));
                if (mCommonFlexAdapter != null) mCommonFlexAdapter.updateDataSet(datas);
            }
        }));

        mCommonFlexAdapter = new CommonFlexAdapter(datas, this);

        SmoothScrollGridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                return 1;
            }
        });
        recycleview.setLayoutManager(gridLayoutManager);
        recycleview.setAdapter(mCommonFlexAdapter);
        return v;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }



    @Override public boolean onItemClick(int position) {

        return true;
    }
}
