package cn.qingchengfit.staffkit.allocate.coach;

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
import cn.qingchengfit.staffkit.allocate.coach.item.ChooseCoachItem;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachBean;
import cn.qingchengfit.staffkit.allocate.coach.presenter.MutiChooseCoachPresenter;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.utils.DialogUtils;
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

/**
 * Created by fb on 2017/5/4.
 */

public class MutiChooseCoachActivity extends BaseActivity
    implements MutiChooseCoachPresenter.CoachAllView, FlexibleAdapter.OnItemClickListener {
    public static final String INPUT_STUDENT = "student";
    public static final String INPUT_COACHES = "coaches";
    public static final String INPUT_CURRENT = "current_coach";
    public static final String INPUT_TYPE = "operation_type";
    public static final int CHANGE = 1;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitile;
    @BindView(R.id.all_coach_recyclerView) RecyclerView mRecyclerview;
    @Inject MutiChooseCoachPresenter mPresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private CommonFlexAdapter mFlexAdapter;
    private List<ChooseCoachItem> mDatas = new ArrayList<>();

    private List<String> mChoosedCoachsList = new ArrayList<>();
    private List<String> mStudentList = new ArrayList<>();
    private ArrayList<String> mNameList = new ArrayList<>();
    private String coachId;

    private int mPageType = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_all_coach);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        if (getIntent().getStringArrayListExtra(INPUT_COACHES) != null) {
            mChoosedCoachsList = getIntent().getStringArrayListExtra(INPUT_COACHES);
        }
        if (getIntent().getStringArrayListExtra(INPUT_STUDENT) != null) {
            mStudentList = getIntent().getStringArrayListExtra(INPUT_STUDENT);
        }
        if (getIntent().getStringExtra(INPUT_CURRENT) != null) {
            coachId = getIntent().getStringExtra(INPUT_CURRENT);
        }

        mPageType = getIntent().getIntExtra(INPUT_TYPE, 0);
        if (mPageType == CHANGE) {
            mToolbarTitile.setText(R.string.change_coach);
        } else {
            mToolbarTitile.setText(getString(R.string.allocate_coach));
        }

        setToolbar();

        if (gymWrapper.inBrand()) {
            ToastUtils.show(getString(R.string.must_operate_in_gym));
            this.finish();
        } else {
            mPresenter.getCoachPreviewList();
        }

        mFlexAdapter = new CommonFlexAdapter(mDatas, this);
        mFlexAdapter.setMode(SelectableAdapter.MODE_MULTI);

        mFlexAdapter.setAnimationOnScrolling(true)
            .setAnimationInitialDelay(50L)
            .setAnimationInterpolator(new DecelerateInterpolator())
            .setAnimationDelay(100L)
            .setAnimationStartPosition(0);

        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                return 1;
            }
        });
        mRecyclerview.setItemViewCacheSize(4);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.addItemDecoration(new SpaceItemDecoration(20, this));
        mRecyclerview.setAdapter(mFlexAdapter);
    }

    private void setToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_titlebar_back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_compelete);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_complete) {
                    ArrayList<CoachBean> resultSalers = new ArrayList<CoachBean>();
                    String salernames = "";
                    mChoosedCoachsList.clear();
                    for (int i = 0; i < mFlexAdapter.getSelectedPositions().size(); i++) {
                        AbstractFlexibleItem flexibleItem = mDatas.get(mFlexAdapter.getSelectedPositions().get(i));
                        if (flexibleItem != null) {
                            CoachBean coachBean = ((ChooseCoachItem) flexibleItem).getData().coach;
                            resultSalers.add(coachBean);
                            salernames = TextUtils.concat(salernames, coachBean.username,
                                i < mFlexAdapter.getSelectedPositions().size() - 1 ? "," : "").toString();
                            mChoosedCoachsList.add(coachBean.id);
                            mNameList.add(coachBean.username);
                        }
                    }
                    if (mPageType > 0 && TextUtils.isEmpty(salernames)) {
                        onShowError(R.string.alert_choose_one_coach);
                        return true;
                    } else {

                    }
                    if (getIntent().getBooleanExtra("hasReturn", false)) {
                        Intent rst = new Intent();
                        rst.putParcelableArrayListExtra("result", resultSalers);
                        setResult(RESULT_OK, rst);
                        finish();
                    } else {
                        String msg = "";
                        if (mPageType != CHANGE && mChoosedCoachsList.size() == 0) {
                            DialogUtils.showAlert(MutiChooseCoachActivity.this, "至少选择一个教练");
                            return true;
                        } else if (mPageType > 0 && !TextUtils.isEmpty(coachId) && !mChoosedCoachsList.contains(coachId)) {
                            msg = getString(R.string.alert_comfirm_trans, salernames);
                        } else if (mPageType > 0) {
                            msg = getString(R.string.alert_comfirm_allot, salernames);
                        } else if (mPageType == 0 && mChoosedCoachsList.size() == 0) {
                            msg = getString(R.string.alert_comfirm_change_coach);
                        } else if (mPageType == 0) {
                            msg = getString(R.string.alert_comfirm_allot, salernames);
                        }
                        new MaterialDialog.Builder(MutiChooseCoachActivity.this).content(msg)
                            .positiveText(R.string.common_comfirm)
                            .negativeText(R.string.common_cancel)
                            .autoDismiss(true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mPresenter.allotCoaches(App.staffId, mStudentList, mChoosedCoachsList);
                                }
                            })
                            .show();
                    }
                }
                return true;
            }
        });
    }

    @Override protected void onDestroy() {
        mPresenter.unattachView();
        super.onDestroy();
    }

    @Override public void onCoaches(List<Staff> salers) {
        if (salers != null && salers.size() > 0) {
            mDatas.clear();
            int position = -1;
            List<Integer> chose = new ArrayList<>();
            for (int i = 0; i < salers.size(); i++) {
                if (salers.get(i).id != null) {
                    Coach coach = new Coach();
                    coach.coach = new CoachBean();
                    coach.coach.username = salers.get(i).username;
                    coach.coach.id = salers.get(i).id;
                    coach.coach.avatar = salers.get(i).avatar;
                    mDatas.add(new ChooseCoachItem(coach));
                    if (mChoosedCoachsList.size() > 0) {
                        if (mChoosedCoachsList != null && mChoosedCoachsList.contains(salers.get(i).id)) chose.add(i);
                    }
                    if (coachId != null && coachId.equals(salers.get(i).id)) {
                        position = i;
                    }
                }
            }
            if (position >= 0) {
                mFlexAdapter.toggleSelection(position);
                mFlexAdapter.notifyItemChanged(position);
            }
            for (int j = 0; j < chose.size(); j++) {
                mFlexAdapter.toggleSelection(chose.get(j));
                mFlexAdapter.notifyItemChanged(chose.get(j));
            }
        }
        mFlexAdapter.notifyDataSetChanged();
    }

    @Override public void onAllotSuccess() {
        hideLoadingTransparent();
        ToastUtils.show("成功");
        Intent intent = new Intent();
        intent.putStringArrayListExtra("coaches", mNameList);
        intent.putExtra("isRemove", !mChoosedCoachsList.contains(coachId));
        setResult(RESULT_OK, intent);
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
        mFlexAdapter.notifyItemChanged(position);
        return true;
    }
}

