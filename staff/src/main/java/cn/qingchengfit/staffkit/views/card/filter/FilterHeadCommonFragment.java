package cn.qingchengfit.staffkit.views.card.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.FilterCardBean;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.CardFilterLeftAdapter;
import cn.qingchengfit.staffkit.views.adapter.CardFilterAdapter;
import cn.qingchengfit.staffkit.views.card.cardlist.RealCardListPresenter;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/2/20.
 */

public class FilterHeadCommonFragment extends BaseFragment
    implements View.OnClickListener, FilterHeadPresenter.OnFilterConditionListener, CardFilterAdapter.OnRightClickListener,
    CardFilterLeftAdapter.OnLeftClickListener {

    @BindView(R.id.ll_card_type_filter) public LinearLayout cardTypeFilter;
    @BindView(R.id.ll_card_state_filter) public LinearLayout cardStateFilter;
    @Inject public FilterHeadPresenter filterHeadPresenter;
    public boolean isBalance = false;
    @BindView(R.id.text_card_type_filter) TextView textCardtype;
    @BindView(R.id.text_card_status_filter) TextView textCardStatus;
    @BindView(R.id.image_type_arrow) ImageView imageTypeArrow;
    @BindView(R.id.image_state_arrow) ImageView imageStateArrow;
    @BindColor(R.color.qc_allotsale_green) int colorGreen;
    @BindColor(R.color.qc_text_black) int colorGrey;
    @Inject RealCardListPresenter realCardListPresenter;
    @Inject RestRepository restRepository;
    private TextView textNormal;
    private TextView textDefault;
    private TextView textLeaving;
    private TextView textOutOfDate;
    private TextView textStop;
    private ImageView imgConfirmDefault, imgConfirmNormal, imgConfirmLeaving, imgConfirmStop, imgOutOfdate;
    private PopupWindow mPopupWindow;
    private PopupWindow mRightPopupWindow;
    private View view;
    private ListView leftListView;
    private ListView rightListView;
    private RelativeLayout llCardStop;
    private CardFilterAdapter rightAdapter;
    private CardFilterLeftAdapter leftAdapter;
    private List<FilterCardBean> parentList = new ArrayList<>();
    private List<CardTpl> childList = new ArrayList<>();
    private int cardStatus = 0;
    private String card_tpl;
    private int cardTplType;
    private int leftLastPosition = -1;
    private int parentType = -1;
    private int parentPosition;
    private int childPosition;

    public void setView(View view) {
        this.view = view;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            try {
                throw new Exception("view is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        unbinder = ButterKnife.bind(this, view);
        if (container != null) {
            container.removeView(view);
        }
        View pupView = LayoutInflater.from(getContext()).inflate(R.layout.layout_filter_drop, null);
        View rightPopupView = LayoutInflater.from(getContext()).inflate(R.layout.layout_filter_right, null);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(pupView, ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(300.0f, getResources()), true);

            leftListView = (ListView) pupView.findViewById(R.id.list_card_filter_parent);
            rightListView = (ListView) pupView.findViewById(R.id.list_card_filter_child);
            initPopWindow(mPopupWindow);
        }

        if (mRightPopupWindow == null) {
            mRightPopupWindow =
                new PopupWindow(rightPopupView, ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(160.0f, getResources()), true);

            textDefault = (TextView) rightPopupView.findViewById(R.id.filter_default);
            textNormal = (TextView) rightPopupView.findViewById(R.id.filter_normal);
            textLeaving = (TextView) rightPopupView.findViewById(R.id.filter_leaving);
            textOutOfDate = (TextView) rightPopupView.findViewById(R.id.out_of_date);
            textStop = (TextView) rightPopupView.findViewById(R.id.filter_stop);
            imgConfirmDefault = (ImageView) rightPopupView.findViewById(R.id.image_filter_confirm_default);
            imgConfirmLeaving = (ImageView) rightPopupView.findViewById(R.id.image_filter_confirm_leaving);
            imgOutOfdate = (ImageView) rightPopupView.findViewById(R.id.image_out_of_date);
            imgConfirmNormal = (ImageView) rightPopupView.findViewById(R.id.image_filter_confirm_normal);
            imgConfirmStop = (ImageView) rightPopupView.findViewById(R.id.image_filter_confirm_stop);
            llCardStop = (RelativeLayout) rightPopupView.findViewById(R.id.ll_card_filter_stop);

            textDefault.setOnClickListener(this);
            textNormal.setOnClickListener(this);
            textLeaving.setOnClickListener(this);
            textStop.setOnClickListener(this);
            textOutOfDate.setOnClickListener(this);

            llCardStop.setVisibility(isBalance ? View.GONE : View.VISIBLE);
            initPopWindow(mRightPopupWindow);
        }
        initView();
        return view;
    }

    private void initView() {
        filterHeadPresenter.setOnFilterConditionListener(this);
        leftAdapter = new CardFilterLeftAdapter(getContext());
        rightAdapter = new CardFilterAdapter(getContext());
        cardStateFilter.setOnClickListener(this);
        cardTypeFilter.setOnClickListener(this);
        cardStateFilter.setTag(false);
        cardTypeFilter.setTag(false);
        refreshView();
        rightAdapter.setOnRightClickListener(this);
        leftAdapter.setOnLeftClickListener(this);
    }

    public void initDataList() {
        childList = filterHeadPresenter.getFilterCondition(parentList.get(0).type);
        rightAdapter.setDataList(childList);
        rightListView.setAdapter(rightAdapter);
        textDefault.setTextColor(ContextCompat.getColor(getContext(), R.color.qc_allotsale_green));
        imgConfirmDefault.setVisibility(View.VISIBLE);
    }

    public void initPopWindow(PopupWindow mPopupWindow) {
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                dismissPopupWindow();
                cardStateFilter.setTag(false);
                cardTypeFilter.setTag(false);
                refreshView();
            }
        });
    }

    @Override public String getFragmentName() {
        return null;
    }

    public void refreshView() {
        cardStateFilter.setClickable(true);
        cardTypeFilter.setClickable(true);
        if ((boolean) (cardStateFilter.getTag())) {
            textCardStatus.setTextColor(colorGreen);
            imageStateArrow.setImageResource(R.drawable.vector_arrow_up_green);
        } else {
            if (cardStatus > 0) {
                textCardtype.setTextColor(colorGreen);
                imageStateArrow.setImageResource(R.drawable.vector_arrow_down_green);
            } else {
                textCardStatus.setTextColor(colorGrey);
                imageStateArrow.setImageResource(R.drawable.vector_arrow_down_grey);
            }
        }

        if ((boolean) (cardTypeFilter.getTag())) {
            textCardtype.setTextColor(colorGreen);
            imageTypeArrow.setImageResource(R.drawable.vector_arrow_up_green);
        } else {
            if (!StringUtils.isEmpty(card_tpl) || cardTplType > 0) {
                textCardtype.setTextColor(colorGreen);
                imageTypeArrow.setImageResource(R.drawable.vector_arrow_down_green);
            } else {
                textCardtype.setTextColor(colorGrey);
                imageTypeArrow.setImageResource(R.drawable.vector_arrow_down_grey);
            }
        }
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_card_state_filter:
                cardStateFilter.setTag(true);
                showAsDropDown(mRightPopupWindow, view);
                break;
            case R.id.ll_card_type_filter:
                cardTypeFilter.setTag(true);
                showAsDropDown(mPopupWindow, view);
                break;
            case R.id.filter_default:
                cardStatus = 0;
                resetImgConfirm();
                ((TextView) view).setTextColor(colorGreen);
                imgConfirmDefault.setVisibility(View.VISIBLE);
                refreshData(card_tpl, cardTplType, cardStatus);
                textCardStatus.setText("默认");

                dissmissRight();
                break;
            case R.id.filter_normal:
                cardStatus = 1;
                resetImgConfirm();
                ((TextView) view).setTextColor(colorGreen);
                imgConfirmNormal.setVisibility(View.VISIBLE);
                refreshData(card_tpl, cardTplType, cardStatus);
                textCardStatus.setText("正常");

                dissmissRight();
                break;
            case R.id.filter_leaving:
                cardStatus = 2;
                resetImgConfirm();
                ((TextView) view).setTextColor(colorGreen);
                imgConfirmLeaving.setVisibility(View.VISIBLE);
                refreshData(card_tpl, cardTplType, cardStatus);
                textCardStatus.setText("请假中");
                dissmissRight();
                break;
            case R.id.filter_stop:
                cardStatus = 3;
                resetImgConfirm();
                ((TextView) view).setTextColor(colorGreen);
                imgConfirmStop.setVisibility(View.VISIBLE);
                refreshData(card_tpl, cardTplType, cardStatus);
                textCardStatus.setText("停卡");
                textCardStatus.setTextColor(colorGreen);
                dissmissRight();
                break;
            case R.id.out_of_date:
                cardStatus = 4;
                resetImgConfirm();
                ((TextView) view).setTextColor(colorGreen);
                imgOutOfdate.setVisibility(View.VISIBLE);
                refreshData(card_tpl, cardTplType, cardStatus);
                textCardStatus.setText("已过期");
                textCardStatus.setTextColor(colorGreen);
                dissmissRight();
                break;
            case R.id.tv_student_filter_confirm:
                onConfirm();
                break;
            case R.id.tv_student_filter_reset:
                onReset();
                break;
        }
    }

    private void dissmissRight() {
        if (mRightPopupWindow.isShowing()) {
            mRightPopupWindow.dismiss();
        }
    }

    public void onConfirm() {

    }

    public void onReset() {

    }

    private void resetImgConfirm() {
        textDefault.setTextColor(colorGrey);
        textNormal.setTextColor(colorGrey);
        textLeaving.setTextColor(colorGrey);
        textStop.setTextColor(colorGrey);
        textOutOfDate.setTextColor(colorGrey);

        imgConfirmDefault.setVisibility(View.GONE);
        imgConfirmNormal.setVisibility(View.GONE);
        imgConfirmLeaving.setVisibility(View.GONE);
        imgConfirmStop.setVisibility(View.GONE);
        imgOutOfdate.setVisibility(View.GONE);
    }

    public void showAsDropDown(PopupWindow mPopupWindow, View view) {
    }

    public void dismissPopupWindow() {

    }

    @Override public void onLoadFinish() {
        if (parentList.size() > 1) {
            parentList.clear();
        }
        parentList.addAll(filterHeadPresenter.getParentList());
        initDataList();
        leftAdapter.setDataList(parentList);
        leftListView.setAdapter(leftAdapter);
    }

    public void resetChildView() {
        for (CardTpl cardTpl : childList) {
            cardTpl.isChoosen = false;
        }
    }

    public void refreshData(String card_tpl_id, int cardTpl_Type, int cardStatus) {
    }

    @Override public void onRightFilter(int position) {
        resetChildView();
        card_tpl = childList.get(position).id;
        filterHeadPresenter.resetRight();
        card_tpl = childList.get(position).id;
        cardTplType = childList.get(position).tpl_type;
        childList.get(position).isChoosen = !childList.get(position).isChoosen;
        refreshData(card_tpl, cardTplType, cardStatus);
        if (parentType != -1) {
            filterHeadPresenter.changeChildList(parentType, childList);
        }
        rightAdapter.setDataList(childList);
        rightAdapter.notifyDataSetChanged();

        textCardtype.setText(parentList.get(parentPosition).name + "-" + childList.get(position).name);
        textCardtype.setTextColor(colorGreen);
        mPopupWindow.dismiss();
        mRightPopupWindow.dismiss();
        dismissPopupWindow();
    }

    @Override public void onSelected(int position) {
        parentPosition = position;
        childList = filterHeadPresenter.getFilterCondition(parentList.get(position).type);
        parentType = parentList.get(position).type;
        rightAdapter.setDataList(childList);
        rightAdapter.notifyDataSetChanged();
        for (FilterCardBean filterCardBean : parentList) {
            filterCardBean.isChoosen = false;
        }
        parentList.get(position).isChoosen = true;
        leftAdapter.setDataList(parentList);
        leftAdapter.notifyDataSetChanged();
        leftLastPosition = position;
    }
}
