package cn.qingchengfit.staffkit.views.wardrobe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.cardtype.ChooseCardTypeActivity;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.NoStudentCardItemItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.PayWardrobeItem;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import dagger.android.support.AndroidSupportInjection;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
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
 * Created by Paper on 16/9/18.
 */
public class WardrobePayBottomFragment extends BaseBottomSheetDialogFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.rv) RecyclerView rv;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    private Subscription sp;

    public static WardrobePayBottomFragment newInstance(String stuId, boolean isSpend) {

        Bundle args = new Bundle();
        args.putString("s", stuId);
        args.putBoolean("isSpend", isSpend);
        WardrobePayBottomFragment fragment = new WardrobePayBottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_wardrobe_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        mAdapter = new CommonFlexAdapter(mDatas, this);
        mDatas.clear();
        rv.setAdapter(mAdapter);
        return view;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override public void onResume() {
        super.onResume();
        freshData();
    }

    private void freshData() {
        mDatas.clear();
        mDatas.add(new SimpleTextItemItem("线下支付"));
        mDatas.add(new PayWardrobeItem(2, null));
        mDatas.add(new PayWardrobeItem(4, null));
        mDatas.add(new PayWardrobeItem(5, null));
        mDatas.add(new PayWardrobeItem(6, null));
        mDatas.add(new SimpleTextItemItem("会员卡支付"));
        sp = restRepository.getGet_api()
            .qcGetStudentCardsWithShopId(App.staffId, getArguments().getString("s"), gymWrapper.getShopParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseStudentCards>() {
                @Override public void call(QcResponseStudentCards qcResponseStudentCards) {
                    if (qcResponseStudentCards.data != null && qcResponseStudentCards.data.cards != null) {
                        if (qcResponseStudentCards.data.cards.size() > 0) {
                            for (Card card : qcResponseStudentCards.data.cards) {

                                //期限卡去掉，过期卡去掉,未开卡 (非自动开卡) 去掉，
                                if (card.getType() != Configs.CATEGORY_DATE
                                    && (!card.isCheck_valid()
                                    || DateUtils.formatDateFromServer(card.getValid_to()).getTime() >= DateUtils.getDayMidnight(new Date()))
                                    //                                            && (!getArguments().getBoolean("isSpend",true) || card.getBalance() >= 0)
                                    && (!getArguments().getBoolean("isSpend", true)
                                    || card.is_auto_start()
                                    || !card.isCheck_valid()
                                    || DateUtils.formatDateFromServer(card.getValid_from()).getTime() <= new Date().getTime())) {
                                    mDatas.add(new PayWardrobeItem(1, card));
                                }
                            }
                        } else {
                            mDatas.add(new NoStudentCardItemItem());
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    @Override public void onDestroyView() {
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof PayWardrobeItem) {
            Card card = ((PayWardrobeItem) mAdapter.getItem(position)).getCard();
            if (card != null) {
                //请假卡
                if (card.is_locked()) {
                    ToastUtils.show("该卡请假中");
                    if (!getArguments().getBoolean("isSpend", true)) return true;
                }
                if (card.isCheck_valid()
                    && DateUtils.formatDateFromServer(card.getValid_from()).getTime() > new Date().getTime()
                    && (card.is_auto_start() || !getArguments().getBoolean("isSpend", true))) {
                    ToastUtils.show("该卡有效期为"
                        + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_from()))
                        + "至"
                        + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_to())));
                }
            }

            RxBus.getBus().post(((PayWardrobeItem) mAdapter.getItem(position)));
            dismiss();
        } else if (mAdapter.getItem(position) instanceof NoStudentCardItemItem) {

            if (!SerPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
                ToastUtils.show("您没有该场馆购卡权限");
            }
            Intent toCardType = new Intent(getActivity(), ChooseCardTypeActivity.class);
            startActivityForResult(toCardType, 10);
        }

        return true;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK) {
            if (requestCode == 10) {
                buyCard(getContext(), (CardTpl) data.getParcelableExtra(Configs.EXTRA_CARD_TYPE), this);
            }
        }
    }

    public void buyCard(final Context context, final CardTpl card_tpl, final Fragment f) {
        Intent it = new Intent(context, BuyCardActivity.class);
        it.putExtra(Configs.EXTRA_CARD_TYPE, card_tpl);
        it.putExtra(Configs.EXTRA_STUDENT_ID, getArguments().getString("s"));
        context.startActivity(it);
    }
}
