package cn.qingchengfit.staffkit.views.statement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.statement.glance.SaleGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.SigninGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.StatementGlanceFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatementFragment extends BaseFragment {
    public static final String TAG = DataStatementFragment.class.getName();
	RelativeLayout statementOrderBtn;
	RelativeLayout statementSalesBtn;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    public DataStatementFragment() {

    }

    public static DataStatementFragment newInstance() {
        Bundle args = new Bundle();
        DataStatementFragment fragment = new DataStatementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_statement, container, false);
      statementOrderBtn = (RelativeLayout) view.findViewById(R.id.statement_order_btn);
      statementSalesBtn = (RelativeLayout) view.findViewById(R.id.statement_sales_btn);
      view.findViewById(R.id.statement_order_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          clickOrder();
        }
      });
      view.findViewById(R.id.statement_sales_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          clickSales();
        }
      });
      view.findViewById(R.id.statement_signin_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          clickSginin();
        }
      });

      //
        mCallbackActivity.setToolbar(getString(R.string.statement_title), false, null, 0, null);
        return view;
    }

 public void clickOrder() {
        if (serPermisAction.checkNoOne(PermissionServerUtils.COST_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new StatementGlanceFragment())
            .addToBackStack(null)
            .commit();
    }

 public void clickSales() {
        if (serPermisAction.checkNoOne(PermissionServerUtils.SALES_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new SaleGlanceFragment())
            .addToBackStack(null)
            .commit();
    }

 public void clickSginin() {
        if (serPermisAction.checkNoOne(PermissionServerUtils.CHECKIN_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new SigninGlanceFragment())
            .addToBackStack(null)
            .commit();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return DataStatementFragment.class.getName();
    }
}
