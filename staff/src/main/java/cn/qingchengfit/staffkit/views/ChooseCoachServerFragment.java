//package cn.qingchengfit.staffkit.views;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.model.base.CoachService;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
//import cn.qingchengfit.saasbase.permission.SerPermisAction;
//import cn.qingchengfit.staffkit.views.abstractflexibleitem.CoachServiceChooseItem;
//import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
//import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
//import cn.qingchengfit.utils.ToastUtils;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 16/8/15.
// *
// *
// * 在指定场馆中选择
// */
//
//public class ChooseCoachServerFragment extends BaseDialogFragment implements FlexibleAdapter.OnItemClickListener {
//
//    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R.id.down) ImageView down;
//    @BindView(R.id.titile_layout) LinearLayout titileLayout;
//    @BindView(R.id.searchview_et) EditText searchviewEt;
//    @BindView(R.id.searchview_clear) ImageView searchviewClear;
//    @BindView(R.id.searchview_cancle) Button searchviewCancle;
//    @BindView(R.id.searchview) LinearLayout searchview;
//    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
//    @BindView(R.id.recycleview) RecyclerView recycleview;
//    private List<CoachService> mGyms;
//    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();
//    private CommonFlexAdapter adapter;
//
//    public static ChooseCoachServerFragment newInstance(List<CoachService> coaches) {
//
//        Bundle args = new Bundle();
//        args.putParcelableArrayList("c", (ArrayList<CoachService>) coaches);
//        ChooseCoachServerFragment fragment = new ChooseCoachServerFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
//        if (getArguments() != null) mGyms = getArguments().getParcelableArrayList("c");
//    }
//
//    @Nullable @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_chooose_gyms, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View view) {
//                dismiss();
//            }
//        });
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.menu_comfirm);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override public boolean onMenuItemClick(MenuItem item) {
//                ArrayList<CoachService> ret = new ArrayList<CoachService>();
//                for (int i = 0; i < mDatas.size(); i++) {
//                    if (mDatas.get(i) instanceof CoachServiceChooseItem && adapter.isSelected(i)) {
//                        ret.add(((CoachServiceChooseItem) mDatas.get(i)).getCoachService());
//                    }
//                }
//                if (getTargetFragment() != null) {
//                    Intent result = new Intent();
//                    result.putExtra("gym", ret);
//                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
//                }
//                return true;
//            }
//        });
//        toolbarTitile.setText(getString(R.string.choose_gym));
//        if (mGyms != null) {
//            for (int i = 0; i < mGyms.size(); i++) {
//                mDatas.add(new CoachServiceChooseItem(mGyms.get(i)));
//            }
//        }
//        adapter = new CommonFlexAdapter(mDatas, this);
//        recycleview.setHasFixedSize(true);
//        recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
//        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        recycleview.setAdapter(adapter);
//
//        return view;
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override public boolean onItemClick(int position) {
//        if (mDatas.get(position) instanceof CoachServiceChooseItem) {
//            if (serPermisAction.check(((CoachServiceChooseItem) mDatas.get(position)).getCoachService().getShop_id(),
//                PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE)) {
//                adapter.toggleSelection(position);
//                adapter.notifyItemChanged(position);
//            } else {
//                ToastUtils.show(getString(R.string.alert_permission_forbid));
//            }
//        }
//        return true;
//    }
//}
