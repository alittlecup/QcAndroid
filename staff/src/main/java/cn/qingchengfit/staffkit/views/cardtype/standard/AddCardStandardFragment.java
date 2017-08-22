package cn.qingchengfit.staffkit.views.cardtype.standard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/17 2016.
 */
public class AddCardStandardFragment extends BaseFragment implements AddCardStandardView {

    int mCategory = 1;
    @BindView(R.id.charge) CommonInputView charge;
    @BindView(R.id.income) CommonInputView income;
    @BindView(R.id.switcher_layout) RelativeLayout switcherLayout;
    @BindView(R.id.desc) CommonInputView desc;
    @BindView(R.id.validdate) CommonInputView validdate;
    @BindView(R.id.switch_name) TextView switchName;
    @BindView(R.id.switcher) SwitchCompat switcher;
    @BindView(R.id.support_new_buy) SwitcherLayout supportNewBuy;
    @BindView(R.id.support_charge) SwitcherLayout supportCharge;
    @Inject AddCardStandardPresenter presenter;
    @BindView(R.id.for_staff) SwitcherLayout forStaff;
    private String cardtpl_id;

    public static AddCardStandardFragment newInstance(int type, String cardtpl_id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("cardtpl_id", cardtpl_id);
        AddCardStandardFragment fragment = new AddCardStandardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt("type");
            cardtpl_id = getArguments().getString("cardtpl_id");
        }
        //
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card_standard, container, false);
        unbinder = ButterKnife.bind(this, view);

        mCallbackActivity.setToolbar(getString(R.string.title_add_card_standard), false, null, 0, null);
        presenter.attachView(this);

        switchName.setText("设置有效期");
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    validdate.setVisibility(View.VISIBLE);
                } else {
                    validdate.setVisibility(View.GONE);
                }
            }
        });
        switch (mCategory) {
            case Configs.CATEGORY_VALUE:
                charge.setLabel(getString(R.string.charge_value));
                switcher.setChecked(false);
                validdate.setVisibility(View.GONE);
                break;
            case Configs.CATEGORY_TIMES:
                charge.setLabel(getString(R.string.charge_times));
                switcher.setChecked(false);
                validdate.setVisibility(View.GONE);
                break;
            case Configs.CATEGORY_DATE:
                charge.setLabel(getString(R.string.charge_day));
                switcherLayout.setVisibility(View.GONE);
                validdate.setVisibility(View.GONE);
                switcher.setChecked(false);
                break;
            default:
                break;
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public void onSuccess() {
        getActivity().onBackPressed();
    }

    @OnClick({ R.id.switcher_layout, R.id.desc, R.id.btn_comfirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher_layout:
                switcher.toggle();
                break;
            case R.id.desc:
                WriteDescFragment.start(this, 3, getString(R.string.edit_intro), "请编辑说明");
                break;
            case R.id.btn_comfirm:
                OptionBody body = new OptionBody();
                if (TextUtils.isEmpty(charge.getContent())) {
                    ToastUtils.show("请填写充值金额");
                    return;
                }
                if (mCategory != Configs.CATEGORY_VALUE) {
                    try {
                        int x = Integer.parseInt(charge.getContent());
                    } catch (Exception e) {
                        ToastUtils.show("充值请填写整数");
                    }
                }

                body.charge = charge.getContent();
                if (TextUtils.isEmpty(income.getContent())) {
                    ToastUtils.show("请填写实收金额");
                    return;
                }
                body.price = income.getContent();
                if (switcher.isChecked()) {
                    if (TextUtils.isEmpty(validdate.getContent())) {
                        ToastUtils.show("请填写有效天数");
                        return;
                    }
                    body.limit_days = true;
                    body.days = Integer.parseInt(validdate.getContent());
                } else {
                    body.limit_days = false;
                }
                body.can_charge = supportCharge.isOpen();
                body.can_create = supportNewBuy.isOpen();
                body.for_staff = forStaff.isOpen();
                body.description = desc.getContent();
                presenter.addCardstandard(cardtpl_id, body);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    break;
                case 3:
                    desc.setContent(IntentUtils.getIntentString(data));
                    break;
                default:
                    break;
            }
        }
    }

    @Override public String getFragmentName() {
        return AddCardStandardFragment.class.getName();
    }
}
