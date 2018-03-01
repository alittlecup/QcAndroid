package cn.qingchengfit.saasbase.student.views.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
import cn.qingchengfit.saasbase.student.other.MyIndicator;
import cn.qingchengfit.saasbase.student.other.MySnapHelper;
import cn.qingchengfit.saasbase.student.views.StudentOperationItem;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * StudentListFragment 顶部的会员功能界面
 */
@Leaf(module = "student",path = "/home/operator")
public class StudentOperationFragment extends BaseFragment
        implements FlexibleAdapter.OnItemClickListener, MySnapHelper.OnSelectListener {

    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
    @BindView(R2.id.indicator)
    MyIndicator indicator;


//        @Inject SerPermisAction serPermisAction;
//    @Inject GymBaseInfoAction gymBaseInfoAction;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter mCommonFlexAdapter;
    private boolean proGym = true;

    @Inject
    public StudentOperationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_operation, container, false);
        unbinder = ButterKnife.bind(this, v);

//        RxRegiste(
//            gymBaseInfoAction.getGymByModel(gymWrapper.id(),gymWrapper.model())
//            .filter(new Func1<List<CoachService>, Boolean>() {
//            @Override public Boolean call(List<CoachService> list) {
//                return list != null && list.size() > 0;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
//            @Override public void call(List<CoachService> list) {
//                CoachService now = list.get(0);
//                proGym = GymUtils.getSystemEndDay(now) >= 0;
//                datas.clear();
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_sales, R.string.qc_student_allotsale, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_attend, R.string.qc_student_attendance, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_coach, R.string.qc_student_allot_coach, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_send_sms, R.string.qc_student_send_sms, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_follow, R.string.qc_student_follow_up, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_export, R.string.qc_student_export, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vd_student_transfer, R.string.qc_student_follow_transfer, proGym, true));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_birthday, R.string.qc_student_birthday_notice, proGym, false));
        datas.add(new StudentOperationItem(R.drawable.vector_student_management_tag, R.string.qc_student_vip, proGym, false));
        if (mCommonFlexAdapter != null) mCommonFlexAdapter.notifyDataSetChanged();
//            }
//        }));

        mCommonFlexAdapter = new CommonFlexAdapter(datas, this);
        SmoothScrollGridLayoutManager gridLayoutManager =
                new SmoothScrollGridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        // REFACTOR: 2017/10/26 底部标
        MySnapHelper pagerSnapHelper = new MySnapHelper();
        pagerSnapHelper.setCount(8);
        pagerSnapHelper.setListener(this);
        pagerSnapHelper.attachToRecyclerView(recycleview);

        recycleview.setLayoutManager(gridLayoutManager);
        recycleview.setAdapter(mCommonFlexAdapter);
        onDatasRefresh();
        return v;
    }

    private void onDatasRefresh() {
        setRecyclerPadding(datas.size());
        indicator.createIndicators(datas.size() / 8 + 1);
    }

    private void setRecyclerPadding(int lastPosition) {
        if (lastPosition % 2 == 0) {
            lastPosition -= 1;
        }
        recycleview.setPadding(0, 0,
                MeasureUtils.getScreenWidth(getResources()) * (4 - lastPosition % 8) / 4, 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }


    @Override
    public boolean onItemClick(int position) {

        if (!gymWrapper.isPro()) {
            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
            return true;
        }
        if (mCommonFlexAdapter.getItem(position) instanceof StudentOperationItem) {
            int strId = ((StudentOperationItem) mCommonFlexAdapter.getItem(position)).getStrRes();
            if (strId == R.string.qc_student_allotsale) {
                Uri uri=Uri.parse("qcstaff://student/allot/student");
                routeTo(uri,null);
            } else if (strId == R.string.qc_student_allot_coach) {
                Uri uri=Uri.parse("qcstaff://student/coach/list");
                routeTo(uri,null);
            } else if (strId == R.string.qc_student_follow_up) {
                Uri uri=Uri.parse("qcstaff://student/followup/student");
                routeTo(uri,null);
            } else if (strId == R.string.qc_student_follow_transfer) {
                Uri uri=Uri.parse("qcstaff://student/transfer/student");
                routeTo(uri,null);
            } else if (strId == R.string.qc_student_attendance) {
                Uri uri=Uri.parse("qcstaff://student/attendance/page");
                routeTo(uri,null);

            } else if (strId == R.string.qc_student_send_sms) {

            } else if (strId == R.string.qc_student_export) {
                Uri uri=Uri.parse("qcstaff://student/follow/home");
                routeTo(uri,null);

            } else if (strId == R.string.qc_student_birthday_notice) {

            } else if (strId == R.string.qc_student_vip) {

            }

        }
        return true;
    }

    @Override
    public void onPageSelect(int position) {
        indicator.onPageSelected(position);
    }
}
