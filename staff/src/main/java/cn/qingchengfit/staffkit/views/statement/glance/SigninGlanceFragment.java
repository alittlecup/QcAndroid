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



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.statement.custom.CustomSigninFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SigninReportFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.Date;
import javax.inject.Inject;

/**
 *
 */
public class SigninGlanceFragment extends BaseFragment implements StatementGlanceView {
    public static final String TAG = SigninGlanceFragment.class.getName();
	TextView statmentGlanceTodayTitle;
	TextView statmentGlanceTodayData;
	RelativeLayout statementGlanceToday;
	TextView statmentGlanceWeekTitle;
	TextView statmentGlanceWeekData;
	RelativeLayout statementGlanceWeek;
	TextView statmentGlanceMonthTitle;
	TextView statmentGlanceMonthData;
	RelativeLayout statementGlanceMonth;
	RelativeLayout statementGlanceCustom;
	SwipeRefreshLayout refresh;

    @Inject GlancePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView down;

    private String mChooseShopId;

    public SigninGlanceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
      statmentGlanceTodayTitle = (TextView) view.findViewById(R.id.statment_glance_today_title);
      statmentGlanceTodayData = (TextView) view.findViewById(R.id.statment_glance_today_data);
      statementGlanceToday = (RelativeLayout) view.findViewById(R.id.statement_glance_today);
      statmentGlanceWeekTitle = (TextView) view.findViewById(R.id.statment_glance_week_title);
      statmentGlanceWeekData = (TextView) view.findViewById(R.id.statment_glance_week_data);
      statementGlanceWeek = (RelativeLayout) view.findViewById(R.id.statement_glance_week);
      statmentGlanceMonthTitle = (TextView) view.findViewById(R.id.statment_glance_month_title);
      statmentGlanceMonthData = (TextView) view.findViewById(R.id.statment_glance_month_data);
      statementGlanceMonth = (RelativeLayout) view.findViewById(R.id.statement_glance_month);
      statementGlanceCustom = (RelativeLayout) view.findViewById(R.id.statement_glance_custom);
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

      initToolbar(toolbar);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView();
        delegatePresenter(presenter, this);
        presenter.querySignInGlance(null);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (gymWrapper.inBrand()) {
            down.setVisibility(View.VISIBLE);
            toolbarTitile.setText(R.string.all_gyms);
        } else {
            toolbarTitile.setText(R.string.statement_signin);
        }
    }

 public void onClickTitle() {
        ChooseGymActivity.start(SigninGlanceFragment.this, 1, PermissionServerUtils.SALES_REPORT, getString(R.string.choose_gym),
            mChooseShopId);
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
                showLoading();
                presenter.querySignInGlance(mChooseShopId);
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
                presenter.querySignInGlance(null);
            }
        });
        statmentGlanceTodayTitle.setText("今日(" + DateUtils.Date2YYYYMMDD(new Date()) + ")");
        statmentGlanceWeekTitle.setText(
            "本周(" + DateUtils.getMondayOfThisWeek(new Date()) + "至" + DateUtils.getSundayOfThisWeek(new Date()) + ")");
        statmentGlanceMonthTitle.setText(
            "本月(" + DateUtils.getStartDayOfMonth(new Date()) + "至" + DateUtils.getEndDayOfMonth(new Date()) + ")");
    }

 public void onClickMonth() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), SigninReportFragment.newInstance(2))
            .addToBackStack(null)
            .commit();
    }

 public void onClickWeek() {

        Fragment fragment = SigninReportFragment.newInstance(1);
        getFragmentManager().beginTransaction()
            //                .addSharedElement(statementGlanceWeek, "test")
            .add(mCallbackActivity.getFragId(), fragment).addToBackStack(null).commit();
    }

 public void onClickToday() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), SigninReportFragment.newInstance(0))
            .addToBackStack(null)
            .commit();
    }

 public void onClickCustom() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), new CustomSigninFragment())
            .addToBackStack(null)
            .commit();
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
        return SigninGlanceFragment.class.getName();
    }
}
