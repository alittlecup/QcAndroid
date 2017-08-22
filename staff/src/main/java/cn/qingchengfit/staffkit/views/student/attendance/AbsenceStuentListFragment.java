package cn.qingchengfit.staffkit.views.student.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.common.Absentce;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.EventContactUser;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.followup.ContactTaDialog;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/2/28.
 */

public class AbsenceStuentListFragment extends BaseFragment
    implements AbsenceListPresenter.AbsenceView, FilterFragment.OnSelectListener, FilterCustomFragment.OnBackFilterDataListener,
    FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

    @BindView(R.id.ll_filter_absence_account) ViewGroup llAbsenceAccount;
    @BindView(R.id.text_absence_condition) TextView textAbsence;
    @BindView(R.id.image_absence_account_arrow) ImageView imageAbsenceArrow;
    @BindView(R.id.rl_absence_account) RecyclerView rlAbsenceAccount;
    @BindView(R.id.tips_absence_account) TextView textTipsAccount;
    @BindView(R.id.absence_list_shadow) View shadow;
    @BindView(R.id.frag_absence_rank) ViewGroup filterLayout;
    @BindView(R.id.frag_absence_setting) ViewGroup settingLayout;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject AbsenceListPresenter absenceListPresenter;

    private List<AbstractFlexibleItem> filterList = new ArrayList<>();
    private List<AbstractFlexibleItem> itemList = new ArrayList<>();

    private String start, end;
    private int account;
    private FilterFragment filterFragment;
    private FilterCustomFragment filterCustomFragment;
    private CommonFlexAdapter commonFlexAdapter;
    private ViewGroup.LayoutParams params;
    private boolean isShow = false;

    private String sixTy;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absence_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(absenceListPresenter, this);
        mCallbackActivity.setToolbar("缺勤统计", false, null, 0, null);

        initFilterData();

        filterCustomFragment = FilterCustomFragmentBuilder.newFilterCustomFragment("缺勤天数（天）");
        filterCustomFragment.setOnBackFilterDataListener(this);

        filterFragment = new FilterFragment();
        filterFragment.setItemList(filterList);
        filterFragment.setOnSelectListener(this);

        params = filterLayout.getLayoutParams();
        params.height = 0;
        filterLayout.setLayoutParams(params);

        start = "7";
        end = "30";

        llAbsenceAccount.setTag(false);

        absenceListPresenter.queryAbsenceList(start, end);

        rlAbsenceAccount.setLayoutManager(new LinearLayoutManager(getContext()));
        commonFlexAdapter = new CommonFlexAdapter(itemList, this);
        //commonFlexAdapter.setEndlessScrollListener(this,new ProgressItem(getContext()));
        rlAbsenceAccount.setAdapter(commonFlexAdapter);

        getChildFragmentManager().beginTransaction().replace(R.id.frag_absence_rank, filterFragment).commit();

        toggleViewState();
        RxBusAdd(EventContactUser.class).subscribe(new Action1<EventContactUser>() {
            @Override public void call(EventContactUser eventContactUser) {
                ContactTaDialog.start(AbsenceStuentListFragment.this, 11, eventContactUser.getUser_student().getPhone());
            }
        });
        return view;
    }

    private void initFilterData() {
        filterList.clear();
        filterList.add(new FilterCommonLinearItem(getString(R.string.absence_three_seven)));
        filterList.add(new FilterCommonLinearItem(getString(R.string.absence_thirty)));
        filterList.add(new FilterCommonLinearItem(getString(R.string.absence_sixty)));
        filterList.add(new FilterCommonLinearItem("自定义"));
    }

    private void toggleViewState() {

        textAbsence.setText(String.format(Locale.CHINA, getString(R.string.absence_condition), start, end));
        textTipsAccount.setText(String.format(Locale.CHINA, getString(R.string.absence_account), start, end, account));
        if (!TextUtils.isEmpty(sixTy)) {
            textAbsence.setText(sixTy);
            textTipsAccount.setText(sixTy + "的会员共计" + account + "人");
        }

        imageAbsenceArrow.setImageResource(isShow ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey);
        shadow.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (isShow) {
            filterFragment.setFilterAnimation(filterLayout, isShow);
        } else if (filterLayout.getHeight() > 0) {
            filterFragment.setFilterAnimation(filterLayout, false);
        }
    }

    @OnClick(R.id.ll_filter_absence_account) public void onConditionFilter() {
        if (settingLayout.getHeight() > 0) {
            return;
        }
        isShow = !isShow;
        toggleViewState();
    }

    @OnClick(R.id.absence_list_shadow) public void onDismiss() {
        isShow = false;
        toggleViewState();
    }

    @Override public String getFragmentName() {
        return AbsenceStuentListFragment.class.getName();
    }

    @Override public void onAbsence(List<Absentce> absentces, int curPage, int pages) {
        hideLoading();
        account = pages;
        textTipsAccount.setText(String.format(Locale.CHINA, getString(R.string.absence_account), start, end, account));
        if (curPage == 1) itemList.clear();
        for (Absentce absentce : absentces) {
            itemList.add(new AttendanceStudentItem(absentce));
        }
        commonFlexAdapter.notifyDataSetChanged();
    }

    @Override public void onNoMore() {

    }

    @Override public void clearDatas() {

    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onSelectItem(int position) {
        isShow = false;
        switch (position) {
            case 0:
                showLoading();
                start = "7";
                end = "30";
                absenceListPresenter.refreshData(start, end);
                sixTy = "";
                toggleViewState();
                break;
            case 1:
                showLoading();
                start = "30";
                end = "60";
                absenceListPresenter.refreshData(start, end);
                sixTy = "";
                toggleViewState();
                break;
            case 2:
                showLoading();
                absenceListPresenter.refreshData("60", "-1");
                sixTy = getString(R.string.absence_sixty);
                toggleViewState();
                break;
            case 3:
                toggleViewState();
                getChildFragmentManager().beginTransaction()
                    .replace(R.id.frag_absence_setting, filterCustomFragment)
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .addToBackStack(null)
                    .commit();
                break;
        }
    }

    @Override public void onSettingData(String start, String end) {
        showLoading();
        absenceListPresenter.refreshData(start, end);
        textAbsence.setText(String.format(Locale.CHINA, getString(R.string.absence_condition), start, end));
        this.start = start;
        this.end = end;
        sixTy = "";
        getChildFragmentManager().popBackStackImmediate();
        isShow = false;
        toggleViewState();
    }

    @Override public void onBack() {
        getChildFragmentManager().popBackStackImmediate();
        isShow = true;
        toggleViewState();
    }

    @Override public boolean onItemClick(int position) {
        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
        if (absenceListPresenter.handleStudentBean(position) != null) {
            it.putExtra("student", absenceListPresenter.handleStudentBean(position));
        } else {
            it.putExtra("student", new StudentBean());
        }
        startActivity(it);
        return false;
    }

    @Override public void noMoreLoad(int i) {
        ToastUtils.show("没有更多数据");
    }

    @Override public void onLoadMore(int i, int i1) {
        absenceListPresenter.queryAbsenceList(start, end);
    }
}
