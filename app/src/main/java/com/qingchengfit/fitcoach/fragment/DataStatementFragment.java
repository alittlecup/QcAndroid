package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatementFragment extends MainBaseFragment {
    public static final String TAG = DataStatementFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;



    public DataStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_statement, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.setTitle("数据报表");
        return view;
    }

    @OnClick(R.id.statement_order_btn)
    public void clickOrder() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fraglayout, new StatementGlanceFragment())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_sales_btn)
    public void clickSales() {
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fraglayout, new StatementGlanceFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
