package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.utils.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatementFragment extends MainBaseFragment {
    public static final String TAG = DataStatementFragment.class.getName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder unbinder;


    public DataStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("fragmentOncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_statement, container, false);
        unbinder=ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.setTitle("数据报表");
        return view;
    }

    @OnClick(R.id.statement_order_btn)
    public void clickOrder() {
        Intent it = new Intent(getActivity(), FragActivity.class);
        it.putExtra("type", 0);
        getActivity().startActivity(it);
    }

    @OnClick(R.id.statement_sales_btn)
    public void clickSales() {
        Intent it = new Intent(getActivity(), FragActivity.class);
        it.putExtra("type", 1);
        getActivity().startActivity(it);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
