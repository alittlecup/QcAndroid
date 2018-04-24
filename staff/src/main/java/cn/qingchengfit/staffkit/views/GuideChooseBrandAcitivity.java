package cn.qingchengfit.staffkit.views;

import android.view.View;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GuideChooseBrandAcitivity extends ChooseBrandActivity {

    public void queryData() {
        if (sp != null) sp.unsubscribe();
        showLoadingTransparent();
        sp = restRepository.getGet_api()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                @Override public void call(final QcDataResponse<BrandsResponse> qcResponseBrands) {
                    hideLoadingTransparent();
                    if (ResponseConstant.checkSuccess(qcResponseBrands)) {
                        datas.clear();
                        datas.addAll(qcResponseBrands.data.brands);
                        if (!getIntent().getBooleanExtra("disable", false)) {
                            Brand add = new Brand("-1");
                            datas.add(add);
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setListener(new OnRecycleItemClickListener() {
                            @Override public void onItemClick(View v, int pos) {
                                boolean isAddGym = !getIntent().getBooleanExtra("disable", false);
                                if (!isAddGym) {
                                    if (datas.get(pos).getGym_count() > 0) {
                                        setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                        GuideChooseBrandAcitivity.this.finish();
                                        overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                        return;
                                    } else {
                                        ToastUtils.show(getString(R.string.hint_brand_whitout_gym));
                                        return;
                                    }
                                }

                                if (pos < datas.size() - 1) {
                                    if (datas.get(pos).isHas_add_permission()) {
                                        if (datas.get(pos).getGym_count() > 0 || isAddGym) {
                                            setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                            GuideChooseBrandAcitivity.this.finish();
                                            overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                        } else {
                                            ToastUtils.show(getString(R.string.hint_brand_whitout_gym));
                                        }
                                    } else {
                                        ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand),
                                            datas.get(pos).getCreated_by() == null || datas.get(pos).getCreated_by().getUsername() == null
                                                ? "" : datas.get(pos).getCreated_by().getUsername()));
                                    }
                                } else {
                                    if (Long.parseLong(datas.get(pos).getId()) < 0) {
                                        setResult(999);
                                        GuideChooseBrandAcitivity.this.finish();
                                    } else {
                                        if (!isAddGym) {
                                            setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                            GuideChooseBrandAcitivity.this.finish();
                                            overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                            return;
                                        }

                                        if (datas.get(pos).isHas_add_permission()) {
                                            if (datas.get(pos).getGym_count() > 0 || isAddGym) {
                                                setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                                GuideChooseBrandAcitivity.this.finish();
                                                overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                            } else {
                                                ToastUtils.show(getString(R.string.hint_brand_whitout_gym));
                                            }
                                        } else {
                                            ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand),
                                                datas.get(pos).getCreated_by() == null
                                                    || datas.get(pos).getCreated_by().getUsername() == null ? ""
                                                    : datas.get(pos).getCreated_by().getUsername()));
                                        }
                                    }
                                }
                            }
                        });
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    hideLoadingTransparent();
                }
            });
    }

    @Override public void onBackPressed() {
        setResult(RESULT_CANCELED);
        this.finish();
    }
}
