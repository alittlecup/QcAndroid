package cn.qingchengfit.staffkit.views.signin.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.presenters.ModuleConfigsPresenter;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
import cn.qingchengfit.staffkit.views.signin.SignInCloseFragment;
import cn.qingchengfit.staffkit.views.signin.SignInHomeFragment;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/2/13.
 */
@FragmentWithArgs public class SignInTypeFragment extends BaseFragment
    implements SigninConfigCardtypePresenter.MVPView, ModuleConfigsPresenter.MVPView {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Arg(required = false) int enterType;// 0 为正常  1为设置跳转
    @Arg(required = false) int autoIn;// 0 为正常  1为自动跳转进来的
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.tag_pro) ImageView tagPro;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    @Inject RestRepositoryV2 restRepository;
    @Inject SigninConfigCardtypePresenter presenter;
    @Inject ModuleConfigsPresenter moduleConfigsPresenter;
    private ArrayList<SignInCardCostBean.CardCost> cardCosts;
    private MaterialDialog exitDialg;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_type, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        delegatePresenter(moduleConfigsPresenter, this);
        tagPro.setVisibility(gymWrapper.isPro() ? View.GONE : View.VISIBLE);
        presenter.getCardCostList();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("签到结算设置");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (cardCosts == null || cardCosts.size() == 0) {
                    ToastUtils.show("你至少要设置一种结算");
                    return true;
                } else {
                    List<SignInCostBody.CardCost> postBody = new ArrayList<SignInCostBody.CardCost>();
                    for (int i = 0; i < cardCosts.size(); i++) {
                        postBody.add(new SignInCostBody.CardCost(cardCosts.get(i).getId() + "", cardCosts.get(i).getCost()));
                    }
                    showLoading();
                    presenter.confirm(postBody);
                }
                return false;
            }
        });
    }

    @Override public String getFragmentName() {
        return SignInTypeFragment.class.getName();
    }

    @Override public boolean onFragmentBackPress() {
        if (exitDialg == null) {
            exitDialg = DialogUtils.instanceDelDialog(getContext(), "是否放弃本次设置", new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    if (autoIn == 1) {//自动跳转进来的先 设置为false
                        moduleConfigsPresenter.putModuleConfigs(false);
                    } else {
                        onCostConfigSuccess();
                    }
                }
            });
        }
        if (!exitDialg.isShowing()) exitDialg.show();
        return true;
    }

    @OnClick(R.id.layout_signin_card) public void onClick() {
        if (gymWrapper.isPro()) {
            Intent x = IntentUtils.getChooseIntent(getContext(), gymWrapper.getCoachService(), gymWrapper.getBrand(),
                ChooseActivity.SIGN_IN_CARDS);
            x.putExtra("costs", cardCosts);
            startActivityForResult(x, ChooseActivity.SIGN_IN_CARDS);
        } else {
            UpgradeInfoDialogFragment.newInstance(QRActivity.getIdentifyKey(QRActivity.MODULE_STUDENT_CARDS)).show(getFragmentManager(), "");
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            tvContent.setText(R.string.common_have_setting);
            ArrayList<Parcelable> costsList = IntentUtils.getListParcelable(data);
            if (costsList != null && costsList.size() > 0) {
                cardCosts.clear();
                for (int j = 0; j < costsList.size(); j++) {
                    SignInCardCostBean.CardCost cc = (SignInCardCostBean.CardCost) costsList.get(j);
                    cardCosts.add(cc);
                }
            }
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs) {
        boolean setted = false;
        if (signInConfigs != null) {
            for (int i = 0; i < signInConfigs.size(); i++) {
                if (signInConfigs.get(i).isSelected()) {
                    setted = true;
                }
            }
        }
        cardCosts = (ArrayList<SignInCardCostBean.CardCost>) signInConfigs;
        tvContent.setText(setted ? R.string.common_have_setting : R.string.common_un_setting);
    }

    @Override public void onCostConfigSuccess() {
        hideLoading();
        if (enterType == 0) {
            getFragmentManager().popBackStackImmediate();
        } else if (enterType == 1) {
            getFragmentManager().beginTransaction().replace(R.id.student_frag, SignInHomeFragment.newInstance()).commit();
        }
    }

    @Override public void onModuleStatus(ScoreStatus.ModuleBean moduleBean) {

    }

    @Override public void onStatusSuccess() {
        if (enterType == 1) {
            getFragmentManager().beginTransaction().replace(mCallbackActivity.getFragId(), new SignInCloseFragment()).commit();
        } else {
            getFragmentManager().popBackStackImmediate();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
