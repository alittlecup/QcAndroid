package cn.qingchengfit.saasbase.student.views.followup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentFollowUpDataStatisticsBinding;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.presenters.followup.FollowUpHomePresenter;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static cn.qingchengfit.saasbase.student.views.followup.FollowUpHomeFragment.FollowUpStatus.NEW_CREATE_USERS;
import static cn.qingchengfit.saasbase.student.views.followup.FollowUpHomeFragment.FollowUpStatus.NEW_FOLLOWING_USERS;
import static cn.qingchengfit.saasbase.student.views.followup.FollowUpHomeFragment.FollowUpStatus.NEW_MEMBER_USERS;

/**
 * Created by huangbaole on 2017/11/6.
 */

public class FollowUpStatusTopFragment extends BaseFragment implements FollowUpHomePresenter.MVPView {

    FragmentFollowUpDataStatisticsBinding binding;
    @Inject
    FollowUpHomePresenter presenter;
    @Inject
    LoginStatus loginStatus;
    @Named("topFilter")
    @Inject
    StudentFilter filter;
    @Inject
    FollowUpFilterModel model;
    int type = 0;
    private int offSetDay=6;


    public static FollowUpStatusTopFragment getInstance(@FollowUpHomeFragment.FollowUpStatus int type) {
        FollowUpStatusTopFragment followUpStatusTopFragment = new FollowUpStatusTopFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        followUpStatusTopFragment.setArguments(bundle);
        return followUpStatusTopFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follow_up_data_statistics, container, false);
        delegatePresenter(presenter, this);
        initFilter();
        type = getArguments().getInt("type");
        presenter.getStudentsStatistics(loginStatus.staff_id(), filter);
        binding.setModel(model);
        binding.setFragment(this);
        initRxbus();
        return binding.getRoot();
    }

    private void initRxbus() {
        RxBusAdd(FollowUpEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BusSubscribe<FollowUpEvent>() {
                    @Override
                    public void onNext(FollowUpEvent followUpEvent) {
                        switch (followUpEvent.getEVENT()) {
                            case FollowUpEvent.Event.LATESTDAY:
                                filter.registerTimeStart = followUpEvent.getFilter().registerTimeStart;
                                filter.registerTimeEnd = followUpEvent.getFilter().registerTimeEnd;
                                break;
                            case FollowUpEvent.Event.TOP_SALERS:
                                filter.sale = followUpEvent.getFilter().sale;
                                break;
                        }
                        presenter.getStudentsStatistics(loginStatus.staff_id(), filter);
                    }
                });
    }

    private void initFilter() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String end = DateUtils.Date2YYYYMMDD(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DATE, -6);
        String start = DateUtils.Date2YYYYMMDD(calendar.getTime());
        filter.registerTimeEnd = end;
        filter.registerTimeStart = start;
    }

    @Override
    public void onFollowUpStatistics(FollowUpDataStatistic statistics) {
        //switch (type) {
        //    case NEW_CREATE_USERS:
        //        binding.lineChar.setData(StudentBusinessUtils
        //                .transformBean2DataByType(
        //                        statistics.new_create_users
        //                        , offSetDay
        //                        , NEW_CREATE_USERS));
        //        break;
        //    case NEW_FOLLOWING_USERS:
        //        binding.lineChar.setData(StudentBusinessUtils
        //                .transformBean2DataByType(
        //                        statistics.new_create_users
        //                        , offSetDay
        //                        , NEW_FOLLOWING_USERS));
        //        break;
        //    case NEW_MEMBER_USERS:
        //        binding.lineChar.setData(StudentBusinessUtils
        //                .transformBean2DataByType(
        //                        statistics.new_create_users
        //                        , offSetDay
        //                        , NEW_MEMBER_USERS));
        //        break;
        //}
        binding.lineChar.setVisibleXRange(6);
    }


    private Action1<Integer> buttonAction;

    public void onQcButtonClick(View view) {
        if (buttonAction == null) return;
        if (view instanceof QcFilterToggle) {
            if (view.getId() == R.id.qft_saler && ((QcFilterToggle) view).isChecked()) {
                buttonAction.call(0);
                return;
            } else if (view.getId() == R.id.qft_times && ((QcFilterToggle) view).isChecked()) {
                buttonAction.call(1);
                return;
            }
            buttonAction.call(-1);
        }
    }

    public void setOnQcButtonClick(Action1<Integer> action1) {
        this.buttonAction = action1;
    }

    public void clearButtonToggle() {
        binding.qcRadioGroup.clearCheck();
    }

    public void loadData() {
        offSetDay=DateUtils.interval(filter.registerTimeStart, filter.registerTimeEnd);
        presenter.getStudentsStatistics(loginStatus.staff_id(), filter);
    }
}
