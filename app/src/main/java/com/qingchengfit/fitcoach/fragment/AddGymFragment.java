package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;

import cn.qingchengfit.widgets.utils.LogUtil;

/**
 * 新增健身房页面
 *
 *  与引导公用  ！！！！！！！！！！！
 *
 */

@FragmentWithArgs
public class AddGymFragment extends GuideSetGymFragment{

//    @Arg
//    String brandid;
//    @Arg
//    String brandImgUrl;
//    @Arg
//    String brandNameStr;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        FragmentArgs.inject(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = super.onCreateView(inflater, container, savedInstanceState);
        view.findViewById(R.id.hint).setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onNextStep() {
        LogUtil.e("nextStep!!!");
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
