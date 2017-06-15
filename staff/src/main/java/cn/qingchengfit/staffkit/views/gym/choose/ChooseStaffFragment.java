package cn.qingchengfit.staffkit.views.gym.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.AddNewCoachFragment;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * <p/>
 * Created by Paper on 16/1/29 2016.
 */
public class ChooseStaffFragment extends BaseDialogFragment implements ChooseStaffView {

    @Inject ChooseStaffPresenter presenter;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.btn) Button btn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    String selectPhone;
    private int mType = 0;
    private ImageTwoTextAdapter adapter;
    private List<ImageTwoTextBean> datas = new ArrayList<>();

    public static void start(Fragment fragment, int requestCode, String phone) {
        BaseDialogFragment f = newInstance(phone);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ChooseStaffFragment newInstance(String phone) {
        Bundle args = new Bundle();
        ChooseStaffFragment fragment = new ChooseStaffFragment();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        selectPhone = getArguments().getString("phone");
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        btn.setVisibility(View.GONE);
        return view;
    }

    private void initData() {

    }

    private void initView() {
        presenter.attachView(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ChooseStaffFragment.this.dismiss();
            }
        });
        toolbarTitile.setText("工作人员");
        adapter = new ImageTwoTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
                    IntentUtils.instanceStringIntent(adapter.getDatas().get(pos).text1, adapter.getDatas().get(pos).text2));
                dismiss();
            }
        });
        presenter.getStaffs();
        for (int i = 0; i < adapter.getDatas().size(); i++) {
            if (adapter.getDatas().get(i).text2.equalsIgnoreCase(selectPhone)) {
                adapter.getDatas().get(i).showRight = true;
                adapter.notifyItemChanged(i);
            }
        }
    }

    @OnClick(R.id.btn) public void onClickAdd() {
        AddNewCoachFragment.start(getTargetFragment(), 2);
        dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onGetStaffs(List<ImageTwoTextBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.freshData(datas);
    }
}
