package cn.qingchengfit.staffkit.views.gym.upgrate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.body.RenewBody;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.model.responese.QcResponseRenew;
import cn.qingchengfit.model.responese.RenewalList;
import cn.qingchengfit.model.responese.RenewalPay;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.staffkit.views.gym.upgrate.item.LinearFunItem;
import cn.qingchengfit.staffkit.views.gym.upgrate.item.PayItem;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
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

	RecyclerView rvFunction;
	TextView tvPayTitle;
	TextView tagPro;
	TextView tvPayHint;
	RecyclerView rvFirstPayChoose;
	TextView tvHideTitle;
	AnimatedButton btnShowAll;
	RecyclerView rvSecoundPayChoose;
	TextView tvBrandName;
	TextView tvTimeLong;
	TextView tvPrice;
	TextView tvHidenHint;
	NestedScrollView rootScroll;
	View hidenTrans;
	RelativeLayout layoutHiden;
	TextView tvFirstDiscount;
	TextView tvFirstDiscountHide;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject StaffRespository staffRespository;
  boolean mIsPro;
  private CommonFlexAdapter mFunAdapter;
  private List<AbstractFlexibleItem> mFunDatas = new ArrayList<>();
  private CommonFlexAdapter mPayAdapter;
  private CommonFlexAdapter mHidenPayAdapter;
  private List<AbstractFlexibleItem> mOriDatas = new ArrayList<>();
  private List<AbstractFlexibleItem> mDisCountDatas = new ArrayList<>();

  private boolean choosehasDiscount = false;
  private boolean has12MonDiscount = false;  //是否有满12月优惠
  private int chooseMonthTime = 12;
  private FlexibleAdapter.OnItemClickListener mPayClickListener =
    new FlexibleAdapter.OnItemClickListener() {
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
        choosehasDiscount = has12MonDiscount;
        return false;
      }
    };
  private FlexibleAdapter.OnItemClickListener mHidenClickListener =
    new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int position) {
        mPayAdapter.clearSelection();
        mHidenPayAdapter.clearSelection();
        mHidenPayAdapter.toggleSelection(position);
        mHidenPayAdapter.notifyDataSetChanged();
        mPayAdapter.notifyDataSetChanged();

        PayItem payItem = (PayItem) mHidenPayAdapter.getItem(position);
        tvTimeLong.setText(payItem.getStrTime());
        tvPrice.setText("￥" + payItem.getPrice());

        choosehasDiscount = !has12MonDiscount;
        chooseMonthTime = payItem.getMonthCount();
        return false;
      }
    };
  long stayTime ;
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SensorsUtils.track("QcSaasStudioRenewVisit").commit(getContext());
    stayTime = System.currentTimeMillis()/1000;
  }

  @Override public void onDestroy() {
    SensorsUtils.track("QcSaasStudioRenewLeave")
      .addProperty("QcSaasStudioRenewDuration",System.currentTimeMillis()/1000 - stayTime)
      .commit(getContext());
    super.onDestroy();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_upgrade_gym, container, false);
    rvFunction = (RecyclerView) view.findViewById(R.id.rv_function);
    tvPayTitle = (TextView) view.findViewById(R.id.tv_pay_title);
    tagPro = (TextView) view.findViewById(R.id.tag_pro);
    tvPayHint = (TextView) view.findViewById(R.id.tv_pay_hint);
    rvFirstPayChoose = (RecyclerView) view.findViewById(R.id.rv_first_pay_choose);
    tvHideTitle = (TextView) view.findViewById(R.id.tv_hide_title);
    btnShowAll = (AnimatedButton) view.findViewById(R.id.btn_show_all);
    rvSecoundPayChoose = (RecyclerView) view.findViewById(R.id.rv_secound_pay_choose);
    tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
    tvTimeLong = (TextView) view.findViewById(R.id.tv_time_long);
    tvPrice = (TextView) view.findViewById(R.id.tv_price);
    tvHidenHint = (TextView) view.findViewById(R.id.tv_hiden_hint);
    rootScroll = (NestedScrollView) view.findViewById(R.id.root_scroll);
    hidenTrans = (View) view.findViewById(R.id.hiden_trans);
    layoutHiden = (RelativeLayout) view.findViewById(R.id.layout_hiden);
    tvFirstDiscount = (TextView) view.findViewById(R.id.tv_first_discount);
    tvFirstDiscountHide = (TextView) view.findViewById(R.id.tv_first_discount_hide);
    view.findViewById(R.id.tv_hide_title).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onShowAll();
      }
    });
    view.findViewById(R.id.btn_show_all).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onShowAll();
      }
    });
    view.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickPay();
      }
    });

    RxBus.getBus()
      .post(new ToolbarBean.Builder().title("高级版")
        .menu(R.menu.menu_pay_history)
        .listener(new Toolbar.OnMenuItemClickListener() {
          @Override public boolean onMenuItemClick(MenuItem item) {
            Intent toHistory = new Intent(getActivity(), GymActivity.class);
            toHistory.putExtra(Configs.EXTRA_GYM_STATUS,
              new GymStatus.Builder().isSingle(true).build());
            toHistory.putExtra(GymActivity.GYM_TO, GymActivity.GYM_HISORY);
            startActivity(toHistory);
            return true;
          }
        })
        .build());

    mIsPro = GymUtils.getSystemEndDay(gymWrapper.getCoachService()) >= 0;

    rvFunction.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    mFunDatas.add(new LinearFunItem(
      new GymFuntion.Builder().moduleName(getString(R.string.module_student_cards))
        .text(R.string.module_student_cards_desc)
        .img(R.drawable.moudule_student_card)
        .build()));
    mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName("运营推广")
      .text(R.string.module_op_activity_desc)
      .img(R.drawable.moudule_op_activity)
      .build()));
    mFunDatas.add(new LinearFunItem(new GymFuntion.Builder().moduleName("员工管理")
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
    RxRegiste(staffRespository.getStaffAllApi()
      .qcGetGymPay(App.staffId, GymUtils.getParams(gymWrapper.getCoachService(), null))
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcDataResponse<RenewalList>>() {
        @Override public void call(QcDataResponse<RenewalList> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mOriDatas.clear();
            mDisCountDatas.clear();
            boolean hasFirst = (qcResponse.getData().has_first_month_favorable
              && qcResponse.getData().first_month_favorable != null);
            RenewalPay renewalPay399 = null;
            if (hasFirst) { renewalPay399 = qcResponse.getData().first_month_favorable.get(0);}

            if (qcResponse.getData().normal != null) {
              for (int i = 0; i < (hasFirst ? qcResponse.getData().first_month_favorable.size()
                : qcResponse.getData().normal.size()); i++) {
                RenewalPay renewalPay = qcResponse.getData().normal.get(i);
                mOriDatas.add(
                  new PayItem(renewalPay.name, (hasFirst && i == 0 && renewalPay399 != null) ? renewalPay.price : null,
                    (hasFirst && i == 0 && renewalPay399 != null) ? renewalPay399.favorable_price:renewalPay.price, renewalPay.times));
                mDisCountDatas.add(
                  new PayItem(renewalPay.name, renewalPay.price, renewalPay.favorable_price,
                    renewalPay.times));
              }
            }
            has12MonDiscount = qcResponse.getData().is_regular;
            tvFirstDiscount.setVisibility((hasFirst && !has12MonDiscount) ? View.VISIBLE : View.GONE);
            tvFirstDiscountHide.setVisibility((hasFirst && has12MonDiscount) ? View.VISIBLE : View.GONE);
            initPay(qcResponse.getData().is_regular);
          }else onShowError(qcResponse.getMsg());
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          onShowError(throwable.getMessage());
        }
      }));
    return view;
  }

 public void onShowAll() {
    btnShowAll.toggle();
    ViewCompat.setPivotY(layoutHiden, 0f);
    if (btnShowAll.isChecked()) {
      ViewCompat.setScaleY(layoutHiden, 0);
      layoutHiden.setVisibility(View.VISIBLE);
      ViewCompat.animate(layoutHiden)
        .scaleY(1)
        .setDuration(300)
        .setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
          @Override public void onAnimationUpdate(View view) {
            rootScroll.fullScroll(View.FOCUS_DOWN);
          }
        })
        .start();
    } else {
      rootScroll.smoothScrollBy(0, -layoutHiden.getHeight());
      ViewCompat.animate(layoutHiden)
        .scaleY(0)
        .setDuration(300)
        .setListener(new ViewPropertyAnimatorListener() {
          @Override public void onAnimationStart(View view) {

          }

          @Override public void onAnimationEnd(View view) {
            if (!btnShowAll.isChecked()) layoutHiden.setVisibility(View.GONE);
          }

          @Override public void onAnimationCancel(View view) {

          }
        })
        .start();
    }
  }

 public void onClickPay() {
    String price = tvPrice.getText().toString();
    int numPrice = -1;
    try {
      numPrice = Integer.parseInt(price.replace("￥",""));
    }catch (Exception e){

    }
    //记录场馆充值
    SensorsUtils.track("QcSaasRechargeBtnClick")
      //.addProperty("qc_saas_first_recharge_time",)//当前场馆第一次付费时间
      .addProperty("qc_saas_shop_status",gymWrapper.isPro()?"pro":"free")//当前Saas场馆的状态
      .addProperty("qc_saas_shop_expire_date",gymWrapper.system_end())//当前场馆的过期时间
      .addProperty("qc_saas_recharge_type","wxPay")//Saas场馆续费方式
      .addProperty("qc_saas_shop_has_trialed",gymWrapper.isHasFirst())//Saas场馆是否续费过
      .addProperty("qc_saas_recharge_times",chooseMonthTime)//Saas场馆续费的月数
      .addProperty("qc_saas_recharge_price",numPrice)//Saas场馆续费的价格
      .commit(getContext());

    showLoading();
    RxRegiste(staffRespository.getStaffAllApi()
      .qcCharge(new RenewBody.Builder().app_id(getString(R.string.wechat_code))
        .type("gym_time")
        .channel(12)
        .favorable(choosehasDiscount)
        .times(chooseMonthTime)
        .id(gymWrapper.id())
        .model(gymWrapper.model())
        .build())
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponseRenew>() {
        @Override public void call(QcResponseRenew qcResponse) {
          hideLoading();
          if (ResponseConstant.checkSuccess(qcResponse)) {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra("url", qcResponse.data.url);
            intent.putExtra("request", 404);
            startActivityForResult(intent, 404);
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
      tvHidenHint.setVisibility(View.GONE);
    } else {
      /**
       * 高级版 不足12月
       */

      tvPayTitle.setText("高级版");
      tagPro.setVisibility(View.VISIBLE);
      tvPayHint.setVisibility(View.GONE);

      tvHideTitle.setText("优惠");
      tvHidenHint.setVisibility(View.VISIBLE);
      tvHidenHint.setText("单场馆累计付费12个月以上，同品牌下场馆即可享受以下优惠");

      mPayAdapter = new CommonFlexAdapter(mOriDatas, mPayClickListener);
      mHidenPayAdapter = new CommonFlexAdapter(mDisCountDatas, mHidenClickListener);
    }
    hidenTrans.setVisibility(hasDiscount ? View.INVISIBLE : View.VISIBLE);

    mPayAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    rvFirstPayChoose.setNestedScrollingEnabled(false);
    rvFirstPayChoose.setLayoutManager(
      new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    rvFirstPayChoose.addItemDecoration(
      new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
    if (mOriDatas.size() >= 3) {
      mPayAdapter.toggleSelection(2);
      PayItem payItem = (PayItem) mPayAdapter.getItem(2);
      tvTimeLong.setText(payItem.getStrTime());
      tvPrice.setText("￥" + payItem.getPrice());
      choosehasDiscount = payItem.hasDiscount();
      chooseMonthTime = payItem.getMonthCount();
    }
    rvFirstPayChoose.setAdapter(mPayAdapter);

    mHidenPayAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    rvSecoundPayChoose.setNestedScrollingEnabled(false);
    rvSecoundPayChoose.setLayoutManager(
      new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    rvSecoundPayChoose.addItemDecoration(
      new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
    rvSecoundPayChoose.setAdapter(mHidenPayAdapter);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 404) {
        RxRegiste(staffRespository.getStaffAllApi()
          .qcGetGymDetail(loginStatus.staff_id(), gymWrapper.id(), gymWrapper.model())
          .onBackpressureLatest()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetSubscribe<QcDataResponse<GymDetail>>() {
            @Override public void onNext(QcDataResponse<GymDetail> qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                gymWrapper.setCoachService(qcResponse.getData().gym);
                routeTo("gym","/upgrade/done/",null);
                getActivity().finish();
              } else {
                onShowError(qcResponse.getMsg());
              }
            }
          }));
                        
      }
    }
  }

  @Override public String getFragmentName() {
    return UpgrateGymFragment.class.getName();
  }
}
