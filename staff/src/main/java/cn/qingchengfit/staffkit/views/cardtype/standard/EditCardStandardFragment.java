package cn.qingchengfit.staffkit.views.cardtype.standard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
 * Created by Paper on 16/3/15 2016.
 */
public class EditCardStandardFragment extends BaseFragment implements EditCardStandardView {

    @BindView(R.id.charge) CommonInputView charge;
    @BindView(R.id.income) CommonInputView income;
    @BindView(R.id.switch_name) TextView switchName;
    @BindView(R.id.switcher) SwitchCompat switcher;
    @BindView(R.id.switcher_layout) RelativeLayout switcherLayout;
    @BindView(R.id.validdate) CommonInputView validdate;
    @BindView(R.id.support_new_buy) SwitcherLayout supportNewBuy;
    @BindView(R.id.support_charge) SwitcherLayout supportCharge;
    @BindView(R.id.desc) CommonInputView desc;
    @BindView(R.id.btn_comfirm) Button btnComfirm;

    @Inject EditCardStandardPresenter presenter;
    @BindView(R.id.del) TextView del;
    OptionBody optionBody = new OptionBody();
    CardStandard cardStandard;
    @BindView(R.id.for_staff) SwitcherLayout forStaff;
    private int mCategory = 0;

    public static EditCardStandardFragment newInstance(CardStandard cardStandard, int type) {
        Bundle args = new Bundle();
        args.putParcelable("standard", cardStandard);
        args.putInt("type", type);
        EditCardStandardFragment fragment = new EditCardStandardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardStandard = getArguments().getParcelable("standard");
            mCategory = getArguments().getInt("type");
        }
        //
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card_standard, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter.attachView(this);
        mCallbackActivity.setToolbar("规格详情", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                optionBody.charge = charge.getContent();
                optionBody.price = income.getContent();
                if (optionBody.limit_days) {
                    if (TextUtils.isEmpty(validdate.getContent())) {
                        ToastUtils.show("请填写有效天数");
                        return true;
                    }

                    optionBody.days = Integer.parseInt(validdate.getContent());
                } else {
                    optionBody.days = 0;
                }

                presenter.editStandard(optionBody);
                return true;
            }
        });

        switchName.setText("设置有效期");
        optionBody.id = cardStandard.getId();
        optionBody.limit_days = cardStandard.getLimitDay() > 0;

        optionBody.can_charge = cardStandard.isSupportCharge();
        optionBody.can_create = cardStandard.isSupportCreate();
        if (cardStandard.getLimitDay() > 0) {
            switcher.setChecked(true);
            validdate.setVisibility(View.VISIBLE);
            validdate.setContent(cardStandard.getLimitDay() + "");
            optionBody.days = cardStandard.getLimitDay();
        } else {
            switcher.setChecked(false);
            validdate.setVisibility(View.GONE);
            validdate.setContent("");
        }

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    validdate.setVisibility(View.VISIBLE);
                    optionBody.limit_days = true;
                } else {
                    validdate.setVisibility(View.GONE);
                    optionBody.limit_days = false;
                }
            }
        });
        switch (mCategory) {
            case Configs.CATEGORY_VALUE:
                charge.setLabel(getString(R.string.charge_value));
                break;
            case Configs.CATEGORY_TIMES:
                charge.setLabel(getString(R.string.charge_times));
                break;
            case Configs.CATEGORY_DATE:
                charge.setLabel(getString(R.string.charge_day));
                switcherLayout.setVisibility(View.GONE);
                validdate.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        charge.setContent(cardStandard.getCharge());
        income.setContent(cardStandard.getIncome() + "");
        supportNewBuy.setOpen(cardStandard.isSupportCreate());
        supportNewBuy.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optionBody.can_create = isChecked;
            }
        });
        supportCharge.setOpen(cardStandard.isSupportCharge());
        supportCharge.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optionBody.can_charge = isChecked;
            }
        });
        forStaff.setOpen(cardStandard.for_staff);
        forStaff.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optionBody.for_staff = isChecked;
            }
        });

        del.setVisibility(View.VISIBLE);
        btnComfirm.setVisibility(View.GONE);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @OnClick(R.id.desc) public void onDesc() {
        WriteDescFragment.start(this, 1, "填写说明", "请填写说明");
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                //说明
                optionBody.description = IntentUtils.getIntentString(data);
            }
        }
    }

    @Override public void onGetStandard() {

    }

    @Override public void onSucceed() {
        getActivity().onBackPressed();
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @OnClick({ R.id.switcher_layout, R.id.del }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher_layout:
                switcher.toggle();
                break;
            case R.id.del:
                MaterialDialog dialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                    .content("确认删除会员卡规格?")
                    .positiveText("删除")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                            presenter.delStandard(cardStandard.getId());
                        }
                    })
                    .build();
                dialog.show();

                break;
        }
    }

    @Override public String getFragmentName() {
        return EditCardStandardFragment.class.getName();
    }
}
