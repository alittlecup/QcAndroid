//package cn.qingchengfit.staffkit.views.gym;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.staffkit.App;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.constant.Configs;
//import cn.qingchengfit.staffkit.rest.RestRepository;
//import cn.qingchengfit.model.responese.Sellers;
//import cn.qingchengfit.utils.IntentUtils;
//import cn.qingchengfit.utils.PreferenceUtils;
//import cn.qingchengfit.staffkit.views.BaseDialogFragment;
//import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextAdapter;
//import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
//import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
//import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 16/1/29 2016.
// */
//public class ChooseSaleFragment extends BaseDialogFragment {
//
//
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;
//    @BindView(R.id.btn)
//    Button btn;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitile;
//    String selectPhone;
////    @BindView(R.id.item_gym_header)
////    ImageView itemGymHeader;
////    @BindView(R.id.item_gym_name)
////    TextView itemGymName;
////    @BindView(R.id.item_is_personal)
////    TextView itemIsPersonal;
////    @BindView(R.id.qc_identify)
////    ImageView qcIdentify;
////    @BindView(R.id.item_gym_brand)
////    TextView itemGymBrand;
////    @BindView(R.id.item_gym_phonenum)
////    TextView itemGymPhonenum;
////    @BindView(R.id.item_right)
////    ImageView itemRight;
//    private int mType = 0;
//
//    @Inject
//    RestRepository restRepository;
//
//    private String shopid;
//    private Subscription sp;
//
//
//    public static void start(Fragment fragment, int requestCode, String shopid, int type) {
//        BaseDialogFragment f = newInstance(shopid, type);
//        f.setTargetFragment(fragment, requestCode);
//        f.show(fragment.getFragmentManager(), "");
//    }
//
//    public static ChooseSaleFragment newInstance(String phone, int type) {
//        Bundle args = new Bundle();
//        args.putInt("type", type);
//        ChooseSaleFragment fragment = new ChooseSaleFragment();
//        args.putString("shopid", phone);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    private List<ImageTwoTextBean> datas = new ArrayList<>();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
//        shopid = getArguments().getString("shopid");
//        mType = getArguments().getInt("type", 0);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        initDI();
//        initView();
//        initData();
//        return view;
//    }
//
//    private void initData() {
////        Glide.with(getActivity()).load(R.drawable.ic_default_head_nogender)
////                .asBitmap()
////                .into(new CircleImgWrapper(itemGymHeader,getActivity()));
////        itemGymName.setText("无销售");
////        itemGymBrand.setVisibility(View.GONE);
////        itemGymPhonenum.setVisibility(View.GONE);
//
//
//    }
////    @OnClick(R.id.no_sale)
////    public void onNoSale(){
////        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK, IntentUtils.instanceStringsIntent(
////                "无销售"
////                ,"-1"
////                ,""
////        ));
////        dismiss();
////    }
//
//    private void initDI() {
//        ((App) (getActivity().getApplication())).getAppCompoent().inject(this);
//    }
//
//    private void initView() {
//        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChooseSaleFragment.this.dismiss();
//            }
//        });
//        toolbarTitile.setText(getString(R.string.title_choose_coach));
//        final ImageTwoTextAdapter adapter = new ImageTwoTextAdapter(datas);
//        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        recyclerview.setAdapter(adapter);
//        adapter.setListener(new OnRecycleItemClickListener() {
//            @Override
//            public void onItemClick(View v, int pos) {
////                if (mType == Configs.INIT_TYPE_GUIDE)
////                    getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK, IntentUtils.instanceStringIntent(adapter.getDatas().get(pos).text1, adapter.getDatas().get(pos).text2));
////                else
//                getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK, IntentUtils.instanceStringsIntent(adapter.getDatas().get(pos).text1
//                        , adapter.getDatas().get(pos).tags.get("id")
//                        , adapter.getDatas().get(pos).imgUrl
//                ));
//                dismiss();
//            }
//        });
//        for (int i = 0; i < adapter.getDatas().size(); i++) {
//            if (adapter.getDatas().get(i).text2.equalsIgnoreCase(selectPhone)) {
//                adapter.getDatas().get(i).showRight = true;
//                adapter.notifyItemChanged(i);
//            }
//        }
//
//        sp = restRepository.getGet_api().qcGetSalers(App.staffId, PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""), shopid, null, null)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Action1<Sellers>() {
//                    @Override
//                    public void call(Sellers qcResponseSalers) {
//                        ArrayList<ImageTwoTextBean> d = new ArrayList<>();
//                        for (Sellers.Saler saler : qcResponseSalers.data.users) {
//                            ImageTwoTextBean b = new ImageTwoTextBean(saler.avatar, saler.username, "");
//                            b.tags.put("id", saler.id);
//                            d.add(b);
//                        }
//                        datas.clear();
//                        datas.addAll(d);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//
//    }
//
//
//    @OnClick(R.id.btn)
//    public void onClickAdd() {
//        dismiss();
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        if (sp != null)
//            sp.unsubscribe();
//        super.onDestroyView();
//
//    }
//}
