package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.JacketManageAdapter;
import com.qingchengfit.fitcoach.items.JackManageItem;
import com.qingchengfit.fitcoach.items.JackTitleItem;
import com.qingchengfit.fitcoach.items.JacketAddItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/7/20.
 */
public class JacketManagerFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, JacketManagerPresenter.JacketManagerView {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.custom_switch) ExpandedLayout customSwitch;
    @BindView(R.id.costum_jacket_hint) TextView costumJacketHint;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;
    @Inject JacketManagerPresenter mPresenter;
    private ArrayList<AbstractFlexibleItem> mDatas = new ArrayList<>();
    private JacketManageAdapter mAdapter;
    private String courseid;
    private ArrayList<String> stringList = new ArrayList<>();

    public static JacketManagerFragment newInstance(List<String> jacket, String courseid, boolean open) {

        Bundle args = new Bundle();
        args.putStringArrayList("jacket", (ArrayList<String>) jacket);
        args.putString("c", courseid);
        args.putBoolean("s", open);
        JacketManagerFragment fragment = new JacketManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stringList.clear();
            stringList = getArguments().getStringArrayList("jacket");
            courseid = getArguments().getString("c");
        }
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jacket_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);
            delegatePresenter(mPresenter, this);
        }
        //        mCallbackActivity.setToolbar("编辑封面照片", false, null, R.menu.menu_compelete, new Toolbar.OnMenuItemClickListener() {
        //            @Override
        //            public boolean onMenuItemClick(MenuItem item) {
        //                mPresenter.completeJacket(courseid, item2Str(), customSwitch.isExpanded());
        //                return true;
        //            }
        //        });

        toolbarTitle.setText("编辑课程封面照片");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.inflateMenu(R.menu.menu_complete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                mPresenter.completeJacket(courseid, item2Str(), customSwitch.isExpanded());
                return true;
            }
        });
        mAdapter = new JacketManageAdapter(mDatas, this);
        mAdapter.addItem(0, new JackTitleItem());
        mAdapter.addItem(1, new JacketAddItem());
      mRecyclerView.setAdapter(mAdapter);
        mAdapter.setHandleDragEnabled(true);
        SmoothScrollLinearLayoutManager manager = new SmoothScrollLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        customSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    costumJacketHint.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    costumJacketHint.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        onSwitch(getArguments().getBoolean("s", false));
        //        setJacket(stringList);
        mPresenter.queryJacket(App.coachid + "", courseid);

        return view;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //        inflater.inflate(R.menu.menu_compelete, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_complete) mPresenter.completeJacket(courseid, item2Str(), customSwitch.isExpanded());
        return true;
    }

    /**
     * 添加照片
     */
    public void onAddImg() {
        if (mAdapter.getItemCount() >= 7) {
            ToastUtils.show("最多可添加五张封面");
            return;
        }
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    showLoading();
                    RxRegiste(UpYunClient.rxUpLoad("/jacket", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {

                            //                            mDatas.add(new JackManageItem(s));
                            //                            mAdapter.notifyDataSetChanged();
                            mAdapter.addItem(mAdapter.getItemCount() - 1, new JackManageItem(s));
                            hideLoading();
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            hideLoading();
                        }
                    }));
                } else {

                }
            }
        });
        dialog.show(getFragmentManager(), "");
    }

    @Override public String getFragmentName() {
        return JacketManagerFragment.class.getName();
    }

    @Override public void onPause() {
        super.onPause();
    }

    private List<JackManageItem> str2Item(@NonNull List<String> strings) {
        List<JackManageItem> items = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            items.add(new JackManageItem(strings.get(i)));
        }
        return items;
    }

    private List<String> item2Str() {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i) instanceof JackManageItem) ret.add(((JackManageItem) mDatas.get(i)).getImg());
        }
        return ret;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof JacketAddItem) {
            onAddImg();
        }
        return false;
    }

    @Override public void setJacket(List<String> jackets) {
        for (int i = 0; i < jackets.size(); i++) {
            mAdapter.addItem(mAdapter.getItemCount() - 1, new JackManageItem(jackets.get(i)));
        }
    }

    @Override public void onSuccess() {
        getActivity().onBackPressed();
    }

    @Override public void onFaied(String s) {
        ToastUtils.show(s);
    }

    @Override public void onSwitch(boolean s) {
        customSwitch.setExpanded(s);
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
