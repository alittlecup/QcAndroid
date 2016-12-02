package com.qingchengfit.fitcoach.fragment;

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
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.base.Shop;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.findViewById(R.id.hint).setVisibility(View.GONE);
        ((Button) view.findViewById(R.id.next_step)).setText(R.string.login_comfirm);
        if (view instanceof LinearLayout) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.common_toolbar, null);
            Toolbar tb = (Toolbar)v.findViewById(R.id.toolbar);
            ((TextView)v.findViewById(R.id.toolbar_title)).setText("新建健身房");
            tb.setNavigationIcon(R.drawable.ic_arrow_left);
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
        //// TODO: 16/11/16 新建健身房
        if (TextUtils.isEmpty(gymName.getContent()) || lat == 0 || lng == 0){
            cn.qingchengfit.widgets.utils.ToastUtils.show("请填写完整");
            return;
        }

        CoachInitBean bean = new CoachInitBean();
        bean.brand_id = brandid;
        bean.shop = new Shop.Builder().gd_lat(lat).gd_lng(lng).name(gymName.getContent()).gd_district_id(city_code+"").build();

        RxRegiste(QcCloudClient.getApi().postApi.qcInit(bean)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                    @Override
                    public void call(QcResponse qcResponse) {
                        hideLoading();
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            // TODO: 16/11/16 新建成功
                            getActivity().onBackPressed();

                        } else ToastUtils.showDefaultStyle(qcResponse.msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideLoading();
                        ToastUtils.showDefaultStyle("error!");
                    }
                })
        );

    }
}

//public class AddGymFragment extends Fragment {
//    public static final String TAG = AddGymFragment.class.getName();
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.addgym_name)
//    CommonInputView addgymName;
//    @BindView(R.id.addgym_contact)
//    CommonInputView addgymContact;
//    @BindView(R.id.addgym_city)
//    CommonInputView addgymCity;
//    @BindView(R.id.workexpedit_descripe)
//    EditText workexpeditDescripe;
//    @BindView(R.id.addgym_addbtn)
//    Button addgymAddbtn;
//    @BindView(R.id.rootview)
//    LinearLayout rootview;
//    @BindView(R.id.addgym_brand)
//    CommonInputView addgymBrand;
//
//    private ArrayList<String> options1Items = new ArrayList<String>();
//    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
//
//    private OptionsPopupWindow pwOptions;
//
//    private CitiesChooser citiesChooser;
//    private int Districtid = 0;
//    private SearchInterface searchListener;
//
//    public AddGymFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        citiesChooser = new CitiesChooser(getActivity());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_guide_setgym, container, false);
//        unbinder=ButterKnife.bind(this, view);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
//        toolbar.setTitle("添加健身房");
//        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
//        citiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
//            @Override
//            public void onCityChoosen(String provice, String city, String district, int id) {
//                Districtid = id;
//                addgymCity.setContent(provice + city + "市");
//            }
//        });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//        return view;
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick(R.id.addgym_city)
//    public void onClickCtiy() {
//        citiesChooser.show(rootview);
//    }
//
//    @OnClick(R.id.addgym_addbtn)
//    public void onClickAdd() {
//
//        if (addgymName.getContent().length() < 3) {
//            Toast.makeText(getContext(), "健身房名称不能少于三个字", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (Districtid == 0) {
//            Toast.makeText(getContext(), "必须选择所在城市", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(addgymBrand.getContent())){
//            Toast.makeText(getContext(), "请填写场馆名称", Toast.LENGTH_SHORT).show();
//            return;
//
//        }
//
//        if (TextUtils.isEmpty(addgymContact.getContent()) || addgymContact.getContent().length() < 7) {
//            Toast.makeText(getContext(), "请填写正确的联系方式", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        AddGymPostBean bean = new AddGymPostBean(addgymName.getContent(), Districtid, addgymContact.getContent(), workexpeditDescripe.getText().toString(),addgymBrand.getContent());
//        QcCloudClient.getApi().postApi.qcAddGym(bean).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(qcAddGymResponse -> {
//                            if (qcAddGymResponse.status == ResponseResult.SUCCESS) {
//                                searchListener.onSearchResult(100, Integer.parseInt(qcAddGymResponse.data.gym.id), qcAddGymResponse.data.gym.name,qcAddGymResponse.data.gym.brand_name,qcAddGymResponse.data.gym.photo,false);
//                            } else {
//                                Toast.makeText(getContext(), qcAddGymResponse.msg, Toast.LENGTH_SHORT).show();
//                            }
//                        }, throwable -> {
//                        }, () -> {
//                        }
//                );
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof SearchInterface) {
//            searchListener = (SearchInterface) context;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        searchListener = null;
//    }
//}
