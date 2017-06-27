package cn.qingchengfit.staffkit.views.signin.in;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.common.RealCard;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.model.responese.SigninValidCard;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.views.adapter.SignInCourseAdapter;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.cardtype.ChooseCardTypeActivity;
import cn.qingchengfit.staffkit.views.custom.SignInCardSelectDialogFragment;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseWardrobeActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 手动签到
 * Created by yangming on 16/8/29.
 */
public class SignInManualFragment extends BaseFragment implements SignInManualPresenter.SignInManualView {

    public static final int RESULT_LOCKER = 2;

    @BindView(R.id.iv_signin_item_student_face) ImageView ivSigninItemStudentFace;
    @BindView(R.id.tv_signin_item_student_name) TextView tvSigninItemStudentName;
    @BindView(R.id.iv_signin_item_student_gender) ImageView ivSigninItemStudentGender;
    @BindView(R.id.tv_signin_item_student_phone) TextView tvSigninItemStudentPhone;
    @BindView(R.id.tv_signin_card) TextView tvSigninCard;
    @BindView(R.id.tv_signin_locker) TextView tvSigninLocker;
    @BindView(R.id.tv_signin_item_confirm) TextView tvSigninItemConfirm;
    @BindView(R.id.ll_signin_confirm) LinearLayout llSigninConfirm;
    @BindView(R.id.tv_signin_course) TextView tvSigninCourse;
    @BindView(R.id.recyclerview_signin_course) RecyclerView recyclerviewSigninCourse;
    @BindView(R.id.tv_sign_in_charge) TextView tvSignInCharge;
    @BindView(R.id.ll_sign_in_balance_less) LinearLayout llSignInBalanceLess;
    @BindView(R.id.margin) View margin;

    @Inject SignInManualPresenter presenter;

    @Inject StudentWrapper studentBean;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;

    List<SignInTasks.Schedule> courseList = new ArrayList<>();
    List<SigninValidCard.DataBean.CardsBean> cardList = new ArrayList<>();
    SignInCourseAdapter adapter;
    String selectedCardId = "";
    int selectedPos = -1;
    SigninValidCard.DataBean.CardsBean selectedCard;
    List<SignInCardCostBean.CardCost> cardCosts = new ArrayList<>();
    @BindView(R.id.ll_signin_manual_locker) LinearLayout llSigninManualLocker;
    @BindView(R.id.course_layout) LinearLayout courseLayout;
    private Locker selectedLocker;//选中的更衣柜
    private Integer selectedCardStatus = 0;

    @Inject public SignInManualFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_manual, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        mCallbackActivity.setToolbar("手动签到", false, null, 0, null);
        recyclerviewSigninCourse.setLayoutManager(new LinearLayoutManager(getContext()));

        Glide.with(getContext())
            .load(PhotoUtils.getSmall(studentBean.checkin_avatar()))
            .asBitmap()
            .placeholder(R.drawable.img_default_checkinphoto)
            .error(R.drawable.img_default_checkinphoto)
            .into(ivSigninItemStudentFace);

        tvSigninItemStudentName.setText(studentBean.username());
        tvSigninItemStudentPhone.setText(studentBean.phone());

        if (studentBean.gender() == 0) {//男
            ivSigninItemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            ivSigninItemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
        }
        showLoading();
        tvSigninCard.setClickable(false);
        presenter.queryCourse();
        presenter.getCardCostList();

