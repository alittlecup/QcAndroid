package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.FollowUpConver;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.FunnelTwoView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * // 转化率 图表
 * //Created by yangming on 16/12/5.
 */
public class FollowUpDataTransfer0Fragment extends BaseFragment implements FollowUpDataTransferPresenter.PresenterView {

    @BindView(R.id.funnelview2) FunnelTwoView funnelview;

    @Inject FollowUpDataTransferPresenter presenter;

    StudentFilter filter = new StudentFilter();
    @BindView(R.id.tv_lable) TextView tvLable;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;

    @Inject public FollowUpDataTransfer0Fragment() {
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up_data_transfer_0, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDI();
        initView();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String end = DateUtils.Date2YYYYMMDD(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DATE, -6);
        String start = DateUtils.Date2YYYYMMDD(calendar.getTime());
        filter.registerTimeEnd = end;
        filter.registerTimeStart = start;

        if (!serPermisAction.checkHasAllMember()) {
            filter.sale = loginStatus.getLoginUser();
        }
        showLoading();
        presenter.getStudentsConver(filter);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initDI() {

        delegatePresenter(presenter, this);
    }

    private void initView() {

    }

    public void refreshData(final StudentFilter filter, final String dayoff) {
        this.filter = filter;
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                if (TextUtils.isEmpty(dayoff)) {
                    tvLable.setText("注册日期为最近" + filter.dayOff + "天的会员转化率");
                } else {
                    tvLable.setText("注册日期为" + filter.registerTimeStart + "至" + filter.registerTimeEnd + "的会员转化率");
                }
                showLoading();
            }
        });

        presenter.getStudentsConver(filter);
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

    @Override public void onFollowUpConver(FollowUpConver conver) {
        hideLoading();
        if (conver != null && conver.create_count != null && conver.following_count != null && conver.member_count != null) {
            ArrayList<Float> counts = new ArrayList<>();
            counts.add(conver.create_count);
            counts.add(conver.following_count);
            counts.add(conver.member_count);
            funnelview.setData(counts);
            funnelview.animateY();
        }
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }
}
