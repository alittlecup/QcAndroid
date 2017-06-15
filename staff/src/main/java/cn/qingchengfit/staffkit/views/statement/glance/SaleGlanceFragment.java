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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import cn.qingchengfit.staffkit.views.statement.custom.CustomSaleFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SaleDetailFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.Date;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleGlanceFragment extends BaseFragment implements StatementGlanceView {
    public static final String TAG = SaleGlanceFragment.class.getName();
    @BindView(R.id.statment_glance_today_title) TextView statmentGlanceTodayTitle;
    @BindView(R.id.statment_glance_today_data) TextView statmentGlanceTodayData;
    @BindView(R.id.statement_glance_today) RelativeLayout statementGlanceToday;
    @BindView(R.id.statment_glance_week_title) TextView statmentGlanceWeekTitle;
    @BindView(R.id.statment_glance_week_data) TextView statmentGlanceWeekData;
    @BindView(R.id.statement_glance_week) RelativeLayout statementGlanceWeek;
    @BindView(R.id.statment_glance_month_title) TextView statmentGlanceMonthTitle;
    @BindView(R.id.statment_glance_month_data) TextView statmentGlanceMonthData;
    @BindView(R.id.statement_glance_month) RelativeLayout statementGlanceMonth;
    @BindView(R.id.statement_glance_custom) RelativeLayout statementGlanceCustom;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;

    @Inject GlancePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    private String mChooseShopId;

    public SaleGlanceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView();

        presenter.querySaleGlance(null);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (gymWrapper.inBrand()) {
            down.setVisibility(View.VISIBLE);
            toolbarTitile.setText(R.string.all_gyms);
        } else {
            toolbarTitile.setText(R.string.statement_sale);
        }
    }

    @OnClick({ R.id.toolbar_titile, R.id.down }) public void onClickTitle() {
        ChooseGymActivity.start(SaleGlanceFragment.this, 1, PermissionServerUtils.SALES_REPORT, getString(R.string.choose_gym),
            mChooseShopId);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    toolbarTitile.setText(R.string.all_gyms);
                    //mCallbackActivity.setBar(new ToolbarBean(getString(R.string.all_gyms), true, new View.OnClickListener() {
                    //    @Override public void onClick(View v) {
                    //        ChooseGymActivity.start(SaleGlanceFragment.this, 1, getString(R.string.choose_gym), mChooseShopId);
                    //    }
                    //}, 0, null));
                } else {
                    toolbarTitile.setText(IntentUtils.getIntentString(data, 0));
                    //mCallbackActivity.setBar(new ToolbarBean(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                    //    @Override public void onClick(View v) {
                    //        ChooseGymActivity.start(SaleGlanceFragment.this, 1, getString(R.string.choose_gym), mChooseShopId);
                    //    }
                    //}, 0, null));
                }
                showLoading();
                presenter.querySaleGlance(mChooseShopId);
            }
        }
    }

    private void initView() {
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(), this);
                refresh.setRefreshing(true);
            }
        });
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querySaleGlance(null);
            }
        });
        statmentGlanceTodayTitle.setText("今日(" + DateUtils.Date2YYYYMMDD(new Date()) + ")");
        statmentGlanceWeekTitle.setText(
            "本周(" + DateUtils.getMondayOfThisWeek(new Date()) + "至" + DateUtils.getSundayOfThisWeek(new Date()) + ")");
        statmentGlanceMonthTitle.setText(
            "本月(" + DateUtils.getStartDayOfMonth(new Date()) + "至" + DateUtils.getEndDayOfMonth(new Date()) + ")");
    }

    @OnClick(R.id.statement_glance_today) public void onClickToday() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), SaleDetailFragment.newInstance(0))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_week) public void onClickWeek() {

        Fragment fragment = SaleDetailFragment.newInstance(1);
        getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), fragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.statement_glance_month) public void onClickMonth() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), SaleDetailFragment.newInstance(2))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_custom) public void onClickCustom() {
        getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), new CustomSaleFragment()).addToBackStack(null).commit();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void setData(String monthData, String weekData, String dayData) {
        hideLoading();
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
        return SaleGlanceFragment.class.getName();
    }
}
