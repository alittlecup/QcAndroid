package com.qingchengfit.fitcoach.fragment.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import com.badoualy.stepperindicator.StepperIndicator;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.activity.ChooseBrandActivity;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.event.EventToolbar;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
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
 * Created by Paper on 16/11/10.
 */
public class GuideFragment extends BaseFragment {

    public static final int RESULT_BRAND = 11;

    @BindView(R.id.step_indicator) StepperIndicator stepIndicator;

    CoachInitBean initBean;
    Gson gson = new Gson();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private Unbinder unbinder;
    private boolean isAddBrannd = false;
    private Brand brand;

    public void setAddBrannd(boolean addBrannd) {
        isAddBrannd = addBrannd;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    ;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_container, container, false);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitle.setText("新建健身房");
        RxBusAdd(EventToolbar.class).subscribe(eventToolbar -> toolbarTitle.setText(eventToolbar.title));

        initBean = new CoachInitBean();
        if (isAddBrannd) {
            getChildFragmentManager().beginTransaction().replace(R.id.guide_frag, new GuideSetBrandFragment()).commit();
            return view;
        } else {
            if (brand != null) initBean.brand_id = brand.getId();
        }
        if (TextUtils.isEmpty(initBean.brand_id)) {
            if (App.gUser != null && App.gUser.id != null) {
                RxRegiste(QcCloudClient.getApi().getApi.qcGetBrands(App.gUser.id + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponseBrands>() {
                        @Override public void call(QcResponseBrands qcResponse) {
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                if (qcResponse.data != null && qcResponse.data.brands.size() > 0) {
                                    Intent toChooseBrand = new Intent(getActivity(), ChooseBrandActivity.class);
                                    startActivityForResult(toChooseBrand, 11);
                                } else {
                                    getChildFragmentManager().beginTransaction()
                                        .replace(R.id.guide_frag, new GuideSetBrandFragment())
                                        .commit();
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                        }
                    }));
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.guide_frag, new GuideSetBrandFragment()).commit();
            }
        } else {
            if (App.gUser != null && App.gUser.id != null) {
                RxRegiste(QcCloudClient.getApi().getApi.qcGetBrands(App.gUser.id + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponseBrands>() {
                        @Override public void call(QcResponseBrands qcResponse) {
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                if (qcResponse.data != null && qcResponse.data.brands.size() > 0) {
                                    for (int i = 0; i < qcResponse.data.brands.size(); i++) {
                                        Brand b = qcResponse.data.brands.get(i);
                                        if (b.getId().equalsIgnoreCase(initBean.brand_id)) {
                                            getChildFragmentManager().beginTransaction()
                                                .replace(R.id.guide_frag,
                                                    new GuideSetGymFragmentBuilder(b.getPhoto(), b.getName(), b.getId()).build())
                                                .commit();
                                            break;
                                        }
                                    }
                                } else {
                                    getChildFragmentManager().beginTransaction()
                                        .replace(R.id.guide_frag, new GuideSetBrandFragment())
                                        .commit();
                                }
                            } else {
                                getChildFragmentManager().beginTransaction().replace(R.id.guide_frag, new GuideSetBrandFragment()).commit();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                        }
                    }));
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.guide_frag, new GuideSetBrandFragment()).commit();
            }
        }

        RxBusAdd(EventStep.class).subscribe(new Action1<EventStep>() {
            @Override public void call(EventStep eventStep) {
                setSetp(eventStep.step);
            }
        });
        RxBusAdd(CoachInitBean.class).subscribe(new Action1<CoachInitBean>() {
            @Override public void call(CoachInitBean coachInitBean) {
                PreferenceUtils.setPrefString(getContext(), "initSystem", gson.toJson(initBean));
                LogUtil.e(gson.toJson(initBean));
            }
        });
        return view;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                Brand b = (Brand) IntentUtils.getParcelable(data);
                initBean.brand_id = b.getId();
                getChildFragmentManager().beginTransaction()
                    .replace(R.id.guide_frag, new GuideSetGymFragmentBuilder(b.getPhoto(), b.getName(), b.getId()).build())
                    .commitAllowingStateLoss();
            }
        }
    }

    public void setSetp(int s) {
        stepIndicator.setCurrentStep(s);
    }

    public CoachInitBean getInitBean() {
        return initBean;
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
