package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTplOption;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.adapter.StandardAdapter;
import cn.qingchengfit.staffkit.views.cardtype.standard.AddCardStandardFragment;
import cn.qingchengfit.staffkit.views.cardtype.standard.EditCardStandardFragment;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.EqualSpaceItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.CardBusinessUtils;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/3/16 2016.
 */
public class CardtypeDetailFragment extends BaseFragment implements CardtypeDetailView {

    @Inject CardtypeDetailPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;

    @BindView(R.id.type) TextView type;
    @BindView(R.id.cardview) View cardview;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    List<CardStandard> datas = new ArrayList<>();
    @BindView(R.id.cardname) TextView cardname;
    @BindView(R.id.cardid) TextView cardid;
    @BindView(R.id.limit) TextView limit;
    @BindView(R.id.intro) TextView intro;
    @BindView(R.id.card_bg) LinearLayout cardBg;
    @BindView(R.id.support_gyms) TextView supportGyms;
    @BindView(R.id.del) TextView del;
    @BindView(R.id.disable_corner) ImageView disableCorner;
    private StandardAdapter adapter;
    private CardTpl card_tpl;

    public static CardtypeDetailFragment newInstance(CardTpl card_tpl) {

        Bundle args = new Bundle();
        args.putParcelable("card_tpl", card_tpl);
        CardtypeDetailFragment fragment = new CardtypeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            card_tpl = getArguments().getParcelable("card_tpl");
        }
        //
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardtype_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        delegatePresenter(presenter, this);
        mCallbackActivity.setToolbar(getString(R.string.title_cardtype_detail), false, null, R.menu.menu_flow,
            new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    BottomSheetListDialogFragment.start(CardtypeDetailFragment.this, 1,
                        getResources().getStringArray(card_tpl.is_enable ? R.array.card_tpl_flow : R.array.card_tpl_flow_resume));
                    return true;
                }
            });
        cardBg.setBackground(DrawableUtils.generateBg(16,
            CardBusinessUtils.getDefaultCardbgColor(card_tpl.getType())));
        datas.add(new CardStandard(true, "", "", "", ""));
        adapter = new StandardAdapter(getContext(), datas, false, card_tpl.is_enable);
        recycleview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleview.addItemDecoration(new EqualSpaceItemDecoration(MeasureUtils.dpToPx(8f, getResources())));
        recycleview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (datas.get(pos).isAdd()) {
                    if (!gymWrapper.inBrand() && serPermisAction.checkNoOnePer(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                        showAlert(R.string.alert_permission_forbid);
                        return;
                    }

                    if (!gymWrapper.inBrand()) {
                        if (card_tpl.getShops().size() > 1) {
                            showAlert(R.string.alert_edit_cardtype_link_manage);
                            return;
                        }
                    }
                    boolean hasP = false;
                    for (int i = 0; i < card_tpl.getShopIds().size(); i++) {
                        if (SerPermisAction.check(card_tpl.getShopIds().get(i), PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                            hasP = true;
                            break;
                        }
                    }

                    if (!hasP) {
                        if (card_tpl.getShopIds().size() > 1) {
                            showAlert(R.string.alert_edit_cardtype_forbid);
                        } else {
                            showAlert(R.string.alert_permission_forbid);
                        }
                        return;
                    }

                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(),
                            AddCardStandardFragment.newInstance(card_tpl.getCardTypeInt(), card_tpl.getId()))
                        .addToBackStack("")
                        .commit();
                    //                    mCallbackActivity.onChangeFragment(AddCardStandardFragment.newInstance(0,card_tpl.getId()));
                } else {//编辑卡模板

                    if (!gymWrapper.inBrand()) {
                        if (card_tpl.getShops().size() > 1) {
                            showAlert(R.string.alert_edit_cardtype_link_manage);
                            return;
                        }
                        if (!gymWrapper.inBrand() && !serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                            showAlert(R.string.alert_permission_forbid);
                            return;
                        }
                    } else {

                        boolean hasP = true;
                        for (int i = 0; i < card_tpl.getShopIds().size(); i++) {
                            if (!SerPermisAction.check(card_tpl.getShopIds().get(i), PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                                hasP = false;
                                break;
                            }
                        }
                        if (!hasP) {
                            if (card_tpl.getShopIds().size() > 1) {
                                showAlert(R.string.alert_edit_cardtype_forbid);
                            } else {
                                showAlert(R.string.alert_permission_forbid);
                            }
                            return;
                        }
                    }

                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), EditCardStandardFragment.newInstance(datas.get(pos), card_tpl.getType()))
                        .addToBackStack("")
                        .commit();
                }
            }
        });
        onGetCardTypeInfo(card_tpl);
        initOptions();
        presenter.queryOptions(card_tpl.getId());
        presenter.queryCardType(card_tpl.getId());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initOptions() {
        List<CardStandard> datas = new ArrayList<CardStandard>();
        for (CardTplOption option : card_tpl.getOptions()) {
            String support =
                "适用于:" + (option.can_create ? "新购卡" : "") + ((option.can_create && option.can_charge) ? "、" : "") + (option.can_charge
                    ? "充值" : "");
            String unit = "";
            if ("1".equals(option.card_tpl.type)) {
                unit = "元";
            } else if ("2".equals(option.card_tpl.type)) {
                unit = "次";
            } else {
                unit = "天";
            }
            CardStandard standard = new CardStandard(option.charge + unit, "(售价: " + option.price + "元)", support,
                option.limit_days ? "有效期: " + option.days + "天" : "有效期: 不限");
            standard.setId(option.id);
            standard.setIncome((int) Float.parseFloat(option.price));
            standard.setSupportCharge(option.can_charge);
            standard.setSupportCreate(option.can_create);
            standard.setCharge(option.charge + "");
            standard.setIncome((int) Float.parseFloat(option.price));
            standard.setLimitDay(option.limit_days ? option.days : 0);
            if (card_tpl.getType() == Configs.CATEGORY_DATE) {
                standard.setValid_date(null);
            }
            datas.add(standard);
        }
        onGetStandards(datas);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                int type = Integer.parseInt(IntentUtils.getIntentString(data));
                if (type == 0) {//编辑
                    if (!card_tpl.is_enable) {
                        showAlert("已停用的会员卡种类无法编辑");
                        return;
                    }

                    if (!gymWrapper.inBrand() && serPermisAction.checkNoOnePer(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                        showAlert(R.string.alert_permission_forbid);
                        return;
                    }

                    boolean hasP = true;
                    for (int i = 0; i < card_tpl.getShopIds().size(); i++) {
                        if (!SerPermisAction.check(card_tpl.getShopIds().get(i), PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
                            hasP = false;
                            break;
                        }
                    }
                    if (!hasP && !gymWrapper.inBrand()) {
                        if (card_tpl.getShopIds().size() > 1) {
                            showAlert(R.string.alert_edit_cardtype_forbid);
                        } else {
                            showAlert(R.string.alert_permission_forbid);
                        }
                        return;
                    }
                    if (!gymWrapper.inBrand()) {
                        if (card_tpl.getShops().size() > 1) {
                            showAlert(R.string.alert_edit_cardtype_link_manage);
                            return;
                        }
                    }

                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), EditCardTypeFragment.newInstance(card_tpl))
                        .addToBackStack(null)
                        .commit();
                } else {//停用或者恢复该会员卡
                    if (card_tpl.is_enable) {//停用会员卡种类
                        if (gymWrapper.inBrand()) {
                            if (!SerPermisAction.checkMuti(PermissionServerUtils.CARDSETTING_CAN_DELETE, card_tpl.getShopIds())) {
                                if (card_tpl.getShops().size() > 1) {
                                    showAlert(R.string.alert_edit_cardtype_forbid);
                                } else {
                                    showAlert(R.string.alert_permission_forbid);
                                }
                                return;
                            }
                        } else {
                            if (card_tpl.getShops().size() > 1) {
                                showAlert(R.string.alert_del_cardtype_link_manage);
                                return;
                            }
                            if (!serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_DELETE)) {
                                showAlert(R.string.alert_permission_forbid);
                                return;
                            }
                        }

                        DialogUtils.instanceDelDialog(getContext(), "是否确认停用", "1.停用后，该会员卡种类无法用于开卡\n2.停用后，该会员卡种类无法用于排课支付\n3.不影响已发行会员卡的使用和显示",
                            new MaterialDialog.SingleButtonCallback() {
                                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    presenter.delCardtype(card_tpl.getId());
                                }
                            }).show();
                    } else {//恢复会员卡

                        if (gymWrapper.inBrand()) {
                            if (!SerPermisAction.checkMuti(PermissionServerUtils.CARDSETTING_CAN_WRITE, card_tpl.getShopIds())) {
                                if (card_tpl.getShops().size() > 1) {
                                    showAlert(R.string.alert_edit_cardtype_forbid);
                                } else {
                                    showAlert(R.string.alert_permission_forbid);
                                }
                                return;
                            }
                        } else {
                            if (card_tpl.getShops().size() > 1) {
                                showAlert(R.string.alert_del_cardtype_link_manage);
                                return;
                            }
                            if (!serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
                                showAlert(R.string.alert_permission_forbid);
                                return;
                            }
                        }
                        DialogUtils.instanceDelDialog(getContext(), "是否确认恢复", new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                presenter.resumeCardtype(App.staffId, card_tpl.getId());
                            }
                        }).show();
                    }
                }
            }
        }
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onGetCardTypeInfo(CardTpl card_tpl) {
        adapter.setEnable(card_tpl.is_enable);
        adapter.notifyDataSetChanged();
        disableCorner.setVisibility(card_tpl.is_enable ? View.GONE : View.VISIBLE);
        cardname.setText(card_tpl.getName());
        cardid.setText("ID:" + card_tpl.getId());
        if (cardview instanceof CardView) {
            ((CardView) cardview).setCardBackgroundColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
        } else {
            //            cardview.setBackgroundResource(R.drawable.card_tpl_bg_less_21);
            GradientDrawable shapeDrawable = (GradientDrawable) cardview.getBackground();
            shapeDrawable.setColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
        }

        limit.setText(card_tpl.getLimit());
        intro.setText(card_tpl.getDescription());
        supportGyms.setText("适用于: " + card_tpl.getShopNames());
        if (card_tpl.getType() == Configs.CATEGORY_VALUE) {
            type.setText("储值类型");
        } else if (card_tpl.getType() == Configs.CATEGORY_TIMES) {
            type.setText("次卡类型");
        } else {
            type.setText("期限类型");
        }
        this.card_tpl = card_tpl;
    }

    @Override public void onGetStandards(List<CardStandard> cardStandards) {
        datas.clear();
        datas.addAll(cardStandards);
        if (card_tpl.is_enable) datas.add(new CardStandard(true, "", "", "", ""));
        adapter.notifyDataSetChanged();
    }

    @Override public void onDelSucceess() {
        getActivity().onBackPressed();
    }

    @Override public void onDelFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public void onResumeOk() {
        hideLoading();
        ToastUtils.show("已恢复");
        presenter.queryCardType(card_tpl.getId());
        card_tpl.is_enable = true;
        presenter.queryOptions(card_tpl.getId());
    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }

    @Override public String getFragmentName() {
        return CardtypeDetailFragment.class.getName();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
