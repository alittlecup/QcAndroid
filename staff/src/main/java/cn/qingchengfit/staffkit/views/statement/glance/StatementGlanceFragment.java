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



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.statement.custom.CustomStatmentFragment;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.Date;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementGlanceFragment extends BaseFragment implements StatementGlanceView {
    public static final String TAG = StatementGlanceFragment.class.getName();
	TextView statmentGlanceMonthTitle;
	TextView statmentGlanceMonthData;
	TextView statmentGlanceWeekTitle;
	TextView statmentGlanceWeekData;
	TextView statmentGlanceTodayTitle;
	TextView statmentGlanceTodayData;
	SwipeRefreshLayout refresh;

    @Inject GlancePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView down;
    private String mChooseShopId;

    public StatementGlanceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
      statmentGlanceMonthTitle = (TextView) view.findViewById(R.id.statment_glance_month_title);
      statmentGlanceMonthData = (TextView) view.findViewById(R.id.statment_glance_month_data);
      statmentGlanceWeekTitle = (TextView) view.findViewById(R.id.statment_glance_week_title);
      statmentGlanceWeekData = (TextView) view.findViewById(R.id.statment_glance_week_data);
      statmentGlanceTodayTitle = (TextView) view.findViewById(R.id.statment_glance_today_title);
      statmentGlanceTodayData = (TextView) view.findViewById(R.id.statment_glance_today_data);
      refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      down = (ImageView) view.findViewById(R.id.down);
      view.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickTitle();
        }
      });
      view.findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickTitle();
        }
      });
      view.findViewById(R.id.statement_glance_month).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickMonth();
        }
      });
      view.findViewById(R.id.statement_glance_week).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickWeek();
        }
      });
      view.findViewById(R.id.statement_glance_today).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickToday();
        }
      });
      view.findViewById(R.id.statement_glance_custom)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onClickCustom();
            }
          });

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
      SensorsUtils.trackScreen(      this.getClass().getCanonicalName());
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

 public void onClickTitle() {
        ChooseGymActivity.start(StatementGlanceFragment.this, 1, PermissionServerUtils.COST_REPORT, getString(R.string.choose_gym),
            mChooseShopId);
    }

    public void freshData() {
        presenter.queryClassGlance(null);
    }

 public void onClickMonth() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(2))
            .addToBackStack(null)
            .commit();
    }

 public void onClickWeek() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(1))
            .addToBackStack(null)
            .commit();
    }

 public void onClickToday() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), StatementDetailFragment.newInstance(0))
            .addToBackStack(null)
            .commit();
    }

 public void onClickCustom() {
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
