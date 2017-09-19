package cn.qingchengfit.staffkit.views.statement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
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
    @BindView(R.id.statement_order_btn) RelativeLayout statementOrderBtn;
    @BindView(R.id.statement_sales_btn) RelativeLayout statementSalesBtn;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

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
        unbinder = ButterKnife.bind(this, view);
        //
        mCallbackActivity.setToolbar(getString(R.string.statement_title), false, null, 0, null);
        return view;
    }

    @OnClick(R.id.statement_order_btn) public void clickOrder() {
        if (SerPermisAction.checkNoOne(PermissionServerUtils.COST_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new StatementGlanceFragment())
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_sales_btn) public void clickSales() {
        if (SerPermisAction.checkNoOne(PermissionServerUtils.SALES_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new SaleGlanceFragment())
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_signin_btn) public void clickSginin() {
        if (SerPermisAction.checkNoOne(PermissionServerUtils.CHECKIN_REPORT)) {
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