        if (SignInActivity.checkinWithLocker == 1) {
            llSigninManualLocker.setVisibility(View.VISIBLE);
        } else {
            llSigninManualLocker.setVisibility(View.GONE);
        }
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .placeholder(R.drawable.img_default_checkinphoto)
                                .into(ivSigninItemStudentFace);
                            studentBean.getStudentBean().checkin_avatar = s;
                            presenter.changeImage(App.staffId, s, studentBean.id());
                            StudentAction.newInstance().updateStudentCheckin(studentBean.id(), s);
                            hideLoading();
                        }
                    }));
            }
        });

        Drawable drawable = getResources().getDrawable(R.drawable.ic_sign_in_selected);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvSigninItemConfirm.setCompoundDrawables(drawable, null, null, null);

        return view;
    }

    @OnClick(R.id.iv_signin_item_student_face) public void onAvatarClick() {

        if (StringUtils.isEmpty(studentBean.getStudentBean().checkin_avatar)) {
            if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                showAlert(R.string.alert_permission_forbid);
                return;
            }

            ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true);
            dialog.show(getFragmentManager(), "");
        } else {
            SimpleImgDialog.newInstance(studentBean.checkin_avatar()).show(getFragmentManager(), "");
        }
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void confirmSignIn() {
        hideLoading();
        ToastUtils.show("签到成功");
        llSigninConfirm.setClickable(false);
        tvSigninCard.setClickable(false);
        tvSigninLocker.setClickable(false);
        getActivity().finish();
    }

    @Override public void onCourseListData(List<SignInTasks.Schedule> list) {
        if (list == null || list.size() == 0) {
            courseLayout.setVisibility(View.GONE);
            margin.setVisibility(View.GONE);
        } else {
            courseLayout.setVisibility(View.VISIBLE);
            margin.setVisibility(View.VISIBLE);
        }
        courseList = list;
        tvSigninCourse.setText(getContext().getString(R.string.sign_in_untreated_class, courseList.size()));
        if (adapter == null) {
            adapter = new SignInCourseAdapter(courseList);
            recyclerviewSigninCourse.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override public void getCardCost(List<SignInCardCostBean.CardCost> signInConfigs) {
        cardCosts = signInConfigs;//.data.card_costs;
        presenter.queryCard();
    }

    @Override public void queryCardList(List<SigninValidCard.DataBean.CardsBean> cards) {
        hideLoading();
        tvSigninCard.setClickable(true);
        cardList.clear();
        if (cards != null && cards.size() > 0) {
            for (SigninValidCard.DataBean.CardsBean card : cards) {
                for (SignInCardCostBean.CardCost cardCost : cardCosts) {
                    if (Integer.valueOf(card.getCard_tpl_id()).intValue() == cardCost.getId() && cardCost.isSelected()) {
                        cardList.add(card);
                    }
                }
            }
        }

        if (cardList != null && cardList.size() > 0) {
            selectCard(0);
        } else {
            selectedCardStatus = 1;//无会员卡
            String html = "<font color='#666666'>无合适会员卡</font> , ";
            html += "<font color='#fbad77'>立即购买</font>";
            CharSequence charSequence = Html.fromHtml(html);
            tvSigninCard.setText(charSequence);
            tvSigninCard.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ToastUtils.show("立即购买");
                    Intent toCardType = new Intent(getActivity(), ChooseCardTypeActivity.class);
                    startActivityForResult(toCardType, 1);
                }
            });
        }
    }

    @Override public void selectLocker() {

    }

    @OnClick({ R.id.tv_signin_card, R.id.tv_signin_locker, R.id.ll_signin_confirm, R.id.tv_sign_in_charge })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signin_card:
                tvSigninCard.setClickable(false);
                SignInCardSelectDialogFragment.start(this, 11, (ArrayList<SigninValidCard.DataBean.CardsBean>) cardList, selectedCardId);
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        tvSigninCard.setClickable(true);
                    }
                }, 1000);
                break;
            case R.id.tv_signin_locker:
                Intent chooseLocker = new Intent(getActivity(), ChooseWardrobeActivity.class);
                chooseLocker.putExtra("locker", selectedLocker);
                startActivityForResult(chooseLocker, RESULT_LOCKER);

                break;
            case R.id.tv_sign_in_charge:
                break;
            case R.id.ll_signin_confirm:
                if (selectedCard == null) {
                    showAlert("请选择会员卡");
                    return;
                }

                if (selectedCardStatus == 1) {
                    showAlert("请购买会员卡");
                    return;
                }

                if (selectedCardStatus == 2) {
                    showAlert("请给会员卡充值");
                    return;
                }

                presenter.confirm(selectedLocker == null ? "" : selectedLocker.id + "", selectedCardId);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                selectCard(Integer.valueOf(IntentUtils.getIntentString(data)));
            } else if (requestCode == RESULT_LOCKER) {
                if (data != null) {
                    Locker locker = data.getParcelableExtra(ChooseWardrobeActivity.LOCKER);
                    if (locker != null) {//  选中的更衣柜
                        tvSigninLocker.setText(locker.name);
                        selectedLocker = locker;
                    }
                } else {
                    tvSigninLocker.setText("");
                    selectedLocker = null;
                }
            } else if (requestCode == 1) {
                buyCard(getContext(), (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE), this);
            }
        }
    }

    public void selectCard(int posotion) {
        //        selectedPos = Integer.valueOf(IntentUtils.getIntentString(data));
        //        selectedCardId = IntentUtils.getIntentString2(data);
        //        selectedCard = cardList.get(selectedPos);
        selectedPos = posotion;
        selectedCard = cardList.get(selectedPos);
        selectedCardId = String.valueOf(selectedCard.getId());
        String html = "<font color='#666666'>" + selectedCard.getName() + "(" + selectedCard.getId() + ")" + "</font> <br>";

        String balance;
        if (selectedCard.getType() == Configs.CATEGORY_VALUE) {
            balance = TextUtils.concat("余额:", String.valueOf(selectedCard.getBalance()), "元").toString();
        } else if (selectedCard.getType() == Configs.CATEGORY_DATE) {
            balance = TextUtils.concat("有效期至:", selectedCard.getEnd().split("T")[0], "").toString();
        } else {
            balance = TextUtils.concat("余额:", String.valueOf(selectedCard.getBalance()), "次").toString();
        }

        // TODO
        int cost = 0;
        String costStr = "";
        for (SignInCardCostBean.CardCost cardCost : cardCosts) {
            if (cardCost.getId() == Integer.valueOf(selectedCard.getCard_tpl_id())) {
                cost = cardCost.getCost();
                if (selectedCard.getType() == Configs.CATEGORY_VALUE) {
                    costStr = "  " + "每次签到扣费" + cost + "元/人";
                } else if (selectedCard.getType() == Configs.CATEGORY_TIMES) {
                    costStr = "  " + "每次签到扣费" + cost + "次/人";
                }
            }
        }

        html += "<font color='#9b9b9b'>" + balance + costStr + "</font>";
        CharSequence charSequence = Html.fromHtml(html);
        tvSigninCard.setText(charSequence);
        if ((selectedCard.getType() != Configs.CATEGORY_DATE && cost != 0 && cost > selectedCard.getBalance()) || (selectedCard.getType()
            == Configs.CATEGORY_DATE && 0 > selectedCard.getBalance())) {
            selectedCardStatus = 2;//余额不足
            llSignInBalanceLess.setVisibility(View.VISIBLE);
            tvSignInCharge.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    RealCard realCard = new RealCard(selectedCard.getName(), studentBean.username(),
                        selectedCard.getType() == Configs.CATEGORY_VALUE ? selectedCard.getBalance() + ""
                            : "" + ((int) selectedCard.getBalance()), "");
                    realCard.card_tpl_id = String.valueOf(selectedCard.getCard_tpl_id());
                    //                            realCard.shopids = selectedCard.getShopIds();
                    realCard.id = String.valueOf(selectedCard.getId());
                    realCard.type = selectedCard.getType();
                    realCard.isDuringHoloday = selectedCard.isIs_locked();
                    //                            realCard.isCancel = !selectedCard.is_active();
                    //                            realCard.student_ids = selectedCard.getUserIds();
                    //                            realCard.support_gyms = selectedCard.getSupportGyms();
                    Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                    intent.putExtra(Configs.EXTRA_REAL_CARD, realCard);

                    startActivity(intent);
                }
            });
        } else {
            selectedCardStatus = 0;//正常
            llSignInBalanceLess.setVisibility(View.GONE);
        }
    }

    public void buyCard(final Context context, final CardTpl card_tpl, final Fragment f) {
        Intent it = new Intent(context, BuyCardActivity.class);
        it.putExtra(Configs.EXTRA_CARD_TYPE, card_tpl);
        it.putExtra(Configs.EXTRA_STUDENT_ID, studentBean.id());
        context.startActivity(it);
    }
}
