package cn.qingchengfit.staffkit.views.statement.glance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.statement.custom.CustomStatmentFragment;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.Date;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementGlanceFragment extends BaseFragment implements StatementGlanceView {
    public static final String TAG = StatementGlanceFragment.class.getName();
    @BindView(R.id.statment_glance_month_title) TextView statmentGlanceMonthTitle;
    @BindView(R.id.statment_glance_month_data) TextView statmentGlanceMonthData;
    @BindView(R.id.statment_glance_week_title) TextView statmentGlanceWeekTitle;
    @BindView(R.id.statment_glance_week_data) TextView statmentGlanceWeekData;
    @BindView(R.id.statment_glance_today_title) TextView statmentGlanceTodayTitle;
    @BindView(R.id.statment_glance_today_data) TextView statmentGlanceTodayData;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;

    @Inject GlancePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    private String mChooseShopId;

    public StatementGlanceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(), this);
                refresh.setRefreshing(true);
                freshData();
            }
        });
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });
        presenter.attachView(this);
        statmentGlanceTodayTitle.setText("今日(" + DateUtils.Date2YYYYMMDD(new Date()) + ")");
        statmentGlanceWeekTitle.setText(
            "本周(" + DateUtils.getMondayOfThisWeek(new Date()) + "至" + DateUtils.getSundayOfThisWeek(new Date()) + ")");
        statmentGlanceMonthTitle.setText(
            "本月(" + DateUtils.getStartDayOfMonth(new Date()) + "至" + DateUtils.getEndDayOfMonth(new Date()) + ")");

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (gymWrapper.inBrand()) {
            //mCallbackActivity.setToolbar("课程报表", false, null, 0, null);
            toolbarTitile.setText("课程报表");
        } else {
            down.setVisibility(View.VISIBLE);
            toolbarTitile.setText(R.string.all_gyms);
        }
    }

    @OnClick({ R.id.toolbar_titile, R.id.down }) public void onClickTitle() {
        ChooseGymActivity.start(StatementGlanceFragment.this, 1, PermissionServerUtils.COST_REPORT, getString(R.string.choose_gym),
            mChooseShopId);
    }

    public void freshData() {
        presenter.queryClassGlance(null);
    }

    @OnClick(R.id.statement_glance_month) public void onClickMonth() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(2))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_week) public void onClickWeek() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(1))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_today) public void onClickToday() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(0))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_custom) public void onClickCustom() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), new CustomStatmentFragment())
            .addToBackStack(null)
            .commit();
        //        Intent toFilter = new Intent(getActivity(), StatmentFilterActivity.class);
        //        toFilter.putExtra(Configs.EXTRA_GYM_ID,coachService.getId());
        //        toFilter.putExtra(Configs.EXTRA_GYM_MODEL,coachService.getModel());
        //        toFilter.putExtra(Configs.EXTRA_BRAND_ID,brand.getId());
        //
        //        startActivity(toFilter);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    toolbarTitile.setText(R.string.all_gyms);
                } else {
                    toolbarTitile.setText(IntentUtils.getIntentString(data, 0));
                }
                presenter.queryClassGlance(mChooseShopId);
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void setData(String monthData, String weekData, String dayData) {
        statmentGlanceMonthData.setText(monthData);
        statmentGlanceWeekData.setText(weekData);
        statmentGlanceTodayData.setText(dayData);
        refresh.setRefreshing(false);
    }

    @Override public void onFailed(String error) {
        ToastUtils.show(error);
        refresh.setRefreshing(false);
    }

    @Override public String getFragmentName() {
        return StatementGlanceFragment.class.getName();
    }
}
