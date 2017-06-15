package com.qingchengfit.fitcoach.fragment.manage;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponseServiceDetail;
import java.util.HashMap;
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
 * Created by Paper on 2016/12/1.
 */
@FragmentWithArgs public class EditGymFragment extends GuideSetGymFragment {
    @Arg String id;
    @Arg String model;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        layoutBrand.setVisibility(View.GONE);
        hint.setVisibility(View.GONE);
        Button btn = ((Button) view.findViewById(R.id.next_step));
        btn.setBackgroundResource(R.drawable.btn_delete);
        btn.setText("删除该场馆");
        ((LinearLayout.LayoutParams) btn.getLayoutParams()).setMargins(0, MeasureUtils.dpToPx(16f, getResources()), 0, 0);
        btn.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        if (view instanceof LinearLayout) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.common_toolbar, null);
            Toolbar tb = (Toolbar) v.findViewById(R.id.toolbar);
            ((TextView) v.findViewById(R.id.toolbar_title)).setText("编辑健身房");
            tb.setNavigationIcon(R.drawable.ic_arrow_left);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            tb.inflateMenu(R.menu.menu_comfirm);
            tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    //修改会员
                    if (TextUtils.isEmpty(gymName.getContent())) {
                        ToastUtils.show("健身房名称不能为空");
                        return true;
                    }
                    if (lat == 0 || lng == 0) {
                        ToastUtils.show("请重新选择健身房地址");
                        return true;
                    }
                    if (getActivity() instanceof FragActivity) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("id", ((FragActivity) getActivity()).getCoachService().getId());
                        params.put("model", ((FragActivity) getActivity()).getCoachService().getModel());
                        Shop shop =
                            new Shop.Builder().gd_district_id(city_code + "").gd_lat(lat).gd_lng(lng).name(gymName.getContent()).build();
                        RxRegiste(QcCloudClient.getApi().postApi.qcUpdateGym(App.coachid + "", params, shop)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<QcResponse>() {
                                @Override public void call(QcResponse qcResponse) {
                                    if (ResponseConstant.checkSuccess(qcResponse)) {
                                        getActivity().onBackPressed();
                                    } else {
                                        ToastUtils.show(qcResponse.getMsg());
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override public void call(Throwable throwable) {
                                    ToastUtils.show(throwable.getMessage());
                                }
                            }));
                    }

                    return false;
                }
            });
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                ((LinearLayout) view).addView(v, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight));
            }
        }
        return view;
    }

    @Override public void initData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("model", model);
        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachServer(App.coachid + "", params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseServiceDetail>() {
                @Override public void call(QcResponseServiceDetail qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        Glide.with(getContext())
                            .load(qcResponse.data.gym.getPhoto())
                            .asBitmap()
                            .into(new CircleImgWrapper(gymImg, getContext()));
                        gymName.setContent(qcResponse.data.gym.getName());
                        gymAddress.setContent(qcResponse.data.gym.getDistrictStr());
                    } else {
                        ToastUtils.show(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    ToastUtils.show(throwable.getMessage());
                }
            }));
    }

    @OnClick(R.id.next_step) public void onNextStep() {
        showAlert(R.string.contact_gm);
    }
}