package cn.qingchengfit.staffkit.views.statement.excel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.BrandResponse;
import cn.qingchengfit.model.responese.ClassStatmentFilterBean;
import cn.qingchengfit.model.responese.SaleFilter;
import cn.qingchengfit.model.responese.SigninFilter;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.views.WriteDescFragment;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.OutExcelBody;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.google.gson.JsonObject;
import javax.inject.Inject;
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
 * Created by Paper on 16/6/30 2016.
 */
public class OutExcelFragment extends BaseFragment {
    private static final int Length_ = 20;
	CommonInputView title;
	CommonInputView email;
	Button comfirm;

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    private String mShopId;

    private ClassStatmentFilterBean mClassFilter;
    private SaleFilter mSaleFilter;
    private SigninFilter mSigninFilter;
    private String titleStr;

    public static OutExcelFragment newInstance(ClassStatmentFilterBean classFilter, String shopid) {

        Bundle args = new Bundle();
        args.putParcelable("class", classFilter);
        args.putString("shopid", shopid);
        OutExcelFragment fragment = new OutExcelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OutExcelFragment newInstance(SaleFilter salefilter, String shopid) {

        Bundle args = new Bundle();
        args.putParcelable("sale", salefilter);
        args.putString("shopid", shopid);
        OutExcelFragment fragment = new OutExcelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OutExcelFragment newInstance(SigninFilter signinFilter, String shopid) {

        Bundle args = new Bundle();
        args.putParcelable("signin", signinFilter);
        args.putString("shopid", shopid);
        OutExcelFragment fragment = new OutExcelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClassFilter = getArguments().getParcelable("class");
            mSaleFilter = getArguments().getParcelable("sale");
            mSigninFilter = getArguments().getParcelable("signin");
            mShopId = getArguments().getString("shopid");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_out_excel, container, false);
      title = (CommonInputView) view.findViewById(R.id.title);
      email = (CommonInputView) view.findViewById(R.id.email);
      comfirm = (Button) view.findViewById(R.id.comfirm);
      view.findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OutExcelFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OutExcelFragment.this.onClick(v);
        }
      });

      //
        mCallbackActivity.setToolbar(getString(R.string.out_excel), false, null, 0, null);

        title.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });
        email.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });

        checkBtn();

        restRepository.getGet_api()
            .qcGetBrand(loginStatus.staff_id(), gymWrapper.brand_id())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<BrandResponse>>() {
                @Override public void call(QcDataResponse<BrandResponse> qcResponseBrand) {
                    if (ResponseConstant.checkSuccess(qcResponseBrand)) {
                        String brandStr = qcResponseBrand.data.brand.getName();
                        titleStr = "";
                        if (gymWrapper.inBrand()) {
                            if (!TextUtils.isEmpty(mShopId)) {
                                CoachService coachService = gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), mShopId);
                                if (coachService != null) {
                                    brandStr = brandStr.concat(coachService.getName());
                                }
                            }
                        } else {
                            brandStr = brandStr.concat(gymWrapper.brand_name());
                        }
                        if (mClassFilter != null) {
                            titleStr = titleStr.concat(mClassFilter.start)
                                .concat(getString(R.string.to))
                                .concat(mClassFilter.end)
                                .concat(brandStr)
                                .concat("课程报表");
                            ;
                        } else if (mSaleFilter != null) {
                            titleStr = titleStr.concat(mSaleFilter.startDay)
                                .concat(getString(R.string.to))
                                .concat(mSaleFilter.endDay)
                                .concat(brandStr)
                                .concat("销售报表");
                        } else if (mSigninFilter != null) {
                            titleStr = titleStr.concat(mSigninFilter.startDay)
                                .concat(getString(R.string.to))
                                .concat(mSigninFilter.endDay)
                                .concat(brandStr)
                                .concat("签到报表");
                        }
                        title.setContent(titleStr.length() > 15 ? titleStr.substring(0, 15).concat("...") : titleStr);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
        return view;
    }

    private void checkBtn() {
        comfirm.setEnabled(!TextUtils.isEmpty(email.getContent()) && !TextUtils.isEmpty(title.getContent()));
    }

    @Override public String getFragmentName() {
        return OutExcelFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title:
                WriteDescFragment.start(this, 1, getString(R.string.title_edit_email), "", titleStr);
                break;
            case R.id.comfirm:
                OutExcelBody body = new OutExcelBody();
                if (gymWrapper.inBrand()) {
                    body.brand_id = gymWrapper.brand_id();
                    body.shop_id = mShopId;
                } else {
                    body.id = gymWrapper.id();
                    body.model = gymWrapper.model();
                }
                body.title = titleStr;
                body.to_email = email.getContent();
                if (mSaleFilter != null) {
                    body.action = OutExcelBody.Sale;
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("start", mSaleFilter.startDay);
                    jsonObject.addProperty("end", mSaleFilter.endDay);
                    if (mSaleFilter.card != null) jsonObject.addProperty("card__card_tpl_id", mSaleFilter.card.card_tpl_id);
                    if (mSaleFilter.card_category > 0) {
                        switch (mSaleFilter.card_category) {
                            case Configs.CATEGORY_VALUE:
                                jsonObject.addProperty("cards_extra", "all_value");
                                break;
                            case Configs.CATEGORY_TIMES:
                                jsonObject.addProperty("cards_extra", "all_times");
                                break;
                            case Configs.CATEGORY_DATE:
                                jsonObject.addProperty("cards_extra", "all_time");
                                break;
                            default:
                                break;
                        }
                    }
                    if (mSaleFilter.saler != null) jsonObject.addProperty("seller_id", mSaleFilter.saler.id);
                    if (mSaleFilter.tradeType > 0) jsonObject.addProperty("type", mSaleFilter.tradeType);
                    if (mSaleFilter.payMethod > 0) jsonObject.addProperty("charge_type", mSaleFilter.payMethod);
                    body.query = jsonObject;
                } else if (mClassFilter != null) {
                    body.action = OutExcelBody.course;

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("start", mClassFilter.start);
                    jsonObject.addProperty("end", mClassFilter.end);
                    if (mClassFilter.coach != null) jsonObject.addProperty("teacher_id", mClassFilter.coach.id);
                    if (mClassFilter.course != null) {
                        jsonObject.addProperty("course_id", mClassFilter.course.getId());
                    }
                    if (mClassFilter.course_type < 0) {
                        if (mClassFilter.course_type == -2) {
                            jsonObject.addProperty("course_extra", "all_private");
                        } else if (mClassFilter.course_type == -3) {
                            jsonObject.addProperty("course_extra", "all_public");
                        }
                    }

                    body.query = jsonObject;
                } else if (mSigninFilter != null) {
                    body.action = OutExcelBody.signin;
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("start", mSigninFilter.startDay);
                    jsonObject.addProperty("end", mSigninFilter.endDay);
                    if (mSigninFilter.card != null) {
                        jsonObject.addProperty("card__card_tpl_id", mSigninFilter.card.getCard_tpl_id());
                    } else if (mSigninFilter.card_category > 0) {
                        switch (mSigninFilter.card_category) {
                            case Configs.CATEGORY_VALUE:
                                jsonObject.addProperty("cards_extra", "all_value");
                                break;
                            case Configs.CATEGORY_TIMES:
                                jsonObject.addProperty("cards_extra", "all_times");
                                break;
                            case Configs.CATEGORY_DATE:
                                jsonObject.addProperty("cards_extra", "all_time");
                                break;
                            default:
                                break;
                        }
                    }
                    if (mSigninFilter.status > 0) {
                        jsonObject.addProperty("status__in", mSigninFilter.status);
                    }
                    body.query = jsonObject;
                }
                showLoading();
                RxRegiste(restRepository.getPost_api()
                    .qcOutExcel(loginStatus.staff_id(), body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<QcResponse>() {
                        @Override public void call(QcResponse qcResponse) {
                            hideLoading();
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                ToastUtils.show(getString(R.string.email_have_send));
                                getActivity().onBackPressed();
                            } else {
                                ToastUtils.show(qcResponse.getMsg());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            hideLoading();
                        }
                    }));
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                titleStr = IntentUtils.getIntentString(data);

                title.setContent(titleStr.length() > 15 ? titleStr.substring(0, 15).concat("...") : titleStr);
            }
        }
    }
}
