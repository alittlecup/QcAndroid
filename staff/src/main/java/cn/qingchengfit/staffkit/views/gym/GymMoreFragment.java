package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import cn.qingchengfit.staffkit.views.gym.items.EmptyFunItem;
import cn.qingchengfit.staffkit.views.gym.items.FunHeaderItem;
import cn.qingchengfit.staffkit.views.gym.items.GymFuntionItem;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.staffkit.views.login.SplashActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.staffkit.views.gym.GymDetailFragment.RESULT_STAFF_MANAGE;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * 更多功能/所有功能页
 * <p/>
 * Created by Paper on 16/5/6 2016.
 */
public class GymMoreFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, GymMorePresenter.MVPView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.recycleview) RecyclerView mRecyclerView;
    @BindView(R.id.my_fun_recycleview) RecyclerView myFunRecycleview;
    @Inject GymMorePresenter mGymMorePresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject GymFunctionFactory gymFunctionFactory;
    private GymMoreAdapter mAdapter;
    /**
     * 我的功能呢详情页
     */
    private GymMoreAdapter mFunAdapter;
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    private List<AbstractFlexibleItem> mFunsDatas = new ArrayList<>();
    private boolean mEditableMode = false;
    private FunHeaderItem mMyFuntions;

    @Inject public GymMoreFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyFuntions = new FunHeaderItem("常用功能");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gymmore, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(mGymMorePresenter, this);
        ViewCompat.setTransitionName(myFunRecycleview, "funcitonView");
        initToolbar(toolbar);
        toolbarTitile.setText("全部功能");
        toolbar.inflateMenu(R.menu.menu_mangage);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (mEditableMode) {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_mangage);
                    mFunAdapter.setStatus(0);
                    mAdapter.setStatus(0);
                    mFunAdapter.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                    List<String> modules = new ArrayList<String>();
                    for (int i = 0; i < mFunsDatas.size(); i++) {
                        if (mFunsDatas.get(i) instanceof GymFuntionItem) {
                            modules.add(((GymFuntionItem) mFunsDatas.get(i)).getGymFuntion().getModuleName());
                        }
                    }
                    mGymMorePresenter.updateFunction(modules);
                } else {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_compelete);

                    mFunAdapter.setStatus(GymFuntionItem.STATUS_REMOVE);
                    mAdapter.setStatus(GymFuntionItem.STATUS_ADD);
                    mAdapter.mOtherDatas.clear();
                    mAdapter.mOtherDatas.addAll(mFunsDatas);
                    mFunAdapter.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                }
                mEditableMode = !mEditableMode;
                return true;
            }
        });

        RxBusAdd(EventFreshCoachService.class).subscribe(new Action1<EventFreshCoachService>() {
            @Override public void call(EventFreshCoachService eventFreshCoachService) {
                mAdapter.setTag("isPro", true);
                mAdapter.notifyDataSetChanged();
                mFunAdapter.setTag("isPro", true);
                mFunAdapter.notifyDataSetChanged();
            }
        });
        myFunRecycleview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(myFunRecycleview.getViewTreeObserver(), this);
                mFunAdapter = new GymMoreAdapter(mFunsDatas, new FlexibleAdapter.OnItemClickListener() {
                    @Override public boolean onItemClick(int position) {
                        if (mFunAdapter.getItem(position) instanceof GymFuntionItem) {
                            if (mEditableMode) {
                                mFunsDatas.remove(position);
                                int len = mFunsDatas.size() - 1;
                                for (int i = len; i >= 0; i--) {
                                    if (mFunsDatas.get(i) instanceof GymFuntionItem) {
                                        if (((GymFuntionItem) mFunsDatas.get(i)).getGymFuntion().getImg() == 0) mFunsDatas.remove(i);
                                    }
                                }
                                notificationMyFuntion();
                                notificationAllFuntion();
                            } else {
                                if (!gymWrapper.isPro()
                                    && GymFunctionFactory.getModuleStatus(
                                    ((GymFuntionItem) mFunAdapter.getItem(position)).getGymFuntion().getModuleName()) > 0) {
                                    new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
                                    return true;
                                }
                                gymFunctionFactory.getJumpIntent(
                                    ((GymFuntionItem) mFunAdapter.getItem(position)).getGymFuntion().getModuleName(),
                                    gymWrapper.getCoachService(), null, null, GymMoreFragment.this);
                            }
                        }
                        return true;
                    }
                });
                mFunAdapter.setTag("isPro", gymWrapper.isPro());
                mFunAdapter.setDisplayHeadersAtStartUp(true);
                myFunRecycleview.setHasFixedSize(true);
                SmoothScrollGridLayoutManager layoutManager1 = new SmoothScrollGridLayoutManager(getContext(), 4);
                layoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override public int getSpanSize(int position) {
                        switch (mFunAdapter.getItemViewType(position)) {
                            case R.layout.item_gym_function:

                                return 1;
                            default:
                                return 4;
                        }
                    }
                });
                myFunRecycleview.setNestedScrollingEnabled(false);
                myFunRecycleview.setLayoutManager(layoutManager1);
                myFunRecycleview.setAdapter(mFunAdapter);

                mDatas.clear();
                FunHeaderItem workFuntion = new FunHeaderItem("工作台");
                mDatas.add(workFuntion);
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WORKSPACE_ORDER_LIST), workFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WORKSPACE_GROUP), workFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WORKSPACE_PRIVATE), workFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WORKSPACE_ORDER_SIGNIN),
                    workFuntion));

                FunHeaderItem serviceFuntion = new FunHeaderItem("健身服务");
                mDatas.add(serviceFuntion);
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_SERVICE_GROUP), serviceFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_SERVICE_PRIVATE), serviceFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_SERVICE_FREE), serviceFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_SERVICE_SHOP), serviceFuntion));

                FunHeaderItem studentsFuntion = new FunHeaderItem("会员管理");
                mDatas.add(studentsFuntion);
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_STUDENT), studentsFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_STUDENT_CARDS), studentsFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_STUDENT_BODY_TEST),
                    studentsFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.REPORT_EXPORT), studentsFuntion));

                FunHeaderItem internalFuntion = new FunHeaderItem("员工管理");
                mDatas.add(internalFuntion);
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_MANAGE_STAFF), internalFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_MANAGE_COACH), internalFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), internalFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), internalFuntion));

                FunHeaderItem runFuntion = new FunHeaderItem("运营推广");
                mDatas.add(runFuntion);
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_SCORE), runFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_ACTIVITY), runFuntion));
                //                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_AD), runFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERTAT_KOUBEI), runFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_REGIST), runFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_ANOUNCE), runFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_OPERATE_COMPETITION), runFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), runFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), runFuntion));

                FunHeaderItem financialFuntion = new FunHeaderItem("财务与报表");
                mDatas.add(financialFuntion);
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINACE_ONLINE), financialFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINANCE_COURSE), financialFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINANCE_SALE), financialFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINANCE_MARK), financialFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINANCE_CARD), financialFuntion));
                mDatas.add(
                    new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_FINANCE_SIGN_IN), financialFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), financialFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), financialFuntion));

                FunHeaderItem gymFuntion = new FunHeaderItem("场馆管理");
                mDatas.add(gymFuntion);
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_GYM_INFO), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_GYM_TIME), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_GYM_SITE), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_MSG), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WARDROBE), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_HOME), gymFuntion));
                //        mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_WECHAT), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), gymFuntion));
                mDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), gymFuntion));

                mAdapter = new GymMoreAdapter(mDatas, GymMoreFragment.this);
                mAdapter.setDisplayHeadersAtStartUp(true);
                mAdapter.setTag("isPro", gymWrapper.isPro());
                mRecyclerView.setHasFixedSize(true);
                SmoothScrollGridLayoutManager layoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override public int getSpanSize(int position) {
                        switch (mAdapter.getItemViewType(position)) {
                            case R.layout.item_gym_function:
                                return 1;
                            default:
                                return 4;
                        }
                    }
                });

                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void notificationMyFuntion() {
        int addcount = ((4 - (mFunsDatas.size()-1) % 4) % 4);
        for (int i = 0; i < addcount; i++) {
            mFunsDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE), mMyFuntions));
        }
        if (mFunsDatas.size() == 1) mFunsDatas.add(new EmptyFunItem());
        mFunAdapter.updateDataSet(mFunsDatas);
    }

    private void notificationAllFuntion() {
        mAdapter.mOtherDatas.clear();
        mAdapter.mOtherDatas.addAll(mFunsDatas);
        mAdapter.notifyDataSetChanged();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof GymFuntionItem) {
            if (mEditableMode) {
                if (mFunsDatas.contains((GymFuntionItem) mAdapter.getItem(position))) return true;
                if (mFunsDatas.size() == 2 && mFunsDatas.get(1) instanceof EmptyFunItem) {
                    mFunsDatas.remove(1);
                }
                int len = mFunsDatas.size() - 1;
                for (int i = len; i >= 0; i--) {
                    if (mFunsDatas.get(i) instanceof GymFuntionItem) {
                        if (((GymFuntionItem) mFunsDatas.get(i)).getGymFuntion().getImg() == 0) mFunsDatas.remove(i);
                    }
                }
                ((GymFuntionItem) mAdapter.getItem(position)).setHeader(mMyFuntions);
                mFunsDatas.add((GymFuntionItem) mAdapter.getItem(position));
                notificationMyFuntion();
                notificationAllFuntion();
            } else {
                if (!gymWrapper.isPro()
                    && GymFunctionFactory.getModuleStatus(((GymFuntionItem) mAdapter.getItem(position)).getGymFuntion().getModuleName())
                    > 0) {
                    new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
                    return true;
                }
                gymFunctionFactory.getJumpIntent(((GymFuntionItem) mAdapter.getItem(position)).getGymFuntion().getModuleName(),
                    gymWrapper.getCoachService(), null, null, this);
            }
        }
        return true;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_STAFF_MANAGE && resultCode == 111) {
            onQuitGym();
        }
    }

    public void onQuitGym() {
        if (gymWrapper.singleMode()) {
            if (getActivity() instanceof MainActivity) {
                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onModule(List<String> modules) {

        mFunsDatas.clear();
        mFunsDatas.add(mMyFuntions);
        if (modules != null) {
            for (int i = 0; i < modules.size(); i++) {
                if (TextUtils.isEmpty(modules.get(i))) continue;
                mFunsDatas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(modules.get(i)), mMyFuntions));
            }
        }
        if (mFunsDatas.size() == 1) {
            mFunsDatas.add(new EmptyFunItem());
            mFunAdapter.notifyDataSetChanged();
        } else {
            notificationMyFuntion();
        }
    }
}
