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
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.event.RefreshListEvent;
import cn.qingchengfit.staffkit.train.item.SignUpGroupItem;
import cn.qingchengfit.staffkit.train.model.GroupListBean;
import cn.qingchengfit.staffkit.train.model.GroupListResponse;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupListPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/21.
 */

public class SignUpFormGroupFragment extends BaseFragment
    implements TitleFragment, SignUpView<GroupListResponse>, FlexibleAdapter.OnItemClickListener {

	EditText etSearch;
	TextView textSignNumber;
	TextView tvCreateGroup;
	RecyclerView recyclerSignGroup;
    @Inject SignUpGroupListPresenter groupListPresenter;

    @Inject TrainIds trainIds;
	RelativeLayout rlGroupSearch;
	View divider;
	View dividerTop;

    private CommonFlexAdapter flexAdapter;
    private List<SignUpGroupItem> signUpGroupItems = new ArrayList<>();
    private Observable<RefreshListEvent> subscription;
    private String keyword;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_group_home, container, false);
      etSearch = (EditText) view.findViewById(R.id.et_search);
      textSignNumber = (TextView) view.findViewById(R.id.text_sign_number);
      tvCreateGroup = (TextView) view.findViewById(R.id.tv_create_group);
      recyclerSignGroup = (RecyclerView) view.findViewById(R.id.recycle_sign_group);
      rlGroupSearch = (RelativeLayout) view.findViewById(R.id.rl_group_search);
      divider = (View) view.findViewById(R.id.divider);
      dividerTop = (View) view.findViewById(R.id.divider_top);
      view.findViewById(R.id.tv_create_group).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onCreateNewGroup();
        }
      });

      initBus();
        delegatePresenter(groupListPresenter, this);

        getData();

        flexAdapter = new CommonFlexAdapter(signUpGroupItems, this);
        recyclerSignGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSignGroup.setAdapter(flexAdapter);
        initSearch();
        return view;
    }

    private void initBus() {
        subscription = RxBus.getBus().register(RefreshListEvent.class);
        subscription.subscribe(new Action1<RefreshListEvent>() {
            @Override public void call(RefreshListEvent refreshListEvent) {
                getData();
            }
        });
    }

    private void getData() {
        groupListPresenter.initPage();
        groupListPresenter.queryGroup(keyword, trainIds.getGymId(), trainIds.getCompetitionId());
    }

    @Override public void onResume() {
        super.onResume();
    }

    private void initSearch() {
        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        RxTextView.textChanges(etSearch).subscribe(new Action1<CharSequence>() {
            @Override public void call(CharSequence charSequence) {
                keyword = charSequence.toString();
                getData();
            }
        });
    }

 void onCreateNewGroup() {
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(mCallbackActivity.getFragId(), new CreateGroupFragment())
            .addToBackStack(null)
            .commit();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        groupListPresenter.unattachView();
        RxBus.getBus().unregister(RefreshListEvent.class.getName(), subscription);
    }

    @Override public String getTitle() {
        return "分组";
    }

    @Override public String getFragmentName() {
        return SignUpFormGroupFragment.class.getName();
    }

    @Override public void onGetSignUpDataSuccess(GroupListResponse data) {
        signUpGroupItems.clear();
        if (data.datas.size() == 0) {
            if (TextUtils.isEmpty(keyword)) {
                recyclerSignGroup.getLayoutParams().height = RecyclerView.LayoutParams.MATCH_PARENT;
                textSignNumber.setVisibility(View.GONE);
                tvCreateGroup.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);
                rlGroupSearch.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                dividerTop.setVisibility(View.GONE);
            } else {
                recyclerSignGroup.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                textSignNumber.setVisibility(View.VISIBLE);
                tvCreateGroup.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.VISIBLE);
                rlGroupSearch.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
                dividerTop.setVisibility(View.VISIBLE);
            }
            flexAdapter.addItem(
                new CommonNoDataItem(R.drawable.no_statement, "还没有任何分组", "", "新建分组", new CommonNoDataItem.OnEmptyBtnClickListener() {
                    @Override public void onEmptyClickListener(View v) {
                        getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(mCallbackActivity.getFragId(), new CreateGroupFragment())
                            .addToBackStack(null)
                            .commit();
                    }
                }));
        } else {
            textSignNumber.setVisibility(View.VISIBLE);
            tvCreateGroup.setVisibility(View.VISIBLE);
            etSearch.setVisibility(View.VISIBLE);
            textSignNumber.setText(getString(R.string.sign_up_group_all, data.datas.size()));
            for (GroupListBean groupListBean : data.datas) {
                SignUpGroupItem signUpGroupItem = new SignUpGroupItem(groupListBean);
                signUpGroupItems.add(signUpGroupItem);
            }
        }
        flexAdapter.notifyDataSetChanged();
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {
    }

    @Override public void onDelSuccess() {

    }

    @Override public boolean onItemClick(int position) {
        if (signUpGroupItems.get(position) != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sign_up_detail_frag, SignUpGroupDetailFragmentBuilder.newSignUpGroupDetailFragment(
                    String.valueOf(signUpGroupItems.get(position).getData().id)))
                .addToBackStack(null)
                .commit();
        }
        return false;
    }
}
