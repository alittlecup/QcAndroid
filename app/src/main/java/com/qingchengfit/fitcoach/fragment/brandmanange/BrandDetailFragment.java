package com.qingchengfit.fitcoach.fragment.brandmanange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import cn.qingchengfit.utils.DateUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.BrandManageActivity;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import com.qingchengfit.fitcoach.adapter.BrandGymsAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.base.Shop;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcReponseBrandDetailShops;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
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
 * Created by Paper on 16/7/14.
 */
public class BrandDetailFragment extends BaseFragment {

    @BindView(R.id.bg) ImageView bg;
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.brand) TextView tvBrand;
    @BindView(R.id.contact) TextView contact;
    @BindView(R.id.create_time) TextView createTime;
    @BindView(R.id.myhome_appBar) AppBarLayout myhomeAppBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    BrandGymsAdapter adapter;
    @BindView(R.id.no_brand_shop) LinearLayout noBrandShop;
    private List<Shop> datas = new ArrayList<>();
    private String mBrandId;
    private Subscription sp;

    //RestRepository restRepository;
    private Brand brand;

    public static BrandDetailFragment newInstance(Brand brand) {
        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        BrandDetailFragment fragment = new BrandDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            brand = getArguments().getParcelable("brand");
            if (brand != null) {
                if (getActivity() instanceof BrandManageActivity) {
                    ((BrandManageActivity) getActivity()).settoolbar(brand.getName(), R.menu.menu_edit,
                        new Toolbar.OnMenuItemClickListener() {
                            @Override public boolean onMenuItemClick(MenuItem item) {
                                getFragmentManager().beginTransaction()
                                    .replace(R.id.frag, BrandEditFragment.newInstance(brand))
                                    .addToBackStack(getFragmentName())
                                    .commit();

                                return true;
                            }
                        });
                }
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
                if (brand.getOwners() != null && brand.getOwners().size() > 0) {
                    creator = brand.getOwners().get(0).getUsername();
                    phone = brand.getOwners().get(0).getPhone();
                }
                this.tvBrand.setText(getString(R.string.choose_brand_content, brand.getCname(), creator));
                if (creator == null) creator = "";
                if (phone == null) phone = "";
                this.contact.setText(
                    getString(R.string.creator_colon).concat(creator).concat("    ").concat(getString(R.string.phone_colon)).concat(phone));
                adapter = new BrandGymsAdapter(datas);
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerview.setAdapter(adapter);

                adapter.setListener(new OnRecycleItemClickListener() {
                    @Override public void onItemClick(View v, int pos) {
                        if (adapter.getItemViewType(pos) == 0) {
                            new MaterialDialog.Builder(getContext()).content(R.string.contact_gm)
                                .autoDismiss(true)
                                .canceledOnTouchOutside(true)
                                .negativeText(R.string.pickerview_cancel)
                                .positiveText(R.string.comfirm)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        PhoneFuncUtils.callPhone(getContext(), getString(R.string.qingcheng_gm_phone));
                                    }
                                })
                                .show();
                        } else if (adapter.getItemViewType(pos) == 1) {
                            //getFragmentManager().beginTransaction()
                            //    .replace(R.id.frag,
                            //        new AddGymFragmentBuilder(brand.getPhoto(), brand.getName(), brand.getId()).build())
                            //    .addToBackStack(null)
                            //    .commit();
                            Intent guide = new Intent(getActivity(), GuideActivity.class);
                            guide.putExtra("brand", brand);
                            startActivity(guide);
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
        RxRegiste(QcCloudClient.getApi().getApi.qcGetBrandShops(App.coachid + "", brand.getId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcReponseBrandDetailShops>() {
                @Override public void call(QcReponseBrandDetailShops qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        datas.clear();
                        datas.addAll(qcResponse.data.shops);
                        adapter.notifyDataSetChanged();
                        noBrandShop.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }

    @Override public String getFragmentName() {
        return BrandDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }
}
