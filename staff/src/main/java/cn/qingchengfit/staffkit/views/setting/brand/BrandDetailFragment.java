package cn.qingchengfit.staffkit.views.setting.brand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.model.responese.Shops;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.FreshGymListEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.BrandGymsAdapter;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 16/7/14.
 */
public class BrandDetailFragment extends BaseFragment {

    @BindView(R.id.bg) ImageView bg;
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.brand) TextView brand;
    @BindView(R.id.contact) TextView contact;
    @BindView(R.id.create_time) TextView createTime;
    @BindView(R.id.myhome_appBar) AppBarLayout myhomeAppBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    BrandGymsAdapter adapter;
    @Inject RestRepository restRepository;
    @BindView(R.id.no_brand_shop) LinearLayout noBrandShop;
    private List<Shop> datas = new ArrayList<>();
    private String mBrandId;
    private Subscription sp;

    public static BrandDetailFragment newInstance(Brand brand) {
        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        BrandDetailFragment fragment = new BrandDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            final Brand brand = getArguments().getParcelable("brand");
            if (brand != null) {
                mCallbackActivity.setToolbar(brand.getName(), false, null, R.menu.menu_edit, new Toolbar.OnMenuItemClickListener() {
                    @Override public boolean onMenuItemClick(MenuItem item) {
                        getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), BrandEditFragment.newInstance(brand))
                            .addToBackStack(getFragmentName())
                            .commit();

                        return true;
                    }
                });

                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(brand.getPhoto()))
                    .asBitmap()
                    .placeholder(R.drawable.ic_default_header)
                    .into(new CircleImgWrapper(img, getContext()));
                Glide.with(getContext()).load(PhotoUtils.getGauss(brand.getPhoto())).placeholder(R.drawable.bg_brand).into(bg);
                this.name.setText(brand.getName());
                if (TextUtils.isEmpty(brand.getCreated_at())) {
                    this.createTime.setText(getString(R.string.create_time));
                } else {
                    this.createTime.setText(getString(R.string.create_time).concat(
                        DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(brand.getCreated_at()))));
                }
                String creator = "", phone = "";
                if (brand.getCreated_by() != null) {
                    creator = brand.getCreated_by().getUsername();
                    phone = brand.getCreated_by().getPhone();
                }
                this.brand.setText(getString(R.string.choose_brand_content, brand.getCname(), creator));

                this.contact.setText(
                    getString(R.string.creator_colon).concat(creator).concat("    ").concat(getString(R.string.phone_colon)).concat(phone));
                adapter = new BrandGymsAdapter(datas);
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerview.setAdapter(adapter);

                adapter.setListener(new OnRecycleItemClickListener() {
                    @Override public void onItemClick(View v, final int pos) {
                        if (adapter.getItemViewType(pos) == 0) {
                            new MaterialDialog.Builder(getContext()).content("确认删除该场馆？")
                                .autoDismiss(true)
                                .canceledOnTouchOutside(true)
                                .negativeText(R.string.pickerview_cancel)
                                .positiveText(R.string.common_comfirm)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (pos - 1 < datas.size()) {
                                            RxRegiste(restRepository.getPost_api()
                                                .qcDelGym(datas.get(pos - 1).id)
                                                .observeOn(Schedulers.io())
                                                .onBackpressureBuffer()
                                                .subscribeOn(Schedulers.io())
                                                .flatMap(new Func1<QcResponse, Observable<Integer>>() {
                                                    @Override public Observable<Integer> call(QcResponse qcResponse) {
                                                        if (ResponseConstant.checkSuccess(qcResponse)) {
                                                            return QCDbManager.getAllCoachService()
                                                                .observeOn(Schedulers.io())
                                                                .onBackpressureBuffer()
                                                                .subscribeOn(Schedulers.io())
                                                                .flatMap(new Func1<List<CoachService>, Observable<Integer>>() {
                                                                    @Override
                                                                    public Observable<Integer> call(List<CoachService> coachServices) {
                                                                        if (coachServices.size() > 1) {
                                                                            RxBus.getBus().post(new FreshGymListEvent());
                                                                        }
                                                                        return Observable.just(coachServices.size() == 1 ? 2 : 1)
                                                                            .observeOn(AndroidSchedulers.mainThread())
                                                                            .onBackpressureBuffer()
                                                                            .subscribeOn(
                                                                                Schedulers.io());
                                                                    }
                                                                });
                                                        } else {
                                                            return Observable.just(-1);
                                                        }
                                                    }
                                                })
                                                .subscribe(new Action1<Integer>() {
                                                    @Override public void call(Integer size) {
                                                        if (size < 0) {
                                                            ToastUtils.show("删除错误");
                                                        } else if (size == 1) {//正常情况 多品牌 或者多场馆 保持当前页面
                                                            freshData();
                                                        } else { //单场馆情况
                                                            BusinessUtils.reOpenApp(getActivity());
                                                        }
                                                    }
                                                }, new NetWorkThrowable()));
                                        }
                                    }
                                })
                                .show();
                        } else if (adapter.getItemViewType(pos) == 1) {
                            SystemInitBody body;
                            if (TextUtils.isEmpty(PreferenceUtils.getPrefString(getContext(), "init", ""))) {
                                body = new SystemInitBody();
                            } else {
                                body = new Gson().fromJson(PreferenceUtils.getPrefString(getContext(), "init", ""), SystemInitBody.class);
                            }
                            App.caches.put("init", body);
                            Intent toGuide = new Intent(getActivity(), GuideActivity.class);
                            toGuide.putExtra(Configs.EXTRA_BRAND, brand);
                            toGuide.putExtra("isAdd", true);
                            startActivity(toGuide);
                        }
                    }
                });
                mBrandId = brand.getId();
                freshData();
            }
        }

        return view;
    }

    public void freshData() {
        if (mBrandId == null || TextUtils.isEmpty(mBrandId)) return;
        sp = restRepository.getGet_api()
            .qcGetBrandAllShops(App.staffId, mBrandId)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<Shops>>() {
                @Override public void call(QcResponseData<Shops> qcResponseBrandShops) {
                    if (ResponseConstant.checkSuccess(qcResponseBrandShops)) {
                        datas.clear();
                        datas.addAll(qcResponseBrandShops.data.shops);
                        adapter.notifyDataSetChanged();
                        noBrandShop.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
                    } else {
                        ToastUtils.show(qcResponseBrandShops.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    @Override public String getFragmentName() {
        return BrandDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }
}
