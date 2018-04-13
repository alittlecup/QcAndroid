package cn.qingchengfit.staffkit.views.allotsales.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MutiChooseSalersActivity extends BaseActivity
    implements MutiChooseSalersPresenterPresenter.MVPView, FlexibleAdapter.OnItemClickListener {
    public static final String INPUT_STUDENT = "mutichoosesalers_input_student";
    public static final String INPUT_SALERS = "mutichoosesalers_input_salers";
    public static final String INPUT_CURRENT = "mutichoosesalers_current_salers";
    public static final String INPUT_TYPE = "mutichoosesalers_input_type";
    public static final int CHOOSE = 0;
    public static final int CHANGE = 1;
    public static final int ALLOT = 2;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitile;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerview;
    @Inject MutiChooseSalersPresenterPresenter mPresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    // 出入的当前销售的id
    String currentSalerId;
    // 传入的 所属销售的 id
    List<String> salerIds = new ArrayList<>();
    private CommonFlexAdapter mFlexAdapter;
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    private List<String> mStudentId = new ArrayList<>();
    private List<String> mChoosedSalersList = new ArrayList<>();
    private int mPageType = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_choose_salers);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        mPresenter.onNewSps();
        mStudentId = getIntent().getStringArrayListExtra(INPUT_STUDENT);
        mChoosedSalersList = getIntent().getStringArrayListExtra(INPUT_SALERS);
        if (mChoosedSalersList == null) mChoosedSalersList = new ArrayList<>();
        mPageType = getIntent().getIntExtra(INPUT_TYPE, 0);
        if (mPageType == CHANGE) {
            mToolbarTitile.setText(R.string.change_saler);
        } else {
            mToolbarTitile.setText(getString(R.string.allot_saler));
        }

        // 出入的当前销售的id
        currentSalerId = getIntent().getStringExtra(INPUT_CURRENT);
        // 传入的 所属销售的 id
        for (String id : mChoosedSalersList) {
            String temp = id;
            salerIds.add(temp);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setPadding(mToolbar.getPaddingLeft(), MeasureUtils.getStatusBarHeight(this),mToolbar.getPaddingRight(),mToolbar.getPaddingBottom());
        mToolbar.inflateMenu(R.menu.menu_compelete);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_complete) {
                    ArrayList<Staff> resultSalers = new ArrayList<Staff>();
                    String salernames = "";
                    mChoosedSalersList.clear();
                    for (int i = 0; i < mFlexAdapter.getSelectedPositions().size(); i++) {
                        AbstractFlexibleItem flexibleItem = mDatas.get(mFlexAdapter.getSelectedPositions().get(i));
                        if (flexibleItem instanceof ChooseSalerItem) {
                            resultSalers.add(((ChooseSalerItem) flexibleItem).getSaler());
                            salernames = TextUtils.concat(salernames, ((ChooseSalerItem) flexibleItem).getSaler().username,
                                i < mFlexAdapter.getSelectedPositions().size() - 1 ? "," : "").toString();
                            mChoosedSalersList.add(((ChooseSalerItem) flexibleItem).getSaler().id);
                        }
                    }

                    if (mPageType > 0 && TextUtils.isEmpty(salernames)) {
                        onShowError(R.string.alert_choose_one_saler);
                        return true;
                    } else {

                    }
                    if (getIntent().getBooleanExtra("hasReturn", false)) {
                        Intent rst = new Intent();
                        rst.putParcelableArrayListExtra("result", resultSalers);
                        setResult(Activity.RESULT_OK, rst);
                        MutiChooseSalersActivity.this.finish();
                    } else {
                        String msg = "";
                        if (mPageType > 0 && !TextUtils.isEmpty(currentSalerId) && !mChoosedSalersList.contains(currentSalerId)) {
                            msg = getString(R.string.alert_comfirm_trans, salernames);
                        } else if (mPageType > 0) {
                            msg = getString(R.string.alert_comfirm_allot, salernames);
                        } else if (mPageType == 0 && mChoosedSalersList.size() == 0) {
                            msg = getString(R.string.alert_comfirm_change);
                        } else if (mPageType == 0) {
                            msg = getString(R.string.alert_comfirm_allot, salernames);
                        }
                        new MaterialDialog.Builder(MutiChooseSalersActivity.this).content(msg)
                            .positiveText(R.string.common_comfirm)
                            .negativeText(R.string.common_cancel)
                            .autoDismiss(true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mPresenter.allotSalers(App.staffId, mStudentId, mChoosedSalersList, currentSalerId);
                                }
                            })
                            .show();
                    }
                }
                return true;
            }
        });

        if (gymWrapper.inBrand()) {
            ToastUtils.show(getString(R.string.must_operate_in_gym));
            this.finish();
        } else {
            mPresenter.querySalers(loginStatus.staff_id());
        }
    }

    @Override protected void onDestroy() {
        mPresenter.unattachView();
        super.onDestroy();
    }

    @Override public void onSalers(List<Staff> salers) {
        if (salers != null && salers.size() > 0) {
            mDatas.clear();
            List<Integer> chose = new ArrayList<>();
            for (int i = 0; i < salers.size(); i++) {
                mDatas.add(new ChooseSalerItem(salers.get(i)));
                if (mChoosedSalersList != null && mChoosedSalersList.contains(salers.get(i).id)) chose.add(i);
            }

            mFlexAdapter = new CommonFlexAdapter(mDatas, this);
            mFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);

            mFlexAdapter.setAnimationOnScrolling(true)
                .setAnimationInitialDelay(50L)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationDelay(100L)
            ;

            GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(this, 4);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override public int getSpanSize(int position) {
                    return 1;
                }
            });

            mRecyclerview.setItemViewCacheSize(4);
            mRecyclerview.setLayoutManager(gridLayoutManager);
            mRecyclerview.addItemDecoration(new SpaceItemDecoration(20, this));

            final List<Integer> is = chose;
            for (int i = 0; i < is.size(); i++) {
                mFlexAdapter.toggleSelection(is.get(i));
            }
            mRecyclerview.setAdapter(mFlexAdapter);
        } else {
            // TODO: 16/10/19 无销售
        }
    }

    @Override public void onAllotSuccess() {
        ToastUtils.show(getString(R.string.allot_saler_success));
        setResult(RESULT_OK);
        this.finish();
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void showSelectSheet(String title, List<String> strs,
      AdapterView.OnItemClickListener listener) {

    }

    @Override public void popBack() {

    }

    @Override public void popBack(int count) {

    }

    @Override public boolean onItemClick(int position) {
        mFlexAdapter.toggleSelection(position);
        return true;
    }
}
