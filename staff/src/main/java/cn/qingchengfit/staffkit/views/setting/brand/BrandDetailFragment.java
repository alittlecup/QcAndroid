package cn.qingchengfit.staffkit.views.setting.brand;

import android.content.Intent;
import android.os.Bundle;
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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.responese.Shops;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.BrandGymsAdapter;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
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

  ImageView bg;
  ImageView img;
  TextView name;
  TextView brand;
  TextView contact;
  TextView createTime;
  AppBarLayout myhomeAppBar;
  RecyclerView recyclerview;
  BrandGymsAdapter adapter;
  @Inject StaffRespository restRepository;
  @Inject QcDbManager qcDbManager;
  LinearLayout noBrandShop;
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

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_brand_detail, container, false);
    bg = (ImageView) view.findViewById(R.id.bg);
    img = (ImageView) view.findViewById(R.id.img);
    name = (TextView) view.findViewById(R.id.name);
    brand = (TextView) view.findViewById(R.id.brand);
    contact = (TextView) view.findViewById(R.id.contact);
    createTime = (TextView) view.findViewById(R.id.create_time);
    myhomeAppBar = (AppBarLayout) view.findViewById(R.id.myhome_appBar);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    noBrandShop = (LinearLayout) view.findViewById(R.id.no_brand_shop);

    if (getArguments() != null) {
      final Brand brand = getArguments().getParcelable("brand");
      if (brand != null) {
        mCallbackActivity.setToolbar(brand.getName(), false, null, R.menu.menu_edit,
            new Toolbar.OnMenuItemClickListener() {
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
        Glide.with(getContext())
            .load(PhotoUtils.getGauss(brand.getPhoto()))
            .placeholder(R.drawable.bg_brand)
            .into(bg);
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

        this.contact.setText(getString(R.string.creator_colon).concat(creator)
            .concat("    ")
            .concat(getString(R.string.phone_colon))
            .concat(phone));
        adapter = new BrandGymsAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);

        adapter.setListener(new OnRecycleItemClickListener() {
          @Override public void onItemClick(View v, final int pos) {
            if (adapter.getItemViewType(pos) == 0) {
              DialogUtils.showConfirm(getContext(), "", "确认删除该场馆？", (dialog, action) -> {
                dialog.dismiss();
                if (action == DialogAction.POSITIVE) {
                  deleteGym(pos);
                }
              });
            } else if (adapter.getItemViewType(pos) == 1) {
              SystemInitBody body;
              if (TextUtils.isEmpty(PreferenceUtils.getPrefString(getContext(), "init", ""))) {
                body = new SystemInitBody();
              } else {
                body = new Gson().fromJson(PreferenceUtils.getPrefString(getContext(), "init", ""),
                    SystemInitBody.class);
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

  private void deleteGym(int pos) {
    if (pos - 1 < datas.size()) {
      RxRegiste(restRepository.getStaffAllApi()
          .qcDelGym(datas.get(pos - 1).id)
          .observeOn(Schedulers.io())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .flatMap(new Func1<QcResponse, Observable<Integer>>() {
            @Override public Observable<Integer> call(QcResponse qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                List<CoachService> coachServices = qcDbManager.getAllCoachServiceNow();
                if (coachServices.size() > 1) {
                  RxBus.getBus().post(new EventFreshGyms());
                }
                return Observable.just(coachServices.size() == 1 ? 2 : 1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io());
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

  public void freshData() {
    if (mBrandId == null || TextUtils.isEmpty(mBrandId)) return;
    sp = restRepository.getStaffAllApi()
        .qcGetBrandAllShops(App.staffId, mBrandId)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<Shops>>() {
          @Override public void call(QcDataResponse<Shops> qcResponseBrandShops) {
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
    return BrandManageFragment.TAG;
  }

  @Override public void onDestroyView() {
    if (sp != null) sp.unsubscribe();
    super.onDestroyView();
  }
}
