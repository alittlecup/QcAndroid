package cn.qingchengfit.staffkit.train.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.train.item.SignUpFormPersonalItem;
import cn.qingchengfit.staffkit.train.model.SignPersonalBean;
import cn.qingchengfit.staffkit.train.model.SignRecord;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.presenter.SignUpPersonalPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CommonNoDataItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/21.
 */

public class SignUpFormPersonalFragment extends BaseFragment
    implements TitleFragment, SignUpView<SignRecord>, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recycle_sign_personal) RecyclerView recyclerView;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.img_search_clear) ImageView imgSearchClear;
    @BindView(R.id.text_sign_number) TextView textSignNumber;
    @BindView(R.id.text_sign_fee) TextView textSignFee;
    @BindView(R.id.rl_personal_search) RelativeLayout rlPersonalSearch;
    @BindView(R.id.ll_personal_info) LinearLayout llPersonalInfo;
    @Inject SignUpPersonalPresenter personalPresenter;
    @Inject TrainIds trainIds;
    private CommonFlexAdapter flexAdapter;
    private List<SignUpFormPersonalItem> dataList = new ArrayList<>();
    private String keyword;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_personal, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(personalPresenter, this);

        initSearch();

        personalPresenter.queryData(keyword, trainIds.getGymId(), trainIds.getCompetitionId());
        flexAdapter = new CommonFlexAdapter(dataList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration =
            new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, MeasureUtils.dpToPx(78f, getContext().getResources()));
        dividerItemDecoration.setType(DividerItemDecoration.LEFT);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(flexAdapter);
        return view;
    }

    private void initSearch() {
        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        RxTextView.textChanges(etSearch).subscribe(new Action1<CharSequence>() {
            @Override public void call(CharSequence charSequence) {
                keyword = charSequence.toString();
                personalPresenter.initPage();
                personalPresenter.queryData(keyword, trainIds.getGymId(), trainIds.getCompetitionId());
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unattachView();
        personalPresenter.unattachView();
    }

    @Override public String getTitle() {
        return "个人";
    }

    @Override public String getFragmentName() {
        return SignUpFormPersonalFragment.class.getName();
    }

    @Override public void onGetSignUpDataSuccess(SignRecord data) {
        setView(data);
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {

    }

    @Override public void onDelSuccess() {

    }

    private void setView(SignRecord data) {
        dataList.clear();
        if (data.users.size() == 0) {
            if (TextUtils.isEmpty(keyword)) {
                rlPersonalSearch.setVisibility(View.GONE);
                llPersonalInfo.setVisibility(View.GONE);
            } else {
                rlPersonalSearch.setVisibility(View.VISIBLE);
                llPersonalInfo.setVisibility(View.VISIBLE);
            }
            flexAdapter.addItem(new CommonNoDataItem(R.drawable.no_statement, "还没有人报名", ""));
        } else {
            rlPersonalSearch.setVisibility(View.VISIBLE);
            llPersonalInfo.setVisibility(View.VISIBLE);
            textSignNumber.setText(getString(R.string.sign_up_personal_member, data.users.size()));
            textSignFee.setText(getString(R.string.sign_up_whole_cost, StringUtils.getNumString(data.totalPrice)));
            for (SignPersonalBean signPersonalBean : data.users) {
                SignUpFormPersonalItem signUpFormPersonalItem = new SignUpFormPersonalItem(getContext(), signPersonalBean);
                dataList.add(signUpFormPersonalItem);
            }
        }
        flexAdapter.notifyDataSetChanged();
    }

    @Override public boolean onItemClick(int position) {

        if (dataList.get(position) != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sign_up_detail_frag,
                    SignUpPersonalDetailFragmentBuilder.newSignUpPersonalDetailFragment(dataList.get(position).getData().getOrderId()))
                .addToBackStack(null)
                .commit();
        }

        return true;
    }
}
