package cn.qingchengfit.staffkit.views.setting.brand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.body.ChangeBrandCreatorBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
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
public class BrandCreatorEditFragment extends BaseFragment {

    @BindView(R.id.name) CommonInputView name;
    @BindView(R.id.gender_male) RadioButton genderMale;
    @BindView(R.id.gender_female) RadioButton genderFemale;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    @BindView(R.id.gender_layout) RelativeLayout genderLayout;

    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @BindView(R.id.checkcode) PasswordView checkcode;

    @Inject RestRepository restRepository;
    private Brand brand;

    public static BrandCreatorEditFragment newInstance(Brand brand) {

        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        BrandCreatorEditFragment fragment = new BrandCreatorEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            brand = getArguments().getParcelable("brand");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_creator, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (brand != null) {
            mCallbackActivity.setToolbar("修改创建人", false, null, R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (TextUtils.isEmpty(name.getContent())) {
                        ToastUtils.show("请填写创始人姓名");
                        return true;
                    }
                    if (phoneNum.checkPhoneNum() && checkcode.checkValid()) {
                        showLoading();
                        RxRegiste(restRepository.getPost_api()
                            .qcChangeBrandUser(brand.getId(), new ChangeBrandCreatorBody.Builder().username(name.getContent())
                                .code(checkcode.getCode())
                                .gender(courseTypeRg.getCheckedRadioButtonId() == R.id.gender_female ? 1 : 0)
                                .phone(phoneNum.getPhoneNum())
                                .area_code(phoneNum.getDistrictInt())
                                .build())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Action1<QcResponse>() {
                                @Override public void call(QcResponse qcResponse) {
                                    hideLoading();
                                    if (ResponseConstant.checkSuccess(qcResponse)) {
                                        ToastUtils.show("修改成功");
                                        getFragmentManager().popBackStack(BrandManageFragment.TAG,
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    } else {
                                        ToastUtils.show("修改失败");
                                    }
                                }
                            }));
                    }

                    return true;
                }
            });
        }
        checkcode.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (phoneNum.checkPhoneNum()) {
                    RxRegiste(restRepository.qcQueryCode(
                        new GetCodeBody.Builder().area_code(phoneNum.getDistrictInt()).phone(phoneNum.getPhoneNum()).build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<QcResponse>() {
                            @Override public void call(QcResponse qcResponse) {
                                if (ResponseConstant.checkSuccess(qcResponse)) {
                                    ToastUtils.show("验证码已发送");
                                    checkcode.blockRightClick(true);
                                    RxRegiste(Observable.interval(0, 1, TimeUnit.SECONDS)
                                        .take(60)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())

                                        .subscribe(new Action1<Long>() {
                                            @Override public void call(Long aLong) {

                                                if (aLong >= 59) {
                                                    checkcode.blockRightClick(false);
                                                    checkcode.setRightText(getResources().getString(R.string.login_getcode));
                                                } else {
                                                    checkcode.setRightText(
                                                        String.format(getString(R.string.login_resend_msg), (int) (60 - aLong)));
                                                }
                                            }
                                        }, new Action1<Throwable>() {
                                            @Override public void call(Throwable throwable) {

                                            }
                                        }));
                                } else {
                                    ToastUtils.show("验证码发送失败");
                                }
                            }
                        }));
                }
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return BrandCreatorEditFragment.class.getName();
    }

    @Override public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
    }
}
