package cn.qingchengfit.staffkit.views.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.ListAddItem;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventUnloginHomeLevel;
import cn.qingchengfit.staffkit.views.BrandItemItem;
import cn.qingchengfit.staffkit.views.ChooseBrandFragment;
import cn.qingchengfit.staffkit.views.gym.AddBrandInMainFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
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
 * Created by Paper on 2017/2/23.
 */
@FragmentWithArgs public class ChooseBrandInMainFragment extends ChooseBrandFragment {

    Toolbar toolbar;
    TextView toolbarTitle;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.ChoosePicDialogStyle);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RxBus.getBus().post(new EventUnloginHomeLevel(1));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.layout_toolbar_container,container,false);
        viewGroup.addView(view,1);
        toolbar = viewGroup.findViewById(R.id.toolbar);
        toolbarTitle = viewGroup.findViewById(R.id.toolbar_title);
        initToolbar(toolbar);
        return viewGroup;

    }

    public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarTitle.setText("选择您的品牌");
        if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup ) {
            ((ViewGroup) toolbar.getParent()).setPadding(0,
              MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
        }
    }

    @Override public boolean onItemClick(int position) {
        if (mCommonFlexAdapter.getItem(position) instanceof BrandItemItem) {
            if (getParentFragment() instanceof HomeUnLoginFragment) {
                ((HomeUnLoginFragment) getParentFragment()).replace(
                    new SetGymInMainFragmentBuilder(((BrandItemItem) mCommonFlexAdapter.getItem(position)).getBrand()).build(), true);
            }
        } else if (mCommonFlexAdapter.getItem(position) instanceof ListAddItem) {
            if (getParentFragment() instanceof HomeUnLoginFragment) {
                ((HomeUnLoginFragment) getParentFragment()).replace(new AddBrandInMainFragment(), true);
            }
        }

        return true;
    }

    public void queryData() {
        RxRegiste(mRestRepository.getStaffAllApi()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                @Override public void call(QcDataResponse<BrandsResponse> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mDatas.clear();
                        if (qcResponse.data.brands != null && qcResponse.data.brands.size() > 0) {
                            for (int i = 0; i < qcResponse.data.brands.size(); i++) {
                                mDatas.add(new BrandItemItem(qcResponse.data.brands.get(i), getContext()));
                                //if (mBrand != null && qcResponse.data.brands.get(i).getId().equalsIgnoreCase(mBrand.getId())){
                                //    pos = i;
                                //}
                            }
                        } else {
                            if (getParentFragment() instanceof HomeUnLoginFragment) {
                                ((HomeUnLoginFragment) getParentFragment()).replace(new AddBrandInMainFragment(), false);
                            }
                        }
                        mDatas.add(new ListAddItem("创建品牌"));

                        mCommonFlexAdapter.clear();
                        mCommonFlexAdapter.updateDataSet(mDatas);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }
}
