package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.bean.CoachInitBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.network.response.QcResponseSystenInit;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 新增健身房页面
 * <p>
 * 与引导公用  ！！！！！！！！！！！
 */
@FragmentWithArgs
public class AddGymFragment extends GuideSetGymFragment {
    private SearchInterface searchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.findViewById(R.id.hint).setVisibility(View.GONE);
        ((Button) view.findViewById(R.id.next_step)).setText(R.string.login_comfirm);
        if (view instanceof LinearLayout) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.common_toolbar, null);
            Toolbar tb = (Toolbar)v.findViewById(R.id.toolbar);
            ((TextView)v.findViewById(R.id.toolbar_title)).setText("完善资料");
            tb.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                ((LinearLayout) view).addView(v, 0,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,actionBarHeight));
            }
        }
        return view;
    }


    @Override
    public void onNextStep() {
        showLoading();
        if (TextUtils.isEmpty(gymName.getContent()) ){
            cn.qingchengfit.utils.ToastUtils.show("请填写场馆名称");
            return;
        }
        if ( lat == 0 || lng == 0){
            cn.qingchengfit.utils.ToastUtils.show("请重新选择场馆位置");
            return;
        }

        CoachInitBean bean = new CoachInitBean();
        bean.brand_id = brandid;
        bean.shop = new Shop.Builder().gd_lat(lat).gd_lng(lng).name(gymName.getContent()).gd_district_id(city_code+"").photo(imgUrl).build();

        RxRegiste(TrainerRepository.getStaticTrainerAllApi().qcInit(bean)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())

            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseSystenInit>() {
                    @Override
                    public void call(QcResponseSystenInit qcResponse) {
                        hideLoading();
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            // TODO: 16/11/16 新建成功
                            //getActivity().onBackPressed();
                            searchListener.onSearchResult(100, Long.parseLong(qcResponse.data.gym_id),qcResponse.data.name,qcResponse.data.brand_name,qcResponse.data.photo,false);
                        } else ToastUtils.showDefaultStyle(qcResponse.msg);
                    }
                }, throwable -> {
                    hideLoading();
                    ToastUtils.showDefaultStyle("error!");
                })
        );

    }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof SearchInterface) {
                searchListener = (SearchInterface) context;
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            searchListener = null;
        }

}

