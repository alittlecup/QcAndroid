package cn.qingchengfit.staffkit.views.card.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.cardtype.ChooseCardTypeActivity;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.utils.IntentUtils;

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
 * Created by Paper on 16/3/23 2016.
 * <p/>
 * 返回值 String 卡类型名称 id
 * 返回值 String 卡状态
 */
public class FilterFragment extends BaseFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.card_name) CommonInputView cardName;
    @BindView(R.id.card_status) CommonInputView cardStatus;

    private String choooseCardtpl = "0";
    private int choooseCardStatus = 0;
    private String chooseCardtplName;
    private CoachService coachService;
    private Brand brand;

    public static FilterFragment newInstance(String cardtplid, String chooseCardtplName, int cardstatus, CoachService coachService,
        Brand brand) {

        Bundle args = new Bundle();
        args.putString("id", cardtplid);
        args.putString("name", chooseCardtplName);
        args.putInt("status", cardstatus);
        args.putParcelable("brandid", brand);
        args.putParcelable("gym", coachService);
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_filter_student, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            chooseCardtplName = getArguments().getString("name");
            choooseCardtpl = getArguments().getString("id");
            choooseCardStatus = getArguments().getInt("status");
            brand = getArguments().getParcelable("brandid");
            coachService = getArguments().getParcelable("gym");
            if (!TextUtils.isEmpty(chooseCardtplName)) cardName.setContent(chooseCardtplName);
            cardStatus.setContent(getResources().getStringArray(R.array.card_status)[choooseCardStatus]);
        }
        toolbar.setNavigationIcon(R.drawable.ic_cross_blace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.card_name, R.id.card_status, R.id.comfirm, R.id.clear_option }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_name:    //筛选卡
                Intent toChoose = new Intent(getActivity(), ChooseCardTypeActivity.class);
                toChoose.putExtra(Configs.EXTRA_BRAND, brand);
                toChoose.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                startActivityForResult(toChoose, 1);
                break;
            case R.id.card_status:
                BottomSheetListDialogFragment.start(this, 2, getResources().getStringArray(R.array.card_status));
                break;
            case R.id.comfirm://确认返回

                getActivity().setResult(Activity.RESULT_OK,
                    IntentUtils.instanceStringsIntent(choooseCardtpl, choooseCardStatus + "", chooseCardtplName));
                getActivity().finish();
                break;
            case R.id.clear_option:
                cardName.setContent("全部卡");
                cardStatus.setContent("全部状态");
                choooseCardtpl = "";
                choooseCardStatus = 0;
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {//卡状态
                choooseCardStatus = Integer.parseInt(IntentUtils.getIntentString(data));
                cardStatus.setContent(getResources().getStringArray(R.array.card_status)[choooseCardStatus]);
            } else if (requestCode == 1) { //卡名称
                CardTpl card_tpl = data.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
                choooseCardtpl = card_tpl.getId();
                chooseCardtplName = card_tpl.getName();
                cardName.setContent(card_tpl.getName());
            }
        }
    }

    @Override public String getFragmentName() {
        return FilterFragment.class.getName();
    }
}
