package cn.qingchengfit.staffkit.views.gym.site;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.AddSiteEvent;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ChooseSiteAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;

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
public class MutiChooseSiteFragment extends BaseDialogFragment implements ChooseSiteView {

    @BindView(R.id.btn) Button btn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    @Inject ChooseSitePresenter presenter;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private int type = 0;//课程类型
    private ChooseSiteAdapter adapter;
    private Observable<AddSiteEvent> mObAdd;
    private List<ImageTwoTextBean> datas = new ArrayList<>();

    public static void start(Fragment fragment, int requestCode, String site, int type) {
        BaseDialogFragment f = newInstance(site, type);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static MutiChooseSiteFragment newInstance(String site, int type) {
        Bundle args = new Bundle();
        MutiChooseSiteFragment fragment = new MutiChooseSiteFragment();
        args.putString("site", site);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        type = getArguments().getInt("type", 0);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initView();
        return view;
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MutiChooseSiteFragment.this.dismiss();
            }
        });
        if (type == Configs.TYPE_PRIVATE) {
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    ArrayList<String> ret = new ArrayList<>();
                    String retStr = "";
                    for (int i = 0; i < datas.size(); i++) {
                        ImageTwoTextBean b = datas.get(i);
                        if (b.rightIcon == R.drawable.ic_radio_checked) {
                            ret.add(b.tags.get("id"));
                            retStr = TextUtils.concat(retStr, b.text1, "、").toString();
                        }
                    }
                    Intent result = new Intent();
                    result.putStringArrayListExtra("ids", ret);
                    result.putExtra("string", retStr.substring(0, retStr.length() - 1));
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
                    dismiss();
                    return true;
                }
            });
        }
        adapter = new ChooseSiteAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querySiteList(type);
            }
        });
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (type == Configs.TYPE_PRIVATE) {
                    if (datas.get(pos).rightIcon == R.drawable.ic_radio_checked) {
                        datas.get(pos).rightIcon = R.drawable.ic_radio_unchecked;
                    } else {
                        datas.get(pos).rightIcon = R.drawable.ic_radio_checked;
                    }
                    adapter.notifyItemChanged(pos);
                } else {
                    ArrayList<String> ret = new ArrayList<String>();
                    String retStr = datas.get(pos).text1;
                    ret.add(datas.get(pos).tags.get("id"));
                    Intent result = new Intent();
                    result.putStringArrayListExtra("ids", ret);
                    result.putExtra("string", retStr);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
                    dismiss();
                }
            }
        });

        //        if (getArguments().getInt("type") == Configs.INIT_TYPE_GUIDE) {//第一次引导 选择场地
        //            SystemInitBody body = (SystemInitBody) App.caches.get("init");
        //            if (body != null && body.spaces.size() > 0) {
        //                for (Space space : body.spaces) {
        //                    datas.add(new ImageTwoTextBean("", space.getName(), String.format("可容纳%s人,可用于%s", space.getCapacity(), space.is_support_private())));
        //                }
        //            } else {
        //                body.spaces.add(new Space("默认场地", "50", true, true));
        //                datas.add(new ImageTwoTextBean("", "默认场地", String.format("可容纳%d人,可用于%s", 50, "团课/私教"), false, true));
        //            }
        //            for (int i = 0; i < datas.size(); i++) {
        //                if (datas.get(i).text1.equalsIgnoreCase(getArguments().getString("site"))) {
        //                    datas.get(i).showRight = true;
        //                }
        //            }
        //            adapter.notifyDataSetChanged();
        //            toolbarTitile.setText(getString(R.string.title_choose_site));
        //        } else if (getArguments().getInt("type") == Configs.INIT_TYPE_ADD) {// 健身房详情中获取列表
        //            presenter.querySiteList();
        //            toolbarTitile.setText(getString(R.string.title_choose_site));
        //        } else if (getArguments().getInt("type") == Configs.INIT_TYPE_CHOOSE) {
        //            presenter.querySiteList();
        //            toolbarTitile.setText("场地");
        //        }
        presenter.querSiteListPermiss(type);
        toolbarTitile.setText("选择场地");

        btn.setText("添加新场地");
        mObAdd = RxBus.getBus().register(AddSiteEvent.class);
        mObAdd.subscribe(new Subscriber<AddSiteEvent>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(AddSiteEvent addSiteEvent) {
                presenter.querySiteList(type);
            }
        });
    }

    @OnClick(R.id.btn) public void onClickAdd() {
        if (gymWrapper.getCoachService() != null && !SerPermisAction.check(gymWrapper.shop_id(),
            PermissionServerUtils.STUDIO_LIST_CAN_WRITE)) {
            new MaterialDialog.Builder(getContext()).content("抱歉!您没有该权限")
                .canceledOnTouchOutside(true)
                .autoDismiss(true)
                .positiveText(R.string.common_i_konw)
                .show();
            return;
        }

        AddNewSiteFragment.start(getTargetFragment(), getTargetRequestCode(), Configs.INIT_TYPE_ADD);
        //        dismiss();
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(AddSiteEvent.class.getName(), mObAdd);
        super.onDestroyView();
    }

    @Override public void onData(List<ImageTwoTextBean> datas) {
        recyclerview.stopLoading();
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
        if (datas.size() > 0) {

        } else {

        }
    }
}
