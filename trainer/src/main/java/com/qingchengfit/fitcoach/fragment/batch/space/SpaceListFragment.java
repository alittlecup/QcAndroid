package com.qingchengfit.fitcoach.fragment.batch.space;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.QcResponseSpaces;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.items.SpaceItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
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
public class SpaceListFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    CommonFlexAdapter mCommonFlexAdapter;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;

    public static SpaceListFragment newInstance(int courseType) {
        Bundle args = new Bundle();
        args.putInt("type", courseType);
        SpaceListFragment fragment = new SpaceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_space_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("选择场地");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (getArguments().getInt("type", Configs.TYPE_GROUP) == Configs.TYPE_PRIVATE) {
            toolbar.inflateMenu(R.menu.menu_complete);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    Intent ret = new Intent();
                    ArrayList<Space> spaces = new ArrayList<>();
                    List<Integer> poses = mCommonFlexAdapter.getSelectedPositions();
                    if (poses == null || poses.size() == 0) {
                        ToastUtils.show(getString(R.string.err_at_least_one_space));
                        return true;
                    }
                    for (int i = 0; i < poses.size(); i++) {
                        spaces.add(((SpaceItem) mCommonFlexAdapter.getItem(poses.get(i))).mSpace);
                    }
                    ret.putParcelableArrayListExtra("spaces", spaces);
                    getActivity().setResult(Activity.RESULT_OK, ret);
                    getActivity().finish();
                    return true;
                }
            });
        }

        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mCommonFlexAdapter = new CommonFlexAdapter(mData, this);
        mCommonFlexAdapter.setMode(SelectableAdapter.MODE_MULTI);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(mCommonFlexAdapter);
        if (getActivity() instanceof FragActivity) {
            RxRegiste(
                QcCloudClient.getApi().getApi.qcGetSpace(App.coachid + "", ((FragActivity) getActivity()).getCoachService().getId() + "",
                    ((FragActivity) getActivity()).getCoachService().getModel())
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponseSpaces>() {
                        @Override public void call(QcResponseSpaces qcResponse) {
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                mData.clear();
                                for (int i = 0; i < qcResponse.data.spaces.size(); i++) {
                                    mData.add(new SpaceItem(getContext(), qcResponse.data.spaces.get(i),
                                        getArguments().getInt("type", Configs.TYPE_GROUP)));
                                }
                                mCommonFlexAdapter.notifyDataSetChanged();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                        }
                    }));
        }
        return view;
    }

    @Override public String getFragmentName() {
        return SpaceListFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        if (mCommonFlexAdapter.getItem(position) instanceof SpaceItem) {
            if (getArguments().getInt("type", Configs.TYPE_GROUP) == Configs.TYPE_GROUP) {
                Intent ret = new Intent();
                ArrayList<Space> spaces = new ArrayList<>();
                spaces.add(((SpaceItem) mCommonFlexAdapter.getItem(position)).mSpace);
                ret.putParcelableArrayListExtra("spaces", spaces);
                getActivity().setResult(Activity.RESULT_OK, ret);
                getActivity().finish();
            } else {
                mCommonFlexAdapter.toggleSelection(position);
                mCommonFlexAdapter.notifyItemChanged(position);
            }
        }
        return true;
    }
}
