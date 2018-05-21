package cn.qingchengfit.saasbase.cards.views;

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
import android.view.Window;
import android.widget.TextView;


import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.ImageTwoTextBean;
import cn.qingchengfit.saasbase.cards.network.response.Shops;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.network.model.Shop;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
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
 * <p/>品牌下所有场馆的过滤
 * <p/>
 * Created by Paper on 16/4/28 2016.
 */
@Leaf(module = "card", path = "/cardtpl/add/gyms")
public class MutiChooseGymFragment extends BaseDialogFragment {

    public static final int FEATURE_ALLOTSALE = 1;

	Toolbar toolbar;
	TextView toolbarTitile;
	RecyclerView recycleview;
    @Inject QcRestRepository restRepository;
    @Inject IPermissionModel serPermisAction;
    @Inject ICardModel cardModel;
    @Inject LoginStatus loginStatus;
    private List<ImageTwoTextBean> mDatas = new ArrayList<>();
    private MutiImageTwoTextAdapter mGymsAdapter;
    private List<Shop> mShops;
    //    public static AdapterView.OnItemClickListener listener;
    private List<String> mIds;
    private String permission;
    private boolean isSimple;
    private int feature = 0;

    public static void start(Fragment fragment, boolean simple, ArrayList<String> chooseIds, int requestcode) {
        MutiChooseGymFragment f = newInstance(simple, chooseIds);
        f.setTargetFragment(fragment, requestcode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static void start(Fragment fragment, boolean simple, ArrayList<String> chooseIds, String permis, int requestcode) {
        MutiChooseGymFragment f = newInstance(simple, chooseIds, permis);
        f.setTargetFragment(fragment, requestcode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static void start(Fragment fragment, boolean simple, ArrayList<String> chooseIds, String permis, int feature, int requestcode) {
        MutiChooseGymFragment f = newInstance(simple, chooseIds, permis, feature);
        f.setTargetFragment(fragment, requestcode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static MutiChooseGymFragment newInstance(boolean simple, ArrayList<String> chooseIds) {

        Bundle args = new Bundle();
        args.putBoolean("is", simple);
        args.putStringArrayList("ids", chooseIds);
        MutiChooseGymFragment fragment = new MutiChooseGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MutiChooseGymFragment newInstance(boolean simple, ArrayList<String> chooseIds, String permis) {

        Bundle args = new Bundle();
        args.putBoolean("is", simple);
        args.putStringArrayList("ids", chooseIds);
        args.putString("p", permis);
        MutiChooseGymFragment fragment = new MutiChooseGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MutiChooseGymFragment newInstance(boolean simple, ArrayList<String> chooseIds, String permis, int feature) {

        Bundle args = new Bundle();
        args.putBoolean("is", simple);
        args.putStringArrayList("ids", chooseIds);
        args.putString("p", permis);
        args.putInt("feature", feature);
        MutiChooseGymFragment fragment = new MutiChooseGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);

        if (getArguments() != null) {
            isSimple = getArguments().getBoolean("is");
            mIds = getArguments().getStringArrayList("ids");
            permission = getArguments().getString("p");
            feature = getArguments().getInt("feature");
        }
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooose_gyms, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recycleview = (RecyclerView) view.findViewById(R.id.recycleview);

      toolbarTitile.setText("选择场馆");
        toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
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
                    if (pos.size() <= 0){
                        showAlert(getResources().getString(R.string.card_not_select_gym));
                        return false;
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
        mGymsAdapter.setListener(new MutiImageTwoTextAdapter.OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (!mDatas.get(pos).hiden) {
                    if (isSimple) {

                        if (mShops != null && mShops.size() > pos) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                                IntentUtils.instancePacecle(mShops.get(Integer.parseInt(mDatas.get(pos).tags.get("pos")))));
                            dismiss();
                            //                            RxRegiste(rx.Observable.just("")
                          //                                .onBackpressureBuffer().subscribeOn(Schedulers.newThread())
                            //                                    .observeOn(Schedulers.newThread())
                            //                                    .delay(500, TimeUnit.MILLISECONDS)
                            //                                    .subscribe(new Action1<String>() {
                            //                                        @Override
                            //                                        public void call(String s) {
                            //
                            //                                        }
                            //                                    })
                            //                            );
                        }
                    } else {
                        mDatas.get(pos).showRight = !mDatas.get(pos).showRight;
                        mGymsAdapter.notifyItemChanged(pos);
                    }
                } else {
                    ToastUtils.show("抱歉!您没有该场馆的权限");
                }
            }
        });
        RxRegiste(cardModel.qcGetBrandShops(PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<Shops>>() {
                           @Override public void call(QcDataResponse<Shops> qcResponseBrandShops) {
                               if (ResponseConstant.checkSuccess(qcResponseBrandShops)) {
                                   mShops = qcResponseBrandShops.data.shops;
                                   mDatas.clear();
                                   int i = 0;
                                   for (Shop service : mShops) {
                                       ImageTwoTextBean baen =
                                           new ImageTwoTextBean(service.photo, service.name + " | " + qcResponseBrandShops.data.brand.getName(), "");
                                       baen.tags.put("pos", i + "");
                                       List<String> shoid = new ArrayList<>();
                                       shoid.add(service.id);
                                       if (isSimple) {
                                           baen.rightIcon = 0;
                                           baen.hiden = !service.has_permission;

                                           if (!StringUtils.isEmpty(permission)) {
                                               if (permission.equalsIgnoreCase(
                                                   PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {

                                                   if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
                                                   && feature != FEATURE_ALLOTSALE) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_COSTS_CAN_WRITE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
                                                   && feature == FEATURE_ALLOTSALE) {
                                                   baen.hiden = !serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE,shoid);
                                               } else {
                                                   baen.hiden = !serPermisAction.check( permission,shoid);
                                               }
                                           }

                                           if (mIds != null && mIds.size() > 0) {
                                               if (mIds.contains(service.id)) mDatas.add(baen);
                                           } else {
                                               mDatas.add(baen);
                                           }
                                       } else {
                                           if (mIds != null && mIds.contains(service.id)) baen.showRight = true;
                                           baen.rightIcon = R.drawable.ic_checkbox_unchecked;
                                           baen.hiden = !service.has_permission;
                                           if (!StringUtils.isEmpty(permission)) {
                                               if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
                                                   && feature != FEATURE_ALLOTSALE) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                                                   if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
                                                   && feature == FEATURE_ALLOTSALE) {
                                                   if (!serPermisAction.check( PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE,shoid)) {
                                                       baen.hiden = true;
                                                   } else {
                                                       baen.hiden = false;
                                                   }
                                               } else {
                                                   baen.hiden = !serPermisAction.check( permission,shoid);
                                               }
                                           }

                                           mDatas.add(baen);
                                       }
                                       i++;
                                   }
                                   if (mDatas.size() == 1) {
                                       getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                                           IntentUtils.instancePacecle(mShops.get(Integer.parseInt(mDatas.get(0).tags.get("pos")))));
                                   }
                                   mGymsAdapter.notifyDataSetChanged();
                               } else {

                               }
                           }
                       }

                , new NetWorkThrowable()));
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
