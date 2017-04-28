package com.qingchengfit.fitcoach.fragment.batch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.manage.StaffAppFragmentFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.QcResponseCardTpls;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.items.AccountExpendItemItem;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/23.
 */
public class SetAccountFragment extends BaseFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;
    @BindView(R.id.count) CommonInputView count;
    @BindView(R.id.layout_need_pay) LinearLayout layoutNeedPay;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.sw_need_pay) SwitchCompat swNeedPay;
    @BindView(R.id.layout_pay_detail) FrameLayout layoutPayDetail;
    @BindView(R.id.no_account_hint) TextView noAccountHint;
    private DialogList stucount;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter mFlexAdapter;

    public static SetAccountFragment newInstance(int s, boolean isfree) {
        Bundle args = new Bundle();
        args.putInt("o", s);
        args.putBoolean("isfree", isfree);
        SetAccountFragment fragment = new SetAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_account, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitle.setText("设置结算方式");
        toolbar.inflateMenu(R.menu.menu_complete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                Intent ret = new Intent();
                ret.putExtra("count", Integer.parseInt(count.getContent()));
                getActivity().setResult(Activity.RESULT_OK, ret);
                getActivity().finish();
                return true;
            }
        });
        if (getArguments() != null) {
            count.setContent(getArguments().getInt("o", 8) + "");
            swNeedPay.setChecked(!getArguments().getBoolean("isfree", true));
            layoutPayDetail.setVisibility(swNeedPay.isChecked() ? View.VISIBLE : View.GONE);
            noAccountHint.setVisibility(swNeedPay.isChecked() ?  View.GONE:View.VISIBLE );
        }

        recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recycleview.setHasFixedSize(true);
        mFlexAdapter = new CommonFlexAdapter(datas, this);
        mFlexAdapter.setAutoCollapseOnExpand(false).setAutoScrollOnExpand(true);
        recycleview.setAdapter(mFlexAdapter);
        if (getActivity() instanceof FragActivity) {
            CoachService coachService = ((FragActivity) getActivity()).getCoachService();

            RxRegiste(QcCloudClient.getApi().getApi.qcGetCardTpls(App.coachid + "", coachService.getId() + "", coachService.getModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseCardTpls>() {
                    @Override public void call(QcResponseCardTpls qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            if (qcResponse.data.card_tpls != null) {
                                datas.clear();
                                for (int i = 0; i < qcResponse.data.card_tpls.size(); i++) {
                                    datas.add(new AccountExpendItemItem(qcResponse.data.card_tpls.get(i), i + 2));
                                }
                                mFlexAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.show(qcResponse.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        ToastUtils.show(throwable.getMessage());
                    }
                }));
        }
        return view;
    }

    @Override public String getFragmentName() {
        return SetAccountFragment.class.getName();
    }

    @OnClick({ R.id.count, R.id.layout_need_pay }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.count:
                stucount = new DialogList(getContext()).list(StringUtils.getNums(1, 300), new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        count.setContent(Integer.toString(position + 1));
                        stucount.dismiss();
                    }
                }).title("选择人数");
                stucount.show();
                break;
            case R.id.layout_need_pay:
                StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
                break;
        }
    }

    @OnClick(R.id.bg_can_not_do) public void goStaff() {
        StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
    }
}
