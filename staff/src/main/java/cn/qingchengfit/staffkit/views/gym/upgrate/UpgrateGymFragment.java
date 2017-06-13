package cn.qingchengfit.staffkit.views.gym.upgrate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.body.RenewBody;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseRenew;
import cn.qingchengfit.model.responese.RenewalList;
import cn.qingchengfit.model.responese.RenewalPay;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.staffkit.views.gym.upgrate.item.LinearFunItem;
import cn.qingchengfit.staffkit.views.gym.upgrate.item.PayItem;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.widgets.AnimatedButton;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 2017/1/17.
 */
public class UpgrateGymFragment extends BaseFragment {

    @BindView(R.id.rv_function) RecyclerView rvFunction;
    @BindView(R.id.tv_pay_title) TextView tvPayTitle;
    @BindView(R.id.tag_pro) TextView tagPro;
    @BindView(R.id.tv_pay_hint) TextView tvPayHint;
    @BindView(R.id.rv_first_pay_choose) RecyclerView rvFirstPayChoose;
    @BindView(R.id.tv_hide_title) TextView tvHideTitle;
    @BindView(R.id.btn_show_all) AnimatedButton btnShowAll;
    @BindView(R.id.rv_secound_pay_choose) RecyclerView rvSecoundPayChoose;
    @BindView(R.id.tv_brand_name) TextView tvBrandName;
    @BindView(R.id.tv_time_long) TextView tvTimeLong;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.tv_hiden_hint) TextView tvHidenHint;
    @BindView(R.id.root_scroll) NestedScrollView rootScroll;
    @BindView(R.id.hiden_trans) View hidenTrans;
    @BindView(R.id.layout_hiden) RelativeLayout layoutHiden;

    @Inject GymWrapper gymWrapper;

    boolean mIsPro;
    private CommonFlexAdapter mFunAdapter;
    private List<AbstractFlexibleItem> mFunDatas = new ArrayList<>();
    private CommonFlexAdapter mPayAdapter;
    private CommonFlexAdapter mHidenPayAdapter;
    private List<AbstractFlexibleItem> mOriDatas = new ArrayList<>();
    private List<AbstractFlexibleItem> mDisCountDatas = new ArrayList<>();

    private boolean choosehasDiscount = false;
    private int chooseMonthTime = 12;
    private FlexibleAdapter.OnItemClickListener mPayClickListener = new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
            mPayAdapter.clearSelection();
            mHidenPayAdapter.clearSelection();
            mPayAdapter.toggleSelection(position);
            mHidenPayAdapter.notifyDataSetChanged();
            mPayAdapter.notifyDataSetChanged();
            PayItem payItem = (PayItem) mPayAdapter.getItem(position);
            tvTimeLong.setText(payItem.getStrTime());
            tvPrice.setText("￥" + payItem.getPrice());
            choosehasDiscount = payItem.hasDiscount();
            chooseMonthTime = payItem.getMonthCount();
            return false;
        }
    };
    private FlexibleAdapter.OnItemClickListener mHidenClickListener = new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
            mPayAdapter.clearSelection();
            mHidenPayAdapter.clearSelection();
            mHidenPayAdapter.toggleSelection(position);
            mHidenPayAdapter.notifyDataSetChanged();
            mPayAdapter.notifyDataSetChanged();

            PayItem payItem = (PayItem) mHidenPayAdapter.getItem(position);
            tvTimeLong.setText(payItem.getStrTime());
            tvPrice.setText("￥" + payItem.getPrice());

            choosehasDiscount = payItem.hasDiscount();
            chooseMonthTime = payItem.getMonthCount();
            return false;
        }
    };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_gym, container, false);
        ButterKnife.bind(this, view);
        //
        RxBus.getBus()
            .post(new ToolbarBean.Builder().title("高级版").menu(R.menu.menu_pay_history).listener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    Intent toHistory = new Intent(getActivity(), GymActivity.class);
                    toHistory.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus.Builder().isSingle(true).build());
                    toHistory.putExtra(GymActivity.GYM_TO, GymActivity.GYM_HISORY);
                    startActivity(toHistory);
                    return true;
                }
            }).build());

        mIsPro = GymUtils.getSystemEndDay(gymWrapper.getCoachService()) >= 0;

        rvFunction.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName(getString(R.string.module_student_cards))
            .text(R.string.module_student_cards_desc)
            .img(R.drawable.moudule_student_card)
            .build()));
        mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName("运营推广")
            .text(R.string.module_op_activity_desc)
            .img(R.drawable.moudule_op_activity)
            .build()));
        mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName("工作人员管理")
            .text(R.string.module_manage_staff_desc)
            .img(R.drawable.moudule_manage_staff)
            .build()));
        mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName("完整报表")
            .text(R.string.module_fi_card_statement_desc)
            .img(R.drawable.moudule_fi_card_statement)
            .build()));
        mFunAdapter = new CommonFlexAdapter(mFunDatas);
        rvFunction.setNestedScrollingEnabled(false);
        rvFunction.setAdapter(mFunAdapter);

        tvBrandName.setText(gymWrapper.brand_name() + gymWrapper.name());
        RxRegiste(new RestRepository().getGet_api()
            .qcGetGymPay(App.staffId, GymUtils.getParams(gymWrapper.getCoachService(), null))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<RenewalList>>() {
                @Override public void call(QcResponseData<RenewalList> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mOriDatas.clear();
                        mDisCountDatas.clear();
                        if (qcResponse.getData().normal != null) {
                            for (int i = 0; i < qcResponse.getData().normal.size(); i++) {
                                RenewalPay renewalPay = qcResponse.getData().normal.get(i);
                                mOriDatas.add(new PayItem(renewalPay.name, null, renewalPay.price, renewalPay.times));
                                mDisCountDatas.add(
                                    new PayItem(renewalPay.name, renewalPay.price, renewalPay.favorable_price, renewalPay.times));
                            }
                        }
                        initPay(qcResponse.getData().is_regular);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
        return view;
    }

    @OnClick({ R.id.tv_hide_title, R.id.btn_show_all }) public void onShowAll() {
        btnShowAll.toggle();
        ViewCompat.setPivotY(layoutHiden, 0f);
        if (btnShowAll.isChecked()) {
            ViewCompat.setScaleY(layoutHiden, 0);
            layoutHiden.setVisibility(View.VISIBLE);
            ViewCompat.animate(layoutHiden).scaleY(1).setDuration(300).setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                @Override public void onAnimationUpdate(View view) {
                    rootScroll.fullScroll(View.FOCUS_DOWN);
                }
            }).start();
        } else {
            rootScroll.smoothScrollBy(0, -layoutHiden.getHeight());
            ViewCompat.animate(layoutHiden).scaleY(0).setDuration(300).setListener(new ViewPropertyAnimatorListener() {
                @Override public void onAnimationStart(View view) {

                }

                @Override public void onAnimationEnd(View view) {
                    if (!btnShowAll.isChecked()) layoutHiden.setVisibility(View.GONE);
                }

                @Override public void onAnimationCancel(View view) {

                }
            }).start();
        }
    }

    @OnClick(R.id.btn_pay) public void onClickPay() {
        showLoading();
        RxRegiste(new RestRepository().getPost_api()
            .qcRenew(new RenewBody.Builder().app_id(getString(R.string.wechat_code))
                .type("gym_time")
                .channel(12)
                .favorable(choosehasDiscount)
                .times(chooseMonthTime)
                .id(gymWrapper.id())
                .model(gymWrapper.model())
                .build())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseRenew>() {
                @Override public void call(QcResponseRenew qcResponse) {
                    hideLoading();
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        WebActivity.startWebForResult(qcResponse.data.url, getActivity(), 404);
                    } else {

                        // ToastUtils.logHttp(qcResponse);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    hideLoading();
                }
            }));
    }

    private void initPay(boolean hasDiscount) {
        if (hasDiscount) {
            /**
             * 高级版 已满12月
             */
            tvPayTitle.setText("您可以享受以下优惠");
            tagPro.setVisibility(View.GONE);
            tvPayHint.setVisibility(View.VISIBLE);
            tvPayHint.setText("您已满足优惠条件：单场馆累计付费12个月以上。同品牌 下场馆可享受以下优惠");
            tvHideTitle.setText("原价");
            hidenTrans.setVisibility(View.GONE);
            mPayAdapter = new CommonFlexAdapter(mDisCountDatas, mPayClickListener);
            mHidenPayAdapter = new CommonFlexAdapter(mOriDatas, mHidenClickListener);
        } else {
            /**
             * 高级版 不足12月
             */

            tvPayTitle.setText("高级版");
            tagPro.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);

            tvHideTitle.setText("优惠");
            tvHidenHint.setText("单场馆累计付费12个月以上，同品牌下场馆即可享受以下优惠");

            mPayAdapter = new CommonFlexAdapter(mOriDatas, mPayClickListener);
            mHidenPayAdapter = new CommonFlexAdapter(mDisCountDatas, mHidenClickListener);
        }
        //        } else {
        //            /**
        //             * free版
        //             */
        //            tvPayTitle.setText("高级版");
        //            tagPro.setVisibility(View.VISIBLE);
        //            tvPayHint.setVisibility(View.GONE);
        //            tvHidenHint.setVisibility(View.VISIBLE);
        //            tvHideTitle.setText("优惠");
        //            tvHidenHint.setText("单场馆累计付费12个月以上，同品牌下场馆即可享受以下优惠");
        //
        //
        //            mPayAdapter = new CommonFlexAdapter(mOriDatas, mPayClickListener);
        //            mHidenPayAdapter = new CommonFlexAdapter(mDisCountDatas, mHidenClickListener);
        //
        //        }
        hidenTrans.setVisibility(hasDiscount ? View.INVISIBLE : View.VISIBLE);

        mPayAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        rvFirstPayChoose.setNestedScrollingEnabled(false);
        rvFirstPayChoose.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFirstPayChoose.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        if (mOriDatas.size() >= 3) {
            mPayAdapter.toggleSelection(2);
            PayItem payItem = (PayItem) mPayAdapter.getItem(2);
            tvTimeLong.setText(payItem.getStrTime());
            tvPrice.setText("￥" + payItem.getPrice());
            choosehasDiscount = payItem.hasDiscount();
            chooseMonthTime = payItem.getMonthCount();
        }
        rvFirstPayChoose.setAdapter(mPayAdapter);

        mHidenPayAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        rvSecoundPayChoose.setNestedScrollingEnabled(false);
        rvSecoundPayChoose.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSecoundPayChoose.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        rvSecoundPayChoose.setAdapter(mHidenPayAdapter);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 404) {
                getActivity().finish();
            }
        }
    }

    @Override public String getFragmentName() {
        return UpgrateGymFragment.class.getName();
    }
}
