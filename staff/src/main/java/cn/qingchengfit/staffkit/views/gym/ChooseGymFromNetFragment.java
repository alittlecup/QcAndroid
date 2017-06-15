package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.model.responese.Shops;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.adapter.MutiImageTwoTextAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/4/28 2016.
 */
public class ChooseGymFromNetFragment extends BaseDialogFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @Inject RestRepository restRepository;
    private List<ImageTwoTextBean> mDatas = new ArrayList<>();
    private MutiImageTwoTextAdapter mGymsAdapter;
    private List<Shop> mShops;
    //    public static AdapterView.OnItemClickListener listener;
    private List<String> mIds;
    private boolean isSimple;

    public static void start(Fragment fragment, boolean simple, ArrayList<String> chooseIds, int requestcode) {
        ChooseGymFromNetFragment f = newInstance(simple, chooseIds);
        f.setTargetFragment(fragment, requestcode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ChooseGymFromNetFragment newInstance(boolean simple, ArrayList<String> chooseIds) {

        Bundle args = new Bundle();
        args.putBoolean("is", simple);
        args.putStringArrayList("ids", chooseIds);
        ChooseGymFromNetFragment fragment = new ChooseGymFromNetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) {
            isSimple = getArguments().getBoolean("is");
            mIds = getArguments().getStringArrayList("ids");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooose_gyms, container, false);
        unbinder = ButterKnife.bind(this, view);

        toolbarTitile.setText(getString(R.string.choose_gym));
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        if (!isSimple) {
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    ArrayList<Shop> pos = new ArrayList<>();
                    for (int i = 0; i < mDatas.size(); i++) {
                        ImageTwoTextBean b = mDatas.get(i);
                        if (b.showRight) {
                            pos.add(mShops.get(i));
                        }
                    }
                    Intent result = new Intent();
                    result.putParcelableArrayListExtra(IntentUtils.RESULT, pos);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
                    dismiss();
                    return true;
                }
            });
        }
        mGymsAdapter = new MutiImageTwoTextAdapter(mDatas);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(mGymsAdapter);
        mGymsAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (!mDatas.get(pos).hiden) {
                    if (isSimple) {

                        if (mShops != null && mShops.size() > pos) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                                IntentUtils.instancePacecle(mShops.get(pos)));
                            RxRegiste(rx.Observable.just("")
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .delay(500, TimeUnit.MILLISECONDS)
                                .subscribe(new Action1<String>() {
                                    @Override public void call(String s) {
                                        dismiss();
                                    }
                                }));
                        }
                    } else {
                        mDatas.get(pos).showRight = !mDatas.get(pos).showRight;
                        mGymsAdapter.notifyItemChanged(pos);
                    }
                }
            }
        });
        RxRegiste(restRepository.getGet_api()
            .qcGetBrandShops(App.staffId, PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<Shops>>() {
                @Override public void call(QcResponseData<Shops> qcResponseBrandShops) {
                    if (ResponseConstant.checkSuccess(qcResponseBrandShops)) {
                        mShops = qcResponseBrandShops.data.shops;
                        mDatas.clear();
                        for (Shop service : mShops) {
                            ImageTwoTextBean baen =
                                new ImageTwoTextBean(service.phone, service.name + " | " + qcResponseBrandShops.data.brand.getName(), "");
                            //                                            baen.showRight = true;
                            baen.hiden = !service.has_permission;
                            if (mIds != null && mIds.contains(service.id)) mDatas.add(baen);
                        }
                        mGymsAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
